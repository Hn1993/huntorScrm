package com.huntor.mscrm.app2.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.DrawLayoutLeftAdapter;
import com.huntor.mscrm.app2.model.FansRecordModel;
import com.huntor.mscrm.app2.model.MessageRecordModel;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFans;
import com.huntor.mscrm.app2.provider.api.ApiFansRecordDb;
import com.huntor.mscrm.app2.push.PushMessageManager;
import com.huntor.mscrm.app2.push.PushMessageReceiverService;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.fragment.interaction.InteractionLocaleFragment;
import com.huntor.mscrm.app2.ui.fragment.member.MyMemberFragment;
import com.huntor.mscrm.app2.ui.fragment.online.InteractionOnlineFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;


import java.util.ArrayList;


public class MainActivity2 extends BaseActivity implements View.OnClickListener {
    private String TAG = "MainActivity2";
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;


    private ArrayList<Integer> drawLeftList;
    private DrawLayoutLeftAdapter mAdapter;

    private android.app.FragmentManager fragmentManager;

    private PushMessageManager messageManager;
    private Button mButton;
    private TextView txtOnlineMessageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        isLogined();
        if (isLogin) {
            Intent serviceIntent = new Intent(this, PushMessageReceiverService.class);
            this.startService(serviceIntent);
        }
        findViews();

        toolbar.setTitle("\t\t现场交互");//设置Toolbar标题
        //toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //设置标题颜色
        setSupportActionBar(toolbar);
        // toolbar.setOnMenuItemClickListener(menuLitener_toolbar);//设置menu
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置菜单列表
        initData();


        fragmentManager = getFragmentManager();
        android.app.FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        InteractionLocaleFragment mbf = new InteractionLocaleFragment();
        transaction.add(R.id.fl_content, mbf);
        transaction.commit();

        messageManager = PushMessageManager.getInstance(this);
        messageManager.registerOnReceivedPushMessageListener(opl);
        // messageManager.registerOnReceivedPushMessageListener(listener);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);

    }

    Toolbar.OnMenuItemClickListener menuLitener_toolbar = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Utils.toast(MainActivity2.this, "setting");
                    break;
                case R.id.action_settings1:
                    Utils.toast(MainActivity2.this, "setting");
                    break;
                case R.id.action_settings2:
                    Utils.toast(MainActivity2.this, "setting");
                    break;
                case R.id.action_search:
                    Utils.toast(MainActivity2.this, "setting");
                    break;
            }

            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        String name = PreferenceUtils.getString(context, Constant.PREFERENCE_EMP_NAME, "");
        if (TextUtils.isEmpty(name)) {
            finish();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        messageManager.unregisterOnReceivedPushMessageListener(opl);
        super.onPause();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        drawLeftList = new ArrayList<>();
        mAdapter = new DrawLayoutLeftAdapter(MainActivity2.this);
        for (int i = 0; i < 4; i++) {
            drawLeftList.add(i);
        }
        lvLeftMenu.setAdapter(mAdapter);
        mAdapter.addendData(drawLeftList, false);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
                mDrawerLayout.closeDrawers();

                if (i == 0) {
                    InteractionLocaleFragment itf = new InteractionLocaleFragment();
                    transaction.replace(R.id.fl_content, itf);
                    toolbar.setTitle("现场交互");
                } else if (i == 1) {
                    InteractionOnlineFragment otf = new InteractionOnlineFragment();
                    transaction.replace(R.id.fl_content, otf);
                    toolbar.setTitle("在线交互");
                } else if (i == 2) {
                    MyMemberFragment mbf = new MyMemberFragment();
                    transaction.replace(R.id.fl_content, mbf);
                    toolbar.setTitle("我的会员");

                } else if (i == 3) {//添加其他功能

                }
                clearBackStack();
                transaction.commitAllowingStateLoss();
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 清空回退栈
     */
    private void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_sync:
                break;
        }
    }


    /**
     * 上一次点击Back按键的时间
     */
    private long lastBackTime = 0;

    /**
     * 当前 Activity 显示的时候,点击屏幕上的返回键的事件监听处理方法
     */
    @Override
    public void onBackPressed() {

        //1,获取当前的毫秒数
        long currentTime = System.currentTimeMillis();
        //2,和上一次点击返回键的时间进行比较,相差多少秒就退出

        if (fragmentManager.popBackStackImmediate()) {
            //处理Fragment回退栈的逻辑
        } else if (currentTime - lastBackTime <= 2000) {
            //利用 super.onBackPressed() 让 Activity 自己处理返回键
            //或者利用 finish() 结束
            super.onBackPressed();
            //也可以通过 System.exit(0) 这个方法,终止整个应用程序进程
            //进程终止,类似 Service 之类的组件将不再继续运行
        } else {
            lastBackTime = currentTime;
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
        }

    }


