package com.huntor.mscrm.app2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.ShakeMessageModle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by tangtang on 15/7/30.
 */
public class ShakeAdapter extends BaseAdapter {

    Context context;
    List<ShakeMessageModle> data;
    public ShakeAdapter(Context context,List<ShakeMessageModle> data)
    {
        this.context = context;
        this.data = data;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ShakeMessageModle> getData() {
        return data;
    }

    public void setData(List<ShakeMessageModle> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = setViewHolder();
        }


        ViewHolder viewHolder = (ViewHolder) convertView.getTag();


        ImageLoader.getInstance().displayImage(data.get(position).avatar, viewHolder.img, options);


        viewHolder.content.setText(data.get(position).nickName + "发来一条摇一摇信息");

        viewHolder.chat_time.setText(data.get(position).time);

        return convertView;
    }


    private View setViewHolder(){
        View  convertView = LayoutInflater.from(context).inflate(R.layout.item_shake,null);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.img = (ImageView) convertView.findViewById(R.id.fan_head);
        viewHolder.chat_time = (TextView) convertView.findViewById(R.id.chat_time);
        viewHolder.content = (TextView) convertView.findViewById(R.id.shake_content);

        convertView.setTag(viewHolder);
        return convertView;
    }

    class ViewHolder {


        ImageView img;
        TextView content;
        TextView chat_time;

    }
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.dimension_code_fail_default)
            .showImageOnFail(R.drawable.dimension_code_fail_default)
            .showImageOnLoading(R.drawable.dimension_code_loading_default)
            .build();
}
