package com.huntor.mscrm.app.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.TrafficStats;
import android.net.Uri;
import android.util.Log;
import com.huntor.mscrm.app.model.MessageRecordModel;
import com.huntor.mscrm.app.provider.MSCRMContract;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 消息表的数据库接口
 */
public class ApiMessageRecordDb {
    private static final String TAG = "ApiMessageRecordDb";
    private Context mContext;

    public ApiMessageRecordDb(Context context) {
        mContext = context;
    }

    /**
     * 插入消息
     *
     * @param messages 消息列表
     * @return 插入数据的条数
     */
    public int bulkInsert(List<MessageRecordModel> messages) {
        /**
         * 删除除登录账户之外的数据
         */
        ContentResolver resolver = mContext.getContentResolver();
        for (int i = 0; i < messages.size(); i++) {
            resolver.delete(MSCRMContract.MessageRecord.CONTENT_URI, MSCRMContract.MessageRecord.MSGID
                    + "=?", new String[]{messages.get(i).msgId + ""});
        }

        if (messages == null || messages.size() < 1) {
            return -1;
        }
        if (Constant.DEBUG) {
            Log.i(TAG, "accounts.size() = " + messages.size());
        }
        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            ContentValues value = new ContentValues();
            MessageRecordModel message = messages.get(i);

            /**
             * 构造一个消息条目
             */
            value.put(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS, message.artificialStatus); //
            value.put(MSCRMContract.MessageRecord.MSGID, message.msgId); // 消息ID
            value.put(MSCRMContract.MessageRecord.TYPE, message.type); // 消息类型
            value.put(MSCRMContract.MessageRecord.CONTENT, message.content); // 消息内容
            value.put(MSCRMContract.MessageRecord.TIMESTAMP, message.timestamp); // 时间
            value.put(MSCRMContract.MessageRecord.TIME, message.time); // 时间
            value.put(MSCRMContract.MessageRecord.FID, message.fid); // 粉丝ID
            value.put(MSCRMContract.MessageRecord.EID, message.eid); // 员工ID
            value.put(MSCRMContract.MessageRecord.GROUP_ID, message.groupId); // 消息ID
            value.put(MSCRMContract.MessageRecord.ISREAD, message.isRead); // 已读未读
            value.put(MSCRMContract.MessageRecord.SEND_OR_RECEIVE, message.sendOrReceive); // 发送还是接收
            value.put(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL, message.successOrFail); // 发送还是接收


            values[i] = value;
        }
        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.MessageRecord.CONTENT_URI, values);
    }

    /**
     * 获取所有消息
     *
     * @param context 上下文
     * @return
     */
    public static List<MessageRecordModel> getMsgList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.MessageRecord.CONTENT_URI, null, null, null,
                null);
        List<MessageRecordModel> messages = new ArrayList<MessageRecordModel>();
        if (cursor != null) {
            MessageRecordModel message = null;
            while (cursor.moveToNext()) {
                message = new MessageRecordModel();
                message.artificialStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS));
                message.msgId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.MSGID));
                message.type = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.TYPE));
                message.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.CONTENT));
                message.timestamp = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIMESTAMP));
                message.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIME));
                message.fid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.FID));
                message.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.EID));
                message.groupId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.GROUP_ID));
                message.isRead = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ISREAD));
                message.sendOrReceive = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SEND_OR_RECEIVE));
                message.successOrFail = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL));

                messages.add(message);
            }
            cursor.close();
        }
        return messages;
    }

    /**
     * 获取所有未读消息
     *
     * @param context 上下文
     * @return
     */
    public static List<MessageRecordModel> getAllUnRead(Context context) {
        ContentResolver resolver = context.getContentResolver();
        int empId = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
        Cursor cursor = resolver.query(MSCRMContract.MessageRecord.CONTENT_URI, null, MSCRMContract.MessageRecord.ISREAD + "=? and " + MSCRMContract.MessageRecord.EID + "=?", new String[]{"0", empId + ""}, null);
        List<MessageRecordModel> messages = new ArrayList<MessageRecordModel>();
        if (cursor != null) {
            MessageRecordModel message = null;
            while (cursor.moveToNext()) {
                message = new MessageRecordModel();
                message.artificialStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS));
                message.msgId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.MSGID));
                message.type = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.TYPE));
                message.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.CONTENT));
                message.timestamp = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIMESTAMP));
                message.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIME));
                message.fid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.FID));
                message.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.EID));
                message.groupId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.GROUP_ID));
                message.isRead = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ISREAD));
                message.sendOrReceive = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SEND_OR_RECEIVE));
                message.successOrFail = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SEND_OR_RECEIVE));
                messages.add(message);
            }
            cursor.close();
        }
        return messages;
    }


    /**
     * 根据粉丝ID获取所有未读消息
     *
     * @param context 上下文
     * @param fId     粉丝ID
     * @return
     */
    public static List<MessageRecordModel> getUnReadByFansId(Context context, int fId) {
        ContentResolver resolver = context.getContentResolver();
        int empId = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
        Cursor cursor = resolver.query(MSCRMContract.MessageRecord.CONTENT_URI, null,
                MSCRMContract.MessageRecord.FID + "=? and " + MSCRMContract.MessageRecord.ISREAD + "=? and " + MSCRMContract.MessageRecord.EID + "=?", new String[]{"" + fId, "0", "" + empId}, "time desc");
        List<MessageRecordModel> messages = new ArrayList<MessageRecordModel>();
        if (cursor != null) {
            MessageRecordModel message = null;
            while (cursor.moveToNext()) {
                message = new MessageRecordModel();
                message.artificialStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS));
                message.msgId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.MSGID));
                message.type = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.TYPE));
                message.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.CONTENT));
                message.timestamp = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIMESTAMP));
                message.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIME));
                message.fid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.FID));
                message.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.EID));
                message.groupId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.GROUP_ID));
                message.isRead = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ISREAD));
                message.sendOrReceive = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SEND_OR_RECEIVE));
                message.successOrFail = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL));

                messages.add(message);
            }
            cursor.close();
        }

        return messages;
    }

    /**
     * 根据粉丝ID查询所有消息记录
     *
     * @param context  上下文
     * @param fId      粉丝ID
     * @param pageSize 查询页面大小
     * @param pageNum  页码
     * @return 按照倒叙排列   从小到大
     */
    public static List<MessageRecordModel> getMessage(Context context, int fId, int pageSize, int pageNum) {
        ContentResolver resolver = context.getContentResolver();
        int eid = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
        Cursor cursor = resolver.query(MSCRMContract.MessageRecord.CONTENT_URI, null,
                MSCRMContract.MessageRecord.FID + "=? and " + MSCRMContract.MessageRecord.EID + "=" + eid, new String[]{"" + fId}, "time desc limit " + pageSize + " offset " + (pageNum - 1) * pageSize);
        List<MessageRecordModel> messages = new ArrayList<MessageRecordModel>();
        if (cursor != null) {
            MessageRecordModel message = null;
            while (cursor.moveToNext()) {
                message = new MessageRecordModel();
                message.artificialStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS));
                message.msgId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.MSGID));
                message.type = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.TYPE));
                message.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.CONTENT));
                message.timestamp = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIMESTAMP));
                message.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIME));
                message.fid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.FID));
                message.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.EID));
                message.groupId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.GROUP_ID));
                message.isRead = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ISREAD));
                message.sendOrReceive = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SEND_OR_RECEIVE));
                message.successOrFail = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL));
                messages.add(message);
            }
            cursor.close();
        }
        Collections.sort(messages);
        //将所有未读消息设为已读
        updateReadStatus(context, fId);
        return messages;
    }

    /**
     * 根据粉丝ID查询最后一条消息记录
     *
     * @param context 上下文
     * @param fId     粉丝ID
     * @return 最后一条消息记录
     */
    public static MessageRecordModel getLastMessage(Context context, int fId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.MessageRecord.CONTENT_URI, null,
                MSCRMContract.MessageRecord.FID + "=? and " + MSCRMContract.MessageRecord.EID + " = ?",
                new String[]{"" + fId, "" + PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, -1)}, "time desc limit 1");
        MessageRecordModel message = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                message = new MessageRecordModel();
                message.artificialStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS));
                message.msgId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.MSGID));
                message.type = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.TYPE));
                message.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.CONTENT));
                message.timestamp = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIMESTAMP));
                message.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIME));
                message.fid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.FID));
                message.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.EID));
                message.groupId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.GROUP_ID));
                message.isRead = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ISREAD));
                message.sendOrReceive = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SEND_OR_RECEIVE));
                message.successOrFail = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL));
            }
            cursor.close();
        }
        return message;
    }

    /**
     * 查询GroupID
     *
     * @param context 上下文
     * @param fId     粉丝ID
     * @return 最后一条消息记录
     */
    public static int getGroupID(Context context, int fId) {
        int groupId = 0;
        ContentResolver resolver = context.getContentResolver();
        int eid = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, -1);
        Cursor cursor = resolver.query(MSCRMContract.MessageRecord.CONTENT_URI, null,
                MSCRMContract.MessageRecord.FID + "=? and " + MSCRMContract.MessageRecord.EID + "=" + eid, new String[]{"" + fId}, null);
        if (cursor != null) {
            MessageRecordModel message = null;
            while (cursor.moveToNext()) {
                message = new MessageRecordModel();
                message.artificialStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS));
                message.msgId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.MSGID));
                message.type = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.TYPE));
                message.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageRecord.CONTENT));
                message.timestamp = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIMESTAMP));
                message.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageRecord.TIME));
                message.fid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.FID));
                message.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.EID));
                message.groupId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.GROUP_ID));
                message.isRead = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.ISREAD));
                message.sendOrReceive = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SEND_OR_RECEIVE));
                message.successOrFail = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL));
                groupId = message.groupId;
                if (groupId > 0) {
                    break;
                }
            }
            cursor.close();
        }
        return groupId;
    }

    /**
     * 插入一条消息记录
     *
     * @param message 消息
     * @return 返回该数据插入后的uri
     */
    public Uri insert(MessageRecordModel message) {
        if (message == null) {
            return null;
        }

        /**
         * 消息存在则不插入
         */
        if (isMessageExists(message.msgId)) {
            return null;
        }

        /**
         * 构造一个消息条目
         */
        ContentValues value = new ContentValues();
        value.put(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS, message.artificialStatus); // 消息ID
        value.put(MSCRMContract.MessageRecord.MSGID, message.msgId); // 消息ID
        value.put(MSCRMContract.MessageRecord.TYPE, message.type); // 消息类型
        value.put(MSCRMContract.MessageRecord.CONTENT, message.content); // 消息内容
        value.put(MSCRMContract.MessageRecord.TIMESTAMP, message.timestamp); // 消息时间
        value.put(MSCRMContract.MessageRecord.TIME, message.time); // 时间
        value.put(MSCRMContract.MessageRecord.FID, message.fid); // 粉丝ID
        value.put(MSCRMContract.MessageRecord.EID, message.eid); // 员工ID
        value.put(MSCRMContract.MessageRecord.GROUP_ID, message.groupId); // 消息ID
        value.put(MSCRMContract.MessageRecord.ISREAD, message.isRead); // 已读未读
        value.put(MSCRMContract.MessageRecord.SEND_OR_RECEIVE, message.sendOrReceive); // 发送还是接收
        value.put(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL, message.successOrFail); //发送状态 成功or失败

        ContentResolver resolver = mContext.getContentResolver();
        return resolver.insert(MSCRMContract.MessageRecord.CONTENT_URI, value);
    }

    /**
     * 更新一条消息记录 状态变为  已读
     *
     * @param context
     * @param fanId   粉丝ID
     */
    public static int updateReadStatus(Context context, int fanId) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues updateValues = new ContentValues();
        updateValues.put(MSCRMContract.MessageRecord.ISREAD, 1); //0是未读  1 是已读
        return resolver.update(MSCRMContract.MessageRecord.CONTENT_URI, updateValues, MSCRMContract.MessageRecord.FID + "=?", new String[]{"" + fanId});
    }

    /***
     * 更新消息发送状态
     *
     * @param context 上下文
     * @param msgId   消息ID
     * @return
     */
    public static int updateSendStatus(Context context, int msgId, int sendStatus) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues updateValues = new ContentValues();
        updateValues.put(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL, sendStatus); //发送状态  1成功 0失败
        return resolver.update(MSCRMContract.MessageRecord.CONTENT_URI, updateValues, MSCRMContract.MessageRecord.MSGID + "=?", new String[]{"" + msgId});
    }

    /**
     * 删除指定消息记录
     *
     * @param id 消息ID
     * @return 删除数据条数
     */
    public int delete(int id) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.MessageRecord.CONTENT_URI,
                MSCRMContract.MessageRecord.MSGID + "=?", new String[]{"" + id});
    }

    /**
     * 删除消息通过粉丝ID
     *
     * @param id
     * @return
     */
    public int deleteByFanId(int id) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.MessageRecord.CONTENT_URI,
                MSCRMContract.MessageRecord.FID + "=?", new String[]{"" + id});
    }

    /**
     * 删除所有消息记录
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.MessageRecord.CONTENT_URI, null, null);
    }

    /**
     * 数据库中是否存在该消息记录
     *
     * @param id 消息ID
     * @return true:存在,false:不存在
     */
    public boolean isMessageExists(int id) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.MessageRecord.CONTENT_URI, null,
                MSCRMContract.MessageRecord.MSGID + "=?", new String[]{"" + id}, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

}
