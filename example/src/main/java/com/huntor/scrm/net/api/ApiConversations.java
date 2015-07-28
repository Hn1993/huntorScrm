package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.Conversation;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

/**
 * 查询导购与粉丝的会话列表
 */
public class ApiConversations extends HttpApiBase {

    private static final String TAG = "ApiConversations";

    /**
     * 查询导购与粉丝的会话列表
     *
     * @param context
     *            上下文
     * @param params
     *            查询会话记录需要的参数
     */
    public ApiConversations(Context context, ApiConversationsParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_CONVERSATIONS;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 查询会话记录需要的参数
     */
    public static class ApiConversationsParams extends BaseRequestParams {
        private int empId;//员工ID
        private int pageSize;//页面大小
        private int pageNum;//页码
        private int orderBy;//排序字段
        private int orderFlag;//排序标志

        /**
         * 查询会话记录需要的参数
         *
         */
        public ApiConversationsParams(int empId,int pageSize,int pageNum,int  orderBy,int orderFlag) {
            super();
            this.empId = empId;
            this.pageSize = pageSize;
            this.pageNum = pageNum;
            this.orderBy = orderBy;
            this.orderFlag = orderFlag;

        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId="+empId+"&pageSize="+pageSize+"&pageNum="+pageNum+"&orderBy="+orderBy+"&orderFlag="+orderFlag;
        }
    }

    /**
     * 查询会话记录返回结果
     */
    public static class ApiConversationsResponse extends BaseResponse {
        public Conversation conversation;
    }

    public ApiConversationsResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiConversationsResponse response = new ApiConversationsResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            Conversation conversation = gson.fromJson(baseResponse.getContent(), Conversation.class);

            Log.i(TAG, "response.conversation = " + response.conversation);
        }
        return response;
    }

}
