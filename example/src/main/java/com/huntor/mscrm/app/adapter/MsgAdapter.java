package com.huntor.mscrm.app.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.model.MessageRecordModel;
import com.huntor.mscrm.app.model.PullMessageNote;
import com.huntor.mscrm.app.provider.api.ApiMessageRecordDb;
import com.huntor.mscrm.app.provider.api.ApiPullMessageNoteDb;
import com.huntor.mscrm.app.ui.component.MyTextView;
import com.huntor.mscrm.app.ui.gif.AnimatedGifDrawable;
import com.huntor.mscrm.app.ui.gif.AnimatedImageSpan;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.DateFormatUtils;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.Template;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Liuhw on 2015/7/1.
 */
public class MsgAdapter extends BaseAdapter {
    private String TAG = "MsgAdapter_站内信";
    private Context context;
    List<PullMessageNote> list = new ArrayList<PullMessageNote>();

    public MsgAdapter(Context context, List<PullMessageNote> list) {
        this.context = context;
        this.list = list;
        initPopWindow();
        initImgPopWindow();
    }

    public void setList(List<PullMessageNote> list) {
        if (this.list == null) {
            this.list = new ArrayList<PullMessageNote>();
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
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = setHolder(holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String type = list.get(position).type;
        //显示时间
        long time = list.get(position).time;
        String timeFormat = "MM-dd HH:mm ";
        Date date = new Date(time);
        String format = DateFormatUtils.format(date, timeFormat);
        holder.time.setText(format);
        pullMsg(position, holder, type);
        holder.layout.setOnLongClickListener(new popAction(convertView,
                position));
        return convertView;
    }

    public View setHolder(ViewHolder holder) {
        View convertView;
        convertView = LayoutInflater.from(context).inflate(R.layout.msg_lv_item, null);
        holder.pullImgHolder = (ImageView) convertView.findViewById(R.id.image_content_pull);
        holder.pullVoiceHolder = (ImageView) convertView.findViewById(R.id.voice_content_pull);
        holder.pullTextContent = (MyTextView) convertView.findViewById(R.id.tv_content_pull);
        holder.time = (TextView) convertView.findViewById(R.id.tv_time_pull);
        holder.layout = (RelativeLayout) convertView.findViewById(R.id.msg_lv_item_layout);
        return convertView;
    }

    class ViewHolder {
        TextView time;
        MyTextView pullTextContent;
        ImageView pullImgHolder;
        ImageView pullVoiceHolder;
        RelativeLayout layout;
    }

    /**
     * 屏蔽listitem的所有事件
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    private PopupWindow popupWindow;
    private TextView copy, delete;
    private PopupWindow imgPopWindow;
    private ImageView imgPop;

    /**
     * 初始化弹出的pop
     */
    private void initPopWindow() {
        View popView = View.inflate(context, R.layout.chat_item_copy_delete_menu,
                null);
        copy = (TextView) popView.findViewById(R.id.chat_copy_menu);
        delete = (TextView) popView.findViewById(R.id.chat_delete_menu);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置popwindow出现和消失动画
        // popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
    }

    private void initImgPopWindow() {
        View imgPopView = View.inflate(context, R.layout.chat_item_img_pop, null);
        imgPop = (ImageView) imgPopView.findViewById(R.id.img_chat_item_pop);
        imgPopWindow = new PopupWindow(imgPopView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imgPopWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
    }

    /**
     * 每个ITEM中按钮对应的点击动作
     * 出现复制删除的动作
     */
    public class popAction implements View.OnLongClickListener {
        int position;
        View view;


        public popAction(View view, int position) {
            this.position = position;
            this.view = view;

        }

        @Override
        public boolean onLongClick(View v) {
            int[] arrayOfInt = new int[2];
            v.getLocationOnScreen(arrayOfInt);
            int x = arrayOfInt[0];
            int y = arrayOfInt[1];
            showPop(v, x, y, view, position);
            return true;
        }
    }


    private void showPop(View parent, int x, int y, final View view,
                         final int position) {
        popupWindow.showAtLocation(parent, 0, x, y);
        // 获取popwindow焦点
        popupWindow.setFocusable(true);
        // 设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        copy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                // 获取剪贴板管理服务
                ClipboardManager cm = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本数据复制到剪贴板
                cm.setText(list.get(position).content);
            }
        });
        // 删除
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                leftRemoveAnimation(view, position);
                ApiPullMessageNoteDb db = new ApiPullMessageNoteDb(context);
                db.delete(list.get(position).time);
            }
        });
        popupWindow.update();
        if (popupWindow.isShowing()) {

        }
    }

    /**
     * item删除动画
     */
    private void leftRemoveAnimation(final View view, final int position) {
        final Animation animation = (Animation) AnimationUtils.loadAnimation(context, R.anim.chatfrom_remove_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                view.setAlpha(0);
                performDismiss(view, position);
                animation.cancel();
            }
        });

        view.startAnimation(animation);
    }

    /**
     * 在此方法中执行item删除之后，其他的item向上或者向下滚动的动画，并且将position回调到方法onDismiss()中
     *
     * @param dismissView
     * @param dismissPosition
     */
    private void performDismiss(final View dismissView,
                                final int dismissPosition) {
        long mAnimationTime = 150;
        final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();// 获取item的布局参数
        final int originalHeight = dismissView.getHeight();// item的高度

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0)
                .setDuration(mAnimationTime);
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                list.remove(dismissPosition);
                notifyDataSetChanged();
                // 这段代码很重要，因为我们并没有将item从ListView中移除，而是将item的高度设置为0
                // 所以我们在动画执行完毕之后将item设置回来
                ViewHelper.setAlpha(dismissView, 1f);
                ViewHelper.setTranslationX(dismissView, 0);
                ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
                lp.height = originalHeight;
                dismissView.setLayoutParams(lp);
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 这段代码的效果是ListView删除某item之后，其他的item向上滑动的效果
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                dismissView.setLayoutParams(lp);
            }
        });

    }

    /****
     * 接收到的消息
     *
     * @param position
     * @param holder
     * @param type
     */
    private void pullMsg(int position, ViewHolder holder, String type) {
        if (type.equals(Constant.CHAT_TYPE_TEXT)) {
            holder.pullTextContent.setVisibility(View.VISIBLE);
            holder.pullImgHolder.setVisibility(View.GONE);
            holder.pullVoiceHolder.setVisibility(View.GONE);
            String content = list.get(position).content;
            if (content != null && !TextUtils.isEmpty(content)) {
                content = replaceFace(content);
                SpannableStringBuilder handler = handler(holder.pullTextContent, content);
                holder.pullTextContent.setTextColor(Color.BLACK);
                holder.pullTextContent.setMaxLines(100);
                holder.pullTextContent.setTextSize(15);
                holder.pullTextContent.setPadding(10, 20, 10, 10);
                holder.pullTextContent.setText(handler);
            } else {
                holder.pullTextContent.setText("不支持接收该类型的消息");
            }
        } else if (type.equals(Constant.CHAT_TYPE_IMAGE)) {
            holder.pullTextContent.setVisibility(View.GONE);
            holder.pullImgHolder.setVisibility(View.VISIBLE);
            holder.pullVoiceHolder.setVisibility(View.GONE);
            ImageLoader imageLoader = ImageLoader.getInstance();
            String content = list.get(position).content;
            displayImageView(holder, imageLoader, content);
        } else if (type.equals(Constant.CHAT_TYPE_VOICE)) {
            holder.pullTextContent.setVisibility(View.GONE);
            holder.pullImgHolder.setVisibility(View.GONE);
            holder.pullVoiceHolder.setVisibility(View.VISIBLE);
            String content = list.get(position).content;
            MyLogger.w(TAG, "content = " + content);
            holder.pullVoiceHolder.setImageResource(R.drawable.voice_left);
        }
    }

    private String replaceFace(String content) {
        String ret = content;

        String[] replace = new String[Constant.WEIXIN_CHAR.length];
        for (int i = 0; i < Constant.WEIXIN_CHAR.length; i++) {
            replace[i] = "#[weixin/" + replaceNumber(i) + ".png]#";
        }
        if (!TextUtils.isEmpty(content)) {
            Template template = new Template(content, Constant.WEIXIN_CHAR);
            ret = template.apply(replace);
        }
        return ret;
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
                InputStream is = context.getAssets().open(gif);
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
                    sb.setSpan(new ImageSpan(context, BitmapFactory.decodeStream(context.getAssets().open(png))), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return sb;
    }

    public void displayImageView(ViewHolder holder, ImageLoader imageLoader, String content) {
        String imgUrl = "";
        try {
            JSONObject jsonObject = new JSONObject(content);
            imgUrl = jsonObject.getString("pic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        imageLoader.displayImage(imgUrl, holder.pullImgHolder, options);
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.dimension_code_fail_default)
            .showImageOnFail(R.drawable.dimension_code_fail_default)
            .showImageOnLoading(R.drawable.dimension_code_loading_default)
            .build();
}
