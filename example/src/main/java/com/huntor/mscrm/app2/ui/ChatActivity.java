package com.huntor.mscrm.app2.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.*;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.ChatLVAdapter;
import com.huntor.mscrm.app2.adapter.FaceGVAdapter;
import com.huntor.mscrm.app2.adapter.FaceVPAdapter;
import com.huntor.mscrm.app2.model.*;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFans;
import com.huntor.mscrm.app2.net.api.ApiRealTimeHistory;
import com.huntor.mscrm.app2.net.api.ApiUpload;
import com.huntor.mscrm.app2.provider.api.ApiAllFansInfoDb;
import com.huntor.mscrm.app2.provider.api.ApiFansRecordDb;
import com.huntor.mscrm.app2.provider.api.ApiMessageRecordDb;
import com.huntor.mscrm.app2.push.PushMessageManager;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.DropdownListView;
import com.huntor.mscrm.app2.ui.component.MyEditText;
import com.huntor.mscrm.app2.utils.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, DropdownListView.OnRefreshListenerHeader {

    private static final String TAG = "ChatActivity";
    public static final int PAGE_SIZE = 200;
    public static final int PAGE_NUM = 1;
    public static int PAGE_NUM_AGAIN = 1;
    private ChatActivity context = ChatActivity.this;
    private RelativeLayout layoutExtra;
    private int visibility;

    private ViewPager mViewPager;
    private LinearLayout mDotsLayout;
    private MyEditText input;
    private DropdownListView mListView;
    private ChatLVAdapter mLvAdapter;

    private LinearLayout chat_face_container;
    private ImageView image_face;//表情图标
    // 7列3行
    private int columns = 6;
    private int rows = 4;
    private List<View> views = new ArrayList<View>();
    private List<String> staticFacesList;
    private List<MessageRecordModel> infos;
    private SimpleDateFormat sd;

    private String reply = "";//模拟回复
    private int fan_id;
    private TextView send;
    private ImageView textExtra;
    private PushMessageManager messageManager;
    private ImageView imgHead;
    private String imgHeadUrl;
    private String title;
    private int groupId;
    //==========================================
    private TextView txtChatExtraimg;
    private ImageView imgetest;//此控件仅供测试使用，到时删除
    private ImageView imgRecord;
    private TextView btn_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        infos = new ArrayList<MessageRecordModel>();

        initStaticFaces();

        initIntentData();

        initViews();

        initPrimitiveData();

        messageManager = PushMessageManager.getInstance(this);

        MyLogger.i(TAG, "注册");
        messageManager.registerOnReceivedPushMessageListener(opl);
        MyLogger.d("----fan_id", fan_id + "-");
        messageManager.setCurrentFid(fan_id);

        NotificationUtils notificationUtils = NotificationUtils.getInstance(this);
        notificationUtils.cancleById(fan_id);
    }

    /**
     * 初始化界面
     */
    @SuppressLint("SimpleDateFormat")
    private void initViews() {
        mListView = (DropdownListView) findViewById(R.id.message_chat_listview);
        sd = new SimpleDateFormat("MM-dd HH:mm");
        MyLogger.e(TAG, "initViews info=" + infos.toString());
        mLvAdapter = new ChatLVAdapter(this, infos, mHandler);
        mListView.setAdapter(mLvAdapter);
        //表情图标
        image_face = (ImageView) findViewById(R.id.image_face);
        //表情布局
        chat_face_container = (LinearLayout) findViewById(R.id.chat_face_container);
        mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
        mViewPager.setOnPageChangeListener(new PageChange());
//        表情下小圆点
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
        RelativeLayout layoutChatTitle = (RelativeLayout) findViewById(R.id.layout_chat_title);
        input = (MyEditText) findViewById(R.id.edit_chat_send);
        input.setOnClickListener(this);
        send = (TextView) findViewById(R.id.btn_chat_send);
        InitViewPager();
        //表情按钮
        image_face.setOnClickListener(this);
        // 发送
        layoutChatTitle.setOnClickListener(this);
        send.setOnClickListener(this);

        mListView.setOnRefreshListenerHead(this);
        mListView.setOnTouchListener(this);
        input.setOnTouchListener(this);

        /**************输入框监听事件************/
        input.addTextChangedListener(watcher);
    }

    /**
     * 界面内容的初始化
     */
    private void initPrimitiveData() {
        layoutExtra = (RelativeLayout) findViewById(R.id.layout_chat_extra);//知识库界面
        textExtra = (ImageView) findViewById(R.id.text_chat_extra);
        TextView btnSend = (TextView) findViewById(R.id.btn_chat_send);
//        ListView listContent = (ListView) findViewById(R.id.list_chat_content);
        RelativeLayout imgLeft = (RelativeLayout) findViewById(R.id.img_left_corner);
        TextView txtChatExtraKnowledgeWords = (TextView) findViewById(R.id.text_chat_extra_knowledge_words);
        imgLeft.setVisibility(View.VISIBLE);
        visibility = layoutExtra.getVisibility();
        btnSend.setOnClickListener(this);
//        listContent.setOnTouchListener(this);
        textExtra.setOnClickListener(this);
        imgLeft.setOnClickListener(this);
        txtChatExtraKnowledgeWords.setOnClickListener(this);


        /******发送图片按钮*****/
        txtChatExtraimg = (TextView) findViewById(R.id.text_chat_extra_img);
        txtChatExtraimg.setOnClickListener(this);
        imgetest = (ImageView) this.findViewById(R.id.image_chat_extra);
        imgetest.setVisibility(View.GONE);
        imgRecord = (ImageView) this.findViewById(R.id.image_recording);
        imgRecord.setOnClickListener(this);
        btn_record = (TextView) this.findViewById(R.id.tv_record);
    }

    /*
     * 初始表情
     */
    private void InitViewPager() {
        // 获取页数
        for (int i = 0; i < getPagerCount(); i++) {
            views.add(viewPagerItem(i));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mViewPager.setAdapter(mVpAdapter);
//        mDotsLayout.getChildAt(0).setSelected(true);
    }

    private ImageView dotsItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    /**
     * 根据表情数量以及GridView设置的行数和列数计算Pager数量
     *
     * @return
     */
    private int getPagerCount() {
        int count = staticFacesList.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }

    /**
     * 表情页改变时，dots效果也要跟着改变
     */
    class PageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(arg0).setSelected(true);
        }

    }

    private View viewPagerItem(int position) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.face_gridview, null);//表情布局
        GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
         * */
        List<String> subList = new ArrayList<String>();
        subList.addAll(staticFacesList
                .subList(position * (columns * rows - 1),
                        (columns * rows - 1) * (position + 1) > staticFacesList
                                .size() ? staticFacesList.size() : (columns
                                * rows - 1)
                                * (position + 1)));
        /**
         * 末尾添加删除图标
         * */
        subList.add("emotion_del_normal.png");
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String png = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
                    if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
                        insert(getFace(png));
                    } else {
                        delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     */

    private void delete() {
        if (input.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(input.getText());
            int iCursorStart = Selection.getSelectionStart(input.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "#[weixin/000.png]#";
                        ((Editable) input.getText()).delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        ((Editable) input.getText()).delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    ((Editable) input.getText()).delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }
    }

    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     */
    private boolean isDeletePng(int cursor) {
        String st = "#[weixin/000.png]#";
        String content = input.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\#\\[weixin/)\\d{3}(.png\\]\\#)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[weixin/0.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(this, BitmapFactory
                            .decodeStream(getAssets().open(png))), sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * 向输入框里添加表情
     */
    private void insert(CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((input.getText()));
        int iCursorEnd = Selection.getSelectionEnd((input.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) input.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((input.getText()));
        ((Editable) input.getText()).insert(iCursor, text);
    }


    /***
     * 获取fans详情
     *
     * @param fan_id
     */
    private void getFansDetail(final int fan_id) {
//        showCustomDialog(R.string.loading);
        HttpRequestController.getFansInfo(this, fan_id,
                new HttpResponseListener<ApiFans.ApiFansResponse>() {
                    @Override
                    public void onResult(ApiFans.ApiFansResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            MyLogger.i(TAG, "response.fanInfo = " + response.fanInfo);
                            if (response.fanInfo != null) {
                                FansRecordModel fanModel = new FansRecordModel();
                                fanModel.accountId = fan_id;
                                fanModel.eid = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, -1);
                                MyLogger.e(TAG, "fanModel.accountId = " + fanModel.accountId);
                                fanModel.avatar = response.fanInfo.avatar;
                                imgHeadUrl = fanModel.avatar;
                                ImageLoader imageLoader = ImageLoader.getInstance();
                                imageLoader.displayImage(imgHeadUrl, imgHead, options);
                                fanModel.realName = response.fanInfo.realName;
                                fanModel.nickName = response.fanInfo.nickName;
                                title = fanModel.nickName;
                                setTitle(title);
                                fanModel.province = response.fanInfo.province;
                                fanModel.city = response.fanInfo.city;
                                fanModel.followStatus = response.fanInfo.followStatus;
                                fanModel.gender = response.fanInfo.gender;
                                fanModel.interactionTimes = response.fanInfo.interactionTimes;
                                fanModel.lastInteractionTime = response.fanInfo.lastInteractionTime;
                                fanModel.subscribeTime = response.fanInfo.subscribeTime;
                                Uri fansRecordModelUrl = new ApiFansRecordDb(context).insert(fanModel);
                                if (fansRecordModelUrl == null) {
                                    //TODO 该粉丝插入不成功
                                    MyLogger.e(TAG, "粉丝详情插入数据库失败");
                                }
                                MyLogger.e(TAG, "fanModel" + fanModel.toString());
                            } else {
                                MyLogger.e(TAG, "粉丝详情联网获取失败");
                            }
                        } else {
                            Utils.toast(context, response.getRetInfo() + "");
                        }
//                        dismissCustomDialog();
                    }
                });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        MyLogger.i(TAG, "onNewIntent");
        if (intent != null) {
            MessageRecordModel pushMessage = (MessageRecordModel) intent.getSerializableExtra(Constant.CHAT_INTENT_PUSH_MESSAGE);
            if (pushMessage != null) {
                fan_id = pushMessage.fid;
                groupId = pushMessage.groupId;
                FansRecordModel fanModel = ApiFansRecordDb.getFansInfoById(this, fan_id);
                if (fanModel == null) {
                    getFansDetail(fan_id);
                } else {
                    title = fanModel.nickName;
                    setTitle(title);
                    imgHeadUrl = fanModel.avatar;
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(imgHeadUrl, imgHead, options);
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        messageManager = PushMessageManager.getInstance(this);
        messageManager.registerOnReceivedPushMessageListener(opl);
        messageManager.setCurrentFid(fan_id);
        NotificationUtils notificationUtils = NotificationUtils.getInstance(this);
        notificationUtils.cancleById(fan_id);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        MyLogger.w(TAG, "onResume");
        Intent intent = getIntent();
        if (intent != null) {
            MessageRecordModel pushMessage = (MessageRecordModel) intent.getSerializableExtra(Constant.CHAT_INTENT_PUSH_MESSAGE);
            if (pushMessage != null) {
                fan_id = pushMessage.fid;
                groupId = pushMessage.groupId;
                FansRecordModel fanModel = ApiFansRecordDb.getFansInfoById(this, fan_id);
                if (fanModel == null) {
                    getFansDetail(fan_id);
                } else {
                    title = fanModel.nickName;
                    setTitle(title);
                    imgHeadUrl = fanModel.avatar;
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(imgHeadUrl, imgHead, options);
                }
            }
        }
        Message msg = new Message();
        msg.what = 2;
        mHandler.sendMessage(msg);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLvAdapter.stopMusic();
        MyLogger.i(TAG, "取消注册");
        messageManager.unregisterOnReceivedPushMessageListener(opl);//取消消息接收注册
        messageManager.setCurrentFid(0);//取消和这个粉丝的对话
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        mLvAdapter.stopMusic();
        MyLogger.i(TAG, "取消注册");
        messageManager.unregisterOnReceivedPushMessageListener(opl);//取消消息接收注册
        messageManager.setCurrentFid(0);//取消和这个粉丝的对话
        super.onStop();
    }

    private PushMessageManager.OnReceivedPushMessageListener opl = new PushMessageManager.OnReceivedPushMessageListener() {

        @Override
        public void onReceivedFansMessage(Object message) {
            MyLogger.e(TAG, "接收到的消息");
            MessageRecordModel pushMessage = (MessageRecordModel) message;
            int fid = pushMessage.fid;
            if (fid == fan_id) {
                MessageRecordModel model = new MessageRecordModel();
                model.sendOrReceive = 0;
                model.groupId = pushMessage.groupId;
                groupId = pushMessage.groupId;
                model.type = pushMessage.type;
                model.content = pushMessage.content;
                model.eid = pushMessage.eid;
                model.fid = pushMessage.fid;
                model.msgId = pushMessage.msgId;
                model.timestamp = pushMessage.timestamp;
                MyLogger.e(TAG, "timestamp = " + model.timestamp);
                ApiMessageRecordDb.updateReadStatus(context, fan_id);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = model;
                mHandler.sendMessage(msg);
            }
        }
    };

    /**
     * 初始化由列表进入聊天界面时传递的数据
     */
    private void initIntentData() {
        imgHead = (ImageView) findViewById(R.id.img_chat_head);
        imgHead.setVisibility(View.VISIBLE);
        fan_id = -1;
        Intent intent = getIntent();
        if (intent != null) {
            fan_id = intent.getIntExtra(Constant.CHAT_CONTACT_FAN_ID, -1);
            title = intent.getStringExtra(Constant.CHAT_CONTACT_NAME);
            imgHeadUrl = intent.getStringExtra(Constant.CHAT_CONTACT_HEAD);
            groupId = ApiMessageRecordDb.getGroupID(context, fan_id);
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgHeadUrl, imgHead, options);
        setTitle(title);
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.chat_head_default)
            .showImageOnFail(R.drawable.chat_head_default)
            .showImageOnLoading(R.drawable.chat_head_default)
            .build();

    /**
     * 初始化表情列表staticFacesList
     */
    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            String[] faces = getAssets().list("weixin");

            //MyLogger.i(TAG,"faces: "+Arrays.asList(faces));
            //将Assets中的表情名称转为字符串一一添加进staticFacesList
            Collections.addAll(staticFacesList, faces);
            //去掉删除图片
            staticFacesList.remove("emotion_del_normal.png");
            staticFacesList.remove("gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        visibility = layoutExtra.getVisibility();
        Message msg = new Message();
        switch (id) {
            case R.id.text_chat_extra:
                chat_face_container.setVisibility(View.GONE);//隐藏表情
                hideSoftInputView();//隐藏软键盘
                if (visibility == View.GONE) {
                    layoutExtra.setVisibility(View.VISIBLE);
                    input.setVisibility(View.VISIBLE);
                    btn_record.setVisibility(View.GONE);
                } else if (visibility == View.VISIBLE) {
                    layoutExtra.setVisibility(View.GONE);
                }
                break;
            case R.id.img_left_corner:
                startActivity(new Intent(this, MainActivity2.class));
                finish();
                break;
            case R.id.text_chat_extra_knowledge_words:
                startActivity(new Intent(this, ChatExtraActivity.class));
                break;
            case R.id.image_face://表情
                hideSoftInputView();//隐藏软键盘
                layoutExtra.setVisibility(View.GONE);//隐藏知识库
                if (chat_face_container.getVisibility() == View.GONE) {
                    chat_face_container.setVisibility(View.VISIBLE);
                    input.setVisibility(View.VISIBLE);
                    btn_record.setVisibility(View.GONE);
                } else {
                    chat_face_container.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_chat_send://发送文字消息
                reply = input.getText().toString();
                if (!TextUtils.isEmpty(reply)) {
                    SendMessage message = setSendMessage(Constant.CHAT_TYPE_TEXT, reply);
                    mLvAdapter.setList(infos);
                    mLvAdapter.notifyDataSetChanged();
                    mListView.setSelection(infos.size());
                    messageManager.sendEmpMessage("chat", message, new PushMessageManager.Rck() {
                        @Override
                        public void onResult(boolean status, final String errorInfo, int msgId) {
                            MyLogger.i(TAG, "ChatActivity(sendEmpMessage.onResult) obj = " + status + "errorInfo=" + errorInfo);
                            if (!status && "socket is null !".equals(errorInfo)) {
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.toast(context, "-" + errorInfo + "-");
                                    }
                                });
                            }
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = status;
                            mHandler.sendMessage(msg);
                        }
                    });
                    input.setText("");
                }
                break;
            case R.id.layout_chat_title://点击title跳转到详情界面
                Intent detailInformationIntent = new Intent(this, DetailedInformationActivity.class);
                detailInformationIntent.putExtra(Constant.FANS_ID, fan_id);
                startActivity(detailInformationIntent);
                break;
            case R.id.edit_chat_send://表情按钮
                MyLogger.w(TAG, "EditText.onClick");
                chat_face_container.setVisibility(View.GONE);
                layoutExtra.setVisibility(View.GONE);
                break;
            case R.id.text_chat_extra_img://弹出选择照片的按钮
                showChooseDialog(v);
                break;
            case R.id.tv_take_Phone://拍照按钮
                takePhoto();
                break;
            case R.id.tv_pick_photo://选择照片按钮
                pickPhoto();
                break;
            case R.id.image_recording: //录音按钮
                swichRecordAndInput();
                break;
        }
    }

    //===========================================================================================
    private TextView btn_take_Phone, btn_pick_photo, btn_cancel;
    private PopupWindow window;

    /******
     * 弹出选择照片的对话框（拍照或者从相册中选择）
     */
    private void showChooseDialog(View v) {
        View root = View.inflate(this, R.layout.view_choose_img, null);
        window = new PopupWindow(root, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
        btn_take_Phone = (TextView) root.findViewById(R.id.tv_take_Phone);
        btn_pick_photo = (TextView) root.findViewById(R.id.tv_pick_photo);
        btn_cancel = (TextView) root.findViewById(R.id.tv_cancel);
        if (window.isShowing()) {
            btn_pick_photo.setOnClickListener(this);
            btn_take_Phone.setOnClickListener(this);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissWindow();
                }
            });
        }

    }

    /*****
     * 取消popwindow 对话框
     */
    private void dismissWindow() {
        if (window.isShowing()) {
            window.dismiss();
        }
    }


    /****
     * 拍照的请求码
     ***/
    private final int requestcode_takephoto = 101;
    /****
     * 选择照片的请求码
     ***/
    private final int requestcode_pickphoto = 102;
    /***
     * 拍照的时候照片的路径
     **/
    private String photoPath;

    /****
     * 拍照
     */
    private void takePhoto() {
        boolean flag = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (flag) {
            Intent getImageByCamera = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            photoPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
            // 指定照片存储文件
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
            // 指定照片为高质量
            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(getImageByCamera, requestcode_takephoto);
            dismissWindow();
        } else {
            Utils.toast(this, "SD卡不存在");
        }
    }

    /***
     * 从相册中选取
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestcode_pickphoto);
        dismissWindow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case requestcode_takephoto://拍照
                if (RESULT_OK == resultCode) {
                    if (photoPath != null) {
                        imageViewSetBitmap(photoPath);
                        return;
                    }
                }
                break;
            case requestcode_pickphoto://选择照片
                if (resultCode == RESULT_OK) {
                    if (resultCode == RESULT_OK) {
                        Uri uri = data.getData();
                        imgetest.setImageURI(uri);
                        String[] res = {MediaStore.Images.Media.DATA};
                        Cursor cursor = this.managedQuery(uri, res, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String path = cursor.getString(column_index);
                        MyLogger.d(TAG, "图片格式" + path);
                        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                            imageViewSetBitmap(path);
                        } else {
                            Utils.toast(context, "仅支持图片的格式为:jpg和jpeg");
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*******
     * 图片缩放尺寸
     ******/
    private static final int PIC_SIZE = 64;

    /*******
     * 压缩图片，并且上传
     *
     * @param imagePath
     */
    private void imageViewSetBitmap(String imagePath) {
        try {
            File zipFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.IMAGE_CACHE_PATH);
            if (!zipFile.exists()) {
                zipFile.mkdir();
            }
            Map<String, File> files = new HashMap<String, File>();
            File file = new File(imagePath);
            files.put("Key_imagePath", file);
            HashMap<String, byte[]> map = PictureUtils.compressFile(files, 300, 500, 50);
            byte[] date = map.get("Key_imagePath");
            MyLogger.e("图片长度", date.length + "");
            Bitmap bm = null;
            if (date.length != 0) {
                bm = BitmapFactory.decodeByteArray(date, 0, date.length);
            }
            String path = android.os.Environment.getExternalStorageDirectory() + Constant.IMAGE_CACHE_PATH + System.currentTimeMillis() + ".jpg";
            PictureUtils.saveMyBitmap(bm, path);
            upLoadImg(path, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 此方法暂时没有用到
     *
     * @param type
     * @param imagePath
     */
    private void send(String type, String imagePath) {
        mLvAdapter.setList(infos);
        mLvAdapter.notifyDataSetChanged();
        mListView.setSelection(infos.size());
    }


    /****
     * 长传图片
     *
     * @param path 图片的路径
     * @param type 文件类型 1图片 2音频 3视频
     */
    public void upLoadImg(String path, String type) {
        HttpRequestController.upload(this, path, type, new HttpResponseListener<ApiUpload.ApiUploadResponse>() {
            @Override
            public void onResult(ApiUpload.ApiUploadResponse response) {
                MyLogger.e(TAG, "---上传图片" + response);
//                try {
//                    String txtContent = FileService.read(File.separator + "cash" + File.separator + "image.txt");
//                    String imeiLog = txtContent + "\n\n\n\n" + "发送消息回调" + response.toString();
//                    WriteJson2SD.writeJson(imeiLog, DateFormatUtils.getTimeStamp(), "上传图片");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                if (response != null) {
                    //if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                    if (response.result != null) {
                        String url = response.result.url;
                        String content = "{\"" + "picUrl\"" + ":" + "\"" + url + "\"}";
                        mLvAdapter.setList(infos);
                        mLvAdapter.notifyDataSetChanged();
                        mListView.setSelection(infos.size());
                        SendMessage message = setSendMessage(Constant.CHAT_TYPE_IMAGE, content);
                        messageManager.sendEmpMessage("chat", message, new PushMessageManager.Rck() {
                            @Override
                            public void onResult(boolean status, final String errorInfo, int msgId) {
                                if (!status && "socket is null !".equals(errorInfo)) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.toast(context, "-" + errorInfo + "-");
                                        }
                                    });
                                }
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = status;
                                mHandler.sendMessage(msg);
                            }
                        });
//                        } else {
//                            Utils.toast(context, "网络异常");
//                        }
                    }
                }
            }
        });
    }

    //  监听Edittext 变化来切换加号和发送按钮
    TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int len = s.length();
            if (len > 0) {
                input.setGravity(Gravity.START);
                send.setVisibility(View.VISIBLE);
                textExtra.setVisibility(View.GONE);
            } else {
                send.setVisibility(View.GONE);
                textExtra.setVisibility(View.VISIBLE);
            }

        }
    };

    /*****
     * 默认的是输入框
     **/
    boolean flag_swich = true;
    private RecordControl control;
    private List<String> paths = new ArrayList<String>();

    /****
     * 录音按钮和输入框之间的切换
     ***/
    private void swichRecordAndInput() {
        input.setText("");
        if (flag_swich) {
            flag_swich = false;
            input.setVisibility(View.GONE);
            chat_face_container.setVisibility(View.GONE);
            btn_record.setVisibility(View.VISIBLE);
            btn_record.setHint("按住 说话");
            btn_record.setHintTextColor(Color.BLACK);
            btn_record.setGravity(Gravity.CENTER);
            imgRecord.setImageResource(R.drawable.ic_chat_keyboard);
            control = new RecordControl(ChatActivity.this, btn_record);
            control.setListener(new RecordControl.GetPathListener() {
                @Override
                public void getPath(final String path) {
                    MyLogger.i(TAG, "path: " + path);
                    if (path != null) {
                        HttpRequestController.upload(context, path, "2", new HttpResponseListener<ApiUpload.ApiUploadResponse>() {
                            @Override
                            public void onResult(ApiUpload.ApiUploadResponse response) {
//                                try {
//                                    String txtContent = FileService.read(File.separator + "cash" + File.separator + "voice.txt");
//                                    Log.i(TAG, "txtContent:===========" + txtContent);
//                                    String imeiLog = txtContent + "\n\n\n\n" + "发送消息回调" + response.toString();
//                                    WriteJson2SD.writeJson(imeiLog, DateFormatUtils.getTimeStamp(), "上传语音");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                                MyLogger.e(TAG, "---发送结果" + response);
                                if (response != null) {
                                    //if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                                    if (response.result != null) {
                                        MyLogger.e("-----voicePath---", response.result.url);
                                        String voicePath = "{\"voiceUrl\":\"" + response.result.url + "\"}";
                                        SendMessage message = setSendMessage(Constant.CHAT_TYPE_VOICE, path + "####" + voicePath);
                                        mLvAdapter.notifyDataSetChanged();
                                        mListView.setSelection(infos.size());
                                        messageManager.sendEmpMessage("chat", message, new PushMessageManager.Rck() {
                                            @Override
                                            public void onResult(boolean status, final String errorInfo, int msgId) {
                                                if (!status && "socket is null !".equals(errorInfo)) {
                                                    mHandler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Utils.toast(context, "-" + errorInfo + "-");
                                                        }
                                                    });
                                                }
                                                Message msg = new Message();
                                                msg.what = 2;
                                                msg.obj = status;
                                                mHandler.sendMessage(msg);
                                            }
                                        });
                                    }
//                                } else {
//                                    Utils.toast(context, response.getRetInfo());
//                                }
                                } else {
                                    Utils.toast(context, "网络异常");
                                }
                            }
                        });
                    } else {
                        Utils.toast(context, "已经取消发送");
                    }
                }
            });

        } else {
            flag_swich = true;
            input.setVisibility(View.VISIBLE);
            btn_record.setVisibility(View.GONE);
            imgRecord.setImageResource(R.drawable.ic_chat_record);
        }
    }

