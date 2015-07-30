package com.huntor.mscrm.app2.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;

/**
 * 数据库操作的一个辅助类，方便数据库的开启和升级
 */
public class MSCRMDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "MSCRMDBHelper";

    private static final String DB_NAME = "mscrm.db"; // 数据库名称
    private static final int DB_VERSION = 16; // 数据库版本号(修改数据库结构时需要增加版本号，以便老版本应用升级)

    public MSCRMDBHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    public MSCRMDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 当数据库不存在时执行onCreate()
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 消息表
        db.execSQL("CREATE TABLE " + MSCRMContract.MessageRecord.TABLE_NAME + "("
                + MSCRMContract.MessageRecord._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.MessageRecord.ARTIFICIAL_STATUS + " INTEGER,"
                + MSCRMContract.MessageRecord.MSGID + " INTEGER,"
                + MSCRMContract.MessageRecord.TYPE + " TEXT,"
                + MSCRMContract.MessageRecord.CONTENT + " TEXT,"
                + MSCRMContract.MessageRecord.TIMESTAMP + " LONG,"
                + MSCRMContract.MessageRecord.TIME + " LONG,"
                + MSCRMContract.MessageRecord.FID + " INTEGER,"
                + MSCRMContract.MessageRecord.EID + " INTEGER,"
                + MSCRMContract.MessageRecord.ISREAD + " INTEGER,"
                + MSCRMContract.MessageRecord.GROUP_ID + " INTEGER,"
                + MSCRMContract.MessageRecord.SUCCESS_OR_FAIL + " INTEGER,"
                + MSCRMContract.MessageRecord.SEND_OR_RECEIVE + " INTEGER)");
        //站内信表
        db.execSQL("CREATE TABLE " + MSCRMContract.MessageNote.TABLE_NAME + "("
                + MSCRMContract.MessageNote._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.MessageNote.FROMUSER + " INTEGER,"
                + MSCRMContract.MessageNote.DEST + " INTEGER,"
                + MSCRMContract.MessageNote.TYPE + " TEXT,"
                + MSCRMContract.MessageNote.CONTENT + " TEXT,"
                + MSCRMContract.MessageNote.TIME + " LONG"
                + ")");
        // 粉丝表
        db.execSQL("CREATE TABLE " + MSCRMContract.FansRecord.TABLE_NAME + "("
                + MSCRMContract.FansRecord._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.FansRecord.FID + " INTEGER,"
                + MSCRMContract.FansRecord.EID + " INTEGER,"
                + MSCRMContract.FansRecord.NICK_NAME + " TEXT,"
                + MSCRMContract.FansRecord.AVATAR + " TEXT,"
                + MSCRMContract.FansRecord.REAL_NAME + " TEXT,"
                + MSCRMContract.FansRecord.GENDER + " TEXT,"
                + MSCRMContract.FansRecord.CITY + " TEXT,"
                + MSCRMContract.FansRecord.PROVINCE + " TEXT,"
                + MSCRMContract.FansRecord.FOLLOWSTATUS + " INTEGER,"
                + MSCRMContract.FansRecord.SUBSCRIBE_TIME + " LONG,"
                + MSCRMContract.FansRecord.LASTINTERACTION_TIME + " LONG,"
                + MSCRMContract.FansRecord.TIME + " LONG,"
                + MSCRMContract.FansRecord.GROUP + " INTEGER,"
                + MSCRMContract.FansRecord.INTERACTION_TIMES + " INTEGER)");

        // 固定分组人数
        db.execSQL("CREATE TABLE " + MSCRMContract.FansGroupCount.TABLE_NAME + "("
                + MSCRMContract.FansGroupCount._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.FansGroupCount.GROUP_TYPE + " INTEGER,"
                + MSCRMContract.FansGroupCount.COUNT + " INTEGER)");

        // 固定分组粉丝列表
        db.execSQL("CREATE TABLE " + MSCRMContract.FixedGroupFansList.TABLE_NAME + "("
                + MSCRMContract.FixedGroupFansList._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.FixedGroupFansList.ID + " INTEGER,"
                + MSCRMContract.FixedGroupFansList.GROUP + " INTEGER,"
                + MSCRMContract.FixedGroupFansList.TARGET_ID + " INTEGER,"
                + MSCRMContract.FixedGroupFansList.AVATAR + " TEXT,"
                + MSCRMContract.FixedGroupFansList.NICK_NAME + " TEXT,"
                + MSCRMContract.FixedGroupFansList.NAME + " TEXT,"
                + MSCRMContract.FixedGroupFansList.REGIST_TIME + " LONG,"
                + MSCRMContract.FixedGroupFansList.SUBSCRIBE_TIME + " LONG)");

        //自定义粉丝分组
        db.execSQL("CREATE TABLE " + MSCRMContract.TargetList.TABLE_NAME + "("
                + MSCRMContract.TargetList._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.TargetList.ID + " INTEGER,"
                + MSCRMContract.TargetList.NAME + " TEXT,"
                + MSCRMContract.TargetList.COUNT + " INTEGER,"
                + MSCRMContract.TargetList.UPDATE_TIME + " LONG)");

        //粉丝详细信息
        db.execSQL("CREATE TABLE " + MSCRMContract.FanInfo.TABLE_NAME + "("
                + MSCRMContract.FanInfo._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.FanInfo.FAN_ID + " INTEGER,"
                + MSCRMContract.FanInfo.NICKNAME + " TEXT,"
                + MSCRMContract.FanInfo.CITY + " TEXT,"
                + MSCRMContract.FanInfo.PROVINCE + " TEXT,"
                + MSCRMContract.FanInfo.FOLLOW_STATUS + " INTEGER,"
                + MSCRMContract.FanInfo.SUBSCRIBE_TIME + " LONG,"
                + MSCRMContract.FanInfo.LAST_INTER_ACTION_TIME + " LONG,"
                + MSCRMContract.FanInfo.INTER_ACTION_TIMES + " INTEGER,"
                + MSCRMContract.FanInfo.AVATAR + " TEXT,"
                + MSCRMContract.FanInfo.ACCOUNT_ID + " INTEGER,"
                + MSCRMContract.FanInfo.REALNAME + " TEXT,"
                + MSCRMContract.FanInfo.GENDER + " TEXT,"
                + MSCRMContract.FanInfo.OCCUPATION + " TEXT,"
                + MSCRMContract.FanInfo.PHONE1 + " TEXT,"
                + MSCRMContract.FanInfo.PHONE2 + " TEXT,"
                + MSCRMContract.FanInfo.PHONE3 + " TEXT)");

        //粉丝所在自定义分组
        db.execSQL("CREATE TABLE " + MSCRMContract.TargetLists.TABLE_NAME + "("
                + MSCRMContract.TargetLists._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.TargetList.ID + " INTEGER,"
                + MSCRMContract.TargetLists.NAME + " TEXT,"
                + MSCRMContract.TargetLists.ACCOUNT_ID + " INTEGER)");

        //交易记录
        db.execSQL("CREATE TABLE " + MSCRMContract.Deals.TABLE_NAME + "("
                + MSCRMContract.Deals._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.Deals.ID + " INTEGER,"
                + MSCRMContract.Deals.MONEY + " DOUBLE,"
                + MSCRMContract.Deals.DEALTIME + " LONG,"
                + MSCRMContract.Deals.ACCOUNT_ID + " INTEGER)");

        //交易详情
        db.execSQL("CREATE TABLE " + MSCRMContract.DealsDetails.TABLE_NAME + "("
                + MSCRMContract.DealsDetails._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.DealsDetails.PRODUCT_ID + " INTEGER,"
                + MSCRMContract.DealsDetails.PRODUCT_NAME + " TEXT,"
                + MSCRMContract.DealsDetails.AMOUNT + " INTEGER,"
                + MSCRMContract.DealsDetails.SN + " TEXT,"
                + MSCRMContract.DealsDetails.DEAL_ID + " INTEGER)");

        db.execSQL("CREATE TABLE " + MSCRMContract.PurchaseIntents.TABLE_NAME + "("
                + MSCRMContract.PurchaseIntents._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.PurchaseIntents.ID + " INTEGER,"
                + MSCRMContract.PurchaseIntents.PRODUCT_ID + " INTEGER,"
                + MSCRMContract.PurchaseIntents.PRODUCT_NAME + " TEXT,"
                + MSCRMContract.PurchaseIntents.DESC + " TEXT,"
                + MSCRMContract.PurchaseIntents.INTENT_TIME + " LONG,"
                + MSCRMContract.PurchaseIntents.ACCOUNT_ID + " INTEGER)");

        //知识话术 类别
        db.execSQL("CREATE TABLE " + MSCRMContract.KbCategories.TABLE_NAME + "("
                + MSCRMContract.KbCategories._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.KbCategories.ID + " INTEGER,"
                + MSCRMContract.KbCategories.PARENTID + " INTEGER,"
                + MSCRMContract.KbCategories.NAME + " TEXT)");

        db.execSQL("CREATE TABLE " + MSCRMContract.KbEntrys.TABLE_NAME + "("
                + MSCRMContract.KbEntrys._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.KbEntrys.ID + " INTEGER,"
                + MSCRMContract.KbEntrys.TITLE + " TEXT,"
                + MSCRMContract.KbEntrys.CONTENT + " TEXT,"
                + MSCRMContract.KbEntrys.CATEGORIES_ID + " INTEGER)");

        //全局搜索粉丝表
        db.execSQL("CREATE TABLE " + MSCRMContract.FansInfo.TABLE_NAME + "("
                + MSCRMContract.FansInfo._ID + " INTEGER PRIMARY KEY,"
                + MSCRMContract.FansInfo.ID + " INTEGER,"
                + MSCRMContract.FansInfo.GROUP_ID + " INTEGER,"
                + MSCRMContract.FansInfo.TARGET_ID + " INTEGER,"
                + MSCRMContract.FansInfo.NICKNAME + " TEXT,"
                + MSCRMContract.FansInfo.NAME + " TEXT,"
                + MSCRMContract.FansInfo.AVATAR + " TEXT,"
                + MSCRMContract.FansInfo.REGIST_TIME + " LONG,"
                + MSCRMContract.FansInfo.SUBSCRIBE_TIME + " LONG,"
                + MSCRMContract.FansInfo.ISCHECK + " INTEGER,"
                + MSCRMContract.FansInfo.NICKNAME_PINYIN_FULL_SPELL + " TEXT,"
                + MSCRMContract.FansInfo.NICKNAME_PINYIN_INITIAL_SPELL + " TEXT,"
                + MSCRMContract.FansInfo.NAME_PINYIN_FULL_SPELL + " TEXT,"
                + MSCRMContract.FansInfo.NAME_PINYIN_INITIAL_SPELL + " TEXT)");

    }

    /**
     * 当版本号升级时执行onUpgrade()
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (Constant.DEBUG) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
        }

        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.MessageRecord.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.FansRecord.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.FansGroupCount.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.TargetList.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.FixedGroupFansList.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.FanInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.TargetLists.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.Deals.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.DealsDetails.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.PurchaseIntents.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.KbCategories.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.KbEntrys.TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.FansInfo.TABLE_NAME);//v15

        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.MessageNote.TABLE_NAME);

        //删除摇一摇
        db.execSQL("DROP TABLE IF EXISTS " + MSCRMContract.Shake.TABLE_NAME);

        onCreate(db);
    }

}
