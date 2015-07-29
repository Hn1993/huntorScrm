package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.Categorie;
import com.huntor.mscrm.app.model.CategorieResult;
import com.huntor.mscrm.app.model.LoginResult;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.provider.api.ApiKbCategoriesDb;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;

import java.util.List;

/**
 * 查询MSCRM知识库分类
 */
public class ApiKbGategories extends HttpApiBase {

    private static final String TAG = "ApiKbGategories";

    /**
     * 查询MSCRM知识库分类
     *
     * @param context
     *            上下文
     * @param params
     *            添加查询MSCRM知识库分类需要的参数
     */
    public ApiKbGategories(Context context, ApiKbGategoriesParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_REALTIME_KNOWLEDGE_CATEGORIE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加查询MSCRM知识库分类需要的参数
     */
    public static class ApiKbGategoriesParams extends BaseRequestParams {

        /**
         * 添加查询MSCRM知识库分类需要的参数
         *
         */
        public ApiKbGategoriesParams() {
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
     * 查询MSCRM知识库分类返回结果
     */
    public static class ApiKbGategoriesResponse extends BaseResponse {
        public List<Categorie> categories;
    }

    public ApiKbGategoriesResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiKbGategoriesResponse response = new ApiKbGategoriesResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            CategorieResult categorieResult = gson.fromJson(baseResponse.getContent(), CategorieResult.class);
            if(categorieResult != null){
                response.categories = categorieResult.categories;
                PreferenceUtils.putLong(mContext,Constant.PREFERENCES_SAVE_TIME,System.currentTimeMillis());
                ApiKbCategoriesDb kbGategories = new ApiKbCategoriesDb(mContext);
                kbGategories.bulkInsert(categorieResult.categories);
            }

            Log.i(TAG, "response.categories = " + response.categories);
        }
        return response;
    }

}
