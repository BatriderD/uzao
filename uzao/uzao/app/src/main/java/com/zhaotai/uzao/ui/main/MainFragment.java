package com.zhaotai.uzao.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.EventBean.UnReadMessageEvent;
import com.zhaotai.uzao.bean.MainTabBean;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.main.adapter.MainFragmentPagerAdapter;
import com.zhaotai.uzao.ui.main.adapter.MainItemSelectAdapter;
import com.zhaotai.uzao.ui.main.contract.MainFragmentNewContract;
import com.zhaotai.uzao.ui.main.fragment.CustomFragment;
import com.zhaotai.uzao.ui.main.fragment.MainChildFragment;
import com.zhaotai.uzao.ui.main.fragment.RecommendFragment;
import com.zhaotai.uzao.ui.main.presenter.MainFragmentPresenter;
import com.zhaotai.uzao.ui.person.message.activity.MessageCenterActivity;
import com.zhaotai.uzao.ui.search.MainSearchActivity;
import com.zhaotai.uzao.ui.util.ScanQrCodeActivity;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * description: 新版主页
 * author : ZP
 * date: 2018/3/21 0021.
 */

public class MainFragment extends BaseFragment implements MainFragmentNewContract.View, BaseQuickAdapter.OnItemClickListener {
    //标签列表
    @BindView(R.id.main_tab)
    public SlidingTabLayout mTab;
    //viewPager 内容
    @BindView(R.id.vp_content)
    public ViewPager mViewPager;
    //所有类别
    @BindView(R.id.ll_all_item)
    public LinearLayout ll_all;
    //搜索条
    @BindView(R.id.tv_home_home_search)
    TextView mTvSearch;
    //收起更多的箭头
    @BindView(R.id.iv_up_arr)
    public ImageView iv_dismiss_arr;
    //未读消息图标
    @BindView(R.id.iv_home_bell_num)
    public ImageView ivUnReadView;
    //展示更多的箭头
    @BindView(R.id.iv_down_arr)
    public ImageView iv_show_arr;
    //所有的分类
    @BindView(R.id.rc_all_item)
    public RecyclerView rc_all_item;
    //未读消息红点控件
    private Badge qBadgeView;

