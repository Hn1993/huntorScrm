package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.LoginResult;
import com.huntor.mscrm.app.model.TargetList;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;

/**
 * 创建自定义粉丝分组
 */
public class ApiCreateTargetlist extends HttpApiBase {

    private static final String TAG = "ApiCreateTargetlist";

    public ApiCreateTargetlist(Context context, ApiCreateTargetlistParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_TARGETLIST_CREATE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 创建一个新的自定义粉丝分组需要的参数
     */
    public static class ApiCreateTargetlistParams extends BaseRequestParams {

        private int empId;//员工id
        private String name;//分组名称
        private String desc;//分组描述

        /**
         * 添加创建一个新的自定义粉丝分组需要的参数
         *
         */
        public ApiCreateTargetlistParams(int empId, String name,String desc) {
            super();
            this.empId = empId;
            this.name = name;
            this.desc = desc;

        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId=" + empId + "&name="+name+"&desc="+desc;
        }
    }

    /**
     * 添加创建一个新的自定义粉丝分组返回结果
     */
    public static class ApiCreateTargetlistResponse extends BaseResponse {
        public TargetList targetList;
    }

    public ApiCreateTargetlistResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiCreateTargetlistResponse response = new ApiCreateTargetlistResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.targetList = gson.fromJson(baseResponse.getContent(), TargetList.class);
            Log.i(TAG, "response.loginResult = " + response.targetList);
        }
        return response;
    }

}
