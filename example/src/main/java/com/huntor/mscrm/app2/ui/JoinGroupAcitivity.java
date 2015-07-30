package com.huntor.mscrm.app2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.MyJoinGroupAdapter;
import com.huntor.mscrm.app2.model.FanInfo;
import com.huntor.mscrm.app2.model.Target;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiAddFansTargetlist;
import com.huntor.mscrm.app2.net.api.ApiFans;
import com.huntor.mscrm.app2.net.api.ApiFansTargetLists;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/5/9.
 */
public class JoinGroupAcitivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView group_listview;
    private MyJoinGroupAdapter adapter;

    private List<Target> mListsInfo;//自定义分组会员的列表信息
    private int[] fans_id = new int[1];

    //通过粉丝详情获取的粉丝所在分组ID 和 name
    List<FanInfo.TargetList> mTargetList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joingroup);

        setTitle(Constant.ADD_TO_MY_GROUP);

        findViewById(R.id.img_left_corner).setOnClickListener(this);

        initView();
        initData();

        Log.i("加入分组", "粉丝IDonCreate" + fans_id[0]);
        HttpRes(fans_id[0]);
        //group_listview.setOnItemClickListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("加入分组", "粉丝IDonReStart" + fans_id[0]);
        HttpRes(fans_id[0]);
    }

    private void initData() {//初始化数据
        mListsInfo = new ArrayList<Target>();
        Intent intent = getIntent();
        if (intent != null) {
            int fan_id = intent.getIntExtra(Constant.FANS_ID, 0);//获取传递的粉丝ID
            fans_id[0] = fan_id;

        }

    }

    private void initView() {//初始化界面
        group_listview = (ListView) findViewById(R.id.group_listview);
        group_listview.setOnItemClickListener(this);
    }

    //获取分组信息
    private void getTargetlist() {
        showCustomDialog(R.string.loading);
        HttpRequestController.getTargetLists(this, PreferenceUtils.getInt(this, Constant.PREFERENCE_EMP_ID, 0),
                new HttpResponseListener<ApiFansTargetLists.ApiFansTargetListsResponse>() {
                    @Override
                    public void onResult(ApiFansTargetLists.ApiFansTargetListsResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

                            List<Target> AllTargetList = response.targetLists;

                            if (mTargetList != null) {
                                for (int i = 0; i < mTargetList.size(); i++) {
                                    int id = mTargetList.get(i).id;
                                    Log.i("加入分组", "获取分组信息id" + id);
                                    for (int j = 0; j < AllTargetList.size(); j++) {
                                        if (AllTargetList.get(j).id == id) {
                                            AllTargetList.remove(j);
                                        }
                                    }
                                }
                                mListsInfo.addAll(AllTargetList);
                                dismissCustomDialog();
                            } else {
                                Log.i("加入分组", "获取分组信息idnull");
                                mListsInfo.addAll(AllTargetList);
                            }


                        } else {
                            Utils.toast(JoinGroupAcitivity.this, response.getRetInfo() + "");
                        }
                        dismissCustomDialog();
                        adapter = new MyJoinGroupAdapter(JoinGroupAcitivity.this);
                        adapter.addendDataTop((ArrayList) mListsInfo, false);
                        group_listview.setAdapter(adapter);
                    }
                });
    }


    //通过接口获取粉丝信息
    public void HttpRes(final int fans_id) {
        Log.i("加入分组", "fans_id" + fans_id);
        showCustomDialog(R.string.loading);
        HttpRequestController.getFansInfo(this, fans_id,
                new HttpResponseListener<ApiFans.ApiFansResponse>() {
                    @Override
                    public void onResult(ApiFans.ApiFansResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i("DetailedInformationA", "response.fanInfo = " + response.fanInfo);
                            //mTargetList.clear();
                            mTargetList = new ArrayList<FanInfo.TargetList>();
                            mTargetList = response.fanInfo.targetLists;
                            if (mTargetList == null) {
                                Log.i("加入分组", "response.fanInfo.targetLists is null" + response.fanInfo.targetLists);
                            } else {
                                Log.i("加入分组", "response.fanInfo.targetLists is not null" + response.fanInfo.targetLists.size());
                                for (int i = 0; i < mTargetList.size(); i++) {
                                    Log.i("加入分组", "mTargetList.name" + mTargetList.get(i).name);
                                }
                            }
                            //Log.i("黄安","mTargetList"+mTargetList.size());
                        } else {
                            Utils.toast(JoinGroupAcitivity.this, response.getRetInfo() + "");
                            dismissCustomDialog();
                            finish();
                            return;
                        }
                        getTargetlist();
                        dismissCustomDialog();
                    }
                });
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Target target = mListsInfo.get(position);
        int targetId = target.id;
        addFansToTarget(targetId);
    }

    private void addFansToTarget(int targetId) {
        showCustomDialog(R.string.loading);
        HttpRequestController.addFansTargetList(this, targetId, fans_id,
                new HttpResponseListener<ApiAddFansTargetlist.ApiAddFansTargetlistResponse>() {
                    @Override
                    public void onResult(ApiAddFansTargetlist.ApiAddFansTargetlistResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i("JoinGroupActivity", "response = " + response);
                            finish();
                        } else {
                            Utils.toast(JoinGroupAcitivity.this, response.getRetInfo() + "");
                        }
                        dismissCustomDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_left_corner:
                finish();
                break;
        }
    }
}
