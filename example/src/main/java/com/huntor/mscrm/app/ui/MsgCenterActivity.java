package com.huntor.mscrm.app.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.adapter.EmptyAdapter;
import com.huntor.mscrm.app.adapter.MsgAdapter;
import com.huntor.mscrm.app.model.MessageRecordModel;
import com.huntor.mscrm.app.model.PullMessageNote;
import com.huntor.mscrm.app.provider.api.ApiMessageRecordDb;
import com.huntor.mscrm.app.provider.api.ApiPullMessageNoteDb;
import com.huntor.mscrm.app.push.PushMessageManager;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.NotificationUtils;
import com.huntor.mscrm.app.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;


public class MsgCenterActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "MsgCenterActivity_站内信";
    private Context context;
    private RelativeLayout layout_back;
    private ListView lv_msg;
    private PushMessageManager messageManager;
    private int fan_id = -1;
    private int groupId;
    private TextView deletAllMsg;
    private List<PullMessageNote> infos;
    private MsgAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PullMessageNote model = (PullMessageNote) msg.obj;
                    infos.add(model);
                    infos = ApiPullMessageNoteDb.getMsgList(context);
                    adapter = new MsgAdapter(context, infos);
                    lv_msg.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    lv_msg.setSelection(infos.size());
                    break;
                case 2:
                    infos = (List<PullMessageNote>) msg.obj;
                    adapter = new MsgAdapter(context, infos);
                    lv_msg.setAdapter(adapter);
                    break;
                case 3:
                    lv_msg.setAdapter(new EmptyAdapter(context));
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_center);
        Constant.notecount = 0;
        context = MsgCenterActivity.this;
        setTitle(getResources().getString(R.string.msg_center));
        layout_back = (RelativeLayout) this.findViewById(R.id.img_left_corner_msgcenter);
        layout_back.setOnClickListener(this);
        deletAllMsg = (TextView) this.findViewById(R.id.btn_delet_allmsg);
        deletAllMsg.setOnClickListener(this);
        deletAllMsg.setVisibility(View.GONE);
        lv_msg = (ListView) this.findViewById(R.id.list_msg_detail);
        infos = new ArrayList<PullMessageNote>();
        initIntentData();
        int dest = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
        infos = ApiPullMessageNoteDb.getMsgList(context, dest);
        MyLogger.e(TAG, infos.toString());
        if (infos != null) {
            if (infos.size() > 0) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = infos;
                handler.sendMessage(msg);
                MyLogger.i(TAG, "有站内信");
            } else {
                MyLogger.i(TAG, "无站内信");
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }

        }
        messageManager = PushMessageManager.getInstance(this);
        MyLogger.i(TAG, "注册");
        messageManager.registerOnReceivedPushMessageListener(opl);
    }

    /**
     * 初始化由列表进入聊天界面时传递的数据
     */
    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            fan_id = intent.getIntExtra(Constant.CHAT_CONTACT_FAN_ID, -1);
            fan_id = 7842563;
        }
    }

    private PushMessageManager.OnPullMessageNoteListener opl = new PushMessageManager.OnPullMessageNoteListener() {
        @Override
        public void OnPullMessageNote(Object message) {
            PullMessageNote pushMessage = (PullMessageNote) message;
            MyLogger.i(TAG, "接收消息");
            PullMessageNote model = new PullMessageNote();
            model.fromUser = pushMessage.fromUser;
            model.dest = pushMessage.dest;
            model.type = pushMessage.type;
            model.content = pushMessage.content;
            model.time = pushMessage.time;
            Message msg = new Message();
            msg.what = 1;
            msg.obj = model;
            handler.sendMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_left_corner_msgcenter:
                finish();
                break;
            case R.id.btn_delet_allmsg:
                ApiPullMessageNoteDb db = new ApiPullMessageNoteDb(context);
                db.delete();
                adapter.setList(null);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        MyLogger.i(TAG, "取消注册");
        messageManager.unregisterOnReceivedPushMessageListener(opl);//取消消息接收注册
        Constant.notecount = 0;
        super.onDestroy();
    }
}
