package com.huntor.scrm.provider.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.huntor.mscrm.app.model.FanInfo;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.provider.MSCRMContract;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PinYinUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/26.
 */
public class ApiAllFansInfoDb {
    private static final String TAG = "ApiAllFansInfoDb";
    private Context mContext;
    public ApiAllFansInfoDb(Context context) {
        mContext = context;
    }


    /**
     * 批量插入粉丝
     *
     * @param fansList
     *            粉丝列表
     * @return 插入数据的条数
     */
    public static int bulkInsert(Context context, List<Fans> fansList) {
        ContentResolver resolver = context.getContentResolver();
        if (fansList == null || fansList.size() < 1) {
            return -1;
        }
        MyLogger.i(TAG, "fansList.size() = " + fansList.size());


        // 根据粉丝列表长度创建一个ContentValues数组
        ContentValues[] values = new ContentValues[fansList.size()];
        for (int i = 0; i < fansList.size(); i++) {
            ContentValues value = new ContentValues();
            Fans fans = fansList.get(i);
            /**
             * 构造一个粉丝信息
             */
            value.put(MSCRMContract.FansInfo.ID, fans.id);
            value.put(MSCRMContract.FansInfo.GROUP_ID, fans.group);
            value.put(MSCRMContract.FansInfo.TARGET_ID, fans.targetId);
            value.put(MSCRMContract.FansInfo.NICKNAME, fans.nickName);
            value.put(MSCRMContract.FansInfo.NAME, fans.name);
            value.put(MSCRMContract.FansInfo.AVATAR, fans.avatar);
            value.put(MSCRMContract.FansInfo.REGIST_TIME, fans.registTime);
            value.put(MSCRMContract.FansInfo.SUBSCRIBE_TIME, fans.subscribeTime);
            value.put(MSCRMContract.FansInfo.ISCHECK, fans.isCheck?1:0);
            value.put(MSCRMContract.FansInfo.NICKNAME_PINYIN_FULL_SPELL, PinYinUtils.getFullSpell(fans.nickName==null?"":fans.nickName));
            value.put(MSCRMContract.FansInfo.NICKNAME_PINYIN_INITIAL_SPELL, PinYinUtils.getInitialSpell(fans.nickName==null?"":fans.nickName));
            value.put(MSCRMContract.FansInfo.NAME_PINYIN_FULL_SPELL, PinYinUtils.getFullSpell(fans.name==null?"":fans.name));
            value.put(MSCRMContract.FansInfo.NAME_PINYIN_INITIAL_SPELL, PinYinUtils.getInitialSpell(fans.name==null?"":fans.name));
            values[i] = value;
        }

        // 通过bulkInsert执行大量数据的插入操作
        return resolver.bulkInsert(MSCRMContract.FansInfo.CONTENT_URI, values);
    }

    /**
     * 插入一条粉丝数据
     *
     * @param fans
     *            粉丝信息
     * @return 返回该数据插入后的uri
     */
    public Uri insert(Fans fans) {
        if (fans == null) {
            return null;
        }
        /**
         * 构造一个粉丝
         */
        ContentValues value = new ContentValues();
        value.put(MSCRMContract.FansInfo.ID, fans.id);
        value.put(MSCRMContract.FansInfo.GROUP_ID, fans.group);
        value.put(MSCRMContract.FansInfo.TARGET_ID, fans.targetId);
        value.put(MSCRMContract.FansInfo.NICKNAME, fans.nickName);
        value.put(MSCRMContract.FansInfo.NAME, fans.name);
        value.put(MSCRMContract.FansInfo.AVATAR, fans.avatar);
        value.put(MSCRMContract.FansInfo.REGIST_TIME, fans.registTime);
        value.put(MSCRMContract.FansInfo.SUBSCRIBE_TIME, fans.subscribeTime);
        value.put(MSCRMContract.FansInfo.ISCHECK, fans.isCheck?0:1);
        value.put(MSCRMContract.FansInfo.NICKNAME_PINYIN_FULL_SPELL, PinYinUtils.getFullSpell(fans.nickName));
        value.put(MSCRMContract.FansInfo.NICKNAME_PINYIN_INITIAL_SPELL, PinYinUtils.getInitialSpell(fans.nickName));
        value.put(MSCRMContract.FansInfo.NAME_PINYIN_FULL_SPELL, PinYinUtils.getFullSpell(fans.name));
        value.put(MSCRMContract.FansInfo.NAME_PINYIN_INITIAL_SPELL, PinYinUtils.getInitialSpell(fans.name));

        ContentResolver resolver = mContext.getContentResolver();
        return resolver.insert(MSCRMContract.FansInfo.CONTENT_URI, value);
    }

