package com.huntor.mscrm.app2.utils;

/**
 * Created by IDEA
 * User : SL
 * on 2015/4/29 0029
 * 11:15.
 */

/**
 * 常用变量
 */
public final class Constant {
    //==============强制登出标示===================
    public static final String LOGINOUT = "LOGINOUT";
    public static final int LOGINOUT_FLAG = 100;
    public static final boolean DEBUG = true;
    public static final String IMAGE_CACHE_PATH = "/mscrm/"; // 缓存图片路径
    public static final String VOICE_CACHE_PATH = "/mscrm/voice/";//缓存声音路径
    public static final int IMAGE_CACHE_SIZE = 20 * 1024 * 1024;//缓存大小

    /**
     * 默认初始密码
     */
    public static final String DEFAULT_INIT_PSW = "abcd1234";
    public static final String INTERACTION = "interaction";
    public static final String INTERACTION_ONLINE = "interaction_online";
    public static final String MY_MEMBER = "mymember";
    public static final String SEARCH = "search";
    public static final String LAST_REFRESH_ALLFANS_TIME = "last_refresh_allfans_time";
    public static final String HAS_UPDATE = "has_update";
    public static final String CUSTOM_GROUP = "custom_group";
    public static final String ADM_FANS = "adm_fans";
    public static final String GROUP_MEMBER = "group_member";
    public static final String INTERACTION_INTENT_DATA = "interaction";
    public static final String ENTER_BY_BUY = "购买入会";
    public static final String ENTER_INVITATION = "入会邀请";
    public static final String NEW_FANS = "新增粉丝";
    public static final String NORMAL_FANS = "普通粉丝";
    public static final String HIGH_USER = "高潜消费者";
    public static final String BUY_USER = "已购用户";
    public static final String MY_GROUP = "我的分组";
    public static final String LOGIN_INFO_PREFERENCES_NAME = "LOGIN_INFO";
    public static final String EMPLOYEE_INFO_PREFERENCES_NAME = "EMPLOYEE_INFO";
    public static final String FANS_ID = "粉丝ID";
    public static final String ADD_TO_MY_GROUP = "加入分组";

    /**
     * SharedPreferenc
     */
    public static final String PREFERENCE_FILE = "mscrm_preference";  // sharedpreferenc文件名
    public static final String PREFERENCE_QRCODE = "qrcode";
    public static final String PREFERENCE_NUMBER = "number";
    public static final String PREFERENCE_PSW = "psw";
    public static final String PREFERENCE_USER_ID = "userId";
    public static final String PREFERENCE_EMP_ID = "empId";
    public static final String PREFERENCE_EMP_NAME = "empName";
    public static final String PREFERENCE_CURRENT_TIME = "currentTime";
    public static final String PREFERENCES_SOCIAL_ID = "socialId";
    public static final String PREFERENCES_SAVE_TIME = "saveTime";
    public static final String PREFERENCE_TOKEN = "token";
    public static final String PREFERENCE_TOKEN_VERIFY = "token_verify";
    public static final long HOURS_24 = 1000 * 60 * 60 * 24;
    public static final long HOURS_48 = 1000 * 60 * 60 * 24 * 2;
    public static final String DES_KEY = "huntor2011"; // DES加密使用的key
    public static final String LAST_TIME = "lastTime";
    public static final String ADAPTER_COUNT = "AdapterCount";

    /***
     * 上传文件地址
     */
    public static final String HTTP_UP_LOAD_FILE_URL = "http://58.67.199.171:8220/Envelope/upload/body";

    /***
     * 在线交互长连接 地址。。。
     * 58.67.199.171
     * 192.168.1.61
     */
  //public static final String SOCKET_TCP_LONG_HOST_URL = "http://scrmapp.vivo.com.cn:83"; //	生产域名
    //public static final String SOCKET_TCP_LONG_HOST_URL = "http://58.67.204.13:29092"; //	生产chatter外网ip
     public static final String SOCKET_TCP_LONG_HOST_URL = "http://58.67.199.171:29092"; //测试地址


    /**
     * Http请求的Url
     * http://58.67.199.171:86/MobileBusiness/
     * http://192.168.1.126:8085/MobileBusiness
     * http://192.168.1.169:8085/MobileBusiness
     */
   // public static final String HTTP_ROOT_URL = "http://scrmapp.vivo.com.cn:82/MobileBusiness"; // 服务器地址
     public static final String HTTP_ROOT_URL = "http://58.67.199.171:86/MobileBusiness"; // 测试服务器地址


