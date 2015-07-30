package com.huntor.mscrm.app.ui.fragment.member;


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
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.adapter.NewFansAdapter;
import com.huntor.mscrm.app.adapter.NormalFansAdapter;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiFansGroup;
import com.huntor.mscrm.app.provider.api.ApiFixedGroupFansListDb;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.component.XListView;
import com.huntor.mscrm.app.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app.ui.DetailedInformationActivity;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;
import com.huntor.mscrm.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 新增粉丝(新APP的在线交互粉丝)
 */
public class NewFansFragment extends BaseFragment implements View.OnClickListener,XListView.IXListViewListener {
    String TAG="NewFansFragment";
    private XListView mListView;
    private final int TYPE = 1;//新增粉丝类型

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


    private NewFansAdapter mAdapter;
    private BaseActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("onCreateView");

        context = (BaseActivity) getActivity();
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_new_fans, container, false);

        RelativeLayout imgLeftCorner = (RelativeLayout) ret.findViewById(R.id.img_left_corner);

        imgLeftCorner.setVisibility(View.VISIBLE);
        imgLeftCorner.setOnClickListener(this);
        mFanList=new ArrayList<Fans>();

        mListView = (XListView) ret.findViewById(R.id.new_fans_list);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);
        mListView.setXListViewListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailedInformationActivity.class);
                Fans item = (Fans) mAdapter.getItem(position - 1);
                if (item == null || intent == null) {
                    Utils.toast(context, "请刷新重试");
                } else {
                    intent.putExtra(Constant.FANS_ID, item.id);
                    //intent.putExtra(Constant.FANS_ID, mFanList.get(position - 1).id);
                    intent.putExtra("type", "new_fans");
                    startActivity(intent);
                }

            }
        });

        return ret;

    }

    @Override
    public void onStart() {
        super.onStart();
        isLoad=true;
        mIndex=1;
        initData();

    }

    public void initData(){
        mAdapter = new NewFansAdapter(context);
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
        final List<Fans> mLocalData=ApiFixedGroupFansListDb.getFixedGroupFansList(context, TYPE);
        getLocalData(mLocalData);
        HttpRequestController.fansGroup(context, PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0), TYPE, SINGLE_PAGE_LOAD_COUNT, pageNum, 1, 2,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            mListView.setPullLoadEnable(true);
                            if (isLoad) {
                                list = response.fansGroupResult.fans;
                                Log.i(TAG, "list.size" + list.size());
                                if (list != null) {
                                    //mFanList.clear(); 不能清空，清空出现数据重复
                                    mFanList.addAll(list);
                                    Log.i(TAG, "mFanList.size" + mFanList.size());
                                    mAdapter.addendData((ArrayList) list, isClear);
                                    mAdapter.notifyDataSetChanged();

                                }
                            }

                            if (!response.fansGroupResult.nextPage) {

                                isLoad = false;
                                mListView.stopLoadMore();
                                mListView.setPullLoadEnable(false);
                                return;

                            }
                            mIndex++;

                        } else if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR){
                            if(context!=null){
                                Utils.toast(context, response.getRetInfo() + "");
                            }
                            if (isLoad) {

                                //list = response.fansGroupResult.fans;

                                if (mLocalData != null) {
                                    mFanList.clear();
                                    mFanList.addAll(mLocalData);
                                    Log.i(TAG, "mFanList.size" + mFanList.size());
                                    mAdapter.addendData((ArrayList) mLocalData, true);
                                    mAdapter.notifyDataSetChanged();
                                    mListView.setPullLoadEnable(false);
                                }
                            }
                            if(context != null){
                                Utils.toast(context, response.getRetInfo() + "");
                            }
                        }
                        mListView.stopLoadMore();
                    }
                });
    }
    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int id = v.getId();
        switch (id) {
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
