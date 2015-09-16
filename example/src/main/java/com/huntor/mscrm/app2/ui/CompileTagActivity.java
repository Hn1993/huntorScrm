package com.huntor.mscrm.app2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.CreatTag;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.Utils;
import com.huntor.mscrm.app2.view.tagview.OnTagDeleteListener;
import com.huntor.mscrm.app2.view.tagview.Tag;
import com.huntor.mscrm.app2.view.tagview.TagView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/9/9.
 */
public class CompileTagActivity extends BaseActivity implements View.OnClickListener{
    private TagView tagView;
    private ArrayList<String> tagList;
    String TAG="CompileTagActivity";
    private EditText et_tag;
    private TextView tv_add;
    private int fan_id;
    //858CED
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_tag);
        Intent intent=getIntent();
        if(intent!=null){
            tagList=intent.getStringArrayListExtra("tags");
            fan_id=intent.getIntExtra("fan_id",-1);
        }
        findViews();
    }

    private void findViews() {
        tagView= (TagView) findViewById(R.id.compile_tag_tagview);
        tagView.setOnTagDeleteListener(new OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag tag, int position) {
                tagDelate(tag.text);
            }
        });
        findViewById(R.id.img_left_corner).setOnClickListener(this);
        //tagList=new ArrayList<>();
        for (int i=0;i<tagList.size();i++){
            Tag tag=new Tag(tagList.get(i));
            tag.layoutColor = Color.parseColor("#1f257b");
            tag.isDeletable = true;
            tag.layoutBorderSize = 1f;
            tagView.addTag(tag);
        }
        et_tag= (EditText) findViewById(R.id.edit_tag);
        tv_add= (TextView) findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);
        //tagView.add
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_left_corner://返回按钮
                onBackPressed();
                break;
            case R.id.tv_add:
                String content_et=et_tag.getText().toString().trim();
                if (!"".equals(content_et)){
                    tagCreat(content_et);

                }else {
                    Utils.toast(CompileTagActivity.this,"请输入标签内容");
                }

                break;
        }
    }

    /**
     * 处理back键
     */
    @Override
    public void onBackPressed() {
//        List<Tag> tags= tagView.getTags();
//        if(tags==null){
//            tags=new ArrayList<>();
//        }
//        for (int i=0;i<tags.size();i++){
//            tagList.add(tags.get(i).text);
//        }
//        //Log.e(TAG, "tags.size="+tags.size());
        Intent intent=new Intent();
//        intent.putStringArrayListExtra("tag", tagList);
        setResult(1001, intent);
        super.onBackPressed();
    }

    private void tagDelate(String content){
        Log.e(TAG,"tagDelate");
        Log.e(TAG,"tagDelate===content="+content);
        Log.e(TAG,"tagDelate===fan_id="+fan_id);
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.setTimeout(1000);
        String url= Constant.HTTP_REQUEST_TAG_DELETE;
        RequestParams params = new RequestParams();
        params.put("fan_id",String.valueOf(fan_id));
        params.put("tags",content);
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e(TAG, "post成功" + new String(bytes));
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e(TAG, "post失败" +throwable);
                Utils.toast(CompileTagActivity.this,"网络连接失败！");
            }
        });

    }


    private void tagCreat(final String content){
        Log.e(TAG,"tagCreat");
        AsyncHttpClient httpClient=new AsyncHttpClient();
        httpClient.setTimeout(1000);
        String url= Constant.HTTP_REQUEST_TAG_CREAT;
        RequestParams params = new RequestParams();
        params.put("fan_id",String.valueOf(fan_id));
        params.put("tags",content);
        httpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e(TAG, "post成功" +new String(bytes));
                Tag tag2=new Tag(content);
                tag2.layoutColor = Color.parseColor("#1f257b");
                tag2.isDeletable = true;
                tag2.layoutBorderSize = 1f;
                tagView.addTag(tag2);
                et_tag.getText().clear();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e(TAG, "post失败" +throwable);
                Utils.toast(CompileTagActivity.this,"网络连接失败！");
            }
        });

//        Gson gson=new Gson();
//        ArrayList<String> tags=new ArrayList<>();
//        tags.add(content);
//        CreatTag.fan_tag.tags=tags;
//        CreatTag.fan_tag.fan_id=String.valueOf(fan_id);
//        JSONObject jo=null;
//        try {
//            jo=new JSONObject(gson.toJson(CreatTag.class));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            ByteArrayEntity entity = new ByteArrayEntity(jo.toString().getBytes("UTF-8"));
//            httpClient.post(CompileTagActivity.this,url,entity,"application/json",new JsonHttpResponseHandler(){
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    super.onSuccess(statusCode, headers, response);
//                    Log.e(TAG,"post成功"+response.toString());
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                    Log.e(TAG, "post成功" + errorResponse.toString());
//                }
//            });
//            //httpClient.post(url,entity ,"application/json", new JsonHttpResponseHandler(){});
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }


}
