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
import android.widget.Button;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.AdmFansAdapter;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiAddFansTargetlist;
import com.huntor.mscrm.app2.net.api.ApiFansGroup;
import com.huntor.mscrm.app2.ui.DetailedInformationActivity;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cary.xi on 2015/5/7.
 * 向自定义分组中添加粉丝
 */
public class AdmFansFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener,XListView.IXListViewListener {

    private final String TAG = "AdmFansFragment";
    private XListView mListView;
    private AdmFansAdapter mAdapter;
    private int mTargetListId;
    private View mRoot;

    /***
     * 每页加载数据个数
     */
    private final int SINGLE_PAGE_LOAD_COUNT = 20;
    /**
     * 下拉加载数据的页码 下标
     */
    private int mIndex = 1;

    private Button mConfirmBtn;
    //分组内的粉丝要和当前所有粉丝进行比较
    private List<Fans> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_adm_fans, container, false);

        Bundle bundle = getArguments();
        mTargetListId = bundle.getInt("targetListId");

        initView();
        initData();
        return mRoot;
    }

    private void initView(){
        mConfirmBtn = (Button) mRoot.findViewById(R.id.sure_btn);
        mConfirmBtn.setOnClickListener(this);
        mRoot.findViewById(R.id.img_left_corner).setOnClickListener(this);
        mListView = (XListView) mRoot.findViewById(R.id.listview);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(false);
        mListView.setOnItemClickListener(this);
        mListView.setXListViewListener(this);

    }

    private void initData(){
        mAdapter = new AdmFansAdapter(getActivity(),new ArrayList<Fans>());
        mListView.setAdapter(mAdapter);
        getAllFans(mIndex);

    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()){
            case R.id.sure_btn:
                //确认添加
                List<Fans> list = new ArrayList<Fans>();
                for(Fans fans:mAdapter.getData()){
                    if(fans.isCheck){
                        list.add(fans);
                    }
                }
                int[] fansId = new int[list.size()];
                for(int i = 0;i<list.size();i++){
                    fansId[i] = list.get(i).id;
                }
                addFans(mTargetListId, fansId);

            break;
            case R.id.img_left_corner:
                manager.popBackStack();
                transaction.remove(this);
                transaction.commit();

            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), DetailedInformationActivity.class);
        startActivity(intent);
        onLoadMore();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        //下拉加载当前页的粉丝
        getAllFans(mIndex);
    }
    private boolean haveLoad = true;
    /**
     * 获取所有粉丝信息
     */
    private void getAllFans(int pageNum){
        HttpRequestController.fansGroup(getActivity(), PreferenceUtils.getInt(getActivity(),Constant.PREFERENCE_EMP_ID,0), 5, SINGLE_PAGE_LOAD_COUNT, pageNum, 1, 1,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i(TAG, "response.fansGroupResult = " + response.fansGroupResult);
                            if(haveLoad){
                                List<Fans> list = response.fansGroupResult.fans;
                                if (list != null) {
                                    for (int j = 0; j < mList.size(); j++) {
                                        Fans fans = mList.get(j);
                                        for (int i = 0; i < list.size(); i++) {
                                            if (fans.id == list.get(i).id) {
                                                list.remove(i);
                                                break;
                                            }
                                        }
                                    }
                                    mAdapter.addData(response.fansGroupResult.fans);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                            if (!response.fansGroupResult.nextPage) {
                                haveLoad = false;
                                mListView.stopLoadMore();
                                mListView.setPullLoadEnable(false);
                                return;
                            }
                            mIndex++;


                        } else {
                            Utils.toast(getActivity(), response.getRetInfo() + "");
                        }
                        mListView.stopLoadMore();
                    }
                });
    }

    /**
     * 向自定义分组中添加粉丝
     * @param targetListId
     * @param fanIds
     */
    private void addFans(int targetListId, int[] fanIds){
        if(getActivity()!= null){
            ((BaseActivity) getActivity()).showCustomDialog(R.string.loading);
        }
        HttpRequestController.addFansTargetList(getActivity(), targetListId, fanIds,
                new HttpResponseListener<ApiAddFansTargetlist.ApiAddFansTargetlistResponse>() {
                    @Override
                    public void onResult(ApiAddFansTargetlist.ApiAddFansTargetlistResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            Log.i("AdmFansFragment", "response = " + response);
                            mRc.onResult();
                        } else {
                            Utils.toast(getActivity(), response.getRetInfo() + "");
                        }
                        if (getActivity() != null) {
                            ((BaseActivity) getActivity()).dismissCustomDialog();
                        }
                        finish();
                    }

                });
    }

    private RefreshCallback mRc;
    public void setData(List<Fans> list,RefreshCallback rc){
        this.mList = list;
        this.mRc = rc;
    }

    /***
     * 关闭当前Fragment
     */
    public void finish(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        manager.popBackStack();
        transaction.remove(this);
        transaction.commit();
    }

    public interface RefreshCallback{
        void onResult();
    }
}
