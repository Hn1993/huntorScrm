package com.huntor.mscrm.app2.ui.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huntor.mscrm.app2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealInformationFragment extends Fragment {


    public DealInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deal_information, container, false);
    }


}
