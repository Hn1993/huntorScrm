package com.huntor.mscrm.app.provider;

import java.util.HashMap;
import java.util.List;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PinYinUtils;
import com.huntor.mscrm.app.utils.PreferenceUtils;

/**
 * 这里使用ContentProvider，是为了为数据的访问提供一个统一的接口，方便客户端的操作，同时可以使用CursorLoader进行数据的加载
 */
public class MSCRMProvider extends ContentProvider {

    private static final String TAG = "MSCRMProvider";

    private MSCRMDBHelper mDatabaseHelper;

    /**
     * 表的MIME类型
     */
    private static final String VENDOR_TYPE_DIR = "vnd.android.cursor.dir";
    private static final String VENDOR_TYPE_ITEM = "vnd.android.cursor.item";
    private static final String VENDOR_SPECIFIC = "vnd." + MSCRMContract.AUTHORITY + ".";

    /**
     * 各Uri通过UriMatcher匹配后的返回结果
     */

    /**
     * 聊天消息
     */
    private static final int MESSAGE = 1;
    private static final int MESSAGE_ID = 2;

    /**
     * 粉丝表
     */
    private static final int FANS = 3;
    private static final int FANS_ID = 4;

    /**
     * 固定分组粉丝人数
     */
    private static final int FANS_GROUP_COUNT = 5;
    private static final int FANS_GROUP_COUNT_ID = 6;

    /***
     * 固定分组粉丝列表
     */
    private static final int FIXED_FANS_GROUP_LIST = 7;
    private static final int FIXED_FANS_GROUP_LIST_ID = 8;

    /***
     * 自定义粉丝分组
     */
    private static final int TARGET_LIST = 9;
    private static final int TARGET_LIST_ID = 10;

    /**
     * 粉丝详情
     */
    private static final int FANSINFO = 11;
    private static final int FANSINFO_ID = 12;

    /**
     * 粉丝所在自定义分组
     */
    private static final int TARGET_LISTS = 13;
    private static final int TARGET_LISTS_ID = 14;

    /**
     * 粉丝交易记录
     */
    private static final int DEALS = 15;
    private static final int DEALS_ID = 16;

    /**
     * 粉丝交易详情
     */
    private static final int DEALS_DETAILS = 17;
    private static final int DEALS_DETAILS_ID = 18;
    /**
     * 粉丝购买意向
     */
    private static final int PURCHASE_INTENTS = 19;
    private static final int PURCHASE_INTENTS_ID = 20;

    /**
     * 知识话术类别
     */
    private static final int KB_CATEGORIES = 21;
    private static final int KB_CATEGORIES_ID = 22;

    /**
     * 知识话术某类别下的具体条目
     */
    private static final int KB_ENTRYS = 23;
    private static final int KB_ENTRYS_ID = 24;

    /**
     * 全局搜索粉丝
     */

    private static final int ALL_FANS_INFO = 25;
    private static final int ALL_FANS_INFO_ID = 26;
    private static final int ALL_FANS_INFO_SEARCH = 27;

    /**
     * 站内信消息
     */
    private static final int PULL_NOTE = 28;
    private static final int PULL_NOTE_ID = 29;

    private static final UriMatcher mUriMatcher;

    private static HashMap<String, String> mMessageMap; // 聊天记录的投影字段
    private static HashMap<String, String> mFansMap; // 粉丝的投影字段
    private static HashMap<String, String> mFansGroupCountMap;//粉丝固定分组的投影字段
    private static HashMap<String, String> mFixedGroupFansListMap;//固定分组粉丝列表的投影字段
    private static HashMap<String, String> mTargetListMap; //自定义粉丝分组的投影字段
    private static HashMap<String, String> mFanInfoMap; //粉丝详情
    private static HashMap<String, String> mTargetListsMap; //自定义分组列表的投影字段
    private static HashMap<String, String> mDealsMap; //粉丝交易记录的投影字段
    private static HashMap<String, String> mDealDetailsMap; //粉丝交易详情的投影字段
    private static HashMap<String, String> mPurchaseIntentsMap; //粉丝购买意向的投影字段
    private static HashMap<String, String> mKbCategories; //知识话术类别的投影字段
    private static HashMap<String, String> mKbEntrys; //知识话术具体条目的投影字段

    private static HashMap<String, String> mAllFansInfoMap;//全局搜索粉丝投影字段

    private static HashMap<String, String> mPULLNOTEMap;//站内信记录的投影字段