    /**
     * 获取粉丝列表
     *
     * @param context
     *            上下文
     * @return
     */
    public static List<Fans> getFansList(Context context, String searchKey) {
        ContentResolver resolver = context.getContentResolver();

        Uri uri = Uri.parse(MSCRMContract.FansInfo.CONTENT_URI.toString() + "/search/" + searchKey);
        MyLogger.i(TAG,"uri:"+uri.toString());
        Cursor cursor = resolver.query(uri, null, null, null, null);
        List<Fans> fansList = new ArrayList<Fans>();

        if (cursor != null) {
            Fans fans = null;
            while (cursor.moveToNext()) {
                fans = new Fans();
                fans.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
                fans.group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.GROUP_ID));
                fans.targetId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.TARGET_ID));
                fans.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NICKNAME));
                fans.name = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NAME));
                fans.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.AVATAR));
                fans.registTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
                fans.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
                fans.isCheck = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.ISCHECK)) == 0 ? false : true;
                fansList.add(fans);
            }
            cursor.close();
        }
        return fansList;
    }

    /**
     * 搜索关键字返回一个Cursor
     * @param context
     * @param searchKey
     * @return
     */
    public static Cursor getCursorBySearchKey(Context context, String searchKey) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse(MSCRMContract.FansInfo.CONTENT_URI.toString() + "/search/" + searchKey);
        return resolver.query(uri, null, null, null, null);
    }

    /**
     * 查询所有本地缓存粉丝
     * @param context
     * @return
     */
    public static List<Fans> getFansList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FansInfo.CONTENT_URI, null, null, null, null);
        List<Fans> fansList = new ArrayList<Fans>();
        if (cursor != null) {
            Fans fans = null;
            while (cursor.moveToNext()) {
                fans = new Fans();
                fans.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
                fans.group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.GROUP_ID));
                fans.targetId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.TARGET_ID));
                fans.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NICKNAME));
                fans.name = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NAME));
                fans.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.AVATAR));
                fans.registTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
                fans.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
                fans.isCheck = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.ISCHECK)) == 0 ? false : true;
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
    public  Fans getFansById(Context context, int fanId) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MSCRMContract.FansInfo.CONTENT_URI, null, MSCRMContract.FansInfo.ID+"=?", new String[]{""+fanId}, null);
        Fans fans = null;
        if (cursor != null && cursor.moveToFirst()) {
            fans = new Fans();
            fans.id = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
            fans.group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.GROUP_ID));
            fans.targetId = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.TARGET_ID));
            fans.nickName = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NICKNAME));
            fans.name = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NAME));
            fans.avatar = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.AVATAR));
            fans.registTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
            fans.subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
            fans.isCheck = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.ISCHECK)) == 0 ? false : true;
            cursor.close();
        }
        return fans;
    }
    /**
     * 删除所有本地缓存粉丝
     *
     * @return 删除数据条数
     */
    public static int delete(Context context) {
        ContentResolver resolver = context.getContentResolver();
        return resolver.delete(MSCRMContract.FansInfo.CONTENT_URI, null, null);
    }


    /**
     * 根据粉丝id删除本地缓存粉丝
     * @param id
     * @return
     */
    public int delete(int id) {
        ContentResolver resolver = mContext.getContentResolver();
        return resolver.delete(MSCRMContract.FansInfo.CONTENT_URI,
                MSCRMContract.FansInfo.ID + "=?", new String[] { "" + id });
    }

}
