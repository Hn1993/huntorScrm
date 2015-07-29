package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.BatchQueryFansResult;
import com.huntor.mscrm.app.model.LoginResult;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PreferenceUtils;

import java.util.List;

/**
 * 批量查询一批粉丝的信息
 */
public class ApiFansList extends HttpApiBase {

    private static final String TAG = "ApiFansList";

    /**
     * 批量查询一批粉丝的信息
     *
     * @param context
     *            上下文
     * @param params
     *            添加批量查询一批粉丝的信息需要的参数
     */
    public ApiFansList(Context context, ApiFansListParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_LIST;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加批量查询一批粉丝的信息需要的参数
     */
    public static class ApiFansListParams extends BaseRequestParams {

        private String fanIds;

        /**
         * 添加批量查询一批粉丝的信息需要的参数
         *
         */
        public ApiFansListParams(int[] fanIds) {
            super();
            Gson gson = new Gson();
            if(fanIds != null){
                this.fanIds = gson.toJson(fanIds);
            }else{
                this.fanIds ="";
            }
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?fanIdList=" + fanIds;
        }
    }

    /**
     * 批量查询一批粉丝返回结果
     */
    public static class ApiFansListResponse extends BaseResponse {
        public List<BatchQueryFansResult.Fans> fansList;
    }

    public ApiFansListResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansListResponse response = new ApiFansListResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());
        MyLogger.e(TAG,"baseResponse: "+baseResponse.toString());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            BatchQueryFansResult bfr = gson.fromJson(baseResponse.getContent(), BatchQueryFansResult.class);
            if(bfr.fans != null){
                response.fansList = bfr.fans;
                Log.i(TAG, "response.fansList = " + response.fansList);
            }

        }
        return response;
    }

}
