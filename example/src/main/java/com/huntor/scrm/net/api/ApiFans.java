package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.FanInfo;
import com.huntor.scrm.model.FansResult;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

/**
 * 获取粉丝的详细信息
 */
public class ApiFans extends HttpApiBase {

    private static final String TAG = "ApiFans";
    private static int mFanId;
    /**
     * 获取粉丝的详细信息
     *
     * @param context
     *            上下文
     * @param params
     *            添加获取粉丝的详细信息需要的参数
     */
    public ApiFans(Context context, ApiFansParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加获取粉丝的详细信息需要的参数
     */
    public static class ApiFansParams extends BaseRequestParams {

        private int fanId;

        /**
         * 获取粉丝的详细信息需要的参数
         *
         */
        public ApiFansParams(int fanId) {
            super();
            this.fanId = fanId;
            mFanId = fanId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?fanId=" + fanId;
        }
    }

    /**
     * 添加登陆返回结果
     */
    public static class ApiFansResponse extends BaseResponse {
        public FanInfo fanInfo;
    }

    public ApiFansResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansResponse response = new ApiFansResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            FansResult fansResult = gson.fromJson(baseResponse.getContent(), FansResult.class);
            if(fansResult != null){
                response.fanInfo = fansResult.fanInfo;
            }
            Log.i(TAG, "response.fanInfo = " + response.fanInfo);

//            ApiFansInFoDb aff = new ApiFansInFoDb(mContext);
//            Log.e(TAG,"================="+System.currentTimeMillis());
//            response.fanInfo.fanId = mFanId;
//            aff.insert(response.fanInfo);
//            Log.e(TAG, "================================" + System.currentTimeMillis());
        }
        return response;
    }

}
