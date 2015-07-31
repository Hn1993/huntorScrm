

package com.huntor.mscrm.app2.ui;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.*;


import com.huntor.mscrm.app2.model.*;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.*;
import com.huntor.mscrm.app2.provider.api.ApiFansInFoDb;
import com.huntor.mscrm.app2.provider.api.ApiFansRecordDb;
import com.huntor.mscrm.app2.ui.component.BaseActivity;

import com.huntor.mscrm.app2.ui.component.MySlideListView;
import com.huntor.mscrm.app2.ui.component.MyViewPager;
import com.huntor.mscrm.app2.utils.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangjie.wavecompat.WaveCompat;
import com.wangjie.wavecompat.WaveDrawable;
import com.wangjie.wavecompat.WaveTouchHelper;
import org.apache.http.protocol.HTTP;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.wangjie.wavecompat.WaveTouchHelper.*;

/**
 * Created by Admin on 2015/5/4.
 */
public class DetailedInformationActivity extends BaseActivity implements View.OnClickListener,OnWaveTouchHelperListener {
    private Context context;
    private String TAG = getClass().getName();

    private ViewPager viewpager;
    private IconTextView buy_more;
    private List<View> viewlist;
    private PagerAdapter myAdapter;


    //长按item弹出popwindow
    private PopupWindow popupWindow;
    //要删除的listview的item
    private int index = 0;

    private TextView delete, cancel;
    private View pop_view;
    private RelativeLayout pop_layout;

    //跳转传值返回结果
    private String BuyInclination_result;

    //标题下面的view  以及文字
    private View detaileinfo_line_1;
    private RelativeLayout detaileinfo_social, detaileinfo_details, detaileinfo_deal;

    /**
     * viewpager1的会员信息
     */
    //fans头像
    private ImageView viewpager1_fans_image;
    //fans 名字
    private TextView viewpager1_fans_name;
    //关注时间
    private TextView viewpager1_attention_time;
    //性别图片
    private Drawable viewpager1_fans_sex;
    //fans城市
    private TextView viewpager1_fans_city;
    //最近交互
    private TextView viewpager1_fans_interaction;
    //交互次数
    private TextView viewpager1_fans_interactionTimes;
    //发消息
    private Button viewpager1_send_message;
    //加入分组
    private Button viewpager1_into_group;

    private List<FanInfo.PurchaseIntents> want_list;


    /**
     * viewpager2的会员信息
     */
    //fans 名字
    private TextView viewpager2_fans_name;
    //fans所在地区（住址）
    private TextView viewpager2_fans_gender;
    //fans邮箱
    private TextView viewpager2_fans_job;
    //fans电话
    private TextView viewpager2_fans_tel;
    // fans 年龄
    private TextView viewpager2_fans_age;

    private ImageView image_people_head_2;
    /**
     * viewpager3的会员交易信息
     */
    //fans 已买和购买意向listview
    //private ListView listview_want_buy_3;
    private ListView listview_want_buy_3;
    private ListView listview_buyed_3;
    private LinearLayout mLinearLyout;


    private UserBuyedAdapter buyed_adapter;

    private FanInfo fan;
    private int fans_id;


    private RelativeLayout mBackImage;
    private TextView mLeftTitle;


    private RelativeLayout relativeLayout;

    private int accountId;
    private UserPurchaseAdapter purchaseAdapter;
    private int position;

    //粉丝所在自定义分组信息
    List<FanInfo.TargetList> mTargetList;
    //发送消息按钮可不可点
    private boolean isClick = true;
    //拨号
    private AlertDialog.Builder builder;
    /*****
     * 请求的时间 ，每隔10分钟请求一次接口
     **/
    private long requestTime = 0;
    /*****
     * 再一次请求的时间
     **/
    private long reRequestTime = 0;

    private String KEY_REQUESTTIME = "KEY_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = DetailedInformationActivity.this;
        setContentView(R.layout.fragment_fans_info);
        Intent intent = getIntent();
        if (intent != null) {

            fans_id = intent.getIntExtra(Constant.FANS_ID, 0);
            String type = intent.getStringExtra("type");
            if (type != null && type.equals("new_fans")) {
                setNewToOld();
            }
        }

