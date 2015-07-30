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
import com.huntor.mscrm.app2.ui.component.SlideItem;
import com.huntor.mscrm.app2.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cao on 2015/5/13.
 */
public class UserPurchaseAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<FanInfo.PurchaseIntents> list;

    public UserPurchaseAdapter(Context context, List<FanInfo.PurchaseIntents> list) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }else {
            throw new IllegalStateException("Context must mot be null");
        }
        setData(list);
    }

    public void setData(List<FanInfo.PurchaseIntents> list) {
        if (this.list == null) {
            this.list = new ArrayList<FanInfo.PurchaseIntents>();
        }
        if (list != null) {
            this.list = list;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public FanInfo.PurchaseIntents getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SlideItem ret = null;
        if (view != null) {
            ret =(SlideItem) view;
        }else {
            //ret = inflater.inflate(R.layout.item_detaileinfo_list, viewGroup, false);
            ret = (SlideItem)View.inflate(context,R.layout.item_slide, null);
        }

        ViewHolder holder = (ViewHolder) ret.getTag();

        if (holder == null) {
            holder = new ViewHolder();

            holder.productHolder = (TextView) ret.findViewById(R.id.item_detaileinfo_wantbuy);
            holder.imgHolder = (ImageView) ret.findViewById(R.id.item_detaileinfo_buyed_image);
            holder.timeHolder = (TextView) ret.findViewById(R.id.item_detaileinfo_time);

            ret.setTag(holder);
        }

        FanInfo.PurchaseIntents intents = getItem(i);

        String passedTime = DateFormatUtils.getPassedTime(context, intents.intentTime);
        if(intents.intentTime!=0){
            holder.timeHolder.setText(passedTime);
        }else{
            holder.timeHolder.setText("暂无");
        }

        if(intents!=null){
            if(intents.productId==0){
                //if productId为0  取desc为产品名
                if(intents.desc!=null){
                    holder.productHolder.setText(intents.desc);
                }else{
                    holder.productHolder.setText("暂无");
                }
            }else{
                if(intents.productName!=null){
                    holder.productHolder.setText(intents.productName);
                }else{
                    holder.productHolder.setText("暂无");
                }
            }
        }


        return ret;
    }

    private class ViewHolder{
        public TextView productHolder;//产品
        public ImageView imgHolder;//产品图片
        public TextView timeHolder;//时间
    }
}
