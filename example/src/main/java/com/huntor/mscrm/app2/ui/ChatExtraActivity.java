package com.huntor.mscrm.app2.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.ChatExtraGroupAdapter;
import com.huntor.mscrm.app2.model.Categorie;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiKbCategorieContent;
import com.huntor.mscrm.app2.net.api.ApiKbGategories;
import com.huntor.mscrm.app2.provider.api.ApiKbCategoriesDb;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ChatExtraActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private final String TAG = getClass().getName();

    private ChatExtraActivity mContext = ChatExtraActivity.this;
    private ListView listCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_extra);

        setTitle(getResources().getString(R.string.chat_extra));

        ImageView imgLeft = (ImageView) findViewById(R.id.img_left_corner);
        listCategory = (ListView) findViewById(R.id.list_chat_extra);
        imgLeft.setVisibility(View.VISIBLE);

        imgLeft.setOnClickListener(this);

        listCategory.setOnItemClickListener(this);

        getChatExtraList();

    }

    private void getChatExtraChildList(int categorieId) {
        showCustomDialog(R.string.loading);
        HttpRequestController.kbContents(this, categorieId,
                new HttpResponseListener<ApiKbCategorieContent.ApiKbCategorieContentResponse>() {
                    @Override
                    public void onResult(ApiKbCategorieContent.ApiKbCategorieContentResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            MyLogger.i(TAG, "response.kbEntries = " + response.kbEntries);
                        } else {
                            Utils.toast(ChatExtraActivity.this, response.getRetInfo() + "");
                        }
                        dismissCustomDialog();
                    }
                });
    }

    /**
     * 查询MSCRM知识库分类
     * @throws IOException
     */
    private void getChatExtraList() {
        long saveTime = PreferenceUtils.getLong(mContext, Constant.PREFERENCES_SAVE_TIME,0);
        List<Categorie> categories = ApiKbCategoriesDb.getCategorieList(mContext);
        if(!DateFormatUtils.isUpdate(mContext,saveTime) && categories != null && categories.size()>=1){
            if(Constant.DEBUG){
                MyLogger.i(TAG, "categories = " + categories.toString());
            }
            for(int i = 0 ;i<categories.size();i++){
                if(categories.get(i).parentId == 0){
                    categories.remove(i);
                }
            }
            listCategory.setAdapter(new ChatExtraGroupAdapter(ChatExtraActivity.this, categories));
        }else {
            showCustomDialog(R.string.loading);
            HttpRequestController.kbCategories(this,
                    new HttpResponseListener<ApiKbGategories.ApiKbGategoriesResponse>() {
                        @Override
                        public void onResult(ApiKbGategories.ApiKbGategoriesResponse response) {
                            if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                                MyLogger.i(TAG, "response.categories = " + response.categories);
                                List<Categorie> categories = response.categories;
                                categories.remove(0);
                                listCategory.setAdapter(new ChatExtraGroupAdapter(ChatExtraActivity.this, categories));
                            } else {
                                Utils.toast(mContext, response.getRetInfo() + "");
                            }
                            dismissCustomDialog();
                        }
                    });
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_left_corner:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MyLogger.i("ChatExtraActivity", "ItemClick");
        boolean clickable = view.isClickable();
        MyLogger.i("ChatExtraActivity", "isClickAble" + clickable);

    }

}
