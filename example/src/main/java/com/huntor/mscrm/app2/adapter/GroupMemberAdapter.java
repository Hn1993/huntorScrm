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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cary.xi on 2015/5/7.
 */
public class GroupMemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<Fans> mData;
    private RemoveFansCallBack mCallBack;

    public GroupMemberAdapter(Context context, List<Fans> data, RemoveFansCallBack callBack) {
        this.mContext = context;
        this.mData = data;
        this.mCallBack = callBack;
    }

    /**
     * 上拉加载 往后追加数据 而不是清空
     *
     * @param list
     */

    public void addData(List<Fans> list) {
        mData = mData != null ? mData : new ArrayList<Fans>();
        mData.addAll(list);
    }


    public void setData(List<Fans> list){
        if(mData != null){
            mData.clear();
            for(Fans fans:list){
                mData.add(fans);
            }
            Log.i("添加人数", "mData mData.size" + mData.size());
        }else{
            mData = new ArrayList<Fans>();
            mData.addAll(list);
        }
    }


    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Fans getItem(int position) {
        return mData != null ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        ViewHolder vh = null;
        if (null == convertView) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_group_member, null);

            vh.headImage = (ImageView) convertView.findViewById(R.id.head_img);
            vh.memberName = (TextView) convertView.findViewById(R.id.member_name);
            vh.time = (TextView) convertView.findViewById(R.id.time);
            vh.remove = (ImageView) convertView.findViewById(R.id.remove);
            vh.remove.setOnClickListener(new View.OnClickListener() {//处理点击事件
                @Override
                public void onClick(View v) {
                    if (null != mCallBack) {
                        mCallBack.removeFans(mData.get(position).id);
                    }
                }
            });
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Fans fans = getItem(position);
        String name = fans.nickName;
        long registTime = fans.registTime;
        int group = fans.group;

        Log.i("GroupMemberFragment", "group = " + group);

        // 2普通 3高潜 4已购
        switch (group) {

            case 2:
                vh.headImage.setImageResource(R.drawable.p_head);
                break;
            case 3:
                vh.headImage.setImageResource(R.drawable.g_head);
                break;
            case 4:
                vh.headImage.setImageResource(R.drawable.y_head);
                break;
            default:
                vh.headImage.setImageResource(R.drawable.m_head);
                break;

        }

        vh.memberName.setText(name);
        String time = DateFormatUtils.getPassedTime(mContext, registTime);
        vh.time.setText(time);

        return convertView;
    }


    class ViewHolder {
        ImageView headImage;
        TextView memberName;
        TextView enterType;
        TextView time;
        ImageView remove;
    }

    /**
     * 移除粉丝回调
     */
    public interface RemoveFansCallBack {
        /**
         * @param fansId 粉丝id
         */
        public void removeFans(int fansId);
    }

}