//===========================================================================================================

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity2.class));
        finish();
    }

    /**
     * 对发送的消息进行处理
     *
     * @return
     */

    public SendMessage setSendMessage(String type, String res) {
        SendMessage message = new SendMessage();
        //message.recordId = (int) (System.currentTimeMillis() / 1000 % Integer.MAX_VALUE);
        int msgID = Integer.parseInt(RandomUtils.getRandom(RandomUtils.NUMBERS, 8));
        message.recordId = msgID;
        message.groupId = groupId;
        long timestamp = System.currentTimeMillis();
        PreferenceUtils.putInt(context, "msgID", msgID);
        PreferenceUtils.putInt(context, "timestamp", new Long(timestamp).intValue());
        message.timestamp = timestamp;
        message.fid = fan_id;
        message.eid = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
        message.type = type;
        message.socialId = PreferenceUtils.getInt(context, Constant.PREFERENCES_SOCIAL_ID, 0);
        if (type == Constant.CHAT_TYPE_TEXT) {
            message.content = replayContent(res);
        } else {
            message.content = res;
        }
        MessageRecordModel model = new MessageRecordModel();
        model.msgId = message.recordId;
        model.successOrFail = 2;
        model.groupId = groupId;
        model.sendOrReceive = 1;
        model.fid = fan_id;
        model.timestamp = message.timestamp;
        // model.content = reply;
        model.content = res;
        model.type = message.type;
        model.eid = message.eid;
        infos.add(model);
        return message;
    }

    private String replayContent(String content) {

        String[] replace = new String[Constant.WEIXIN_CHAR.length];

        for (int i = 0; i < Constant.WEIXIN_CHAR.length; i++) {
            replace[i] = "#[weixin/" + replaceNumber(i) + ".png]#";
        }
        Template template = new Template(content, replace);
        content = template.apply(Constant.WEIXIN_CHAR);
        return content;
    }

    private static String replaceNumber(int number) {
        if (number < 10) {
            return "00" + Integer.toString(number);
        } else if (number < 100) {
            return "0" + Integer.toString(number);
        } else {
            return Integer.toString(number);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (id) {
            case R.id.edit_chat_send:
                chat_face_container.setVisibility(View.GONE);
                layoutExtra.setVisibility(View.GONE);
                break;
            case R.id.message_chat_listview:
                visibility = layoutExtra.getVisibility();
                // 隐藏软键盘
                hideSoftInputView();
                if (visibility == View.VISIBLE) {
                    layoutExtra.setVisibility(View.GONE);
                }
                break;
        }
        return false;
    }

    /**
     * 下拉加载
     */
    @Override
    public void onRefresh() {
        MyLogger.w(TAG, "onRefresh");
        //TODO 加载历史记录
        List<MessageRecordModel> list = ApiMessageRecordDb.getMessage(context, fan_id, PAGE_SIZE, 1);
        if (list != null) {
            if (list.size() > 0) {
                list = ApiMessageRecordDb.getMessage(context, fan_id, PAGE_SIZE, ++PAGE_NUM_AGAIN);
                final List<MessageRecordModel> messageList = list;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            messageList.addAll(infos);
                            infos = messageList;
                            sleep(1000);
                            Message msg = new Message();
                            msg.what = 4;
                            mHandler.sendMessage(msg);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } else {
                getConversations();
            }
        }
    }

    /****
     * 查询聊天记录
     */
    private void getConversations() {
        final int empId = PreferenceUtils.getInt(this, Constant.PREFERENCE_EMP_ID, -1);
        //加载100条记录
        MyLogger.e(TAG, "此方法执行的次数 ");
        int msgID = PreferenceUtils.getInt(context, "msgID", 0);
        HttpRequestController.realTimeHistory(this, fan_id, msgID, 300, 1, 2,
                new HttpResponseListener<ApiRealTimeHistory.ApiRealTimeHistoryResponse>() {
                    @Override
                    public void onResult(ApiRealTimeHistory.ApiRealTimeHistoryResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            List<Messages> messages = response.messageHistory.messages;
                            List<MessageRecordModel> messageList = new ArrayList<MessageRecordModel>();
                            MessageRecordModel messageModel = null;
                            MyLogger.e(TAG, "fanId = " + fan_id);
                            Message msg = new Message();
                            if (messages.size() > 0) {
                                for (Messages message : messages) {
                                    messageModel = new MessageRecordModel();
                                    messageModel.artificialStatus = 5;
                                    messageModel.msgId = message.id;
                                    messageModel.type = Integer.toString(message.type);
                                    MyLogger.e(TAG, "消息类型：" + message.type + "消息内容：" + message.content);
                                    if (message.type == 1) {
                                        messageModel.content = message.content;
                                    } else if (message.type == 2) {
                                        messageModel.content = getImageUrl(message.content);
                                    } else if (message.type == 3) {
                                        messageModel.content = message.content;
                                    }
                                    messageModel.timestamp = message.sendingTime;
                                    messageModel.fid = fan_id;
                                    messageModel.eid = empId;
                                    messageModel.groupId = message.groupId;
                                    groupId = message.groupId;
                                    messageModel.isRead = 1;
                                    messageModel.successOrFail = 1;
                                    messageModel.sendOrReceive = message.receiveFlag - 1;
                                    messageModel.time = message.sendingTime;
                                    messageList.add(messageModel);
                                }
                                ApiMessageRecordDb messageDb = new ApiMessageRecordDb(context);
                                int length = messageDb.bulkInsert(messageList);
                                if (length > 0) {
                                    MyLogger.w(TAG, "批量插入数据库，长度 = " + length);
                                }
                                msg.what = 2;
                            } else {
                                msg.what = 4;
                            }
                            mHandler.sendMessage(msg);
                        } else {
                            Utils.toast(context, response.getRetInfo() + "");
                        }
                    }
                });
    }

    /***
     * 查询历史记录的时候，从网络上获取的消息的图片地址
     *
     * @param content
     * @return
     */
    private String getImageUrl(String content) {
        String url = "";
        JSONObject object = null;
        try {
            object = new JSONObject(content);
            String json = object.getString("mediaId");
            MyLogger.e(TAG, "json=" + json);
            if (json.startsWith("{\"")) {
                JSONObject obj = new JSONObject(json);
                url = obj.getString("picUrl");
                MyLogger.e(TAG, "picUrl=" + url);
            } else {
                url = object.getString("pic");
                MyLogger.e(TAG, "pic=" + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }


    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://刷新界面
                    mLvAdapter.setList(infos);
                    mLvAdapter.notifyDataSetChanged();
                    mListView.setSelection(infos.size());
                    break;
                case 1://接收消息，并添加消息
                    MessageRecordModel model = (MessageRecordModel) msg.obj;
                    infos.add(model);
                    mLvAdapter.notifyDataSetChanged();
                    mListView.setSelection(infos.size());
                    break;
                case 2://重新获取历史消息
                    infos = ApiMessageRecordDb.getMessage(ChatActivity.this, fan_id, PAGE_SIZE, PAGE_NUM);
                    MyLogger.w(TAG, "infos.size() = " + infos.size());
                    if (infos.size() <= 0) {
                        getConversations();
                    }
                    mLvAdapter.setList(infos);
                    mLvAdapter.notifyDataSetChanged();
                    mListView.setSelection(infos.size());
                    break;
                case 3://删除某一条历史记录
                    int position = Integer.parseInt(msg.obj.toString());
                    infos.remove(position);
                    mLvAdapter.notifyDataSetChanged();
                    break;
                case 4://取消加载历史记录
                    mLvAdapter.setList(infos);
                    mLvAdapter.notifyDataSetChanged();
                    mListView.onRefreshCompleteHeader();
                    break;
                case 5://取消加载历史记录
                    Bundle bundle = msg.getData();

                    break;
            }
        }
    };
}
