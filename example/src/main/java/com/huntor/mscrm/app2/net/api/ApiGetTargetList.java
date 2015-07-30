package com.huntor.mscrm.app2.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.model.FansQueryResult;
import com.huntor.mscrm.app2.model.LoginResult;
import com.huntor.mscrm.app2.net.BaseRequestParams;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequest;
import com.huntor.mscrm.app2.provider.api.ApiFixedGroupFansListDb;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

/**
 * 查询员工的一个自定义粉丝分组内的粉丝列表
 */
public class ApiGetTargetList extends HttpApiBase {

    private static final String TAG = "ApiGetTargetList";
    private static int mTargetListId;
    /**
     * 查询员工的一个自定义粉丝分组内的粉丝列表
     *
     * @param context
     *            上下文
     * @param params
     *            添加登陆需要的参数
     */
    public ApiGetTargetList(Context context, ApiGetTargetListParams params) {
        super(context);

        String request_url_main = Constant.HTTP_REQUEST_FANS_TARGETLIST_LIST;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 查询员工的一个自定义粉丝分组内的粉丝列表需要的参数
     */
    public static class ApiGetTargetListParams extends BaseRequestParams {

        private int targetListId;//自定义分组id
        private int pageSize;//页面大小
        private int pageNum;//页码
        private int orderBy;//排序字段
        private int orderFlag;//排序标志

        /**
         * 添加查询员工的一个自定义粉丝分组内的粉丝列表需要的参数
         *
         */
        public ApiGetTargetListParams(int targetListId,int pageSize,int pageNum,int orderBy,int orderFlag) {
            super();
            this.targetListId = targetListId;
            this.pageSize = pageSize;
            this.pageNum = pageNum;
            this.orderBy = orderBy;
            this.orderFlag = orderFlag;
            mTargetListId  = targetListId;
        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?targetListId=" + targetListId + "&pageSize="+pageSize+"&pageNum="+pageNum+"&orderBy="+orderBy+"&orderFlag="+orderFlag;
        }
    }

    /**
     * 查询员工的一个自定义粉丝分组内的粉丝列表返回结果
     */
    public static class ApiGetTargetListResponse extends BaseResponse {
        public FansQueryResult fansQueryResult;
    }

    public ApiGetTargetListResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiGetTargetListResponse response = new ApiGetTargetListResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            response.fansQueryResult = gson.fromJson(baseResponse.getContent(), FansQueryResult.class);
            Log.i(TAG, "response.fansQueryResult = " + response.fansQueryResult);

            ApiFixedGroupFansListDb afg = new ApiFixedGroupFansListDb(mContext);
            afg.bulkInsertByTargetId(response.fansQueryResult.fans,mTargetListId);
        }
        return response;
    }

}
