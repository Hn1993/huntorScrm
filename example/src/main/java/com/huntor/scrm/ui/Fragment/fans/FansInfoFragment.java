package com.huntor.scrm.ui.Fragment.fans;

import android.app.FragmentManager;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huntor.scrm.R;
import com.huntor.scrm.adapter.MyPagerAdapter;
import com.huntor.scrm.ui.Fragment.BaseFragment;
import com.wangjie.wavecompat.WaveCompat;
import com.wangjie.wavecompat.WaveDrawable;
import com.wangjie.wavecompat.WaveTouchHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2015/7/23.
 */
public class FansInfoFragment extends BaseFragment implements WaveTouchHelper.OnWaveTouchHelperListener,View.OnClickListener{
    private FragmentManager fragmentManager;
    private TextView fansName;//粉丝名字
    private ViewPager viewPager;
    private MyPagerAdapter pagerAdapter;//viewpager适配器
    private View view_line;//tab下的view_line


    private WaveCompat.mCallBack callBack;//接口的回调
    private RelativeLayout mRelativeLayout;
    ImageView mImageView;//点击出现水波纹效果
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_fans_info, container, false);
        fragmentManager = getFragmentManager();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.tl_custom);
        toolbar.setTitle("粉丝详情");
        findViews(ret);
        initViewpager(inflater);
        return ret;
    }


    /**
     * 水波纹动画效果
     * @param view
     * @param locationInView
     * @param locationInScreen
     */
    @Override
    public void onWaveTouchUp(View view, Point locationInView, Point locationInScreen) {
        switch (view.getId()){
            case R.id.imageView:
//                str=WaveCompat.startWaveFilter(getActivity(),
//                        new WaveDrawable()
//                                .setColor(0xddffffff) //0xddffffff
//                                .setTouchPoint(locationInScreen),
//                        generateIntent(0xddffffff,getActivity()),imageView);
//                //showPoupopWindow(getActivity());
//                imageView.setVisibility(View.VISIBLE);

                WaveCompat.startWaveFilter(getActivity(), new WaveDrawable().setColor(0x99ffffff)
                        .setTouchPoint(locationInScreen), callBack);
                break;
        }
    }


    /**
     * 初始化购买意向
     */
    private void initPurchase(View view) {

    }

    /**
     * 初始化联系方式
     */
    private void initContact(View view) {

    }

    /**
     * 初始化会员详情页
     */
    private void initMemberInfo(View view) {

    }

    /**
     * 初始化viewpager
     * @param inflater
     */
    List<View> viewPagerData;
    private void initViewpager(LayoutInflater inflater) {
        viewPagerData=new ArrayList<>();
        viewPagerData.add(inflater.inflate(R.layout.layout_viewpager1,null));
        viewPagerData.add(inflater.inflate(R.layout.layout_viewpager2,null));
        viewPagerData.add(inflater.inflate(R.layout.layout_viewpager3,null));
        pagerAdapter=new MyPagerAdapter(viewPagerData);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(vpChangeLisenter);

        initMemberInfo(viewPagerData.get(0));
        initContact(viewPagerData.get(1));
        initPurchase(viewPagerData.get(2));
    }

    /**
     * viewpager的滑动监听
     */
    private ViewPager.OnPageChangeListener vpChangeLisenter=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    mImageView.setVisibility(View.VISIBLE);
                    viewLineAnimation((getActivity().getWindowManager().getDefaultDisplay().getWidth()) / 3, 0);//从0-屏幕三分之一的位置
                    break;
                case 1:
                    mImageView.setVisibility(View.GONE);
                    viewLineAnimation((getActivity().getWindowManager().getDefaultDisplay().getWidth()) / 3 * 2, (getActivity().getWindowManager().getDefaultDisplay().getWidth()) / 3);//从屏幕三分之一的位置到三分之二
                    break;
                case 2:
                    mImageView.setVisibility(View.GONE);
                    viewLineAnimation((getActivity().getWindowManager().getDefaultDisplay().getWidth()),(getActivity().getWindowManager().getDefaultDisplay().getWidth())/3*2);//从最右侧到2/3的位置
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void findViews(View view) {
        fansName= (TextView) view.findViewById(R.id.fans_info_name);
        viewPager= (ViewPager) view.findViewById(R.id.detailte_viewpager);
        //获取view_line
        view_line = view.findViewById(R.id.detaileinfo_line_1);
        view_line.setLayoutParams(new LinearLayout.LayoutParams((getActivity().getWindowManager().getDefaultDisplay().getWidth()) / 3, 5));
        //Tab的点击事件
        view.findViewById(R.id.fans_info_tab_purchase).setOnClickListener(this);
        view.findViewById(R.id.fans_info_tab_member).setOnClickListener(this);
        view.findViewById(R.id.fans_info_tab_phone).setOnClickListener(this);

        mImageView = (ImageView) view.findViewById(R.id.imageView);
        WaveTouchHelper.bindWaveTouchHelper(mImageView, this);//
        mRelativeLayout= (RelativeLayout) view.findViewById(R.id.fans_info_layout_finish);
        mRelativeLayout.setOnClickListener(this);
        /**
         * 在动画的完成后显示一个view
         */
        callBack=new WaveCompat.mCallBack() {
            @Override
            public void resault() {
                mRelativeLayout.setVisibility(View.VISIBLE);
            }
        };
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fans_info_tab_member:
                viewPager.setCurrentItem(0);
                break;
            case R.id.fans_info_tab_phone:
                viewPager.setCurrentItem(1);
                break;
            case R.id.fans_info_tab_purchase:
                viewPager.setCurrentItem(2);
                break;
            case R.id.fans_info_layout_finish:
                mRelativeLayout.setVisibility(View.GONE);
                break;

        }
    }

    /**
     * 标题下面线的动画
     * 第一个参数为动画开始的位置
     * 第二个参数为动画结束的位置
     */
    public void viewLineAnimation(int start,int end){
        TranslateAnimation an = new TranslateAnimation(start, end, 0, 0);
        an.setFillAfter(true);
        view_line.startAnimation(an);
    }

}
