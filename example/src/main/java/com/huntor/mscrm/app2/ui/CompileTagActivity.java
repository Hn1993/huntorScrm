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
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.Utils;
import com.huntor.mscrm.app2.view.tagview.Tag;
import com.huntor.mscrm.app2.view.tagview.TagView;

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
    //858CED
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_tag);
        findViews();
    }

    private void findViews() {
        tagView= (TagView) findViewById(R.id.compile_tag_tagview);
        findViewById(R.id.img_left_corner).setOnClickListener(this);
        tagList=new ArrayList<>();
        for (int i=0;i<10;i++){
            Tag tag=new Tag("测试");
            tag.layoutColor = Color.parseColor("#858CED");
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
                    Tag tag2=new Tag(content_et);
                    tag2.layoutColor = Color.parseColor("#858CED");
                    tag2.isDeletable = true;
                    tag2.layoutBorderSize = 1f;
                    tagView.addTag(tag2);
                    et_tag.getText().clear();
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
        List<Tag> tags= tagView.getTags();
        if(tags==null){
            tags=new ArrayList<>();
        }
        for (int i=0;i<tags.size();i++){
            tagList.add(tags.get(i).text);
        }
        //Log.e(TAG, "tags.size="+tags.size());
        Intent intent=new Intent();
        intent.putStringArrayListExtra("tag", tagList);
        setResult(1001, intent);
        super.onBackPressed();
    }
}
