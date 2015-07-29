package com.huntor.mscrm.app.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiUpload;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.Utils;

import java.io.File;

/**
 * 录音控制
 * Created by jh on 2015/6/18.
 */
public class RecordControl implements View.OnTouchListener, View.OnClickListener {
    private String TAG = "RecordControl";
    private TextView textView;
    private Context context;
    private int flag = 1;
    private Handler mHandler = new Handler();
    /******
     * 录音的名字
     ****/
    private String voiceName = "chat.amr";
    private Chronometer timedown;
    private MediaPlayer player;
    /******
     * 弹出录音的view
     ****/
    private View rcChat_popup;
    /******
     * window
     ****/
    private PopupWindow window;
    private SoundMeter mSensor;
    private static final int POLL_INTERVAL = 300;
    /****
     * 总时长
     **/
    private long timeTotalInS = 60;
    /****
     * 剩余时长
     **/
    private long timeLeftInS = 0;
    /******
     * 总共按下去按钮的时间
     **/
    private long time_press;

    private String path_voice;

    private ImageView volume;
    private ImageView phone;
    private TextView cancle;

    public RecordControl(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
        textView.setOnClickListener(this);
        textView.setOnTouchListener(this);
        mSensor = new SoundMeter();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        time_press = event.getEventTime() - event.getDownTime();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            textView.setText("松开 结束");
            int[] location = new int[2];
            //获取在当前窗口内的绝对坐标
            textView.getLocationInWindow(location);
            int btn_rc_Y = location[1];
            int btn_rc_X = location[0];
            if (flag == 1) {
                if (!Environment.getExternalStorageDirectory().exists()) {
                    Utils.toast(context, "SD卡异常");
                    return false;
                }
                if (event.getY() < btn_rc_Y || event.getX() < btn_rc_X) {
                    showPop(v);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                        }
                    }, POLL_INTERVAL);
                    voiceName = System.currentTimeMillis() + ".amr";
                    start(voiceName);
                    initTimer(timeTotalInS);
                    timedown.start();
                    flag = 2;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            textView.setText("按住 说话");
            if (window.isShowing()) {
                timedown.stop();
                if (flag == 2) {
                    disimisPop();
                    stop();
                    flag = 1;
                    if (time_press > 1000) {
                        soundUse(voiceName);
                    }
                } else {
                    stop();
                    flag = 1;
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float getY = event.getY();
            int top = textView.getTop();
            if (getY < top) {
                volume.setVisibility(View.GONE);
                phone.setImageResource(R.drawable.ic_chat_cancle);
                cancle.setText("松开手指，取消发送");
                cancle.setBackgroundResource(R.drawable.shape_textview_backround_cancle_send);
                stop();
                delete(voiceName);
            }
        }
        return false;
    }

    /********
     * 录音时间不能小于1秒
     ***/
    @Override
    public void onClick(View v) {
        if (time_press <= 1000) {
            Utils.toast(context, "录音时间太短");
            // Utils.toast(context, getPath_voice());
        }
    }

    private void soundUse(String voiceName) {
        player = new MediaPlayer();
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.VOICE_CACHE_PATH + voiceName;
        File file = new File(path);
        if (file.exists()) {
            listener.getPath(path);
        }
    }

    private void delete(String voiceName) {
        player = new MediaPlayer();
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.VOICE_CACHE_PATH + voiceName;
        File file = new File(path);
        MyLogger.e(TAG, "松手删除的文件：" + path);
        if (file.exists()) {
            deleteFile(file);
            listener.getPath(path);
        }
    }

    /*****
     * 松开手后删除刚刚录音的文件
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            MyLogger.e(TAG, "文件不存在！");
        }
    }

    /****
     * 长传图片
     *
     * @param path 图片的路径
     * @param type 文件类型 1图片 2音频 3视频
     */
    private void upLoadRecord(String path, String type) {
        HttpRequestController.upload(context, path, type, new HttpResponseListener<ApiUpload.ApiUploadResponse>() {
            @Override
            public void onResult(ApiUpload.ApiUploadResponse response) {
                if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                    String url = response.result.url;
                    String voicePath = "{" + "\"" + "voiceUrl" + "\"" + ":" + "\"" + url + "\"" + "}";
                    Log.d("---voicePath-", voicePath);
                }
            }
        });
    }

    /****
     * 显示录音的对话框
     **/
    private void showPop(View v) {
        rcChat_popup = View.inflate(context, R.layout.view_show_record, null);
        window = new PopupWindow(rcChat_popup, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.showAtLocation(v, Gravity.CENTER, 0, 0);
        timedown = (Chronometer) rcChat_popup.findViewById(R.id.timedown);
        volume = (ImageView) rcChat_popup.findViewById(R.id.volume);
        phone = (ImageView) rcChat_popup.findViewById(R.id.ic_chat_phone);
        cancle = (TextView) rcChat_popup.findViewById(R.id.tv_cancel_send_record);

    }

    /****
     * 隐藏录音对话框
     **/
    private void disimisPop() {
        if (window.isShowing()) {
            window.dismiss();
        }
    }

    private Runnable mSleepTask = new Runnable() {
        @Override
        public void run() {
            stop();
        }
    };
    private Runnable mPollTask = new Runnable() {
        @Override
        public void run() {
            double amp = mSensor.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    private void start(String voiceName) {
        mSensor.start(voiceName);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        mSensor.stop();
    }

    private void updateDisplay(double signalEMA) {
        switch ((int) signalEMA) {
            case 0:
            case 1:
                volume.setImageResource(R.drawable.ic_chat_amp1);
                break;
            case 2:
            case 3:
                volume.setImageResource(R.drawable.ic_chat_amp2);
                break;
            case 4:
            case 5:
            case 6:
                volume.setImageResource(R.drawable.ic_chat_amp3);
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                volume.setImageResource(R.drawable.ic_chat_amp4);
                break;
            default:
                volume.setImageResource(R.drawable.ic_chat_amp1);
                break;
        }
    }

    /*****
     * 录音倒计时
     *
     * @param total
     */
    private void initTimer(long total) {
        this.timeTotalInS = total;
        this.timeLeftInS = total;
        timedown.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (timeLeftInS <= 0) {
                    timedown.stop();
                    stop();
                    rcChat_popup.setVisibility(View.GONE);
                    timedown.setVisibility(View.GONE);
                    return;
                }
                timeLeftInS--;
                refreshTimeLeft();
            }
        });
    }

    private void refreshTimeLeft() {
        this.timedown.setText("倒计时:" + timeLeftInS);
    }

    private GetPathListener listener;

    public void setListener(GetPathListener listener) {
        this.listener = listener;
    }

    public interface GetPathListener {
        public void getPath(String path);
    }


    /****
     * 获取录音文件的路径，以方便调取
     *
     * @return
     */
    public String getPath_voice() {
        return path_voice;
    }

    public void setPath_voice(String path_voice) {
        this.path_voice = path_voice;
    }


    public String getVoiceName() {
        return voiceName;
    }

    /****
     * 设置存储的录音的名字，默认的名字是chat.amr
     *
     * @param voiceName
     */
    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }


    public long getTimeTotalInS() {
        return timeTotalInS;
    }

    /****
     * 设置存储的录音的总时长，默认的是60秒
     *
     * @param timeTotalInS
     */
    public void setTimeTotalInS(long timeTotalInS) {
        this.timeTotalInS = timeTotalInS;
    }
}
