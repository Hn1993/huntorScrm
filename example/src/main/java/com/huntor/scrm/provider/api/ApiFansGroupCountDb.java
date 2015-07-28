package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.mscrm.app.model.Data;
import com.huntor.mscrm.app.provider.MSCRMContract;
import com.huntor.mscrm.app.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/26.
 */
public class ApiFansGroupCountDb {
    private static final String TAG = "ApiFansGroupCountDb";
    private Context mContext;
    public ApiFansGroupCountDb(Context context) {
        mContext = context;
    }


    /**
     * 插入固定分组人数信息
     *
     * @param fansGroupCountList
     *            粉丝固定分组人数信息
     * @return 插入数据的条数
     */
    public int bulkInsert(List<Data> fansGroupCountList) {
        delete();
        ContentResolver resolver = mContext.getContentResolver();

        if (fansGroupCountList == null || fansGroupCountList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "accounts.size() = " + fansGroupCountList.size());
        }

        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[fansGroupCountList.size()];
        for (int i = 0; i < fansGroupCountList.size(); i++) {
            ContentValues value = new ContentValues();
            Data data = fansGroupCountList.get(i);
            /**
             * 构造一个粉丝固定分组人数信息
             */
            value.put(MSCRMContract.FansGroupCount.GROUP_TYPE, data.group);
            value.put(MSCRMContract.FansGroupCount.COUNT, data.count); // 人数

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.FansGroupCount.CONTENT_URI, values);
    }

    /**
     * 获取固定分组人数列表
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<Data> getFansGroupCountList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FansGroupCount.CONTENT_URI, null,null,null,null);
        List<Data> fansGroupCountList = new ArrayList<Data>();
        if (cursor != null) {
            Data data = null;
            while (cursor.moveToNext()) {
                data = new Data();
                data.group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansGroupCount.GROUP_TYPE));
                data.count = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansGroupCount.COUNT));

                fansGroupCountList.add(data);
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
        return resolver.delete(MSCRMContract.FansGroupCount.CONTENT_URI, null, null);
    }

}