        initData();
        initview();
        findImage();
    }

    private void initData() {
        want_list = new ArrayList<FanInfo.PurchaseIntents>();
        purchaseAdapter = new UserPurchaseAdapter(this, want_list);
    }

    private void setNewToOld() {
        HttpRequestController.fansStatusNewTwoOld(this, fans_id,
                new HttpResponseListener<ApiNewTwoOld.ApiNewTwoOldResponse>() {
                    @Override
                    public void onResult(ApiNewTwoOld.ApiNewTwoOldResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i("DetailedInformation", "response.response = " + response.response);
                        }
                    }
                });
    }

    private void initview() {
//        relativeLayout = (RelativeLayout) findViewById(R.id.detailinfo_buttom_layout);
//        relativeLayout.setVisibility(View.GONE);

//        mBackImage = (RelativeLayout) findViewById(R.id.img_left_corner);
//        mLeftTitle = (TextView) findViewById(R.id.text_base_title);
//        mBackImage.setVisibility(View.VISIBLE);
//        mLeftTitle.setVisibility(View.VISIBLE);
//        mBackImage.setOnClickListener(this);
//        mLeftTitle.setText("会员详情");

        //Viewpager的三个界面
        viewpager = (ViewPager) findViewById(R.id.detailte_viewpager);
        viewlist = new ArrayList<View>();
        viewlist.add(getLayoutInflater().inflate(R.layout.layout_viewpager1, null));
        viewlist.add(getLayoutInflater().inflate(R.layout.layout_viewpager2, null));
        viewlist.add(getLayoutInflater().inflate(R.layout.layout_viewpager3, null));
        //viewpager的适配器
        myAdapter = new MyPagerAdapter(viewlist);
        viewpager.setAdapter(myAdapter);
        //viewpager的滑动监听
        viewpager.setOnPageChangeListener(viewpager_linster);

        //购买意向按钮
//        buy_more = (IconTextView) viewlist.get(2).findViewById(R.id.member_info_buymore_3);
//        buy_more.setOnClickListener(this);
        //购买意向和已购产品的Listview
        listview_want_buy_3 = (ListView) viewlist.get(2).findViewById(R.id.listview_wantbuy);
        listview_buyed_3 = (ListView) viewlist.get(2).findViewById(R.id.listview_buyed);
        //mLinearLyout = (LinearLayout) viewlist.get(2).findViewById(R.id.no_buyed_product);
        //购买意向的长按事件
        listview_want_buy_3.setOnItemLongClickListener(longClickListener);
        //popwindow上的控件
        pop_view = getLayoutInflater().inflate(R.layout.layout_mypop, null);
        delete = (TextView) pop_view.findViewById(R.id.pop_delete);
        cancel = (TextView) pop_view.findViewById(R.id.pop_cancel);
        //pop_layout = (RelativeLayout) pop_view.findViewById(R.id.pop_layout);

        //获取view_line
        detaileinfo_line_1 = findViewById(R.id.detaileinfo_line_1);
        detaileinfo_line_1.setLayoutParams(new LinearLayout.LayoutParams((getWindowManager().getDefaultDisplay().getWidth()) / 3, 5));

        //获取二级标题（三个Tab）
        detaileinfo_social = (RelativeLayout) findViewById(R.id.detaileinfo_social);
        detaileinfo_details = (RelativeLayout) findViewById(R.id.detaileinfo_details);
        detaileinfo_deal = (RelativeLayout) findViewById(R.id.detaileinfo_deal);
        detaileinfo_social.setOnClickListener(this);
        detaileinfo_details.setOnClickListener(this);
        detaileinfo_deal.setOnClickListener(this);
        //detaileinfo_social.setTextColor(getResources().getColor(R.color.title_selected));

        //账户信息里的Icontext的点击事件
//        viewlist.get(1).findViewById(R.id.account_information_compile_gender).setOnClickListener(this);
//        viewlist.get(1).findViewById(R.id.account_information_compile_job).setOnClickListener(this);
//        viewlist.get(1).findViewById(R.id.account_information_compile_tel).setOnClickListener(this);
//        viewlist.get(1).findViewById(R.id.account_information_compile_name).setOnClickListener(this);
//        viewlist.get(1).findViewById(R.id.account_information_compile_age).setOnClickListener(this);

        //通过接口获取网络数据

        HttpRes(fans_id);
    }


    private void getData(FanInfo fan) {
        if (fan != null) {
            accountId = fan.accountId;
            ArrayList<FanInfo.PurchaseIntents> purchaseIntents = fan.purchaseIntents;
            if (purchaseIntents != null) {
                //Log.i("黄安", "purchaseIntents.size = " + fan.purchaseIntents.size());
            } else {
                //Log.i("黄安", "purchaseIntents.size(null) = " + purchaseIntents);
            }
            ArrayList<FanInfo.Deals> deals = null;
            if (fan.deals != null) {
                deals = fan.deals;
            } else {
                deals = new ArrayList<FanInfo.Deals>();
            }

            ArrayList<FanInfo.Details> detailses = new ArrayList<FanInfo.Details>();
            //detailses=deals.
            for (FanInfo.Deals deal : deals) {
                detailses.addAll(deal.details);
            }
            //初始化3个viewpager页面的内容
            initviewpager1Info(fan);
            initviewpager2Info(fan);
            get_buyed_Data(detailses, deals);

            want_list = fan.purchaseIntents;

            purchaseAdapter.setData(want_list);
            listview_want_buy_3.setAdapter(purchaseAdapter);
            setPullLvHeight(listview_want_buy_3);
        }
    }

    //通过接口获取粉丝信息
    public void HttpRes(final int fans_id) {
        Log.i(TAG, "fans_id: " + fans_id);
        final FanInfo mLocalData = ApiFansInFoDb.getFansInfoById(this, fans_id);//先获取本地数据库里的数据
        getData(mLocalData);
        reRequestTime = System.currentTimeMillis();
        requestTime = PreferenceUtils.getLong(context, KEY_REQUESTTIME, System.currentTimeMillis());
        showCustomDialog(R.string.loading);
        HttpRequestController.getFansInfo(this, fans_id, new HttpResponseListener<ApiFans.ApiFansResponse>() {
            @Override
            public void onResult(ApiFans.ApiFansResponse response) {
                if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                    Log.i(TAG, "response.fanInfo = " + response.fanInfo);
                    fan = response.fanInfo;
                    requestTime = System.currentTimeMillis();
                    PreferenceUtils.putLong(context, KEY_REQUESTTIME, requestTime);
                    getData(fan);
                } else if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR) {
                    Log.i(TAG, "response.fanInfo = " + response.fanInfo);
                    Utils.toast(DetailedInformationActivity.this, response.getRetInfo() + "");
                    getData(mLocalData);
                }
                dismissCustomDialog();
            }
        });
    }

    /**
     * 设置ListView的高度
     *
     * @param pull
     */
    private void setPullLvHeight(ListView pull) {
        int totalHeight = 0;
        ListAdapter adapter = pull.getAdapter();
        for (int i = 0, len = adapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null, pull);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = pull.getLayoutParams();
        params.height = totalHeight + (pull.getDividerHeight() * pull.getCount());
        pull.setLayoutParams(params);
    }

    /**
     * 初始化社交信息界面
     */
    public void initviewpager1Info(FanInfo fan) {
        mTargetList = new ArrayList<FanInfo.TargetList>();
        if (fan == null) {
            return;
        }
        //加入分组和发消息的按钮
        viewpager1_send_message = (Button) findViewById(R.id.send_message);
        viewpager1_send_message.setClickable(true);
        viewpager1_send_message.setOnClickListener(this);
        viewpager1_into_group = (Button)findViewById(R.id.join_group);
        viewpager1_into_group.setOnClickListener(this);

        viewpager1_fans_image = (ImageView) findViewById(R.id.image_people_head);

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.dimension_code_loading_default)
                .showImageForEmptyUri(R.drawable.chat_contact_list_default_head).cacheOnDisc(true)
                .showImageOnFail(R.drawable.dimension_code_fail_default).cacheInMemory(true).build();
        String url = null;
        if (fan.avatar == null) {
            viewpager1_fans_image.setImageResource(R.drawable.chat_contact_list_default_head);
        } else {
            url = fan.avatar;
            imageLoader.displayImage(url, viewpager1_fans_image, options);
        }

        viewpager1_fans_name = (TextView) findViewById(R.id.people_name);
        if (fan.nickName != null || ("null".equals(fan.nickName))) {
            viewpager1_fans_name.setText(fan.nickName);
        } else {
            viewpager1_fans_name.setText("暂无");
        }


        viewpager1_attention_time = (TextView) findViewById(R.id.people_time);
        long subscribeTime = fan.subscribeTime;
        if (subscribeTime != 0) {
            String time1 = DateFormatUtils.getPassedTime(this, fan.subscribeTime);
            viewpager1_attention_time.setText("关注于" + time1);
        } else {
            viewpager1_attention_time.setText("");
        }

        //设置drawerableRight
        if (fan.gender.equals("f")) {
            viewpager1_fans_sex = getResources().getDrawable(R.drawable.woman);
            viewpager1_fans_sex.setBounds(0, 0, viewpager1_fans_sex.getMinimumWidth(), viewpager1_fans_sex.getMinimumHeight());
            viewpager1_fans_name.setCompoundDrawables(null, null, viewpager1_fans_sex, null);
        } else {
            viewpager1_fans_sex = getResources().getDrawable(R.drawable.man);
            viewpager1_fans_sex.setBounds(0, 0, viewpager1_fans_sex.getMinimumWidth(), viewpager1_fans_sex.getMinimumHeight());
            viewpager1_fans_name.setCompoundDrawables(null, null, viewpager1_fans_sex, null);
        }
        //粉丝所在城市
        //viewpager1_fans_city = (TextView) viewlist.get(0).findViewById(R.id.member_provice_1);
        viewpager1_fans_city = (TextView) viewlist.get(0).findViewById(R.id.member_provice_1);
        Log.i("黄安", "粉丝城市" + fan.city);
        if (("").equals(fan.city) || fan.city == null || ("null").equals(fan.city)) {
            viewpager1_fans_city.setText("暂无");
        } else {
            viewpager1_fans_city.setText(fan.city);
        }


        viewpager1_fans_interaction = (TextView) viewlist.get(0).findViewById(R.id.interation_time_before_1);
        if (fan.lastInteractionTime != 0) {

            String time2 = DateFormatUtils.getPassedTime(this, fan.lastInteractionTime);

            if ("46年前".equals(time2)) {
                viewpager1_fans_interaction.setText("暂无");
            } else {
                viewpager1_fans_interaction.setText(time2);
                isClick = true;
            }
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - fan.lastInteractionTime;
            if (timePassed >= 202800000) {
                isClick = false;
            }
        } else {
            viewpager1_fans_interaction.setText("暂无");
            //viewpager1_send_message.setClickable(false);
            isClick = false;
        }
        viewpager1_fans_interactionTimes = (TextView) viewlist.get(0).findViewById(R.id.brwose_num);

        if (fan.interactionTimes != 0) {
            viewpager1_fans_interactionTimes.setText(fan.interactionTimes + "次");
        } else {
            viewpager1_fans_interactionTimes.setText("暂无");
        }


       // RelativeLayout groupRelativelayout = (RelativeLayout) viewlist.get(0).findViewById(R.id.relativelayout_in_the_group);
        mTargetList = fan.targetLists;
        TextView InTheGroup = (TextView) viewlist.get(0).findViewById(R.id.in_the_group);
        MyLogger.i(TAG, "mTargetList.size = :" + mTargetList.size());
