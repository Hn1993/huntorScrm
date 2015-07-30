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
 * 修改密码
 */
public class ApiModifyPwd extends HttpApiBase {

    private static final String TAG = "ApiModifyPwd";

    /**
     * 修改密码
     *
     * @param context
     *            上下文
     * @param params
     *            添加修改密码需要的参数
     */
    public ApiModifyPwd(Context context, ApiModifyPwdParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_MODIFY_PWD;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加修改密码需要的参数
     */
    public static class ApiModifyPwdParams extends BaseRequestParams {

        private String userName;//用户名
        private String pwd;//密码
        private String newPwd;//新密码

        /**
         * 添加修改密码需要的参数
         *
         */
        public ApiModifyPwdParams(String userName, String pwd,String newPwd) {
            super();
            this.userName = userName;
            this.pwd = pwd;
            this.newPwd = newPwd;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?userName=" + userName + "&pwd="+pwd+"&newPwd="+newPwd;
        }
    }

    /**
     * 添加修改密码返回结果
     */
    public static class ApiModifyPwdResponse extends BaseResponse {
        public Response response;
    }

    public ApiModifyPwdResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiModifyPwdResponse response = new ApiModifyPwdResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Log.i(TAG, "response.loginResult = " + response.response);
        }
        return response;
    }

}
