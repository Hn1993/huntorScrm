package com.huntor.mscrm.app.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.mscrm.app.model.Data;
import com.huntor.mscrm.app.model.FanInfo;
import com.huntor.mscrm.app.provider.MSCRMContract;
import com.huntor.mscrm.app.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/26.
 */
public class ApiPurchaseIntentsDb {
    private static final String TAG = "ApiPurchaseIntentsDb";
    private Context mContext;
    public ApiPurchaseIntentsDb(Context context) {
        mContext = context;
    }


    /**
     * 插入粉丝购买意向
     *
     * @param purchaseIntentses
     *            粉丝购买意向详情
     * @return 插入数据的条数
     */
    public int bulkInsert(List<FanInfo.PurchaseIntents> purchaseIntentses,int accountId) {

        delete(accountId);
        ContentResolver resolver = mContext.getContentResolver();

        if (purchaseIntentses == null || purchaseIntentses.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "purchaseIntentses.size() = " + purchaseIntentses.size());
        }

        // 根据购买意向列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[purchaseIntentses.size()];
        for (int i = 0; i < purchaseIntentses.size(); i++) {
            ContentValues value = new ContentValues();
            FanInfo.PurchaseIntents purchaseIntents = purchaseIntentses.get(i);
            /**
             * 构造一个粉丝固定分组人数信息
             */
            value.put(MSCRMContract.PurchaseIntents.ID, purchaseIntents.id);
            value.put(MSCRMContract.PurchaseIntents.PRODUCT_ID, purchaseIntents.productId); // 人数
            value.put(MSCRMContract.PurchaseIntents.PRODUCT_NAME, purchaseIntents.productName); // 人数
            value.put(MSCRMContract.PurchaseIntents.DESC, purchaseIntents.desc); // 人数
            value.put(MSCRMContract.PurchaseIntents.INTENT_TIME, purchaseIntents.intentTime); // 人数
            value.put(MSCRMContract.PurchaseIntents.ACCOUNT_ID, purchaseIntents.accountId); // 人数

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.PurchaseIntents.CONTENT_URI, values);
    }

    /**
     * 获取粉丝购买意向列表
     *
     * @param context
     *            上下文
     * @return
     */
    public static ArrayList<FanInfo.PurchaseIntents> getPurchaseIntentsByAccountId(Context context,int accountId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.PurchaseIntents.CONTENT_URI, null,MSCRMContract.PurchaseIntents.ACCOUNT_ID+"=?",new String[]{""+accountId},null);
        ArrayList<FanInfo.PurchaseIntents> fansGroupCountList = new ArrayList<FanInfo.PurchaseIntents>();
        if (cursor != null) {
            FanInfo.PurchaseIntents purchaseIntents = null;
            while (cursor.moveToNext()) {
                purchaseIntents = new FanInfo.PurchaseIntents();
                purchaseIntents.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.PurchaseIntents.ID));
                purchaseIntents.productId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.PurchaseIntents.PRODUCT_ID));
                purchaseIntents.productName = cursor.getString(cursor.getColumnIndex(MSCRMContract.PurchaseIntents.PRODUCT_NAME));
                purchaseIntents.desc = cursor.getString(cursor.getColumnIndex(MSCRMContract.PurchaseIntents.DESC));
                purchaseIntents.intentTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.PurchaseIntents.INTENT_TIME));
                purchaseIntents.accountId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.PurchaseIntents.ACCOUNT_ID));

                fansGroupCountList.add(purchaseIntents);
            }
            cursor.close();
        }
        return fansGroupCountList;
    }
    /**
     * 删除所有固定分组列表信息
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.PurchaseIntents.CONTENT_URI, null, null);
    }

    public int delete(int accountId) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.PurchaseIntents.CONTENT_URI,
                MSCRMContract.PurchaseIntents.ACCOUNT_ID + "=?", new String[] { "" + accountId });
    }
}
