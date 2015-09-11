package com.huntor.mscrm.app2.ui.fragment.member;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.huntor.mscrm.app2.R;
import com.huntor.mscrm.app2.model.Data;
import com.huntor.mscrm.app2.net.BaseResponse;
import com.huntor.mscrm.app2.net.HttpRequestController;
import com.huntor.mscrm.app2.net.HttpResponseListener;
import com.huntor.mscrm.app2.net.api.ApiFansGroupCount;
import com.huntor.mscrm.app2.provider.api.ApiFansGroupCountDb;
import com.huntor.mscrm.app2.ui.MainActivity2;
import com.huntor.mscrm.app2.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app2.utils.Constant;
import com.huntor.mscrm.app2.utils.MyLogger;
import com.huntor.mscrm.app2.utils.PreferenceUtils;
import com.huntor.mscrm.app2.utils.Utils;

import java.util.List;

/**
 * 我的会员fragment
 */
public class MyMemberFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG = "MyMemberFragment";
    private TextView newFansView;//新会员
    private TextView normalFansView;//普通会员
    private TextView highUserView;//高级会员
    private TextView buyedUserView;//已买会员


    private List<Data> mMemberData;
    private FragmentManager fragmentManager;

    Toolbar toolbar;

    public MyMemberFragment() {
    }
//    public MyMemberFragment(Toolbar toolbar) {
//        // Required java.lang.Stringempty public constructor
//        this.toolbar=toolbar;
//    }

    @Override
    public void onResume() {
        super.onResume();
        //getFanGroupCount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("onCreateView");
        fragmentManager = getFragmentManager();
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_my_member_new, container, false);
        //toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar=MainActivity2.toolbar;
        Typeface typeFace =Typeface.createFromAsset(getActivity().getAssets(),"fonts/KhmerUI.ttf");
        newFansView = (TextView) ret.findViewById(R.id.text_number_people_new_fans);
        newFansView.setTypeface(typeFace);
        normalFansView = (TextView) ret.findViewById(R.id.text_number_people_normal_fans);
        normalFansView.setTypeface(typeFace);
        highUserView = (TextView) ret.findViewById(R.id.text_number_people_high_user);
        highUserView.setTypeface(typeFace);
        buyedUserView = (TextView) ret.findViewById(R.id.text_number_people_buyed_user);
        highUserView.setTypeface(typeFace);

//        ImageView iv_search = (ImageView) ret.findViewById(R.id.iv_search);
//        iv_search.setOnClickListener(this);

        ret.findViewById(R.id.layout_fragment_mymember_new_fans).setOnClickListener(this);
        ret.findViewById(R.id.layout_fragment_mymember_normal_fans).setOnClickListener(this);
        ret.findViewById(R.id.layout_fragment_mymember_high_user).setOnClickListener(this);
        ret.findViewById(R.id.layout_fragment_mymember_buyed_user).setOnClickListener(this);
        ret.findViewById(R.id.layout_fragment_mymember_my_group).setOnClickListener(this);
        ret.findViewById(R.id.layout_fragment_mymember_other_group).setOnClickListener(this);

