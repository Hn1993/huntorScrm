package com.huntor.mscrm.app2.ui.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.MemberAdapter;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.model.FansGroupResult;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansGroup;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cary.xi on 2015/5/6.
 */
public class BaseFragment extends Fragment implements XListView.IXListViewListener {

    protected static final int PAGE_SIZE_DEFAULT = 10;//页面大小
    protected static final int PAGE_NUM_DEFAULT = 1;//页码
    protected static final int ORDER_BY_DEFAULT = 1;//排序字段
    //1.顺序  2。逆序
    protected static final int ORDER_FLAG = 2;//排序标识
    protected List<Fans> mFans;
    protected TextView mNoContentHint;//无法加载数据提示
    protected boolean isLoading = false;//标记是否正在
    protected int mCurrentIndex = 0;//记录List当前item的位置
    public int mPageNum = 1;


    private XListView mListview;
    private int mType;
    boolean isLoad = true;

//    protected List<Target> mListsInfo;//自定义分组会员的列表信息

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {//再次初始化一些数据
        super.onCreate(savedInstanceState);
        mPageNum = 1;
        isLoading = false;
        mFans = new ArrayList<Fans>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        Log.i("NormalFansFragment", "BaseonStart");
//        mPageNum=1;
//        isLoad=true;
//        getMemberListData(mListview,mType,mPageNum);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        FragmentManager manager = getFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.remove(this);
//        manager.popBackStack();//每次返回，将fragment弹出stack


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    FansGroupResult mFansGroup;
    /**
     * 查询员工各个固定分组（新增、普通、高潜、已购）中的粉丝列表
     *
     * @param listview
     * @param type
     */
    protected void getMemberListData(final XListView listview, final int type,int mIndex,final boolean Load) {

        Log.i("NormalFansFragment", "getMemberListData" );
        mPageNum=mIndex;
        mListview=listview;
        mType=type;
        //isLoad=Load;
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showCustomDialog(R.string.loading);
        }
        HttpRequestController.fansGroup(getActivity(), PreferenceUtils.getInt(getActivity(),Constant.PREFERENCE_EMP_ID,0), type, PAGE_SIZE_DEFAULT, mPageNum, ORDER_BY_DEFAULT, ORDER_FLAG,
                new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                    @Override
                    public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {


                            if (Load) {
                                List<Fans> list=response.fansGroupResult.fans;
                                if (list != null && list.size() > 0) {
                                    mFans.clear();
                                    mFans.addAll(list);
                                    Log.i("NormalFansFragment", "mFans" + mFans.size());
                                    listview.setAdapter(new MemberAdapter(getActivity(), mFans, type));
                                    BaseActivity activity = (BaseActivity) getActivity();
                                    if (activity != null) {
                                        activity.dismissCustomDialog();
                                    }
                                }
                            }
                            if(!response.fansGroupResult.nextPage){
                                isLoad=false;
                                listview.stopLoadMore();
                                listview.setPullLoadEnable(false);

                                BaseActivity activity = (BaseActivity) getActivity();
                                if (activity != null) {
                                    activity.dismissCustomDialog();
                                }
                                return;

                            }
                            mPageNum++;
                        } else {
//                            /mNoContentHint.setVisibility(View.VISIBLE);//提示网络差
                        }
                        BaseActivity activity = (BaseActivity) getActivity();
                        if (activity != null) {
                            activity.dismissCustomDialog();
                        }
                    }
                });
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        getMemberListData(mListview,mType,mPageNum,isLoad);
    }
}
