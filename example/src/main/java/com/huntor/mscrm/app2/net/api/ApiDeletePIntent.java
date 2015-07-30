package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.model.Response;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

/**
 * 粉丝删除购买意向
 */
public class ApiDeletePIntent extends HttpApiBase {

    private static final String TAG = "ApiDeletePIntent";

    /**
     * 粉丝删除购买意向
     *
     * @param context
     *            上下文
     * @param params
     *            粉丝删除购买意向需要的参数
     */
    public ApiDeletePIntent(Context context, ApiDeletePIntentParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_DELETE_PURCHASE_INTENTS;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 粉丝删除购买意向需要的参数
     */
    public static class ApiDeletePIntentParams extends BaseRequestParams {

        private int accountId;
        private int purchaseIntentId;

        /**
         * 粉丝删除购买意向需要的参数
         *
         */
        public ApiDeletePIntentParams(int accountId, int purchaseIntentId) {
            super();
            this.accountId = accountId;
            this.purchaseIntentId = purchaseIntentId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?accountId=" + accountId + "&purchaseIntentId="+purchaseIntentId;
        }
    }

    /**
     * 粉丝删除购买意向返回结果
     */
    public static class ApiDeletePIntentResponse extends BaseResponse {
        public Response response;
    }

    public ApiDeletePIntentResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiDeletePIntentResponse response = new ApiDeletePIntentResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Log.i(TAG, "response = " + response);
        }
        return response;
    }

}
