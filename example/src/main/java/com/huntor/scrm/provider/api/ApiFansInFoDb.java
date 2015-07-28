package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.huntor.scrm.model.FanInfo;
import com.huntor.scrm.provider.MSCRMContract;
import com.huntor.scrm.utils.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cao on 2015/5/26.
 */
public class ApiFansInFoDb {
    private static final String TAG = "ApiFansInFoDb";
    private Context mContext;
    public ApiFansInFoDb(Context context) {
        mContext = context;
    }


    public void insert(List<FanInfo> fanInfoList){
        bulkInsert(fanInfoList);

    }

    /**
     * 插入粉丝详情
     *
     * @param fanInfoList
     *            粉丝详情列表
     * @return 插入数据的条数
     */
    public int bulkInsert(List<FanInfo> fanInfoList) {
        /**
         * 删除除登录账户之外的数据
         */
        ContentResolver resolver = mContext.getContentResolver();

        if (fanInfoList == null || fanInfoList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "fanInfoList.size() = " + fanInfoList.size());
        }

        // 根据粉丝列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[fanInfoList.size()];
        for (int i = 0; i < fanInfoList.size(); i++) {
            ContentValues value = new ContentValues();
            FanInfo fanInfo = fanInfoList.get(i);
            /**
             * 构造一个粉丝固定分组人数信息
             */
            value.put(MSCRMContract.FanInfo.NICKNAME, fanInfo.nickName);
            value.put(MSCRMContract.FanInfo.CITY, fanInfo.city);
            value.put(MSCRMContract.FanInfo.PROVINCE, fanInfo.province);
            value.put(MSCRMContract.FanInfo.FOLLOW_STATUS, fanInfo.followStatus);
            value.put(MSCRMContract.FanInfo.SUBSCRIBE_TIME, fanInfo.subscribeTime);
            value.put(MSCRMContract.FanInfo.LAST_INTER_ACTION_TIME, fanInfo.lastInteractionTime);
            value.put(MSCRMContract.FanInfo.INTER_ACTION_TIMES, fanInfo.interactionTimes);
            value.put(MSCRMContract.FanInfo.AVATAR, fanInfo.avatar);
            value.put(MSCRMContract.FanInfo.OCCUPATION, fanInfo.occupation);
            value.put(MSCRMContract.FanInfo.ACCOUNT_ID, fanInfo.accountId);
            value.put(MSCRMContract.FanInfo.REALNAME, fanInfo.realName);
            value.put(MSCRMContract.FanInfo.GENDER, fanInfo.gender);
            value.put(MSCRMContract.FanInfo.PHONE1, fanInfo.phone1);
            value.put(MSCRMContract.FanInfo.PHONE2, fanInfo.phone2);
            value.put(MSCRMContract.FanInfo.PHONE3, fanInfo.phone3);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.FanInfo.CONTENT_URI, values);
    }

    /**
     * 插入一条粉丝数据
     *
     * @param fanInfo
     *            粉丝信息
     * @return 返回该数据插入后的uri
     */
    public Uri insert(FanInfo fanInfo) {
        if (fanInfo == null) {
            return null;
        }

        delete(fanInfo.accountId);

        ApiTargetListsDb atl = new ApiTargetListsDb(mContext);
        for(FanInfo.TargetList tl : fanInfo.targetLists){
            tl.accountId = fanInfo.accountId;
        }
        atl.bulkInsert(fanInfo.targetLists,fanInfo.accountId);
        ApiDealsDb ad = new ApiDealsDb(mContext);
        ApiDealsDetailsDb add = new ApiDealsDetailsDb(mContext);
        for(FanInfo.Deals deals : fanInfo.deals){
            deals.accountId = fanInfo.accountId;
            if(deals.details != null){
                //交易详情
                add.bulkInsert(deals.details,deals.id);
            }
        }
        //交易记录
        ad.bulkInsert(fanInfo.deals,fanInfo.accountId);

        ApiPurchaseIntentsDb pid = new ApiPurchaseIntentsDb(mContext);
        for(FanInfo.PurchaseIntents pi : fanInfo.purchaseIntents){
            pi.accountId = fanInfo.accountId;
        }
        pid.bulkInsert(fanInfo.purchaseIntents,fanInfo.accountId);

        /**
         * 构造一个粉丝信息条目
         */
        ContentValues value = new ContentValues();
        /**
         * 构造一个粉丝固定分组人数信息
         */
        value.put(MSCRMContract.FanInfo.FAN_ID, fanInfo.fanId);
        value.put(MSCRMContract.FanInfo.NICKNAME, fanInfo.nickName);
        value.put(MSCRMContract.FanInfo.CITY, fanInfo.city);
        value.put(MSCRMContract.FanInfo.PROVINCE, fanInfo.province);
        value.put(MSCRMContract.FanInfo.FOLLOW_STATUS, fanInfo.followStatus);
        value.put(MSCRMContract.FanInfo.SUBSCRIBE_TIME, fanInfo.subscribeTime);
        value.put(MSCRMContract.FanInfo.LAST_INTER_ACTION_TIME, fanInfo.lastInteractionTime);
        value.put(MSCRMContract.FanInfo.INTER_ACTION_TIMES, fanInfo.interactionTimes);
        value.put(MSCRMContract.FanInfo.AVATAR, fanInfo.avatar);
        value.put(MSCRMContract.FanInfo.ACCOUNT_ID, fanInfo.accountId);
        value.put(MSCRMContract.FanInfo.REALNAME, fanInfo.realName);
        value.put(MSCRMContract.FanInfo.GENDER, fanInfo.gender);
        value.put(MSCRMContract.FanInfo.OCCUPATION, fanInfo.occupation);
        value.put(MSCRMContract.FanInfo.PHONE1, fanInfo.phone1);
        value.put(MSCRMContract.FanInfo.PHONE2, fanInfo.phone2);
        value.put(MSCRMContract.FanInfo.PHONE3, fanInfo.phone3);


        ContentResolver resolver = mContext.getContentResolver();
        return resolver.insert(MSCRMContract.FanInfo.CONTENT_URI, value);
    }

    /**
     * 获取粉丝列表
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<FanInfo> getFansList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FanInfo.CONTENT_URI, null, null, null, null);
        List<FanInfo> fanInfoList = new ArrayList<FanInfo>();
        if (cursor != null) {
            FanInfo fanInfo = null;
            while (cursor.moveToNext()) {
                fanInfo = new FanInfo();
                fanInfo.fanId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.FAN_ID));
                fanInfo.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.NICKNAME));
                fanInfo.city = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.CITY));
                fanInfo.province = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PROVINCE));
                fanInfo.followStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.FOLLOW_STATUS)) == 1 ? true : false;
                fanInfo.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FanInfo.SUBSCRIBE_TIME));
                fanInfo.lastInteractionTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FanInfo.LAST_INTER_ACTION_TIME));
                fanInfo.interactionTimes = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.INTER_ACTION_TIMES));
                fanInfo.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.AVATAR));
                fanInfo.accountId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.ACCOUNT_ID));
                fanInfo.realName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.REALNAME));
                fanInfo.gender = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.GENDER));
                fanInfo.occupation = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.OCCUPATION));
                fanInfo.phone1 = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PHONE1));
                fanInfo.phone2 = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PHONE2));
                fanInfo.phone3 = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PHONE3));

                fanInfoList.add(fanInfo);
            }
            cursor.close();
        }
        return fanInfoList;
    }

    /**
     * 根据粉丝ID获取指定粉丝信息
     * @param context
     * @param fanId
     * @return
     */
    public static FanInfo getFansInfoById(Context context, int fanId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FanInfo.CONTENT_URI, null, MSCRMContract.FanInfo.FAN_ID+"=?", new String[]{""+fanId}, null);
        FanInfo fanInfo = null;
        if (cursor != null && cursor.moveToFirst()) {
            fanInfo = new FanInfo();
            fanInfo.fanId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.FAN_ID));
            fanInfo.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.NICKNAME));
            fanInfo.city = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.CITY));
            fanInfo.province = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PROVINCE));
            fanInfo.followStatus = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.FOLLOW_STATUS)) == 1 ? true : false;
            fanInfo.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FanInfo.SUBSCRIBE_TIME));
            fanInfo.lastInteractionTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FanInfo.LAST_INTER_ACTION_TIME));
            fanInfo.interactionTimes = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.INTER_ACTION_TIMES));
            fanInfo.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.AVATAR));
            fanInfo.accountId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FanInfo.ACCOUNT_ID));
            fanInfo.realName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.REALNAME));
            fanInfo.gender = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.GENDER));
            fanInfo.occupation = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.OCCUPATION));
            fanInfo.phone1 = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PHONE1));
            fanInfo.phone2 = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PHONE2));
            fanInfo.phone3 = cursor.getString(cursor.getColumnIndex(MSCRMContract.FanInfo.PHONE3));
            cursor.close();
        }
        //获取粉丝所在组信息
        if(fanInfo!= null){
            if(Constant.DEBUG){
                Log.e(TAG,"FanInfo is not null!!!");
            }
            fanInfo.targetLists = ApiTargetListsDb.getTargetListsByAccountId(context,fanInfo.accountId);
            fanInfo.deals = ApiDealsDb.getDealsByAccountId(context, fanInfo.accountId);
            fanInfo.purchaseIntents = ApiPurchaseIntentsDb.getPurchaseIntentsByAccountId(context, fanInfo.accountId);
        }

        return fanInfo;
    }
    /**
     * 删除所有固定分组列表信息
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FanInfo.CONTENT_URI, null, null);
    }

    public int delete(int accountId) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FanInfo.CONTENT_URI,
                MSCRMContract.FanInfo.ACCOUNT_ID + "=?", new String[] { "" + accountId });
    }

}
