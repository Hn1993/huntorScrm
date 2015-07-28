package com.huntor.scrm.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huntor.scrm.R;

/**
 * Created by Admin on 2015/7/23.
 */
public class HighUserAdapter extends MyBaseAdapter {
    /**
     *
     *
     * @param c
     */
    public HighUserAdapter(Context c) {
        super(c);
    }


    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        viewHolder vh=null;
        if(convertView==null){
            vh=new viewHolder();
            convertView=inflater.inflate(R.layout.member_adapter_item,null);
            //vh.textView= (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(vh);
        }else {
            vh = (viewHolder) convertView.getTag();
        }

        //vh.textView.setText(list.get(position)+"");

        return convertView;
    }


    public class viewHolder{
        TextView textView;
    }
}
