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
 * 从员工的自定义粉丝分组中移除一个粉丝
 */
public class ApiFansTargetListRemove extends HttpApiBase {

    private static final String TAG = "ApiFansTargetListRemove";

    /**
     * 从员工的自定义粉丝分组中移除一个粉丝
     *
     * @param context
     *            上下文
     * @param params
     *            添加从员工的自定义粉丝分组中移除一个粉丝需要的参数
     */
    public ApiFansTargetListRemove(Context context, ApiFansTargetListRemoveParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_TARGETLIST_REMOVE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加从员工的自定义粉丝分组中移除一个粉丝需要的参数
     */
    public static class ApiFansTargetListRemoveParams extends BaseRequestParams {

        private int targetListId;
        private String fanIdList;

        /**
         * 添加从员工的自定义粉丝分组中移除一个粉丝需要的参数
         *
         */
        public ApiFansTargetListRemoveParams(int targetListId, int[] fanIdList) {
            super();
            this.targetListId = targetListId;
            Gson gson = new Gson();
            if(fanIdList != null){
                this.fanIdList = gson.toJson(fanIdList);
            }else{
                this.fanIdList ="";
            }
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?targetListId=" + targetListId + "&fanIdList="+fanIdList;
        }
    }

    /**
     * 添加从员工的自定义粉丝分组中移除一个粉丝返回结果
     */
    public static class ApiFansTargetListRemoveResponse extends BaseResponse {
        public Response response;
    }

    public ApiFansTargetListRemoveResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansTargetListRemoveResponse response = new ApiFansTargetListRemoveResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

            Log.i(TAG, "response.response = " + response.response);
        }
        return response;
    }

}
