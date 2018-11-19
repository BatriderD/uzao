package com.zhaotai.uzao.ui.person.discount.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.activity.ProtocolActivity;
import com.zhaotai.uzao.ui.person.discount.fragment.OverdueFragment;
import com.zhaotai.uzao.ui.person.discount.fragment.UnusedFragment;
import com.zhaotai.uzao.ui.person.discount.fragment.UsedFragment;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/7/18
 * Created by LiYou
 * Description : 我的优惠券
 */

public class MyDiscountCouponActivity extends BaseFragmentActivity {

    @BindView(R.id.st_my_discount_tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_my_discount)
    ViewPager mViewPager;

    @BindView(R.id.right_btn)
    Button mRightBtn;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MyDiscountCouponActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_discount_coupon);
        mTitle.setText("我的优惠券");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("说明");
        //未使用
        mFragments.add(UnusedFragment.newInstance());
        mTitles.add("未使用");
        //已使用
        mFragments.add(UsedFragment.newInstance());
        mTitles.add("已使用");
        //已失效
        mFragments.add(OverdueFragment.newInstance());
        mTitles.add("已失效");

        if (mAdapter == null) {
            mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
            mViewPager.setAdapter(mAdapter);
            mTab.setViewPager(mViewPager);
        }
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.right_btn)
    public void onClickRightBtn() {
        ProtocolActivity.launch(mContext, GlobalVariable.PROTOCOL_DISCOUNT);
    }
}
