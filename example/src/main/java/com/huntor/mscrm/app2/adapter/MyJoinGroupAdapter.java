package com.huntor.mscrm.app2.adapter;

import android.app.Service;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.Target;

/**
 * Created by Admin on 2015/5/9.
 */
public class MyJoinGroupAdapter extends MyBaseAdapter{

    Context context;
    /**
     * 构造方法
     *
     * @param c
     */
    public MyJoinGroupAdapter(Context c) {
        super(c);
        this.context=c;
    }

    /**
     * 用户必须实现此方法 加载不同的布局
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_jion_group, parent, false);
            holder.nameHolder = (TextView) convertView.findViewById(R.id.item_join_group_mygroup);

            Target myGroup = (Target) list .get(position);
            String groupName = myGroup.name;
            //holder.nameHolder.setText(groupName);
            int count = myGroup.count;
            holder.nameHolder.setText(Html.fromHtml(groupName + "<font color=grey>(" + count + "人)</font>"));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }
    private class ViewHolder{
        public TextView nameHolder;

    }
}
