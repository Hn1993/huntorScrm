package com.huntor.mscrm.app2.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.SearchAdapter;
import com.huntor.mscrm.app2.model.Fans;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansGroup;
import com.huntor.mscrm.app2.provider.api.ApiAllFansInfoDb;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.component.XListView;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;


public class SearchActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, TextWatcher {
    private static final String TAG = "SearchActivity";
    private XListView mListView;
    private TextView mNoContentHint;
    private ImageView imgLeftCorner;
    private BaseActivity context;
    private MaterialEditText et_searchkey;
    private Cursor cursor;
    private SearchAdapter adapter;
    private static final long THREE_HOUR = 3*60*60*1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        initView();
        refreshLocalFans();
    }

    private void initView() {
        context = SearchActivity.this;
        imgLeftCorner = (ImageView) findViewById(R.id.img_left_corner);
        et_searchkey = (MaterialEditText) findViewById(R.id.et_searchkey);
        mListView = (XListView) findViewById(R.id.list_search_fans);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(false);
        mNoContentHint = (TextView) findViewById(R.id.no_content_hint);

        imgLeftCorner.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        et_searchkey.addTextChangedListener(this);

        et_searchkey.setFocusable(true);
        et_searchkey.setFocusableInTouchMode(true);
        et_searchkey.requestFocus();

        adapter = new SearchAdapter(context, R.layout.item_group_member2,cursor);
        mListView.setAdapter(adapter);
        toggleInput();
    }

    /**
     * 刷新本地全局搜索粉丝
     */
    private void refreshLocalFans() {
        long last_refresh_all_fans_time = PreferenceUtils.getLong(context, Constant.LAST_REFRESH_ALLFANS_TIME, 0);
        long currentTime = System.currentTimeMillis();

        if(currentTime - last_refresh_all_fans_time > THREE_HOUR) {

            int empID = PreferenceUtils.getInt(context, Constant.PREFERENCE_EMP_ID, 0);
            HttpRequestController.fansGroup(context, empID, 5, 1000, 1, 1, 1,
                    new HttpResponseListener<ApiFansGroup.ApiFansGroupResponse>() {
                        @Override
                        public void onResult(ApiFansGroup.ApiFansGroupResponse response) {
                            if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                                List<Fans> fans = response.fansGroupResult.fans;
                                MyLogger.i(TAG, "fans: " + fans.size());

                                ApiAllFansInfoDb.delete(context);
                                ApiAllFansInfoDb.bulkInsert(context, fans);
                                PreferenceUtils.putLong(context, Constant.LAST_REFRESH_ALLFANS_TIME, System.currentTimeMillis());
                            }
                            //Utils.toast(context, "" + response.getRetInfo());
                        }
                    });
        }

    }


    @Override
    public void onClick(View v) {
        toggleInput();
        finish();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, DetailedInformationActivity.class);
        int fans_id = adapter.getItem(position - 1) ;
        MyLogger.i(TAG,"fans_id: "+fans_id);
        intent.putExtra(Constant.FANS_ID, fans_id);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(cursor != null){
            if(!cursor.isClosed()) {
                MyLogger.i(TAG,"onTextChanged cursor.isClosed(): "+cursor.isClosed());
                cursor.close();
            }
        }

        cursor = ApiAllFansInfoDb.getCursorBySearchKey(context, s.toString());
        adapter.changeCursor(cursor);
        MyLogger.i(TAG, "cursor.getCount(): " + cursor.getCount());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDestroy() {
        if(cursor != null){
            if(!cursor.isClosed()) {
                MyLogger.i(TAG,"onDestroy cursor.isClosed(): "+cursor.isClosed());
                cursor.close();
            }
        }
        super.onDestroy();
    }

    public void toggleInput(){
        if(context != null){
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
