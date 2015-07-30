package com.huntor.mscrm.app.adapter;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.Fans;
import com.huntor.mscrm.app.utils.DateFormatUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Admin on 2015/5/22.
 */
public class NewFansAdapter extends MyBaseAdapter {

    private Context context;

    int mType;

    /**
     * 构造方法
     *
     * @param c
     */
    public NewFansAdapter(Context c) {
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
            convertView = inflater.inflate(R.layout.item_member, null);
            //vh.headPortrait = (ImageView) convertView.findViewById(R.id.head_portrait);
            vh.name = (TextView) convertView.findViewById(R.id.member_name);
            vh.time = (TextView) convertView.findViewById(R.id.time);
            //vh.grade = (TextView) convertView.findViewById(R.id.tv_grade);
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
            vh.time.setText(time);
        } else {
            vh.time.setText("");
        }


//        mType = ((Fans) list.get(position)).group;
//        //vh.headPortrait.setImageResource(R.drawable.n_head);
//        //1新增 2普通 3高潜 4已购 5全部
//        if (1 == mType) {
//
//        } else if (2 == mType) {
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.dimension_code_loading_default)
//                    .showImageForEmptyUri(R.drawable.p_head).cacheOnDisc(true)
//                    .showImageOnFail(R.drawable.p_head).cacheInMemory(true).build();
//            String url = null;
//            if (fans.avatar == null) {
//                vh.headPortrait.setImageResource(R.drawable.p_head);
//            } else {
//                url = fans.avatar;
//                imageLoader.displayImage(url, vh.headPortrait, options);
//            }
//            //vh.headPortrait.setImageResource(R.drawable.p_head);
//
//            vh.grade.setText("普通");
//        } else if (3 == mType) {
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.dimension_code_loading_default)
//                    .showImageForEmptyUri(R.drawable.g_head).cacheOnDisc(true)
//                    .showImageOnFail(R.drawable.g_head).cacheInMemory(true).build();
//            String url = null;
//            if (fans.avatar == null) {
//                vh.headPortrait.setImageResource(R.drawable.g_head);
//            } else {
//                url = fans.avatar;
//                imageLoader.displayImage(url, vh.headPortrait, options);
//            }
//            //vh.headPortrait.setImageResource(R.drawable.g_head);
//
//            vh.grade.setText("高潜");
//        } else if (4 == mType) {
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.dimension_code_loading_default)
//                    .showImageForEmptyUri(R.drawable.y_head).cacheOnDisc(true)
//                    .showImageOnFail(R.drawable.y_head).cacheInMemory(true).build();
//            String url = null;
//            if (fans.avatar == null) {
//                vh.headPortrait.setImageResource(R.drawable.y_head);
//            } else {
//                url = fans.avatar;
//                imageLoader.displayImage(url, vh.headPortrait, options);
//            }
//            //vh.headPortrait.setImageResource(R.drawable.y_head);
//            vh.grade.setText("已购");
//        } else {
//
//        }
        return convertView;
    }

    public class ViewHolder {
        TextView name;//姓名
        //ImageView headPortrait;//ͷ头像
        TextView time;//时间
        //TextView grade;//显示等级
    }
}
