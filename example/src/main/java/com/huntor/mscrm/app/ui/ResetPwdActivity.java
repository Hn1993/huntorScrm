package com.huntor.mscrm.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiResetPwdDone;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.component.GrayUnderLineEditText;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PreferenceUtils;
import com.huntor.mscrm.app.utils.Utils;

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener {

	public final String TAG = getClass().getName();
	private Context context = ResetPwdActivity.this;
	private GrayUnderLineEditText et_new_password;
	private GrayUnderLineEditText et_new_password2;
	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		setTitle(getString(R.string.reset_password));
		Intent intent = getIntent();
		if (intent != null) {
			userName = intent.getStringExtra(Constant.PREFERENCE_NUMBER);
		}

		et_new_password = (GrayUnderLineEditText) findViewById(R.id.et_new_password);
		et_new_password2 = (GrayUnderLineEditText) findViewById(R.id.et_new_password2);

		Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
		ImageView img_left_corner = (ImageView) findViewById(R.id.img_left_corner);

		btn_confirm.setOnClickListener(this);
		img_left_corner.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String pwd = et_new_password.getText().toString();
		String pwd2 = et_new_password2.getText().toString();
		String token = PreferenceUtils.getString(context, Constant.PREFERENCE_TOKEN_VERIFY, "");

		switch (v.getId()) {
			case R.id.btn_confirm:
				if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwd2)) {
					Utils.toast(context, R.string.new_pwd_can_not_be_empty);
				} else if (!TextUtils.equals(pwd, pwd2)) {
					Utils.toast(context, R.string.twice_pwd_not_same);
				} else {
					  if(TextUtils.isEmpty(userName)){
						  Utils.toast(context,R.string.username_pass_exception);
					  } else {
						  resetNewPwd(userName,token,pwd);
					  }
				}
				break;
			case R.id.img_left_corner:
				finish();
				break;
		}
	}

	/**
	 * 重置密码
	 * @param userName
	 * @param token
	 * @param pwd
	 */
	private void resetNewPwd(String userName, String token, String pwd) {
		HttpRequestController.resetPwd(context, userName, token, pwd, new HttpResponseListener<ApiResetPwdDone.ApiResetPwdDoneResponse>() {
			@Override
			public void onResult(ApiResetPwdDone.ApiResetPwdDoneResponse response) {
				MyLogger.i(TAG,"response: "+response.toString());
				if(response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK){
					Utils.toast(context,R.string.reset_pwd_success);
					finish();
				} else {
					Utils.toast(context,response.getRetInfo()+"");
				}
			}
		});
	}

}
