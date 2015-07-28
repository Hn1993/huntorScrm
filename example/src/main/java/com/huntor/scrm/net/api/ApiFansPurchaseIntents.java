package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.PurchaseIntent;
import com.huntor.scrm.model.PurchaseIntentReq;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

import java.util.Date;

/**
 * 粉丝新增购买意向
 */
public class ApiFansPurchaseIntents extends HttpApiBase {

    private static final String TAG = "ApiFansPurchaseIntents";

    /**
     * 登陆
     *
     * @param context
     *            上下文
     * @param params
     *            粉丝增购买意向需要的参数
     */
    public ApiFansPurchaseIntents(Context context, ApiFansPurchaseIntentsParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_ADD_PURCHASE_INTENTS;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 粉丝增购买意向需要的参数
     */
    public static class ApiFansPurchaseIntentsParams extends BaseRequestParams {

        private int accountId;//产品id
        private String purchaseIntent;//用户购买意向

        /**
         * 粉丝增购买意向需要的参数
         *
         */
        public ApiFansPurchaseIntentsParams(int accountId, int productId,String desc,long intentTime) {
            super();
            this.accountId = accountId;
            PurchaseIntentReq pi = new PurchaseIntentReq();
            pi.productId = productId;
            pi.desc = desc;
            pi.intentTime = intentTime;
            Gson gson = new Gson();
            purchaseIntent = gson.toJson(pi);
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?accountId=" + accountId + "&purchaseIntent="+purchaseIntent;
        }
    }

    /**
     * 添加登陆返回结果
     */
    public static class ApiFansPurchaseIntentsResponse extends BaseResponse {
        public PurchaseIntent purchaseIntent;
    }

    public ApiFansPurchaseIntentsResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansPurchaseIntentsResponse response = new ApiFansPurchaseIntentsResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.purchaseIntent = gson.fromJson(baseResponse.getContent(), PurchaseIntent.class);
            Log.e(TAG, "response.purchaseIntent="+response.purchaseIntent);
        }
        return response;
    }

}
