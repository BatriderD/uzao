package com.zhaotai.uzao.ui.search.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.ui.order.fragment.AllOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitApproveOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitCommendOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitDeliveryOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitPayOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitReceiveOrderFragment;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Time: 2018/4/11
 * Created by LiYou
 * Description :
 */

public class MainSearchFragment extends BaseFragment {

    @BindView(R.id.search_tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    public static MainSearchFragment newInstance(ArrayList<String> titles) {
        MainSearchFragment frag = new MainSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", titles);
        frag.setArguments(bundle);
        return new MainSearchFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_main_search;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initData() {
//        mFragments.add(AllOrderFragment.newInstance());
//        mFragments.add(WaitPayOrderFragment.newInstance());
//        mFragments.add(WaitApproveOrderFragment.newInstance());
//        mFragments.add(WaitDeliveryOrderFragment.newInstance());
//        mFragments.add(WaitReceiveOrderFragment.newInstance());
//        mFragments.add(WaitCommendOrderFragment.newInstance());
//        mTitles.add("全部订单");
//        mTitles.add("待付款");
//        mTitles.add("待审核");
//        mTitles.add("待发货");
//        mTitles.add("待收货");
//        mTitles.add("待评价");
//        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
//        mViewPager.setAdapter(mAdapter);
//        mTab.setViewPager(mViewPager);
//        // mViewPager.setOffscreenPageLimit(5);
//        int position = getIntent().getIntExtra("currentPosition", 0);
//        mViewPager.setCurrentItem(position);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
