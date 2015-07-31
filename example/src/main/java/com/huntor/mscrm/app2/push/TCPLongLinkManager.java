package com.huntor.mscrm.app2.push;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.*;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.api.ApiFans;
import com.huntor.mscrm.app2.provider.api.ApiMessageRecordDb;
import com.huntor.mscrm.app2.utils.*;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;


public class TCPLongLinkManager {
    private Context context; // 上下文
    private final String TAG = "TCPLongLinkManager";
    private Socket mSocket;
    private static TCPLongLinkManager mTCPLongLinkManager; // 外部获取到的该类实例
    private PushMessageManager pmm;

    /**
     * 私有化构造方法
     *
     * @param context 上下文
     */
    private TCPLongLinkManager(Context context) {
        this.context = context;
        pmm = PushMessageManager.getInstance(context);
    }

    /**
     * 外部获取该类实例的方法
     *
     * @param context 上下文
     * @return 该类的实例
     */
    public static TCPLongLinkManager getInstance(Context context) {
        if (mTCPLongLinkManager == null) {
            mTCPLongLinkManager = new TCPLongLinkManager(context);
        }
        return mTCPLongLinkManager;
    }

    /**
     * 开始连接
     */
    public void startConnect() {//192.168.1.179:9092
        if (mSocket == null) {
            Log.e(TAG, "准备连接。。。");
            try {//http://chat.socket.io
                IO.Options opts = new IO.Options();
                opts.forceNew = false;

//                mSocket = IO.socket(Constant.SOCKET_TCP_LONG_HOST_URL, opts);
                String chatter_url_main = Constant.SOCKET_TCP_LONG_HOST_URL;
                String chatter_url = PreferenceUtils.getString(context, "chatter_url", "");
                chatter_url_main = !TextUtils.isEmpty(chatter_url) ? chatter_url : chatter_url_main;
                MyLogger.e(TAG, "chatter_url_main: " + chatter_url_main);
                mSocket = IO.socket(chatter_url_main, opts);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            mSocket.on(Socket.EVENT_RECONNECT, onReConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisConnect);
            mSocket.on(Socket.EVENT_MESSAGE, onMessage);
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("chat", chat);
            mSocket.on("internalmsg", internalmsg);
            mSocket.on("shake_event", shake_event);
            mSocket.connect();

        }
    }

    /**
     * 停止连接
     */
    public void stopConnect() {
        if (mSocket == null) {
            //startConnect();
            return;
        }
        Log.e(TAG, "" + new Date().getTime());
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_DISCONNECT, onDisConnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off(Socket.EVENT_MESSAGE, onMessage);
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off("chat", chat);
            mSocket.off("internalmsg", internalmsg);
            Log.e(TAG, "" + new Date().getTime());
        }
        mSocket.close();
        mSocket = null;
    }

    private Emitter.Listener onReConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "正在重连==" + args[0].toString());
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "连接错误==" + args[0].toString());
        }
    };
    private Emitter.Listener onDisConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mSocket != null) {
                Log.e(TAG, "连接断开" + mSocket.connected());
            }
        }
    };

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "===" + args.toString());
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, String.format("连接%s成功！准备登陆!", mSocket.id()));
            Login login = new Login();
            login.id = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, -1);
            login.secret = "secret";
            Log.e(TAG, "" + login.toString());