    /**
     * POST请求
     * userName String
     * pwd String(md5)
     * <li>
     * <ol>code	结果状态码	String	正常结果为0</ol>
     * <ol>msg	结果状态信息	String	正常结果为OK</ol>
     * <ol>userId	用户id	int</ol>
     * <ol>empId	员工id	int</ol>
     * <ol>empName	员工姓名	String</ol>
     * </li>
     */
    public static final String HTTP_REQUEST_USER_LOGIN = HTTP_ROOT_URL + "/user/login";//员工登陆地址
    public static final String HTTP_REQUEST_USER_RESET_PWD_REQ = HTTP_ROOT_URL + "/user/reset_pwd/req";//请求验证码
    public static final String HTTP_REQUEST_USER_RESET_PWD_VERIFY = HTTP_ROOT_URL + "/user/reset_pwd/verify";//验证短信验证码
    public static final String HTTP_REQUEST_USER_RESET_PWD_DONE = HTTP_ROOT_URL + "/user/reset_pwd/done";//完成重置密码
    public static final String HTTP_REQUEST_FLUSH_CACHE = HTTP_ROOT_URL + "/common/flush_cache";//手工刷新业务层缓存
    public static final String HTTP_REQUEST_PRODUCTS = HTTP_ROOT_URL + "/common/products";//获取MSCRM产品列表
    public static final String HTTP_REQUEST_CONVERSATIONS = HTTP_ROOT_URL + "/employee/conversations";//查询导购与粉丝的会话列表
    public static final String HTTP_REQUEST_QR_CREATE = HTTP_ROOT_URL + "/employee/product/qr/create";//生成员工卖出产品的注册二维码。该码用于顾客扫描进入注册页
    public static final String HTTP_REQUEST_EMPLOYEE = HTTP_ROOT_URL + "/employee"; //员工基础信息查询
    public static final String HTTP_REQUEST_FANS_DEAL_CREATE = HTTP_ROOT_URL + "/fans/deal/create"; //粉丝创建一条交易
    public static final String HTTP_REQUEST_FANS_GROUP_COUNT = HTTP_ROOT_URL + "/fans/group/count"; //各固定分组人数查询
    public static final String HTTP_REQUEST_FANS_GROUP = HTTP_ROOT_URL + "/fans/group"; //查询员工各个固定分组（新增、普通、高潜、已购）中的粉丝列表
    public static final String HTTP_REQUEST_FANS_ADD_PURCHASE_INTENTS = HTTP_ROOT_URL + "/fans/purchase_intents/create"; //粉丝新增购买意向
    public static final String HTTP_REQUEST_FANS_DELETE_PURCHASE_INTENTS = HTTP_ROOT_URL + "/fans/purchase_intents/delete"; //粉丝删除一个购买意向
    public static final String HTTP_REQUEST_FANS_TARGETLIST_ADD = HTTP_ROOT_URL + "/fans/targetlist/add"; //向员工自定义粉丝分组中添加一个粉丝
    public static final String HTTP_REQUEST_FANS_TARGETLIST_CREATE = HTTP_ROOT_URL + "/fans/targetlist/create"; //创建一个新的自定义粉丝分组
    public static final String HTTP_REQUEST_FANS_TARGETLIST_DELETE = HTTP_ROOT_URL + "/fans/targetlist/delete"; //删除员工的一个自定义分组
    public static final String HTTP_REQUEST_FANS_TARGETLIST_LIST = HTTP_ROOT_URL + "/fans/targetlist/list"; //查询员工的一个自定义粉丝分组内的粉丝列表
    public static final String HTTP_REQUEST_FANS_TARGETLIST_REMOVE = HTTP_ROOT_URL + "/fans/targetlist/remove"; //从员工的自定义粉丝分组中移除一个粉丝
    public static final String HTTP_REQUEST_FANS_TARGETLIST_UPDATE = HTTP_ROOT_URL + "/fans/targetlist/update"; //员工自定义分组 编辑
    public static final String HTTP_REQUEST_FANS_TARGETLISTS = HTTP_ROOT_URL + "/fans/targetlists"; //查询员工的自定义分组列表
    public static final String HTTP_REQUEST_FANS = HTTP_ROOT_URL + "/fans"; //获取粉丝的详细信息
    public static final String HTTP_REQUEST_REALTIME_CLOSE = HTTP_ROOT_URL + "/realtime/close"; //关闭导购与粉丝的实时交互
    public static final String HTTP_REQUEST_REALTIME_HISTORY = HTTP_ROOT_URL + "/realtime/history"; //查询导购与粉丝交互的历史消息
    public static final String HTTP_REQUEST_REALTIME_KNOWLEDGE_CATEGORIE_CONTENTS = HTTP_ROOT_URL + "/realtime/knowledge/categorie/contents"; //查询某知识库分类下的具体条目
    public static final String HTTP_REQUEST_REALTIME_KNOWLEDGE_CATEGORIE = HTTP_ROOT_URL + "/realtime/knowledge/categories"; //查询某知识库分类下的具体条目
    public static final String HTTP_REQUEST_REALTIME_OPEN = HTTP_ROOT_URL + "/realtime/open"; //开启导购与粉丝的实时交互
    public static final String HTTP_REQUEST_REALTIME_SEND = HTTP_ROOT_URL + "/realtime/send"; //发送消息
    public static final String HTTP_REQUEST_MODIFY_PWD = HTTP_ROOT_URL + "/user/modify_pwd";//修改密码
    public static final String HTTP_REQUEST_FANS_STTUA_NEWTWOOLD = HTTP_ROOT_URL + "/fans/status/new2old";//更改粉丝状态：新增改为非新增
    public static final String HTTP_REQUEST_FANS_LIST = HTTP_ROOT_URL + "/fans/list";//批量查询一批粉丝的信息
    public static final String HTTP_REQUEST_FANS_MODIFY = HTTP_ROOT_URL + "/fans/modify";//粉丝详情修改
    public static final String HTTP_REQUEST_ALL_FANS_ID_LIST = HTTP_ROOT_URL + "/fans/targetlist/listall/only_id"; //查询所有粉丝 不分页

