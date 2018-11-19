package com.zhaotai.uzao.ui.person.collection.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.person.collection.CollectThemeSearchActivity;
import com.zhaotai.uzao.ui.person.collection.OnChangeFragmentInterface;
import com.zhaotai.uzao.ui.person.collection.adapter.CollectionThemeListAdapter;
import com.zhaotai.uzao.ui.person.collection.contract.CollectionThemeContract;
import com.zhaotai.uzao.ui.person.collection.model.CategoryCodeBean;
import com.zhaotai.uzao.ui.person.collection.presenter.CollectionThemePresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.ui.theme.activity.ThemeListActivity;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/13
 * Created by LiYou
 * Description : 收藏主题页面
 */

public class CollectionThemeFragment extends BaseFragment implements CollectionThemeContract.View, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rc_person_collection_product_content)
    RecyclerView mRecycler;

    @BindView(R.id.ll_collect_product)
    LinearLayout mLlTab;

    //管理按钮
    @BindView(R.id.tv_collect_product_title_manager)
    TextView mTitleManager;

    @BindView(R.id.ll_collect_product_bottom)
    RelativeLayout mLlBottom;

    @BindView(R.id.iv_collect_product_all_choose)
    ImageView mImageAllChoose;

    @BindView(R.id.view_collect_product_tab_bottom_line_1)
    View mBottomLine1;
    @BindView(R.id.view_collect_product_tab_bottom_line_2)
    View mBottomLine2;

    private CollectionThemeListAdapter mAdapter;
    private CollectionThemePresenter mPresenter;
    private PageInfo<ThemeBean> data = new PageInfo<>();
    private boolean allSelectState = false;//记录是否全选
    private boolean isSelectState = false;//默认不可以管理
    private Map<String, String> params = new HashMap<>();

    public static final String STATUS_NORMAL = "";//全部主题
    public static final String STATUS_SOLD_OUT = "disable";//下架主题

    private String status = STATUS_NORMAL;

    public static CollectionThemeFragment newInstance() {
        return new CollectionThemeFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_collect_theme;
    }

    @Override
    public void initView() {
        mRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        mAdapter = new CollectionThemeListAdapter();
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(this, mRecycler);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final ThemeBean info = (ThemeBean) adapter.getItem(position);
                if (info == null) return;
                if (mAdapter.getSelectState()) {
                    info.selected = !info.selected;
                    mAdapter.notifyItemChanged(position);
                    if (mPresenter.checkSelectState(mAdapter.getData())) {
                        mImageAllChoose.setImageResource(R.drawable.icon_circle_selected);
                    } else {
                        mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
                    }
                } else if (!"I".equals(info.recStatus)) {
                    ThemeDetailActivity.launch(_mActivity, info.themeId);
                }
            }
        });

        mAdapter.setEmptyStateView(_mActivity, R.mipmap.ic_state_empty_collect, "快选几件好物放进来吧", getString(R.string.empty_btn), new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                ThemeListActivity.launch(getActivity());
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new CollectionThemePresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        mPresenter.getCollectThemeList(0, params, status);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 切换标题栏
     */
    @OnClick(R.id.tv_collect_top_left)
    public void onClickTopLeft() {
        ((OnChangeFragmentInterface) _mActivity).changeFragment(0);
    }

    /**
     * 主题
     */
    @OnClick(R.id.tv_collect_top_right)
    public void onClickTopRight() {
        ((OnChangeFragmentInterface) _mActivity).changeFragment(1);
    }

    @OnClick(R.id.tool_back)
    public void onClickBack() {
        ((OnChangeFragmentInterface) _mActivity).finishView();
    }


    /**
     * 搜索
     */
    @OnClick(R.id.tv_collect_product_title_search)
    public void onClickSearch() {
        CollectThemeSearchActivity.launch(_mActivity);
    }

    /**
     * 管理
     */
    @OnClick(R.id.tv_collect_product_title_manager)
    public void onClickManager() {
        if (!isSelectState) return;

        if (mTitleManager.getText().equals("管理")) {
            mTitleManager.setText("取消");
            mLlBottom.setVisibility(View.VISIBLE);
            mAdapter.setSelectState(true);
        } else {
            mTitleManager.setText("管理");
            mLlBottom.setVisibility(View.GONE);
            //重置选中状态
            allSelectState = false;
            mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
            mPresenter.changeSelectState(data.list, false);
            mAdapter.setSelectState(false);
        }
    }

    /**
     * 全部主题
     */
    @OnClick(R.id.ll_collect_product_all_theme)
    public void onClickAllGoods() {
        changeBottomLine(1);
        hideDelete();
        status = STATUS_NORMAL;
        mPresenter.getCollectThemeList(0, params, status);
    }

    /**
     * 失效主题
     */
    @OnClick(R.id.tv_collect_product_out_theme)
    public void onClickDownPrice() {
        changeBottomLine(2);
        status = STATUS_SOLD_OUT;
        hideDelete();
        mPresenter.getCollectThemeList(0, params, status);
    }

    /**
     * 隐藏删除
     */
    private void hideDelete() {
        mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
        allSelectState = false;
        mTitleManager.setText("管理");
        mLlBottom.setVisibility(View.GONE);
        mAdapter.setSelectState(false);
    }

    /**
     * 取消收藏主题
     */
    @OnClick(R.id.tv_collect_product_delete)
    public void onClickDelete() {
        //删除收藏的主题
        AlertDialog.Builder alert = new AlertDialog.Builder(_mActivity);
        alert.setMessage("是否删除此主题");
        alert.setNeutralButton("取消", null);
        alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击删除
                mPresenter.cancelCollection(mAdapter.getData());
            }
        });
        alert.show();
    }

    /**
     * 全选
     */
    @OnClick(R.id.ll_collect_product_bottom)
    public void onClickAllSelect() {
        if (allSelectState) {
            allSelectState = false;
            //变换到未选中
            mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
            mPresenter.changeSelectState(mAdapter.getData(), false);
            mAdapter.notifyDataSetChanged();
        } else {
            //变换到全选状态
            mImageAllChoose.setImageResource(R.drawable.icon_circle_selected);
            allSelectState = true;
            mPresenter.changeSelectState(mAdapter.getData(), true);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCollectionThemeList(PageInfo<ThemeBean> list) {
        data = list;
        if (data.list.size() > 0) {
            isSelectState = true;
            if (allSelectState) {
                mPresenter.changeSelectState(data.list, true);
            }
        } else {
            isSelectState = false;
            mTitleManager.setText("管理");
            mLlBottom.setVisibility(View.GONE);
        }

        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(data.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(data.list);
        }
    }

    /**
     * 取消收藏
     */
    @Override
    public void cancelCollection() {
        mPresenter.getCollectThemeList(0, params, status);
    }

    @Override
    public void showCollectionThemeCategoryCode(List<CategoryCodeBean> categoryCodeList) {

    }

    /**
     * 收藏商品数量
     */
    @Override
    public void showProductNum(String num) {
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getCollectThemeList(start, params, status);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    public void changeBottomLine(int position) {
        switch (position) {
            case 1:
                mBottomLine1.setVisibility(View.VISIBLE);
                mBottomLine2.setVisibility(View.GONE);
                break;
            case 2:
                mBottomLine1.setVisibility(View.GONE);
                mBottomLine2.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }

}
