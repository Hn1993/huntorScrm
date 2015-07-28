package com.huntor.scrm.ui.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.huntor.scrm.R;
import com.huntor.scrm.ui.Fragment.myMember.BuyedFragment;
import com.huntor.scrm.ui.Fragment.myMember.HighUserFragment;
import com.huntor.scrm.ui.Fragment.myMember.MyGroupFragment;
import com.huntor.scrm.ui.Fragment.myMember.NormalFragment;
import com.huntor.scrm.ui.Fragment.myMember.OnlineChatFragment;
import com.huntor.scrm.ui.Fragment.myMember.OtherGroupFragment;
import com.huntor.scrm.utils.Utils;
import com.wangjie.wavecompat.WaveCompat;
import com.wangjie.wavecompat.WaveDrawable;
import com.wangjie.wavecompat.WaveTouchHelper;


/**
 * Created by Admin on 2015/7/16.
 */
public class MyMemberFragment extends BaseFragment implements View.OnClickListener {
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_my_member, container, false);
        fragmentManager = getFragmentManager();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tl_custom);
        toolbar.setTitle("我的会员");
        findViews(ret);
        return ret;
    }

    public void findViews(View view){
        view.findViewById(R.id.member_high_user).setOnClickListener(this);
        view.findViewById(R.id.member_buyed_user).setOnClickListener(this);
        view.findViewById(R.id.member_normal).setOnClickListener(this);
        view.findViewById(R.id.member_my_group).setOnClickListener(this);
        view.findViewById(R.id.member_online).setOnClickListener(this);
        view.findViewById(R.id.member_other_group).setOnClickListener(this);
    }



    public void showPoupopWindow(Context context){
        PopupWindow mPopWindow;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View mPopView=inflater.inflate(R.layout.activity_second_wave,null);
        mPopWindow = new PopupWindow(mPopView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setFocusable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.showAtLocation(mPopView, Gravity.CENTER, 0, 0);
        WaveCompat.transitionDefaultInitial(mPopView);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        //transaction.hide(this);//隐藏当前的Fragment
        switch (view.getId()){
            case R.id.member_online:
                Utils.toast(getActivity(),"online onclick");
                OnlineChatFragment ocf=new OnlineChatFragment();
                transaction.replace(R.id.fl_content, ocf);
                break;
            case R.id.member_high_user:
                Utils.toast(getActivity(),"Highuser onclick");
                HighUserFragment huf=new HighUserFragment();
                transaction.replace(R.id.fl_content,huf);
                break;
            case R.id.member_normal:
                transaction.replace(R.id.fl_content,new NormalFragment());
                break;
            case R.id.member_buyed_user:
                transaction.replace(R.id.fl_content,new BuyedFragment());
                break;
            case R.id.member_my_group:
                transaction.replace(R.id.fl_content,new MyGroupFragment());
                break;
            case R.id.member_other_group:
                transaction.replace(R.id.fl_content,new OtherGroupFragment());
                break;
        }
        transaction.commit();
    }
}
