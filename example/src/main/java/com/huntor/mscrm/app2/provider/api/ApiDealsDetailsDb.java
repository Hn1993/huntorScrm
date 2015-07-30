package com.huntor.mscrm.app2.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.mscrm.app2.model.FanInfo;
import com.huntor.mscrm.app2.provider.MSCRMContract;
import com.huntor.mscrm.app2.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/26.
 */
public class ApiDealsDetailsDb {
    private static final String TAG = "ApiDealsDetailsDb";
    private Context mContext;
    public ApiDealsDetailsDb(Context context) {
        mContext = context;
    }


    /**
     * 插入粉丝交易记录交易详情
     * @param detailsList
     *            粉丝交易记录信息
     * @return 插入数据的条数
     */
    public int bulkInsert(List<FanInfo.Details> detailsList,int dealId) {
        /**
         * 如果存在先删除
         */
        delete(dealId);
        ContentResolver resolver = mContext.getContentResolver();
        if (detailsList == null || detailsList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "detailsList.size() = " + detailsList.size());
        }

        // 根据粉丝交易记录列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[detailsList.size()];
        for (int i = 0; i < detailsList.size(); i++) {
            ContentValues value = new ContentValues();
            FanInfo.Details details = detailsList.get(i);
            /**
             * 构造一个交易记录信息条目
             */
            value.put(MSCRMContract.DealsDetails.PRODUCT_ID, details.productId);
            value.put(MSCRMContract.DealsDetails.PRODUCT_NAME, details.productName);
            value.put(MSCRMContract.DealsDetails.AMOUNT, details.amount);
            value.put(MSCRMContract.DealsDetails.SN, details.sn);
            value.put(MSCRMContract.DealsDetails.DEAL_ID, dealId);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.DealsDetails.CONTENT_URI, values);
    }

    /**
     * 获取交易记录交易详情列表
     * @param context
     *            上下文
     * @return
     */
    public static List<FanInfo.Details> getDealsDetails(Context context,int dealId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.DealsDetails.CONTENT_URI, null,MSCRMContract.DealsDetails.DEAL_ID+"=?",new String[]{""+dealId},null);
        List<FanInfo.Details> detailsList = new ArrayList<FanInfo.Details>();
        if (cursor != null) {
            FanInfo.Details details = null;
            while (cursor.moveToNext()) {
                details = new FanInfo.Details();
                details.productId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.DealsDetails.PRODUCT_ID));
                details.productName = cursor.getString(cursor.getColumnIndex(MSCRMContract.DealsDetails.PRODUCT_NAME));
                details.amount = cursor.getInt(cursor.getColumnIndex(MSCRMContract.DealsDetails.AMOUNT));
                details.sn = cursor.getString(cursor.getColumnIndex(MSCRMContract.DealsDetails.SN));
                details.dealId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.DealsDetails.DEAL_ID));

                detailsList.add(details);
            }
            cursor.close();
        }
        return detailsList;
    }
    /**
     * 删除粉丝交易记录交易详情
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.DealsDetails.CONTENT_URI, null, null);
    }
    public int delete(int dealId) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.DealsDetails.CONTENT_URI,
                MSCRMContract.DealsDetails.DEAL_ID + "=?", new String[] { "" + dealId });
    }
}
