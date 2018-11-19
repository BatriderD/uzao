package com.zhaotai.uzao.ui.person.invite;

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
import com.zhaotai.uzao.ui.person.invite.fragment.InviteDetailFragment;
import com.zhaotai.uzao.ui.person.invite.fragment.InviteFriendFragment;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/12/1
 * Created by LiYou
 * Description : 邀请返利界面
 */

public class InviteActivity extends BaseFragmentActivity {

    @BindView(R.id.order_tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;
    @BindView(R.id.right_btn)
    Button mRightBtn;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    public static void launch(Context context) {
        Intent intent = new Intent(context, InviteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_invite);
        mTitle.setText("邀请返利");
        mFragments.add(InviteFriendFragment.newInstance());
//        mFragments.add(MyAwardFragment.newInstance());
        mFragments.add(InviteDetailFragment.newInstance());
//        mFragments.add(RecordConversionFragment.newInstance());
        mTitles.add("邀请好友");
//        mTitles.add("我的奖励");
        mTitles.add("邀请明细");
//        mTitles.add("兑换记录");
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTab.setViewPager(mViewPager);
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("说明");
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.right_btn)
    public void onClickProtocol() {
        ProtocolActivity.launch(mContext, GlobalVariable.PROTOCOL_REBATE);
    }
}
