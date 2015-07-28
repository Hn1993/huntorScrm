package com.huntor.scrm.ui.Fragment;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huntor.scrm.R;
import com.huntor.scrm.adapter.BaseFragmentAdapter;
import com.huntor.scrm.adapter.HighUserAdapter;
import com.wangjie.wavecompat.WaveCompat;
import com.wangjie.wavecompat.WaveTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/7/16.
 */
public class BaseFragment extends Fragment implements WaveTouchHelper.OnWaveTouchHelperListener{


    public BaseFragmentAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public ArrayList initData() {
        mAdapter=new BaseFragmentAdapter(getActivity());
        ArrayList list=new ArrayList();
        for (int i=0;i<10;i++){
            list.add(i);
        }
        return list;
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
    public void onWaveTouchUp(View view, Point locationInView, Point locationInScreen) {

    }
}
