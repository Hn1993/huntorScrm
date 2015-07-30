package com.huntor.mscrm.app2.adapter;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntor.mscrm.app2.R;


/**
 * Created by Admin on 2015/7/16.
 */
public class DrawLayoutLeftAdapter extends MyBaseAdapter{

    String TAG=getClass().getName();
    private Context context;
    /**
     * 构造方法
     *
     * @param c
     */
    public DrawLayoutLeftAdapter(Context c) {
        super(c);
        this.context=c;
    }
    viewHolder vh=null;
    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "list.size==" + list.size());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            vh=new viewHolder();
            convertView=inflater.inflate(R.layout.drawerlayout_left_item,null);
            vh.header= (ImageView) convertView.findViewById(R.id.drawerlayout_left_image);
            vh.huntorImage= (ImageView) convertView.findViewById(R.id.drawerlayout_left_huntor_image);
            vh.content= (TextView) convertView.findViewById(R.id.drawerlayout_left_text);
            convertView.setTag(vh);

            if((int)list.get(position)==0){
                vh.header.setImageResource(R.drawable.draw_left_header_interactive);
                vh.content.setText("现场交互");
                //vh.huntorImage.setVisibility(View.VISIBLE);
            }else if((int)list.get(position)==1){
                vh.header.setImageResource(R.drawable.draw_left_header_online);
                vh.content.setText("在线交互");
            }else if((int)list.get(position)==2){
                vh.header.setImageResource(R.drawable.draw_left_header_member);
                vh.content.setText("我的会员");
            }else if((int)list.get(position)==3){
                vh.header.setImageResource(R.drawable.draw_left_header_more);
                vh.content.setText("添加其他功能");
            }

        }else{
            convertView.getTag();
        }

        return convertView;
    }



    public class viewHolder{
        ImageView header;
        ImageView huntorImage;
        TextView content;
    }
}
