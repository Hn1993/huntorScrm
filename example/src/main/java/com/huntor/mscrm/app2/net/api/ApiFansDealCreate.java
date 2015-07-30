package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.DealDetails;
import com.huntor.mscrm.app2.model.DealResult;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

import java.util.List;

/**
 * 粉丝创建一条交易
 */
public class ApiFansDealCreate extends HttpApiBase {

    private static final String TAG = "ApiFansDealCreate";

    /**
     * 粉丝创建一条交易
     *
     * @param context
     *            上下文
     * @param params
     *            粉丝创建一条交易需要的参数
     */
    public ApiFansDealCreate(Context context, ApiFansDealCreateParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_DEAL_CREATE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 粉丝创建一条交易需要的参数
     */
    public static class ApiFansDealCreateParams extends BaseRequestParams {

        private int empId;
        private int accountId;
        private String details;
        /**
         * 粉丝创建一条交易需要的参数
         *
         */
        public ApiFansDealCreateParams(int empId, int accountId,List<DealDetails> details) {
            super();
            this.empId = empId;
            this.accountId = accountId;
            if(details != null){
                Gson gson = new Gson();
                this.details = gson.toJson(details);
            }
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId=" + empId + "&accountId="+accountId+"&details="+details;
        }
    }

    /**
     * 交易返回结果
     */
    public static class ApiFansDealCreateResponse extends BaseResponse {
        public DealResult dealResult;
    }

    public ApiFansDealCreateResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansDealCreateResponse response = new ApiFansDealCreateResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.dealResult = gson.fromJson(baseResponse.getContent(), DealResult.class);
            Log.i(TAG, "response.dealResult = " + response.dealResult);
        }
        return response;
    }

}
