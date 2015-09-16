package com.huntor.mscrm.app2.ui.fragment.member;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.GroupMemberAdapter2;
import com.huntor.mscrm.app2.adapter.GroupMemberAdapter2.onCheckItemListener;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansTargetListRemove;
import com.huntor.mscrm.app2.net.api.ApiGetAllFansId;
import com.huntor.mscrm.app2.net.api.ApiGetTargetList;
import com.huntor.mscrm.app2.provider.api.ApiFixedGroupFansListDb;
import com.huntor.mscrm.app2.ui.DetailedInformationActivity;
import com.huntor.mscrm.app2.ui.MainActivity2;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberFragment2 extends BaseFragment implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemLongClickListener, onCheckItemListener, AdapterView.OnItemClickListener {
    private static final String TAG = "GroupMemberFragment2";
    private View view;
    private XListView mListView;
    private List<Fans> mFanList;
    private int mTargetListId;
    public GroupMemberAdapter2 mAdapter;
    private int mPageSize = 20;
    public IconTextView itv_add;
    private BaseActivity activity;
    public String mGroupName;
    private RefreshCallback refreshCallback;
    protected static final int ORDER_BY_DEFAULT = 3;//排序字段
    protected static final int ORDER_FLAG = 2;//排序标识 1.顺序  2。逆序
    public Toolbar toolbar;
    private int mPreviousVisibleItem;
    public FloatingActionButton fab;
    public int mGroupCount;

    public enum Status {ADD, CANCEL, DELETE}
    public Status FabStatus = Status.ADD;

    public void setRefreshCallback(RefreshCallback refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group_member, container, false);
        initView();
        showFAB();
        setListener();
        loadLocalData();
        loadData();
        return view;
    }

    private void initView() {
        toolbar = MainActivity2.toolbar;
        mListView = (XListView) view.findViewById(R.id.group_member_listview);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);
        mFanList = new ArrayList<>();
        mAdapter = new GroupMemberAdapter2(getActivity(), mFanList);
        mListView.setAdapter(mAdapter);
        mPageNum = 1;
        activity = (BaseActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mTargetListId = bundle.getInt("targetListId");
            mGroupName = bundle.getString("name");
            mGroupCount = bundle.getInt("size");
            toolbar.setTitle(mGroupName+"("+mGroupCount+")");
        }
    }

    private void showFAB() {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show(true);
                fab.setShowAnimation(AnimationUtils.loadAnimation(activity, R.anim.show_from_bottom));
                fab.setHideAnimation(AnimationUtils.loadAnimation(activity, R.anim.hide_to_bottom));
            }
        }, 300);
        fab.setOnClickListener(this);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    fab.hide(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    fab.show(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    public void onResume() {
        //toolbar.setTitle(mGroupName);
        super.onResume();
    }


    private void loadLocalData() {
        List<Fans> fans = ApiFixedGroupFansListDb.getFixedGroupFansList(activity, mTargetListId);
        updateAdapter(fans, false);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        if (activity != null) {
            //activity.showCustomDialog(R.string.loading);
        }
        HttpRequestController.getFansTargetList(activity, mTargetListId, mPageSize, mPageNum,
                ORDER_BY_DEFAULT, ORDER_FLAG, new HttpResponseListener<ApiGetTargetList.ApiGetTargetListResponse>() {
                    @Override
                    public void onResult(ApiGetTargetList.ApiGetTargetListResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            updateAdapter(response.fansQueryResult.fans, response.fansQueryResult.nextPage);
                        } else if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR) {
                            //updateAdapter(response.fansQueryResult.fans,response.fansQueryResult.nextPage);
                        } else {
                            Utils.toast(activity, "加载数据失败！");
                        }
                        if (activity != null) {
                            activity.dismissCustomDialog();
                        }
                        onLoad();
                    }
                });
    }

    private void updateAdapter(List<Fans> responseData, boolean nextPage) {
        MyLogger.i(TAG, "responseData: " + responseData.toString());
        if (responseData != null && responseData.size() > 0) {
            if (mPageNum == 1) {
                mFanList.clear();
                mFanList.addAll(responseData);
            } else {
                mFanList.addAll(responseData);
            }
            mAdapter.notifyDataSetChanged();
        }
        mListView.setPullLoadEnable(nextPage);
        mPageNum = nextPage ? ++mPageNum : mPageNum;
    }

    //设置监听事件
    private void setListener() {
        //view.findViewById(R.id.img_left_corner).setOnClickListener(this);
        //view.findViewById(R.id.add_member_symbol_imag).setOnClickListener(this);
        mListView.setXListViewListener(this);
        mListView.setOnItemLongClickListener(this);
        //复选框点击事件监听
        mAdapter.setOnCheckItemListener(this);
        mListView.setOnItemClickListener(this);
    }

    //XListView 停止下拉刷新和上拉加载更多
    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    //Title图标点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab://添加粉丝
                MyLogger.i(TAG,"FabStatus: "+FabStatus);
                if (FabStatus == Status.ADD) {
                    MyLogger.i(TAG,"FabStatus: ADD");
                    addFans();
                }

                if (FabStatus == Status.CANCEL) {
                    MyLogger.i(TAG,"FabStatus: CANCEL");
                    FabStatus = Status.ADD;
                    fab.setImageResource(R.drawable.ic_add_white_24dp);
                    mAdapter.clearCheckedItems();
                    mAdapter.notifyDataSetChanged();
                    toolbar.setTitle(mGroupName + "(" + mGroupCount + ")");
                }
                if (FabStatus == Status.DELETE) {
                    MyLogger.i(TAG,"FabStatus: DELETE");
                    List<Fans> mCheckedItems = mAdapter.mCheckedItems;
                    if (mCheckedItems.size() > 0) {
                        int[] items = new int[mCheckedItems.size()];
                        for (int i = 0; i < items.length; i++) {
                            items[i] = mCheckedItems.get(i).id;
                        }
                        deleteFansList(items);
                    } else {
                        Utils.toast(getActivity(), "请选择要删除的粉丝！");
                    }
                }

                break;
        }
    }

    private void addFans() {
        HttpRequestController.getAllFansId(activity, mTargetListId, false, new HttpResponseListener<ApiGetAllFansId.ApiGetAllFansIdResponse>() {
            @Override
            public void onResult(ApiGetAllFansId.ApiGetAllFansIdResponse response) {
                if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                    if (response.allFansId.ids != null) {
                        MyLogger.i(TAG, "response.allFansId.ids: " + response.allFansId.ids);

                        Bundle bundle = new Bundle();
                        bundle.putInt("targetListId", mTargetListId);
                        AdmFansFragment2 admFansFragment = new AdmFansFragment2();
                        admFansFragment.setArguments(bundle);
                        admFansFragment.setData(response.allFansId.ids, mGroupName, new AdmFansFragment2.RefreshCallback() {
                            @Override
                            public void onResult(List<Fans> addedFans) {
                                if(addedFans != null){
                                    mFanList.addAll(addedFans);
                                    mAdapter.notifyDataSetChanged();
                                    mGroupCount = mFanList.size();
                                    if (refreshCallback != null) {
                                        refreshCallback.onResult(mTargetListId, mFanList.size());
                                    }
                                }else{
                                    toolbar.setTitle(mGroupName + "(" + mGroupCount + ")");
                                }
                            }
                        });
                        FragmentManager manager = getFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.add(R.id.fl_content, admFansFragment, Constant.ADM_FANS);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } else {
                    Utils.toast(activity, response.getRetInfo());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        MyLogger.i(TAG,"onDestroyView");
        if (refreshCallback != null) {
            refreshCallback.onResult(mTargetListId, -1);
        }
        super.onDestroyView();
    }

    /**
     * 删除粉丝对话框确认
     *
     * @param items
     */
    private void deleteFansList(final int[] items) {
        LayoutInflater infalter = (LayoutInflater) getActivity().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View view = infalter.inflate(R.layout.set_group_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("确定删除?");
        EditText et_group_name = (EditText) view.findViewById(R.id.et_group_name);
        et_group_name.setVisibility(View.GONE);

        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteFans(items);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    /**
     * 网络请求批量删除粉丝
     *
     * @param items
     */
    private void deleteFans(int[] items) {
        activity.showCustomDialog(R.string.loading);
        HttpRequestController.getFansTargetListRemove(getActivity(), mTargetListId, items,
                new HttpResponseListener<ApiFansTargetListRemove.ApiFansTargetListRemoveResponse>() {
                    @Override
                    public void onResult(ApiFansTargetListRemove.ApiFansTargetListRemoveResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            FabStatus = Status.ADD;
                            fab.setImageResource(R.drawable.ic_add_white_24dp);
                            mFanList.removeAll(mAdapter.mCheckedItems);
                            mAdapter.clearCheckedItems();
                            mAdapter.notifyDataSetChanged();
                            MyLogger.i(TAG,"mFanList: "+mFanList.size());

                            toolbar.setTitle(mGroupName+"("+(mFanList.size())+")");
                            Utils.toast(getActivity(), "删除成功！");
                            if (refreshCallback != null) {
                                refreshCallback.onResult(mTargetListId, mFanList.size());
                            }
                        } else {
                            Utils.toast(getActivity(), response.getRetInfo() + "");
                        }
                        activity.dismissCustomDialog();
                    }
                });
    }

    //XListView下拉刷新
    @Override
    public void onRefresh() {
        mAdapter.isCheckBoxShow = false;
        mAdapter.clearCheckedItems();
        mPageNum = 1;
        mFanList.clear();
        loadData();
    }

    //XListView上拉加载更多
    @Override
    public void onLoadMore() {
        loadData();
    }

    //XListView条目长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        FabStatus = Status.CANCEL;
        fab.setImageResource(R.drawable.ic_close);
        mAdapter.isCheckBoxShow = true;
        mAdapter.notifyDataSetChanged();
        return true;
    }

    //XListView条目复选框点击事件
    @Override
    public void onItemCheck(int position) {
        Fans fans = mAdapter.getItem(position);
        fans.isCheck = !fans.isCheck;
        if (fans.isCheck) {
            mAdapter.mCheckedItems.add(fans);
        } else {
            mAdapter.mCheckedItems.remove(fans);
        }
        int checkedItems = mAdapter.mCheckedItems.size();
        //Log.i(TAG, "checkedItems:"+checkedItems +" ----- "+ fans.nickName);
        if (checkedItems > 0) {
            FabStatus = Status.DELETE;
            fab.setImageResource(R.drawable.ic_delete);
            toolbar.setTitle(mGroupName + "(已选择:" + checkedItems + ")");
        } else {
            FabStatus = Status.CANCEL;
            fab.setImageResource(R.drawable.ic_close);
            //toolbar.setTitle(mGroupName);
            toolbar.setTitle(mGroupName+"("+mGroupCount+")");
        }
    }

    //XListView条目点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.isCheckBoxShow) {
            CheckBox cb_item = (CheckBox) view.findViewById(R.id.cb_item);
            cb_item.setChecked(!cb_item.isChecked());
            onItemCheck(position - 1);
        } else {
            Intent intent = new Intent(getActivity(), DetailedInformationActivity.class);
            intent.putExtra(Constant.FANS_ID, mAdapter.getItem(position - 1).id);
            startActivity(intent);
        }
    }

    //分组列表刷新回调
    public interface RefreshCallback {
        void onResult(int targetListId, int groupSize);
    }
}
