package com.huntor.mscrm.app.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.provider.MSCRMContract;
import com.huntor.mscrm.app.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/26.
 */
public class ApiFixedGroupFansListDb {
    private static final String TAG = "ApiFixedGroupFansListDb";
    private Context mContext;
    public ApiFixedGroupFansListDb(Context context) {
        mContext = context;
    }


    /**
     * 插入固定分组粉丝列表信息
     *
     * @param fansList
     *            粉丝固定分组粉丝列表
     * @return 插入数据的条数
     */
    public int bulkInsertByTargetId(List<Fans> fansList,int targetId) {
        for(Fans fans : fansList){
            fans.targetId = targetId;
        }
        deleteByTargetId(targetId);
        ContentResolver resolver = mContext.getContentResolver();
        if (fansList == null || fansList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "fansList.size() = " + fansList.size());
        }

        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[fansList.size()];
        for (int i = 0; i < fansList.size(); i++) {
            ContentValues value = new ContentValues();
            Fans fans = fansList.get(i);
            /**
             * 构造一个粉丝固定分组列表信息
             */
            value.put(MSCRMContract.FixedGroupFansList.ID, fans.id);
            value.put(MSCRMContract.FixedGroupFansList.GROUP, fans.group);
            value.put(MSCRMContract.FixedGroupFansList.TARGET_ID,fans.targetId);
            value.put(MSCRMContract.FixedGroupFansList.NICK_NAME, fans.nickName); // 昵称
            value.put(MSCRMContract.FixedGroupFansList.NAME, fans.name); // 昵称
            value.put(MSCRMContract.FixedGroupFansList.AVATAR, fans.avatar);
            value.put(MSCRMContract.FixedGroupFansList.REGIST_TIME, fans.registTime);
            value.put(MSCRMContract.FixedGroupFansList.SUBSCRIBE_TIME, fans.subscribeTime);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.FixedGroupFansList.CONTENT_URI, values);
    }

    public int bulkInsertByFixGroupId(List<Fans> fansList,int groupId) {

        //插入之前 删除之前记录
        deleteByGroup(groupId);
        ContentResolver resolver = mContext.getContentResolver();
        if (fansList == null || fansList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "fansList.size() = " + fansList.size());
        }

        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[fansList.size()];
        for (int i = 0; i < fansList.size(); i++) {
            ContentValues value = new ContentValues();
            Fans fans = fansList.get(i);
            /**
             * 构造一个粉丝固定分组列表信息
             */
            value.put(MSCRMContract.FixedGroupFansList.ID, fans.id);
            value.put(MSCRMContract.FixedGroupFansList.GROUP, fans.group);
            value.put(MSCRMContract.FixedGroupFansList.TARGET_ID,fans.targetId);
            value.put(MSCRMContract.FixedGroupFansList.NICK_NAME, fans.nickName); // 昵称
            value.put(MSCRMContract.FixedGroupFansList.NAME, fans.name); // 昵称
            value.put(MSCRMContract.FixedGroupFansList.AVATAR, fans.avatar);
            value.put(MSCRMContract.FixedGroupFansList.REGIST_TIME, fans.registTime);
            value.put(MSCRMContract.FixedGroupFansList.SUBSCRIBE_TIME, fans.subscribeTime);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.FixedGroupFansList.CONTENT_URI, values);
    }

    /**
     * 获取固定分组粉丝列表
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<Fans> getFixedGroupFansList(Context context,int group) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FixedGroupFansList.CONTENT_URI, null, MSCRMContract.FixedGroupFansList.GROUP + "=?", new String[]{""+group}, null);
        List<Fans> fixedGroupFansList = new ArrayList<Fans>();
        if (cursor != null) {
            Fans fans = null;
            while (cursor.moveToNext()) {
                fans = new Fans();
                fans.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.ID));
                fans.group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.GROUP));
                fans.targetId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.TARGET_ID));
                fans.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.NICK_NAME));
                fans.name = cursor.getString(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.NAME));
                fans.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.AVATAR));
                fans.registTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.REGIST_TIME));
                fans.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FixedGroupFansList.SUBSCRIBE_TIME));

                fixedGroupFansList.add(fans);
            }
            cursor.close();
        }
        return fixedGroupFansList;
    }


    /**
     * 删除所有固定分组粉丝列表信息
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FansGroupCount.CONTENT_URI, null, null);
    }

    /**
     * 删除指定分组的粉丝信息
     *
     * @param id
     *            粉丝分组
     * @return 删除数据条数
     */
    public int delete(int id) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FixedGroupFansList.CONTENT_URI,
                MSCRMContract.FixedGroupFansList.ID + "=?", new String[] { "" + id });
    }

    public int deleteByGroup(int group) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FixedGroupFansList.CONTENT_URI,
                MSCRMContract.FixedGroupFansList.GROUP + "=?", new String[] { "" + group });
    }

    public int deleteByTargetId(int targetId){
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FixedGroupFansList.CONTENT_URI,
                MSCRMContract.FixedGroupFansList.TARGET_ID + "=?", new String[] { "" + targetId });

    }

}
