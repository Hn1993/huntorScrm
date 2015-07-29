package com.huntor.mscrm.app.ui.component;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.huntor.mscrm.app.R;
import com.umeng.analytics.MobclickAgent;


public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    private Dialog mDialog; // 自定义的Dialog
    private boolean mIsVisiable;
    public RelativeLayout mTitleLayout;
    public ImageView mBackImage;
    public TextView mLeftTitle;
    public TextView mCenterTitle;
    public TextView mRightTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.custom_title_theme);
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        setContentView(new LinearLayout(this));
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_head);
//        mTitleLayout = (RelativeLayout) findViewById(R.id.custom_title_relative_layout);
//        mBackImage = (ImageView) findViewById(R.id.img_left_corner);
//        mLeftTitle = (TextView) findViewById(R.id.left_title);
//        mCenterTitle = (TextView) findViewById(R.id.center_title);
//        mRightTitle = (TextView) findViewById(R.id.right_title);

        mDialog = new Dialog(this, R.style.CustomLoadingDialog);
        mDialog.setContentView(R.layout.custom_loading_dialog);
        mDialog.setCancelable(true);

    }

    @Override
    public void setTitle(CharSequence title) {
        if (title != null) {
            TextView txtTitle = (TextView) findViewById(R.id.text_base_title);
            txtTitle.setText(title);
        }

    }

    /**
     * 返回
     */
    public View.OnClickListener onBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /**
     * 显示Dialog
     *
     * @param content Dialog中要显示的内容
     */
    public void showCustomDialog(String content) {
        TextView tv = (TextView) mDialog.findViewById(R.id.message);
        tv.setText(content);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    /**
     * 显示Dialog
     *
     * @param resid Dialog中要显示的内容
     */
    public void showCustomDialog(int resid) {
        TextView tv = (TextView) mDialog.findViewById(R.id.message);
        tv.setText(resid);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    /**
     * 取消Dialog
     */
    public void dismissCustomDialog() {
        if (mDialog != null && mIsVisiable) {
            mDialog.dismiss();
        }
    }

    /**
     * Toast
     *
     * @param msg
     * 提示内容
     */
    private static Toast mToast = null;

    public void toast(String msg) {
        if (this != null) {
            if (mToast == null) {
                mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(msg);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }

    /**
     * Toast
     *
     * @param resId 字串资源id
     */
    public void toast(int resId) {
        if (this != null) {
            if (mToast == null) {
                mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(resId);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }

            mToast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsVisiable = true;
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsVisiable = false;
        // SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