//    private Boolean has_update = false;
//    //友盟更新
//    private void checkUpdate(final boolean isManual) {
//        String new_version = tv_new_version.getText().toString();
//        if (isManual && !TextUtils.isEmpty(new_version)) {
//            UmengUpdateAgent.forceUpdate(context);
//            return;
//        }
//        UmengUpdateAgent.setUpdateAutoPopup(false);
//        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//            @Override
//            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
//                switch (updateStatus) {
//                    case UpdateStatus.Yes:
//                        has_update = true;
//                        UmengUpdateAgent.showUpdateDialog(context, updateResponse);
//                        break;
//                    case UpdateStatus.No:
//                        has_update = false;
//                        if (isManual) {
//                            Utils.toast(context, "没有更新！");
//                        }
//                        break;
//                }
//                tv_new_version.setVisibility(has_update ? View.VISIBLE : View.GONE);
//                tv_new_version.setText(has_update ? "新" : "");
//            }
//        });
//        UmengUpdateAgent.update(context);
//    }

    private boolean isLogin;

    private void isLogined() {
        String empName = PreferenceUtils.getString(this, Constant.PREFERENCE_EMP_NAME, "");
        if (TextUtils.isEmpty(empName)) {
            isLogin = false;
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            isLogin = true;
        }
    }

    private PushMessageManager.OnReceivedPushMessageListener opl = new PushMessageManager.OnReceivedPushMessageListener() {

        @Override
        public void onReceivedFansMessage(Object message) {
            MessageRecordModel pushMessage = (MessageRecordModel) message;
            int fid = pushMessage.fid;
            MyLogger.e(TAG, "fid = " + fid);
            MyLogger.e(TAG, "pushMessage = " + pushMessage.toString());
            FansRecordModel fanModel = ApiFansRecordDb.getFansInfoById(context, fid);
            if (fanModel == null) {
                getFansDetail(fid, true);
            } else {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
    };

    private void getFansDetail(final int fan_id, final boolean isSave) {
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
                                fanModel.realName = response.fanInfo.realName;
                                fanModel.nickName = response.fanInfo.nickName;
                                fanModel.province = response.fanInfo.province;
                                fanModel.city = response.fanInfo.city;
                                fanModel.followStatus = response.fanInfo.followStatus;
                                fanModel.gender = response.fanInfo.gender;
                                fanModel.interactionTimes = response.fanInfo.interactionTimes;
                                fanModel.lastInteractionTime = response.fanInfo.lastInteractionTime;
                                fanModel.subscribeTime = response.fanInfo.subscribeTime;
                                if (isSave) {
                                    Uri fansRecordModelUrl = new ApiFansRecordDb(context).insert(fanModel);
                                    if (fansRecordModelUrl == null) {
                                        //TODO 该粉丝插入不成功
                                        MyLogger.w(TAG, "粉丝加入数据库不成功");
                                    }
                                }
                                MyLogger.e(TAG, "fanModel" + fanModel.toString());
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        } else {
                            Utils.toast(context, response.getRetInfo() + "");
                        }
                    }
                });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // setMessageNumber();
                    break;
                case 2:
                    int notecount = msg.getData().getInt("notecount", 0);
                    // setMsgNum(notecount);
                    break;
            }
        }
    };
}