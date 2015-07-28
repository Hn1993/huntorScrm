package com.huntor.scrm.push;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.huntor.scrm.model.MessageContext;
import com.huntor.scrm.model.MessageRecordModel;
import com.huntor.scrm.model.PullMessageNote;
import com.huntor.scrm.model.SendMessage;
import com.huntor.scrm.provider.api.ApiFansRecordDb;
import com.huntor.scrm.provider.api.ApiMessageRecordDb;
import com.huntor.scrm.provider.api.ApiPullMessageNoteDb;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.MyLogger;
import com.huntor.scrm.utils.NotificationUtils;
import com.huntor.scrm.utils.PreferenceUtils;


/**
 * 该类用来解析WX推送的消息
 *
 * @author C
 */
public class PushMessageManager {

    /**
     * 收到推送消息后的回调
     */
    public interface OnReceivedPushMessageListener {
        /**
         * 收到推送消息的回调
         *
         * @param pushMessage
         */
        public void onReceivedFansMessage(Object pushMessage);

    }

    /**
     * 收到推送消息后的回调
     */
    public interface OnPullMessageNoteListener {
        /**
         * 收到推送消息的回调
         *
         * @param pushMessage
         */
        public void OnPullMessageNote(Object pushMessage);
    }

    public interface Rck {
        /**
         * 发送消息的回调接口
         */
        public void onResult(boolean status, String errorInfo, int id);
    }

    private static final String TAG = "PushMessageManager";

    private static final int MSG_PARSE_PUSH_MESSAGE = 1; // 解析推送消息
    private static final int MSG_PARSE_SEND_MESSAGE = 2; // 发送消息

    private static final List<OnReceivedPushMessageListener> mPushListeners = new ArrayList<OnReceivedPushMessageListener>();
    private static final List<OnPullMessageNoteListener> mlisteners = new ArrayList<OnPullMessageNoteListener>();

    private static PushMessageManager mPushMessageManager;
    private Context mContext; // 上下文
    private PushMessageHandler mPushMessageHandler; // 处理服务端推送消息的Handler

    private NotificationUtils mNotificationUtils; // 通知的工具类

    public int mFanId = 0;//当前交互的粉丝ID

    /**
     * 私有化构造方法，在构造方法中将PushMessageHandler绑定到另外一个线程的Looper上
     *
     * @param context 上下文
     */
    private PushMessageManager(Context context) {
        mContext = context;
        HandlerThread mHandlerThread = new HandlerThread("push_message_receiver",
                Thread.NORM_PRIORITY);
        mHandlerThread.start();
        mPushMessageHandler = new PushMessageHandler(mHandlerThread.getLooper());
        mNotificationUtils = NotificationUtils.getInstance(context);
    }

    /**
     * 提供一个静态的方法获取PushMessageManager类的实例
     *
     * @param context 上下文
     * @return PushMessageManager类的实例
     */
    public static PushMessageManager getInstance(Context context) {
        if (mPushMessageManager == null) {
            mPushMessageManager = new PushMessageManager(context);
        }
        return mPushMessageManager;
    }

    /**
     * 注册监听器，用来接收推送消息
     *
     * @param listener OnReceivedPushMessageListener
     */
    public void registerOnReceivedPushMessageListener(OnReceivedPushMessageListener listener) {
        mPushListeners.add(listener);
    }

    public void registerOnReceivedPushMessageListener(OnPullMessageNoteListener listener) {
        mlisteners.add(listener);
    }


    /**
     * 设置当前交互的粉丝Id
     *
     * @param fanId 粉丝Id  不在当前交互页面不用传粉丝Id
     */
    public void setCurrentFid(int fanId) {
        if (fanId != 0) {
            this.mFanId = fanId;
        }
    }

    /**
     * 注销接收推送消息的监听
     *
     * @param listener OnReceivedPushMessageListener
     */
    public void unregisterOnReceivedPushMessageListener(OnReceivedPushMessageListener listener) {
        mPushListeners.remove(listener);
        mFanId = 0;
    }

    public void unregisterOnReceivedPushMessageListener(OnPullMessageNoteListener listener) {
        mPushListeners.remove(listener);
    }

    /**
     * 解析推送的消息
     *
     * @param receiveMessage 推送过来的数据
     */
    public void sendPushMessage(Object receiveMessage, String type) {
        Message msg = mPushMessageHandler.obtainMessage(MSG_PARSE_PUSH_MESSAGE);
        msg.obj = receiveMessage;
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        msg.setData(bundle);
        mPushMessageHandler.sendMessageDelayed(msg, 500);
    }

    /**
     * 员工发送消息
     *
     * @param type        chat 员工发送的消息   context 导购粉丝上下文状态交互
     * @param sendMessage 发送消息的对象
     */
    public void sendEmpMessage(String type, Object sendMessage, Rck rck) {
        Message msg = mPushMessageHandler.obtainMessage(MSG_PARSE_SEND_MESSAGE);
        msg.obj = rck;
        Bundle b = new Bundle();
        b.putString("type", type);
        if (type.equals("chat")) {//员工发送的消息
            b.putSerializable("sendMessage", (SendMessage) sendMessage);
        } else if (type.equals("context")) {//导购粉丝上下文状态交互
            b.putSerializable("sendMessage", (MessageContext) sendMessage);
        }
        Log.e(TAG, "" + sendMessage);
        msg.setData(b);
        mPushMessageHandler.sendMessageDelayed(msg, 500);
    }

