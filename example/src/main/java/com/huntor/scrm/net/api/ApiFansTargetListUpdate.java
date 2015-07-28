package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

/**
 * 员工自定义分组 编辑
 */
public class ApiFansTargetListUpdate extends HttpApiBase {

    private static final String TAG = "ApiFansTargetListUpdate";

    /**
     * 员工自定义分组 编辑
     *
     * @param context
     *            上下文
     * @param params
     *            添加登陆需要的参数
     */
    public ApiFansTargetListUpdate(Context context, ApiFansTargetListUpdateParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_FANS_TARGETLIST_UPDATE;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_POST, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 添加员工自定义分组 编辑需要的参数
     */
    public static class ApiFansTargetListUpdateParams extends BaseRequestParams {

        private int targetListId;
        private String name;
        private String desc;

        /**
         * 添加员工自定义分组 编辑需要的参数
         *
         */
        public ApiFansTargetListUpdateParams(int targetListId, String name,String desc) {
            super();
            this.targetListId = targetListId;
            this.name = name;
            this.desc = desc;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?targetListId=" + targetListId + "&name="+name+"&desc="+desc;
        }
    }

    /**
     * 添加员工自定义分组 编辑返回结果
     */
    public static class ApiFansTargetListUpdateResponse extends BaseResponse {
        public Response response;
    }

    public ApiFansTargetListUpdateResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansTargetListUpdateResponse response = new ApiFansTargetListUpdateResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

            Log.i(TAG, "response.response = " + response.response);
        }
        return response;
    }

}
