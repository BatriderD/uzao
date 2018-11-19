package com.zhaotai.uzao.ui.person.attention;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.ui.person.attention.fragment.AttentionBrandFragment;
import com.zhaotai.uzao.ui.person.attention.fragment.AttentionDesignerFragment;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/22
 * Created by LiYou
 * Description : 关注
 */

public class AttentionActivity extends BaseFragmentActivity {

    @BindView(R.id.attention_tab)
    SlidingTabLayout mSlideTab;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @BindView(R.id.tool_bar_right_img)
    ImageView mRightImg;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();


    public static void launch(Context context) {
        context.startActivity(new Intent(context, AttentionActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_attention);
        mTitle.setText("关注");
        mFragments.add(AttentionDesignerFragment.newInstance());
        mFragments.add(AttentionBrandFragment.newInstance());
        mTitles.add("设计师");
        mTitles.add("品牌");
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
        mSlideTab.setViewPager(mViewPager);
        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.icon_search_black);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.tool_bar_right_img)
    public void onClickSearch() {
        switch (mSlideTab.getCurrentTab()) {
            case 0:
                //设计师
                AttentionDesignerSearchActivity.launch(mContext);
                break;
            case 1:
                //品牌
                AttentionBrandSearchActivity.launch(mContext);
                break;
        }
    }
}
