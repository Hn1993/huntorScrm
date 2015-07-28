package com.huntor.scrm.ui.Fragment.myMember;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huntor.scrm.R;
import com.huntor.scrm.adapter.HighUserAdapter;
import com.huntor.scrm.ui.Fragment.BaseFragment;
import com.huntor.scrm.ui.Fragment.fans.FansInfoFragment;
import com.huntor.scrm.view.XlistView.XListView;

import java.util.ArrayList;

/**
 * Created by Admin on 2015/7/20.
 */
public class HighUserFragment extends BaseFragment{

    private FragmentManager fragmentManager;
    private ListView mXlisView;
    private ArrayList mData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View ret = inflater.inflate(R.layout.fragment_high_user, container, false);
        fragmentManager = getFragmentManager();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tl_custom);
        toolbar.setTitle("高潜会员");
        findViews(ret);
        return ret;
    }

    private void findViews(View view) {
        mXlisView= (ListView) view.findViewById(R.id.xlistview_highuser);
        mData=initData();
        mXlisView.setAdapter(mAdapter);
        mAdapter.addendData(mData,false);

        mXlisView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction ft=fragmentManager.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fl_content,new FansInfoFragment());
                ft.commit();
            }
        });
    }

}