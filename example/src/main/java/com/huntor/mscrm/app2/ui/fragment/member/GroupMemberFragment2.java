package com.huntor.mscrm.app2.ui.fragment.member;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
	private String mGroupName;
	private RefreshCallback refreshCallback;

	protected static final int ORDER_BY_DEFAULT = 3;//排序字段
	//1.顺序  2。逆序
	protected static final int ORDER_FLAG = 2;//排序标识

	public void setRefreshCallback(RefreshCallback refreshCallback) {
		this.refreshCallback = refreshCallback;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_group_member, container, false);
		initView();
		return view;
	}

	private void initView() {

		TextView title = (TextView) view.findViewById(R.id.title);
		mListView = (XListView) view.findViewById(R.id.group_member_listview);
		//itv_add = (IconTextView) view.findViewById(R.id.add_member_symbol_imag);
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(false);
		mFanList = new ArrayList<Fans>();
		mAdapter = new GroupMemberAdapter2(getActivity(), mFanList);
		mListView.setAdapter(mAdapter);
		mPageNum = 1;
		activity = (BaseActivity) getActivity();

		Bundle bundle = getArguments();
		if (bundle != null) {
			mTargetListId = bundle.getInt("targetListId");
			mGroupName = bundle.getString("name");
			title.setText(mGroupName);
		}

		setListener();
		loadLocalData();
		loadData();
	}


	private void loadLocalData(){
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
			if(mPageNum == 1){
				mFanList.clear();
				mFanList.addAll(responseData);
			}else{
				mFanList.addAll(responseData);
			}
			mAdapter.notifyDataSetChanged();
		}
		mListView.setPullLoadEnable(nextPage);
		mPageNum = nextPage ? ++mPageNum : mPageNum;
	}

	//设置监听事件
	private void setListener() {
		view.findViewById(R.id.img_left_corner).setOnClickListener(this);
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
		final FragmentManager manager = getFragmentManager();
		final FragmentTransaction transaction = manager.beginTransaction();
		switch (v.getId()) {
			case R.id.img_left_corner://左上角返回
				manager.popBackStack();
				transaction.remove(this);
			transaction.commit();
				break;
			/*case R.id.add_member_symbol_imag://添加粉丝
				if (mAdapter.isCheckBoxShow) {
					if (itv_add.getText().toString().equals("取消")) {
						mAdapter.clearCheckedItems();
						mAdapter.notifyDataSetChanged();
						itv_add.setText(R.string.icon_text_plus);
					} else {
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
				} else {

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
											//onRefresh();
											mFanList.addAll(addedFans);
											mAdapter.notifyDataSetChanged();
											if (refreshCallback != null) {
												refreshCallback.onResult(mTargetListId, mFanList.size());
											}
										}
									});
									transaction.add(R.id.frame_main, admFansFragment, Constant.ADM_FANS);
									transaction.addToBackStack(null);
									transaction.commit();

								}
							}else{
							   	Utils.toast(activity,response.getRetInfo());
							}
						}
					});
				}
				break;*/
		}
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
							itv_add.setText(R.string.icon_text_plus);
							mFanList.removeAll(mAdapter.mCheckedItems);
							mAdapter.clearCheckedItems();
							mAdapter.notifyDataSetChanged();
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
		itv_add.setText("取消");
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
			itv_add.setVisibility(View.VISIBLE);
			itv_add.setText("删除(" + checkedItems + ")");
		} else {
			itv_add.setText("取消");
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