    /**
     * 该静态代码块用来将各Uri装入UriMatcher并初始化要查询表的投影投影字段
     */
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /**
         * 消息记录
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.MessageRecord.TABLE_NAME,
                MESSAGE);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.MessageRecord.TABLE_NAME
                + "/#", MESSAGE_ID);

        /**
         * 消息记录的投影字段
         */
        mMessageMap = new HashMap<String, String>();
        mMessageMap.put(MSCRMContract.MessageRecord.ARTIFICIAL_STATUS, MSCRMContract.MessageRecord.ARTIFICIAL_STATUS);
        mMessageMap.put(MSCRMContract.MessageRecord.MSGID, MSCRMContract.MessageRecord.MSGID);
        mMessageMap.put(MSCRMContract.MessageRecord.TYPE, MSCRMContract.MessageRecord.TYPE);
        mMessageMap.put(MSCRMContract.MessageRecord.CONTENT, MSCRMContract.MessageRecord.CONTENT);
        mMessageMap.put(MSCRMContract.MessageRecord.TIMESTAMP, MSCRMContract.MessageRecord.TIMESTAMP);
        mMessageMap.put(MSCRMContract.MessageRecord.TIME, MSCRMContract.MessageRecord.TIME);
        mMessageMap.put(MSCRMContract.MessageRecord.FID, MSCRMContract.MessageRecord.FID);
        mMessageMap.put(MSCRMContract.MessageRecord.EID, MSCRMContract.MessageRecord.EID);
        mMessageMap.put(MSCRMContract.MessageRecord.GROUP_ID, MSCRMContract.MessageRecord.GROUP_ID);
        mMessageMap.put(MSCRMContract.MessageRecord.ISREAD, MSCRMContract.MessageRecord.ISREAD);
        mMessageMap.put(MSCRMContract.MessageRecord.SEND_OR_RECEIVE, MSCRMContract.MessageRecord.SEND_OR_RECEIVE);
        mMessageMap.put(MSCRMContract.MessageRecord.SUCCESS_OR_FAIL, MSCRMContract.MessageRecord.SUCCESS_OR_FAIL);
        /**
         *站内信记录
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.MessageNote.TABLE_NAME,
                PULL_NOTE);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.MessageNote.TABLE_NAME
                + "/#", PULL_NOTE_ID);
        /**
         * 站内信记录的投影字段
         */
        mPULLNOTEMap = new HashMap<String, String>();
        mPULLNOTEMap.put(MSCRMContract.MessageNote.FROMUSER, MSCRMContract.MessageNote.FROMUSER);
        mPULLNOTEMap.put(MSCRMContract.MessageNote.DEST, MSCRMContract.MessageNote.DEST);
        mPULLNOTEMap.put(MSCRMContract.MessageNote.TYPE, MSCRMContract.MessageNote.TYPE);
        mPULLNOTEMap.put(MSCRMContract.MessageNote.CONTENT, MSCRMContract.MessageNote.CONTENT);
        mPULLNOTEMap.put(MSCRMContract.MessageNote.TIME, MSCRMContract.MessageNote.TIME);

        /**
         * 粉丝
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansRecord.TABLE_NAME,
                FANS);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansRecord.TABLE_NAME
                + "/#", FANS_ID);
        /**
         * 粉丝的投影字段
         */
        mFansMap = new HashMap<String, String>();
        mFansMap.put(MSCRMContract.FansRecord.FID, MSCRMContract.FansRecord.FID);
        mFansMap.put(MSCRMContract.FansRecord.EID, MSCRMContract.FansRecord.EID);
        mFansMap.put(MSCRMContract.FansRecord.NICK_NAME, MSCRMContract.FansRecord.NICK_NAME);
        mFansMap.put(MSCRMContract.FansRecord.AVATAR, MSCRMContract.FansRecord.AVATAR);
        mFansMap.put(MSCRMContract.FansRecord.REAL_NAME, MSCRMContract.FansRecord.REAL_NAME);
        mFansMap.put(MSCRMContract.FansRecord.GENDER, MSCRMContract.FansRecord.GENDER);
        mFansMap.put(MSCRMContract.FansRecord.CITY, MSCRMContract.FansRecord.CITY);
        mFansMap.put(MSCRMContract.FansRecord.PROVINCE, MSCRMContract.FansRecord.PROVINCE);
        mFansMap.put(MSCRMContract.FansRecord.FOLLOWSTATUS, MSCRMContract.FansRecord.FOLLOWSTATUS);
        mFansMap.put(MSCRMContract.FansRecord.SUBSCRIBE_TIME, MSCRMContract.FansRecord.SUBSCRIBE_TIME);
        mFansMap.put(MSCRMContract.FansRecord.LASTINTERACTION_TIME, MSCRMContract.FansRecord.LASTINTERACTION_TIME);
        mFansMap.put(MSCRMContract.FansRecord.TIME, MSCRMContract.FansRecord.TIME);
        mFansMap.put(MSCRMContract.FansRecord.INTERACTION_TIMES, MSCRMContract.FansRecord.INTERACTION_TIMES);
        mFansMap.put(MSCRMContract.FansRecord.GROUP, MSCRMContract.FansRecord.GROUP);

