package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.KbEntry;
import com.huntor.mscrm.app2.model.KbEntryResult;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.provider.api.ApiKbEntryDb;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

import java.util.List;

/**
 * 查询某知识库分类下的具体条目
 */
public class ApiKbCategorieContent extends HttpApiBase {

    private static final String TAG = "ApiKbCategorieContent";

    private static int mCategorieId = 0;
    /**
     * 查询某知识库分类下的具体条目
     *
     * @param context
     *            上下文
     * @param params
     *            添加查询某知识库分类下的具体条目需要的参数
     */
    public ApiKbCategorieContent(Context context, ApiKbCategorieContentParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_REALTIME_KNOWLEDGE_CATEGORIE_CONTENTS;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加查询某知识库分类下的具体条目需要的参数
     */
    public static class ApiKbCategorieContentParams extends BaseRequestParams {

        private int categorieId;

        /**
         * 查询某知识库分类下的具体条目需要的参数
         *
         */
        public ApiKbCategorieContentParams(int categorieId) {
            super();
            this.categorieId = categorieId;
            mCategorieId = categorieId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?categorieId=" + categorieId;
        }
    }

    /**
     * 查询某知识库分类下的具体条目返回结果
     */
    public static class ApiKbCategorieContentResponse extends BaseResponse {
        public List<KbEntry> kbEntries;
    }

    public ApiKbCategorieContentResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiKbCategorieContentResponse response = new ApiKbCategorieContentResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            KbEntryResult kbEntryResult = gson.fromJson(baseResponse.getContent(), KbEntryResult.class);
            if(kbEntryResult != null){
                response.kbEntries = kbEntryResult.kbEntrys;
                ApiKbEntryDb kbEntryDb = new ApiKbEntryDb(mContext);
                kbEntryDb.bulkInsert(kbEntryResult.kbEntrys,mCategorieId);

            }
            Log.i(TAG, "response.kbEntries = " + response.kbEntries);
        }
        return response;
    }

}
