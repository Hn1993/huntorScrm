package com.huntor.scrm.net.api;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

import java.util.ArrayList;

/**
 * 向员工自定义分组中添加一个粉丝
 */
public class ApiAddFansTargetlist extends HttpApiBase {

    private static final String TAG = "ApiAddFansTargetlist";

    /**
     * 向员工自定义分组中添加一个粉丝
     *
     * @param context
     *            上下文
     * @param params
     *           向员工自定义分组中添加一个粉丝需要的参数
     */
    public ApiAddFansTargetlist(Context context, ApiAddFansTargetlistParams params) {
        super(context);
        mHttpRequest = new HttpRequest(Constant.HTTP_REQUEST_FANS_TARGETLIST_ADD,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 向员工自定义分组中添加一个粉丝需要的参数
     */
    public static class ApiAddFansTargetlistParams extends BaseRequestParams {

        private int targetListId;
        private String fanIds;

        /**
         * 添加向员工自定义分组中添加一个粉丝需要的参数
         *
         */
        public ApiAddFansTargetlistParams(int targetListId, int[] fanIds) {
            super();
            this.targetListId = targetListId;
            Gson gson = new Gson();
            if(fanIds != null){
                this.fanIds = gson.toJson(fanIds);
            }else{
                this.fanIds ="";
            }
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?targetListId=" + targetListId + "&fanIdList="+fanIds;
        }
    }

    /**
     * 添加登陆返回结果
     */
    public static class ApiAddFansTargetlistResponse extends BaseResponse {
        public Response response;
    }

    public ApiAddFansTargetlistResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiAddFansTargetlistResponse response = new ApiAddFansTargetlistResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());
        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Log.i(TAG, "response = " + response);
        }
        
        return response;
    }

}
