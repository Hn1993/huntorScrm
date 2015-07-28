package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.FansGroupCount;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

/**
 * 各固定分组人数查询
 */
public class ApiFansGroupCount extends HttpApiBase {

    private static final String TAG = "ApiFansGroupCount";

    /**
     * 各固定分组人数查询
     *
     * @param context
     *            上下文
     * @param params
     *            各固定分组人数查询需要的参数
     */
    public ApiFansGroupCount(Context context, ApiFansGroupCountParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_GROUP_COUNT;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }
        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 各固定分组人数查询需要的参数
     */
    public static class ApiFansGroupCountParams extends BaseRequestParams {

        private int empId;

        /**
         * 各固定分组人数查询需要的参数
         *
         */
        public ApiFansGroupCountParams(int empId) {
            super();
            this.empId = empId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId=" + empId;
        }
    }

    /**
     * 各固定分组人数查询返回结果
     */
    public static class ApiFansGroupCountResponse extends BaseResponse {
        public FansGroupCount fansGroupCount;
    }

    public ApiFansGroupCountResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansGroupCountResponse response = new ApiFansGroupCountResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.fansGroupCount = gson.fromJson(baseResponse.getContent(), FansGroupCount.class);
            Log.i(TAG, "response.fansGroupCount = " + response.fansGroupCount.data.toString());

//            ApiFansGroupCountDb fansGroupCountDb = new ApiFansGroupCountDb(mContext);
//            fansGroupCountDb.bulkInsert(response.fansGroupCount.data);
        }
        return response;
    }

}
