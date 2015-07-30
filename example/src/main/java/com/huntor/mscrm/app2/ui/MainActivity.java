//package com.huntor.mscrm.app.ui;
//
//import android.app.AlertDialog;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.drawable.Drawable;
//
//import android.hardware.Camera;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.Settings;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.widget.DrawerLayout;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.*;
//import com.huntor.mscrm.app.R;
//import com.huntor.mscrm.app.adapter.DrawLayoutLeftAdapter;
//import com.huntor.mscrm.app.model.FansRecordModel;
//import com.huntor.mscrm.app.model.MessageRecordModel;
//import com.huntor.mscrm.app.model.PullMessageNote;
//import com.huntor.mscrm.app.net.BaseResponse;
//import com.huntor.mscrm.app.net.HttpRequestController;
//import com.huntor.mscrm.app.net.HttpResponseListener;
//import com.huntor.mscrm.app.net.api.ApiFans;
//import com.huntor.mscrm.app.provider.api.ApiFansRecordDb;
//import com.huntor.mscrm.app.provider.api.ApiMessageRecordDb;
//import com.huntor.mscrm.app.push.PushMessageManager;
//import com.huntor.mscrm.app.push.PushMessageReceiverService;
//import com.huntor.mscrm.app.ui.component.BaseActivity;
//import com.huntor.mscrm.app.ui.fragment.interaction.InteractionLocaleFragment;
//import com.huntor.mscrm.app.ui.fragment.member.GroupMemberFragment2;
//import com.huntor.mscrm.app.ui.fragment.member.MyMemberFragment;
//import com.huntor.mscrm.app.ui.fragment.online.InteractionOnlineFragment;
//import com.huntor.mscrm.app.utils.Constant;
//import com.huntor.mscrm.app.utils.MyLogger;
//import com.huntor.mscrm.app.utils.PreferenceUtils;
//import com.huntor.mscrm.app.utils.Utils;
//import com.umeng.update.UmengUpdateAgent;
//import com.umeng.update.UmengUpdateListener;
//import com.umeng.update.UpdateResponse;
//import com.umeng.update.UpdateStatus;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
//
//	private static final String TAG = "MainActivity";
//	private static final int REQUEST = 100;
//	private MainActivity context = MainActivity.this;
//	private TextView txtOnlineMessageNumber;
//	private PushMessageManager messageManager;
//
//	/**
//	 * 侧滑菜单
//	 */
////	public DrawerLayout drawerLayout;
////	private ActionBarDrawerToggle drawerbar;
//	private RelativeLayout mRl;
//	/**
//	 * 注销
//	 */
//	private TextView mLogout;
//	/**
//	 * 修改密码
//	 */
//	private TextView modifyPwd;
//	/**
//	 * 版本号
//	 */
//	private TextView mVersionCode;
//	private TextView mCheckUpdate;//检查更新
//	private Intent serviceIntent;
//	/**
//	 * 设置小尾巴
//	 */
//	private TextView mSetTails;
//	private boolean isLogin;
//	/**
//	 * 消息中心
//	 */
//	private TextView mMsgCenter;
//	private TextView mMsgNum;
//	private TextView tv_new_version;
//	private ConnectionChangeReceiver receiver;
//	private LinearLayout ll_net_issue;
//	private Boolean has_update = false;
//
//
//	/**
//	 * 新版控件
//	 */
//	private android.support.v7.widget.Toolbar toolbar;
//	private DrawerLayout mDrawerLayout;
//	private ActionBarDrawerToggle mDrawerToggle;
//	private ListView lvLeftMenu;
//
//
//	private ArrayList<Integer> drawLeftList;
//	private DrawLayoutLeftAdapter mAdapter;
//
//	private android.app.FragmentManager fragmentManager;
//	//private android.app.FragmentTransaction transaction;
//
//	private Button mButton;
//
//	public void copyFile(String oldPath, String newPath) {
//		try {
//			File old_file = new File(oldPath);
//			if (old_file.exists()) { //文件不存在时
//				InputStream inStream = new FileInputStream(oldPath); //读入原文件
//				FileOutputStream fs = new FileOutputStream(newPath);
//				byte[] buffer = new byte[1024 * 4];
//				int length;
//				while ((length = inStream.read(buffer)) != -1) {
//					fs.write(buffer, 0, length);
//				}
//				inStream.close();
//				Utils.toast(getApplicationContext(), R.string.copy_success);
//			}
//		} catch (Exception e) {
//			Utils.toast(getApplicationContext(), R.string.copy_fail);
//			e.printStackTrace();
//		}
//	}
//
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
////        PropertyConfigurator.getConfigurator(this).configure();
////        final FileAppender fileAppender = (FileAppender) Utils.logger.getAppender(1);
////        fileAppender.setFileName("mylog.txt");
////        fileAppender.setAppend(true);
//
//		setContentView(R.layout.activity_main3);
//		isLogined();
//		String empName = PreferenceUtils.getString(this, Constant.PREFERENCE_EMP_NAME, "");
//		MyLogger.w(TAG, "empName = " + empName);
//		if (isLogin) {
//			serviceIntent = new Intent(this, PushMessageReceiverService.class);
//			this.startService(serviceIntent);
//		}
//		//initView();
//		checkUpdate(false);
//
////      TCPLongLinkManager.getInstance(this).startConnect();
//		messageManager = PushMessageManager.getInstance(this);
//		messageManager.registerOnReceivedPushMessageListener(opl);
//		messageManager.registerOnReceivedPushMessageListener(listener);
//
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//		receiver = new ConnectionChangeReceiver();
//		registerReceiver(receiver, filter);
//
//
//
//
//		//new app
//		findViews();
//
//		toolbar.setTitle("我的会员");//设置Toolbar标题
//		//toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
//		toolbar.setTitleTextColor(getResources().getColor(R.color.white)); //设置标题颜色
//
//		setSupportActionBar(toolbar);
//		toolbar.setOnMenuItemClickListener((android.support.v7.widget.Toolbar.OnMenuItemClickListener) menuLitener_toolbar);//设置menu
//		getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
//
//		//创建返回键，并实现打开关/闭监听
//		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
//			@Override
//			public void onDrawerOpened(View drawerView) {
//				super.onDrawerOpened(drawerView);
//
//			}
//
//			@Override
//			public void onDrawerClosed(View drawerView) {
//				super.onDrawerClosed(drawerView);
//
//			}
//		};
//		mDrawerToggle.syncState();
//		mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//		//设置菜单列表
//		initData();
//
//
//		fragmentManager = getFragmentManager();
//		android.app.FragmentTransaction transaction; transaction = fragmentManager.beginTransaction();
//		MyMemberFragment mbf = new MyMemberFragment();
//		transaction.add(R.id.fl_content, mbf);
//		transaction.commit();
//	}
//
//	private void findViews() {
//		toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tl_custom);
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
//		lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
//		//mButton= (Button) findViewById(R.id.button_bt_raise_wave_color);
//		//mButton.setOnClickListener(listener_delay);
//	}
//
//	Toolbar.OnMenuItemClickListener menuLitener_toolbar=new Toolbar.OnMenuItemClickListener() {
//		@Override
//		public boolean onMenuItemClick(MenuItem item) {
//			switch(item.getItemId()){
//				case R.id.action_settings:
//					Utils.toast(MainActivity.this, "setting");
//					break;
//				case R.id.action_settings1:
//					Utils.toast(MainActivity.this,"setting1");
//					break;
//				case R.id.action_settings2:
//					Utils.toast(MainActivity.this,"setting2");
//					break;
//				case R.id.action_search:
//					Utils.toast(MainActivity.this,"search");
//					break;
//			}
//
//			return true;
//		}
//	};
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.menu_main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		switch (id){
//			case R.id.action_settings:
//				return true;
//			case R.id.action_search:
//				return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	/**
//	 * 初始化数据
//	 */
//	private void initData(){
//		drawLeftList=new ArrayList<>();
//		mAdapter=new DrawLayoutLeftAdapter(MainActivity.this);
//		for (int i=0;i<4;i++){
//			drawLeftList.add(i);
//		}
//		lvLeftMenu.setAdapter(mAdapter);
//		mAdapter.addendData(drawLeftList,false);
//		lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//				android.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
//				mDrawerLayout.closeDrawers();
//
//				if(i==0){
//					InteractionLocaleFragment itf=new InteractionLocaleFragment();
//					transaction.replace(R.id.fl_content, itf);
//					toolbar.setTitle("现场交互");
//				}else if(i==1){
//					InteractionOnlineFragment otf=new InteractionOnlineFragment();
//					transaction.replace(R.id.fl_content,otf);
//					toolbar.setTitle("在线交互");
//
//				}else if(i==2){
//					MyMemberFragment mbf = new MyMemberFragment();
//					transaction.replace(R.id.fl_content, mbf);
//					toolbar.setTitle("我的会员");
//
//				}else if(i==3){//添加其他功能
//
//				}
//				clearBackStack();
//				transaction.commitAllowingStateLoss();
//				mAdapter.notifyDataSetChanged();
//			}
//		});
//
//	}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//	//友盟更新
//	private void checkUpdate(final boolean isManual) {
//		String new_version = tv_new_version.getText().toString();
//		if(isManual && !TextUtils.isEmpty(new_version)){
//			UmengUpdateAgent.forceUpdate(context);
//			return;
//		}
//		UmengUpdateAgent.setUpdateAutoPopup(false);
//		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
//			@Override
//			public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
//				switch (updateStatus) {
//					case UpdateStatus.Yes:
//						has_update = true;
//						UmengUpdateAgent.showUpdateDialog(context, updateResponse);
//						break;
//					case UpdateStatus.No:
//						has_update = false;
//						if (isManual) {
//							Utils.toast(context, "没有更新！");
//						}
//						break;
//				}
//				tv_new_version.setVisibility(has_update ? View.VISIBLE : View.GONE);
//				tv_new_version.setText(has_update ? "新" : "");
//			}
//		});
//		UmengUpdateAgent.update(context);
//	}
//
//	@Override
//	protected void onResume() {
//		String name = PreferenceUtils.getString(context, Constant.PREFERENCE_EMP_NAME, "");
//		if (TextUtils.isEmpty(name)) {
//			finish();
//		}
//		setMessageNumber();
//		super.onResume();
//	}
//
//	public void setMessageNumber() {
//		List<MessageRecordModel> allUnRead = ApiMessageRecordDb.getAllUnRead(this);
//		int size = allUnRead.size();
//		if (size > 0) {
//			txtOnlineMessageNumber.setText(Integer.toString(size));
//			txtOnlineMessageNumber.setVisibility(View.VISIBLE);
//		} else {
//			txtOnlineMessageNumber.setVisibility(View.GONE);
//		}
//	}
//
//	private void isLogined() {
//		String empName = PreferenceUtils.getString(this, Constant.PREFERENCE_EMP_NAME, "");
//		if (TextUtils.isEmpty(empName)) {
//			isLogin = false;
//			startActivity(new Intent(this, LoginActivity.class));
//			finish();
//		} else {
//			isLogin = true;
//		}
//	}
//
////	private void initView() {
////		drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
////		drawerbar = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_launcher, R.string.open, R.string.close) {
////			@Override
////			public void onDrawerOpened(View drawerView) {
////				super.onDrawerOpened(drawerView);
////				int notecount = Constant.notecount;
////				setMsgNum(notecount);
////			}
////
////			@Override
////			public void onDrawerClosed(View drawerView) {
////				super.onDrawerClosed(drawerView);
////			}
////		};
////		drawerLayout.setDrawerListener(drawerbar);
////		mRl = (RelativeLayout) super.findViewById(R.id.main_left_drawer_layout);
////		ll_net_issue = (LinearLayout) findViewById(R.id.ll_net_issue);
////		ll_net_issue.setOnClickListener(new View.OnClickListener() {
////			@Override
////			public void onClick(View v) {
////				context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
////			}
////		});
////
////		initSetView();
////		txtOnlineMessageNumber = (TextView) findViewById(R.id.text_main_interaction_message_number);
////		RadioGroup layout_main_bottom = (RadioGroup) findViewById(R.id.layout_main_bottom);
////		layout_main_bottom.setOnCheckedChangeListener(this);
////
////		RadioButton rb_main_interaction = (RadioButton) findViewById(R.id.rb_main_interaction);
////		RadioButton rb_main_interaction_online = (RadioButton) findViewById(R.id.rb_main_interaction_online);
////		RadioButton rb_main_mymember = (RadioButton) findViewById(R.id.rb_main_mymember);
////		Integer dimension = (int) getResources().getDimension(R.dimen.activity_main_bottom_icon_size);
////
////		Drawable[] compoundDrawables1 = rb_main_interaction.getCompoundDrawables();
////		compoundDrawables1[1].setBounds(0, 0, dimension, dimension);
////		Drawable[] compoundDrawables2 = rb_main_interaction_online.getCompoundDrawables();
////		compoundDrawables2[1].setBounds(0, 0, dimension, dimension);
////		Drawable[] compoundDrawables3 = rb_main_mymember.getCompoundDrawables();
////		compoundDrawables3[1].setBounds(0, 0, dimension, dimension);
////
////		rb_main_interaction.setCompoundDrawables(null, compoundDrawables1[1], null, null);
////		rb_main_interaction_online.setCompoundDrawables(null, compoundDrawables2[1], null, null);
////		rb_main_mymember.setCompoundDrawables(null, compoundDrawables3[1], null, null);
////
////		fragmentManager = getFragmentManager();
////		FragmentTransaction transaction = fragmentManager.beginTransaction();
////		InteractionLocaleFragment ilf = new InteractionLocaleFragment();
////		transaction.add(R.id.frame_main, ilf);
////		transaction.commit();
////	}
//
//	/**
//	 * 上一次点击Back按键的时间
//	 */
//	private long lastBackTime = 0;
//
//	/**
//	 * 当前 Activity 显示的时候,点击屏幕上的返回键的事件监听处理方法
//	 */
//	@Override
//	public void onBackPressed() {
//		GroupMemberFragment2 fragment = (GroupMemberFragment2) fragmentManager.findFragmentByTag(Constant.GROUP_MEMBER);
//		if (fragment != null) {
//			MyLogger.i(TAG, "GroupMemberFragment2 is not null");
//			if (fragment.mAdapter.isCheckBoxShow) {
//				fragment.mAdapter.clearCheckedItems();
//				fragment.mAdapter.notifyDataSetChanged();
//				fragment.itv_add.setVisibility(View.VISIBLE);
//				fragment.itv_add.setText(R.string.icon_text_plus);
//				return;
//			}
//		}
//
//		//1,获取当前的毫秒数
//		long currentTime = System.currentTimeMillis();
//		//2,和上一次点击返回键的时间进行比较,相差多少秒就退出
//
//		if (fragmentManager.popBackStackImmediate()) {
//			MyLogger.i(TAG, "fragmentManager.popBackStackImmediate");
//		} else if (currentTime - lastBackTime <= 2000) {
//			//利用 super.onBackPressed() 让 Activity 自己处理返回键
//			//或者利用 finish() 结束
//			super.onBackPressed();
//
//			//也可以通过 System.exit(0) 这个方法,终止整个应用程序进程
//			//进程终止,类似 Service 之类的组件将不再继续运行
//
//		} else {
//			lastBackTime = currentTime;
//			Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
//		}
//
//	}
//
//	//  接收的站内信消息
//	private PushMessageManager.OnPullMessageNoteListener listener = new PushMessageManager.OnPullMessageNoteListener() {
//		@Override
//		public void OnPullMessageNote(Object pushMessage) {
//			PullMessageNote note = (PullMessageNote) pushMessage;
//			int notecount = Constant.notecount;
//			Message msg = new Message();
//			msg.what = 2;
//			Bundle bundle = new Bundle();
//			bundle.putInt("notecount", notecount);
//			msg.setData(bundle);
//			handler.sendMessage(msg);
//		}
//	};
//
//	private PushMessageManager.OnReceivedPushMessageListener opl = new PushMessageManager.OnReceivedPushMessageListener() {
//
//		@Override
//		public void onReceivedFansMessage(Object message) {
//			MessageRecordModel pushMessage = (MessageRecordModel) message;
//			int fid = pushMessage.fid;
//			MyLogger.e(TAG, "fid = " + fid);
//			MyLogger.e(TAG, "pushMessage = " + pushMessage.toString());
//			FansRecordModel fanModel = ApiFansRecordDb.getFansInfoById(context, fid);
//			if (fanModel == null) {
//				getFansDetail(fid, true);
//			} else {
//				Message msg = new Message();
//				msg.what = 1;
//				handler.sendMessage(msg);
//			}
//		}
//	};
//
//	private void getFansDetail(final int fan_id, final boolean isSave) {
//		HttpRequestController.getFansInfo(this, fan_id,
//				new HttpResponseListener<ApiFans.ApiFansResponse>() {
//					@Override
//					public void onResult(ApiFans.ApiFansResponse response) {
//						if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
//							MyLogger.i(TAG, "response.fanInfo = " + response.fanInfo);
//							if (response.fanInfo != null) {
//								FansRecordModel fanModel = new FansRecordModel();
//								fanModel.accountId = fan_id;
//								fanModel.eid = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, -1);
//								MyLogger.e(TAG, "fanModel.accountId = " + fanModel.accountId);
//								fanModel.avatar = response.fanInfo.avatar;
//								fanModel.realName = response.fanInfo.realName;
//								fanModel.nickName = response.fanInfo.nickName;
//								fanModel.province = response.fanInfo.province;
//								fanModel.city = response.fanInfo.city;
//								fanModel.followStatus = response.fanInfo.followStatus;
//								fanModel.gender = response.fanInfo.gender;
//								fanModel.interactionTimes = response.fanInfo.interactionTimes;
//								fanModel.lastInteractionTime = response.fanInfo.lastInteractionTime;
//								fanModel.subscribeTime = response.fanInfo.subscribeTime;
//								if (isSave) {
//									Uri fansRecordModelUrl = new ApiFansRecordDb(context).insert(fanModel);
//									if (fansRecordModelUrl == null) {
//										//TODO 该粉丝插入不成功
//										MyLogger.w(TAG, "粉丝加入数据库不成功");
//									}
//								}
//								MyLogger.e(TAG, "fanModel" + fanModel.toString());
//								Message msg = new Message();
//								msg.what = 1;
//								handler.sendMessage(msg);
//							}
//						} else {
//							Utils.toast(context, response.getRetInfo() + "");
//						}
//					}
//				});
//	}
//
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//				case 1:
//					setMessageNumber();
//					break;
//				case 2:
//					int notecount = msg.getData().getInt("notecount", 0);
//					setMsgNum(notecount);
//					break;
//			}
//		}
//	};
//
//	@Override
//	protected void onDestroy() {
//		messageManager.unregisterOnReceivedPushMessageListener(opl);
//		if(receiver != null) {
//			unregisterReceiver(receiver);
//		}
//		super.onDestroy();
//	}
//
//	@Override
//	public void onClick(View v) {
//
//	}
//
//	private void clearBackStack() {
//		if (fragmentManager.getBackStackEntryCount() > 0) {
//			fragmentManager.popBackStack();
//			if (fragmentManager.getBackStackEntryCount() > 0) {
//				fragmentManager.popBackStack();
//			}
//		}
//	}
//
//	private void initSetView() {
//
//		TextView tv_test = (TextView) super.findViewById(R.id.tv_test);
//
//		tv_test.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//				//menu();
//				//Intent intent = new Intent(context, TestActivity.class);
//				//startActivity(intent);
//				//switchServerURL();
//
//				//MyLogger.i(TAG, "dataDirectory: " + dataDirectory.getAbsolutePath());
//				//MyLogger.i(TAG, "getFilesDir: " + context.getFilesDir().getAbsolutePath());
//				//MyLogger.i(TAG, "getExternalStorageDirectory:" + Environment.getExternalStorageDirectory());
//				String db_path = Environment.getDataDirectory().getAbsolutePath() + "/data/" + getApplicationInfo().packageName + "/databases/mscrm.db";
//				String save_path = Environment.getExternalStorageDirectory() + "/copyfile_mscrm.db";
//				MyLogger.i(TAG, "db_path: " + db_path);
//				MyLogger.i(TAG, "save_path: " + save_path);
//				copyFile(db_path, save_path);
//				return true;
//			}
//		});
//
//		mVersionCode = (TextView) super.findViewById(R.id.version_code);
//		mCheckUpdate = (TextView) super.findViewById(R.id.check_update);
//		tv_new_version = (TextView) super.findViewById(R.id.tv_new_version);
//
//		mVersionCode.setText("版本: " + Utils.getVersionCode(context));
//		mLogout = (TextView) super.findViewById(R.id.logout);
//		mCheckUpdate.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				//友盟更新
//				//UmengUpdateAgent.update(context);
//				checkUpdate(true);
//			}
//		});
//		//注销按钮点击事件
//		mLogout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				logout();
//			}
//		});
//		modifyPwd = (TextView) super.findViewById(R.id.modify_pwd);
//		modifyPwd.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				//跳转到修改密码界面
//				Intent pswChangeIntent = new Intent(context, PswChangeActivity.class);
//				pswChangeIntent.putExtra(Constant.MAIN_INTENT_DATA, "IntentPsw");
//				startActivity(pswChangeIntent);
//				//menu();
//			}
//		});
//		mSetTails = (TextView) super.findViewById(R.id.text_set_tails);
//
//		String tails = PreferenceUtils.getString(context, Constant.CHAT_TAILS, "");
//		if (TextUtils.isEmpty(tails)) {
//			mSetTails.setText(R.string.edit_signature);
//		} else {
//			mSetTails.setText("“" + tails + "”");
//		}
//
//
//		mSetTails.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				//跳转到设置小尾巴界面
//				//startActivity(new Intent(context, SetTailsActivity.class));
//				startActivityForResult(new Intent(context, SetTailsActivity.class), REQUEST);
//				overridePendingTransition(R.anim.activity_apper, R.anim.activity_apper);
//				//menu();
//			}
//		});
//
//		//===================================================================
//		//去往消息中心页面
//
//		mMsgCenter = (TextView) super.findViewById(R.id.tv_msg_center);
//		mMsgNum = (TextView) super.findViewById(R.id.tv_msg_center_num);
//		mMsgCenter.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent msgIntent = new Intent(context, MsgCenterActivity.class);
//				msgIntent.putExtra(Constant.MAIN_INTENT_DATA, "msgIntent");
//				startActivity(msgIntent);
//				//menu();
//			}
//		});
//	}
//
//	//===================================================================
//
//	//显示消息中心的数量
//	public void setMsgNum(int num) {
//		if (num > 0) {
//			mMsgNum.setText(Integer.toString(num));
//			mMsgNum.setVisibility(View.VISIBLE);
//		} else {
//			mMsgNum.setVisibility(View.GONE);
//		}
//	}
//
//	//退出到登陆界面
//	public void logout() {
//		context.stopService(serviceIntent);
//		PreferenceUtils.clearUser(context);
//		PreferenceUtils.putLong(context, Constant.LAST_REFRESH_ALLFANS_TIME, 0);
//		startActivity(new Intent(context, LoginActivity.class));
//		finish();
//	}
//
//
//	private void switchServerURL() {
//		LayoutInflater infalter = (LayoutInflater) getSystemService(Service.LAYOUT_INFLATER_SERVICE);
//		View view = infalter.inflate(R.layout.set_url_dialog, null);
//		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//		final AlertDialog dialog = builder.create();
//		dialog.setCancelable(false);
//		dialog.setView(view, 0, 0, 0, 0);
//		dialog.show();
//
//		final EditText et_chatter_url = (EditText) view.findViewById(R.id.et_chatter_url);
//		final EditText et_request_url = (EditText) view.findViewById(R.id.et_request_url);
//
//		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
//		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
//
//		btn_ok.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String chatter_url = et_chatter_url.getText().toString().trim();
//				String request_url = et_request_url.getText().toString().trim();
//
//				if (TextUtils.isEmpty(chatter_url) || TextUtils.isEmpty(request_url)) {
//					Utils.toast(getApplicationContext(), "chatter or request url can not be empty!");
//				} else {
//					//http://58.67.204.13:29092
//					chatter_url = "http://" + chatter_url;
//
//					//http://scrmapp.vivo.com.cn:82/MobileBusiness
//					request_url = "http://" + request_url + "/MobileBusiness";
//
//					PreferenceUtils.putString(getApplicationContext(), "chatter_url", chatter_url);
//					PreferenceUtils.putString(getApplicationContext(), "request_url", request_url);
//
//					MyLogger.e(TAG, "chatter_url:" + chatter_url);
//					MyLogger.e(TAG, "request_url:" + request_url);
//
//					dialog.dismiss();
//					logout();
//				}
//			}
//		});
//		btn_cancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//	}
//
//	/**
//	 * 刷新小尾巴
//	 *
//	 * @param requestCode
//	 * @param resultCode
//	 * @param data
//	 */
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == REQUEST && resultCode == 200) {
//			String tails = data.getStringExtra("tails");
//			if (TextUtils.isEmpty(tails)) {
//				mSetTails.setText(R.string.edit_signature);
//			} else {
//				mSetTails.setText("“" + tails + "”");
//			}
//		}
//	}
//
////	public void menu() {
////		openLeftLayout();
////	}
//
//	public void stop() {
//		logout();
//	}
//
////	public void openLeftLayout() {
////		if (drawerLayout.isDrawerOpen(mRl)) {
////			drawerLayout.closeDrawer(mRl);
////		} else {
////			drawerLayout.openDrawer(mRl);
////
////		}
////	}
//
//	@Override
//	public void onCheckedChanged(RadioGroup group, int checkedId) {
//		FragmentTransaction transaction = fragmentManager.beginTransaction();
//		switch (checkedId) {
//			case R.id.rb_main_interaction:
//				clearBackStack();
//				InteractionLocaleFragment ilf = new InteractionLocaleFragment();
//				transaction.replace(R.id.frame_main, ilf, Constant.INTERACTION);
//				break;
//			case R.id.rb_main_interaction_online:
//				clearBackStack();
//				InteractionOnlineFragment iof = new InteractionOnlineFragment();
//				transaction.replace(R.id.frame_main, iof, Constant.INTERACTION_ONLINE);
//				break;
//			case R.id.rb_main_mymember:
//				MyMemberFragment mmf = new MyMemberFragment();
//				transaction.replace(R.id.frame_main, mmf, Constant.MY_MEMBER);
//				break;
//		}
//		transaction.commitAllowingStateLoss();
//	}
//
//	private class ConnectionChangeReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			MyLogger.e(TAG, "网络状态改变");
//			boolean success = false;
//			//获得网络连接服务
//			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			// State state = connManager.getActiveNetworkInfo().getState();
//			NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
//			if (NetworkInfo.State.CONNECTED == state) { // 判断是否正在使用WIFI网络
//				MyLogger.e(TAG, "wifi connected");
//				success = true;
//			}
//			state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
//			if (NetworkInfo.State.CONNECTED == state) { // 判断是否正在使用GPRS网络
//				MyLogger.e(TAG, "mobile gprs connected");
//				success = true;
//			}
//			if (!success) {
//				Utils.toast(context,"您的网络连接已中断");
//				ll_net_issue.setVisibility(View.VISIBLE);
//			}else {
//				//Utils.toast(context,"您的网络连接已连接");
//				ll_net_issue.setVisibility(View.GONE);
//			}
//		}
//	}
//}
