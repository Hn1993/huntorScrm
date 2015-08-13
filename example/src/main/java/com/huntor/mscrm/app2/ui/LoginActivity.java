package com.huntor.mscrm.app2.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.huntor.mscrm.app2.model.EmpLoyeeInfo;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiEmployee;
import com.huntor.mscrm.app2.net.api.ApiLogin;
import com.huntor.mscrm.app2.push.PushMessageReceiverService;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;
import com.umeng.analytics.MobclickAgent;
import com.huntor.mscrm.app2.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
	private final String TAG = "LoginActivity";
	private LoginActivity context = LoginActivity.this;
	private EditText et_username;
	private EditText et_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);
		initView();
	}

	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

		String userName = PreferenceUtils.getString(context, Constant.PREFERENCE_NUMBER, "");//获取保存的用户名称
		String userPsw = PreferenceUtils.getString(context, Constant.PREFERENCE_PSW, "");//获取保存的用户密码
		String saveTime = PreferenceUtils.getString(context, Constant.PREFERENCE_CURRENT_TIME, "");//获取保存的上次登录时间

		long time = 0;
		if (saveTime != null && !TextUtils.isEmpty(saveTime)) {
			time = Long.parseLong(saveTime);
		}
		long currentTime = System.currentTimeMillis();
		if (isUserNull(userName, userPsw) && (time - currentTime < Constant.HOURS_24)) {//判断用户登录是否超过24小时
			startActivity(new Intent(this, MainActivity2.class));//没有超过24小时，自动跳转到主界面
			finish();
		}

		TextView tv_login = (TextView) findViewById(R.id.tv_login);
		tv_login.setOnClickListener(this);
		TextView tv_need_help = (TextView) findViewById(R.id.tv_need_help);
		tv_need_help.setOnClickListener(this);
		TextView tv_back = (TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);


		if (getIntentFlag() == Constant.LOGINOUT_FLAG) {
			showDialog();
		}
	}

	/**
	 * 判断用户名和密码是否为空
	 *
	 * @param userName
	 * @param userPsw
	 * @return
	 */
	private boolean isUserNull(String userName, String userPsw) {
		return userName != null && userPsw != null && !userName.equals("") && !userPsw.equals("");
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_login:
				String username = et_username.getText().toString().trim();
				String password = et_password.getText().toString().trim();
				login(username, password);
				break;
			case R.id.tv_need_help:
				startActivity(new Intent(context,ReqVerifyCodeActivity.class));
				break;
			case R.id.tv_back:
				finish();
				break;
		}
	}

	/**
	 * 登陆
	 *
	 * @param number
	 * @param psw
	 */
	private void login(final String number, final String psw) {
		showCustomDialog(R.string.loading);
		HttpRequestController.login(this, number, psw,
				new HttpResponseListener<ApiLogin.ApiLoginResponse>() {
					@Override
					public void onResult(ApiLogin.ApiLoginResponse response) {
						if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
							if (TextUtils.equals(psw, Constant.DEFAULT_INIT_PSW)) { //如果密码是初始默认密码就提示修改密码
								Utils.toast(context, R.string.notice_modify_init_psw);
								Intent intent = new Intent(context, PswChangeActivity.class);
								intent.putExtra(Constant.PREFERENCE_NUMBER, number);
								startActivity(intent);
							} else {
								setUserDetail(response, number, psw);
							}
						} else {
							if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR) {
								Utils.toast(context, R.string.login_network_err);
							} else if (response.getRetCode() == BaseResponse.RET_RESULT_STATUS_ERROR) {
								Utils.toast(context, R.string.login_pwd_name_err);
							} else {
								Utils.toast(context, response.getRetInfo() + "");
							}
						}
						dismissCustomDialog();
					}
				});
	}

	/**
	 * 员工登录成功之后保存员工信息，并进行相应的操作
	 *
	 * @param response 员工登录的结果
	 * @param userName 员工登录的名称
	 * @param userPsw  员工登陆的密码
	 */
	public void setUserDetail(ApiLogin.ApiLoginResponse response, String userName, String userPsw) {
		int userId = response.loginResult.getUserId();
		int empId = response.loginResult.getEmpId();
		String empName = response.loginResult.getEmpName();
		long currentTimeMillis = System.currentTimeMillis();
		PreferenceUtils.putString(context, Constant.PREFERENCE_CURRENT_TIME, Long.toString(currentTimeMillis));
		PreferenceUtils.putInt(context, Constant.PREFERENCE_USER_ID, userId);
		PreferenceUtils.putInt(context, Constant.PREFERENCE_EMP_ID, empId);
		PreferenceUtils.putString(context, Constant.PREFERENCE_EMP_NAME, empName);
		PreferenceUtils.putString(context, Constant.PREFERENCE_NUMBER, userName);//将用户登录的用户名与密码保存到Preference中
		PreferenceUtils.putString(context, Constant.PREFERENCE_PSW, userPsw);
		PreferenceUtils.putLong(context, Constant.LAST_REFRESH_ALLFANS_TIME, 0);
		PreferenceUtils.putLong(context, Constant.LAST_REFRESH_ALLFANS_TIME, 0);

		MyLogger.i("Login", "empId = " + empId);
		//启动服务
		Intent intents = new Intent(context, PushMessageReceiverService.class);
		context.startService(intents);
		emplyouTask(empId);//去获取并缓存员工的基础信息
	}

	/**
	 * @param empId 获取员工基础信息
	 */
	private void emplyouTask(int empId) {
		HttpRequestController.queryEmployeeInfo(this, empId,
				new HttpResponseListener<ApiEmployee.ApiEmployeeResponse>() {
					@Override
					public void onResult(ApiEmployee.ApiEmployeeResponse response) {
						if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
//                            MyLogger.i(TAG, "response.qrcode = " + response.employeeInfo);
							EmpLoyeeInfo employeeInfo = response.employeeInfo;
							PreferenceUtils.putInt(context, Constant.PREFERENCES_SOCIAL_ID, employeeInfo.socialId);
							MyLogger.i(TAG, "socialId = " + employeeInfo.socialId);
							PreferenceUtils.putInt(context, "id", employeeInfo.id);
							PreferenceUtils.putString(context, "number", employeeInfo.number);
							PreferenceUtils.putString(context, "name", employeeInfo.name);
							PreferenceUtils.putInt(context, "teamId", employeeInfo.teamId);
							PreferenceUtils.putString(context, "teamName", employeeInfo.teamName);
							PreferenceUtils.putInt(context, "organizationId", employeeInfo.organizationId);
							PreferenceUtils.putString(context, "organizationName", employeeInfo.organizationName);
							PreferenceUtils.putInt(context, "employeeType", employeeInfo.employeeType);
							PreferenceUtils.putString(context, "phone", employeeInfo.phone);
							PreferenceUtils.putString(context, Constant.PREFERENCE_QRCODE, employeeInfo.qrcode);
							MyLogger.i("LoginActivity", "employeeInfo.qrcode = " + employeeInfo.qrcode);
							Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
							startActivity(intent);
							finish();
						} else {
							Utils.toast(LoginActivity.this, response.getRetInfo() + "");
						}
						Log.e(TAG, "" + response.getRetInfo());
						dismissCustomDialog();

					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);

	}

	private int getIntentFlag() {
		return getIntent().getIntExtra(Constant.LOGINOUT, 0);
	}

	private void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		builder.setMessage("你的账号已经在其他手机上登录");
		builder.setTitle("下线通知");
		builder.setPositiveButton("取消登录", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String number = PreferenceUtils.getString(context, Constant.PREFERENCE_NUMBER, "");
				String psw = PreferenceUtils.getString(context, Constant.PREFERENCE_PSW, "");
				login(number, psw);
			}
		});
		builder.create().show();
	}
}
