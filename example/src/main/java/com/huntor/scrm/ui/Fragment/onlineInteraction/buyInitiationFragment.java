package com.huntor.scrm.ui.Fragment.onlineInteraction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huntor.scrm.myZXing.CaptureActivity;
import com.huntor.scrm.ui.Fragment.BaseFragment;


/**
 * Created by Admin on 2015/7/20.
 * 购买入会
 */
public class buyInitiationFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent=new Intent(getActivity(), CaptureActivity.class);
        startActivity(intent);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
