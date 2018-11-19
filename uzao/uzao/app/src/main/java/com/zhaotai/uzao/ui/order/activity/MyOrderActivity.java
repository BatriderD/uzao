package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.ui.order.fragment.AllOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitApproveOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitCommendOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitDeliveryOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitPayOrderFragment;
import com.zhaotai.uzao.ui.order.fragment.WaitReceiveOrderFragment;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 我的订单
 */

public class MyOrderActivity extends BaseFragmentActivity implements CancelAdapt{

    @BindView(R.id.order_tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    public static void launch(Context context, int currentPosition) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.putExtra("currentPosition", currentPosition);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_order);
        mTitle.setText("我的订单");
        mFragments.add(AllOrderFragment.newInstance());
        mFragments.add(WaitPayOrderFragment.newInstance());
        mFragments.add(WaitApproveOrderFragment.newInstance());
        mFragments.add(WaitDeliveryOrderFragment.newInstance());
        mFragments.add(WaitReceiveOrderFragment.newInstance());
        mFragments.add(WaitCommendOrderFragment.newInstance());
        mTitles.add("全部订单");
        mTitles.add("待付款");
        mTitles.add("待审核");
        mTitles.add("待发货");
        mTitles.add("待收货");
        mTitles.add("待评价");
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTab.setViewPager(mViewPager);
        int position = getIntent().getIntExtra("currentPosition", 0);
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void initData() {

    }

}
