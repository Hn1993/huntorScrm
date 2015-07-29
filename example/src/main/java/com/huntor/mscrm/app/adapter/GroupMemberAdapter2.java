package com.huntor.mscrm.app.adapter;

import android.app.Service;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.utils.DateFormatUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.LinkedList;
import java.util.List;

public class GroupMemberAdapter2 extends BaseAdapter {

	private static final String TAG = "GroupMemberAdapter2";
	private Context mContext;
	private List<Fans> mData;
	public boolean isCheckBoxShow = false;
	public List<Fans> mCheckedItems;
	private onCheckItemListener listener;
	private DisplayImageOptions options;

	public GroupMemberAdapter2(Context context, List<Fans> data) {
		this.mContext = context;
		this.mData = data;
		mCheckedItems = new LinkedList<Fans>();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.dimension_code_loading_default)
				.showImageForEmptyUri(R.drawable.m_head)
				.showImageOnFail(R.drawable.m_head)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				//.displayer(new RoundedBitmapDisplayer(15))
				.build();
	}

	public void setOnCheckItemListener(onCheckItemListener listener) {
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public Fans getItem(int position) {
		return mData != null ? mData.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_group_member2, null);
			holder.cb_item = (CheckBox) convertView.findViewById(R.id.cb_item);
			holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Fans fans = getItem(position);
		holder.cb_item.setChecked(fans.isCheck);
		holder.cb_item.setVisibility(isCheckBoxShow ? View.VISIBLE : View.GONE);
		holder.cb_item.setClickable(false);

		//holder.tv_name.setText(fans.nickName);
		//2015-06-26 粉丝昵称修改为粉丝名字
		holder.tv_name.setText(TextUtils.isEmpty(fans.name)?"暂无":fans.name);

		//2015-06-04 显示时间有注册时间改为关注时间
		//String time = DateFormatUtils.getPassedTime(mContext, fans.registTime);
		if (fans.subscribeTime != 0) {
			String time = DateFormatUtils.getPassedTime(mContext, fans.subscribeTime);
			holder.tv_time.setText(time);
		} else {
			holder.tv_time.setText("");
		}
		// 2普通 3高潜 4已购
		switch (fans.group) {
			case 2:
				holder.tv_grade.setText(R.string.user_grade_normal);
				break;
			case 3:
				holder.tv_grade.setText(R.string.user_grade_high);
				break;
			case 4:
				holder.tv_grade.setText(R.string.user_grade_buyed);
				break;
			default:
				holder.tv_grade.setText("");
				break;
		}

		ImageLoader.getInstance().displayImage(fans.avatar,holder.iv_head,options);

		return convertView;
	}

	private class ViewHolder {
		CheckBox cb_item;
		ImageView iv_head;
		TextView tv_name;
		TextView tv_time;
		TextView tv_grade;
	}

	public interface onCheckItemListener {
		void onItemCheck(int position);
	}

	public void clearCheckedItems() {
		isCheckBoxShow = false;
		if (mCheckedItems.size() > 0) {
			for (Fans fan : mCheckedItems) {
				fan.isCheck = false;
			}
			mCheckedItems.clear();
		}
	}


}
