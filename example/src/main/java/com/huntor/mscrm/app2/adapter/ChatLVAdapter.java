package com.huntor.mscrm.app2.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.MessageRecordModel;
import com.huntor.mscrm.app2.model.SendMessage;
import com.huntor.mscrm.app2.model.UploadResult;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiUpload;
import com.huntor.mscrm.app2.net.task.VoiceDownloadTask;
import com.huntor.mscrm.app2.provider.api.ApiMessageRecordDb;
import com.huntor.mscrm.app2.push.PushMessageManager;
import com.huntor.mscrm.app2.ui.ImageActivity;
import com.huntor.mscrm.app2.ui.gif.AnimatedGifDrawable;
import com.huntor.mscrm.app2.ui.gif.AnimatedImageSpan;
import com.huntor.mscrm.app2.utils.*;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("NewApi")
public class ChatLVAdapter extends BaseAdapter {

    private static final String TAG = "ChatLVAdapter";

    private Context mContext;
    private List<MessageRecordModel> list;
    /**
     * 弹出的更多选择框
     */
    private PopupWindow popupWindow;

    private PopupWindow imgPopWindow;
    private ImageView imgPop;

    /**
     * 复制，删除
     */
    private TextView copy, delete;

    private LayoutInflater inflater;
    /**
     * 执行动画的时间
     */
    protected long mAnimationTime = 150;
    private AnimationDrawable leftAnimation;

    private Handler handler;
    private final List<ImageView> views;


    private double rightImgID;
    private double leftImgID;

    public ChatLVAdapter(Context mContext, List<MessageRecordModel> list, Handler handler) {
        super();
        if (mContext != null) {
            this.mContext = mContext;
        } else {
            throw new IllegalStateException("Contect must not null");
        }
        if (handler != null) {
            this.handler = handler;
        }
        this.list = list;
        inflater = LayoutInflater.from(mContext);
        views = new ArrayList<ImageView>();
        initPopWindow();
        initImgPopWindow();
//        initAnimation();
    }

//    private void initAnimation() {
//        leftAnimation = (Animation) mContext.getResources().getAnimation(R.anim.voice_play_left);
//    }

    public void setList(List<MessageRecordModel> list) {
        if (this.list == null) {
            this.list = new ArrayList<MessageRecordModel>();
        }
        if (list != null) {
            this.list = list;
        }
    }

    private List<String> paths;

