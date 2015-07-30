package com.huntor.mscrm.app2.ui.fragment.member;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiCreateTargetlist;
import com.huntor.mscrm.app2.ui.component.BaseActivity;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;

/**
 * Created by cary.xi on 2015/5/6.
 * 自定义分组
 */
public class CustomGroupFragment extends BaseFragment implements View.OnClickListener {

    private EditText mGroupName;
    public CreateFansGroupCallBack mCreateFansGroupCallBack;
    public interface CreateFansGroupCallBack{
        public void onResult();
    }

    public void setOnCallbackListener(CreateFansGroupCallBack createFansGroupCallBack){
        this.mCreateFansGroupCallBack = createFansGroupCallBack;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View ret = inflater.inflate(R.layout.fragment_custom_group, container, false);

        ret.findViewById(R.id.sure_add_btn).setOnClickListener(this);//确认添加
        ret.findViewById(R.id.img_left_corner).setOnClickListener(this);//左上角返回
        mGroupName = (EditText) ret.findViewById(R.id.edit_text);

        return ret;
    }

    /**
     * 自定义粉丝分组
     * @param name
     * @param desc
     */
    private void createGroup(String name, String desc){
        int  empId = PreferenceUtils.getInt(getActivity(), "empId", 0);
        final BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.showCustomDialog(R.string.loading);
        }

        HttpRequestController.createFansTargetList(getActivity(), empId, name, desc,
                new HttpResponseListener<ApiCreateTargetlist.ApiCreateTargetlistResponse>() {
                    @Override
                    public void onResult(ApiCreateTargetlist.ApiCreateTargetlistResponse response) {
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {
//                            Log.i(TAG, "response.targetList = " + response.targetList);
                            //在此返回分组id
                            FragmentManager manager = getFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.remove(CustomGroupFragment.this);
                            transaction.commit();
                            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mGroupName.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                            //返回粉丝分组列表重新刷新粉丝分组
                            if(mCreateFansGroupCallBack != null)
                                mCreateFansGroupCallBack.onResult();
                        }else {
                            Utils.toast(getActivity(), response.getRetInfo() + "");
                        }
                        if (activity != null) {
                            activity.dismissCustomDialog();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (v.getId()) {
            case R.id.sure_add_btn:
                String name = mGroupName.getText().toString();
                if(name.equals("")){//合法性
                    Utils.toast(getActivity(), "please fill name");
                    return;
                }
                createGroup(name, "");//向后台提交数据
                break;
            case R.id.img_left_corner:
                manager.popBackStack();
                transaction.remove(this);
                transaction.commit();
                break;
        }

    }
}
