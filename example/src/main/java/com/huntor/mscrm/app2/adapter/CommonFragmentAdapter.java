package com.huntor.mscrm.app2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by IDEA
 * User : SL
 * on 2015/4/30 0030
 * 18:23.
 */
public class CommonFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public CommonFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment ret = null;
        if (fragments != null) {
            ret = fragments.get(position);
        }
        return ret;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
