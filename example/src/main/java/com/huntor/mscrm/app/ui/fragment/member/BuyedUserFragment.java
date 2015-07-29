package com.huntor.mscrm.app.ui.fragment.member;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.adapter.BuyedUserXdkAdapter;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiFansGroup;
import com.huntor.mscrm.app.provider.api.ApiFixedGroupFansListDb;
import com.huntor.mscrm.app.ui.DetailedInformationActivity;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.component.XListView;
import com.huntor.mscrm.app.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;
import com.huntor.mscrm.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 已购用户
 */
public class BuyedUserFragment extends BaseFragment implements View.OnClickListener,XListView.IXListViewListener{


    String TAG="BuyedUserFragment";
    private XListView mListview;
    private final int TYPE = 4;
    private boolean isBottom = false;


    private BuyedUserXdkAdapter mAdapter;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_buyed_user, container, false);
        RelativeLayout imgLeftCorner = (RelativeLayout) ret.findViewById(R.id.img_left_corner);
        imgLeftCorner.setVisibility(View.VISIBLE);
        imgLeftCorner.setOnClickListener(this);
        mListview = (XListView) ret.findViewById(R.id.buyed_user_list);
        mListview.setPullRefreshEnable(false);
        mListview.setPullLoadEnable(false);
        mListview.setXListViewListener(this);
        //mListview.setOnClickListener(this);
        mFanList=new ArrayList<Fans>();

        //initData();
        //mListview.setOnItemClickListener(this);




        return ret;
    }
    //从Activity返回Fragment的时候  再次执行此方法
    @Override
    public void onStart() {

        super.onStart();
        Log.i(TAG, "onStart");
        isLoad=true;
        mIndex=1;
        initData();
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("onItemClick", "position=" + position);
                Intent intent = new Intent(getActivity(), DetailedInformationActivity.class);
//                if (position > list.size()) {
//                    Log.i("onItemClick","list.size()="+list.size());
//                    return;
//                }
                Fans item = (Fans) mAdapter.getItem(position - 1);
                intent.putExtra(Constant.FANS_ID, item.id);
                //intent.putExtra(Constant.FANS_ID, mFanList.get(position - 1).id);
                startActivity(intent);
            }
        });
    }



    public void initData(){
        mAdapter = new BuyedUserXdkAdapter(getActivity());
        mListview.setAdapter(mAdapter);

        getAllFans(mIndex,true);
    }
    private void getLocalData(List<Fans> mLocalData){
        mFanList.clear();
        mFanList.addAll(mLocalData);
        mAdapter.addendData((ArrayList) mFanList, true);
        mAdapter.notifyDataSetChanged();
    }
    private void getAllFans(int pageNum,final boolean isClear) {
        Log.i(TAG,"getAllFans");

        final List<Fans> mLocalData= ApiFixedGroupFansListDb.getFixedGroupFansList(getActivity(), TYPE);
        getLocalData(mLocalData);
//        BaseActivity activity = (BaseActivity) getActivity();
//        if (activity != null) {
//            activity.showCustomDialog(R.string.loading);
//        }
        HttpRequestController.fansGroup(getActivity(), PreferenceUtils.getInt(getActivity(),Constant.PREFERENCE_EMP_ID,0), TYPE, SINGLE_PAGE_LOAD_COUNT, pageNum, 1, 2,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            mListview.setPullLoadEnable(true);
                            if(isLoad){

                                list = response.fansGroupResult.fans;

                                if (list != null) {
                                    //mFanList.clear(); 不能清空   清空就会出现报错
                                    mFanList.addAll(list);
                                    Log.i(TAG, "mFanList.size"+mFanList.size());
                                    mAdapter.addendData((ArrayList) list, isClear);
                                    mAdapter.notifyDataSetChanged();

                                }
                            }

                            if (!response.fansGroupResult.nextPage) {

                                isLoad=false;
                                mListview.stopLoadMore();
                                mListview.setPullLoadEnable(false);
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
                            if(isLoad){

                                //list = response.fansGroupResult.fans;

                                if (mLocalData != null) {
                                    mFanList.clear();
                                    mFanList.addAll(mLocalData);
                                    Log.i(TAG, "mFanList.size"+mFanList.size());
                                    mAdapter.addendData((ArrayList) mLocalData, true);
                                    mAdapter.notifyDataSetChanged();
                                    mListview.setPullLoadEnable(false);
                                }
                            }

                        }
//                        BaseActivity activity = (BaseActivity) getActivity();
//                        if (activity != null) {
//                            activity.dismissCustomDialog();
//                        }
                        mListview.stopLoadMore();
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
        Log.i("下拉刷新","下拉刷新BUYED");
        //下拉加载当前页的粉丝
        getAllFans(mIndex,false);
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
