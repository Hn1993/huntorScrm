package com.huntor.mscrm.app2.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.huntor.mscrm.app2.model.PullMessageNote;
import com.huntor.mscrm.app2.model.ShakeModle;
import com.huntor.mscrm.app2.provider.MSCRMContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送的站内信消息表的数据库接口
 */
public class ApiPullMessageShakeDb {

    private static final String TAG = "ApiPullMessageShakeDb";
    private Context mContext;

    public ApiPullMessageShakeDb(Context context) {
        mContext = context;
    }



    /***
     * 获取所有的消息
     *
     * @return
     */
    public static List<ShakeModle> getMsgList(Context context) {
        List<ShakeModle> messages = new ArrayList<ShakeModle>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.Shake.CONTENT_URI, null, null, null, MSCRMContract.Shake.TIMESTAMP + " desc");
        if (cursor != null) {
            ShakeModle note = null;
            while (cursor.moveToNext()) {
                note = new ShakeModle();
                note.status = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Shake.STATUS));
                note.socialId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Shake.SOCIALID));
                note.deviceId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Shake.DEVICEID));
                note.distance = cursor.getString(cursor.getColumnIndex(MSCRMContract.Shake.DISTANCE));
                note.empId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Shake.EMPID));
                note.fanRealId = cursor.getString(cursor.getColumnIndex(MSCRMContract.Shake.FANREALID));
                note.fanId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Shake.FANID));
                note.timestamp = cursor.getLong(cursor.getColumnIndex(MSCRMContract.Shake.TIMESTAMP));
                note.groupId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Shake.GROUPID));
                note.isRead = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Shake.ISREAD));

                messages.add(note);
            }
            cursor.close();
        }
        return messages;
    }

    /**
     * 插入数据
     * @param message
     * @return
     */
    public Uri insert(ShakeModle message) {
        if (message == null) {
            return null;
        }
        /**
         * 消息存在则不插入
         */
        if (isMessageExists(message.timestamp)) {
            return null;
        }
        ContentValues value = new ContentValues();
        value.put(MSCRMContract.Shake.STATUS, message.status); //
        value.put(MSCRMContract.Shake.SOCIALID, message.socialId); //
        value.put(MSCRMContract.Shake.DEVICEID, message.deviceId); //
        value.put(MSCRMContract.Shake.DISTANCE, message.distance); //
        value.put(MSCRMContract.Shake.EMPID, message.empId); //
        value.put(MSCRMContract.Shake.FANREALID, message.fanRealId); //
        value.put(MSCRMContract.Shake.FANID, message.fanId); //
        value.put(MSCRMContract.Shake.TIMESTAMP, message.timestamp); //
        value.put(MSCRMContract.Shake.GROUPID, message.groupId); //
        value.put(MSCRMContract.Shake.ISREAD, message.isRead); //



        ContentResolver resolver = mContext.getContentResolver();
        return resolver.insert(MSCRMContract.Shake.CONTENT_URI, value);
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
        updateValues.put(MSCRMContract.Shake.ISREAD, 1); //0是未读  1 是已读
        return resolver.update(MSCRMContract.Shake.CONTENT_URI, updateValues, MSCRMContract.Shake.ISREAD + "=?", new String[]{"0"});
    }

    /****
     * 删除指定的ID消息
     *
     * @param id 时间
     * @return
     */
    public int delete(long id) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.Shake.CONTENT_URI,
                MSCRMContract.Shake.TIMESTAMP + "=?", new String[]{"" + id});
    }

    /**
     * 删除所有消息记录
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.Shake.CONTENT_URI, null, null);
    }

    /**
     * 数据库中是否存在该消息记录
     *
     * @param id 消息ID
     * @return true:存在,false:不存在
     */
    public boolean isMessageExists(long id) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.Shake.CONTENT_URI, null,
                MSCRMContract.Shake.TIMESTAMP + "=?", new String[]{"" + id}, null);
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