//            login(login);

            JSONObject jo = new JSONObject();
            try {
                jo.put("id", login.id);
                jo.put("secret", login.secret);
            } catch (JSONException e) {
            }
            Log.e("Login", login.toString());
            if (mSocket.connected()) {
                mSocket.emit("login", jo);
            }
        }
    };

    /****
     * 收到的站内信信息
     */
    private Emitter.Listener internalmsg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "执行站内信消息");
            if (args != null && args.length > 0) {
                JSONObject data = (JSONObject) args[0];
                Gson gson = new Gson();
                PullMessageNote sm = gson.fromJson(data.toString(), PullMessageNote.class);
                Log.e(TAG, "站内信消息=====" + data.toString());
                Log.e(TAG, "站内信消息=====" + sm.toString());
                pmm.sendPushMessage(sm, "internalmsg");
                ++Constant.notecount;
            } else {
                Log.e(TAG, "receive message====null");
            }
        }
    };
    /****
     * 收到的聊天信息
     */
    private Emitter.Listener chat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "receive message=====");
            if (args != null && args.length > 0) {
                JSONObject data = (JSONObject) args[0];
                Gson gson = new Gson();
                MessageRecordModel sm = gson.fromJson(args[0].toString(), MessageRecordModel.class);
                Log.e(TAG, "receive message=====" + sm.toString());
                pmm.sendPushMessage(sm, "chat");
            } else {
                Log.e(TAG, "receive message====null");
            }
        }
    };

    /****
     * 收到的摇一摇信息
     */
    private Emitter.Listener shake_event = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.e(TAG, "receive message=====");
            if (args != null && args.length > 0) {
                JSONObject data = (JSONObject) args[0];
                Gson gson = new Gson();
                ShakeModle sm = gson.fromJson(args[0].toString(), ShakeModle.class);
                Log.e(TAG, "receive message=====" + sm.toString());

                ApiFans.ApiFansParams params = new ApiFans.ApiFansParams(sm.fanId);

                ApiFans.ApiFansResponse response = new ApiFans(context,params).getHttpResponse();
                if(response.getRetCode() != BaseResponse.RET_CACHE_STATUS_OK){
                    handler.sendEmptyMessage(2);
                }
                pmm.sendPushMessage(sm, "shake_event");
                ++Constant.notecount;
            } else {
                Log.e(TAG, "receive message====null");
            }
        }
    };

    /**
     * 发送消息
     *
     * @param type chat or context
     * @param obj  要发送的消息对象
     * @param rck  发送之后的回调
     */
    public void sendMessage(String type, Object obj, final PushMessageManager.Rck rck) {
        if (mSocket == null) {
            SendMessage sm = (SendMessage) obj;
            rck.onResult(false, "socket is null !", sm.recordId);
            return;
        }
        if (TextUtils.equals(type, "chat")) {
            final SendMessage sendMessage = (SendMessage) obj;
            JSONObject jo = new JSONObject();
            try {
                jo.put("type", sendMessage.type);
                jo.put("content", sendMessage.content);
                jo.put("groupId", sendMessage.groupId);
                jo.put("timestamp", sendMessage.timestamp);
                jo.put("eid", sendMessage.eid);
                jo.put("fid", sendMessage.fid);
                jo.put("socialId", sendMessage.socialId);
                jo.put("recordId", sendMessage.recordId);
            } catch (JSONException e) {
            }
            boolean status = mSocket.connected();
            Log.e(TAG, "连接状态=" + status);
            if (status) {
                mSocket.emit("chat", jo, new Ack() {
                    @Override
                    public void call(Object... objects) {
                        //发送回执
                        if (objects != null && objects.length >= 2) {
                            Log.e(TAG, "发送消息回调" + objects.length + "recordId=" + objects[0].toString() + " 结果=" + objects[1].toString());
//                            try {
//                                MobclickAgent.reportError(context, "发送消息回调" + objects.length + "recordId=" + objects[0].toString() + " 结果=" + objects[1].toString());
//                                String txtContent = FileService.read(File.separator + "cash" + File.separator + "msg_result.txt");
//                                MyLogger.e(TAG, "txtContent=" + txtContent);
//                                String log = txtContent + "\n\n" + "发送消息回调" + objects.length + "recordId=" + objects[0].toString() + " 结果=" + objects[1].toString();
//                                MyLogger.e(TAG, "LOG=" + log);
//                                WriteJson2SD.writeJson(log, DateFormatUtils.getTimeStamp(), "msg_result");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                            Gson gson = new Gson();
                            Response response = gson.fromJson(objects[1].toString(), Response.class);
                            int id = Integer.parseInt(objects[0].toString());
                            String code = response.code;
                            String msg = response.msg;
                            if (msg != null) {
                                if (code.equals("-1") && msg.contains("system error")) {
                                    Utils.toast(context, "微信接口调用失败");
                                }
                            }
                            onResult(response, rck, id);
                        }
                    }
                });

            } else {  //连接失败导致消息发送失败的处理
                //发送消息失败 更改消息状态
                ApiMessageRecordDb.updateSendStatus(context, sendMessage.recordId, 0);
                Utils.toast(context, "网络异常");
                rck.onResult(false, "send fail !", sendMessage.recordId);
            }
        } else if (TextUtils.equals("context", type)) {
            MessageContext c = (MessageContext) obj;
            JSONObject jo = new JSONObject();
            try {
                jo.put("eid", c.eid);
                jo.put("fid", c.fid);
                jo.put("last_mid", c.last_mid);
            } catch (JSONException e) {
            }
            mSocket.emit("context", jo);
        }
    }

    private void onResult(Response response, PushMessageManager.Rck rck, int id) {
        MyLogger.e(TAG, "onResult=" + response.toString());
        if (TextUtils.equals(response.code, "0")) {
            ApiMessageRecordDb.updateSendStatus(context, id, 1);
            rck.onResult(true, "send success !", id);
        } else if (response.code.equals("99020")) {
            ApiMessageRecordDb.updateSendStatus(context, id, 0);
            rck.onResult(false, "socket is null !", id);
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        } else {
            ApiMessageRecordDb.updateSendStatus(context, id, 0);
            rck.onResult(false, "socket is null !", id);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Utils.toast(context, "该粉丝已经取消关注，看来服务要更用心哦！");
                    break;
                case 2:
                    Utils.toast(context, "获取摇一摇粉丝信息失败！");
                    break;

            }
        }
    };
}