//        ret.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity2 ma = ((MainActivity2) getActivity());
//                if(ma != null){
//                    //ma.menu();
//                }
//            }
//        });
        getFanGroupCount();
        return ret;
    }


    private void getMemberCount(List<Data> list){
        List<Data> fansGroupCounts = list;
        if(fansGroupCounts!=null&&fansGroupCounts.size()>0){
            for (int i = 0; i < 4; i++) {
                int group = fansGroupCounts.get(i).group;
                int count = fansGroupCounts.get(i).count;
                Log.i(TAG,"count:"+count);
                String countStr = Integer.toString(count);
                if (count == 0) {
                    countStr = "";
                }
                Activity getActivity = getActivity();
                if (getActivity != null) {
                    Resources resources = getActivity.getResources();
                    if (1 == group) {//新增粉丝
                        newFansView.setText(countStr + "");
                        if (resources != null) {
                            newFansView.setTextColor(resources.getColor(R.color.login_bg));
                        }
                    } else if (2 == group) {//普通粉丝
                        normalFansView.setText(countStr + "");
                        if (resources != null) {
                            normalFansView.setTextColor(resources.getColor(R.color.lighter_gray));
                        }
                    } else if (3 == group) {//高消费者
                        highUserView.setText(countStr + "");
                        if (resources != null) {
                            highUserView.setTextColor(resources.getColor(R.color.lighter_gray));
                        }
                    } else if (4 == group) {//已购用户
                        buyedUserView.setText(countStr + "");
                        if (resources != null) {
                            buyedUserView.setTextColor(resources.getColor(R.color.lighter_gray));
                        }
                    }
                }
            }
        }

    }
    /**
     *获取会员列表人数
     */
    private void getFanGroupCount(){
        //调本地缓存
        mMemberData = ApiFansGroupCountDb.getFansGroupCountList(getActivity());
        getMemberCount(mMemberData);
//        BaseActivity activity = (BaseActivity) getActivity();
//        if (activity != null) {
//            activity.showCustomDialog(R.string.loading);
//        }
        HttpRequestController.fansGroupCount(getActivity(), PreferenceUtils.getInt(getActivity(), Constant.PREFERENCE_EMP_ID, 0),
                new HttpResponseListener<ApiFansGroupCount.ApiFansGroupCountResponse>() {
                    @Override
                    public void onResult(ApiFansGroupCount.ApiFansGroupCountResponse response) {
                        MyLogger.i(TAG,"response.getRetCode():"+response.getRetCode());
                        if (response.getRetCode() == BaseResponse.RET_HTTP_STATUS_OK) {

                            mMemberData=response.fansGroupCount.data;
                            MyLogger.i(TAG,"mMemberData: "+mMemberData.toString());
                            getMemberCount(mMemberData);

                        } else if(response.getRetCode() == BaseResponse.RET_HTTP_STATUS_ERROR){
                            if(getActivity()!=null){
                                Utils.toast(getActivity(), response.getRetInfo() + "");
                            }
                            //Utils.toast(getActivity(), response.getRetInfo() + "");
                            getMemberCount(mMemberData);
                        }
//                        BaseActivity activity = (BaseActivity) getActivity();
//                        if (activity != null) {
//                            activity.dismissCustomDialog();
//                        }
                    }

                });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (id){
            case R.id.layout_fragment_mymember_new_fans://新增
                clearBackStack();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fl_content, new NewFansFragment(), Constant.MY_MEMBER);
                toolbar.setTitle("在线交互粉丝");
                break;
            case R.id.layout_fragment_mymember_normal_fans://普通
                clearBackStack();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fl_content, new NormalFansFragment(), Constant.MY_MEMBER);
                toolbar.setTitle("普通粉丝");
                break;
            case R.id.layout_fragment_mymember_high_user://高潜
                clearBackStack();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fl_content, new HighUserFragment(), Constant.MY_MEMBER);
                toolbar.setTitle("高潜会员");
                break;
            case R.id.layout_fragment_mymember_buyed_user://已购
                clearBackStack();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fl_content, new BuyedUserFragment(), Constant.MY_MEMBER);
                toolbar.setTitle("已购用户");
                break;
            case R.id.layout_fragment_mymember_my_group://我的分组
                clearBackStack();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fl_content, new MyGroupFragment(), Constant.MY_MEMBER);
                toolbar.setTitle("我的分组");
                break;
            case R.id.layout_fragment_mymember_other_group://其他分组
                clearBackStack();
                transaction.addToBackStack(null);
                transaction.replace(R.id.fl_content, new MyGroupFragment(), Constant.MY_MEMBER);
                toolbar.setTitle("其他分组");
                break;
//            case R.id.iv_search:
//                clearBackStack();
//                transaction.addToBackStack(Constant.SEARCH);
//                transaction.add(R.id.frame_main, new SearchFragment(), Constant.SEARCH);
//                break;
        }
        transaction.commit();
    }

    private void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            }
        }
    }

}
