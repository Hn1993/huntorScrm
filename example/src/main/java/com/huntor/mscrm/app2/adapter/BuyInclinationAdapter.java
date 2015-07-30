package com.huntor.mscrm.app2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/5/8.
 */
public class BuyInclinationAdapter extends BaseAdapter {
    // 上下文对象
    protected Context context;
    // 布局填充器
    protected LayoutInflater mInflater;
    private List<Product> mList;
    /*
     * 构造方法
     *
     * @param c
     */
    public BuyInclinationAdapter(Context context,List<Product> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        setData(list);

    }

    /**
     * 设置产品adapter数据
     * @param list
     */
    public void setData(List<Product> list){
        if(mList == null){
            mList= new ArrayList<Product>();
        }
        if(list != null){
            mList.clear();
            mList.addAll(list);
        }

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Product getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_buyinclination_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.productTv = (TextView) convertView.findViewById(R.id.buyinclination_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product product = getItem(position);
        if(product==null){ //如果产品名为空则显示暂无
            viewHolder.productTv.setText(R.string.no);
        }else{
            viewHolder.productTv.setText(product.name);
        }
        return convertView;
    }


    class ViewHolder {
        TextView productTv;//产品名
    }

}
