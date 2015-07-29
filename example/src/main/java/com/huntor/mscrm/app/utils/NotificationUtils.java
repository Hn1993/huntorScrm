package com.huntor.mscrm.app.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.*;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiFans;
import com.huntor.mscrm.app.provider.MSCRMContract;
import com.huntor.mscrm.app.provider.api.ApiFansRecordDb;
import com.huntor.mscrm.app.ui.ChatActivity;
import com.huntor.mscrm.app.ui.MsgCenterActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;
import java.util.UUID;

/**
 * 通知消息的工具类
 *
 * @author C
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class NotificationUtils {

    private final String TAG = "NotificationUtils";
    private static NotificationUtils mNotificationUtils;

    private Context mContext; // 上下文
    private NotificationManager mNotificationManager;
    private RemoteViews mRemoteViews; // 通知的布局
    private static Handler mTimeHandler;

    /**
     * 单例，私有化构造方法
     *
     * @param context 上下文
     */
    private NotificationUtils(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_order);

    }

    /**
     * 工厂方法，用来获取NotificationUtils实例
     *
     * @param context 上下文
     * @return NotificationUtils实例
     */
    public static NotificationUtils getInstance(Context context) {
        if (mNotificationUtils == null) {
            mNotificationUtils = new NotificationUtils(context);
        }
        if (mTimeHandler == null) {
            mTimeHandler = new Handler();
        }
        return mNotificationUtils;
    }

    /**
     * 发送推送消息
     *
     * @param pushMessage
     */
    public void sendPush(PullMessageNote pushMessage) {
        Notification.Builder builder = new Notification.Builder(mContext);
        Date date = new Date(pushMessage.time);
        String dates = DateFormatUtils.format(date, DateFormatUtils.PATTERN_H_S);
        //时间
        mRemoteViews.setTextViewText(R.id.tv_time, dates);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(mContext.getResources().getString(R.string.notice_receive_new_msg));
        builder.setContent(mRemoteViews);
        String type = pushMessage.type;
        if (TextUtils.equals(type, Constant.CHAT_TYPE_TEXT)) {
            mRemoteViews.setTextViewText(R.id.tv_message_content, "站内信: " + pushMessage.content);
            builder.setTicker("站内信:" + pushMessage.content);
        } else if (TextUtils.equals(type, Constant.CHAT_TYPE_VOICE)) {
            mRemoteViews.setTextViewText(R.id.tv_message_content, "站内信" + mContext.getResources().getString(R.string.notice_receive_msg_voice));
            builder.setTicker("站内信" + mContext.getResources().getString(R.string.notice_receive_img));
        } else if (TextUtils.equals(type, Constant.CHAT_TYPE_IMAGE)) {
            mRemoteViews.setTextViewText(R.id.tv_message_content, "站内信" + mContext.getResources().getString(R.string.notice_receive_msg_img));
            builder.setTicker("站内信" + mContext.getResources().getString(R.string.notice_receive_img));
        }
        /***
         * 是否消息通知声音
         */
        builder.setDefaults(Notification.DEFAULT_SOUND);
        /**
         * 是否消息震动
         */
        builder.setVibrate(new long[]{200, 400, 200, 400});
        builder.setAutoCancel(true);// 点击完之后消失
        Intent intent = new Intent(mContext, MsgCenterActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constant.CHAT_INTENT_PUSH_MESSAGE, pushMessage);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pIntent);
        mNotificationManager.notify(pushMessage.fromUser, builder.getNotification());
    }

    /**
     * 发送推送消息
     *
     * @param pushMessage
     */
    public void sendPush(MessageRecordModel pushMessage, FansRecordModel fansRecordModel) {
        Notification.Builder builder = new Notification.Builder(mContext);

        Date date = new Date(pushMessage.timestamp);
        String dates = DateFormatUtils.format(date, DateFormatUtils.PATTERN_H_S);
        //时间
        mRemoteViews.setTextViewText(R.id.tv_time, dates);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(mContext.getResources().getString(R.string.notice_receive_new_msg));
        builder.setContent(mRemoteViews);

        String type = pushMessage.type;
        if (TextUtils.equals(type, Constant.CHAT_TYPE_TEXT)) {
            mRemoteViews.setTextViewText(R.id.tv_message_content, fansRecordModel.nickName + ": " + pushMessage.content);
            builder.setTicker(fansRecordModel.nickName + ":" + pushMessage.content);
        } else if (TextUtils.equals(type, Constant.CHAT_TYPE_VOICE)) {
            mRemoteViews.setTextViewText(R.id.tv_message_content, fansRecordModel.nickName + mContext.getResources().getString(R.string.notice_receive_msg_voice));
            builder.setTicker(fansRecordModel.nickName + mContext.getResources().getString(R.string.notice_receive_img));
        } else if (TextUtils.equals(type, Constant.CHAT_TYPE_IMAGE)) {
            mRemoteViews.setTextViewText(R.id.tv_message_content, fansRecordModel.nickName + mContext.getResources().getString(R.string.notice_receive_msg_img));
            builder.setTicker(fansRecordModel.nickName + mContext.getResources().getString(R.string.notice_receive_img));
        }
        /***
         * 是否消息通知声音
         */
        builder.setDefaults(Notification.DEFAULT_SOUND);
        /**
         * 是否消息震动
         */
        builder.setVibrate(new long[]{200, 400, 200, 400});
        builder.setAutoCancel(true);// 点击完之后消失
        Intent intent = new Intent(mContext, ChatActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Constant.CHAT_INTENT_PUSH_MESSAGE, pushMessage);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pIntent);
        mNotificationManager.notify(pushMessage.fid, builder.getNotification());
    }

    /**
     * 推送消息
     *
     * @param pushMessage 推送消息消息
     */
    public void sendPushNotification(MessageRecordModel pushMessage) {
        Log.e(TAG, "" + pushMessage.toString());
        int fid = pushMessage.fid;
        Log.e(TAG, "fid = " + fid);
        FansRecordModel fanModel = ApiFansRecordDb.getFansInfoById(mContext, fid);
        if (fanModel == null) {
            getFansDetail(fid, true, pushMessage);
        } else {
            sendPush(pushMessage, fanModel);
        }
    }


    public void cancleById(int id) {
        mNotificationManager.cancel(id);
    }

    public void clearAll() {
        mNotificationManager.cancelAll();
    }

    /**
     * 获取粉丝详情
     *
     * @param fan_id
     * @param isSave
     */
    private void getFansDetail(final int fan_id, final boolean isSave, final MessageRecordModel pushMessage) {
        HttpRequestController.getFansInfo(mContext, fan_id,
                new HttpResponseListener<ApiFans.ApiFansResponse>() {
                    @Override
                    public void onResult(ApiFans.ApiFansResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.fanInfo = " + response.fanInfo);
                            FansRecordModel fanModel = new FansRecordModel();
                            if (response.fanInfo != null) {
                                fanModel.accountId = fan_id;
                                fanModel.eid = PreferenceUtils.getInt(mContext, Constant.PREFERENCE_EMP_ID, -1);
                                Log.e(TAG, "fanModel.accountId = " + fanModel.accountId);
                                fanModel.avatar = response.fanInfo.avatar;
                                fanModel.realName = response.fanInfo.realName;
                                fanModel.nickName = response.fanInfo.nickName;
                                fanModel.province = response.fanInfo.province;
                                fanModel.city = response.fanInfo.city;
                                fanModel.followStatus = response.fanInfo.followStatus;
                                fanModel.gender = response.fanInfo.gender;
                                fanModel.interactionTimes = response.fanInfo.interactionTimes;
                                fanModel.lastInteractionTime = response.fanInfo.lastInteractionTime;
                                fanModel.subscribeTime = response.fanInfo.subscribeTime;
                                if (isSave) {
                                    Uri fansRecordModelUrl = new ApiFansRecordDb(mContext).insert(fanModel);
                                    if (fansRecordModelUrl == null) {
                                        //TODO 该粉丝插入不成功
                                    }
                                }

                                FansRecordModel frm = ApiFansRecordDb.getFansInfoById(mContext, fanModel.accountId);
                                Log.e(TAG, "fanModel" + frm.toString());
                                sendPush(pushMessage, frm);
                            }
                        } else {
                            Utils.toast(mContext, response.getRetInfo() + "");
                        }

                    }
                });
    }

    private void wakeLock() {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);
        wakeLock.acquire();
        Log.e(TAG, "===============wake lock");
        mTimeHandler.postDelayed(new Runnable() {
            public void run() {
                wakeLock.release();
            }
        }, 5 * 1000);
    }

}
