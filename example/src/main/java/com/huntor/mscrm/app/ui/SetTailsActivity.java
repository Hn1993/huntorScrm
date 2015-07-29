package com.huntor.mscrm.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.utils.PreferenceUtils;
import com.huntor.mscrm.app.utils.Utils;


public class SetTailsActivity extends BaseActivity implements View.OnClickListener {

    private static final int RESULT = 200 ;
    private EditText mEditTails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_tails);
        setTitle(getResources().getString(R.string.setting_tails));

       // mEditTails = (MaterialEditText) findViewById(R.id.edit_set_tails);
        ImageView mConfirm = (ImageView) findViewById(R.id.btn_set_tails_confirm);
        RelativeLayout leftCorner = (RelativeLayout) findViewById(R.id.img_left_corner);

        leftCorner.setOnClickListener(this);
        mConfirm.setOnClickListener(this);

        mEditTails.setFocusable(true);
        mEditTails.setFocusableInTouchMode(true);
        mEditTails.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        String tails = PreferenceUtils.getString(this, Constant.CHAT_TAILS, "");
        if (!TextUtils.isEmpty(tails)){
            mEditTails.setText(tails);
            mEditTails.setSelection(tails.length());
        }



    }


    @Override
    public void onClick(View v) {
        String tails = mEditTails.getText().toString();
        int id = v.getId();
        switch (id){
            case R.id.btn_set_tails_confirm:
                if(tails.length()>100){
                    Utils.toast(this,getResources().getString(R.string.tails_max_length_error_msg));
                } else {
                    PreferenceUtils.putString(this, Constant.CHAT_TAILS, tails);
                    Intent intent = new Intent();
                    intent.putExtra("tails",tails) ;
                    setResult(RESULT, intent);
                    finish();
                }

                break;
            case R.id.img_left_corner:
                finish();
                break;
        }
    }



}
