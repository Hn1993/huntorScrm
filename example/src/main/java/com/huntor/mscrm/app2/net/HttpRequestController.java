package com.huntor.mscrm.app2.net;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.huntor.mscrm.app2.model.DealDetails;
import com.huntor.mscrm.app2.model.ModifyFansParam;
import com.huntor.mscrm.app2.net.api.*;
import com.huntor.mscrm.app2.utils.MyLogger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Http请求的一个总接口，通过该类调用各Api请求服务器。由于网络请求比较耗时，所以需要在线程中作处理。
 */
public class HttpRequestController {

    private static final String TAG = "HttpRequestController";

    private static Handler mHandler = null;
    // 创建线程池
    private static final ThreadPoolExecutor mThreadPoolExecutor = (ThreadPoolExecutor) Executors
            .newCachedThreadPool();

    private HttpRequestController() {
    }

    private static void checkHandler() {
        try {
            if (mHandler == null) {
                mHandler = new Handler();
            }
        } catch (Exception e) {
            mHandler = null;
        }
    }

    /**
     * 登陆
     *
     * @param context  上下文
     * @param userName 用户名
     * @param psw      密码
     * @param listener 请求完成后的回调
     */
    public static void login(final Context context, final String userName, final String psw,
                             final HttpResponseListener<ApiLogin.ApiLoginResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiLogin.ApiLoginParams params = new ApiLogin.ApiLoginParams(userName, psw);

                ApiLogin apiLogin = new ApiLogin(context, params);
                final ApiLogin.ApiLoginResponse response = apiLogin.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 请求验证码
     *
     * @param context  上下文
     * @param userName 用户名
     * @param listener 请求完成后的回调
     */
    public static void reqVerifyCode(final Context context, final String userName, final HttpResponseListener<ApiReqVerifyCode.ApiReqVerifyCodeResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiReqVerifyCode.ApiReqVerifyCodeParams params = new ApiReqVerifyCode.ApiReqVerifyCodeParams(userName);
                ApiReqVerifyCode apiReqVerifyCode = new ApiReqVerifyCode(context, params);
                final ApiReqVerifyCode.ApiReqVerifyCodeResponse response = apiReqVerifyCode.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 验证短信验证码
     *
     * @param context  上下文
     * @param userName 用户名
     * @param listener 请求完成后的回调
     */
    public static void verify(final Context context, final String userName, final String verifyCode, final HttpResponseListener<ApiVerify.ApiVerifyResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiVerify.ApiVerifyParams params = new ApiVerify.ApiVerifyParams(userName,verifyCode);
                ApiVerify apiVerify = new ApiVerify(context, params);
                final ApiVerify.ApiVerifyResponse response = apiVerify.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 设置新密码
     *
     * @param context  上下文
     * @param userName 用户名
     * @param listener 请求完成后的回调
     */
    public static void resetPwd(final Context context, final String userName, final String token, final String newPwd, final HttpResponseListener<ApiResetPwdDone.ApiResetPwdDoneResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiResetPwdDone.ApiResetPwdDoneParams params = new ApiResetPwdDone.ApiResetPwdDoneParams( userName ,token,newPwd);
                ApiResetPwdDone apiResetPwdDone = new ApiResetPwdDone(context, params);
                final ApiResetPwdDone.ApiResetPwdDoneResponse response = apiResetPwdDone.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 手工刷新业务缓存
     *
     * @param context   上下文
     * @param cacheName 要刷新的缓存名称
     * @param listener  请求完成后的回调
     */
    public static void flushCache(final Context context, final String cacheName,
                                  final HttpResponseListener<ApiFlushCache.ApiFlushCacheResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFlushCache.ApiFlushCacheParams params = new ApiFlushCache.ApiFlushCacheParams(cacheName);

                ApiFlushCache apiFlushCache = new ApiFlushCache(context, params);
                final ApiFlushCache.ApiFlushCacheResponse response = apiFlushCache.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 获取产品列表
     *
     * @param context  上下文
     * @param listener 请求完成后的回调
     */
    public static void getProducts(final Context context,
                                   final HttpResponseListener<ApiProducts.ApiProductsResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiProducts.ApiProductsParams params = new ApiProducts.ApiProductsParams();

                ApiProducts apiProducts = new ApiProducts(context, params);
                final ApiProducts.ApiProductsResponse response = apiProducts.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 查询导购与粉丝的会话列表
     *
     * @param empId     员工id
     * @param pageSize  页面大小
     * @param pageNum   页码
     * @param orderBy   排序字段
     * @param orderFlag 排序标志
     * @param context   上下文
     * @param listener  请求完成后的回调
     */
    public static void getConversations(final Context context, final int empId, final int pageSize, final int pageNum, final int orderBy, final int orderFlag,
                                        final HttpResponseListener<ApiConversations.ApiConversationsResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiConversations.ApiConversationsParams params = new ApiConversations.ApiConversationsParams(empId, pageSize, pageNum, orderBy, orderFlag);

                ApiConversations ApiConversations = new ApiConversations(context, params);
                final ApiConversations.ApiConversationsResponse response = ApiConversations.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 查询导购与粉丝的会话列表
     *
     * @param empId    员工id
     * @param socialId 公众号id
     * @param sn       商品
     * @param context  上下文
     * @param listener 请求完成后的回调
     */
    public static void createQr(final Context context, final int empId, final int socialId, final String sn,
                                final HttpResponseListener<ApiQrCreate.ApiQrCreateResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiQrCreate.ApiQrCreateParams params = new ApiQrCreate.ApiQrCreateParams(empId, socialId, sn);

                ApiQrCreate apiQrCreate = new ApiQrCreate(context, params);
                final ApiQrCreate.ApiQrCreateResponse response = apiQrCreate.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 员工基础信息查询
     *
     * @param empId    员工id
     * @param context  上下文
     * @param listener 请求完成后的回调
     */
    public static void queryEmployeeInfo(final Context context, final int empId,
                                         final HttpResponseListener<ApiEmployee.ApiEmployeeResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiEmployee.ApiEmployeeParams params = new ApiEmployee.ApiEmployeeParams(empId);

                ApiEmployee apiEmployee = new ApiEmployee(context, params);
                final ApiEmployee.ApiEmployeeResponse response = apiEmployee.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 创建交易
     *
     * @param empId       员工id
     * @param accountId   账户id
     * @param dealDetails 交易明细
     * @param context     上下文
     * @param listener    请求完成后的回调
     */
    public static void createDeal(final Context context, final int empId, final int accountId, final List<DealDetails> dealDetails,
                                  final HttpResponseListener<ApiFansDealCreate.ApiFansDealCreateResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansDealCreate.ApiFansDealCreateParams params = new ApiFansDealCreate.ApiFansDealCreateParams(empId, accountId, dealDetails);

                ApiFansDealCreate apiFansDealCreate = new ApiFansDealCreate(context, params);
                final ApiFansDealCreate.ApiFansDealCreateResponse response = apiFansDealCreate.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }


    /**
     * 各固定分组人数查询
     *
     * @param context  上下文
     * @param empId    员工Id
     * @param listener 请求完成后的回调
     */
    public static void fansGroupCount(final Context context, final int empId,
                                      final HttpResponseListener<ApiFansGroupCount.ApiFansGroupCountResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansGroupCount.ApiFansGroupCountParams params = new ApiFansGroupCount.ApiFansGroupCountParams(empId);

                ApiFansGroupCount apiFansGroupCount = new ApiFansGroupCount(context, params);
                final ApiFansGroupCount.ApiFansGroupCountResponse response = apiFansGroupCount.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 查询员工固定分组中的粉丝列表
     *
     * @param context   上下文
     * @param empId     员工id
     * @param type      分组类型  1新增 2普通 3高潜 4已购 5全部
     * @param pageSize  页面大小
     * @param pageNum   页码
     * @param orderBy   排序字段
     * @param orderFlag 排序标志
     * @param listener  请求完成后的回调
     */
    public static void fansGroup(final Context context, final int empId, final int type, final int pageSize, final int pageNum, final int orderBy, final int orderFlag,
                                 final HttpResponseListener<ApiFansGroup.ApiFansGroupResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansGroup.ApiFansGroupParams params = new ApiFansGroup.ApiFansGroupParams(empId, type, pageSize, pageNum, orderBy, orderFlag);

                ApiFansGroup apiFansGroup = new ApiFansGroup(context, params);
                final ApiFansGroup.ApiFansGroupResponse response = apiFansGroup.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 新增粉丝购买意向
     *
     * @param context    上下文
     * @param accountId  账户id
     * @param productId  产品id
     * @param desc       描述
     * @param intentTime 意向时间
     * @param listener   请求完成后的回调
     */
    public static void addFansPurchaseIntent(final Context context, final int accountId, final int productId, final String desc, final long intentTime,
                                             final HttpResponseListener<ApiFansPurchaseIntents.ApiFansPurchaseIntentsResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansPurchaseIntents.ApiFansPurchaseIntentsParams params = new ApiFansPurchaseIntents.ApiFansPurchaseIntentsParams(accountId, productId, desc, intentTime);

                ApiFansPurchaseIntents apiFansPurchaseIntents = new ApiFansPurchaseIntents(context, params);
                final ApiFansPurchaseIntents.ApiFansPurchaseIntentsResponse response = apiFansPurchaseIntents.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 粉丝删除购买意向
     *
     * @param context          上下文
     * @param accountId        账户id
     * @param purchaseIntentId 购买意向id
     * @param listener         请求完成后的回调
     */
    public static void deletePurchaseIntent(final Context context, final int accountId, final int purchaseIntentId,
                                            final HttpResponseListener<ApiDeletePIntent.ApiDeletePIntentResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiDeletePIntent.ApiDeletePIntentParams params = new ApiDeletePIntent.ApiDeletePIntentParams(accountId, purchaseIntentId);

                ApiDeletePIntent apiDeletePIntent = new ApiDeletePIntent(context, params);
                final ApiDeletePIntent.ApiDeletePIntentResponse response = apiDeletePIntent.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 添加粉丝到员工自定义分组
     *
     * @param context      上下文
     * @param targetListId 自定义分组id
     * @param fanIds       粉丝id
     * @param listener     请求完成后的回调
     */
    public static void addFansTargetList(final Context context, final int targetListId, final int[] fanIds,
                                         final HttpResponseListener<ApiAddFansTargetlist.ApiAddFansTargetlistResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiAddFansTargetlist.ApiAddFansTargetlistParams params = new ApiAddFansTargetlist.ApiAddFansTargetlistParams(targetListId, fanIds);

                ApiAddFansTargetlist apiAddFansTargetlist = new ApiAddFansTargetlist(context, params);
                final ApiAddFansTargetlist.ApiAddFansTargetlistResponse response = apiAddFansTargetlist.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 创建一个粉丝自定义分组
     *
     * @param context  上下文
     * @param empId    员工id
     * @param name     分组名称
     * @param desc     分组描述
     * @param listener 请求完成后的回调
     */
    public static void createFansTargetList(final Context context, final int empId, final String name, final String desc,
                                            final HttpResponseListener<ApiCreateTargetlist.ApiCreateTargetlistResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiCreateTargetlist.ApiCreateTargetlistParams params = new ApiCreateTargetlist.ApiCreateTargetlistParams(empId, name, desc);

                ApiCreateTargetlist apiCreateTargetlist = new ApiCreateTargetlist(context, params);
                final ApiCreateTargetlist.ApiCreateTargetlistResponse response = apiCreateTargetlist.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 删除一个粉丝自定义分组
     *
     * @param context      上下文
     * @param targetListId 分组id
     * @param listener     请求完成后的回调
     */
    public static void deleteFansTargetList(final Context context, final int targetListId,
                                            final HttpResponseListener<ApiDeleteTargetList.ApiDeleteTargetListResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiDeleteTargetList.ApiDeleteTargetListParams params = new ApiDeleteTargetList.ApiDeleteTargetListParams(targetListId);

                ApiDeleteTargetList apiDeleteTargetList = new ApiDeleteTargetList(context, params);
                final ApiDeleteTargetList.ApiDeleteTargetListResponse response = apiDeleteTargetList.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 查询员工的一个自定义粉丝分组内的粉丝列表
     *
     * @param context      上下文
     * @param targetListId 自定义分组id
     * @param pageSize     页面大小
     * @param pageNum      页码
     * @param orderBy      排序字段
     * @param orderFlag    排序标志
     * @param listener     请求完成后的回调
     */
    public static void getFansTargetList(final Context context, final int targetListId, final int pageSize, final int pageNum, final int orderBy, final int orderFlag,
                                         final HttpResponseListener<ApiGetTargetList.ApiGetTargetListResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiGetTargetList.ApiGetTargetListParams params = new ApiGetTargetList.ApiGetTargetListParams(targetListId, pageSize, pageNum, orderBy, orderFlag);

                ApiGetTargetList apiGetTargetList = new ApiGetTargetList(context, params);
                final ApiGetTargetList.ApiGetTargetListResponse response = apiGetTargetList.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 从员工的自定义粉丝分组中批量移除粉丝
     *
     * @param context      上下文
     * @param targetListId 自定义分组id
     * @param fanIdList    粉丝id int[]
     * @param listener     请求完成后的回调
     */
    public static void getFansTargetListRemove(final Context context, final int targetListId, final int[] fanIdList,
                                               final HttpResponseListener<ApiFansTargetListRemove.ApiFansTargetListRemoveResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansTargetListRemove.ApiFansTargetListRemoveParams params = new ApiFansTargetListRemove.ApiFansTargetListRemoveParams(targetListId, fanIdList);

                ApiFansTargetListRemove apiFansTargetListRemove = new ApiFansTargetListRemove(context, params);
                final ApiFansTargetListRemove.ApiFansTargetListRemoveResponse response = apiFansTargetListRemove.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 员工自定义分组 编辑
     *
     * @param context      上下文
     * @param targetListId 分组id
     * @param name         分组名称
     * @param desc         分组描述
     * @param listener     请求完成后的回调
     */
    public static void getFansTargetListUpdate(final Context context, final int targetListId, final String name, final String desc,
                                               final HttpResponseListener<ApiFansTargetListUpdate.ApiFansTargetListUpdateResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansTargetListUpdate.ApiFansTargetListUpdateParams params = new ApiFansTargetListUpdate.ApiFansTargetListUpdateParams(targetListId, name, desc);

                ApiFansTargetListUpdate apiFansTargetListUpdate = new ApiFansTargetListUpdate(context, params);
                final ApiFansTargetListUpdate.ApiFansTargetListUpdateResponse response = apiFansTargetListUpdate.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 查询员工的自定义分组列表
     *
     * @param context  上下文
     * @param empId    员工iD
     * @param listener 请求完成后的回调
     */
    public static void getTargetLists(final Context context, final int empId,
                                      final HttpResponseListener<ApiFansTargetLists.ApiFansTargetListsResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansTargetLists.ApiFansTargetListsParams params = new ApiFansTargetLists.ApiFansTargetListsParams(empId);

                ApiFansTargetLists apiFansTargetLists = new ApiFansTargetLists(context, params);
                final ApiFansTargetLists.ApiFansTargetListsResponse response = apiFansTargetLists.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }


    /**
     * 获取粉丝的详细信息
     *
     * @param context  上下文
     * @param fanId    粉丝ID
     * @param listener 请求完成后的回调
     */
    public static void getFansInfo(final Context context, final int fanId,
                                   final HttpResponseListener<ApiFans.ApiFansResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFans.ApiFansParams params = new ApiFans.ApiFansParams(fanId);

                ApiFans apiFans = new ApiFans(context, params);
                final ApiFans.ApiFansResponse response = apiFans.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 关闭导购与粉丝的实时交互
     *
     * @param context  上下文
     * @param empId    员工id
     * @param fanId    粉丝id
     * @param listener 请求完成后的回调
     */
    public static void realTimeClose(final Context context, final int empId, final int fanId,
                                     final HttpResponseListener<ApiRealTimeClose.ApiRealTimeCloseResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiRealTimeClose.ApiRealTimeCloseParams params = new ApiRealTimeClose.ApiRealTimeCloseParams(empId, fanId);

                ApiRealTimeClose apiRealTimeClose = new ApiRealTimeClose(context, params);
                final ApiRealTimeClose.ApiRealTimeCloseResponse response = apiRealTimeClose.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 查询导购与粉丝交互的历史消息
     *
     * @param context   上下文
     * @param fanId     粉丝ID
     * @param pageSize  页面大小
     * @param sinceId   起始消息id  从这个id开始往前查，若为空则表示从最近一条开始查
     * @param orderBy   排序字段  1消息发送时间
     * @param orderFlag 排序标志  1正序 2逆序
     * @param listener  请求完成后的回调
     */
    public static void realTimeHistory(final Context context, final int fanId, final int sinceId, final int pageSize, final int orderBy, final int orderFlag,
                                       final HttpResponseListener<ApiRealTimeHistory.ApiRealTimeHistoryResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final ApiRealTimeHistory.ApiRealTimeHistoryParams params = new ApiRealTimeHistory.ApiRealTimeHistoryParams(fanId, sinceId, pageSize, orderBy, orderFlag);

                ApiRealTimeHistory apiRealTimeHistory = new ApiRealTimeHistory(context, params);
                final ApiRealTimeHistory.ApiRealTimeHistoryResponse response = apiRealTimeHistory.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());

                    }
                });
            }
        });
    }

    /***
     * 查询某知识库分类下的具体条目
     *
     * @param context     上下文
     * @param categorieId 分类Id
     * @param listener    请求完成后的回调
     */
    public static void kbContents(final Context context, final int categorieId,
                                  final HttpResponseListener<ApiKbCategorieContent.ApiKbCategorieContentResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiKbCategorieContent.ApiKbCategorieContentParams params = new ApiKbCategorieContent.ApiKbCategorieContentParams(categorieId);

                ApiKbCategorieContent apiKbCategorieContent = new ApiKbCategorieContent(context, params);
                final ApiKbCategorieContent.ApiKbCategorieContentResponse response = apiKbCategorieContent.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 查询MSCRM知识库分类
     *
     * @param context  上下文
     * @param listener 请求完成后的回调
     */
    public static void kbCategories(final Context context,
                                    final HttpResponseListener<ApiKbGategories.ApiKbGategoriesResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiKbGategories.ApiKbGategoriesParams params = new ApiKbGategories.ApiKbGategoriesParams();

                ApiKbGategories apiKbGategories = new ApiKbGategories(context, params);
                final ApiKbGategories.ApiKbGategoriesResponse response = apiKbGategories.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 开启导购与粉丝的实时交互
     *
     * @param context  上下文
     * @param empId    员工id
     * @param fanId    粉丝id
     * @param listener 请求完成后的回调
     */
    public static void realtimeOpen(final Context context, final int empId, final int fanId,
                                    final HttpResponseListener<ApiRealtimeOpen.ApiRealtimeOpenResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiRealtimeOpen.ApiRealtimeOpenParams params = new ApiRealtimeOpen.ApiRealtimeOpenParams(empId, fanId);

                ApiRealtimeOpen apiRealtimeOpen = new ApiRealtimeOpen(context, params);
                final ApiRealtimeOpen.ApiRealtimeOpenResponse response = apiRealtimeOpen.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }


    /***
     * 发送消息
     *
     * @param context  上下文
     * @param empId    员工id
     * @param fanId    粉丝id
     * @param groupId  分组id
     * @param type     类型
     * @param content  消息内容
     * @param listener 请求完成后的回调
     */
    public static void realtimeSend(final Context context, final int empId, final int fanId, final int groupId, final int type, final String content,
                                    final HttpResponseListener<ApiRealtimeSend.ApiRealtimeSendResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiRealtimeSend.ApiRealtimeSendParams params = new ApiRealtimeSend.ApiRealtimeSendParams(empId, fanId, groupId, type, content);

                ApiRealtimeSend apiRealtimeSend = new ApiRealtimeSend(context, params);
                final ApiRealtimeSend.ApiRealtimeSendResponse response = apiRealtimeSend.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 修改密码
     *
     * @param context  上下文
     * @param userName 用户名
     * @param pwd      密码
     * @param newPwd   新密码
     * @param listener 请求完成后的回调
     */
    public static void modifyPwd(final Context context, final String userName, final String pwd, final String newPwd,
                                 final HttpResponseListener<ApiModifyPwd.ApiModifyPwdResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiModifyPwd.ApiModifyPwdParams params = new ApiModifyPwd.ApiModifyPwdParams(userName, pwd, newPwd);

                ApiModifyPwd apiModifyPwd = new ApiModifyPwd(context, params);
                final ApiModifyPwd.ApiModifyPwdResponse response = apiModifyPwd.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 更改粉丝状态：新增改为非新增
     *
     * @param context  上下文
     * @param fanId    粉丝Id
     * @param listener 请求完成后的回调
     */
    public static void fansStatusNewTwoOld(final Context context, final int fanId,
                                           final HttpResponseListener<ApiNewTwoOld.ApiNewTwoOldResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiNewTwoOld.ApiNewTwoOldParams params = new ApiNewTwoOld.ApiNewTwoOldParams(fanId);

                ApiNewTwoOld apiNewTwoOld = new ApiNewTwoOld(context, params);
                final ApiNewTwoOld.ApiNewTwoOldResponse response = apiNewTwoOld.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /***
     * 批量查询一批粉丝的信息
     *
     * @param context   上下文
     * @param fanIdList 粉丝id列表
     * @param listener
     */
    public static void fansList(final Context context, final int[] fanIdList,
                                final HttpResponseListener<ApiFansList.ApiFansListResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansList.ApiFansListParams params = new ApiFansList.ApiFansListParams(fanIdList);

                ApiFansList apiFansList = new ApiFansList(context, params);
                final ApiFansList.ApiFansListResponse response = apiFansList.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    /**
     * 上传U方法
     *
     * @param context
     * @param filePath 文件本地地址
     * @param type     文件类型 1图片 2音频 3视频
     * @param listener
     */
    public static void upload(final Context context, final String filePath, final String type,
                              final HttpResponseListener<ApiUpload.ApiUploadResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiUpload apiUpload = new ApiUpload(context, filePath, type);
                final ApiUpload.ApiUploadResponse response = apiUpload.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                    }
                });
            }
        });
    }

    public static void modifyFans(final Context context, final ModifyFansParam modifyFansParam,
                                  final HttpResponseListener<ApiFansModify.ApiFansModifyResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiFansModify.ApiFansModifyParams params = new ApiFansModify.ApiFansModifyParams(modifyFansParam);
                ApiFansModify apiFansModify = new ApiFansModify(context, params);
                final ApiFansModify.ApiFansModifyResponse response = apiFansModify.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }

    public static void getAllFansId(final Context context, final int targetListId, final boolean containsUnfollow,
                                    final HttpResponseListener<ApiGetAllFansId.ApiGetAllFansIdResponse> listener) {
        checkHandler();
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ApiGetAllFansId.ApiGetAllFansIdParams params = new ApiGetAllFansId.ApiGetAllFansIdParams(targetListId, containsUnfollow);
                ApiGetAllFansId apiFansModify = new ApiGetAllFansId(context, params);
                final ApiGetAllFansId.ApiGetAllFansIdResponse response = apiFansModify.getHttpResponse();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(response);
                        Log.i(TAG, "" + response.getRetCode());
                    }
                });
            }
        });
    }
}
