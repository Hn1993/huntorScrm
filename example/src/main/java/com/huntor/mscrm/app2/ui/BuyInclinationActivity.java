package com.huntor.mscrm.app2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.adapter.BuyInclinationAdapter;
import com.huntor.mscrm.app2.model.Product;
import com.huntor.mscrm.app2.model.ProductCategories;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansPurchaseIntents;
import com.huntor.mscrm.app2.net.api.ApiProducts;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 2015/5/7.
 */
public class BuyInclinationActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    private final String TAG = "BuyInclinationActivity";
    private BuyInclinationActivity context = BuyInclinationActivity.this;

    private ListView mListView;
    private BuyInclinationAdapter adapter;
    private TextView buyinclination_textview;

    private ArrayList<View> list;
    private String result;
    private ImageView mBack;
    private int procuctId,accountId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyinclination);
        Intent intent=getIntent();
        accountId= intent.getIntExtra("accountId",1);
        initView();
        initData();
    }

    /**
     * 初始化View
     */
    private void initView(){
        mBack = (ImageView)super.findViewById(R.id.back);
        mBack.setOnClickListener(this);
        mListView= (ListView) findViewById(R.id.buyinclination_listview);
        mListView.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        adapter=new BuyInclinationAdapter(context,new ArrayList<Product>());
        mListView.setAdapter(adapter);
        getProductlist();
    }

    public void getProductlist(){
        showCustomDialog(R.string.loading);
        HttpRequestController.getProducts(this,
                new HttpResponseListener<ApiProducts.ApiProductsResponse>() {
                    @Override
                    public void onResult(ApiProducts.ApiProductsResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                            //获取产品列表
                            List<ProductCategories> productList = response.categories;

                            if (productList != null && productList.size() > 0) {
                                List<Product> list = new ArrayList<Product>();
                                for (int i = 0; i < productList.size(); i++) {

                                    List<Product> pL = productList.get(i).products;
                                    String mProductCategoriesName = productList.get(i).name;
                                    if (("整机").equals(mProductCategoriesName)) {
                                        if (pL != null && pL.size() > 0) {
                                            for (int j = 0; j < pL.size(); j++) {
                                                list.add(pL.get(j)); //当前所有名称（类别）为“整机”产品都放在同一个列表中所有构造数据都放在同一个list中
                                            }
                                        }
                                    }

                                }
                                adapter.setData(list);
                                adapter.notifyDataSetChanged();
                            }else{

                            }
                        } else {
                            Utils.toast(BuyInclinationActivity.this, response.getRetInfo() + "");
                        }
                        dismissCustomDialog();
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent();
        Product product = (Product)adapterView.getAdapter().getItem(i);
        //产品ID
        procuctId = product.id;
        getData(accountId,procuctId);
        BuyInclinationActivity.this.setResult(RESULT_OK, intent);
    }

    public void getData(int accountId,int procuctId){
        showCustomDialog(R.string.loading);
            HttpRequestController.addFansPurchaseIntent(this, accountId, procuctId, "new", new Date().getTime(),
                    new HttpResponseListener<ApiFansPurchaseIntents.ApiFansPurchaseIntentsResponse>() {
                        @Override
                        public void onResult(ApiFansPurchaseIntents.ApiFansPurchaseIntentsResponse response) {
                            if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
                                Log.i(TAG, "response.fansGroupResult = " + response.purchaseIntent);
                            }else {
                                Utils.toast(context, response.getRetInfo() + "");
                            }
                            dismissCustomDialog();
                            finish();
                        }
                    });
        }

}
