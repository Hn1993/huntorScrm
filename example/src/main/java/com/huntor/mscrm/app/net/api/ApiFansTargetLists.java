package com.huntor.mscrm.app.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app.model.LoginResult;
import com.huntor.mscrm.app.model.Target;
import com.huntor.mscrm.app.model.TargetListResult;
import com.huntor.mscrm.app.net.BaseRequestParams;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequest;
import com.huntor.mscrm.app.provider.api.ApiTargetListDb;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;

import java.util.List;

/**
 * 查询员工的自定义分组列表
 */
public class ApiFansTargetLists extends HttpApiBase {

    private static final String TAG = "ApiFansTargetLists";

    /**
     * 查询员工的自定义分组列表
     *
     * @param context
     *            上下文
     * @param params
     *            添加查询员工的自定义分组列表需要的参数
     */
    public ApiFansTargetLists(Context context, ApiFansTargetListsParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_TARGETLISTS;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加查询员工的自定义分组列表需要的参数
     */
    public static class ApiFansTargetListsParams extends BaseRequestParams {

        private int empId;//员工ID

        /**
         * 添加查询员工的自定义分组列表需要的参数
         *
         */
        public ApiFansTargetListsParams(int empId) {
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
     * 添加查询员工的自定义分组列表返回结果
     */
    public static class ApiFansTargetListsResponse extends BaseResponse {
        public List<Target> targetLists;
    }

    public ApiFansTargetListsResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansTargetListsResponse response = new ApiFansTargetListsResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            TargetListResult tlr = gson.fromJson(baseResponse.getContent(), TargetListResult.class);
            if(tlr != null){
                response.targetLists = tlr.targetList;
            }

            Log.i(TAG, "response.targetlists = " + response.targetLists);
            ApiTargetListDb at = new ApiTargetListDb(mContext);
            at.bulkInsert(response.targetLists);
        }
        return response;
    }

}
