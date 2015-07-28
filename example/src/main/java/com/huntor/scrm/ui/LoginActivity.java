package com.huntor.scrm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huntor.scrm.R;
import com.huntor.scrm.net.BaseResponse;
import com.huntor.scrm.net.HttpRequestController;
import com.huntor.scrm.net.HttpResponseListener;
import com.huntor.scrm.net.api.ApiLogin;
import com.huntor.scrm.utils.Constant;
import com.huntor.scrm.utils.Utils;

/**
 * Created by Admin on 2015/7/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView mBack,mLogin;//登陆  返回
    private EditText edit_name,edit_pwd;//输入用户名  密码
    private String name,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    /**
     * 初始化控件
     */
    public void initView(){
        mBack= (TextView) findViewById(R.id.activity_login_back);
        mBack.setOnClickListener(this);
        mLogin= (TextView) findViewById(R.id.activity_login_buttom);
        mLogin.setOnClickListener(this);
        edit_name= (EditText) findViewById(R.id.activity_login_name);
        name=edit_name.getText().toString().trim();
        edit_pwd= (EditText) findViewById(R.id.activity_login_pwd);
        pwd=edit_pwd.getText().toString().trim();
    }

    /**
     * 验证用户名密码是否正确
     */
    public boolean verifyNamePwd(String name,String pwd){


        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_login_back:
                this.finish();
                break;
            case R.id.activity_login_buttom:
                if(verifyNamePwd(name,pwd)){
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                    this.finish();
                }else{
                    Utils.toast(this, "用户名密码错误");
                }

                break;
        }
    }


}
