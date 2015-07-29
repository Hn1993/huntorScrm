package com.huntor.mscrm.app.adapter;

import android.app.Service;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.Target;
import com.huntor.mscrm.app.utils.DateFormatUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by IDEA
 * User : SL
 * on 2015/4/30 0030
 * 15:38.
 */
public class MyGroupAdapter extends BaseAdapter {

	private Context mContext;
	private List<Target> mData;

	public List<Target> getData() {
		return mData;
	}

	public MyGroupAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<Target> data) {
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData != null ? mData.size() : 0;
	}

	@Override
	public Target getItem(int position) {
		return mData != null ? mData.get(position): null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_my_group, parent, false);
			holder.nameHolder = (TextView) convertView.findViewById(R.id.text_item_my_group_name);
			holder.updateHolder = (TextView) convertView.findViewById(R.id.text_item_my_group_update);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Target target = getItem(position);
		String name = target.name;
		long updateTime = target.updateTime;
		int count = target.count;
		Log.i("各组人数", "" + count);
		holder.nameHolder.setText(Html.fromHtml(name + "<font color=grey>(" + count + "人)</font>"));
		String time = DateFormatUtils.getPassedTime(mContext, updateTime);
		holder.updateHolder.setText(time);
		return convertView;
	}

	private class ViewHolder {
		public TextView nameHolder;
		//public TextView numberHolder;
		public TextView updateHolder;
	}
}
