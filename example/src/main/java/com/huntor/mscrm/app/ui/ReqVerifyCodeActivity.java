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
import com.huntor.mscrm.app.net.api.ApiReqVerifyCode;
import com.huntor.mscrm.app.net.api.ApiVerify;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.component.GrayUnderLineEditText;
import com.huntor.mscrm.app.ui.component.MyCountTimer;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PreferenceUtils;
import com.huntor.mscrm.app.utils.Utils;

public class ReqVerifyCodeActivity extends BaseActivity implements View.OnClickListener {

	public final String TAG = getClass().getName();
	private Context context = ReqVerifyCodeActivity.this;
	private GrayUnderLineEditText et_username;
	private GrayUnderLineEditText et_verify_code;
	private MyCountTimer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		setTitle(getString(R.string.reset_password2));
		et_username = (GrayUnderLineEditText) findViewById(R.id.et_username);
		et_verify_code = (GrayUnderLineEditText) findViewById(R.id.et_verify_code);

		Button btn_req_verify_code = (Button) findViewById(R.id.btn_req_verify_code);
		Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
		ImageView  img_left_corner = (ImageView) findViewById(R.id.img_left_corner);

		btn_req_verify_code.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		img_left_corner.setOnClickListener(this);
		timer = new MyCountTimer(btn_req_verify_code);
	}

	@Override
	public void onClick(View v) {
		String userName = et_username.getText().toString().trim();
		String verifyCode = et_verify_code.getText().toString().trim();

		switch (v.getId()) {
			case R.id.btn_req_verify_code:
				if (TextUtils.isEmpty(userName)) {
					Utils.toast(context, R.string.user_name_can_not_be_empty);
				} else {
					timer.start();
					//reqVerifyCode(userName);
				}
				break;
			case R.id.btn_confirm:
				/*if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(verifyCode)){
					Utils.toast(context, R.string.user_name_or_verify_code_can_not_be_empty);
				}  else {
					verifyCode(userName,verifyCode);
				}*/

				Intent intent = new Intent(context,ResetPwdActivity.class);
				intent.putExtra(Constant.PREFERENCE_NUMBER,userName);
				startActivity(intent);
				break;
			case R.id.img_left_corner:
				finish();
				break;
		}
	}

	/**
	 *验证短信动态码
	 * @param userName
	 * @param verifyCode
	 */
	private void verifyCode(final String userName, String verifyCode) {
		HttpRequestController.verify(context, userName, verifyCode, new HttpResponseListener<ApiVerify.ApiVerifyResponse>() {
			@Override
			public void onResult(ApiVerify.ApiVerifyResponse response) {
				if(response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK){
					String token = response.verifyResult.token;
					if(TextUtils.isEmpty(token)){
						Utils.toast(context,getString(R.string.token_exception));
					}  else {
						PreferenceUtils.putString(context, Constant.PREFERENCE_TOKEN_VERIFY, token);
						Intent intent = new Intent(context,ResetPwdActivity.class);
						intent.putExtra(Constant.PREFERENCE_NUMBER,userName);
						startActivity(intent);
						finish();
					}
				} else {
					Utils.toast(context, response.getRetInfo()+"");
				}
			}
		});
	}

	/**
	 * 获取验证码
	 *
	 * @param userName
	 */
	private void reqVerifyCode(String userName) {
		HttpRequestController.reqVerifyCode(context, userName, new HttpResponseListener<ApiReqVerifyCode.ApiReqVerifyCodeResponse>() {
			@Override
			public void onResult(ApiReqVerifyCode.ApiReqVerifyCodeResponse response) {
				MyLogger.i(TAG,response.toString());
				if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
					Utils.toast(context, getString(R.string.send_success));
				} else {
					Utils.toast(context, response.getRetInfo()+"");
				}
			}
		});
	}
}
