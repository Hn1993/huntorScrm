package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.ProductCategories;
import com.huntor.scrm.model.ProductResult;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

import java.util.List;

/**
 * 手工刷新业务缓存
 */
public class ApiProducts extends HttpApiBase {

    private static final String TAG = "ApiProducts";

    /**
     * 手工业务缓存
     *
     * @param context
     *            上下文
     * @param params
     *            添加刷新需要的参数
     */
    public ApiProducts(Context context, ApiProductsParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_PRODUCTS;
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
    public static class ApiProductsParams extends BaseRequestParams {

        /**
         * 添加刷新缓存需要的参数
         *
         */
        public ApiProductsParams() {
            super();
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "";
        }
    }

    /**
     * 刷新缓存返回结果
     */
    public static class ApiProductsResponse extends BaseResponse {
        public List<ProductCategories> categories;
    }

    public ApiProductsResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiProductsResponse response = new ApiProductsResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            ProductResult product = gson.fromJson(baseResponse.getContent(), ProductResult.class);

            if(product != null){
                response.categories = product.categories;
            }
            Log.i(TAG, "response.categories = " + response.categories);
        }
        return response;
    }

}
