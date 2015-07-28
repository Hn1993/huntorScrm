package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.EmpInfos;
import com.huntor.scrm.model.EmpLoyeeInfo;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

import java.util.List;

/**
 * 员工基础信息查询
 */
public class ApiEmployee extends HttpApiBase {

    private static final String TAG = "ApiEmployee";

    /**
     * 员工基础信息查询
     *
     * @param context
     *            上下文
     * @param params
     *            员工基础信息查询需要的参数
     */
    public ApiEmployee(Context context, ApiEmployeeParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_EMPLOYEE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }
        Log.e(TAG,request_url_main);
        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 员工基础信息查询需要的参数
     */
    public static class ApiEmployeeParams extends BaseRequestParams {

        private int empId;//员工ID

        /**
         * 员工基础信息查询需要的参数
         *
         */
        public ApiEmployeeParams(int empId) {
            super();
            this.empId = empId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId=" + empId;
        }
    }

    /**
     * 员工基础信息查询返回结果
     */
    public static class ApiEmployeeResponse extends BaseResponse {
        public EmpLoyeeInfo employeeInfo;
    }

    public ApiEmployeeResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiEmployeeResponse response = new ApiEmployeeResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            EmpInfos empInfos = gson.fromJson(baseResponse.getContent(), EmpInfos.class);
            if(empInfos != null){
                response.employeeInfo = empInfos.empInfo;
            }

            Log.i(TAG, "response.employeeInfo = " + response.employeeInfo);
        }
        return response;
    }

}
