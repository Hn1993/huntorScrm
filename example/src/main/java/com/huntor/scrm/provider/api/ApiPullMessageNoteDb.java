package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.huntor.mscrm.app.model.MessageContext;
import com.huntor.mscrm.app.model.PullMessageNote;
import com.huntor.mscrm.app.provider.MSCRMContract;

import java.util.ArrayList;
import java.util.List;

/**
 * 推送的站内信消息表的数据库接口
 */
public class ApiPullMessageNoteDb {

    private static final String TAG = "ApiMessageRecordDb";
    private Context mContext;

    public ApiPullMessageNoteDb(Context context) {
        mContext = context;
    }

    /***
     * 插入消息
     *
     * @param messages
     * @return
     */
    public int bulkInsert(List<PullMessageNote> messages) {
        ContentResolver resolver = mContext.getContentResolver();
        for (int i = 0; i < messages.size(); i++) {
            resolver.delete(MSCRMContract.MessageNote.CONTENT_URI, MSCRMContract.MessageNote.DEST + "=?", new String[]{messages.get(i).dest + ""});
        }
        if (messages == null || messages.size() < 1) {
            return -1;
        }
        ContentValues[] values = new ContentValues[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            ContentValues value = new ContentValues();
            PullMessageNote note = messages.get(i);
            /**
             * 构造一个消息条目
             */
            value.put(MSCRMContract.MessageNote.FROMUSER, note.fromUser); //
            value.put(MSCRMContract.MessageNote.DEST, note.dest); // 消息ID
            value.put(MSCRMContract.MessageNote.TYPE, note.type); // 消息类型
            value.put(MSCRMContract.MessageNote.CONTENT, note.content); // 消息内容
            value.put(MSCRMContract.MessageNote.TIME, note.time); // 时间

            values[i] = value;
        }
        return resolver.bulkInsert(MSCRMContract.MessageNote.CONTENT_URI, values);
    }

    /***
     * 获取所有的消息
     *
     * @return
     */
    public static List<PullMessageNote> getMsgList(Context context) {
        List<PullMessageNote> messages = new ArrayList<PullMessageNote>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.MessageNote.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            PullMessageNote note = null;
            while (cursor.moveToNext()) {
                note = new PullMessageNote();
                note.fromUser = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageNote.FROMUSER));
                note.dest = cursor.getInt(cursor.getColumnIndex(MSCRMContract.MessageNote.DEST));
                note.type = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageNote.TYPE));
                note.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.MessageNote.CONTENT));
                note.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.MessageNote.TIME));
                messages.add(note);
            }
            cursor.close();
        }
        return messages;
    }

    /****
     * 获取所有的未读的消息
     *
     * @param context
     * @return
     */
    public static List<PullMessageNote> getAllUnRead(Context context) {
        List<PullMessageNote> messages = new ArrayList<PullMessageNote>();
        return messages;
    }

    public Uri insert(PullMessageNote message) {
        if (message == null) {
            return null;
        }
        /**
         * 消息存在则不插入
         */
        if (isMessageExists(message.time)) {
            return null;
        }
        ContentValues value = new ContentValues();
        value.put(MSCRMContract.MessageNote.FROMUSER, message.fromUser); //
        value.put(MSCRMContract.MessageNote.DEST, message.dest); // 消息ID
        value.put(MSCRMContract.MessageNote.TYPE, message.type); // 消息类型
        value.put(MSCRMContract.MessageNote.CONTENT, message.content); // 消息内容
        value.put(MSCRMContract.MessageNote.TIME, message.time); // 时间
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.insert(MSCRMContract.MessageNote.CONTENT_URI, value);
    }

    /****
     * 删除指定的ID消息
     *
     * @param id
     * @return
     */
    public int delete(long id) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.MessageNote.CONTENT_URI,
                MSCRMContract.MessageNote.TIME + "=?", new String[]{"" + id});
    }

    /**
     * 删除所有消息记录
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.MessageNote.CONTENT_URI, null, null);
    }

    /**
     * 数据库中是否存在该消息记录
     *
     * @param id 消息ID
     * @return true:存在,false:不存在
     */
    public boolean isMessageExists(long id) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.MessageNote.CONTENT_URI, null,
                MSCRMContract.MessageNote.TIME + "=?", new String[]{"" + id}, null);
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
