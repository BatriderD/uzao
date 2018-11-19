package com.zhaotai.uzao.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Time: 2017/5/13
 * Created by LiYou
 * Description :
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;
    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,ArrayList<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
