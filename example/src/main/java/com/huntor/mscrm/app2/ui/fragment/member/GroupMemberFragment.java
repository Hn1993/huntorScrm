package com.huntor.mscrm.app2.ui.fragment.member;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.GroupMemberAdapter;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansTargetListRemove;
import com.huntor.mscrm.app2.net.api.ApiGetTargetList;
import com.huntor.mscrm.app2.ui.DetailedInformationActivity;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cary.xi on 2015/5/7.
 * 分组会员列表展示
 */
public class GroupMemberFragment extends BaseFragment implements View.OnClickListener, GroupMemberAdapter.RemoveFansCallBack,XListView.IXListViewListener,AdmFansFragment.RefreshCallback {

    private XListView mListView;
    private int mTargetListId;
    private boolean isBottom = false;
    private int pageNumber;
    private List<Fans> fans;
    private List<Fans> mFanList;
    private RefreshCallBack  mCallBack;
    private GroupMemberAdapter mAdapter;

    /***
     * 每页加载数据个数
     */
    private final int SINGLE_PAGE_LOAD_COUNT = 10;
    /**
     * 下拉加载数据的页码 下标
     */
    private int mIndex = 1;
    private boolean isLoad=true;
    private View ret;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ret = inflater.inflate(R.layout.fragment_group_member, container, false);
        TextView title = (TextView) ret.findViewById(R.id.title);
        mListView = (XListView) ret.findViewById(R.id.group_member_listview);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);

        fans = new LinkedList<Fans>();

        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(true);
        mFanList=new ArrayList<Fans>();

        Bundle bundle = getArguments();
        if(bundle != null){
            mTargetListId = bundle.getInt("targetListId");
            title.setText(bundle.getString("name"));
        }
        setListener();
        initData();
        return ret;
    }

    private void setListener() {
        ret.findViewById(R.id.img_left_corner).setOnClickListener(this);
        ret.findViewById(R.id.add_member_symbol_imag).setOnClickListener(this);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("debug1", "setOnItemClickListener");
                Intent intent = new Intent(getActivity(), DetailedInformationActivity.class);
                intent.putExtra(Constant.FANS_ID, mFanList.get(position - 1).id);
                startActivity(intent);
            }
        });
    }

    public void initData(){
        mAdapter = new GroupMemberAdapter(getActivity(),fans,GroupMemberFragment.this);
        mListView.setAdapter(mAdapter);
        getAllFans(mIndex,true);
    }


    private void getAddFans(int pageNum,boolean load) {
        Log.i("添加人数","pageNum"+pageNum);
        isLoad=load;
        Log.i("添加人数","isLoad"+isLoad);
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showCustomDialog(R.string.loading);
        }
        HttpRequestController.getFansTargetList(getActivity(), mTargetListId, SINGLE_PAGE_LOAD_COUNT, pageNum, ORDER_BY_DEFAULT, ORDER_FLAG,
                new HttpResponseListener<ApiGetTargetList.ApiGetTargetListResponse>() {
                    @Override
                    public void onResult(final ApiGetTargetList.ApiGetTargetListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i("添加人数", "2222");

                            mFanList.clear();
                            fans = response.fansQueryResult.fans;
                            mFanList.addAll(fans);

                            if (isLoad) {
                                //mFanList.clear();

                                if (mFanList != null) {
                                    Log.i("添加人数", "mFanList.size" + mFanList.size());
                                    mAdapter.setData(mFanList);
                                    mAdapter.notifyDataSetChanged();
                                }
                                //mListView.stopLoadMore();
                            }

                            if (!response.fansQueryResult.nextPage) {
                                //mFanList.clear();

                                Log.i("添加人数", "nextPage list.size" + mFanList.size());
                                isLoad = false;
                                mListView.stopLoadMore();
                                mListView.setPullLoadEnable(false);

                                ((BaseActivity) getActivity()).dismissCustomDialog();
                                return;
                            }
                            mIndex++;
                        } else {
                            Utils.toast(getActivity(), response.getRetInfo() + "");
                            ((BaseActivity) getActivity()).dismissCustomDialog();
                        }
                        Log.i("添加人数", "stopLoadMore");

//                        mAdapter.addData(mFanList);
//                        mAdapter.notifyDataSetChanged();
                        mListView.stopLoadMore();
                        ((BaseActivity) getActivity()).dismissCustomDialog();
                    }
                });
    }


    private void getAllFans(int pageNum,boolean load) {
        Log.i("添加人数","pageNum"+pageNum);
        isLoad=load;
        Log.i("添加人数","isLoad"+isLoad);
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showCustomDialog(R.string.loading);
        }
        HttpRequestController.getFansTargetList(getActivity(), mTargetListId, SINGLE_PAGE_LOAD_COUNT, pageNum, ORDER_BY_DEFAULT, ORDER_FLAG,
                new HttpResponseListener<ApiGetTargetList.ApiGetTargetListResponse>() {
                    @Override
                    public void onResult(final ApiGetTargetList.ApiGetTargetListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

                            Log.i("添加人数", "2222");
                            mFanList.clear();
                            fans = response.fansQueryResult.fans;
                            mFanList.addAll(fans);


                            if (isLoad) {
                                //mFanList.clear();

                                if (mFanList != null) {
                                    Log.i("添加人数","mFanList.size"+mFanList.size());
                                    mAdapter.addData(mFanList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                            if (!response.fansQueryResult.nextPage) {

                                isLoad = false;
                                mListView.stopLoadMore();
                                mListView.setPullLoadEnable(false);

                                mFanList.clear();
                                fans = response.fansQueryResult.fans;
                                mFanList.addAll(fans);
                                //mFanList.clear();

                                Log.i("添加人数", "nextPage list.size" + mFanList.size());
                                isLoad = false;
                                mListView.stopLoadMore();
                                mListView.setPullLoadEnable(false);

                                ((BaseActivity) getActivity()).dismissCustomDialog();
                                return;
                            }
                            mIndex++;
                        } else {
                            Utils.toast(getActivity(), response.getRetInfo() + "");
                            ((BaseActivity) getActivity()).dismissCustomDialog();
                        }
                        Log.i("添加人数", "stopLoadMore");

//                        mAdapter.addData(mFanList);
//                        mAdapter.notifyDataSetChanged();
                        mListView.stopLoadMore();
                        ((BaseActivity) getActivity()).dismissCustomDialog();
                    }
                });

    }


    public void setCallBack(RefreshCallBack callBack){
        this.mCallBack = callBack;
    }

    /**
     * 删除自定义分组中的一个粉丝
     * @param targetListId
     * @param fansId
     */
    private void removeFansTask(int targetListId, int fansId){
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showCustomDialog(R.string.loading);
        }
        HttpRequestController.getFansTargetListRemove(getActivity(), targetListId, new int[]{1,2},
                new HttpResponseListener<ApiFansTargetListRemove.ApiFansTargetListRemoveResponse>() {
                    @Override
                    public void onResult(ApiFansTargetListRemove.ApiFansTargetListRemoveResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            fans.clear();
                            mPageNum = 1;

                            getAllFans(mIndex,false);

                            //getListInfo(mTargetListId);//刷新列表

                        } else {
                            Utils.toast(getActivity(), response.getRetInfo() + "");
                            ((BaseActivity) getActivity()).dismissCustomDialog();
                        }
                        BaseActivity activity = (BaseActivity) getActivity();
                        if (activity != null) {
                            activity.dismissCustomDialog();
                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.img_left_corner://左上角返回
                manager.popBackStack();
                transaction.remove(this);
                transaction.commit();

                break;
            case R.id.add_member_symbol_imag://添加粉丝
                transaction.addToBackStack(null);
                Bundle bundle = new Bundle();
                bundle.putInt("targetListId", mTargetListId);
                AdmFansFragment admFansFragment = new AdmFansFragment();
                admFansFragment.setArguments(bundle);
                transaction.add(R.id.frame_main, admFansFragment, Constant.ADM_FANS);
                transaction.commit();

                admFansFragment.setData(fans,new AdmFansFragment.RefreshCallback(){
                    @Override
                    public void onResult() {
                        Log.i("添加人数",""+111111);
                        fans.clear();
                        mPageNum = 1;
                        mIndex=1;
                        getAddFans(mIndex,true);
                    }
                });

                break;
        }
    }

    @Override
    public void removeFans(int fansId) {

        Log.i("添加人数","删除的回调接口");

        removeFansTask(mTargetListId, fansId);
    }


    @Override
    public void onRefresh() {
        Log.i("添加人数","onRefresh");
        getAllFans(mIndex,false);
    }

    @Override
    public void onLoadMore() {
        Log.i("添加人数","下拉刷新GROUPMEMBER");
        getAllFans(mIndex,true);
    }

    @Override
    public void onResult() {
        Log.i("添加人数","接口回调onResult");
        getAllFans(mIndex,false);
    }

    public interface RefreshCallBack{
        public void refresh();
    }
}
