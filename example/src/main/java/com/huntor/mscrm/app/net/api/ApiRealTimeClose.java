package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.LoginResult;
import com.huntor.mscrm.app.model.Response;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;

/**
 * 关闭导购与粉丝的实时交互
 */
public class ApiRealTimeClose extends HttpApiBase {

    private static final String TAG = "ApiRealTimeClose";

    /**
     * 关闭导购与粉丝的实时交互
     *
     * @param context
     *            上下文
     * @param params
     *            添加关闭导购与粉丝的实时交互需要的参数
     */
    public ApiRealTimeClose(Context context, ApiRealTimeCloseParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_REALTIME_CLOSE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加关闭导购与粉丝的实时交互需要的参数
     */
    public static class ApiRealTimeCloseParams extends BaseRequestParams {

        private int empId;
        private int fanId;

        /**
         * 添加关闭导购与粉丝的实时交互需要的参数
         *
         */
        public ApiRealTimeCloseParams(int empId, int fanId) {
            super();
            this.empId = empId;
            this.fanId = fanId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId=" + empId + "&fanId="+fanId;
        }
    }

    /**
     * 添加关闭导购与粉丝的实时交互返回结果
     */
    public static class ApiRealTimeCloseResponse extends BaseResponse {
        public Response response;
    }

    public ApiRealTimeCloseResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiRealTimeCloseResponse response = new ApiRealTimeCloseResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Log.i(TAG, "response.response = " + response.response);
        }
        return response;
    }

}
