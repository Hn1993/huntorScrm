package com.huntor.mscrm.app2.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.mscrm.app2.model.Data;
import com.huntor.mscrm.app2.model.DealResult;
import com.huntor.mscrm.app2.model.FanInfo;
import com.huntor.mscrm.app2.provider.MSCRMContract;
import com.huntor.mscrm.app2.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/26.
 */
public class ApiDealsDb {
    private static final String TAG = "ApiDealsDb";
    private Context mContext;
    public ApiDealsDb(Context context) {
        mContext = context;
    }


    /**
     * 插入粉丝交易记录
     * @param dealsList
     *            粉丝交易记录信息
     * @return 插入数据的条数
     */
    public int bulkInsert(List<FanInfo.Deals> dealsList,int accountId) {
        delete(accountId);
        ContentResolver resolver = mContext.getContentResolver();
        if (dealsList == null || dealsList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "dealsList.size() = " + dealsList.size());
        }

        // 根据粉丝交易记录列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[dealsList.size()];
        for (int i = 0; i < dealsList.size(); i++) {
            ContentValues value = new ContentValues();
            FanInfo.Deals deals = dealsList.get(i);
            /**
             * 构造一个交易记录信息条目
             */
            value.put(MSCRMContract.Deals.ID, deals.id);
            value.put(MSCRMContract.Deals.MONEY, deals.money);
            value.put(MSCRMContract.Deals.DEALTIME, deals.dealTime);
            value.put(MSCRMContract.Deals.ACCOUNT_ID, deals.accountId);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.Deals.CONTENT_URI, values);
    }

    /**
     * 获取交易记录列表
     * @param context
     *            上下文
     * @return
     */
    public static ArrayList<FanInfo.Deals> getDealsByAccountId(Context context,int accountId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.Deals.CONTENT_URI, null, MSCRMContract.Deals.ACCOUNT_ID+"=?", new String[]{""+accountId}, null);
        ArrayList<FanInfo.Deals> dealsList = new ArrayList<FanInfo.Deals>();
        if (cursor != null) {
            FanInfo.Deals deals = null;
            while (cursor.moveToNext()) {
                deals = new FanInfo.Deals();
                deals.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Deals.ID));
                deals.money = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Deals.MONEY));
                deals.dealTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.Deals.DEALTIME));
                deals.accountId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.Deals.ACCOUNT_ID));
                deals.details = ApiDealsDetailsDb.getDealsDetails(context,deals.id);
                dealsList.add(deals);
            }
            cursor.close();
        }

        return dealsList;
    }
    /**
     * 删除粉丝交易记录
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.Deals.CONTENT_URI, null, null);
    }
    public int delete(int accountId) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.Deals.CONTENT_URI,
                MSCRMContract.Deals.ACCOUNT_ID + "=?", new String[] { "" + accountId });
    }
}
