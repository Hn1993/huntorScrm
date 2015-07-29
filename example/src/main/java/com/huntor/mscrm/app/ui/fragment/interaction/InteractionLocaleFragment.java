package com.huntor.mscrm.app.ui.fragment.interaction;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.huntor.mscrm.app.R;


import com.huntor.mscrm.app.myZXing.CaptureActivity;
import com.huntor.mscrm.app.ui.MainActivity2;

import com.huntor.mscrm.app.ui.component.BaseActivity;
import com.huntor.mscrm.app.ui.fragment.SearchFragment;
import com.huntor.mscrm.app.ui.fragment.base.BaseFragment;
import com.huntor.mscrm.app.ui.fragment.member.MyGroupFragment;
import com.huntor.mscrm.app.utils.Constant;
import com.huntor.mscrm.app.ui.InteractionLocaleActivity;
import com.huntor.mscrm.app.utils.Utils;

/**
 * 现场互动的fragment
 */
public class InteractionLocaleFragment extends Fragment implements View.OnClickListener {

    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.dismissCustomDialog();
        }
        View ret = inflater.inflate(R.layout.fragment_interaction, container, false);
        RelativeLayout layoutBuyEnter = (RelativeLayout) ret.findViewById(R.id.layout_fragment_interaction_buy_enter);
        RelativeLayout layoutEnterInvitation = (RelativeLayout) ret.findViewById(R.id.layout_fragment_interaction_enter_invitation);
        ImageView iv_search = (ImageView) ret.findViewById(R.id.iv_search);

        layoutBuyEnter.setOnClickListener(this);
        layoutEnterInvitation.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        ret.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity2 ma = ((MainActivity2) getActivity());
                if (ma != null) {
                    //ma.menu();
                }
            }
        });

        fragmentManager = getFragmentManager();
        return ret;
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (id){
            case R.id.layout_fragment_interaction_buy_enter:
                intent = new Intent(getActivity(), CaptureActivity.class);		//CaptureActivity是扫描的Activity类
                //startActivityForResult(intent, 0);							//当前扫描完条码或二维码后,会回调当前类的onActivityResult方法,
                startActivity(intent);
                break;
            case R.id.layout_fragment_interaction_enter_invitation:
                intent = new Intent(getActivity(), InteractionLocaleActivity.class);
                intent.putExtra(Constant.INTERACTION_INTENT_DATA, Constant.ENTER_INVITATION);
                startActivity(intent);
                break;
            case R.id.iv_search:
                transaction.addToBackStack(Constant.SEARCH);
                transaction.add(R.id.frame_main, new SearchFragment(), Constant.SEARCH);
                break;
        }
        transaction.commitAllowingStateLoss();
    }



}
