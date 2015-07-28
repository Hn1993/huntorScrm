package com.huntor.scrm.ui.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huntor.scrm.R;


/**
 * Created by Admin on 2015/7/16.
 */
public class OnlineInteractionFragment extends BaseFragment{


    private FragmentManager fragmentManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_online_interaction, container, false);
        fragmentManager = getFragmentManager();
        return ret;
    }
}
