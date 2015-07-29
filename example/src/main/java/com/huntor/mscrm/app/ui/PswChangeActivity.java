package com.huntor.mscrm.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiModifyPwd;
import com.huntor.mscrm.app.push.PushMessageReceiverService;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.component.GrayUnderLineEditText;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.MyLogger;
import com.huntor.mscrm.app.utils.PreferenceUtils;
import com.huntor.mscrm.app.utils.Utils;

public class PswChangeActivity extends BaseActivity implements View.OnClickListener{

    public final String TAG = getClass().getName();
    private Context context = PswChangeActivity.this;

    private GrayUnderLineEditText mPswBefore;
    private GrayUnderLineEditText mPswNew;
    private GrayUnderLineEditText mPswConform;

    private String mNumber;
    private static final String PSW_CHANGE_SUCCESS = "0";//修改成功状态码
    private String mMainToPsw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw_change);
        View leftCorner = findViewById(R.id.img_left_corner);
        setTitle(getString(R.string.modify_psw));
        Intent intent = getIntent();
        if (intent != null) {
            mMainToPsw = intent.getStringExtra(Constant.MAIN_INTENT_DATA);
            mNumber = intent.getStringExtra(Constant.PREFERENCE_NUMBER);
            if (!TextUtils.isEmpty(mMainToPsw) && TextUtils.equals(mMainToPsw, "IntentPsw")){
                mNumber = PreferenceUtils.getString(this, Constant.PREFERENCE_NUMBER, "");
                leftCorner.setVisibility(View.VISIBLE);
                leftCorner.setOnClickListener(this);
            }else {
                leftCorner.setVisibility(View.GONE);
            }
        }


        mPswBefore = (GrayUnderLineEditText) findViewById(R.id.edit_psw_change_old);
        mPswNew = (GrayUnderLineEditText) findViewById(R.id.edit_psw_change_new);
        mPswConform = (GrayUnderLineEditText) findViewById(R.id.edit_psw_change_confirm);
        findViewById(R.id.modify_pwd_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == R.id.modify_pwd_btn){//确认修改密码

            String oldPwd = mPswBefore.getText().toString();
            String newPwd = mPswNew.getText().toString();
            String pswConfirm = mPswConform.getText().toString();

            if("".equals(oldPwd) || "".equals(newPwd)){//检查合法性
                Utils.toast(this, "密码不能为空");
                return;
            }

            if (!oldPwd.equals("") && !newPwd.equals("") && pswConfirm.equals(newPwd)){
                MyLogger.i(TAG,"mNumber: "+mNumber);
                changePsw(mNumber, oldPwd, newPwd);//修改密码
            }else {
                Utils.toast(this, "请重新输入密码");
            }
        }else if (id == R.id.img_left_corner){
            finish();
        }
    }

    /**
     *密码修改
     * @param number
     * @param oldPwd
     * @param newPwd
     */
    private void changePsw(String number, String oldPwd, final String newPwd){
        showCustomDialog(R.string.loading);
        HttpRequestController.modifyPwd(this, number, oldPwd, newPwd,
                new HttpResponseListener<ApiModifyPwd.ApiModifyPwdResponse>() {
                    @Override
                    public void onResult(ApiModifyPwd.ApiModifyPwdResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            MyLogger.i(TAG, "密码修改成功");

                            if (!TextUtils.isEmpty(mMainToPsw) && mMainToPsw.equals("IntentPsw")) {
                                PswChangeActivity.this.stopService(new Intent(PswChangeActivity.this, PushMessageReceiverService.class));//修改密码成功，退出当前账户
                                PreferenceUtils.clearUser(context);//清空Preference
                            }
                            Intent intent = new Intent(PswChangeActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.toast(PswChangeActivity.this, response.getRetInfo() + "");
                        }
                        dismissCustomDialog();
                    }
                });
    }

}
