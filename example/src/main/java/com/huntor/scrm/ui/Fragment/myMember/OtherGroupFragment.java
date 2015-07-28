package com.huntor.scrm.ui.Fragment.myMember;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huntor.scrm.R;
import com.huntor.scrm.ui.Fragment.BaseFragment;

/**
 * Created by Admin on 2015/7/23.
 */
public class OtherGroupFragment extends BaseFragment{
    private FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View ret = inflater.inflate(R.layout.fragment_buyed_user, container, false);
        fragmentManager = getFragmentManager();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tl_custom);
        toolbar.setTitle("其他分组");
        return ret;
    }
}