        /**
         * 粉丝固定分组
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansGroupCount.TABLE_NAME,
                FANS_GROUP_COUNT);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansGroupCount.TABLE_NAME
                + "/#", FANS_GROUP_COUNT_ID);
        /**
         * 粉丝固定分组的投影字段
         */
        mFansGroupCountMap = new HashMap<String, String>();
        mFansGroupCountMap.put(MSCRMContract.FansGroupCount.GROUP_TYPE, MSCRMContract.FansGroupCount.GROUP_TYPE);
        mFansGroupCountMap.put(MSCRMContract.FansGroupCount.COUNT, MSCRMContract.FansGroupCount.COUNT);

        /**
         * 固定分组粉丝列表
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FixedGroupFansList.TABLE_NAME,
                FIXED_FANS_GROUP_LIST);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FixedGroupFansList.TABLE_NAME
                + "/#", FIXED_FANS_GROUP_LIST_ID);
        /**
         * 固定分组粉丝列表的投影字段
         */
        mFixedGroupFansListMap = new HashMap<String, String>();
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.ID, MSCRMContract.FixedGroupFansList.ID);
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.GROUP, MSCRMContract.FixedGroupFansList.GROUP);
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.TARGET_ID, MSCRMContract.FixedGroupFansList.TARGET_ID);
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.AVATAR, MSCRMContract.FixedGroupFansList.AVATAR);
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.NICK_NAME, MSCRMContract.FixedGroupFansList.NICK_NAME);
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.NAME, MSCRMContract.FixedGroupFansList.NAME);
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.REGIST_TIME, MSCRMContract.FixedGroupFansList.REGIST_TIME);
        mFixedGroupFansListMap.put(MSCRMContract.FixedGroupFansList.SUBSCRIBE_TIME, MSCRMContract.FixedGroupFansList.SUBSCRIBE_TIME);

        /**
         * 自定义粉丝分组
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.TargetList.TABLE_NAME,
                TARGET_LIST);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.TargetList.TABLE_NAME
                + "/#", TARGET_LIST_ID);
        /**
         * 自定义粉丝分组的投影字段
         */
        mTargetListMap = new HashMap<String, String>();
        mTargetListMap.put(MSCRMContract.TargetList.ID, MSCRMContract.TargetList.ID);
        mTargetListMap.put(MSCRMContract.TargetList.NAME, MSCRMContract.TargetList.NAME);
        mTargetListMap.put(MSCRMContract.TargetList.COUNT, MSCRMContract.TargetList.COUNT);
        mTargetListMap.put(MSCRMContract.TargetList.UPDATE_TIME, MSCRMContract.TargetList.UPDATE_TIME);

        /**
         * 粉丝详情
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FanInfo.TABLE_NAME,
                FANSINFO);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FanInfo.TABLE_NAME
                + "/#", FANSINFO_ID);
        /**
         * 粉丝详情的投影字段
         */
        mFanInfoMap = new HashMap<String, String>();
        mFanInfoMap.put(MSCRMContract.FanInfo.FAN_ID, MSCRMContract.FanInfo.FAN_ID);
        mFanInfoMap.put(MSCRMContract.FanInfo.NICKNAME, MSCRMContract.FanInfo.NICKNAME);
        mFanInfoMap.put(MSCRMContract.FanInfo.CITY, MSCRMContract.FanInfo.CITY);
        mFanInfoMap.put(MSCRMContract.FanInfo.PROVINCE, MSCRMContract.FanInfo.PROVINCE);
        mFanInfoMap.put(MSCRMContract.FanInfo.FOLLOW_STATUS, MSCRMContract.FanInfo.FOLLOW_STATUS);
        mFanInfoMap.put(MSCRMContract.FanInfo.SUBSCRIBE_TIME, MSCRMContract.FanInfo.SUBSCRIBE_TIME);
        mFanInfoMap.put(MSCRMContract.FanInfo.LAST_INTER_ACTION_TIME, MSCRMContract.FanInfo.LAST_INTER_ACTION_TIME);
        mFanInfoMap.put(MSCRMContract.FanInfo.INTER_ACTION_TIMES, MSCRMContract.FanInfo.INTER_ACTION_TIMES);
        mFanInfoMap.put(MSCRMContract.FanInfo.AVATAR, MSCRMContract.FanInfo.AVATAR);
        mFanInfoMap.put(MSCRMContract.FanInfo.OCCUPATION, MSCRMContract.FanInfo.OCCUPATION);
        mFanInfoMap.put(MSCRMContract.FanInfo.ACCOUNT_ID, MSCRMContract.FanInfo.ACCOUNT_ID);
        mFanInfoMap.put(MSCRMContract.FanInfo.REALNAME, MSCRMContract.FanInfo.REALNAME);
        mFanInfoMap.put(MSCRMContract.FanInfo.GENDER, MSCRMContract.FanInfo.GENDER);
        mFanInfoMap.put(MSCRMContract.FanInfo.PHONE1, MSCRMContract.FanInfo.PHONE1);
        mFanInfoMap.put(MSCRMContract.FanInfo.PHONE2, MSCRMContract.FanInfo.PHONE2);
        mFanInfoMap.put(MSCRMContract.FanInfo.PHONE3, MSCRMContract.FanInfo.PHONE3);
        /**
         * 粉丝所在自定义分组
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.TargetLists.TABLE_NAME,
                TARGET_LISTS);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.TargetLists.TABLE_NAME
                + "/#", TARGET_LISTS_ID);
        /**
         * 粉丝所在自定义分组的投影字段
         */
        mTargetListsMap = new HashMap<String, String>();
        mTargetListsMap.put(MSCRMContract.TargetLists.ID, MSCRMContract.TargetLists.ID);
        mTargetListsMap.put(MSCRMContract.TargetLists.NAME, MSCRMContract.TargetLists.NAME);
        mTargetListsMap.put(MSCRMContract.TargetLists.ACCOUNT_ID, MSCRMContract.TargetLists.ACCOUNT_ID);
        /**
         * 交易记录
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.Deals.TABLE_NAME,
                DEALS);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.Deals.TABLE_NAME
                + "/#", DEALS_ID);
        /**
         * 交易记录的投影字段
         */
        mDealsMap = new HashMap<String, String>();
        mDealsMap.put(MSCRMContract.Deals.ID, MSCRMContract.Deals.ID);
        mDealsMap.put(MSCRMContract.Deals.MONEY, MSCRMContract.Deals.MONEY);
        mDealsMap.put(MSCRMContract.Deals.DEALTIME, MSCRMContract.Deals.DEALTIME);
        mDealsMap.put(MSCRMContract.Deals.ACCOUNT_ID, MSCRMContract.Deals.ACCOUNT_ID);
        /**
         * 交易详情
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.DealsDetails.TABLE_NAME,
                DEALS_DETAILS);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.DealsDetails.TABLE_NAME
                + "/#", DEALS_DETAILS_ID);
        /**
         * 交易详情的投影字段
         */
        mDealDetailsMap = new HashMap<String, String>();
        mDealDetailsMap.put(MSCRMContract.DealsDetails.PRODUCT_ID, MSCRMContract.DealsDetails.PRODUCT_ID);
        mDealDetailsMap.put(MSCRMContract.DealsDetails.PRODUCT_NAME, MSCRMContract.DealsDetails.PRODUCT_NAME);
        mDealDetailsMap.put(MSCRMContract.DealsDetails.AMOUNT, MSCRMContract.DealsDetails.AMOUNT);
        mDealDetailsMap.put(MSCRMContract.DealsDetails.SN, MSCRMContract.DealsDetails.SN);
        mDealDetailsMap.put(MSCRMContract.DealsDetails.DEAL_ID, MSCRMContract.DealsDetails.DEAL_ID);

        /**
         * 粉丝购买意向
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.PurchaseIntents.TABLE_NAME,
                PURCHASE_INTENTS);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.PurchaseIntents.TABLE_NAME
                + "/#", PURCHASE_INTENTS_ID);
        /**
         * 粉丝购买意向的投影字段
         */
        mPurchaseIntentsMap = new HashMap<String, String>();
        mPurchaseIntentsMap.put(MSCRMContract.PurchaseIntents.ID, MSCRMContract.PurchaseIntents.ID);
        mPurchaseIntentsMap.put(MSCRMContract.PurchaseIntents.PRODUCT_ID, MSCRMContract.PurchaseIntents.PRODUCT_ID);
        mPurchaseIntentsMap.put(MSCRMContract.PurchaseIntents.PRODUCT_NAME, MSCRMContract.PurchaseIntents.PRODUCT_NAME);
        mPurchaseIntentsMap.put(MSCRMContract.PurchaseIntents.DESC, MSCRMContract.PurchaseIntents.DESC);
        mPurchaseIntentsMap.put(MSCRMContract.PurchaseIntents.INTENT_TIME, MSCRMContract.PurchaseIntents.INTENT_TIME);
        mPurchaseIntentsMap.put(MSCRMContract.PurchaseIntents.ACCOUNT_ID, MSCRMContract.PurchaseIntents.ACCOUNT_ID);


        /**
         * 知识话术类别
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.KbCategories.TABLE_NAME,
                KB_CATEGORIES);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.KbCategories.TABLE_NAME
                + "/#", KB_CATEGORIES_ID);
        /**
         * 知识话术类的投影字段
         */
        mKbCategories = new HashMap<String, String>();
        mKbCategories.put(MSCRMContract.KbCategories.ID, MSCRMContract.KbCategories.ID);
        mKbCategories.put(MSCRMContract.KbCategories.PARENTID, MSCRMContract.KbCategories.PARENTID);
        mKbCategories.put(MSCRMContract.KbCategories.NAME, MSCRMContract.KbCategories.NAME);

        /**
         * 知识话术类别
         */
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.KbEntrys.TABLE_NAME,
                KB_ENTRYS);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.KbEntrys.TABLE_NAME
                + "/#", KB_ENTRYS_ID);
        /**
         * 知识话术类别下的具体条目=的投影字段
         */
        mKbEntrys = new HashMap<String, String>();
        mKbEntrys.put(MSCRMContract.KbEntrys.ID, MSCRMContract.KbEntrys.ID);
        mKbEntrys.put(MSCRMContract.KbEntrys.TITLE, MSCRMContract.KbEntrys.TITLE);
        mKbEntrys.put(MSCRMContract.KbEntrys.CONTENT, MSCRMContract.KbEntrys.CONTENT);
        mKbEntrys.put(MSCRMContract.KbEntrys.CATEGORIES_ID, MSCRMContract.KbEntrys.CATEGORIES_ID);

        /**
         * 全局搜索粉丝
         */

        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansInfo.TABLE_NAME, ALL_FANS_INFO);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansInfo.TABLE_NAME + "/#", ALL_FANS_INFO_ID);

        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansInfo.TABLE_NAME + "/search", ALL_FANS_INFO_SEARCH);
        mUriMatcher.addURI(MSCRMContract.AUTHORITY, MSCRMContract.FansInfo.TABLE_NAME + "/search/*", ALL_FANS_INFO_SEARCH);

        /**
         * 全局搜索粉丝的投影字段
         */
        mAllFansInfoMap = new HashMap<String, String>();
        mAllFansInfoMap.put(MSCRMContract.FansInfo._ID, MSCRMContract.FansInfo._ID);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.ID, MSCRMContract.FansInfo.ID);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.GROUP_ID, MSCRMContract.FansInfo.GROUP_ID);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.TARGET_ID, MSCRMContract.FansInfo.TARGET_ID);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.NICKNAME, MSCRMContract.FansInfo.NICKNAME);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.NAME, MSCRMContract.FansInfo.NAME);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.AVATAR, MSCRMContract.FansInfo.AVATAR);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.REGIST_TIME, MSCRMContract.FansInfo.REGIST_TIME);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.SUBSCRIBE_TIME, MSCRMContract.FansInfo.SUBSCRIBE_TIME);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.ISCHECK, MSCRMContract.FansInfo.ISCHECK);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.NICKNAME_PINYIN_FULL_SPELL, MSCRMContract.FansInfo.NICKNAME_PINYIN_FULL_SPELL);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.NICKNAME_PINYIN_INITIAL_SPELL, MSCRMContract.FansInfo.NICKNAME_PINYIN_INITIAL_SPELL);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.NAME_PINYIN_FULL_SPELL, MSCRMContract.FansInfo.NAME_PINYIN_FULL_SPELL);
        mAllFansInfoMap.put(MSCRMContract.FansInfo.NAME_PINYIN_INITIAL_SPELL, MSCRMContract.FansInfo.NAME_PINYIN_INITIAL_SPELL);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new MSCRMDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // 初始化查询语句的构建器
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        /**
         * 设置要查询的表,投影区,筛选条件
         */
        switch (mUriMatcher.match(uri)) {
            case MESSAGE:
                qb.setTables(MSCRMContract.MessageRecord.TABLE_NAME);
                qb.setProjectionMap(mMessageMap);
                break;
            case MESSAGE_ID:
                qb.setTables(MSCRMContract.MessageRecord.TABLE_NAME);
                qb.setProjectionMap(mMessageMap);
                qb.appendWhere(MSCRMContract.MessageRecord._ID + "=" + ContentUris.parseId(uri));
                break;
            case PULL_NOTE:
                qb.setTables(MSCRMContract.MessageNote.TABLE_NAME);
                qb.setProjectionMap(mPULLNOTEMap);
                break;
            case PULL_NOTE_ID:
                qb.setTables(MSCRMContract.MessageNote.TABLE_NAME);
                qb.setProjectionMap(mPULLNOTEMap);
                qb.appendWhere(MSCRMContract.MessageNote._ID + "=" + ContentUris.parseId(uri));
                break;
            case FANS:
                qb.setTables(MSCRMContract.FansRecord.TABLE_NAME);
                qb.setProjectionMap(mFansMap);
                break;
            case FANS_ID:
                qb.setTables(MSCRMContract.FansRecord.TABLE_NAME);
                qb.setProjectionMap(mFansMap);
                qb.appendWhere(MSCRMContract.FansRecord._ID + "=" + ContentUris.parseId(uri));
                break;
            case FANS_GROUP_COUNT:
                qb.setTables(MSCRMContract.FansGroupCount.TABLE_NAME);
                qb.setProjectionMap(mFansGroupCountMap);
                break;
            case FANS_GROUP_COUNT_ID:
                qb.setTables(MSCRMContract.FansGroupCount.TABLE_NAME);
                qb.setProjectionMap(mFansGroupCountMap);
                qb.appendWhere(MSCRMContract.FansGroupCount._ID + "=" + ContentUris.parseId(uri));
                break;
            case FIXED_FANS_GROUP_LIST:
                qb.setTables(MSCRMContract.FixedGroupFansList.TABLE_NAME);
                qb.setProjectionMap(mFixedGroupFansListMap);
                break;
            case FIXED_FANS_GROUP_LIST_ID:
                qb.setTables(MSCRMContract.FixedGroupFansList.TABLE_NAME);
                qb.setProjectionMap(mFixedGroupFansListMap);
                qb.appendWhere(MSCRMContract.FixedGroupFansList._ID + "=" + ContentUris.parseId(uri));
                break;
            case TARGET_LIST:
                qb.setTables(MSCRMContract.TargetList.TABLE_NAME);
                qb.setProjectionMap(mTargetListMap);
                break;
            case TARGET_LIST_ID:
                qb.setTables(MSCRMContract.TargetList.TABLE_NAME);
                qb.setProjectionMap(mTargetListMap);
                qb.appendWhere(MSCRMContract.TargetList._ID + "=" + ContentUris.parseId(uri));
                break;
            case FANSINFO:
                qb.setTables(MSCRMContract.FanInfo.TABLE_NAME);
                qb.setProjectionMap(mFanInfoMap);
                break;
            case FANSINFO_ID:
                qb.setTables(MSCRMContract.FanInfo.TABLE_NAME);
                qb.setProjectionMap(mFanInfoMap);
                qb.appendWhere(MSCRMContract.FanInfo._ID + "=" + ContentUris.parseId(uri));
                break;
            case TARGET_LISTS:
                qb.setTables(MSCRMContract.TargetLists.TABLE_NAME);
                qb.setProjectionMap(mTargetListsMap);
                break;
            case TARGET_LISTS_ID:
                qb.setTables(MSCRMContract.TargetLists.TABLE_NAME);
                qb.setProjectionMap(mTargetListsMap);
                qb.appendWhere(MSCRMContract.TargetLists._ID + "=" + ContentUris.parseId(uri));
                break;
            case DEALS:
                qb.setTables(MSCRMContract.Deals.TABLE_NAME);
                qb.setProjectionMap(mDealsMap);
                break;
            case DEALS_ID:
                qb.setTables(MSCRMContract.Deals.TABLE_NAME);
                qb.setProjectionMap(mDealsMap);
                qb.appendWhere(MSCRMContract.Deals._ID + "=" + ContentUris.parseId(uri));
                break;
            case DEALS_DETAILS:
                qb.setTables(MSCRMContract.DealsDetails.TABLE_NAME);
                qb.setProjectionMap(mDealDetailsMap);
                break;
            case DEALS_DETAILS_ID:
                qb.setTables(MSCRMContract.DealsDetails.TABLE_NAME);
                qb.setProjectionMap(mDealDetailsMap);
                qb.appendWhere(MSCRMContract.DealsDetails._ID + "=" + ContentUris.parseId(uri));
                break;
            case PURCHASE_INTENTS:
                qb.setTables(MSCRMContract.PurchaseIntents.TABLE_NAME);
                qb.setProjectionMap(mPurchaseIntentsMap);
                break;
            case PURCHASE_INTENTS_ID:
                qb.setTables(MSCRMContract.PurchaseIntents.TABLE_NAME);
                qb.setProjectionMap(mPurchaseIntentsMap);
                qb.appendWhere(MSCRMContract.PurchaseIntents._ID + "=" + ContentUris.parseId(uri));
                break;
            case KB_CATEGORIES:
                qb.setTables(MSCRMContract.KbCategories.TABLE_NAME);
                qb.setProjectionMap(mKbCategories);
                break;
            case KB_CATEGORIES_ID:
                qb.setTables(MSCRMContract.KbCategories.TABLE_NAME);
                qb.setProjectionMap(mKbCategories);
                qb.appendWhere(MSCRMContract.KbCategories._ID + "=" + ContentUris.parseId(uri));
                break;
            case KB_ENTRYS:
                qb.setTables(MSCRMContract.KbEntrys.TABLE_NAME);
                qb.setProjectionMap(mKbEntrys);
                break;
            case KB_ENTRYS_ID:
                qb.setTables(MSCRMContract.KbEntrys.TABLE_NAME);
                qb.setProjectionMap(mKbEntrys);
                qb.appendWhere(MSCRMContract.KbEntrys._ID + "=" + ContentUris.parseId(uri));
                break;

            case ALL_FANS_INFO:
                qb.setTables(MSCRMContract.FansInfo.TABLE_NAME);
                qb.setProjectionMap(mAllFansInfoMap);
                break;

            case ALL_FANS_INFO_SEARCH:
                /*List<String> segments = uri.getPathSegments();*/
                String filter = "";
                /*if (segments.size() > 2) {
                    // 获取uri中的搜索参数
					filter = segments.get(2);
				}*/

                filter = uri.toString().replaceFirst(MSCRMContract.FansInfo.CONTENT_URI.toString() + "/search/", "");

                MyLogger.i(TAG, "filter: " + filter);

                StringBuilder whereClause = new StringBuilder();
                whereClause.append(MSCRMContract.FansInfo.TABLE_NAME);
                /**
                 * 如果搜索参数中包含中文，则与nickname name字段做匹配；
                 * 否则与pinyin_full_spell或pinyin_initial_spell字段做匹配
                 */
                if (TextUtils.isEmpty(filter)) {
                    // 当filter是空串时返回一个空的cursor
                    whereClause.append(" JOIN (SELECT NULL WHERE 0)");
                } else {

                    if (filter.contains("_") || filter.contains("%") || PinYinUtils.containsChinese(filter) || TextUtils.isEmpty(PinYinUtils.getInitialSpell(filter)) || TextUtils.isEmpty(PinYinUtils.getFullSpell(filter))) {
                        filter = filter.replace("_", "\\_");
                        filter = filter.replace("%", "\\%");
                        whereClause.append(" WHERE (" + MSCRMContract.FansInfo.NICKNAME + " LIKE '%" + filter + "%' OR " +
                                MSCRMContract.FansInfo.NAME + " LIKE '%" + filter + "%'");
                    } else {
                        whereClause.append(" WHERE ("
                                + MSCRMContract.FansInfo.NICKNAME_PINYIN_FULL_SPELL + " LIKE '%"
                                + PinYinUtils.getFullSpell(filter) + "%' OR "
                                + MSCRMContract.FansInfo.NICKNAME_PINYIN_INITIAL_SPELL + " LIKE '%"
                                + PinYinUtils.getInitialSpell(filter) + "%' OR "
                                + MSCRMContract.FansInfo.NAME_PINYIN_FULL_SPELL + " LIKE '%"
                                + PinYinUtils.getFullSpell(filter) + "%' OR "
                                + MSCRMContract.FansInfo.NAME_PINYIN_INITIAL_SPELL + " LIKE '%"
                                + PinYinUtils.getInitialSpell(filter) + "%' ");
                    }

                    if (filter.contains("_") || filter.contains("%")) {
                        whereClause.append(" ESCAPE '\\' ");
                    }
                    whereClause.append(")");
                }

                MyLogger.d(TAG, "search filter = " + whereClause);
                qb.setTables(whereClause.toString());
                qb.setProjectionMap(mAllFansInfoMap);
                break;

            case ALL_FANS_INFO_ID:
                qb.setTables(MSCRMContract.FansInfo.TABLE_NAME);
                qb.setProjectionMap(mAllFansInfoMap);
                qb.appendWhere(MSCRMContract.FansInfo._ID + "=" + ContentUris.parseId(uri));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // 打开数据库
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        // 按照设置的条件执行查询同时可以设置额外的查询条件
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // 通知该Uri的监听者数据发生变化了
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * 该方法用来进行大量数据的插入操作，通过将所有的插入操作放入一个事务(transaction)中，来提升插入操作的效率
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int count = 0;
        long start = System.currentTimeMillis();

        // 打开数据库
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // 开启数据库事务
        db.beginTransaction();
        try {
            // db.delete(getTable(uri), null, null);
            int num = values.length;
            for (int i = 0; i < num; i++) {
                if (db.insert(getTable(uri), null, values[i]) < 0) {
                    return count;
                }
            }
            // 设置事务操作执行成功
            db.setTransactionSuccessful();
        } finally {
            // 结束数据库事务，若事务操作没有成功，则rollback到之前状态。
            db.endTransaction();
        }
        count = values.length;

        long end = System.currentTimeMillis();
        Log.i(TAG, "bulk insert cost " + (end - start) + " ms");

        // 通知该Uri的监听者数据发生变化了
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // 打开数据库
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // 插入数据后返回该条数据的id
        long rowId = db.insert(getTable(uri), "", values);
        MyLogger.e(TAG, "rowId=" + rowId);
        /**
         * rowId大于0表示插入数据成功
         */
        if (rowId > 0) {
            Uri retUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(retUri, null);
            return retUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        int count = -1;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        count = db.delete(getTable(uri), where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        int count = -1;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        count = db.update(getTable(uri), values, whereClause, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * 对于用new Intent(String action, Uri uri)方法启动activity是很重要的，如果它返回的MIME
     * type和activity在<intent filter>中定义的data的MIME type不一致，将造成activity无法启动
     */
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case MESSAGE:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.MessageRecord.TABLE_NAME;
            case MESSAGE_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.MessageRecord.TABLE_NAME;
            case PULL_NOTE:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.MessageNote.TABLE_NAME;
            case PULL_NOTE_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.MessageNote.TABLE_NAME;
            case FANS:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FansRecord.TABLE_NAME;
            case FANS_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FansRecord.TABLE_NAME;
            case FANS_GROUP_COUNT:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FansGroupCount.TABLE_NAME;
            case FANS_GROUP_COUNT_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FansGroupCount.TABLE_NAME;
            case FIXED_FANS_GROUP_LIST:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FixedGroupFansList.TABLE_NAME;
            case FIXED_FANS_GROUP_LIST_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FixedGroupFansList.TABLE_NAME;
            case TARGET_LIST:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.TargetList.TABLE_NAME;
            case TARGET_LIST_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.TargetList.TABLE_NAME;
            case FANSINFO:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FanInfo.TABLE_NAME;
            case FANSINFO_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FanInfo.TABLE_NAME;
            case TARGET_LISTS:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.TargetLists.TABLE_NAME;
            case TARGET_LISTS_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.TargetLists.TABLE_NAME;
            case DEALS:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.Deals.TABLE_NAME;
            case DEALS_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.Deals.TABLE_NAME;
            case DEALS_DETAILS:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.DealsDetails.TABLE_NAME;
            case DEALS_DETAILS_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.DealsDetails.TABLE_NAME;
            case PURCHASE_INTENTS:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.PurchaseIntents.TABLE_NAME;
            case PURCHASE_INTENTS_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.PurchaseIntents.TABLE_NAME;
            case KB_CATEGORIES:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.KbCategories.TABLE_NAME;
            case KB_CATEGORIES_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.KbCategories.TABLE_NAME;
            case KB_ENTRYS:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.KbEntrys.TABLE_NAME;
            case KB_ENTRYS_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.KbEntrys.TABLE_NAME;
            case ALL_FANS_INFO:
                return VENDOR_TYPE_DIR + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FansInfo.TABLE_NAME;
            case ALL_FANS_INFO_ID:
                return VENDOR_TYPE_ITEM + "/" + VENDOR_SPECIFIC
                        + MSCRMContract.FansInfo.TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * 根据uri获取匹配的表名
     *
     * @param uri 数据库表的uri
     * @return 与该uri匹配的表名
     */
    private static String getTable(Uri uri) {
        String tableName = null;
        MyLogger.e(TAG, "uri.getPath()=" + uri.getPath());
        MyLogger.e(TAG, "mUriMatcher.match(uri) = " + mUriMatcher.match(uri));
        switch (mUriMatcher.match(uri)) {
            //消息记录
            case MESSAGE:
                tableName = MSCRMContract.MessageRecord.TABLE_NAME;
                break;
            //站内信
            case PULL_NOTE:
                tableName = MSCRMContract.MessageNote.TABLE_NAME;
                break;
            //粉丝
            case FANS:
                tableName = MSCRMContract.FansRecord.TABLE_NAME;
                break;
            //固定分组人数
            case FANS_GROUP_COUNT:
                tableName = MSCRMContract.FansGroupCount.TABLE_NAME;
                break;
            //固定分组粉丝列表
            case FIXED_FANS_GROUP_LIST:
                tableName = MSCRMContract.FixedGroupFansList.TABLE_NAME;
                break;
            //自定义分组列表
            case TARGET_LIST:
                tableName = MSCRMContract.TargetList.TABLE_NAME;
                break;
            //粉丝详情
            case FANSINFO:
                tableName = MSCRMContract.FanInfo.TABLE_NAME;
                break;
            //粉丝所在自定义分组
            case TARGET_LISTS:
                tableName = MSCRMContract.TargetLists.TABLE_NAME;
                break;
            //交易记录
            case DEALS:
                tableName = MSCRMContract.Deals.TABLE_NAME;
                break;
            //交易详情
            case DEALS_DETAILS:
                tableName = MSCRMContract.DealsDetails.TABLE_NAME;
                break;
            //粉丝购买意向
            case PURCHASE_INTENTS:
                tableName = MSCRMContract.PurchaseIntents.TABLE_NAME;
                break;
            //类别
            case KB_CATEGORIES:
                tableName = MSCRMContract.KbCategories.TABLE_NAME;
                break;
            //条目
            case KB_ENTRYS:
                tableName = MSCRMContract.KbEntrys.TABLE_NAME;
                break;
            //全局搜索粉丝信息
            case ALL_FANS_INFO:
                tableName = MSCRMContract.FansInfo.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return tableName;
    }

}
