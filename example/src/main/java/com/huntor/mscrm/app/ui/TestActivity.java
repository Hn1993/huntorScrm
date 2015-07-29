package com.huntor.mscrm.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.DealDetails;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.model.MessageRecordModel;
import com.huntor.mscrm.app.model.ProductCategories;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.*;
import com.huntor.mscrm.app.provider.api.ApiAllFansInfoDb;
import com.huntor.mscrm.app.provider.api.ApiFansRecordDb;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.utils.*;
import com.loopj.android.http.AsyncHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caogq on 2015/5/5.
*/
public class TestActivity extends BaseActivity {
    private final String TAG = "TestActivity";

    private TestActivity context  = TestActivity.this;
    public Intent intent ;
    private EditText et_searchkey;
    private EditText et_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MessageRecordModel mrm = new MessageRecordModel();
        mrm.eid = 1;
        mrm.fid = 7818792;
        mrm.timestamp = new Date().getTime();
        mrm.content = "乱七八糟";
        mrm.msgId = 1000;

        NotificationUtils.getInstance(context).sendPushNotification(mrm);
        ApiFansRecordDb.getFansInfoById(this, 7818792);

        et_searchkey = (EditText) findViewById(R.id.et_searchkey);
        et_username = (EditText) findViewById(R.id.et_username);
    }

    public void test(View view){

    }

    public void login(View view){
        intent = new Intent(this,DetailedInformationActivity.class);
        startActivity(intent);
    }

    /***
     * 用户登录
     * @param
     * @return
     * @throws IOException
     */
    public void testApiLogin(View view) throws IOException {
        HttpRequestController.login(this, "mbtest", "helloWorld",
                new HttpResponseListener<ApiLogin.ApiLoginResponse>() {
                    @Override
                    public void onResult(ApiLogin.ApiLoginResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.loginResult = " + response.loginResult);

                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 手工刷新业务层缓存
     * @param
     * @return
     * @throws IOException
     */
    public void testApiFlushCache(View view) throws IOException {
        HttpRequestController.flushCache(this, "all",
                new HttpResponseListener<ApiFlushCache.ApiFlushCacheResponse>() {
                    @Override
                    public void onResult(ApiFlushCache.ApiFlushCacheResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.flushResponse = " + response.flushResponse);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 获取产品列表
     * @param
     * @return
     * @throws IOException
     */
    public void testApiGetProducts(View view) throws IOException {
        HttpRequestController.getProducts(this,
                new HttpResponseListener<ApiProducts.ApiProductsResponse>() {
                    @Override
                    public void onResult(ApiProducts.ApiProductsResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.categories = " + response.categories);

                            for (int i = 0; i < response.categories.size(); i++) {
                                ProductCategories p = response.categories.get(i);
                                if (p.products != null) {
                                    for (int j = 0; j < p.products.size(); j++) {
                                        Log.e(TAG, p.products.get(j).toString());
                                    }
                                }
                            }
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }


    /***
     * 查询导购与粉丝的会话列表
     * @param
     * @return
     * @throws IOException
     */
    public void testApiGetConversations(View view) throws IOException {
        HttpRequestController.getConversations(this, 1, 1, 1, 1, 1,
                new HttpResponseListener<ApiConversations.ApiConversationsResponse>() {
                    @Override
                    public void onResult(ApiConversations.ApiConversationsResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.conversations = " + response.conversation);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 生成员工卖出产品的注册二维码
     * @param
     * @return
     * @throws IOException
     */
    public void testApiQrCreate(View view) throws IOException {
        HttpRequestController.createQr(this, 1, 26, "ma",
                new HttpResponseListener<ApiQrCreate.ApiQrCreateResponse>() {
                    @Override
                    public void onResult(ApiQrCreate.ApiQrCreateResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.qrcode = " + response.qrcode);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 员工基础信息查询
     * @param
     * @return
     * @throws IOException
     */
    public void testApiEmployee(View view) throws IOException {
        HttpRequestController.queryEmployeeInfo(this, 556,
                new HttpResponseListener<ApiEmployee.ApiEmployeeResponse>() {
                    @Override
                    public void onResult(ApiEmployee.ApiEmployeeResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.qrcode = " + response.employeeInfo);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 创建一条交易
     * @param
     * @return
     * @throws IOException
     */
    public void testApiCreateDeal(View view) throws IOException {
        List<DealDetails> list = new ArrayList<DealDetails>();
        for(int i = 0;i<10;i++){
            DealDetails d = new DealDetails();
            d.productId = 1+i;
            d.amount = 11+i;
            d.sn = 111+i;
            list.add(d);
        }

        HttpRequestController.createDeal(this, 555, 48, list,
                new HttpResponseListener<ApiFansDealCreate.ApiFansDealCreateResponse>() {
                    @Override
                    public void onResult(ApiFansDealCreate.ApiFansDealCreateResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.dealResult = " + response.dealResult);

                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 各固定分组中的人数
     * @param
     * @return
     * @throws IOException
     */
    public void testApiFansGroupCount(View view) throws IOException {
        HttpRequestController.fansGroupCount(this, 556,
                new HttpResponseListener<ApiFansGroupCount.ApiFansGroupCountResponse>() {
                    @Override
                    public void onResult(ApiFansGroupCount.ApiFansGroupCountResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.fansGroupCount = " + response.fansGroupCount);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 查询员工固定分组中的粉丝列表
     * @param
     * @return
     * @throws IOException
     */
    public void testApiFansGroup(View view) throws IOException {
        HttpRequestController.fansGroup(this, 1, 1, 1, 1, 1, 1,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.fansGroupResult = " + response.fansGroupResult);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 添加购买意向
     * @param
     * @return
     * @throws IOException
     */
    public void testApiFansPurchaseIntent(View view) throws IOException {
        HttpRequestController.addFansPurchaseIntent(this, 77, 5988, "哇啦个擦擦", new Date().getTime(),
                new HttpResponseListener<ApiFansPurchaseIntents.ApiFansPurchaseIntentsResponse>() {
                    @Override
                    public void onResult(ApiFansPurchaseIntents.ApiFansPurchaseIntentsResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.fansGroupResult = " + response.purchaseIntent);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 删除粉丝购买意向
     * @param
     * @return
     * @throws IOException
     */
    public void testApiDeletePurchaseIntent(View view) throws IOException {
        HttpRequestController.deletePurchaseIntent(this, 49626, 11,
                new HttpResponseListener<ApiDeletePIntent.ApiDeletePIntentResponse>() {
                    @Override
                    public void onResult(ApiDeletePIntent.ApiDeletePIntentResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 添加粉丝到自定义分组
     * @param
     * @return
     * @throws IOException
     */
    public void testApiAddFansTargetList(View view) throws IOException {
        HttpRequestController.addFansTargetList(this, 1, new int[]{1, 2},
                new HttpResponseListener<ApiAddFansTargetlist.ApiAddFansTargetlistResponse>() {
                    @Override
                    public void onResult(ApiAddFansTargetlist.ApiAddFansTargetlistResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 创建一个自定义粉丝分组
     * @param
     * @return
     * @throws IOException
     */
    public void testApiCreateFansTargetList(View view) throws IOException {
        HttpRequestController.createFansTargetList(this, 1, "打手机大手机", "cgq",
                new HttpResponseListener<ApiCreateTargetlist.ApiCreateTargetlistResponse>() {
                    @Override
                    public void onResult(ApiCreateTargetlist.ApiCreateTargetlistResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.targetList = " + response.targetList);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 删除一个自定义粉丝分组
     * @param
     * @return
     * @throws IOException
     */
    public void testApiDeleteFansTargetList(View view) throws IOException {
        HttpRequestController.deleteFansTargetList(this, 1,
                new HttpResponseListener<ApiDeleteTargetList.ApiDeleteTargetListResponse>() {
                    @Override
                    public void onResult(ApiDeleteTargetList.ApiDeleteTargetListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 查询员工的一个自定义粉丝分组内的粉丝列表
     * @param
     * @return
     * @throws IOException
     */
    public void testApiGetFansTargetList(View view) throws IOException {
        HttpRequestController.getFansTargetList(this, 1, 1, 1, 1, 1,
                new HttpResponseListener<ApiGetTargetList.ApiGetTargetListResponse>() {
                    @Override
                    public void onResult(ApiGetTargetList.ApiGetTargetListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 从员工的自定义粉丝分组中移除一个粉丝
     *
     * @param
     * @return
     * @throws IOException
     */
    public void testApiFansTargetListRemove(View view) throws IOException {
        HttpRequestController.getFansTargetListRemove(this, 1, new int[]{123},
                new HttpResponseListener<ApiFansTargetListRemove.ApiFansTargetListRemoveResponse>() {
                    @Override
                    public void onResult(ApiFansTargetListRemove.ApiFansTargetListRemoveResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }
    /***
     * 员工自定义分组 编辑
     * @param
     * @return
     * @throws IOException
     */
    public void testApiFansTargetListUpdate(View view) throws IOException {
        HttpRequestController.getFansTargetListUpdate(this, 1, "1", "cgq",
                new HttpResponseListener<ApiFansTargetListUpdate.ApiFansTargetListUpdateResponse>() {
                    @Override
                    public void onResult(ApiFansTargetListUpdate.ApiFansTargetListUpdateResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }


    /***
     * 查询员工的自定义分组列表
     * @param
     * @return
     * @throws IOException
     */
    public void testApiGetTargetLists(View view) throws IOException {
        HttpRequestController.getTargetLists(this, 1,
                new HttpResponseListener<ApiFansTargetLists.ApiFansTargetListsResponse>() {
                    @Override
                    public void onResult(ApiFansTargetLists.ApiFansTargetListsResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.targetLists = " + response.targetLists);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /***
     * 获取粉丝的详细信息
     * @param
     * @return
     * @throws IOException
     */
    public void testApiGetFans(View view) throws IOException {
        HttpRequestController.getFansInfo(this, 77,
                new HttpResponseListener<ApiFans.ApiFansResponse>() {
                    @Override
                    public void onResult(ApiFans.ApiFansResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.fanInfo = " + response.fanInfo);
//                            Intent intent=new Intent(TestActivity.this,DetailedInformationActivity.class);
//                            startActivity(intent);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 关闭导购 与粉丝的实时交互
     * @param view
     * @throws IOException
     */
    public void testApiRealTimeClose(View view) throws IOException {
        HttpRequestController.realTimeClose(this, 1, 1,
                new HttpResponseListener<ApiRealTimeClose.ApiRealTimeCloseResponse>() {
                    @Override
                    public void onResult(ApiRealTimeClose.ApiRealTimeCloseResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 查询导购与粉丝交互的历史消息
     * @param view
     * @throws IOException
     */
    public void testApiRealTimeHistory(View view) throws IOException {
        HttpRequestController.realTimeHistory(this, 7818823, 2964229, 20, 1, 1,
                new HttpResponseListener<ApiRealTimeHistory.ApiRealTimeHistoryResponse>() {
                    @Override
                    public void onResult(ApiRealTimeHistory.ApiRealTimeHistoryResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response.messageHistory);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 查询某知识库分类下的具体条目
     * @param view
     * @throws IOException
     */
    public void testApiKbContents(View view) throws IOException {
        HttpRequestController.kbContents(this, 1,
                new HttpResponseListener<ApiKbCategorieContent.ApiKbCategorieContentResponse>() {
                    @Override
                    public void onResult(ApiKbCategorieContent.ApiKbCategorieContentResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.kbEntries = " + response.kbEntries);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 查询MSCRM知识库分类
     * @param view
     * @throws IOException
     */
    public void testApiKbCategories(View view) throws IOException {
        HttpRequestController.kbCategories(this,
                new HttpResponseListener<ApiKbGategories.ApiKbGategoriesResponse>() {
                    @Override
                    public void onResult(ApiKbGategories.ApiKbGategoriesResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.categories = " + response.categories);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 开启导购与粉丝的实时交互
     * @param view
     * @throws IOException
     */
    public void testApiRealtimeOpen(View view) throws IOException {
        HttpRequestController.realtimeOpen(this, 1, 1,
                new HttpResponseListener<ApiRealtimeOpen.ApiRealtimeOpenResponse>() {
                    @Override
                    public void onResult(ApiRealtimeOpen.ApiRealtimeOpenResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.response = " + response.response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 发送消息
     * @param view
     * @throws IOException
     */
    public void testApiRealtimeSend(View view) throws IOException {
        HttpRequestController.realtimeSend(this, 1, 1, 1, 1, "",
                new HttpResponseListener<ApiRealtimeSend.ApiRealtimeSendResponse>() {
                    @Override
                    public void onResult(ApiRealtimeSend.ApiRealtimeSendResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.response = " + response.response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 修改密码
     * @param view
     * @throws IOException
     */
    public void testApiModifyPwd(View view) throws IOException {
        HttpRequestController.modifyPwd(this, "a", "b", "c",
                new HttpResponseListener<ApiModifyPwd.ApiModifyPwdResponse>() {
                    @Override
                    public void onResult(ApiModifyPwd.ApiModifyPwdResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.response = " + response.response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    public void testApiChat(View view) {
        startActivity(new Intent(this, ChatActivity.class));
    }

    public void testLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }


    /**
     * 更改粉丝状态：新增改为非新增
     * @param view
     * @throws IOException
     */
    public void fansStatusNewTwoOld(View view) throws IOException {
        HttpRequestController.fansStatusNewTwoOld(this, 1,
                new HttpResponseListener<ApiNewTwoOld.ApiNewTwoOldResponse>() {
                    @Override
                    public void onResult(ApiNewTwoOld.ApiNewTwoOldResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.response = " + response.response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    /**
     * 批量查询一批粉丝的信息
     * @param view
     * @throws IOException
     */
    public void testApiFanslist(View view) throws IOException {
        HttpRequestController.fansList(this, new int[]{1, 22939393},
                new HttpResponseListener<ApiFansList.ApiFansListResponse>() {
                    @Override
                    public void onResult(ApiFansList.ApiFansListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response = " + response);
                        }
                        Utils.toast(context, response.getRetInfo() + "");
                    }
                });
    }

    public void testApiUpload() throws IOException{
        HttpRequestController.upload(this, "", "", new HttpResponseListener<ApiUpload.ApiUploadResponse>() {
            @Override
            public void onResult(ApiUpload.ApiUploadResponse response) {
                if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                    Log.e(TAG, "response = " + response);
                }
                Utils.toast(context, response.getRetInfo() + "");
            }
        });
    }

    /**
     * 测试本地缓存所有粉丝
     * @param view
     */
    public void testAllFansInfo(View view){
        MyLogger.i(TAG,"testAllFansInfo");
        int empID = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
        HttpRequestController.fansGroup(context, empID, 5, 200, 1, 1, 1,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            List<Fans> fans = response.fansGroupResult.fans;
                            boolean nextPage = response.fansGroupResult.nextPage;
                            MyLogger.i(TAG, "testAllFansInfo fans: " + fans.toString());

                            //ApiAllFansInfoDb allFansInfoDb = new ApiAllFansInfoDb(context);
                            ApiAllFansInfoDb.bulkInsert(context, fans);
                        }
                        Utils.toast(context, "" + response.getRetInfo());

                    }
                });

    }

    /**
     * 测试搜索粉丝
     * @param view
     */
    public void testSearchFans(View view){
        String searchkey = et_searchkey.getText().toString();
        List<Fans> fansList = ApiAllFansInfoDb.getFansList(context, searchkey);
        MyLogger.i(TAG, "fansList: " + fansList.toString());
    }

    /**
     * 测试删除所有本地缓存粉丝
     * @param view
     */
    public void testDeleteAllFans(View view ){
        ApiAllFansInfoDb.delete(context);
        MyLogger.i(TAG,"ApiAllFansInfoDb.getFansList(context).size();"+ApiAllFansInfoDb.getFansList(context).size());
    }

    /**
     * 测试接受短信验证码
     * @param view
     */
    public void testReqCode(View view){
        MyLogger.i(TAG, "testReqCode");
        String username = et_username.getText().toString();
        HttpRequestController.reqVerifyCode(context, username, new HttpResponseListener<ApiReqVerifyCode.ApiReqVerifyCodeResponse>() {
            @Override
            public void onResult(ApiReqVerifyCode.ApiReqVerifyCodeResponse response) {
                MyLogger.i(TAG,response.toString());
                Utils.toast(context,response.toString());
            }
        });
    }





}


