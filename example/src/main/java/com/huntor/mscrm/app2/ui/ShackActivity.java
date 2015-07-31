package com.huntor.mscrm.app2.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.ShakeAdapter;
import com.huntor.mscrm.app2.model.FanInfo;
import com.huntor.mscrm.app2.model.ShakeMessageModle;
import com.huntor.mscrm.app2.model.ShakeModle;
import com.huntor.mscrm.app2.provider.api.ApiFansInFoDb;
import com.huntor.mscrm.app2.provider.api.ApiPullMessageShakeDb;
import com.huntor.mscrm.app2.push.PushMessageManager;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tangtang on 15/7/30.
 */
public class ShackActivity extends Activity implements View.OnClickListener{

    ListView shakeListView;
    ShakeAdapter shakeAdapter;
    List<ShakeMessageModle> data = new ArrayList<ShakeMessageModle>();

    PushMessageManager pushMessageManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        initView();

        pushMessageManager = PushMessageManager.getInstance(this);
        pushMessageManager.registerOnReceivedPushMessageListener(shakeListener);


    }

    public void initView(){

        shakeListView = (ListView)findViewById(R.id.shake_msg_list);
        shakeAdapter = new ShakeAdapter(this,data);
        shakeListView.setAdapter(shakeAdapter);

        findViewById(R.id.img_left_corner).setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    public void getShakeData(){
        List<ShakeModle> modles = ApiPullMessageShakeDb.getMsgList(this);
        ApiPullMessageShakeDb.updateReadStatus(this);
        Constant.shakecount = 0;


        List<ShakeMessageModle> newData = new ArrayList<ShakeMessageModle>();
        ShakeMessageModle shakeMessageModle;

        for(ShakeModle modle : modles){
            shakeMessageModle = new ShakeMessageModle();

            FanInfo fanInfo = ApiFansInFoDb.getFansInfoById(this,modle.fanId);

            if (fanInfo != null){
                shakeMessageModle.nickName = fanInfo.nickName;
                shakeMessageModle.avatar = fanInfo.avatar;
            }else{
                shakeMessageModle.nickName = "未知粉丝";
            }

            shakeMessageModle.time = DateFormatUtils.format(new Date(modle.timestamp),"MM-dd HH:mm");

            newData.add(shakeMessageModle);
        }

        data = newData;

        handler.sendEmptyMessage(0);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        pushMessageManager.unregisterOnReceivedPushMessageListener(shakeListener);
    }

    @Override
    protected void onResume() {
        getShakeData();
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.img_left_corner:
                finish();
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:

                    shakeAdapter.setData(data);
                    shakeAdapter.notifyDataSetChanged();

                    shakeListView.setSelection(data.size());
                    break;
            }
        }
    };
    PushMessageManager.OnPullMessageShakeListener shakeListener = new PushMessageManager.OnPullMessageShakeListener() {
        @Override
        public void OnPullMessageShake(Object pushMessage) {

            Log.e("SahkeActivity"  ,"收到摇一摇消息");
            getShakeData();
        }
    };
}
