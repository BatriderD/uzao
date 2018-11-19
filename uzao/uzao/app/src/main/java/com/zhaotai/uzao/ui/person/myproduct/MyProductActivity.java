package com.zhaotai.uzao.ui.person.myproduct;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.ui.person.myproduct.fragment.AllProductFragment;
import com.zhaotai.uzao.ui.person.myproduct.fragment.CheckProductFragment;
import com.zhaotai.uzao.ui.person.myproduct.fragment.PublishProductFragment;
import com.zhaotai.uzao.ui.person.myproduct.fragment.UnApprovedProductFragment;
import com.zhaotai.uzao.ui.person.myproduct.fragment.UnPublishProductFragment;
import com.zhaotai.uzao.ui.person.myproduct.fragment.UnReviewedProductFragment;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Time: 2017/5/23
 * Created by LiYou
 * Description : 我的商品
 */

public class MyProductActivity extends BaseFragmentActivity {

    @BindView(R.id.slid_tab_my_product_tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_my_product_content)
    ViewPager mViewPager;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MyProductActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_product);
        mTitle.setText("我的商品");
        //全部商品
        mFragments.add(AllProductFragment.newInstance());
        mTitles.add(getString(R.string.all_product));
        //未审核
        mFragments.add(UnReviewedProductFragment.newInstance());
        mTitles.add("未审核");
        //审核中
        mFragments.add(CheckProductFragment.newInstance());
        mTitles.add(getString(R.string.audit_product));
        //未发布商品
        mFragments.add(UnPublishProductFragment.newInstance());
        mTitles.add(getString(R.string.un_publish_product));
        //已发布商品
        mFragments.add(PublishProductFragment.newInstance());
        mTitles.add(getString(R.string.published_product));
        //审核未通过
        mFragments.add(UnApprovedProductFragment.newInstance());
        mTitles.add("未通过");

        if (mAdapter == null) {
            mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
            mViewPager.setAdapter(mAdapter);
            mTab.setViewPager(mViewPager);
        }
        mViewPager.setOffscreenPageLimit(4);
    }

    @Override
    protected void initData() {

    }

}
