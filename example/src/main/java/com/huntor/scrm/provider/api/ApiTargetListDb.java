package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.scrm.model.Target;
import com.huntor.scrm.provider.MSCRMContract;
import com.huntor.scrm.utils.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cao on 2015/5/26.
 */
public class ApiTargetListDb {
    private static final String TAG = "ApiTargetListDb";
    private Context mContext;
    public ApiTargetListDb(Context context) {
        mContext = context;
    }


    /**
     * 插入自定义分组信息
     *
     * @param targetList
     *            自定义分组列表信息
     * @return 插入数据的条数
     */
    public int bulkInsert(List<Target> targetList) {
        //先删除再插入
        delete();
        ContentResolver resolver = mContext.getContentResolver();
        if (targetList == null || targetList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "targetList.size() = " + targetList.size());
        }

        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[targetList.size()];
        for (int i = 0; i < targetList.size(); i++) {
            ContentValues value = new ContentValues();
            Target target = targetList.get(i);
            /**
             * 构造一个粉丝固定分组人数信息
             */
            value.put(MSCRMContract.TargetList.ID, target.id);
            value.put(MSCRMContract.TargetList.NAME, target.name);
            value.put(MSCRMContract.TargetList.COUNT, target.count);
            value.put(MSCRMContract.TargetList.UPDATE_TIME, target.updateTime); // 人数

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.TargetList.CONTENT_URI, values);
    }

    /**
     * 获取自定义分组信息
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<Target> getTargetList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.TargetList.CONTENT_URI, null,null,null,null);
        List<Target> targetList = new ArrayList<Target>();
        if (cursor != null) {
            Target target = null;
            while (cursor.moveToNext()) {
                target = new Target();
                target.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.TargetList.ID));
                target.name = cursor.getString(cursor.getColumnIndex(MSCRMContract.TargetList.NAME));
                target.count = cursor.getInt(cursor.getColumnIndex(MSCRMContract.TargetList.COUNT));
                target.updateTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.TargetList.UPDATE_TIME));

                targetList.add(target);
            }
            cursor.close();
        }
        return targetList;
    }
    /**
     * 删除所有自定义分组信息
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.TargetList.CONTENT_URI, null, null);
    }

}