    private MainFragmentPresenter mPresenter;
    private MainFragmentPagerAdapter mAdapter;
    //fragments
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //标题
    private ArrayList<String> mTitles = new ArrayList<>();
    //adapter
    private MainItemSelectAdapter mainItemSelectAdapter;
    private SimpleDividerItemDecoration simpleDividerItemDecoration = new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(17));


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_main;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        qBadgeView = new QBadgeView(getActivity()).bindTarget(ivUnReadView).setBadgeGravity(Gravity.CENTER)
                .setBadgeTextSize(8, true);
    }

    @Override
    public void initPresenter() {
        mPresenter = new MainFragmentPresenter(getActivity(), this);
    }


    boolean isFirst = true;

    @Override
    public void initData() {
        if (isFirst) {
            //第一次初始话固定标签
            initHeadFragment();
        }
        //初始化默认标签
        mPresenter.getTabData();
        //请求唯独消息
        mPresenter.getUnHandleMessage();
    }

    private void initHeadFragment() {
        mFragments.add(MainChildFragment.newInstance());
        mFragments.add(new RecommendFragment());
        mTitles.add("首页");
        mTitles.add("推荐");
        mAdapter = new MainFragmentPagerAdapter(this.getChildFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTab.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mainItemSelectAdapter != null) {
                    if (mTitles.size() >= position)
                        mainItemSelectAdapter.setmItem(mTitles.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        初始化标签 和recycleView
        mainItemSelectAdapter = new MainItemSelectAdapter();
        mainItemSelectAdapter.setOnItemClickListener(this);
        rc_all_item.setAdapter(mainItemSelectAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false);
        rc_all_item.setLayoutManager(gridLayoutManager);

        rc_all_item.removeItemDecoration(simpleDividerItemDecoration);
        rc_all_item.addItemDecoration(simpleDividerItemDecoration);
    }

    @Override
    protected boolean hasLazy() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 收起标签面板
     */
    @OnClick(R.id.view_item_bg)
    public void onClickHideItemBg() {
        mTab.setVisibility(View.VISIBLE);
        ll_all.setVisibility(View.GONE);
    }

    /**
     * 扫一扫
     */
    @OnClick(R.id.iv_home_scan)
    public void onClickScan() {
        if (LoginHelper.getLoginStatus()) {
            ScanQrCodeActivity.launch(_mActivity);
        } else {
            LoginActivity.launch(_mActivity);
        }
    }

//    /**
//     * 设置未读
//     *
//     * @param info 个人消息数据
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(PersonInfo info) {
//        switch (info.code) {
//            //刷新消息未读消息数量
//            case EventBusEvent.REFRESH_UNREAD_COUNT:
//                mPresenter.getUnHandleMessage();
//                break;
//        }
//    }

    /**
     * 通知消息
     *
     * @param info 消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage info) {
        if (EventBusEvent.REFRESH_MAIN_DATA.equals(info.eventType)) {
            mPresenter.getTabData();
        }
    }


    /**
     * 展示标签数据
     *
     * @param children 其他页面数据
     */
    @Override
    public void addTabList(List<MainTabBean> children) {
        for (MainTabBean bean : children) {
            if (bean != null) {
                BaseFragment baseFragment = initFragments(bean);
                if (baseFragment != null) {
                    mFragments.add(baseFragment);
                    mTitles.add(bean.navigateName);
                    mAdapter.notifyDataSetChanged();
                    mTab.setViewPager(mViewPager);
                }
            }
        }
        if (mTitles.size() > 0) {
            mainItemSelectAdapter.setmItem(mTitles.get(0));
            mainItemSelectAdapter.setNewData(mTitles);
            rc_all_item.setPadding(0, 22, 0, 0);
        }

    }

    /**
     * 显示未读的消息数目
     *
     * @param integer 未读消息数目
     */
    @Override
    public void showUnHandleMessage(Integer integer) {
        Log.d(this.getClass().toString(), "主页 showUnHandleMessage: ");
        if (qBadgeView != null) {
            qBadgeView.setBadgeNumber(integer);
        }
    }

    /**
     * 初始化其他的fragment
     *
     * @param bean tab标签
     * @return 基础的frag
     */
    private BaseFragment initFragments(MainTabBean bean) {
        BaseFragment baseFragment = null;
        if (bean.associateType != null && bean.navigateCode != null) {
            switch (1) {
                case 1:
                    baseFragment = CustomFragment.newInstance(bean.navigateCode);
                    break;
            }
        }
        return baseFragment;

    }


    /**
     * 展示更多标签消息
     */
    @OnClick(R.id.iv_down_arr)
    public void onShowArr() {
        mTab.setVisibility(View.INVISIBLE);
        ll_all.setVisibility(View.VISIBLE);
    }

    /**
     * 收起标签
     */
    @OnClick(R.id.iv_up_arr)
    public void onDisMissArr() {
        mTab.setVisibility(View.VISIBLE);
        ll_all.setVisibility(View.GONE);
    }

    /**
     * 搜索按钮 进入搜索页面
     */
    @OnClick(R.id.tv_home_home_search)
    public void onSearch() {
        if (mTvSearch.getHint() == null) {
            MainSearchActivity.launch(getActivity());
        } else {
            MainSearchActivity.launch(getActivity(), mTvSearch.getHint().toString());
        }
    }

    /**
     * 消息按钮 进入消息页面
     */
    @OnClick(R.id.iv_home_home_bell)
    public void onMessageClick() {
        if (LoginHelper.getLoginStatus()) {
            MessageCenterActivity.launch(_mActivity);
        } else {
            LoginActivity.launch(_mActivity);
        }
    }

    /**
     * 设置预置关键词
     */
    public void setPresetSearchWord(String word) {
        mTvSearch.setHint(word);
    }

    /**
     * 标签按钮的点击，切换到制定fragment
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        //标签点击
        //收起面板
        onDisMissArr();
        //设置显示对应fragment 和对应标签选中
        if (mAdapter != null) {
            mViewPager.setCurrentItem(position);
            mainItemSelectAdapter.setmItem(mTitles.get(position));
        }
    }

    /**
     * 未读消息
     *
     * @param unReadMessageEvent 未消息数目
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnReadMessageEvent unReadMessageEvent) {
        qBadgeView.setBadgeNumber(unReadMessageEvent.messageCount);
    }


    /**
     * 销毁页面 取消eventBus监听
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