    /**
     * 解析推送的聊天消息，并把消息发送到监听者
     *
     * @param mrm
     */
    public void parsePushMessage(MessageRecordModel mrm) {
        Log.i(TAG, "pushMessage = " + mrm);
        if (mrm != null) {
            //所有接收到的推送消息都要保存到历史记录表里
            ApiMessageRecordDb amrd = new ApiMessageRecordDb(mContext);
            mrm.isRead = 0; //未读消息
            mrm.successOrFail = 1;  //消息接收成功
            mrm.sendOrReceive = 0; //发送还是接收消息  0  接收
            mrm.time = new Date().getTime();
            amrd.insert(mrm);
            /***
             * 每一次收到消息  都去本地更新会员与导购最后一次交互时间
             * 这样列表才能及时刷新页面  保持最后一次交互的粉丝  在交互过的粉丝列表最顶端
             */
            ApiFansRecordDb.updateFansLastInteractiveTime(mContext, mrm.fid);
            // 发送Notification
            if (mrm.fid != mFanId || mFanId == 0) { //如果是当前交互的粉丝消息就不弹出notify
                mNotificationUtils.sendPushNotification(mrm);
            }
            /**
             * 通知监听者
             */
            for (int i = 0; i < mPushListeners.size(); i++) {
                OnReceivedPushMessageListener listener = mPushListeners.get(i);
                if (listener != null) {
                    listener.onReceivedFansMessage(mrm);
                }
            }
        }
    }

    /**
     * 解析推送的站内信消息，并把消息发送到监听者
     *
     * @param note
     */
    public void parsePushMessage(PullMessageNote note) {
        Log.i(TAG, "pushMessage = " + note);
        if (note != null) {
            ApiPullMessageNoteDb noteDb = new ApiPullMessageNoteDb(mContext);
            note.time = new Date().getTime();
            noteDb.insert(note);
            mNotificationUtils.sendPush(note);
            /**
             * 通知监听者
             */
            for (int i = 0; i < mlisteners.size(); i++) {
                OnPullMessageNoteListener listener = mlisteners.get(i);
                if (listener != null) {
                    listener.OnPullMessageNote(note);

                }
            }
        }
    }

    /**
     * 解析发送的消息，并把消息发送到监听者
     *
     * @param type 1 文本  10  图文
     * @param obj
     */
    public void parseSendMessage(String type, Object obj, Rck rck) {
        Log.i(TAG, "sendMessage = " + obj);
        if (obj != null) {
            if (TextUtils.equals("chat", type)) {  //发送消息
                SendMessage sendMessage = (SendMessage) obj;
                //所有接收到的推送消息都要保存到历史记录表里
                ApiMessageRecordDb amrd = new ApiMessageRecordDb(mContext);
                MessageRecordModel mrm = new MessageRecordModel();
                mrm.msgId = sendMessage.recordId; //消息ID
                MyLogger.i(TAG, "sendMessage.content: " + sendMessage.content);
                if (sendMessage.type == Constant.CHAT_TYPE_VOICE) {
                    mrm.content = sendMessage.content.split("####")[0];
                } else {
                    mrm.content = sendMessage.content;
                }
                mrm.type = sendMessage.type;
                mrm.eid = sendMessage.eid;  //员工ID
                mrm.fid = sendMessage.fid; //粉丝Id
                mrm.timestamp = sendMessage.timestamp;
                mrm.time = new Date().getTime();
                mrm.isRead = 1; //未读消息 发送的消息状态都是已读
                mrm.successOrFail = 1;  //消息接收成功
                mrm.sendOrReceive = 1; //发送还是接收消息  0  接收
                amrd.insert(mrm);
                /***
                 * 每一次收到消息  都去本地更新会员与导购最后一次交互时间
                 * 这样列表才能及时刷新页面  保持最后一次交互的粉丝  在交互过的粉丝列表最顶端
                 */
                String tails = PreferenceUtils.getString(mContext, Constant.CHAT_TAILS, "");
                MyLogger.w(TAG, "tails = " + tails);

                if (sendMessage.type == Constant.CHAT_TYPE_VOICE) {
                    sendMessage.content = sendMessage.content.split("####")[1];
                }
                if (!TextUtils.isEmpty(tails)) {
                    if (sendMessage.type == Constant.CHAT_TYPE_TEXT) {
                        sendMessage.content = tails + ":" + sendMessage.content;
                    }
                }
                ApiFansRecordDb.updateFansLastInteractiveTime(mContext, sendMessage.fid);
            }
            TCPLongLinkManager.getInstance(mContext).sendMessage(type, obj, rck);
        }
    }

    /**
     * 自定义Handler，用来解析WX的推送消息
     */
    @SuppressLint("HandlerLeak")
    private class PushMessageHandler extends Handler {

        public PushMessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PARSE_PUSH_MESSAGE:
                    String type = msg.getData().getString("type");
                    if (type.equals("chat")) {
                        parsePushMessage((MessageRecordModel) msg.obj);
                    } else if (type.equals("internalmsg")) {
                        parsePushMessage((PullMessageNote) msg.obj);
                    }
                    break;
                case MSG_PARSE_SEND_MESSAGE:
                    Rck rck = (Rck) msg.obj;
                    Log.e(TAG, "handler========");
                    parseSendMessage(msg.getData().getString("type"), msg.getData().getSerializable("sendMessage"), rck);
                    break;
            }
        }
    }

}
