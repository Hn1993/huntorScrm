package com.huntor.mscrm.app.net;

/**
 * Http请求返回后的回调接口
 */
public interface HttpResponseListener<T extends BaseResponse> {

    public void onResult(T response);

}