    public void setVoicePath(List<String> paths) {
        if (this.paths == null) {
            this.paths = new ArrayList<String>();
        }
        if (paths != null) {
            this.paths = paths;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MessageRecordModel getItem(int position) {
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
        long time = list.get(position).timestamp;
        String timeFormat = "MM-dd HH:mm ";
        Date date = new Date(time);
        String format = DateFormatUtils.format(date, timeFormat);
        holder.time.setText(format);

        if (list.get(position).sendOrReceive == 0) {
            // 收到消息 from显示
//            getFrom(position, holder);
            setFrom(position, holder, type);
        } else {
            // 发送消息 to显示
//            setTo(position, holder);
            setTo(position, holder, type);
        }
        setListener(position, convertView, holder);
        return convertView;
    }

    public void setListener(int position, View convertView, ViewHolder holder) {
        holder.fromTextContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        holder.fromVoiceHolder.setOnClickListener(new VoiceAction(position));
        // holder.toVoiceHolder.setOnClickListener(new VoiceAction(position));

        holder.fromImgHolder.setOnClickListener(new ImgClickAction(position, convertView));
        holder.toImgHolder.setOnClickListener(new ImgClickAction(position, convertView));
        rightImgID = holder.toImgHolder.getId();
        leftImgID = holder.fromImgHolder.getId();

        holder.toContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        holder.failHolder.setOnClickListener(new FailAction(position));


        //长点击事件
        // 设置+按钮点击效果
        holder.fromTextContent.setOnLongClickListener(new popAction(convertView,
                position, list.get(position).sendOrReceive));
        holder.toContent.setOnLongClickListener(new popAction(convertView,
                position, list.get(position).sendOrReceive));
        holder.fromImgHolder.setOnLongClickListener(new popAction(convertView,
                position, list.get(position).sendOrReceive));
        holder.toImgHolder.setOnLongClickListener(new popAction(convertView,
                position, list.get(position).sendOrReceive));
        holder.fromVoiceHolder.setOnLongClickListener(new popAction(convertView,
                position, list.get(position).sendOrReceive));
        holder.toVoiceHolder.setOnLongClickListener(new popAction(convertView,
                position, list.get(position).sendOrReceive));
    }

    public void setTo(final int position, final ViewHolder holder, String type) {
        try {
            holder.fromContainer.setVisibility(View.GONE);
            holder.toContent.setVisibility(View.VISIBLE);
            holder.sendingHolder.setVisibility(View.VISIBLE);
            holder.failHolder.setVisibility(View.GONE);

            if (type != null) {
                if (type.equals(Constant.CHAT_TYPE_TEXT)) {
                    holder.toContent.setVisibility(View.VISIBLE);
                    holder.toImgHolder.setVisibility(View.GONE);
                    holder.toVoiceHolder.setVisibility(View.GONE);
                    String content = list.get(position).content;
                    content = replaceFace(content);
                    if (TextUtils.isEmpty(content)) {
                        content = "";
                    }
                    SpannableStringBuilder handler = handler(holder.toContent, content);
                    holder.toContent.setText(handler);
                    holder.sendingHolder.setVisibility(View.VISIBLE);
                    int successOrFail = list.get(position).successOrFail;
                    MyLogger.e(TAG, "position=" + position + "successOrFail=" + successOrFail);
                    if (successOrFail == 1) {//成功
                        holder.sendingHolder.setVisibility(View.GONE);
                        holder.failHolder.setVisibility(View.GONE);
                    } else if (successOrFail == 0) {//失败
                        holder.sendingHolder.setVisibility(View.GONE);
                        holder.failHolder.setVisibility(View.VISIBLE);
                    } else {//发送中
                        holder.sendingHolder.setVisibility(View.VISIBLE);
                        holder.failHolder.setVisibility(View.GONE);
                    }
                } else if (type.equals(Constant.CHAT_TYPE_IMAGE)) {
                    holder.toContent.setVisibility(View.GONE);
                    holder.toImgHolder.setVisibility(View.VISIBLE);
                    holder.toVoiceHolder.setVisibility(View.GONE);
                    String content = list.get(position).content;
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    displayImageViewTo(holder, imageLoader, content);
                    int successOrFail = list.get(position).successOrFail;
                    if (successOrFail == 1) {//成功
                        holder.sendingHolder.setVisibility(View.GONE);
                        holder.failHolder.setVisibility(View.GONE);
                    } else if (successOrFail == 0) {//失败
                        holder.sendingHolder.setVisibility(View.GONE);
                        holder.failHolder.setVisibility(View.VISIBLE);
                    } else {//发送中
                        holder.sendingHolder.setVisibility(View.VISIBLE);
                        holder.failHolder.setVisibility(View.GONE);
                    }
                } else if (type.equals(Constant.CHAT_TYPE_VOICE)) {
                    holder.toContent.setVisibility(View.GONE);
                    holder.toImgHolder.setVisibility(View.GONE);
                    holder.toVoiceHolder.setVisibility(View.VISIBLE);
                    final String content = list.get(position).content;
                    holder.toVoiceHolder.setImageResource(R.drawable.voice_playing_right_33);
                    holder.toVoiceHolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.toVoiceHolder.setImageResource(R.drawable.voice_play_right1);
                            playMusicRight(content, holder.toVoiceHolder);
                        }
                    });
                    int successOrFail = list.get(position).successOrFail;
                    if (successOrFail == 1) {//成功
                        holder.sendingHolder.setVisibility(View.GONE);
                        holder.failHolder.setVisibility(View.GONE);
                    } else if (successOrFail == 0) {//失败
                        holder.sendingHolder.setVisibility(View.GONE);
                        holder.failHolder.setVisibility(View.VISIBLE);
                    } else {//发送中
                        holder.sendingHolder.setVisibility(View.VISIBLE);
                        holder.failHolder.setVisibility(View.GONE);
                    }
                }
            } else {
                MyLogger.e(TAG, "Message not have type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFrom(int position, ViewHolder holder, String type) {
        holder.toContent.setVisibility(View.GONE);
        holder.toImgHolder.setVisibility(View.GONE);
        holder.toVoiceHolder.setVisibility(View.GONE);
        holder.sendingHolder.setVisibility(View.GONE);
        holder.failHolder.setVisibility(View.GONE);
        holder.fromContainer.setVisibility(View.VISIBLE);
        if (type.equals(Constant.CHAT_TYPE_TEXT)) {
            holder.fromTextContent.setVisibility(View.VISIBLE);
            holder.fromImgHolder.setVisibility(View.GONE);
            holder.fromVoiceHolder.setVisibility(View.GONE);
            String content = list.get(position).content;
            if (content != null && !TextUtils.isEmpty(content)) {
                content = replaceFace(content);
                MyLogger.w(TAG, "content = " + content);
                SpannableStringBuilder handler = handler(holder.fromTextContent, content);
                holder.fromTextContent.setText(handler);
            } else {
                holder.fromTextContent.setText("不支持接收该类型的消息");
            }
        } else if (type.equals(Constant.CHAT_TYPE_IMAGE)) {
            holder.fromImgHolder.setVisibility(View.VISIBLE);
            holder.fromTextContent.setVisibility(View.GONE);
            holder.fromVoiceHolder.setVisibility(View.GONE);
            ImageLoader imageLoader = ImageLoader.getInstance();
            String content = list.get(position).content;
            MyLogger.w(TAG, "content = " + content);
            displayImageView(holder, imageLoader, content);
        } else if (type.equals(Constant.CHAT_TYPE_PUSHVOICE)) {
            holder.fromVoiceHolder.setVisibility(View.VISIBLE);
            holder.fromTextContent.setVisibility(View.GONE);
            holder.fromImgHolder.setVisibility(View.GONE);
            String content = list.get(position).content;
            MyLogger.w(TAG, "content22 = " + content);
            holder.fromVoiceHolder.setImageResource(R.drawable.voice_left);
        }
    }

    public View setHolder(ViewHolder holder) {
        View convertView;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.chat_lv_item, null);
        holder.fromContainer = (ViewGroup) convertView.findViewById(R.id.chart_text_from_container);
        holder.fromImgHolder = (ImageView) convertView.findViewById(R.id.img_chat_from_container);
        holder.fromVoiceHolder = (ImageView) convertView.findViewById(R.id.view_chat_from_voice);
        holder.fromTextContent = (TextView) convertView.findViewById(R.id.text_chat_from_text);
        holder.toContent = (TextView) convertView.findViewById(R.id.chatto_content);
        holder.toImgHolder = (ImageView) convertView.findViewById(R.id.img_chat_to_container);
        holder.toVoiceHolder = (ImageView) convertView.findViewById(R.id.view_chat_to_voice);
        holder.time = (TextView) convertView.findViewById(R.id.chat_time);
        holder.sendingHolder = (ProgressBar) convertView.findViewById(R.id.process_message_sending);
        holder.failHolder = (ImageView) convertView.findViewById(R.id.img_message_send_fail);
        return convertView;
    }

    public class FailAction implements View.OnClickListener {

        private int position;
        //  PushMessageManager messageManager = PushMessageManager.getInstance(mContext);

        public FailAction(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            final MessageRecordModel model = list.get(position);
            //在页面上删除本记录
            //  Message msg = new Message();
            //  msg.what = 3;
            //  msg.obj = position;
            //  handler.sendMessage(msg);

            ChatLVAdapter.this.list.get(position).successOrFail = 3;

            notifyDataSetChanged();

            model.successOrFail = 2;
            final SendMessage sendMessage = new SendMessage();
            sendMessage.content = model.content;
            sendMessage.groupId = model.groupId;
            sendMessage.socialId = PreferenceUtils.getInt(mContext, Constant.PREFERENCES_SOCIAL_ID, 0);
            sendMessage.eid = model.eid;
            sendMessage.fid = model.fid;
            sendMessage.recordId = model.msgId;
            sendMessage.timestamp = model.timestamp;
            sendMessage.type = model.type;
            sendMessage.groupId = ApiMessageRecordDb.getGroupID(mContext, sendMessage.fid);
            MyLogger.e(TAG, "model=" + model.toString());
            MyLogger.e(TAG, "sendMessage=" + sendMessage.toString());
            MyLogger.e(TAG, "sendMessage.type = " + sendMessage.type);
            if (sendMessage.type.equals(Constant.CHAT_TYPE_TEXT)) {
                PushMessageManager messageManager = PushMessageManager.getInstance(mContext);
                messageManager.sendEmpMessage("chat", sendMessage, new PushMessageManager.Rck() {
                    @Override
                    public void onResult(boolean status, String errorInfo, int id) {
                        MyLogger.i(TAG, "ChatActivity(sendEmpMessage.onResult) obj = " + status + "errorInfo=" + errorInfo);
                        list = ApiMessageRecordDb.getMessage(mContext, model.fid, 10, 1);
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                });
            }
            if (sendMessage.type.equals(Constant.CHAT_TYPE_VOICE)) {
                HttpRequestController.upload(mContext, sendMessage.content, "2", new HttpResponseListener<ApiUpload.ApiUploadResponse>() {
                    @Override
                    public void onResult(ApiUpload.ApiUploadResponse response) {
                        String voicePath = "{\"voiceUrl\":\"" + response.result.url + "\"}";
                        PushMessageManager messageManager = PushMessageManager.getInstance(mContext);
                        sendMessage.content += "####" + voicePath;
                        messageManager.sendEmpMessage("chat", sendMessage, new PushMessageManager.Rck() {
                            @Override
                            public void onResult(boolean status, final String errorInfo, int msgId) {
                                if (!status && "socket is null !".equals(errorInfo)) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.toast(mContext, "-" + errorInfo + "-");
                                        }
                                    });
                                }
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = status;
                                handler.sendMessage(msg);
                            }
                        });
                    }
                });
            }
            if (sendMessage.type.equals(Constant.CHAT_TYPE_IMAGE)) {
                HttpRequestController.upload(mContext, sendMessage.content, "2", new HttpResponseListener<ApiUpload.ApiUploadResponse>() {
                    @Override
                    public void onResult(ApiUpload.ApiUploadResponse response) {
                        if (response.result == null) {
                            Utils.toast(mContext, "网络异常");
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                            return;
                        }
                        String url = response.result.url;
                        String content = "{\"" + "picUrl\"" + ":" + "\"" + url + "\"}";
                        sendMessage.content = content;
                        PushMessageManager messageManager = PushMessageManager.getInstance(mContext);
                        messageManager.sendEmpMessage("chat", sendMessage, new PushMessageManager.Rck() {
                            @Override
                            public void onResult(boolean status, final String errorInfo, int msgId) {
                                if (!status && "socket is null !".equals(errorInfo)) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.toast(mContext, "-" + errorInfo + "-");
                                        }
                                    });
                                }
                                Message msg = new Message();
                                msg.what = 2;
                                msg.obj = status;
                                handler.sendMessage(msg);
                            }
                        });
                    }
                });
            }

        }
    }

    public String getMediaPath() {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.VOICE_CACHE_PATH;
        } else {
            path = Environment.getDataDirectory().getAbsolutePath() + Constant.VOICE_CACHE_PATH;
        }
        File fil = new File(path);
        if (!fil.exists()) {
            fil.mkdirs();
        }
        return path;
    }

    private MediaPlayer mMediaPlayer = new MediaPlayer();

    private void playMusic(String name, final ImageView imageView) {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            File file = new File(name);
            FileInputStream fis = new FileInputStream(file);
            mMediaPlayer.setDataSource(fis.getFD());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            int duration = mMediaPlayer.getDuration();
            MyLogger.i(TAG, "name: " + name + "\n" + "duration:" + duration);

            leftAnimation = (AnimationDrawable) imageView.getDrawable();
            leftAnimation.setOneShot(false);
            leftAnimation.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.stop();
                    leftAnimation.stop();
                    imageView.clearAnimation();
                    imageView.setImageResource(R.drawable.voice_left);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ImageView> imageViews = new ArrayList<ImageView>();

    private void playMusicRight(String name, final ImageView imageView) {
        if (name != null) {
            imageViews.add(imageView);
            for (ImageView view : imageViews) {
                view.clearAnimation();
                view.setImageResource(R.drawable.voice_playing_right_33);
                imageView.setImageResource(R.drawable.voice_play_right1);
            }
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                File file = new File(name);
                FileInputStream fis = new FileInputStream(file);
                mMediaPlayer.setDataSource(fis.getFD());
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                leftAnimation = (AnimationDrawable) imageView.getDrawable();
                leftAnimation.setOneShot(false);
                leftAnimation.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mMediaPlayer.stop();
                        leftAnimation.stop();
                        imageView.clearAnimation();
                        imageView.setImageResource(R.drawable.voice_playing_right_33);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopMusic() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void displayImageView(ViewHolder holder, ImageLoader imageLoader, String content) {
        String imgUrl = "";
        try {
            JSONObject jsonObject = new JSONObject(content);
            imgUrl = jsonObject.getString("pic");
        } catch (JSONException e) {
            e.printStackTrace();
            imgUrl = content;
        }
        imageLoader.displayImage(imgUrl, holder.fromImgHolder, options);
    }

    public void displayImageViewTo(ViewHolder holder, ImageLoader imageLoader, String content) {
        String imgUrl = "";
        MyLogger.e(TAG, "下拉刷新:" + content);
        try {
            JSONObject jsonObject = new JSONObject(content);
            imgUrl = jsonObject.getString("picUrl");
        } catch (JSONException e) {
            e.printStackTrace();
            imgUrl = content;
        }
        imageLoader.displayImage(imgUrl, holder.toImgHolder, options);
    }

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.dimension_code_fail_default)
            .showImageOnFail(R.drawable.dimension_code_fail_default)
            .showImageOnLoading(R.drawable.dimension_code_loading_default)
            .build();

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

    class ViewHolder {
        TextView fromTextContent, toContent, time;
        ViewGroup fromContainer;
        ImageView fromImgHolder, toImgHolder;
        ImageView fromVoiceHolder, toVoiceHolder;
        ImageView failHolder;
        ProgressBar sendingHolder;
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

    /**
     * 初始化弹出的pop
     */
    private void initPopWindow() {
        View popView = inflater.inflate(R.layout.chat_item_copy_delete_menu,
                null);
        copy = (TextView) popView.findViewById(R.id.chat_copy_menu);
        delete = (TextView) popView.findViewById(R.id.chat_delete_menu);
        popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置popwindow出现和消失动画
        // popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
    }

    private void initImgPopWindow() {
        View imgPopView = inflater.inflate(R.layout.chat_item_img_pop, null);
        imgPop = (ImageView) imgPopView.findViewById(R.id.img_chat_item_pop);
        imgPopWindow = new PopupWindow(imgPopView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        imgPopWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

    }

    public void showImgPop(View parent, int x, int y, final View view,
                           final int position) {
        imgPopWindow.showAtLocation(parent, Gravity.TOP | Gravity.LEFT, x, y);
        imgPopWindow.setFocusable(true);
        imgPopWindow.setOutsideTouchable(false);
        String content = list.get(position).content;
        String imgUrl = "";
        try {
            JSONObject jsonObject = new JSONObject(content);
            if (parent.getId() == leftImgID) {
                imgUrl = jsonObject.getString("pic");
            } else if (parent.getId() == rightImgID) {
                imgUrl = jsonObject.getString("picUrl");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            imgUrl = content;
        }


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imgUrl, imgPop, options);
        imgPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgPopWindow.isShowing()) {
                    imgPopWindow.dismiss();
                }
            }
        });
        imgPopWindow.update();
    }

    /**
     * 显示popWindow
     */
    public void showPop(View parent, int x, int y, final View view,
                        final int position, final int fromOrTo) {
        // 设置popwindow显示位置
        popupWindow.showAtLocation(parent, 0, x, y);
        // 获取popwindow焦点
        popupWindow.setFocusable(true);
        // 设置popwindow如果点击外面区域，便关闭。
        popupWindow.setOutsideTouchable(true);
        // 为按钮绑定事件
        // 复制
        copy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                // 获取剪贴板管理服务
                ClipboardManager cm = (ClipboardManager) mContext
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
                if (fromOrTo == 0) {
                    // from
                    leftRemoveAnimation(view, position);
                } else if (fromOrTo == 1) {
                    // to
                    rightRemoveAnimation(view, position);
                }
                ApiMessageRecordDb messageRecordDb = new ApiMessageRecordDb(mContext);
                messageRecordDb.delete(list.get(position).msgId);
            }
        });
        popupWindow.update();
        if (popupWindow.isShowing()) {

        }
    }

    public class VoiceAction implements View.OnClickListener, VoiceDownloadTask.CallBack {
        private int position;
        private ImageView fromImageView;

        public VoiceAction(int position) {
//            MyLogger.w(TAG, "onClick");
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            fromImageView = (ImageView) v.findViewById(R.id.view_chat_from_voice);
            views.add(fromImageView);
            for (ImageView view : views) {
                view.clearAnimation();
                view.setImageResource(R.drawable.voice_left);
                fromImageView.setImageResource(R.drawable.voice_play_left);
            }
            String type = list.get(position).type;
            String content = list.get(position).content;
            MyLogger.w(TAG, "录音文件" + content);
            String url;
            String mediaId;
            String format;
            if (type.equals(Constant.CHAT_TYPE_PUSHVOICE)) {
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    mediaId = jsonObject.getString("mediaId");
                    url = jsonObject.getString("url");
                    format = jsonObject.getString("format");
                    File file = new File(getMediaPath(), mediaId + "." + format);
                    if (file.exists()) {
                        MyLogger.w(TAG, "file.exists");
                        playMusic(getMediaPath() + "/" + mediaId + "." + format, fromImageView);
                    } else {
                        MyLogger.w(TAG, "file is  not exists");
                        new VoiceDownloadTask(this).execute(url, mediaId, format);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onResult(String name) {
            playMusic(getMediaPath() + "/" + name, fromImageView);
        }
    }

    public class ImgClickAction implements View.OnClickListener {
        int position;
        View view;

        public ImgClickAction(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public void onClick(View v) {


            String content = list.get(position).content;
            String imgUrl = "";
            try {
                JSONObject jsonObject = new JSONObject(content);
                if (v.getId() == leftImgID) {
                    imgUrl = jsonObject.getString("pic");
                } else if (v.getId() == rightImgID) {
                    imgUrl = jsonObject.getString("picUrl");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                imgUrl = content;
            }

            Log.e("图片地址", "图片地址＝＝" + imgUrl);

            Intent intent = new Intent();
            intent.setClass(mContext, ImageActivity.class);
            intent.putExtra("url",imgUrl);

            mContext.startActivity(intent);

          /*  int[] arrayOfInt = new int[2];
            // 获取点击按钮的坐标
            v.getLocationOnScreen(arrayOfInt);
            int x = arrayOfInt[0];
            int y = arrayOfInt[1];
            // System.out.println("x: " + x + " y:" + y + " w: " +
            // v.getMeasuredWidth() + " h: " + v.getMeasuredHeight() );
            showImgPop(v, x, y, view, position);*/


        }
    }

    /**
     * 每个ITEM中more按钮对应的点击动作
     * 出现复制删除的动作
     */
    public class popAction implements OnLongClickListener {
        int position;
        View view;
        int fromOrTo;

        public popAction(View view, int position, int fromOrTo) {
            this.position = position;
            this.view = view;
            this.fromOrTo = fromOrTo;
        }

        @Override
        public boolean onLongClick(View v) {
            int[] arrayOfInt = new int[2];
            // 获取点击按钮的坐标
            v.getLocationOnScreen(arrayOfInt);
            int x = arrayOfInt[0];
            int y = arrayOfInt[1];
            // System.out.println("x: " + x + " y:" + y + " w: " +
            // v.getMeasuredWidth() + " h: " + v.getMeasuredHeight() );
            showPop(v, x, y, view, position, fromOrTo);
            return true;
        }
    }

    /**
     * item删除动画
     */
    private void rightRemoveAnimation(final View view, final int position) {
        final Animation animation = (Animation) AnimationUtils.loadAnimation(
                mContext, R.anim.chatto_remove_anim);
        animation.setAnimationListener(new AnimationListener() {
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
     * item删除动画
     */
    private void leftRemoveAnimation(final View view, final int position) {
        final Animation animation = (Animation) AnimationUtils.loadAnimation(mContext, R.anim.chatfrom_remove_anim);
        animation.setAnimationListener(new AnimationListener() {
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

}
