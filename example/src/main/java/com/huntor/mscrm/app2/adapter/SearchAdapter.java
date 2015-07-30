package com.huntor.mscrm.app2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.provider.MSCRMContract;
import com.huntor.mscrm.app2.utils.DateFormatUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Admin on 2015/7/1.
 */
public class SearchAdapter extends CursorAdapter {

	private int viewResId;
	private DisplayImageOptions options;
	private Cursor cursor;

	public SearchAdapter(Context context, int resource, Cursor cursor){
		super(context,cursor,true);
		this.cursor = cursor;
		this.viewResId = resource;

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.dimension_code_loading_default)
				.showImageForEmptyUri(R.drawable.m_head)
				.showImageOnFail(R.drawable.m_head)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				//.displayer(new RoundedBitmapDisplayer(15))
				.build();
	}

	@Override
	public void changeCursor(Cursor cursor) {
		super.changeCursor(cursor);
		this.cursor = cursor;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout rl_item = (RelativeLayout) inflater.inflate(viewResId, parent, false);
		return rl_item;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		CheckBox cb_item = (CheckBox) view.findViewById(R.id.cb_item);
		ImageView iv_head = (ImageView) view.findViewById(R.id.iv_head);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
		TextView tv_grade = (TextView) view.findViewById(R.id.tv_grade);
		cb_item.setVisibility(View.GONE);

		ImageLoader.getInstance().displayImage(cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.AVATAR)), iv_head, options);
		String name  = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NAME));
		String nickName  = cursor.getString(cursor.getColumnIndex(MSCRMContract.FansInfo.NICKNAME));
		name = TextUtils.isEmpty(name)?"":name;
		nickName = TextUtils.isEmpty(nickName)?"":"("+nickName+")";
		tv_name.setText(name+nickName);

		Long subscribeTime = cursor.getLong(cursor.getColumnIndex(MSCRMContract.FansInfo.SUBSCRIBE_TIME));
		if(subscribeTime != 0){
			String time = DateFormatUtils.getPassedTime(context, subscribeTime);
			tv_time.setText(time);
		}else{
			tv_time.setText("");
		}

		Integer group = cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.GROUP_ID)) ;
		switch (group) {
			case 2:
				tv_grade.setText(R.string.user_grade_normal);
				break;
			case 3:
				tv_grade.setText(R.string.user_grade_high);
				break;
			case 4:
				tv_grade.setText(R.string.user_grade_buyed);
				break;
			default:
				tv_grade.setText("");
				break;
		}

	}

	@Override
	public Integer getItem(int position) {
		if ( cursor != null) {
			if (cursor.moveToPosition(position)) {
				return cursor.getInt(cursor.getColumnIndex(MSCRMContract.FansInfo.ID));
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}


}
