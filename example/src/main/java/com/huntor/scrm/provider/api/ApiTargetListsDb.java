package com.huntor.scrm.provider.api;

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
public class ApiTargetListsDb {
    private static final String TAG = "ApiTargetListsDb";
    private Context mContext;
    public ApiTargetListsDb(Context context) {
        mContext = context;
    }


    /**
     * 粉丝所在自定义分组
     *
     * @param targetLists
     *            粉丝所在自定义分组信息
     * @return 插入数据的条数
     */
    public int bulkInsert(List<FanInfo.TargetList> targetLists,int accountId) {
        //删除已经存在的
        delete(accountId);
        ContentResolver resolver = mContext.getContentResolver();

        if (targetLists == null || targetLists.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "targetLists.size() = " + targetLists.size());
        }

        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[targetLists.size()];
        for (int i = 0; i < targetLists.size(); i++) {
            ContentValues value = new ContentValues();
            FanInfo.TargetList targetList = targetLists.get(i);
            /**
             * 构造一个粉丝所在分组
             */
            value.put(MSCRMContract.TargetLists.ID, targetList.id);
            value.put(MSCRMContract.TargetLists.NAME, targetList.name);
            value.put(MSCRMContract.TargetLists.ACCOUNT_ID, targetList.accountId);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.TargetLists.CONTENT_URI, values);
    }

    /**
     * 粉丝所在自定义分组
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<FanInfo.TargetList> getTargetListsByAccountId(Context context,int accountId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.TargetLists.CONTENT_URI, null,MSCRMContract.TargetLists.ACCOUNT_ID+"=?",new String[]{""+accountId},null);
        List<FanInfo.TargetList> targetLists = new ArrayList<FanInfo.TargetList>();
        if (cursor != null) {
            FanInfo.TargetList targetList = null;
            while (cursor.moveToNext()) {
                targetList = new FanInfo.TargetList();
                targetList.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.TargetLists.ID));
                targetList.name = cursor.getString(cursor.getColumnIndex(MSCRMContract.TargetLists.NAME));
                targetList.accountId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.TargetLists.ACCOUNT_ID));

                targetLists.add(targetList);
            }
            cursor.close();
        }
        return targetLists;
    }
    /**
     * 删除所有固定分组列表信息
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.TargetLists.CONTENT_URI, null, null);
    }
    public int delete(int accountId) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.TargetLists.CONTENT_URI,
                MSCRMContract.TargetLists.ACCOUNT_ID + "=?", new String[] { "" + accountId });
    }
}
