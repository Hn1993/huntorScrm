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
public class ApiReqVerifyCode extends HttpApiBase {

	private static final String TAG = "ApiReqVerifyCode";

	/**
	 * 登陆
	 *
	 * @param context 上下文
	 * @param params  添加请求验证码需要的参数
	 */
	public ApiReqVerifyCode(Context context, ApiReqVerifyCodeParams params) {
		super(context);
		String request_url_main = Constant.HTTP_REQUEST_USER_RESET_PWD_REQ;
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
	public static class ApiReqVerifyCodeParams extends BaseRequestParams {

		private String userName;

		/**
		 * 请求验证码需要的参数
		 */
		public ApiReqVerifyCodeParams(String userName) {
			super();
			this.userName = userName;
		}

		/**
		 * 根据成员变量生成参数
		 */
		@Override
		public String generateRequestParams() {
			return "?userName=" + userName;
		}
	}

	/**
	 * 请求验证码返回结果
	 */
	public static class ApiReqVerifyCodeResponse extends BaseResponse {
		public Response response;
	}

	public ApiReqVerifyCodeResponse getHttpResponse() {
		BaseResponse baseResponse = getHttpContent();
		ApiReqVerifyCodeResponse response = new ApiReqVerifyCodeResponse();

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
