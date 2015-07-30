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
import com.huntor.mscrm.app2.model.FanInfo;
import com.huntor.mscrm.app2.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/5/6.
 */
public class DetaileInfoAdapter extends MyBaseAdapter {

    private Context mContext;


    private LayoutInflater inflater;

    private ArrayList<FanInfo.Details> buyed_list;

    private List<FanInfo.PurchaseIntents> want_list;

    /**
     * 构造方法
     *
     * @param c
     */
    public DetaileInfoAdapter(Context c) {
        super(c);
        this.mContext=c;
    }

    public void setData(List<FanInfo.PurchaseIntents> want_list){
        if (this.want_list == null) {
            this.want_list = new ArrayList<FanInfo.PurchaseIntents>();
        }
        if (want_list != null) {
            this.want_list = want_list;
        }
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
        LayoutInflater inflater = LayoutInflater.from(mContext);
        //DetaileInfo detaileInfo=new DetaileInfo("平板电脑");
        ViewHolder vh = null;
        if(null == convertView){
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_detaileinfo_list, parent, false);
            vh.headPortrait = (ImageView) convertView.findViewById(R.id.item_detaileinfo_image);
            vh.product = (TextView) convertView.findViewById(R.id.item_detaileinfo_wantbuy);
            vh.time = (TextView) convertView.findViewById(R.id.item_detaileinfo_time);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        FanInfo.PurchaseIntents fp = (FanInfo.PurchaseIntents)list.get(position);
        vh.product.setText(fp.productName);

        String time= DateFormatUtils.getPassedTime(context,fp.intentTime);
        if(time!=null){
            vh.time.setText(time);
        }else{
            vh.time.setText("暂无");
        }

        Log.i("黄安","getView");
        return convertView;
    }


    class ViewHolder {
        TextView product;//产品
        ImageView headPortrait;//ͷ头像
        TextView time;//时间

    }

}

