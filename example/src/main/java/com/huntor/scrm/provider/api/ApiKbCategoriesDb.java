package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.huntor.scrm.model.Categorie;
import com.huntor.scrm.provider.MSCRMContract;
import com.huntor.scrm.utils.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by cao on 2015/5/30.
 */
public class ApiKbCategoriesDb {
    private static final String TAG = "ApiKbCategoriesDb";
    private Context mContext;
    public ApiKbCategoriesDb(Context context) {
        mContext = context;
    }


    /**
     * 插入自定义分组信息
     *
     * @param categoriesList
     *            自定义分组列表信息
     * @return 插入数据的条数
     */
    public int bulkInsert(List<Categorie> categoriesList) {
        //先删除再插入
        delete();
        ContentResolver resolver = mContext.getContentResolver();
        if (categoriesList == null || categoriesList.size() < 1) {
            return -1;
        }
        if(Constant.DEBUG){
            Log.i(TAG, "categoriesList.size() = " + categoriesList.size());
        }

        // 根据分组列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[categoriesList.size()];
        for (int i = 0; i < categoriesList.size(); i++) {
            ContentValues value = new ContentValues();
            Categorie categorie = categoriesList.get(i);
            /**
             * 构造一个粉丝固定分组人数信息
             */
            value.put(MSCRMContract.KbCategories.ID, categorie.id);
            value.put(MSCRMContract.KbCategories.PARENTID, categorie.parentId);
            value.put(MSCRMContract.KbCategories.NAME, categorie.name);

            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.KbCategories.CONTENT_URI, values);
    }

    /**
     * 获取知识话术分组信息
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<Categorie> getCategorieList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.KbCategories.CONTENT_URI, null,null,null,null);
        List<Categorie> categoriesList = new ArrayList<Categorie>();
        if (cursor != null) {
            Categorie categorie = null;
            while (cursor.moveToNext()) {
                categorie = new Categorie();
                categorie.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.KbCategories.ID));
                categorie.parentId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.KbCategories.PARENTID));
                categorie.name = cursor.getString(cursor.getColumnIndex(MSCRMContract.KbCategories.NAME));

                categoriesList.add(categorie);
            }
            cursor.close();
        }
        return categoriesList;
    }
    /**
     * 删除所有知识话术分组信息
     *
     * @return 删除数据条数
     */
    public int delete() {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.KbCategories.CONTENT_URI, null, null);
    }

}
