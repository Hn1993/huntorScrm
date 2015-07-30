package com.huntor.mscrm.app2.ui.fragment.member;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.BuyedUserXdkAdapter;
import com.huntor.mscrm.app2.adapter.MemberAdapter;
import com.huntor.mscrm.app2.adapter.NormalFansAdapter;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.model.FansGroupResult;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansGroup;
import com.huntor.mscrm.app2.provider.api.ApiFixedGroupFansListDb;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.ui.DetailedInformationActivity;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NormalFansFragment extends BaseFragment implements View.OnClickListener,XListView.IXListViewListener {

    String TAG="NormalFansFragment";

    private XListView mListView;
    private final int TYPE = 2;

    /***
     * 每页加载数据个数
     */
    private final int SINGLE_PAGE_LOAD_COUNT = 10;
    /**
     * 下拉加载数据的页码 下标
     */
    private int mIndex = 1;

    private boolean isLoad=true;
    //列表
    private List<Fans> list;

    private List<Fans> mFanList;


    private NormalFansAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_normal_fans, container, false);
        RelativeLayout imgLeftCorner = (RelativeLayout) ret.findViewById(R.id.img_left_corner);
        imgLeftCorner.setVisibility(View.VISIBLE);
        imgLeftCorner.setOnClickListener(this);

        mFanList=new ArrayList<Fans>();
        mListView = (XListView) ret.findViewById(R.id.normal_fans_list);
        //Xlistview的四个方法
        mListView.setPullRefreshEnable(false);//不显示上拉刷新
        mListView.setPullLoadEnable(false);//不显示下拉加载更多
        mListView.setXListViewListener(this);//onloadmore   onrefresh


        return ret;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        isLoad=true;
        mIndex=1;
        initData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//item点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailedInformationActivity.class);
                Fans item = (Fans) mAdapter.getItem(position - 1);
                intent.putExtra(Constant.FANS_ID, item.id);
                //intent.putExtra(Constant.FANS_ID, mFanList.get(position - 1).id);
                startActivity(intent);
            }
        });
    }
    public void initData(){
        mAdapter = new NormalFansAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        getAllFans(mIndex,true);
    }

    private void getLocalData(List<Fans> mLocalData){
        mFanList.clear();
        mFanList.addAll(mLocalData);
        mAdapter.addendData((ArrayList) mFanList, true);
        mAdapter.notifyDataSetChanged();
    }
    private void getAllFans(int pageNum,final boolean isClear) {
        Log.i(TAG, "getAllFans");
        final List<Fans> mLocalData= ApiFixedGroupFansListDb.getFixedGroupFansList(getActivity(), TYPE);
        getLocalData(mLocalData);

//        BaseActivity activity = (BaseActivity) getActivity();
//        if (activity != null) {
//            activity.showCustomDialog(R.string.loading);
//        }

        HttpRequestController.fansGroup(getActivity(), PreferenceUtils.getInt(getActivity(), Constant.PREFERENCE_EMP_ID, 0), TYPE, SINGLE_PAGE_LOAD_COUNT, pageNum, 1, 2,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            mListView.setPullLoadEnable(true);//显示加载更多

                            if (isLoad) {
                                //mFanList.clear();
                                list = response.fansGroupResult.fans;
                                Log.i(TAG, "list.size"+list.size());
                                if (list != null) {
                                    mFanList.clear();
                                    Log.i(TAG, "mFanList.size" + mFanList.size());
                                    mFanList.addAll(list);

                                    Log.i(TAG,"isClear:"+isClear);
                                    mAdapter.addendData((ArrayList) mFanList, isClear);
                                    mAdapter.notifyDataSetChanged();

                                }
                            }

                            if (!response.fansGroupResult.nextPage) {

                                isLoad = false;
                                mListView.stopLoadMore();
                                mListView.setPullLoadEnable(false);
//                                BaseActivity activity = (BaseActivity) getActivity();
//                                if (activity != null) {
//                                    activity.dismissCustomDialog();
//                                }
                                return;

                            }
                            mIndex++;

                        } else if(response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR){

                            if(getActivity()!=null){
                                Utils.toast(getActivity(), response.getRetInfo() + "");
                            }
                            mIndex=1;
                            if (isLoad) {

                                //list = response.fansGroupResult.fans;
                                //Log.i(TAG, "list.size"+list.size());
                                if (mLocalData != null) {
                                    mFanList.clear();
                                    mFanList.addAll(mLocalData);
                                    Log.i(TAG, "mFanList.size" + mFanList.size());
                                    mAdapter.addendData((ArrayList) mLocalData, true);
                                    mAdapter.notifyDataSetChanged();
                                    mListView.setPullLoadEnable(false);
                                }
                            }

                        }
//                        BaseActivity activity = (BaseActivity) getActivity();
//                        if (activity != null) {
//                            activity.dismissCustomDialog();
//                        }
                        mListView.stopLoadMore();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int id = v.getId();
        switch (id){
            case R.id.img_left_corner:
                manager.popBackStack();
                transaction.remove(this);
                transaction.commit();
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        getAllFans(mIndex,false);
    }
}
