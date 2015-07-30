package com.huntor.mscrm.app.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.adapter.DrawLayoutLeftAdapter;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.fragment.interaction.InteractionLocaleFragment;
import com.huntor.mscrm.app.ui.fragment.member.MyMemberFragment;
import com.huntor.mscrm.app.ui.fragment.online.InteractionOnlineFragment;
import com.huntor.mscrm.app.utils.Utils;

import java.util.ArrayList;



public class MainActivity2 extends BaseActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;


    private ArrayList<Integer> drawLeftList;
    private DrawLayoutLeftAdapter mAdapter;

    private android.app.FragmentManager fragmentManager;
    //private android.app.FragmentTransaction transaction;

    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);




        findViews();

        toolbar.setTitle("我的会员");//设置Toolbar标题
        //toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //设置标题颜色

        setSupportActionBar(toolbar);
        //toolbar.setOnMenuItemClickListener(menuLitener_toolbar);//设置menu
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
        android.app.FragmentTransaction transaction; transaction = fragmentManager.beginTransaction();
        InteractionLocaleFragment mbf = new InteractionLocaleFragment();
        transaction.add(R.id.fl_content, mbf);
        transaction.commit();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }

//    Toolbar.OnMenuItemClickListener menuLitener_toolbar=new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            switch(item.getItemId()){
//                case R.id.action_settings:
//                    Utils.toast(MainActivity2.this, "setting");
//                    break;
//                case R.id.action_settings1:
//                    Utils.toast(MainActivity2.this, "setting");
//                    break;
//                case R.id.action_settings2:
//                    Utils.toast(MainActivity2.this, "setting");
//                    break;
//                case R.id.action_search:
//                    Utils.toast(MainActivity2.this, "setting");
//                    break;
//            }
//
//            return true;
//        }
//    };
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.action_settings:
//                return true;
//            case R.id.action_search:
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * 初始化数据
     */
    private void initData(){
        drawLeftList=new ArrayList<>();
        mAdapter=new DrawLayoutLeftAdapter(MainActivity2.this);
        for (int i=0;i<4;i++){
            drawLeftList.add(i);
        }
        lvLeftMenu.setAdapter(mAdapter);
        mAdapter.addendData(drawLeftList,false);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                android.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
                mDrawerLayout.closeDrawers();

                if(i==0){
                    InteractionLocaleFragment itf=new InteractionLocaleFragment();
                    transaction.replace(R.id.fl_content, itf);
                    toolbar.setTitle("\t\t现场交互");
                }else if(i==1){
                    InteractionOnlineFragment otf=new InteractionOnlineFragment();
                    transaction.replace(R.id.fl_content,otf);
                    toolbar.setTitle("\t\t在线交互");

                }else if(i==2){
                    MyMemberFragment mbf = new MyMemberFragment();
                    transaction.replace(R.id.fl_content, mbf);
                    toolbar.setTitle("\t\t我的会员");

                }else if(i==3){//添加其他功能

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
        switch (view.getId()){
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

}
