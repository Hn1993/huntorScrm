package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.model.Response;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

/**
 * 手工刷新业务缓存
 */
public class ApiFlushCache extends HttpApiBase {

    private static final String TAG = "ApiFlushCache";

    /**
     * 手工业务缓存
     *
     * @param context
     *            上下文
     * @param params
     *            添加刷新需要的参数
     */
    public ApiFlushCache(Context context, ApiFlushCacheParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FLUSH_CACHE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加刷新缓存需要的参数
     */
    public static class ApiFlushCacheParams extends BaseRequestParams {

        private String cacheName;

        /**
         * 添加刷新缓存需要的参数
         *
         */
        public ApiFlushCacheParams(String cacheName) {
            super();
            this.cacheName = cacheName;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?cacheName=" + cacheName;
        }
    }

    /**
     * 刷新缓存返回结果
     */
    public static class ApiFlushCacheResponse extends BaseResponse {
        public Response flushResponse;
    }

    public ApiFlushCacheResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFlushCacheResponse response = new ApiFlushCacheResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.flushResponse = gson.fromJson(baseResponse.getContent(), LoginResult.class);
            Log.i(TAG, "response.flushResponse = " + response.flushResponse);
        }
        return response;
    }

}
