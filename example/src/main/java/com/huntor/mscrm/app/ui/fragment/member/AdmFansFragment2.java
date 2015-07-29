package com.huntor.mscrm.app.ui.fragment.member;

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
import android.widget.CheckBox;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.adapter.AdmFansAdapter;
import com.huntor.mscrm.app.adapter.AdmFansAdapter2;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiAddFansTargetlist;
import com.huntor.mscrm.app.net.api.ApiFansGroup;
import com.huntor.mscrm.app.ui.DetailedInformationActivity;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.component.XListView;
import com.huntor.mscrm.app.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PreferenceUtils;
import com.huntor.mscrm.app.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 向自定义分组中添加粉丝
 */
public class AdmFansFragment2 extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener, AdmFansAdapter2.onListViewItemClickListener {
	private final String TAG = "AdmFansFragment2";
	private XListView mListView;
	private AdmFansAdapter2 mAdapter;
	private int mTargetListId;
	private View mRoot;
	private final int SINGLE_PAGE_LOAD_COUNT = 30;
	private int mPageNum = 1;
	private Button mConfirmBtn;
	private List<Integer> mGroupIDs;
	private List<Fans> mAdapterList;
	private List<Fans> mCheckedList;
	private BaseActivity activity;
	private RefreshCallback refCallBack;
	private String groupName;
	private TextView tv_title;
	private TextView no_content_hint;
	private int empID;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.fragment_adm_fans, container, false);
		initView();
		return mRoot;
	}

	private void initView() {
		Bundle bundle = getArguments();
		mTargetListId = bundle.getInt("targetListId");
		activity = (BaseActivity) getActivity();

		mCheckedList = new LinkedList<Fans>();
		mAdapterList = new LinkedList<Fans>();

		mConfirmBtn = (Button) mRoot.findViewById(R.id.sure_btn);
		tv_title = (TextView) mRoot.findViewById(R.id.text_base_title);
		no_content_hint = (TextView) mRoot.findViewById(R.id.no_content_hint);
		mConfirmBtn.setVisibility(View.GONE);
		tv_title.setText("分组管理(" + mGroupIDs.size() + ")");
		mConfirmBtn.setOnClickListener(this);
		mRoot.findViewById(R.id.img_left_corner).setOnClickListener(this);
		mListView = (XListView) mRoot.findViewById(R.id.listview);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(false);
		mListView.setOnItemClickListener(this);
		mListView.setXListViewListener(this);

		mAdapter = new AdmFansAdapter2(getActivity(), mAdapterList);
		mAdapter.setOnListViewItemClickListener(this);
		mListView.setAdapter(mAdapter);
		empID = PreferenceUtils.getInt(getActivity(), Constant.PREFERENCE_EMP_ID, 0);
		getAllFans();
	}

	public void setData(List<Integer> ids, String groupName, RefreshCallback refCallBack) {
		this.mGroupIDs = ids;
		this.refCallBack = refCallBack;
		this.groupName = groupName;
	}

	/**
	 * 标题栏按钮点击事件
	 *
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.sure_btn:
				if (mCheckedList.size() > 0) {
					int[] fansId = new int[mCheckedList.size()];
					for (int i = 0; i < mCheckedList.size(); i++) {
						fansId[i] = mCheckedList.get(i).id;
						mCheckedList.get(i).isCheck = false;
					}
					addFans(mTargetListId, fansId);
				} else {
					Utils.toast(getActivity(), "请选择要添加的粉丝！");
				}

				break;
			case R.id.img_left_corner:
				finish();
				break;
		}
	}

	/**
	 * ListView条目点击事件
	 *
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int totalList = mGroupIDs.size() + mCheckedList.size();
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_item);
		if (totalList >= 500 && !checkBox.isChecked()) {
			Utils.toast(getActivity(), "每组粉丝数量不能超过500！");
		} else {
			checkBox.setChecked(!checkBox.isChecked());
			Fans fans = mAdapter.getItem(position - 1);
			fans.isCheck = !fans.isCheck;
			if (fans.isCheck) {
				mCheckedList.add(fans);
				totalList++;
			} else {
				mCheckedList.remove(fans);
				totalList--;
			}
			tv_title.setText("分组管理(" + totalList + ")");
		}
	}

	/**
	 * 回调接口监听条目点击
	 *
	 * @param position
	 */
	@Override
	public void onItemClick(int position) {
	}

	/**
	 * 停止下拉刷新和下拉刷新
	 */
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	/**
	 * XListView下拉刷新执行动作
	 */
	@Override
	public void onRefresh() {
	}

	/**
	 * XListView上拉加载更多
	 */
	@Override
	public void onLoadMore() {
		getAllFans();
	}

	/**
	 * 网络请求该分组以外的粉丝数据
	 */
	private void getAllFans() {
		if (mPageNum == 1 && activity != null) {
			//activity.showCustomDialog(R.string.loading);
		}
		final long requestTime = System.currentTimeMillis();
		HttpRequestController.fansGroup(activity, empID, 5, SINGLE_PAGE_LOAD_COUNT, mPageNum, 1, 1,
				new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
					@Override
					public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
						if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
							List<Fans> fans = response.fansGroupResult.fans;
							boolean nextPage = response.fansGroupResult.nextPage;
							long responesTime = System.currentTimeMillis();
							long time = responesTime - requestTime;
							MyLogger.i(TAG,"response time: "+time);
							//Utils.toast(activity,"response time: "+time);

							if(fans != null && fans.size() > 0){
								updateAdapter(fans,nextPage);
							}
						} else if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR) {
							//updateAdapter(response);
						} else {
							//Utils.toast(activity, response.getRetInfo() + "");
						}
						/*if (activity != null) {
							activity.dismissCustomDialog();
						}*/
						onLoad();
					}
				});
	}

	/**
	 * 刷新Adapter
	 * @param fans
	 * @param nextPage
	 */
	private void updateAdapter(List<Fans> fans, boolean nextPage) {

		mPageNum = nextPage ? ++mPageNum : mPageNum;
		mListView.setPullLoadEnable(nextPage);
		if (fans != null && fans.size() > 0) {
			if (mGroupIDs != null && mGroupIDs.size() > 0) {
				for ( int id : mGroupIDs) {
					for (int i = 0; i < fans.size(); i++) {
						if (id == fans.get(i).id) {
							fans.remove(i);
							break;
						}
					}
				}
			}
			if(fans.size()>0){
				mAdapterList.addAll(fans);
				mAdapter.notifyDataSetChanged();
				mConfirmBtn.setVisibility(mAdapterList.size() > 0 ? View.VISIBLE : View.GONE);

			}else{
				//Utils.toast(activity,"没有更多粉丝！" );
				//getAllFans();
			}
		}

	}


	/**
	 * 网络请求批量添加粉丝
	 *
	 * @param targetListId
	 * @param fanIds
	 */
	private void addFans(int targetListId, int[] fanIds) {
		activity.showCustomDialog(R.string.loading);
		HttpRequestController.addFansTargetList(getActivity(), targetListId, fanIds,
				new HttpResponseListener<ApiAddFansTargetlist.ApiAddFansTargetlistResponse>() {
					@Override
					public void onResult(ApiAddFansTargetlist.ApiAddFansTargetlistResponse response) {
						if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
							refCallBack.onResult(mCheckedList);
						} else {
							Utils.toast(getActivity(), response.getRetInfo() + "");
						}
						activity.dismissCustomDialog();
						finish();
					}
				});
	}

	/**
	 * 返回上一个界面
	 */
	public void finish() {
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		manager.popBackStack();
		transaction.remove(this);
		transaction.commit();
	}

	/**
	 * 回调接口刷新
	 */
	public interface RefreshCallback {
		void onResult(List<Fans> addedFans);
	}
}
