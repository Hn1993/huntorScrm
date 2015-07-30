package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.model.ModifyFansParam;
import com.huntor.mscrm.app2.model.Response;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

/**
 * 粉丝详情修改
 */
public class ApiFansModify extends HttpApiBase {

    private static final String TAG = "ApiFansModify";

    /**
     * 登陆
     *
     * @param context 上下文
     * @param params  添加登陆需要的参数
     */
    public ApiFansModify(Context context, ApiFansModifyParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_MODIFY;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);

    }

    /**
     * 添加登陆需要的参数
     */
    public static class ApiFansModifyParams extends BaseRequestParams {

        private ModifyFansParam mFp;

        /**
         * 添加登陆需要的参数
         */
        public ApiFansModifyParams(ModifyFansParam mfp) {
            super();
            mFp = mfp;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?accountId=" + mFp.accountId + "&fanId=" + mFp.fanId + "&name=" + mFp.name + "&gender=" + mFp.gender + "&ageGroup=" + mFp.ageGroup + "&occupation=" + mFp.occupation + "&phone1=" + mFp.phone1 + "&phone2=" + mFp.phone2 + "&phone3=" + mFp.phone3;
        }
    }

    /**
     * 添加登陆返回结果
     */
    public static class ApiFansModifyResponse extends BaseResponse {
        public Response re;
    }

    public ApiFansModifyResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansModifyResponse response = new ApiFansModifyResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());
        Log.e(TAG, "" + baseResponse.toString());
        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.re = gson.fromJson(baseResponse.getContent(), Response.class);
            Log.e(TAG, "response.re = " + response.re);
        }
        return response;
    }

}
