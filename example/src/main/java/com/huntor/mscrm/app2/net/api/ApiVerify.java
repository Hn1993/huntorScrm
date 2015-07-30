package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.model.VerifyResult;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

/**
 * 登陆
 */
public class ApiVerify extends HttpApiBase {

	private static final String TAG = "ApiVerify";

	/**
	 * 登陆
	 *
	 * @param context 上下文
	 * @param params  添加验证需要的参数
	 */
	public ApiVerify(Context context, ApiVerifyParams params) {
		super(context);
		String request_url_main = Constant.HTTP_REQUEST_USER_RESET_PWD_VERIFY;
		String request_url = PreferenceUtils.getString(context, "request_url", "");
		if (!TextUtils.isEmpty(request_url)) {
			request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
		}
		mHttpRequest = new HttpRequest(request_url_main,
				HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
	}

	/**
	 * 验证需要的参数
	 */
	public static class ApiVerifyParams extends BaseRequestParams {
		private String userName;
		private String verifyCode;

		/**
		 * 添加验证需要的参数
		 */
		public ApiVerifyParams(String userName, String verifyCode) {
			super();
			this.userName = userName;
			this.verifyCode = verifyCode;
		}

		/**
		 * 根据成员变量生成参数
		 */
		@Override
		public String generateRequestParams() {
			return "?userName=" + userName + "&verifyCode=" + verifyCode;
		}
	}

	/**
	 * 添加登陆返回结果
	 */
	public static class ApiVerifyResponse extends BaseResponse {
		public VerifyResult verifyResult;
	}

	public ApiVerifyResponse getHttpResponse() {
		PreferenceUtils.putString(mContext,Constant.PREFERENCE_TOKEN_VERIFY,"");
		BaseResponse baseResponse = getHttpContent();

		ApiVerifyResponse response = new ApiVerifyResponse();
		response.setRetCode(baseResponse.getRetCode());
		response.setRetInfo(baseResponse.getRetInfo());
		MyLogger.e(TAG, "" + baseResponse.toString());

		if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
			Gson gson = new Gson();
			response.verifyResult = gson.fromJson(baseResponse.getContent(), VerifyResult.class);
			if(response.verifyResult != null){
				PreferenceUtils.putString(mContext,Constant.PREFERENCE_TOKEN_VERIFY,response.verifyResult.token);
			}
			MyLogger.e(TAG, "response.verifyResult = " + response.verifyResult);
		}
		return response;
	}

}
