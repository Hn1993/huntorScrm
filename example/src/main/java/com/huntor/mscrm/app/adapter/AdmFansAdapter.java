package com.huntor.mscrm.app.adapter;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.AdmFansResult;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.ui.fragment.member.AdmFansFragment;
import com.huntor.mscrm.app.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

public class AdmFansAdapter extends BaseAdapter{
    private final String TAG = "AdmFansAdapter";
    private Context mContext;
    private List<Fans> mList;

    public AdmFansAdapter(Context context, List<Fans> list){
        if (context != null) {
            this.mContext = context;
        }
        setData(list);
    }

    public void setData(List<Fans> list){
        if(mList == null){
            mList = new ArrayList<Fans>();
        }
        if(list != null){
            mList = list;
        }
    }

    public List<Fans> getData(){
        return mList;
    }

    /***
     * 上拉加载 往后追加数据 而不是清空
     * @param list
     */
    public void addData(List<Fans> list){
        if(mList != null){
            for(Fans fans:list){
                mList.add(fans);
            }
        }else{
            mList = new ArrayList<Fans>();
            mList.addAll(list);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fans getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        ViewHolder vh = null;
        if(null == convertView){
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_adm_fans, null);
            vh.root = (LinearLayout) convertView.findViewById(R.id.root);
            vh.check = (CheckBox) convertView.findViewById(R.id.checkbox);//选中框
            vh.name = (TextView) convertView.findViewById(R.id.name);//名称
            vh.location = (TextView) convertView.findViewById(R.id.location);
            vh.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Fans fans = getItem(position);
        vh.name.setText(fans.nickName);
        vh.time.setText(DateFormatUtils.getPassedTime(mContext, fans.registTime));
        vh.check.setClickable(false);
        if(fans.isCheck){
            vh.check.setChecked(true);
        }else{
            vh.check.setChecked(false);
        }
        final int positions  = position;
        vh.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox check = (CheckBox)view.findViewById(R.id.checkbox);
                Fans fans = getItem(positions);
                if(!fans.isCheck){
                    check.setChecked(true);
                    getItem(positions).isCheck = true;
                } else {
                    check.setChecked(false);
                    getItem(positions).isCheck = false;
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        CheckBox check;
        TextView name;
        TextView location;
        TextView time;
        LinearLayout root;
    }
}
