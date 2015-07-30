package com.huntor.mscrm.app2.adapter;

import android.app.Service;
import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.utils.DateFormatUtils;
import com.huntor.mscrm.app2.utils.MyApplication;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Admin on 2015/5/18.
 */
public class BuyedUserXdkAdapter extends MyBaseAdapter {
    public String Tag = "BuyedUserXdkAdapter";
    private Context context;

    /**
     * 构造方法
     *
     * @param c
     */
    public BuyedUserXdkAdapter(Context c) {
        super(c);
        this.context = c;
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
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.member_adapter_item, null);
            //vh.headPortrait = (ImageView) convertView.findViewById(R.id.head_portrait);
            vh.name = (TextView) convertView.findViewById(R.id.member_name);
            vh.time = (TextView) convertView.findViewById(R.id.member_time);
            //vh.grade= (TextView) convertView.findViewById(R.id.tv_grade);
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Fans fans = (Fans) list.get(position);
        if (fans.name != null && !("".equals(fans.name))) {
            vh.name.setText(fans.name);
        } else {
            vh.name.setText("暂无");
        }
        //registTime 注册时间
        //subscribeTime 关注时间
        if (fans.subscribeTime != 0) {
            String time = DateFormatUtils.getPassedTime(context, fans.subscribeTime);
            MyLogger.e(Tag, "time=" + time);
            vh.time.setText(time);
        } else {
            vh.time.setText("");
        }
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.dimension_code_loading_default)
//                .showImageForEmptyUri(R.drawable.y_head).cacheOnDisc(true)
//                .showImageOnFail(R.drawable.y_head).cacheInMemory(true).build();
//        String url = null;
//        if (fans.avatar == null) {
//            vh.headPortrait.setImageResource(R.drawable.y_head);
//        } else {
//            url = fans.avatar;
//            imageLoader.displayImage(url, vh.headPortrait, options);
//        }
        //vh.headPortrait.setImageResource(R.drawable.y_head);


        //vh.grade.setText("已购");
        return convertView;
    }

    public class ViewHolder {
        TextView name;//姓名
        //ImageView headPortrait;//ͷ头像
        TextView time;//时间
        //TextView grade;//显示等级
    }
}
