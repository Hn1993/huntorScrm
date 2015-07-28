package com.huntor.scrm.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.huntor.scrm.R;
import com.huntor.scrm.model.Contact;
import com.huntor.scrm.ui.gif.AnimatedGifDrawable;
import com.huntor.scrm.ui.gif.AnimatedImageSpan;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.DateFormatUtils;
import com.huntor.scrm.utils.MyLogger;
import com.huntor.scrm.utils.Template;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IDEA
 * User : SL
 * on 2015/5/14 0014
 * 18:15.
 */
public class ContactListAdapter extends BaseAdapter {
    private String TAG = getClass().getName();

    private Context mContext;
    private LayoutInflater inflater;
    private List<Contact> data;

    public ContactListAdapter(Context context, List<Contact> data) {
        if (context != null) {
            mContext = context;
            this.data = data;
            inflater = LayoutInflater.from(context);
        } else {
            throw new IllegalStateException("Context must not be null");
        }
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Contact getItem(int position) {
        return data != null ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_chat_contact_list, parent, false);
            holder.headHolder = (ImageView) convertView.findViewById(R.id.img_item_chat_contact_list_head);
            holder.nameHolder = (TextView) convertView.findViewById(R.id.text_item_chat_contact_list_name);
            holder.wordHolder = (TextView) convertView.findViewById(R.id.text_item_chat_contact_list_last_word);
            holder.timeHolder = (TextView) convertView.findViewById(R.id.text_item_chat_contact_list_last_time);
            holder.numberHolder = (TextView) convertView.findViewById(R.id.text_item_chat_contact_number_unread);
            holder.gradeHolder = (TextView) convertView.findViewById(R.id.img_item_chat_contact_list_grade);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = getItem(position);
        int numberUnread = contact.numberUnread;
        holder.numberHolder.setText(numberUnread + "");
        if (numberUnread > 0) {
            holder.numberHolder.setVisibility(View.VISIBLE);
        } else {
            holder.numberHolder.setVisibility(View.GONE);
        }

        int grade = contact.grade;//所在固定分组 2普通 3高潜 4已购
        if (grade == 2) {
            holder.gradeHolder.setVisibility(View.VISIBLE);
            holder.gradeHolder.setText(mContext.getResources().getString(R.string.user_grade_normal));
        } else if (grade == 3) {
            holder.gradeHolder.setVisibility(View.VISIBLE);
            holder.gradeHolder.setText(mContext.getResources().getString(R.string.user_grade_high));
        } else if (grade == 4) {
            holder.gradeHolder.setVisibility(View.VISIBLE);
            holder.gradeHolder.setText(mContext.getResources().getString(R.string.user_grade_buyed));
        } else {
            holder.gradeHolder.setVisibility(View.GONE);
        }

        String imgHeadUrl = contact.imgHead;
        ImageLoader.getInstance().displayImage(imgHeadUrl, holder.headHolder, options);
        String name = contact.name;
        String lastWord = contact.lastWord;
        long lastTime = contact.lastTime;
        String type = contact.type;
        MyLogger.e(TAG, "消息类型type" + type);//后台规定的语音消息类型（客户端发送的是3，接收的是5）
        if (type.equals(Constant.CHAT_TYPE_VOICE) || type.equals("5")) {
            lastWord = "[语音]";
            holder.wordHolder.setText(lastWord);
        } else if (type.equals(Constant.CHAT_TYPE_IMAGE)) {
            lastWord = "[图片]";
            holder.wordHolder.setText(lastWord);
        } else if (type.equals(Constant.CHAT_TYPE_TEXT)) {
            lastWord = replaceFace(lastWord);
            SpannableStringBuilder handler = handler(holder.wordHolder, lastWord);
            holder.wordHolder.setText(handler);
        }
        String time = DateFormatUtils.getPassedTime(mContext, lastTime);
        holder.nameHolder.setText(name);
        holder.timeHolder.setText(time);

        return convertView;
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.chat_contact_list_default_head)
            .showImageOnFail(R.drawable.chat_contact_list_default_head)
            .showImageOnLoading(R.drawable.chat_contact_list_default_head)
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .displayer(new RoundedBitmapDisplayer(15))
            .build();

    private String replaceFace(String content) {
        String[] replace = new String[Constant.WEIXIN_CHAR.length];
        for (int i = 0; i < replace.length; i++) {
            replace[i] = "#[weixin/" + replaceNumber(i) + ".png]#";
        }
        if (!TextUtils.isEmpty(content)) {
            Template template = new Template(content, Constant.WEIXIN_CHAR);
            return template.apply(replace);
        }
        return "";
    }

    private String replaceNumber(int number) {
        if (number < 10) {
            return "00" + Integer.toString(number);
        } else if (number < 100) {
            return "0" + Integer.toString(number);
        } else {
            return Integer.toString(number);
        }
    }

    /**
     * 根据数据中的本地表情地址替换本地表情图片
     *
     * @param gifTextView
     * @param content
     * @return
     */
    private SpannableStringBuilder handler(final TextView gifTextView, String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "(\\#\\[weixin/)\\d{3}(.png\\]\\#)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();
            try {
                String num = tempText.substring("#[weixin/".length(), tempText.length() - ".png]#".length());
                String gif = "weixin/gif/" + num + ".gif";
                /**
                 * 如果open这里不抛异常说明存在gif，则显示对应的gif
                 * 否则说明gif找不到，则显示png
                 * */
                InputStream is = mContext.getAssets().open(gif);
                sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is, new AnimatedGifDrawable.UpdateListener() {
                            @Override
                            public void update() {
                                gifTextView.postInvalidate();
                            }
                        })), m.start(), m.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                is.close();
            } catch (Exception e) {
                String png = tempText.substring("#[".length(), tempText.length() - "]#".length());
                try {
                    sb.setSpan(new ImageSpan(mContext, BitmapFactory.decodeStream(mContext.getAssets().open(png))), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return sb;
    }

    private class ViewHolder {
        public ImageView headHolder;
        public TextView numberHolder;
        public TextView nameHolder;
        public TextView wordHolder;
        public TextView timeHolder;
        public TextView gradeHolder;
    }
}
