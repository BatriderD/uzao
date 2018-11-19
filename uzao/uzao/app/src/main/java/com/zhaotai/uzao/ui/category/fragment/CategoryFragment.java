package com.zhaotai.uzao.ui.category.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.CategoryLeftAdapter;
import com.zhaotai.uzao.adapter.CategoryRightSectionAdapter;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.CategoryMultiRightBean;
import com.zhaotai.uzao.bean.DynamicBodyBean;
import com.zhaotai.uzao.bean.EventBean.UnReadMessageEvent;
import com.zhaotai.uzao.bean.MainTabBean;
import com.zhaotai.uzao.constants.DynamicType;
import com.zhaotai.uzao.ui.category.goods.activity.CategoryListActivity;
import com.zhaotai.uzao.ui.category.goods.contract.CateGoryContract;
import com.zhaotai.uzao.ui.category.goods.presenter.CateGoryPresenter;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.person.message.activity.MessageCenterActivity;
import com.zhaotai.uzao.ui.search.CollectionSearchActivity;
import com.zhaotai.uzao.ui.search.CommodityAndSearchActivity;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.ui.web.SpecialActivityWebActivity;
import com.zhaotai.uzao.ui.web.WebActivity;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;

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
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 分类列表
 */

public class CategoryFragment extends BaseFragment implements CateGoryContract.View {

    @BindView(R.id.tool_back)
    ImageView mBack;

    @BindView(R.id.iv_category_bell_num)
    public ImageView ivUnReadView;
    @BindView(R.id.iv_category_bell)
    public ImageView mBell;

    @BindView(R.id.category_left_recycler)
    RecyclerView leftRecycler;

    @BindView(R.id.category_right_recycler)
    RecyclerView rightRecycler;

    @BindView(R.id.tv_category_search)
    TextView mHint;

    private CategoryLeftAdapter mLeftAdapter;
    private CategoryRightSectionAdapter mRightAdapter;
    //记录上一个点击的位置
    private int pressIndex = 0;
    private CateGoryPresenter mPresenter;
    private Badge qBadgeView;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_category;
    }

    @Override
    public void initView() {
        mBell.setVisibility(View.VISIBLE);
        mBack.setVisibility(View.GONE);
        ivUnReadView.setVisibility(View.VISIBLE);
        qBadgeView = new QBadgeView(getActivity()).bindTarget(ivUnReadView).setBadgeGravity(Gravity.CENTER)
                .setBadgeTextSize(8, true);
        //左边列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        leftRecycler.setLayoutManager(linearLayoutManager);
        mLeftAdapter = new CategoryLeftAdapter();
        leftRecycler.setAdapter(mLeftAdapter);
        //右边列表
        GridLayoutManager mGridManager = new GridLayoutManager(_mActivity, 3, GridLayoutManager.VERTICAL, false);
        rightRecycler.setLayoutManager(mGridManager);
        mRightAdapter = new CategoryRightSectionAdapter(new ArrayList<CategoryMultiRightBean>());
        rightRecycler.setAdapter(mRightAdapter);

        mRightAdapter.addHeaderView(View.inflate(_mActivity, R.layout.category_header, null));
        mRightAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                CategoryMultiRightBean item = mRightAdapter.getItem(position);
                if (item != null && item.getItemType() == CategoryMultiRightBean.TYPE_SECTION_HEADER) {
                    return 3;
                }
                return 1;
            }
        });
        //一级类目条目点击
        mLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (pressIndex == position) {
                    return;
                } else {
                    MainTabBean oldItem = (MainTabBean) adapter.getItem(pressIndex);
                    MainTabBean nowItem = (MainTabBean) adapter.getItem(position);
                    if (oldItem != null && nowItem != null) {
                        oldItem.isChecked = false;
                        nowItem.isChecked = true;
                        pressIndex = position;
                        mLeftAdapter.notifyDataSetChanged();
                    }
                }
                MainTabBean item = (MainTabBean) adapter.getItem(position);
                if (item != null && item.hasChildren)
                    mRightAdapter.setNewData(mPresenter.getRightMultiList(item.children));
            }
        });
        //二级类目条目点击
        mRightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryMultiRightBean item = mRightAdapter.getItem(position);
                if (item != null && !StringUtil.isEmpty(item.associateType)) {
                    switch (item.associateType) {
                        case DynamicType.PAGE://专题
                            Gson gson = new Gson();
                            DynamicBodyBean body = gson.fromJson(item.associateData, DynamicBodyBean.class);
                            SpecialActivityWebActivity.launch(_mActivity, body.webUrl);
                            break;
                        case DynamicType.THEME://主题
                            ThemeDetailActivity.launch(_mActivity, item.associateData);
                            break;
                        case DynamicType.CUSTOM://自定义网址
                            WebActivity.launch(_mActivity, item.associateData);
                            break;
                        case DynamicType.CATEGORY://导航列表
                            CategoryListActivity.launchNavigate(_mActivity, item.categoryCode, item.categoryName);
                            break;
                        case DynamicType.COLLECTION://集合
                            CollectionSearchActivity.launch(_mActivity, item.categoryName, item.associateData);
                            break;
                    }
                }
            }
        });

        EventBus.getDefault().register(this);
    }

    @Override
    public void initPresenter() {
        mPresenter = new CateGoryPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        //showLoading();
        //不强制更新左侧列表
        //mPresenter.getLeftList(false);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    protected boolean hasLazy() {
        return false;
    }

    /**
     * 设置预置关键词
     */
    public void setPresetSearchWord(String word) {
        mHint.setHint(word);
    }

    @Override
    public void showLeftList(List<CategoryBean> data) {

    }

    @Override
    public void showRightList(List<CategoryBean> data, int position) {

    }

    @OnClick(R.id.iv_category_bell)
    public void onMessage() {
        if (LoginHelper.getLoginStatus()) {
            MessageCenterActivity.launch(_mActivity);
        } else {
            LoginActivity.launch(_mActivity);
        }
    }

    /**
     * 跳转搜索页面
     */
    @OnClick(R.id.tv_category_search)
    public void goToSearch() {
        if (mHint.getHint() == null) {
            CommodityAndSearchActivity.launch(_mActivity);
        } else {
            CommodityAndSearchActivity.launch(_mActivity, mHint.getHint().toString());
        }
    }

    /**
     * 接收导航分类
     *
     * @param mainTabBean 导航分类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainTabBean mainTabBean) {
        if (mLeftAdapter != null && mainTabBean.hasChildren) {
            mainTabBean.children.get(0).isChecked = true;
            mLeftAdapter.setNewData(mainTabBean.children);
            if (mRightAdapter != null && mainTabBean.children.get(0).hasChildren) {
                mRightAdapter.setNewData(mPresenter.getRightMultiList(mainTabBean.children.get(0).children));
            }
        }
    }

    /**
     * 更新未读消息
     *
     * @param unReadMessageEvent 未读消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnReadMessageEvent unReadMessageEvent) {
        qBadgeView.setBadgeNumber(unReadMessageEvent.messageCount);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
