package com.huntor.mscrm.app2.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.Utils;

/**
 * Created by Admin on 2015/6/10.
 * 手动输入条形码
 */
public class InputBarcodeActivity extends BaseActivity implements View.OnClickListener{
    String TAG=getClass().getName();

    //Edittext
    private EditText mEdittext;
    //点击事件
    private TextView goScanning,goShowBarcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inputbarcode);
        goScanning= (TextView) findViewById(R.id.go_scanning_barcode);
        goScanning.setOnClickListener(this);
        goShowBarcode= (TextView) findViewById(R.id.go_show_barcode);
        goShowBarcode.setOnClickListener(this);
        mEdittext= (EditText) findViewById(R.id.edit_input_barcode);
        //mEditText的长度的监听
        mEdittext.addTextChangedListener(mTextWatcher);
    }

    /*
    * EditText的长度监听
    * */
    public TextWatcher mTextWatcher=new TextWatcher() {
        private CharSequence temp;
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(mEdittext.getText().length()==15){
                goShowBarcode.setBackground(getResources().getDrawable(R.drawable.shape_show_barcode));
            }else {
                goShowBarcode.setBackground(getResources().getDrawable(R.drawable.shape_input_barcode));
            }


        }
    };


    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 弹出软键盘
     *
     */
    public void showSoftInputView(final EditText editText){
        InputMethodManager inputManager =
                (InputMethodManager)editText.getContext().getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_input_barcode:
                showSoftInputView(mEdittext);
                break;
            case R.id.go_scanning_barcode:
                MyLogger.i(TAG,"切换扫码");
                InputBarcodeActivity.this.finish();
                hideSoftInputView();
                break;
            case R.id.go_show_barcode:
                MyLogger.i(TAG,"显示条码");
                if("".equals(mEdittext.getText().toString())){
                    goShowBarcode.setBackground(getResources().getDrawable(R.drawable.shape_input_barcode));
                    Utils.toast(this,"条形码为空");
                }else{
                    if(mEdittext.getText().length()<15){
                        Utils.toast(InputBarcodeActivity.this,"IEMI码输入位数不正确，请检查");
                    }else{
                        Intent intent=new Intent(this,InteractionLocaleActivity.class);
                        String imei=mEdittext.getText().toString().trim();//去空格
                        intent.putExtra("list",imei);
                        intent.putExtra("inputBarcode","true");
                        startActivity(intent);
                        finish();
                        hideSoftInputView();
                    }
                }
                break;
        }
    }
}
