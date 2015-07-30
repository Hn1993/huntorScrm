package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

/**
 * 登陆
 */
public class ApiLogin extends HttpApiBase {

	private static final String TAG = "ApiLogin";

	/**
	 * 登陆
	 *
	 * @param context 上下文
	 * @param params  添加登陆需要的参数
	 */
	public ApiLogin(Context context, ApiLoginParams params) {
		super(context);
		String request_url_main = Constant.HTTP_REQUEST_USER_LOGIN;
		String request_url = PreferenceUtils.getString(context, "request_url", "");

		if (!TextUtils.isEmpty(request_url)) {
			request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
		}

		mHttpRequest = new HttpRequest(request_url_main,
				HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);

	}

	/**
	 * 添加登陆需要的参数
	 */
	public static class ApiLoginParams extends BaseRequestParams {

		private String userName;
		private String pwd;

		/**
		 * 添加登陆需要的参数
		 */
		public ApiLoginParams(String userName, String pwd) {
			super();
			this.userName = userName;
			this.pwd = pwd;
		}

		/**
		 * 根据成员变量生成参数
		 */
		@Override
		public String generateRequestParams() {
			return "?userName=" + userName + "&pwd=" + pwd;
		}
	}

	/**
	 * 添加登陆返回结果
	 */
	public static class ApiLoginResponse extends BaseResponse {
		public LoginResult loginResult;
	}

	public ApiLoginResponse getHttpResponse() {
		PreferenceUtils.putString(mContext,Constant.PREFERENCE_TOKEN,"");
		BaseResponse baseResponse = getHttpContent();

		ApiLoginResponse response = new ApiLoginResponse();
		response.setRetCode(baseResponse.getRetCode());
		response.setRetInfo(baseResponse.getRetInfo());
		Log.e(TAG,""+baseResponse.toString());
		if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
			Gson gson = new Gson();
			response.loginResult = gson.fromJson(baseResponse.getContent(), LoginResult.class);
			if(response.loginResult != null){
				PreferenceUtils.putString(mContext,Constant.PREFERENCE_TOKEN,response.loginResult.getToken());
			}
			Log.e(TAG, "response.loginResult = " + response.loginResult);
		}
		return response;
	}

}
