package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.LoginResult;
import com.huntor.mscrm.app.model.MessageHistory;
import com.huntor.mscrm.app.model.Messages;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PreferenceUtils;

import java.util.List;

/**
 * 查询导购与粉丝交互的历史消息
 */
public class ApiRealTimeHistory extends HttpApiBase {

    private static final String TAG = "ApiRealTimeHistory";

    /**
     * 登陆
     *
     * @param context 上下文
     * @param params  添加查询导购与粉丝交互的历史消息需要的参数
     */
    public ApiRealTimeHistory(Context context, ApiRealTimeHistoryParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_REALTIME_HISTORY;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 查询导购与粉丝交互的历史消息需要的参数
     */
    public static class ApiRealTimeHistoryParams extends BaseRequestParams {

        private int fanId; //粉丝ID
        private int sinceId; //页码
        private int pageSize; //页面大小
        private int orderBy; //排序字段
        private int orderFlag; //排序标志

        /**
         * 查询导购与粉丝交互的历史消息需要的参数
         */
        public ApiRealTimeHistoryParams(int fanId, int sinceId, int pageSize, int orderBy, int orderFlag) {
            super();
            this.fanId = fanId;
            this.sinceId = sinceId;
            this.pageSize = pageSize;
            this.orderBy = orderBy;
            this.orderFlag = orderFlag;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            MyLogger.e(TAG, "查询历史记录参数：" + "?fanId=" + fanId + "&sinceId=" + sinceId + "&pageSize=" + pageSize + "&orderBy=" + orderBy + "&orderFlag=" + orderFlag);
            return "?fanId=" + fanId + "&sinceId=" + sinceId + "&pageSize=" + pageSize + "&orderBy=" + orderBy + "&orderFlag=" + orderFlag;
        }
    }

    /**
     * 查询导购与粉丝交互的历史消息返回结果
     */
    public static class ApiRealTimeHistoryResponse extends BaseResponse {
        public MessageHistory messageHistory;
    }

    public ApiRealTimeHistoryResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiRealTimeHistoryResponse response = new ApiRealTimeHistoryResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.messageHistory = gson.fromJson(baseResponse.getContent(), MessageHistory.class);

            Log.i(TAG, "response.messageHistory = " + response.messageHistory);
        }
        return response;
    }

}
