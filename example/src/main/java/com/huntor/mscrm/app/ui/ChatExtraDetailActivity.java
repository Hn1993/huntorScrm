package com.huntor.mscrm.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.utils.Constant;

public class ChatExtraDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView textTitle;
    private WebView webContent;

    private TextView mGoToChat;

    private String title = "1.耳机插入没有声音";
    private String content = "vivo手机的工业设计非常严谨，耳机孔结构也是非常的紧凑。插入耳机时需要多用一点力，将耳机完全插入。很多顾客朋友反应的耳机没声音是由于太过于爱护手机不敢用力导致耳机只插入了一小截，没有完全插入。所以放心大胆的使用吧！";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_extra_detail);

        setTitle(getResources().getString(R.string.chat_extra_detail));

        initView();

        initData();
    }

    private void initView() {
        textTitle = (TextView) findViewById(R.id.text_chat_extra_detail_title);
        webContent = (WebView) findViewById(R.id.web_chat_extra_content);
        mGoToChat = (TextView) findViewById(R.id.text_chat_extra_detail_to_chat);
        mGoToChat.setOnClickListener(this);
        ImageView imgLeft = (ImageView) findViewById(R.id.img_left_corner);
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra(Constant.CHAT_EXTRA_INTENT_TITLE);
            content = intent.getStringExtra(Constant.CHAT_EXTRA_INTENT_CONTENT);
        }
        textTitle.setText(title);
        webContent.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_left_corner:
                finish();
                break;
            case R.id.text_chat_extra_detail_to_chat:
                startActivity(new Intent(this, ChatActivity.class));
                break;
        }
    }
}
