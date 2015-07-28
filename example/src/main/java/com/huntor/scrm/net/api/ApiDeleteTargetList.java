package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

/**
 * 删除员工的一个自定义分组
 */
public class ApiDeleteTargetList extends HttpApiBase {

    private static final String TAG = "ApiLogin";

    /**
     * 删除员工的一个自定义分组
     *
     * @param context
     *            上下文
     * @param params
     *            删除员工的一个自定义分组需要的参数
     */
    public ApiDeleteTargetList(Context context, ApiDeleteTargetListParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_TARGETLIST_DELETE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }
        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 删除员工的一个自定义分组需要的参数
     */
    public static class ApiDeleteTargetListParams extends BaseRequestParams {

        private int targetListId;

        /**
         * 删除员工的一个自定义分组需要的参数
         *
         */
        public ApiDeleteTargetListParams(int targetListId) {
            super();
            this.targetListId = targetListId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?targetListId=" + targetListId;
        }
    }

    /**
     * 删除员工的一个自定义分组返回结果
     */
    public static class ApiDeleteTargetListResponse extends BaseResponse {
        public Response response;
    }

    public ApiDeleteTargetListResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiDeleteTargetListResponse response = new ApiDeleteTargetListResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Log.i(TAG, "response = " + response);
        }
        return response;
    }

}
