package com.huntor.mscrm.app2.ui.fragment.member;


import android.app.Activity;
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
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.HighUserAdapter;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansGroup;
import com.huntor.mscrm.app2.provider.api.ApiFixedGroupFansListDb;
import com.huntor.mscrm.app2.ui.DetailedInformationActivity;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 高消费会员界面
 */
public class HighUserFragment extends BaseFragment implements View.OnClickListener, XListView.IXListViewListener {


    final String TAG="HighUserFragment";
    private XListView mListView;
    private final int TYPE = 3;
    private boolean isBottom = false;


    private HighUserAdapter mAdapter;
    /**
     * 每页加载数据个数
     */
    private final int SINGLE_PAGE_LOAD_COUNT = 10;
    /**
     * 下拉加载数据的页码 下标
     */
    private int mIndex = 1;

    boolean isLoad = true;
    List<Fans> list;

    private List<Fans> mFanList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.i(TAG, "onCreateView");

        View ret = inflater.inflate
                (R.layout.fragment_high_user, container, false);
        RelativeLayout imgLeftCorner = (RelativeLayout)
                ret.findViewById(R.id.img_left_corner);
        imgLeftCorner.setVisibility(View.VISIBLE);
        imgLeftCorner.setOnClickListener(this);
        mNoContentHint = (TextView)
                ret.findViewById(R.id.no_content_hint);
        mListView = (XListView) ret.findViewById
                (R.id.high_user_list);
        //Xlistview必须要设置的4个属性
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        //mListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        mListView.setXListViewListener(this);

        mFanList=new ArrayList<Fans>();
        //initData();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i("高消费者Item点击","list"+list.size());

                Intent intent = new Intent(getActivity(), DetailedInformationActivity.class);
//                if (position > list.size()) {
//                    return;
//                }
                Fans item = (Fans) mAdapter.getItem(position - 1);
                intent.putExtra(Constant.FANS_ID, item.id);
                //intent.putExtra(Constant.FANS_ID, mFanList.get(position - 1).id);
                Log.i(TAG, "position" + position);
                Log.i(TAG, "mFanList.get(position - 1).id"+mFanList.get(position - 1).id);
                //intent.putExtra("HighUserFragment", new DetailedInformationActivity.RefreshPeople);
                startActivity(intent);
            }


        });


        return ret;
    }

    public void initData() {
        mAdapter = new HighUserAdapter(getActivity
                ());
        mListView.setAdapter(mAdapter);
        getAllFans(mIndex,true);
    }

    private void getLocalData(List<Fans> mLocalData){
        mFanList.clear();
        mFanList.addAll(mLocalData);
        //每次获取都清空原数据
        mAdapter.addendData((ArrayList) mFanList, true);
        mAdapter.notifyDataSetChanged();
    }
    private void getAllFans(int pageNum, final boolean isClear) {
        final List<Fans> mLocalData= ApiFixedGroupFansListDb.getFixedGroupFansList(getActivity(), TYPE);
        getLocalData(mLocalData);
//        final BaseActivity activity = (BaseActivity) getActivity();
//        if (activity != null) {
//            activity.showCustomDialog(R.string.loading);
//        }
        HttpRequestController.fansGroup(getActivity
                        (), PreferenceUtils.getInt(getActivity(), Constant.PREFERENCE_EMP_ID, 0),
                3, SINGLE_PAGE_LOAD_COUNT, pageNum, 1, 2,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult
                            (ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() ==
                                BaseResponse.RET_HTTP_STATUS_OK) {
                            MyLogger.i(TAG, "fans: " + response.fansGroupResult.fans.toString());
                            mListView.setPullLoadEnable(true);
                            Log.i(TAG,"mFanList.size:"+mFanList.size());
                            if (isLoad) {
                                list = response.fansGroupResult.fans;

                                if (list != null) {
                                    //mFanList.clear();不能清空   清空就会出现报错
                                    mFanList.addAll(list);
                                    Log.i(TAG, "list != null" + mFanList.size());
                                    mAdapter.addendData((ArrayList) list, isClear);
                                    mAdapter.notifyDataSetChanged();

                                    //mListView.
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
                        }
                        else if(response.getRetCode() ==
                                BaseResponse.RET_HTTP_STATUS_ERROR){
                            if(getActivity()!=null){
                                Utils.toast(getActivity(), response.getRetInfo() + "");
                            }
                            if (isLoad) {
                                //list = response.fansGroupResult.fans;

                                if (mLocalData != null) {
                                    mFanList.clear();//在网络状况不好的情况下  可能会出现重复数据的问题   清空一下
                                    mFanList.addAll(mLocalData);
                                    //Log.i(TAG, "list != null" + mFanList.size());
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

                        //mListView.setPullLoadEnable(false);
                    }
                });
    }



    @Override
    public void onClick(View v) {
        FragmentManager manager =
                getFragmentManager();
        FragmentTransaction transaction =
                manager.beginTransaction();
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
        Log.i("下拉刷新", "下拉刷新HIGH");
        //下拉加载当前页的粉丝
        getAllFans(mIndex,false);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG,"onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    //从Activity返回Fragment的时候  再次执行此方法
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        //getFlushFans(mIndex);
        isLoad=true;
        mIndex=1;
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach");
    }
}
