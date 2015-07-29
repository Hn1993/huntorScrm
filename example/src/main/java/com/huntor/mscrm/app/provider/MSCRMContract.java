package com.huntor.mscrm.app.provider;

import android.content.Intent;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * EMenuManagerProvider的Contract，用来向外界暴露表的uri及列名
 */
public abstract class MSCRMContract {

    /**
     * ContentProvider的Authority
     */
    public static final String AUTHORITY = "com.huntor.mscrm.app.provider";

    private static final String BASE_CONTENT_URI = "content://" + AUTHORITY + "/";

    /**
     * 消息记录表
     */
    public interface MessageRecord extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "message";

        // 列名
        public static final String ARTIFICIAL_STATUS = "artificial_status";
        public static final String MSGID = "msg_id"; // 消息ID
        public static final String TYPE = "type"; //  消息类型(INTEGER)
        public static final String CONTENT = "content"; // 消息内容(TEXT)
        public static final String TIMESTAMP = "timestamp"; // 时间戳(TEXT)
        public static final String FID = "fid"; // 粉丝ID(INTEGER)
        public static final String EID = "eid"; // 员工ID(INTEGER)
        public static final String GROUP_ID = "group_id";//消息组ID
        public static final String ISREAD = "is_read"; // 消息状态
        public static final String SEND_OR_RECEIVE = "send_or_receive"; // 发送或接收
        public static final String SUCCESS_OR_FAIL = "successOrFail"; //发送消息状态：成功/失败
        public static final String TIME = "time"; //时间

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 站内信记录表
     */
    public interface MessageNote extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "messagenote";

        public static final String FROMUSER = "fromUser";
        public static final String DEST = "dest";
        public static final String TYPE = "type"; //  消息类型(INTEGER)
        public static final String CONTENT = "content"; // 消息内容(TEXT)
        public static final String TIME = "time"; //时间
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);

    }

    /**
     * 和导购交互过的粉丝
     */
    public interface FansRecord extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "fans";

        // 列名
        public static final String FID = "fid"; // FansID
        public static final String EID = "eid"; //EMPID
        public static final String AVATAR = "avatar"; // 粉丝头像
        public static final String NICK_NAME = "nick_name"; // 粉丝昵称
        public static final String REAL_NAME = "real_name"; // 真是姓名
        public static final String GENDER = "gender"; // 性别
        public static final String CITY = "city"; // 城市
        public static final String PROVINCE = "province"; // 省份
        public static final String FOLLOWSTATUS = "followstatus"; // 关注状态
        public static final String SUBSCRIBE_TIME = "subscribe_time"; // 关注时间
        public static final String LASTINTERACTION_TIME = "lastinteraction_time"; // 上次交互时间
        public static final String INTERACTION_TIMES = "interaction_times"; //交互次数
        public static final String TIME = "time";//时间
        public static final String GROUP = "grade";//当前粉丝所属固定分组

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 固定分组人数
     */
    public interface FansGroupCount extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "fans_group_count";

        public static final String GROUP_TYPE = "group_type";
        public static final String COUNT = "count";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 固定分组粉丝列表
     */
    public interface FixedGroupFansList extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "fans_list";

        public static final String ID = "id";
        public static final String GROUP = "group_type";
        public static final String TARGET_ID = "target_id";
        public static final String NICK_NAME = "nickName";
        public static final String NAME = "name";
        public static final String AVATAR = "avatar";
        public static final String REGIST_TIME = "registTime";
        public static final String SUBSCRIBE_TIME = "subscribeTime";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 自定义分组列表
     */
    public interface TargetList extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "target_list";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String COUNT = "count";
        public static final String UPDATE_TIME = "updateTime";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 粉丝详情信息
     */
    public interface FanInfo extends BaseColumns {
        public static final String TABLE_NAME = "fan_info";

        public static final String FAN_ID = "fanId";
        public static final String NICKNAME = "nickName";
        public static final String CITY = "city";
        public static final String PROVINCE = "province";
        public static final String FOLLOW_STATUS = "followStatus";
        public static final String SUBSCRIBE_TIME = "subscribeTime";
        public static final String LAST_INTER_ACTION_TIME = "lastInteractionTime";
        public static final String INTER_ACTION_TIMES = "interactionTimes";
        public static final String AVATAR = "avatar";
        public static final String OCCUPATION = "occupation";
        public static final String ACCOUNT_ID = "accountId";
        public static final String REALNAME = "realName";
        public static final String GENDER = "gender";
        public static final String PHONE1 = "phone1";
        public static final String PHONE2 = "phone2";
        public static final String PHONE3 = "phone3";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 粉丝所在自定义分组信息
     */
    public interface TargetLists extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "target_lists";

        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String ACCOUNT_ID = "accountId";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 交易记录
     */
    public interface Deals extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "deals";

        public static final String ID = "id"; //交易ID
        public static final String MONEY = "money";
        public static final String DEALTIME = "dealTime";
        public static final String ACCOUNT_ID = "accountId";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 交易详情
     */
    public interface DealsDetails extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "deals_details";

        public static final String PRODUCT_ID = "productId"; //交易ID
        public static final String PRODUCT_NAME = "productName";//产品名
        public static final String AMOUNT = "amount";
        public static final String SN = "sn";
        public static final String DEAL_ID = "deal_id";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    public interface PurchaseIntents extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "purchase_intents";

        public static final String ID = "id"; //交易ID
        public static final String PRODUCT_ID = "productId";//产品名
        public static final String PRODUCT_NAME = "productName";
        public static final String DESC = "desc";
        public static final String INTENT_TIME = "intentTime";
        public static final String ACCOUNT_ID = "accountId";

        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 知识话术
     * MSCRM知识库分类
     */
    public interface KbCategories extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "categories";

        public static final String ID = "id"; //分类ID
        public static final String PARENTID = "parentId";//父分类ID
        public static final String NAME = "name"; //分类名称
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 某知识库分类下的具体条目
     */
    public interface KbEntrys extends BaseColumns {
        // 表名
        public static final String TABLE_NAME = "kb_entrys";

        public static final String ID = "id"; //知识库条目id
        public static final String TITLE = "title";//标题
        public static final String CONTENT = "content";//内容
        public static final String CATEGORIES_ID = "categories_id"; //类别ID
        public static final Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

    /**
     * 所有粉丝信息（全局搜索）
     */
    public interface FansInfo extends BaseColumns {
        String TABLE_NAME = "all_fansInfo";

        String ID = "id";
        String GROUP_ID = "groupId";
        String TARGET_ID = "targetId";
        String NICKNAME = "nickName";
        String NAME = "name";
        String AVATAR = "avatar";
        String REGIST_TIME = "registTime";
        String SUBSCRIBE_TIME = "subscribeTime";
        String ISCHECK = "isCheck";
        String NICKNAME_PINYIN_FULL_SPELL = "nickname_pinyin_full_spell";
        String NICKNAME_PINYIN_INITIAL_SPELL = "nickname_pinyin_initial_spell";
        String NAME_PINYIN_FULL_SPELL = "name_pinyin_full_spell";
        String NAME_PINYIN_INITIAL_SPELL = "name_pinyin_initial_spell";


        Uri CONTENT_URI = Uri.parse(BASE_CONTENT_URI + TABLE_NAME);
    }

}
