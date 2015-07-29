package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.Response;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PreferenceUtils;

/**
 * 登陆
 */
public class ApiResetPwdDone extends HttpApiBase {

	private static final String TAG = "ApiResetPwdDone";

	/**
	 * 登陆
	 *
	 * @param context 上下文
	 * @param params  添加请求验证码需要的参数
	 */
	public ApiResetPwdDone(Context context, ApiResetPwdDoneParams params) {
		super(context);
		String request_url_main = Constant.HTTP_REQUEST_USER_RESET_PWD_DONE;
		String request_url = PreferenceUtils.getString(context, "request_url", "");

		if (!TextUtils.isEmpty(request_url)) {
			request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
		}

		mHttpRequest = new HttpRequest(request_url_main,
				HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);

	}

	/**
	 * 请求验证码需要的参数
	 */
	public static class ApiResetPwdDoneParams extends BaseRequestParams {
		private String userName;
		private String token;
		private String newPwd;

		public ApiResetPwdDoneParams(String userName, String token, String newPwd) {
			this.userName = userName;
			this.token = token;
			this.newPwd = newPwd;
		}

		/**
		 * 根据成员变量生成参数
		 */
		@Override
		public String generateRequestParams() {
			return "?userName=" + userName+"&token="+token+"&newPwd="+newPwd;
		}
	}

	/**
	 * 请求验证码返回结果
	 */
	public static class ApiResetPwdDoneResponse extends BaseResponse {
		public Response response;
	}

	public ApiResetPwdDoneResponse getHttpResponse() {
		BaseResponse baseResponse = getHttpContent();
		ApiResetPwdDoneResponse response = new ApiResetPwdDoneResponse();
		response.setRetCode(baseResponse.getRetCode());
		response.setRetInfo(baseResponse.getRetInfo());
		MyLogger.e(TAG, "" + baseResponse.toString());
		if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
			Gson gson = new Gson();
			response.response = gson.fromJson(baseResponse.getContent(), Response.class);
			MyLogger.e(TAG, "response.loginResult = " + response.response);
		}
		return response;
	}

}
