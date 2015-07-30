package com.huntor.mscrm.app2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.FanInfo;
import com.huntor.mscrm.app2.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cao on 2015/5/13.
 */
public class UserBuyedAdapter extends BaseAdapter {

    String TAG = "UserBuyedAdapter";

    private LayoutInflater inflater;
    private List<FanInfo.Details> list;
    private List<FanInfo.Deals> dealsList;
    private Context context;

    public UserBuyedAdapter(Context context, List<FanInfo.Details> list, List<FanInfo.Deals> dealsList) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        } else {
            throw new IllegalStateException("Context must nut be null");
        }
        setData(list, dealsList);
    }

    private void setData(List<FanInfo.Details> list, List<FanInfo.Deals> dealsList) {
        if (list == null && dealsList == null) {
            list = new ArrayList<FanInfo.Details>();
            dealsList = new ArrayList<FanInfo.Deals>();
            this.list = list;
            this.dealsList = dealsList;
        }
        if (list != null && dealsList != null) {
            this.list = list;
            this.dealsList = dealsList;
        }

    }

    @Override
    public int getCount() {


        return list.size();
    }

    @Override
    public FanInfo.Details getItem(int i) {

        return list.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View ret = null;

        ViewHolder holder = null;
        if (ret == null) {
            ret = inflater.inflate(R.layout.item_dataileinfo_buyed_list, null);
            holder = new ViewHolder();
            holder.imgHolder = (ImageView) ret.findViewById(R.id.item_detaileinfo_buyed_image);
            holder.productHolder = (TextView) ret.findViewById(R.id.item_detaileinfo_buyed_product);
            holder.time = (TextView) ret.findViewById(R.id.item_detaileinfo_buyed_time);
            ret.setTag(holder);
        } else {
            ret.getTag();
        }
        if (getItem(i).productName != null) {
            holder.productHolder.setText(getItem(i).productName);
        } else {
            holder.productHolder.setText("暂无");
        }
        Log.i(TAG, "dealsList.size:" + dealsList.size());
        Log.i(TAG, "i:" + i);
        if (i < dealsList.size()) {
            if (dealsList.get(i) != null) {
                String time = DateFormatUtils.getPassedTime(context, dealsList.get(i).dealTime);
                holder.time.setText(time);
            } else {
                holder.time.setText("");
            }

        }


//        if (view != null) {
//            ret = view;
//        }else {
//            ret = inflater.inflate(R.layout.item_dataileinfo_buyed_list, viewGroup, false);
//        }
//
//        ViewHolder holder = (ViewHolder) ret.getTag();
//
//        if (holder == null) {
//            holder = new ViewHolder();
//
//            holder.imgHolder = (ImageView) ret.findViewById(R.id.item_detaileinfo_buyed_image);
//            holder.productHolder = (TextView) ret.findViewById(R.id.item_detaileinfo_buyed_product);
//            holder.time= (TextView) ret.findViewById(R.id.item_detaileinfo_buyed_time);
//            ret.setTag(holder);
//        }
//        if(getItem(i).productName!=null){
//            holder.productHolder.setText(getItem(i).productName);
//        }else {
//            holder.productHolder.setText("暂无");
//        }
//        String time= DateFormatUtils.getPassedTime(context,dealsList.get(i).dealTime);
//        holder.time.setText(time);

        //holder.imgHolder.setImageResource(R.drawable.y_head);
        return ret;
    }

    private class ViewHolder {
        public TextView productHolder;
        public ImageView imgHolder;
        public TextView time;
    }


}
