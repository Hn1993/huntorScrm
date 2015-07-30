package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.Conversation;
import com.huntor.mscrm.app2.model.Qrcode;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

/**
 * 生成员工卖出产品的注册二维码。
 */
public class ApiQrCreate extends HttpApiBase {

    private static final String TAG = "ApiQrCreate";

    /**
     * 生成员工卖出产品的注册二维码。
     *
     * @param context
     *            上下文
     * @param params
     *            查询会话记录需要的参数
     */
    public ApiQrCreate(Context context, ApiQrCreateParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_QR_CREATE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 生成员工卖出产品的注册二维码需要的参数
     */
    public static class ApiQrCreateParams extends BaseRequestParams {
        private int empId;//员工ID
        private int socialId;//公众号id
        private String sn;//商品SN

        /**
         * 生成员工卖出产品的注册二维码需要的参数
         *
         */
        public ApiQrCreateParams(int empId,int socialId,String sn) {
            super();
            this.empId = empId;
            this.socialId = socialId;
            this.sn = sn;

        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId="+empId+"&socialId="+socialId+"&sn="+sn;
        }
    }

    /**
     * 查询会话记录返回结果
     */
    public static class ApiQrCreateResponse extends BaseResponse {
        public Qrcode qrcode;
    }

    public ApiQrCreateResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiQrCreateResponse response = new ApiQrCreateResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());
        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.qrcode = gson.fromJson(baseResponse.getContent(), Qrcode.class);

            Log.i(TAG, "response.qrcode = " + response.qrcode);
        }
        return response;
    }

}
