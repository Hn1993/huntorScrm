package com.huntor.mscrm.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.IconTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huntor.mscrm.app.R;
import com.huntor.mscrm.app.net.BaseResponse;
import com.huntor.mscrm.app.net.HttpRequestController;
import com.huntor.mscrm.app.net.HttpResponseListener;
import com.huntor.mscrm.app.net.api.ApiQrCreate;
import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.utils.*;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;

/**
 * 入会邀请activity
 *  扫描二维码界面
 */
public class InteractionLocaleActivity extends BaseActivity implements View.OnClickListener{

    private String TAG = "InteractionLocaleActivity";

    private ImageView imgDimensionCode;
    private static final String QR_SUCCESS = "0";

    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private DisplayImageOptions mOptions = null;

    IconTextView mIconText;

    String isInputBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction_locale);

        RelativeLayout imgLeftCorner = (RelativeLayout) findViewById(R.id.img_left_corner);
        imgLeftCorner.setVisibility(View.VISIBLE);
        imgLeftCorner.setOnClickListener(this);
        imgDimensionCode = (ImageView) findViewById(R.id.img_interaction_dimension_code);
        TextView textCode = (TextView) findViewById(R.id.text_interaction_code_two_dimension);
        mIconText= (IconTextView) findViewById(R.id.imei_interaction_code_two_dimension);

        Intent intent = getIntent();
        String title = intent.getStringExtra(Constant.INTERACTION_INTENT_DATA);//确定由哪一个选项进入该界面
        if (title == null) {
            title = Constant.ENTER_BY_BUY;
        }
        setTitle(title);
        int empId = PreferenceUtils.getInt(this,Constant.PREFERENCE_EMP_ID,0);
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.dimension_code_loading_default)
                .showImageForEmptyUri(R.drawable.dimension_code_loading_default).cacheOnDisc(false)
                .showImageOnFail(R.drawable.dimension_code_loading_default).cacheInMemory(false)
                .build();
        if (title.equals(Constant.ENTER_INVITATION)) {//由现场互动中的入会邀请跳转过来
            textCode.setVisibility(View.GONE);
            mIconText.setVisibility(View.GONE);
            int socialId = PreferenceUtils.getInt(this, Constant.PREFERENCES_SOCIAL_ID, -1);
            Log.i(TAG,"empId:"+empId);
            Log.i(TAG,"socialId:"+socialId);
            qrCreateTask(empId, socialId,null);
        } else if (title.equals(Constant.ENTER_BY_BUY)) {//由二维码扫描后跳转过来
            isInputBarcode=intent.getStringExtra("inputBarcode");//isInputBarcode 为 "true"是手动输入条形码

            String mSn = intent.getStringExtra("list");
            Log.i("msn","msn.length:"+mSn.length());
            Log.i("msn",""+mSn);
            if(mSn.length()!=15){
                textCode.setText("IMEI:" + mSn);
                Log.i("msn", "string:" + getString(R.string.icon_imei_false));
//                mIconText.setText(getString(R.string.icon_imei_false));
//                mIconText.setTextColor(getResources().getColor(R.color.imei_false));


                //如果扫到的码不是15位的   显示vivo logo
                showLogo();

            }else{
                textCode.setText("IMEI:" + mSn);
                Log.i("msn", "string:" + getString(R.string.icon_imei_true));

                int socialId = PreferenceUtils.getInt(this,Constant.PREFERENCES_SOCIAL_ID, -1);
                qrCreateTask(empId, socialId, mSn);
            }
        }
    }


    private void showLogo(){

        //httpGet();

        imgDimensionCode.setVisibility(View.GONE);
        mIconText.setText(getString(R.string.icon_imei_false));
        mIconText.setTextColor(getResources().getColor(R.color.imei_false));
        ImageView mImage= (ImageView) findViewById(R.id.img_interaction_dimension_logo);
        mImage.setVisibility(View.VISIBLE);
        mImage.setImageResource(R.drawable.logo_500px);
    }


    /**
     * 生成二维码
     * @param empId
     * @param socialId
     * @param sn
     */
    private void qrCreateTask(final int empId, int socialId, final String sn){

        Log.i(TAG, "empId:" + empId);
        Log.i(TAG, "socialId:" + socialId);
        showCustomDialog(R.string.loading);
        HttpRequestController.createQr(this, empId, socialId, sn,
                new HttpResponseListener<ApiQrCreate.ApiQrCreateResponse>() {
                    @Override
                    public void onResult(ApiQrCreate.ApiQrCreateResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

                            String url = response.qrcode.qrPicUrl;
                            String code = response.qrcode.code;

                            //判断返回码是否正确   0正确   其他为不正确
                            if (("0").equals(code)) {
                                if (url != null) {
                                    mIconText.setText(getString(R.string.icon_imei_true));
                                    mIconText.setTextColor(getResources().getColor(R.color.imei_true));
                                    mImageLoader.displayImage(url, imgDimensionCode, mOptions);
                                    Log.i("二维码地址", url);

                                } else {
                                    if(isInputBarcode.equals("true")){
                                        Utils.toast(InteractionLocaleActivity.this,"IMEI输入有误，请检查");
                                    }else{

                                    }
                                    Utils.toast(InteractionLocaleActivity.this, "二维码地址为空");
                                    showLogo();
                                }
                            } else {
                                showLogo();
                            }


                        } else {


                            Utils.toast(InteractionLocaleActivity.this, response.getRetInfo() + "");
                            //imgDimensionCode.setBackgroundResource(R.drawable.dimension_code_fail_default);
                            showLogo();

                        }
                        dismissCustomDialog();
                    }
                });
    }


    public void httpGet(){
        Log.i(TAG, "httpget====================" );
        try{

            AsyncHttpClient httpClient=new AsyncHttpClient();
            RequestParams params=new RequestParams();
            params.put("userName","121001a998");
            params.put("pwd", "1234");
            httpClient.addHeader("AppId", "");
            httpClient.addHeader("AppSercet","");
            httpClient.post( "http://scrmapp.vivo.com.cn:82/MobileBusiness/user/login",params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    Log.i(TAG,"success======");
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Log.i(TAG,"fail=============");
                }
            });
        }catch (Exception e){

        }

    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.img_left_corner) {
            finish();
        }
    }

}
