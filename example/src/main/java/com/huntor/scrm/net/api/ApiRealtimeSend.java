package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.model.SendMessageContent;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

/**
 * 发送消息
 */
public class ApiRealtimeSend extends HttpApiBase {

    private static final String TAG = "ApiRealtimeSend";

    /**
     * 发送消息
     *
     * @param context 上下文
     * @param params  添加发送消息需要的参数
     */
    public ApiRealtimeSend(Context context, ApiRealtimeSendParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_REALTIME_SEND;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加发送消息需要的参数
     */
    public static class ApiRealtimeSendParams extends BaseRequestParams {

        private int empId;
        private int fanId;
        private String message;

        /**
         * 添加发送消息需要的参数
         */
        public ApiRealtimeSendParams(int empId, int fanId, int groupId, int type, String content) {
            super();
            this.empId = empId;
            this.fanId = fanId;
            SendMessageContent sendMessageContent = new SendMessageContent();
            sendMessageContent.groupId = groupId;
            sendMessageContent.type = type;
            sendMessageContent.content = content;
            Gson gson = new Gson();
            message = gson.toJson(sendMessageContent);
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId=" + empId + "&fanId=" + fanId + "&message=" + message;
        }
    }

    /**
     * 添加发送消息返回结果
     */
    public static class ApiRealtimeSendResponse extends BaseResponse {
        public Response response;
    }

    public ApiRealtimeSendResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiRealtimeSendResponse response = new ApiRealtimeSendResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

            Log.i(TAG, "response.response = " + response.response);
        }
        return response;
    }

}
