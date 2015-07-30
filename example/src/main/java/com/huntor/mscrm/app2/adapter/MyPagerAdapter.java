package com.huntor.mscrm.app2.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Admin on 2015/5/4.
 */
public class MyPagerAdapter extends PagerAdapter{

    public List<View> viewlist;
    int pos;

    public MyPagerAdapter(List<View> mListViews) {
        this.viewlist = mListViews;
    }

    public MyPagerAdapter(List<View> mListViews,int pos) {
        this.viewlist = mListViews;
        this.pos=pos;
    }
    @Override
    public int getCount() {
        return viewlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(viewlist.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position) {

            ((ViewPager) container).addView(viewlist.get(position));
            return viewlist.get(position);

    }
}
