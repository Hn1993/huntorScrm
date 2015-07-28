package com.huntor.scrm.net.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.huntor.scrm.model.FansGroupResult;
import com.huntor.scrm.model.Response;
import com.huntor.scrm.net.BaseRequestParams;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequest;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.PreferenceUtils;

/**
 * 查询员工固定分组中的粉丝列表
 */
public class ApiFansGroup extends HttpApiBase {

    private static final String TAG = "ApiFansGroup";

    public static int mType = -1;
    /**
     * 查询员工固定分组中的粉丝列表
     *
     * @param context
     *            上下文
     * @param params
     *            查询员工固定分组中的粉丝列表需要的参数
     */
    public ApiFansGroup(Context context, ApiFansGroupParams params) {
        super(context);
        String request_url_main = Constant.HTTP_REQUEST_FANS_GROUP;
        String request_url = PreferenceUtils.getString(context, "request_url", "");

        if (!TextUtils.isEmpty(request_url)) {
            request_url_main = request_url + request_url_main.replace(Constant.HTTP_ROOT_URL, "");
        }

        mHttpRequest = new HttpRequest(request_url_main,
                HttpRequest.REQUEST_METHOD_HTTP_GET, HttpRequest.RESPONSE_TYPE_JSON, params);
    }

    /**
     * 查询员工固定分组中的粉丝列表需要的参数
     */
    public static class ApiFansGroupParams extends BaseRequestParams {

        private int empId;//员工id
        private int type;//分组类型  1新增 2普通 3高潜 4已购 5全部
        private int pageSize;//页面大小
        private int pageNum;//页码
        private int orderBy;//排序字段
        private int orderFlag;//排序标注

        /**
         * 查询员工固定分组中的粉丝列表需要的参数
         *
         */
        public ApiFansGroupParams(int empId, int type,int pageSize,int pageNum,int orderBy,int orderFlag) {
            super();
            this.empId = empId;
            this.type = type;
            this.pageSize = pageSize;
            this.pageNum = pageNum;
            this.orderBy = orderBy;
            this.orderFlag = orderFlag;
            mType = type;

        }

        /**
         * 根据成员变量生成参数
         */
        @Override
        public String generateRequestParams() {
            return "?empId=" + empId + "&type=" + type +"&pageSize="+pageSize + "&pageNum="+pageNum+"&orderBy="+orderBy+"&orderFlag="+orderFlag;
        }
    }

    /**
     * 查询员工固定分组中的粉丝列表返回结果
     */
    public static class ApiFansGroupResponse extends BaseResponse {
        public FansGroupResult fansGroupResult;
    }

    public ApiFansGroupResponse getHttpResponse() {
        BaseResponse baseResponse = getHttpContent();

        ApiFansGroupResponse response = new ApiFansGroupResponse();
        response.setRetCode(baseResponse.getRetCode());
        response.setRetInfo(baseResponse.getRetInfo());

        if (baseResponse.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
            Gson gson = new Gson();
            Log.i(TAG, "baseResponse.getContent = " + baseResponse.getContent());
            response.fansGroupResult = gson.fromJson(baseResponse.getContent(), FansGroupResult.class);
            Log.i(TAG, "response.fansGroupResult = " + response.fansGroupResult);

//            ApiFixedGroupFansListDb afd = new ApiFixedGroupFansListDb(mContext);
//            if(mType != 5){
//                afd.bulkInsertByFixGroupId(response.fansGroupResult.fans,mType);
//            }

        }
        return response;
    }

}
