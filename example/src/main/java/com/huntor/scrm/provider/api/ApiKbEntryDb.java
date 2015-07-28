package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.mscrm.app.model.FanInfo;
import com.huntor.mscrm.app.model.KbEntry;
import com.huntor.mscrm.app.provider.MSCRMContract;
import com.huntor.mscrm.app.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/30.
 */
public class ApiKbEntryDb {
    private static final String TAG = "ApiKbEntryDb";
    private Context mContext;
    public ApiKbEntryDb(Context context) {
        mContext = context;
    }


    /**
     * 知识话术某类别下具体条目
     *
     * @param kbEntryList
     *            具体条目列表
     * @return 插入数据的条数
     */
    public int bulkInsert(List<KbEntry> kbEntryList,int categorieId) {
        //删除已经存在的
        delete(categorieId);
        ContentResolver resolver = mContext.getContentResolver();

        if (kbEntryList == null || kbEntryList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "kbEntryList.size() = " + kbEntryList.size());
        }

        // 根据账户列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[kbEntryList.size()];
        for (int i = 0; i < kbEntryList.size(); i++) {
            ContentValues value = new ContentValues();
            KbEntry kbEntry = kbEntryList.get(i);
            /**
             * 构造一个粉丝所在分组
             */
            value.put(MSCRMContract.KbEntrys.ID, kbEntry.id);
            value.put(MSCRMContract.KbEntrys.TITLE, kbEntry.title);
            value.put(MSCRMContract.KbEntrys.CONTENT, kbEntry.content);
            value.put(MSCRMContract.KbEntrys.CATEGORIES_ID,categorieId);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.KbEntrys.CONTENT_URI, values);
    }

    /**
     * 获取知识话术具体条目 通过类别ID
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<KbEntry> getKbEntryByCategorieId(Context context,int categorieId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.KbEntrys.CONTENT_URI, null,MSCRMContract.KbEntrys.CATEGORIES_ID+"=?",new String[]{""+categorieId},null);
        List<KbEntry> kbEntrieList = new ArrayList<KbEntry>();
        if (cursor != null) {
            KbEntry kbEntry = null;
            while (cursor.moveToNext()) {
                kbEntry = new KbEntry();
                kbEntry.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.KbEntrys.ID));
                kbEntry.title = cursor.getString(cursor.getColumnIndex(MSCRMContract.KbEntrys.TITLE));
                kbEntry.content = cursor.getString(cursor.getColumnIndex(MSCRMContract.KbEntrys.CONTENT));
                kbEntry.categorieId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.KbEntrys.CATEGORIES_ID));

                kbEntrieList.add(kbEntry);
            }
            cursor.close();
        }
        return kbEntrieList;
    }
    /**
     * 删除所有知识话术具体条目的信息
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.KbEntrys.CONTENT_URI, null, null);
    }

    public int delete(int categorieId) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.KbEntrys.CONTENT_URI,
                MSCRMContract.KbEntrys.CATEGORIES_ID + "=?", new String[] { "" + categorieId });
    }
}
