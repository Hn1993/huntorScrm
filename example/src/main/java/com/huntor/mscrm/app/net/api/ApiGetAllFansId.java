package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.AllFansId;
import com.huntor.mscrm.app.model.ModifyFansParam;
import com.huntor.mscrm.app.model.Response;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;

/**
 * 获取所有粉丝
 */
public class ApiGetAllFansId extends HttpApiBase {

	private static final String TAG = "ApiGetAllFansId";

	/**
	 * 获取所有粉丝
	 *
	 * @param context 上下文
	 * @param params  添加获取所有粉丝需要的参数
	 */
	public ApiGetAllFansId(Context context, ApiGetAllFansIdParams params) {
		super(context);
		String request_url_main = Constant.HTTP_REQUEST_ALL_FANS_ID_LIST;
		String request_url = PreferenceUtils.getString(context, "request_url", "");

		if (!TextUtils.isEmpty(request_url)) {
			request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
		}

		mHttpRequest = new HttpRequest(request_url_main,
				HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);

	}

	/**
	 * 添加登陆需要的参数
	 */
	public static class ApiGetAllFansIdParams extends BaseRequestParams {

		private int mTargetListId;
		private boolean mContainsUnfollow;
		/**
		 * 添加登陆需要的参数
		 */
		public ApiGetAllFansIdParams(int targetListId,boolean containsUnfollow) {
			super();
			mTargetListId = targetListId;
			mContainsUnfollow = containsUnfollow;
		}

		/**
		 * 根据成员变量生成参数
		 */
		@Override
		public String generateRequestParams() {
			return "?targetListId=" + mTargetListId + "&containsUnfollow=" + mContainsUnfollow;
		}
	}

	/**
	 * 添加获取所有粉丝ID返回结果
	 */
	public static class ApiGetAllFansIdResponse extends BaseResponse {
		public AllFansId allFansId;
	}

	public ApiGetAllFansIdResponse getHttpResponse() {
		BaseResponse baseResponse = getHttpContent();

		ApiGetAllFansIdResponse response = new ApiGetAllFansIdResponse();
		response.setRetCode(baseResponse.getRetCode());
		response.setRetInfo(baseResponse.getRetInfo());
		Log.e(TAG,""+baseResponse.toString());
		if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
			Gson gson = new Gson();
			response.allFansId = gson.fromJson(baseResponse.getContent(), AllFansId.class);
			Log.e(TAG, "response.allFansId = " + response.allFansId);
		}
		return response;
	}

}
