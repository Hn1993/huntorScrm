package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.huntor.scrm.model.FansRecordModel;
import com.huntor.scrm.provider.MSCRMContract;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 消息表的数据库接口
 */
public class ApiFansRecordDb {

    private static final String TAG = "ApiFansRecordDb";

    private Context mContext;

    public ApiFansRecordDb(Context context) {
        mContext = context;
    }

    /**
     * 插入粉丝
     * 
     * @param fansList
     *            粉丝列表
     * @return 插入数据的条数
     */
    public int bulkInsert(List<FansRecordModel> fansList) {
        /**
         * 删除除登录账户之外的数据
         */
        ContentResolver resolver = mContext.getContentResolver();
        /*resolver.delete(MSCRMContract.MessageRecord.CONTENT_URI, MSCRMContract.MessageRecord.ID
                + "!=?", new String[] { id });*/

        if (fansList == null || fansList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "accounts.size() = " + fansList.size());
        }


        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[fansList.size()];
        for (int i = 0; i < fansList.size(); i++) {
            ContentValues value = new ContentValues();
            FansRecordModel fans = fansList.get(i);
            /**
             * 构造一个消息条目
             */
            value.put(MSCRMContract.FansRecord.FID, fans.accountId); // 是否是登录账户
            value.put(MSCRMContract.FansRecord.EID, fans.eid);
            value.put(MSCRMContract.FansRecord.NICK_NAME, fans.nickName); // 粉丝昵称
            value.put(MSCRMContract.FansRecord.AVATAR, fans.avatar); // 粉丝头像
            value.put(MSCRMContract.FansRecord.REAL_NAME, fans.realName); // 真实姓名
            value.put(MSCRMContract.FansRecord.GENDER, fans.gender); // 性别
            value.put(MSCRMContract.FansRecord.CITY, fans.city); // 城市
            value.put(MSCRMContract.FansRecord.PROVINCE, fans.province); // 省份
            value.put(MSCRMContract.FansRecord.FOLLOWSTATUS, fans.followStatus); // 关注状态
            value.put(MSCRMContract.FansRecord.SUBSCRIBE_TIME, fans.subscribeTime); // 关注时间
            value.put(MSCRMContract.FansRecord.LASTINTERACTION_TIME, fans.lastInteractionTime); //上次交互时间
            value.put(MSCRMContract.FansRecord.TIME, new Date().getTime()); //时间
            value.put(MSCRMContract.FansRecord.INTERACTION_TIMES, fans.interactionTimes); // 交互次数
            value.put(MSCRMContract.FansRecord.GROUP, fans.group); // 粉丝所属固定分组


            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.FansRecord.CONTENT_URI, values);
    }

    /**
     * 获取所有消息
     * 
     * @param context
     *            上下文
     * @return 
     */
    public static List<FansRecordModel> getFansList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        int empId = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, -1);
        Log.w(TAG, "empId = " + empId);
        Cursor cursor = resolver.query(MSCRMContract.FansRecord.CONTENT_URI, null, MSCRMContract.MessageRecord.EID + " = ?", new String[]{ "" + empId},
                MSCRMContract.FansRecord.TIME+" desc");
        List<FansRecordModel> fansList = new ArrayList<FansRecordModel>();
        if (cursor != null) {
        	FansRecordModel fans = null;
            while (cursor.moveToNext()) {
                fans = new FansRecordModel();
                fans.accountId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.FID));
                fans.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.EID));
                fans.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.NICK_NAME));
                fans.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.AVATAR));
                fans.realName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.REAL_NAME));
                fans.gender = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.GENDER));
                fans.city = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.CITY));
                fans.province = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.PROVINCE));
                fans.followStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.FOLLOWSTATUS)) == 1 ? true : false;
                fans.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansRecord.SUBSCRIBE_TIME));
                fans.lastInteractionTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansRecord.LASTINTERACTION_TIME));
                fans.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansRecord.TIME));
                fans.interactionTimes = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.INTERACTION_TIMES));
                fans.group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.GROUP));

                fansList.add(fans);
            }
            cursor.close();
        }
        return fansList;
    }

    /**
     * 根据粉丝ID获取指定粉丝信息
     * @param context
     * @param fanId
     * @return
     */
    public static FansRecordModel getFansInfoById(Context context, int fanId) {

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FansRecord.CONTENT_URI, null, MSCRMContract.FansRecord.FID + "=?", new String[]{"" + fanId}, null);
        FansRecordModel fans = null;
        if (cursor != null && cursor.moveToFirst()) {
            fans = new FansRecordModel();
            fans.accountId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.FID));
            fans.eid = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.EID));
            fans.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.NICK_NAME));
            fans.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.AVATAR));
            fans.realName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.REAL_NAME));
            fans.gender = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.GENDER));
            fans.city = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.CITY));
            fans.province = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansRecord.PROVINCE));
            fans.followStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.FOLLOWSTATUS)) == 1 ? true : false;
            fans.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansRecord.SUBSCRIBE_TIME));
            fans.lastInteractionTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansRecord.LASTINTERACTION_TIME));
            fans.time = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansRecord.TIME));
            fans.interactionTimes = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.INTERACTION_TIMES));
            fans.group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansRecord.GROUP));

            cursor.close();
        }

        return fans;
    }

    /**
     * 插入一条粉丝数据
     * 
     * @param fans
     *            粉丝信息
     * @return 返回该数据插入后的uri
     */
    public Uri insert(FansRecordModel fans) {
        if (fans == null) {
            return null;
        }

        /**
         * 粉丝存在则不插入
         */
        if (isFansExists(fans.accountId)) {
            delete(fans.accountId);
            //return null;
        }

        /**
         * 构造一个粉丝信息条目
         */
        ContentValues value = new ContentValues();
        /**
         * 构造一个消息条目
         */
        value.put(MSCRMContract.FansRecord.FID, fans.accountId); // 是否是登录账户
        value.put(MSCRMContract.FansRecord.EID, PreferenceUtils.getInt(mContext, Constant.PREFERENCE_EMP_ID, -1));
        value.put(MSCRMContract.FansRecord.NICK_NAME, fans.nickName); // 粉丝昵称
        value.put(MSCRMContract.FansRecord.AVATAR, fans.avatar); // 粉丝头像
        value.put(MSCRMContract.FansRecord.REAL_NAME, fans.realName); // 真实姓名
        value.put(MSCRMContract.FansRecord.GENDER, fans.gender); // 性别
        value.put(MSCRMContract.FansRecord.CITY, fans.city); // 城市
        value.put(MSCRMContract.FansRecord.PROVINCE, fans.province); // 省份
        value.put(MSCRMContract.FansRecord.FOLLOWSTATUS, fans.followStatus); // 关注状态
        value.put(MSCRMContract.FansRecord.SUBSCRIBE_TIME, fans.subscribeTime); // 关注时间
        value.put(MSCRMContract.FansRecord.LASTINTERACTION_TIME, fans.lastInteractionTime); //上次交互时间
        value.put(MSCRMContract.FansRecord.TIME, new Date().getTime()); // 时间
        value.put(MSCRMContract.FansRecord.INTERACTION_TIMES, fans.interactionTimes); // 交互次数
        value.put(MSCRMContract.FansRecord.GROUP, fans.group); // 交互次数


        ContentResolver resolver = mContext.getContentResolver();
        return resolver.insert(MSCRMContract.FansRecord.CONTENT_URI, value);
    }

    /**
     * 删除指定粉丝信息
     * 
     * @param id
     *            粉丝ID
     * @return 删除数据条数
     */
    public int delete(int id) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FansRecord.CONTENT_URI,
                MSCRMContract.FansRecord.FID + "=?", new String[] { "" + id });
    }

    /**
     * 删除所有粉丝信息
     * 
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FansRecord.CONTENT_URI, null, null);
    }

    /**
     *  更新粉丝最后交互时间
     * @param context
     * @param fanId
     * @return
     */
    public static int updateFansLastInteractiveTime(Context context,int fanId){
        ContentResolver resolver = context.getContentResolver();
        ContentValues updateValues = new ContentValues();
        updateValues.put(MSCRMContract.FansRecord.TIME, new Date().getTime());
        return resolver.update(MSCRMContract.FansRecord.CONTENT_URI, updateValues, MSCRMContract.FansRecord.FID + "=?", new String[]{"" + fanId});
    }

    /***
     * 更新粉丝所属固定分组
     * @param context
     * @param fanId 粉丝ID
     * @param group  粉丝所属固定分组
     * @return
     */
    public static int updateFansBelongToGroup(Context context,int fanId,int group){
        ContentResolver resolver = context.getContentResolver();
        ContentValues updateValues = new ContentValues();
        updateValues.put(MSCRMContract.FansRecord.GROUP, group);
        return resolver.update(MSCRMContract.FansRecord.CONTENT_URI, updateValues, MSCRMContract.FansRecord.FID + "=?", new String[]{"" + fanId});
    }


    /**
     * 数据库中是否存在该粉丝信息
     * 
     * @param id  消息ID
     * @return true:存在,false:不存在
     */
    public boolean isFansExists(int id) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FansRecord.CONTENT_URI, null,
                MSCRMContract.FansRecord.FID + "=?", new String[] { "" + id }, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        if(cursor != null){
            cursor.close();
        }
        return false;
    }

}