//        for (int i=0;i<mTargetList.size();i++){
//
//            if(mTargetList.size()==1){
//                InTheGroup.setText(mTargetList.get(i).name);
//            }else if(mTargetList.size()==0){
//                groupRelativelayout.setVisibility(View.GONE);
//            }else{
//                //String s=new String();
//                        str+=mTargetList.get(i).name+"\n";
//                MyLogger.i(TAG,"s======================="+str);
//                InTheGroup.setText(str);
//            }
//
//        }

        String str="";
        if(mTargetList.size()==0){
            //groupRelativelayout.setVisibility(View.GONE);
        }else{
            for (int i = 0; i < mTargetList.size(); i++) {
                //截取字符串   让最后一行不加 "\n"
                str += mTargetList.get(i).name + "\n";
            }
            MyLogger.i(TAG,"str============="+str);
            InTheGroup.setText(str.substring(0,str.length()-1));
        }


    }

    /**
     * 初始化账户信息界面
     */
    public void initviewpager2Info(FanInfo fan) {
        if (fan == null) {
            return;
        }
        try {
            viewpager2_fans_name = (TextView) viewlist.get(1).findViewById(R.id.menber_address_name_2);
            if (fan.realName == null || "".equals(fan.realName)) {

                viewpager2_fans_name.setText("暂无");
            } else {

                viewpager2_fans_name.setText(fan.realName);
            }
            //性别
            viewpager2_fans_gender = (TextView) viewlist.get(1).findViewById(R.id.menber_address_text_2);
            if (fan.gender != null) {
                if ("m".equals(fan.gender)) {
                    viewpager2_fans_gender.setText("男");
                } else if ("f".equals(fan.gender)) {
                    viewpager2_fans_gender.setText("女");
                } else {
                    viewpager2_fans_gender.setText("未知");
                }
            } else {
                viewpager2_fans_gender.setText("暂无");
            }

            viewpager2_fans_job = (TextView) viewlist.get(1).findViewById(R.id.menber_mail_text_2);
            if (fan.occupation != null) {
                viewpager2_fans_job.setText(fan.occupation);
            } else {
                viewpager2_fans_job.setText("暂无");
            }
            //viewpager2_fans_email.setText("暂无");
            //账户信息的电话
            viewpager2_fans_tel = (TextView) viewlist.get(1).findViewById(R.id.menber_phone_text_2);
            if (fan.phone1 == null) {
                if (fan.phone2 == null) {
                    if (fan.phone3 == null) {
                        viewpager2_fans_tel.setText("暂无");
                    } else {
                        viewpager2_fans_tel.setText(fan.phone3);
                    }
                } else {
                    viewpager2_fans_tel.setText(fan.phone2);
                }
            } else {
                viewpager2_fans_tel.setText(fan.phone1);
            }

            viewpager2_fans_age = (TextView) viewlist.get(1).findViewById(R.id.menber_age_text_2);
            if(fan.ageGroup==0){
                viewpager2_fans_age.setText("暂无");
            }else {
                if (fan.ageGroup == 1) {
                    viewpager2_fans_age.setText("15-20");
                } else if (fan.ageGroup == 2) {
                    viewpager2_fans_age.setText("20-25");
                } else if (fan.ageGroup == 3) {
                    viewpager2_fans_age.setText("25-30");
                } else if (fan.ageGroup == 4) {
                    viewpager2_fans_age.setText("30-35");
                } else if (fan.ageGroup == 5) {
                    viewpager2_fans_age.setText("35-40");
                } else {
                    viewpager2_fans_age.setText("40以上");
                }
            }



            image_people_head_2 = (ImageView) viewlist.get(1).findViewById(R.id.image_people_head_2);
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.dimension_code_loading_default)
                    .showImageForEmptyUri(R.drawable.chat_contact_list_default_head).cacheOnDisc(true)
                    .showImageOnFail(R.drawable.dimension_code_fail_default).cacheInMemory(true).build();
            String url = null;
            if (fan.avatar == null) {
                image_people_head_2.setImageResource(R.drawable.chat_contact_list_default_head);
            } else {
                url = fan.avatar;
                imageLoader.displayImage(url, image_people_head_2, options);
            }


            LinearLayout mGoTel = (LinearLayout) viewlist.get(1).findViewById(R.id.detailinfo2_phone_call);//点击跳转到电话界面
            mGoTel.setOnClickListener(this);
            viewlist.get(1).findViewById(R.id.detaileinfo_layout).setOnClickListener(this);//点击别处   消失dialog
        } catch (Exception e) {

        }
    }

    /**
     * 给一个控件设置一个动画
     *
     * @param view
     */
    private void setAnimation(View view) {
        AnimationSet set = new AnimationSet(false);
        ScaleAnimation scale = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(400);
        scale.setFillAfter(true);
        set.addAnimation(scale);

        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(400);
        alpha.setFillAfter(true);
        set.addAnimation(alpha);
        view.startAnimation(set);
    }


    //实现item的长按删除效果
    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            index = i;
            popupWindow = new PopupWindow(pop_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());//点击别处消失popwindow
            popupWindow.setOutsideTouchable(false);

            int[] location = new int[2];

            TextView text_item_my_group_name = (TextView) view.findViewById(R.id.item_detaileinfo_wantbuy);
            text_item_my_group_name.getLocationInWindow(location);
            int x = location[0];
            int y = location[1] - 30;
            popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, x, y);
            //控件的动画效果
            setAnimation(pop_view);
            delete.setOnClickListener(DetailedInformationActivity.this);
            cancel.setOnClickListener(DetailedInformationActivity.this);
            //pop_layout.setOnClickListener(DetailedInformationActivity.this);

            position = i;

            return false;
        }
    };

    //访问删除粉丝购买意向的接口
    public void getDeletelist(int accountId, int purchaseIntentId) {
        showCustomDialog(R.string.loading);

        HttpRequestController.deletePurchaseIntent(this, accountId, purchaseIntentId,
                new HttpResponseListener<ApiDeletePIntent.ApiDeletePIntentResponse>() {
                    @Override
                    public void onResult(ApiDeletePIntent.ApiDeletePIntentResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i("删除粉丝购买意向", "response = " + response);
                            want_list.remove(position);
                            purchaseAdapter.notifyDataSetChanged();
                        } else {
                            Utils.toast(DetailedInformationActivity.this, response.getRetInfo() + "");
                        }
                        dismissCustomDialog();
                    }
                });
    }

    //已购产品数据
    public void get_buyed_Data(ArrayList<FanInfo.Details> buyed_list, ArrayList<FanInfo.Deals> dealsList) {
        if (buyed_list == null) {
            buyed_list = new ArrayList<FanInfo.Details>();
        }
//        if (buyed_list.size() == 0) {
//            Log.i(TAG, "buyed_list.size:" + buyed_list.size());
//            mLinearLyout.setVisibility(View.VISIBLE);
//        } else {
//            mLinearLyout.setVisibility(View.GONE);
//        }
        buyed_adapter = new UserBuyedAdapter(this, buyed_list, dealsList);
        listview_buyed_3.setAdapter(buyed_adapter);
        setPullLvHeight(listview_buyed_3);
        buyed_adapter.notifyDataSetChanged();
    }


    private ViewPager.OnPageChangeListener viewpager_linster = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }


        @Override
        public void onPageSelected(int i) {
            TranslateAnimation an;
            switch (i) {
                case 0:
//                    relativeLayout.setVisibility(View.GONE);
//                    buy_more.setVisibility(View.GONE);
                    mImageView.setVisibility(View.VISIBLE);
//                    detaileinfo_social.setTextColor(getResources().getColor(R.color.title_selected));
//                    detaileinfo_details.setTextColor(getResources().getColor(R.color.title));
//                    detaileinfo_deal.setTextColor(getResources().getColor(R.color.title));
                    an = new TranslateAnimation((getWindowManager().getDefaultDisplay().getWidth()) / 3, 0, 0, 0);
                    an.setFillAfter(true);
                    detaileinfo_line_1.startAnimation(an);
                    //viewpager.setScrollble(true);
                    break;
                case 1:
//                    relativeLayout.setVisibility(View.GONE);
//                    buy_more.setVisibility(View.GONE);
                    mImageView.setVisibility(View.GONE);
//                    detaileinfo_social.setTextColor(getResources().getColor(R.color.title));
//                    detaileinfo_details.setTextColor(getResources().getColor(R.color.title_selected));
//                    detaileinfo_deal.setTextColor(getResources().getColor(R.color.title));
                    an = new TranslateAnimation((getWindowManager().getDefaultDisplay().getWidth()) / 3 * 2, (getWindowManager().getDefaultDisplay().getWidth()) / 3, 0, 0);
                    an.setFillAfter(true);
                    detaileinfo_line_1.startAnimation(an);
                    //viewpager.setScrollble(true);
                    break;
                case 2:
//                    relativeLayout.setVisibility(View.GONE);
//                    buy_more.setVisibility(View.GONE);
                    mImageView.setVisibility(View.GONE);
//                    detaileinfo_social.setTextColor(getResources().getColor(R.color.title));
//                    detaileinfo_details.setTextColor(getResources().getColor(R.color.title));
//                    detaileinfo_deal.setTextColor(getResources().getColor(R.color.title_selected));
                    an = new TranslateAnimation((getWindowManager().getDefaultDisplay().getWidth()), (getWindowManager().getDefaultDisplay().getWidth()) / 3 * 2, 0, 0);
                    an.setFillAfter(true);
                    detaileinfo_line_1.startAnimation(an);
                    //viewpager.setScrollble(false);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };



    PopupWindow mPopWindow;

    //弹出popwindow  选择职业  性别等
    public void ShowSelectPop(View view) {
        //View pop_view=getLayoutInflater().inflate(R.layout.layoutId,null);
        //View view = getLayoutInflater().inflate(R.layout.layout_popwindow_gender,null);
        //Drawable selectDrawable= getResources().getDrawable(R.drawable.woman);
        //TextView selectTextview=view.findViewById(R.id.)
        mPopWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //LinearLayout


    }


    //弹出电话对话框
    public void showTelDialog() {
        builder = new AlertDialog.Builder(DetailedInformationActivity.this);
        LayoutInflater layout = LayoutInflater.from(DetailedInformationActivity.this);
        //View sudokulistView = layout.inflate(R.layout.calldialog, null);
        //builder.setView(sudokulistView);
        builder.setCancelable(true);
        builder.setTitle("拨号");
        //final TextView tvSudokuPreview = (TextView) sudokulistView.findViewById(R.id.tvSudokuPreview);
        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + viewpager2_fans_tel.getText().toString()));
                startActivity(intentPhone);
                // Dialog不关闭处理
                try {
                    Field field = dialog.getClass().getSuperclass()
                            .getDeclaredField("mShowing");
                    field.setAccessible(true);
                    // set false?
                    field.setBoolean(dialog, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dialog关闭处理
                        try {
                            Field field = dialog.getClass().getSuperclass()
                                    .getDeclaredField("mShowing");
                            field.setAccessible(true);
                            // set true
                            field.setBoolean(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }


    @Override
    public void onClick(View view) {

        String compile_title;//编辑的标题
        ShowListDialog mShowing = new ShowListDialog();//ShowListDialog的对象
        String return_str;//返回的选中结果
        ModifyFansParam modifyFansParam = new ModifyFansParam();//需要传递的参数对象
        modifyFansParam.accountId = accountId;
        modifyFansParam.fanId = fans_id;
        View mPopView = null;

        switch (view.getId()) {
            case R.id.fans_info_layout_finish:
                mRelativeLayout.setVisibility(View.GONE);
                break;
            case R.id.fans_info_button_finish:
                mRelativeLayout.setVisibility(View.GONE);
                break;
//            case R.id.img_left_corner:
//                finish();
//                break;
            //新增购买意向
//            case R.id.member_info_buymore_3:
//                Utils.toast(this, "增加购买意向！");
//                if (accountId == 0) {
//                    Utils.toast(this, "数据错误！！");
//                } else {
//                    Intent intent = new Intent(this, BuyInclinationActivity.class);
//                    intent.putExtra("accountId", accountId);
//                    startActivity(intent);
//                }
//
//                break;
            //点击pop删除购买意向
            case R.id.pop_delete:
                if (null != popupWindow) {
                    int purchaseIntentId = 0;
                    if (want_list != null) {
                        want_list = fan.purchaseIntents;
                        FanInfo.PurchaseIntents purchaseIntents = want_list.get(position);
                        purchaseIntentId = purchaseIntents.id;
                    }

                    //删除一个粉丝购买意向
                    getDeletelist(accountId, purchaseIntentId);
                    popupWindow.dismiss();
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().setAttributes(lp);
                    popupWindow = null;
                }

                break;
            //popwindow的取消
            case R.id.pop_cancel:
                if (null != popupWindow) {
                    popupWindow.dismiss();
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1.0f;
                    getWindow().setAttributes(lp);
                    popupWindow = null;
                }
                break;
            //点击popwindow的别处   让pop消失
            case R.id.pop_layout:
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }

                break;
            //发送消息
            case R.id.send_message:
                if (fan != null) {
                    //向数据库里插入数据
                    ApiFansRecordDb mApiFansRecord = new ApiFansRecordDb(this);
                    FansRecordModel mFansRecordModel = new FansRecordModel();
                    mFansRecordModel.accountId = fans_id;
                    mFansRecordModel.eid = PreferenceUtils.getInt(this, Constant.PREFERENCE_EMP_ID, -1);
                    mFansRecordModel.avatar = fan.avatar;
                    mFansRecordModel.city = fan.city;
                    mFansRecordModel.followStatus = fan.followStatus;
                    mFansRecordModel.gender = fan.gender;
                    mFansRecordModel.interactionTimes = fan.interactionTimes;
                    mFansRecordModel.lastInteractionTime = fan.lastInteractionTime;
                    mFansRecordModel.nickName = fan.nickName;
                    mFansRecordModel.province = fan.province;
                    mFansRecordModel.subscribeTime = fan.subscribeTime;
                    mFansRecordModel.realName = fan.realName;
                    mFansRecordModel.time = System.currentTimeMillis();
                    mApiFansRecord.insert(mFansRecordModel);

                    Intent intentSendMessage = new Intent(this, ChatActivity.class);
                    intentSendMessage.putExtra(Constant.CHAT_CONTACT_NAME, fan.nickName);
                    intentSendMessage.putExtra(Constant.CHAT_CONTACT_FAN_ID, fans_id);
                    intentSendMessage.putExtra(Constant.CHAT_CONTACT_HEAD, fan.avatar);
                    startActivity(intentSendMessage);
                    finish();
                } else if (fan == null) {
                    Utils.toast(this, "获取粉丝信息失败！");
                }
                break;
            //加入分组
            case R.id.join_group:
                Intent intentJoin = new Intent(this, JoinGroupAcitivity.class);
                intentJoin.putExtra(Constant.FANS_ID, fans_id);
                startActivity(intentJoin);

                break;

            //Tab的点击事件
            case R.id.detaileinfo_social:
                //让第几个viewpager被选中
                viewpager.setCurrentItem(0);
                break;
            case R.id.detaileinfo_details:
                //让第几个viewpager被选中
                viewpager.setCurrentItem(1);
                break;
            case R.id.detaileinfo_deal:
                //让第几个viewpager被选中
                viewpager.setCurrentItem(2);
                break;
            case R.id.detailinfo2_phone_call:
                //跳转到电话
                showTelDialog();
                break;
            //账户信息的编辑
//            case R.id.account_information_compile_gender:
////                compile_title="性别";
////                String[] genderStr={"男","女","未知"};
////                //弹出Listdialog
////                mShowing.showSudokuListDialog(mShowing.GetCity(genderStr), DetailedInformationActivity.this, compile_title, viewpager2_fans_gender,accountId);
//                //modifyFansParam.gender=
//
//                mPopView = getLayoutInflater().inflate(R.layout.layout_popwindow_gender, null);
//                ShowSelectPop(mPopView);
//                mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//                genderSetOnClick(mPopView);
//                setGenderImageVisiable(viewpager2_fans_gender);
//                break;
            case R.id.detailinfo2_gender_layout_outside:
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_gender_select_man:
                modifyFansParam.gender = 1;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                genderManImageView.setVisibility(View.VISIBLE);
                viewpager2_fans_gender.setText("男");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_gender_select_woman:
                modifyFansParam.gender = 2;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                genderWomanImageView.setVisibility(View.VISIBLE);
                viewpager2_fans_gender.setText("女");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_gender_select_no:
                modifyFansParam.gender = 3;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                genderNoImageView.setVisibility(View.VISIBLE);
                viewpager2_fans_gender.setText("未知");
                mPopWindow.dismiss();
                break;

//            case R.id.account_information_compile_job:
////                compile_title="职业";
////                String[] jobStr={"个体户老板","初级白领","外来务工","学生","家庭主妇","企业单位管理人员","其他"};
////                mShowing.showSudokuListDialog(mShowing.GetCity(jobStr), DetailedInformationActivity.this, compile_title, viewpager2_fans_job,accountId);
//                mPopView = getLayoutInflater().inflate(R.layout.layout_pop_job, null);
//                ShowSelectPop(mPopView);
//                mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//                jobSetOnClick(mPopView);
//                setJobImageVisiable(viewpager2_fans_job);
//                break;
            case R.id.detailinfo2_job_layout_outside:
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_job_individual:
                modifyFansParam.occupation = "个体户老板";
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_job.setText("个体户老板");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_job_junior_white_collar:
                modifyFansParam.occupation = "初级白领";
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_job.setText("初级白领");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_job_exotic_laborers:
                modifyFansParam.occupation = "外来务工";
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_job.setText("外来务工");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_job_student:
                modifyFansParam.occupation = "学生";
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_job.setText("学生");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_job_housewife:
                modifyFansParam.occupation = "家庭主妇";
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_job.setText("家庭主妇");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_job_manager:
                modifyFansParam.occupation = "企业单位管理人员";
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_job.setText("企业单位管理人员");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_job_other:
                modifyFansParam.occupation = "其他";
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                //viewpager2_fans_job.setText("其他");
                mPopWindow.dismiss();
                mShowing.JobInfoCompile(DetailedInformationActivity.this, viewpager2_fans_job, modifyFansParam, accountId, fans_id);
                break;

//            case R.id.account_information_compile_tel:
//                compile_title = "tel";
//                new ShowListDialog().goInput(compile_title, DetailedInformationActivity.this, viewpager2_fans_tel, accountId, fans_id);
//                break;
//            case R.id.account_information_compile_name:
//                compile_title = "name";
//                new ShowListDialog().goInput(compile_title, DetailedInformationActivity.this, viewpager2_fans_name, accountId, fans_id);
//                break;
//
//
//            case R.id.account_information_compile_age:
//                mPopView = getLayoutInflater().inflate(R.layout.layout_pop_age, null);
//                ShowSelectPop(mPopView);
//                mPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//                ageSetOnClick(mPopView);
//                setAgeImageVisiable(viewpager2_fans_age);
//                break;
            case R.id.detailinfo2_age_layout_outside:
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_age_15_20:
                modifyFansParam.ageGroup = 1;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_age.setText("15-20");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_age_20_25:
                modifyFansParam.ageGroup = 2;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_age.setText("20-25");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_age_25_30:
                modifyFansParam.ageGroup = 3;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_age.setText("25-30");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_age_30_35:
                modifyFansParam.ageGroup = 4;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_age.setText("30-35");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_age_35_40:
                modifyFansParam.ageGroup = 5;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_age.setText("35-40");
                mPopWindow.dismiss();
                break;
            case R.id.detailinfo2_age_40_99:
                modifyFansParam.ageGroup = 6;
                mShowing.postCompile(modifyFansParam, DetailedInformationActivity.this);
                viewpager2_fans_age.setText("40以上");
                mPopWindow.dismiss();
                break;
        }
    }


    private ImageView genderManImageView, genderWomanImageView, genderNoImageView;

    public void genderSetOnClick(View mPopView) {
        mPopView.findViewById(R.id.detailinfo2_gender_layout_outside).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_gender_select_man).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_gender_select_woman).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_gender_select_no).setOnClickListener(this);

        genderManImageView = (ImageView) mPopView.findViewById(R.id.detailinfo2_gender_select_man_image);
        genderWomanImageView = (ImageView) mPopView.findViewById(R.id.detailinfo2_gender_select_woman_image);
        genderNoImageView = (ImageView) mPopView.findViewById(R.id.detailinfo2_gender_select_no_image);

    }

    public void setGenderImageVisiable(TextView mText) {
        if ("男".equals(mText.getText().toString())) {
            genderManImageView.setVisibility(View.VISIBLE);
        } else if ("女".equals(mText.getText().toString())) {
            genderWomanImageView.setVisibility(View.VISIBLE);
        } else {
            genderNoImageView.setVisibility(View.VISIBLE);
        }
    }

    //选择职业  图片
    private ImageView jobIndividualImage, jobWhitecCollarImage, jobExoticLlaborersImage, jobStudentImage, jobHouseWifeImage, jobManagerImage, jobOtherImage;

    public void jobSetOnClick(View mPopView) {
        mPopView.findViewById(R.id.detailinfo2_job_layout_outside).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_job_individual).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_job_junior_white_collar).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_job_exotic_laborers).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_job_student).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_job_housewife).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_job_manager).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_job_other).setOnClickListener(this);

        jobIndividualImage = (ImageView) mPopView.findViewById(R.id.detailinfo2_job_individual_image);
        jobWhitecCollarImage = (ImageView) mPopView.findViewById(R.id.detailinfo2_job_junior_white_collar_image);
        jobExoticLlaborersImage = (ImageView) mPopView.findViewById(R.id.detailinfo2_job_exotic_laborers_image);
        jobStudentImage = (ImageView) mPopView.findViewById(R.id.detailinfo2_job_student_image);
        jobHouseWifeImage = (ImageView) mPopView.findViewById(R.id.detailinfo2_job_housewife_image);
        jobManagerImage = (ImageView) mPopView.findViewById(R.id.detailinfo2_job_manager_image);
        jobOtherImage = (ImageView) mPopView.findViewById(R.id.detailinfo2_job_other_image);

    }

    public void setJobImageVisiable(TextView mText) {
        if ("个体户老板".equals(mText.getText().toString())) {
            jobIndividualImage.setVisibility(View.VISIBLE);
        } else if ("初级白领".equals(mText.getText().toString())) {
            jobWhitecCollarImage.setVisibility(View.VISIBLE);
        } else if ("外来务工".equals(mText.getText().toString())) {
            jobExoticLlaborersImage.setVisibility(View.VISIBLE);
        } else if ("学生".equals(mText.getText().toString())) {
            jobStudentImage.setVisibility(View.VISIBLE);
        } else if ("家庭主妇".equals(mText.getText().toString())) {
            jobHouseWifeImage.setVisibility(View.VISIBLE);
        } else if ("企业单位管理人员".equals(mText.getText().toString())) {
            jobManagerImage.setVisibility(View.VISIBLE);
        } else {
            jobOtherImage.setVisibility(View.VISIBLE);
        }
    }

    private ImageView age1Image, age2Image, age3Image, age4Image, age5Image, age6Image;

    public void ageSetOnClick(View mPopView) {
        mPopView.findViewById(R.id.detailinfo2_age_layout_outside).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_age_15_20).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_age_20_25).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_age_25_30).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_age_30_35).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_age_35_40).setOnClickListener(this);
        mPopView.findViewById(R.id.detailinfo2_age_40_99).setOnClickListener(this);

        age1Image = (ImageView) mPopView.findViewById(R.id.detailinfo2_age_15_20_image);
        age2Image = (ImageView) mPopView.findViewById(R.id.detailinfo2_age_20_25_image);
        age3Image = (ImageView) mPopView.findViewById(R.id.detailinfo2_age_25_30_image);
        age4Image = (ImageView) mPopView.findViewById(R.id.detailinfo2_age_30_35_image);
        age5Image = (ImageView) mPopView.findViewById(R.id.detailinfo2_age_35_40_image);
        age6Image = (ImageView) mPopView.findViewById(R.id.detailinfo2_age_40_99_image);
    }

    public void setAgeImageVisiable(TextView mText) {
        if ("15-20".equals(mText.getText().toString())) {
            age1Image.setVisibility(View.VISIBLE);
        } else if ("20-25".equals(mText.getText().toString())) {
            age2Image.setVisibility(View.VISIBLE);
        } else if ("25-30".equals(mText.getText().toString())) {
            age3Image.setVisibility(View.VISIBLE);
        } else if ("30-35".equals(mText.getText().toString())) {
            age4Image.setVisibility(View.VISIBLE);
        } else if ("35-40".equals(mText.getText().toString())) {
            age5Image.setVisibility(View.VISIBLE);
        } else if("40以上".equals(mText.getText().toString())){
            age6Image.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("黄安", "onRestart");
        HttpRes(fans_id);//在其他界面返回以后，重新获取员工信息
    }


    @Override
    protected void onPause() {
        super.onPause();
        dismissCustomDialog();
        Log.i("黄安", "onPause");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("黄安", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("黄安", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("黄安", "onStop");
    }


    /**
     * 新app的水波纹点击事件
     */

    private WaveCompat.mCallBack callBack;//接口的回调
    private RelativeLayout mRelativeLayout;
    ImageView mImageView;//点击出现水波纹效果

    public void findImage(){
        mImageView = (ImageView) findViewById(R.id.imageView);
        WaveTouchHelper.bindWaveTouchHelper(mImageView, this);//
        mRelativeLayout= (RelativeLayout) findViewById(R.id.fans_info_layout_finish);
        mRelativeLayout.setOnClickListener(this);

        findViewById(R.id.fans_info_button_finish).setOnClickListener(this);
        /**
         * 在动画的完成后显示一个view
         */
        callBack=new WaveCompat.mCallBack() {
            @Override
            public void resault() {
                mRelativeLayout.setVisibility(View.VISIBLE);
            }
        };
    }
    @Override
    public void onWaveTouchUp(View view, Point locationInView, Point locationInScreen) {
        switch (view.getId()){
            case R.id.imageView:
//                str=WaveCompat.startWaveFilter(getActivity(),
//                        new WaveDrawable()
//                                .setColor(0xddffffff) //0xddffffff
//                                .setTouchPoint(locationInScreen),
//                        generateIntent(0xddffffff,getActivity()),imageView);
//                //showPoupopWindow(getActivity());
//                imageView.setVisibility(View.VISIBLE);

                WaveCompat.startWaveFilter(this, new WaveDrawable().setColor(0x99ffffff)
                        .setTouchPoint(locationInScreen), callBack);
                break;
        }
    }


//    @Override
//    public boolean onLongClick(View view) {
//        switch (view.getId()){
//            case R.id.menber_phone_text_2:
//
//                // 获取剪贴板管理服务
//                ClipboardManager cm = (ClipboardManager) this
//                        .getSystemService(Context.CLIPBOARD_SERVICE);
//                // 将文本数据复制到剪贴板
//                cm.setText(viewpager2_fans_tel.getText().toString());
//                break;
//
//        }
//        return true;
//    }
}
