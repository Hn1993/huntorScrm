package com.huntor.mscrm.app2.adapter;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.utils.DateFormatUtils;

import java.util.List;

/**
 * Created by cary.xi on 2015/5/4.
 */
public class MemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<Fans> mData;
    private int mType;

    public MemberAdapter(Context context, List<Fans> data) {
        this.mContext = context;
        this.mData = data;
    }

    public MemberAdapter(Context context, List<Fans> data, int type) {
        this.mContext = context;
        this.mData = data;
        this.mType = type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Fans getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        ViewHolder vh = null;
        if (null == convertView) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_member, null);
            vh.headPortrait = (ImageView) convertView.findViewById(R.id.head_portrait);
            vh.name = (TextView) convertView.findViewById(R.id.member_name);
            vh.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Fans fans = getItem(position);
        vh.name.setText(fans.nickName);

        String time = DateFormatUtils.getPassedTime(mContext, fans.registTime);
        vh.time.setText(time);


        //1新增 2普通 3高潜 4已购 5全部
        if( 1 == mType){

        } else if(2 == mType){
            vh.headPortrait.setImageResource(R.drawable.p_head);
        } else if(3 == mType){
            vh.headPortrait.setImageResource(R.drawable.g_head);
        } else if(4 == mType){
            vh.headPortrait.setImageResource(R.drawable.y_head);
        } else {

        }

        return convertView;
    }


    class ViewHolder {
        TextView name;//姓名
        ImageView headPortrait;//ͷ头像
        TextView time;//时间
    }
}
