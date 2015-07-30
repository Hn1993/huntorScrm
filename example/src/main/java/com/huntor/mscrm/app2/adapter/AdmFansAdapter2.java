package com.huntor.mscrm.app2.adapter;

import android.app.Service;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.utils.DateFormatUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.util.List;

public class AdmFansAdapter2 extends BaseAdapter{
    private final String TAG = "AdmFansAdapter2";
    private Context mContext;
    public List<Fans> mList;
    private onListViewItemClickListener listener;
    private DisplayImageOptions options;

    public AdmFansAdapter2(Context context, List<Fans> list){
        this.mContext = context;
        this.mList = list;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.dimension_code_loading_default)
                .showImageForEmptyUri(R.drawable.m_head)
                .showImageOnFail(R.drawable.m_head)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                //.displayer(new RoundedBitmapDisplayer(15))
                .build();
    }

    public void setOnListViewItemClickListener(onListViewItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mList == null ? 0: mList.size();
    }

    @Override
    public Fans getItem(int position) {
        return mList == null? null:mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        ViewHolder vh;
        if(null == convertView){
            vh = new ViewHolder();
            //convertView = inflater.inflate(R.layout.item_adm_fans2, null);
            convertView = inflater.inflate(R.layout.item_group_member2, null);

            vh.check = (CheckBox) convertView.findViewById(R.id.cb_item);
            vh.head = (ImageView) convertView.findViewById(R.id.iv_head);
            vh.name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Fans fans = getItem(position);

        if(TextUtils.isEmpty(fans.name)){
            vh.name.setText(R.string.no);
        }else{
            vh.name.setText(fans.name);
        }


        if(fans.subscribeTime != 0){
            String time = DateFormatUtils.getPassedTime(mContext, fans.subscribeTime);
            vh.time.setText(time);
        }else{
            vh.time.setText("");
        }
        vh.check.setClickable(false);
        vh.check.setChecked(fans.isCheck);

        switch (fans.group) {
            case 2:
                vh.tv_grade.setText(R.string.user_grade_normal);
                break;
            case 3:
                vh.tv_grade.setText(R.string.user_grade_high);
                break;
            case 4:
                vh.tv_grade.setText(R.string.user_grade_buyed);
                break;
            default:
                vh.tv_grade.setText("");
                break;
        }

        ImageLoader.getInstance().displayImage(fans.avatar,vh.head,options);

        return convertView;
    }

    public interface onListViewItemClickListener{
        void onItemClick(int position);
    }

    class ViewHolder {
        CheckBox check;
        ImageView head;
        TextView name;
        TextView time;
        TextView tv_grade;
    }
}
