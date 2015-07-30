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
 * 更改粉丝状态：新增改为非新增
 */
public class ApiNewTwoOld extends HttpApiBase {

    private static final String TAG = "ApiNewTwoOld";

    /**
     * 更改粉丝状态：新增改为非新增
     *
     * @param context
     *            上下文
     * @param params
     *            添加更改粉丝状态：新增改为非新增需要的参数
     */
    public ApiNewTwoOld(Context context, ApiNewTwoOldParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_FANS_STTUA_NEWTWOOLD;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }


        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加更改粉丝状态：新增改为非新增需要的参数
     */
    public static class ApiNewTwoOldParams extends BaseRequestParams {

        private int fanId;//粉丝ID

        /**
         * 更改粉丝状态：新增改为非新增需要的参数
         *
         */
        public ApiNewTwoOldParams(int fanId) {
            super();
            this.fanId = fanId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?fanId=" + fanId ;
        }
    }

    /**
     * 更改粉丝状态：新增改为非新增返回结果
     */
    public static class ApiNewTwoOldResponse extends BaseResponse {
        public Response response;
    }

    public ApiNewTwoOldResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiNewTwoOldResponse response = new ApiNewTwoOldResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

            Log.i(TAG, "response.response = " + response.response);
        }
        return response;
    }

}
