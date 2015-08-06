package com.huntor.mscrm.app2.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.net.task.ImageDownloadTask;
import com.huntor.mscrm.app2.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 *
 * Created by tangtang on 15/8/5.
 */
public class ImageActivity extends Activity implements View.OnClickListener,ImageDownloadTask.CallBack{

    String imageurl;

    Button savebtn;
    ImageView mImg;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImg = (ImageView)LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_item_img_pop,null);
        setContentView(mImg);

        initImgPopWindow();

        imageurl = getIntent().getExtras().getString("url");

        initView();
    }


    public void initView(){

        ImageLoader.getInstance().displayImage(imageurl, mImg, options);

        mImg.setOnClickListener(this);

        mImg.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                Log.e("longclick", "longclick");
                if (dialog == null) {
                    showDialog();
                }


                return true;
            }
        });



    }
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.dimension_code_fail_default)
            .showImageOnFail(R.drawable.dimension_code_fail_default)
            .showImageOnLoading(R.drawable.dimension_code_loading_default)
            .build();
    public void initImgPopWindow(){

    }


    public void showDialog(){

        View imgPopView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_image, null);

        savebtn = (Button)imgPopView.findViewById(R.id.save_image_button);

        savebtn.setOnClickListener(this);

        dialog = new Dialog(ImageActivity.this,R.style.CustomLoadingDialog);
        dialog.setContentView(imgPopView);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.save_image_button:
                ImageDownloadTask imgTask = new ImageDownloadTask(this);
                imgTask.execute(imageurl);

                if (dialog != null) {
                    dialog.cancel();
                }
                break;

            case  R.id.img_chat_item_pop:
                finish();
                break;
        }
    }

    @Override
    public void onResult(int result) {

        if(result == 0){
            //成功
            Utils.toast(getApplicationContext(), "保存成功");
        }else{
            //失败
            Utils.toast(getApplicationContext(), "保存失败");
        }
    }
}
