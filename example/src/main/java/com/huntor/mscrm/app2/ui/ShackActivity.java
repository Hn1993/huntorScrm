package com.huntor.mscrm.app2.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.ShakeAdapter;
import com.huntor.mscrm.app2.model.FanInfo;
import com.huntor.mscrm.app2.model.Fans;
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                getShakeData();
            }
        }).start();
    }

    public void initView(){

        shakeListView = (ListView)findViewById(R.id.shake_msg_list);
        shakeAdapter = new ShakeAdapter(this,data);
        shakeListView.setAdapter(shakeAdapter);
        shakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("fanId", "" + data.get(position).fanId);
                Intent intent = new Intent(ShackActivity.this, DetailedInformationActivity.class);

                intent.putExtra(Constant.FANS_ID, data.get(position).fanId);
                if(data.get(position).avatar == null && "未知粉丝".equals(data.get(position).nickName)){
                    intent.putExtra("type","new_fans");
                }
                //intent.putExtra(Constant.FANS_ID, mFanList.get(position - 1).id);
                startActivity(intent);
            }
        });
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

            //获取粉丝对象
            FanInfo fanInfo = ApiFansInFoDb.getFansInfoById2(this,modle.fanId);

            if (fanInfo != null){
                shakeMessageModle.nickName = fanInfo.nickName;
                shakeMessageModle.avatar = fanInfo.avatar;
            }else{
                shakeMessageModle.nickName = "未知粉丝";
                shakeMessageModle.avatar = null;
            }
            shakeMessageModle.fanId = modle.fanId;
            shakeMessageModle.timestamp = modle.timestamp;

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