    public static final String BAR_CODE_CONTENT = "bar_code_content";

    // 缓存文件夹
    public static final String CACHE_DIR = "mscrm/";

    public static final String LOGIN_IS_AUTO_LOGIN = "isAutoLogin";
    public static final String LOGIN_AUTO_TRUE = "true";
    public static final String LOGIN_REMEMBER_TRUE = "true";
    public static final String LOGIN_IS_REMEMBER_PSW = "isRememberPsw";
    public static final String LOGIN_REMEMBER_FALSE = "false";
    public static final String FALSE = "false";
    public static final String CHAT_EXTRA_INTENT_TITLE = "title";//用于在知识库跳转到详情界面时的title
    public static final String CHAT_EXTRA_INTENT_CONTENT = "content";//在知识库跳转到详情界面时传递的内容
    public static final String CHAT_CONTACT_NAME = "ChatContactName";//在聊天列表跳转到聊天界面时传递的聊天对象名称
    public static final String CHAT_CONTACT_HEAD = "contactHead";//聊天时传递的用户头像地址
    public static final String CHAT_CONTACT_FAN_ID = "ChatFanId";//聊天时传递的对象粉丝ID

    /**
     * 微信替换
     */
    public static final String[] WEIXIN_CHAR = {"/::)", "/::~", "/::B", "/::|", "/:8-)", "/::<", "/::$", "/::X", "/::Z", "/::'(", "/::-|", "/::@", "/::P", "/::D", "/::O", "/::(", "/::+", "/:--b", "/::Q", "/::T", "/:,@P", "/:,@-D", "/::d", "/:,@o", "/::g", "/:|-)", "/::!", "/::L", "/::>", "/::,@", "/:,@f", "/::-S", "/:?", "/:,@x", "/:,@@", "/::8", "/:,@!", "/:!!!", "/:xx", "/:bye", "/:wipe", "/:dig", "/:handclap", "/:&-(", "/:B-)", "/:<@", "/:@>", "/::-O", "/:>-|", "/:P-(", "/::'|", "/:X-)", "/::*", "/:@x", "/:8*", "/:pd", "/:<W>", "/:beer", "/:basketb", "/:oo", "/:coffee", "/:eat", "/:pig", "/:rose", "/:fade", "/:showlove", "/:heart", "/:break", "/:cake", "/:li", "/:bome", "/:kn", "/:footb", "/:ladybug", "/:shit", "/:moon", "/:sun", "/:gift", "/:hug", "/:strong", "/:weak", "/:share", "/:v", "/:@)", "/:jj", "/:@@", "/:bad", "/:lvu", "/:no", "/:ok", "/:love", "/:<L>", "/:jump", "/:shake", "/:<O>", "/:circle", "/:kotow", "/:turn", "/:skip", "/:oY"};
    public static final String[] WEIXIN_IMAGE = {"[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]", "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]", "[尴尬]", "[发怒]", "[调皮]", "[呲牙]", "[惊讶]", "[难过]", "[酷]", "[冷汗]", "[抓狂]", "[吐]", "[偷笑]", "[愉快]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]", "[流汗]", "[憨笑]", "[悠闲]", "[奋斗]", "[咒骂]", "[疑问]", "[嘘]", "[晕]", "[疯了]", "[衰]", "[骷髅]", "[敲打]", "[再见]", "[擦汗]", "[抠鼻]", "[鼓掌]", "[糗大了]", "[坏笑]", "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]", "[快哭了]", "[阴险]", "[亲亲]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]", "[啤酒]", "[篮球]", "[乒乓]", "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]", "[嘴唇]", "[爱心]", "[心碎]", "[蛋糕]", "[闪电]", "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]", "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]", "[弱]", "[握手]", "[胜利]", "[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]", "[爱情]", "[飞吻]", "[跳跳]", "[发抖]", "[怄火]", "[转圈]", "[磕头]", "[回头]", "[跳绳]", "[投降]"};
    public static final String CHAT_TYPE_TEXT = "1";//聊天内容类型:文本
    public static final String CHAT_TYPE_VOICE = "3";//聊天：音频
    public static final String CHAT_TYPE_IMAGE = "2";//图片
    public static final String CHAT_TYPE_SHAKE = "4";//摇一摇
    public static final String CHAT_TYPE_PUSHVOICE = "5";
    public static final String CHAT_INTENT_PUSH_MESSAGE = "pushMessage";
    public static final String MAIN_INTENT_DATA = "MainIntentData";//侧滑界面跳转到其他界面时传递的key
    public static final String CHAT_TAILS = "ChatTails";//小尾巴
    public static int notecount = 0;//消息中心
    public static int shakecount = 0;//摇一摇消息中心
    public static final String PREFERENCE_DEST = "dest";
}
