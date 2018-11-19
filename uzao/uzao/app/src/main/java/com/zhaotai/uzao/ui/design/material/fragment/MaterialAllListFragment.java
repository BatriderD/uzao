package com.zhaotai.uzao.ui.design.material.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.material.adapter.MaterialAllListAdapter;
import com.zhaotai.uzao.ui.design.material.adapter.MaterialListTabAdapter;
import com.zhaotai.uzao.ui.design.material.contract.MaterialListAllContract;
import com.zhaotai.uzao.ui.design.material.presenter.MaterialAllListPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.MultipleStatusView;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * 所有素材的fragment
 * 结构为tab +tab对应的内容
 * author : ZP
 * date: 2018/3/9 0009.
 */

public class MaterialAllListFragment extends BaseFragment implements MaterialListAllContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , OnRefreshListener {


    //   导航
    @BindView(R.id.rc_tab_bar)
    public RecyclerView rcTab;


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    // 中心recycleview
    @BindView(R.id.recycler)
    public RecyclerView recycler;


    @BindView(R.id.multiple_status_view1)
    public MultipleStatusView multipleStatusView1;

    private MaterialAllListPresenter mPresenter;
    private MaterialListTabAdapter tabAdapter;
    private MaterialAllListAdapter mAdapter;
    private String tabCode;
    private PageInfo<MaterialListBean> data;


    public static MaterialAllListFragment newInstance() {
        return new MaterialAllListFragment();
    }

    @Override
    protected boolean hasLazy() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_material_all;
    }

    @Override
    public void initView() {
        mSwipe.setOnRefreshListener(this);

        rcTab.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        tabAdapter = new MaterialListTabAdapter();
        rcTab.setAdapter(tabAdapter);
        tabAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                tabAdapter.setSelected(position);
                tabCode = tabAdapter.getData().get(position).categoryCode;
                autoRefresh();
            }
        });

        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new MaterialAllListAdapter();
        mAdapter.setOnLoadMoreListener(this, recycler);
        recycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //使用这个素材
                MaterialListBean bean = mAdapter.getData().get(position);
                if (bean.data != null) {
                    AddEditorMaterialBean addEditorMaterialBean = new AddEditorMaterialBean();
                    addEditorMaterialBean.thumbnail = bean.pic;
                    addEditorMaterialBean.sourceMaterialId = bean.id;
                    addEditorMaterialBean.fileMime = bean.data.fileMime;
                    addEditorMaterialBean.resizeScale = bean.data.resizeScale;
                    EventBus.getDefault().post(addEditorMaterialBean);
                } else {
                    ToastUtil.showShort("素材信息错误");
                }
                getActivity().finish();

            }
        });
        mAdapter.setEmptyStateView(getActivity(), R.mipmap.ic_state_empty_1, "抱歉没有内容,去别处逛逛吧");
    }

    @Override
    public void initPresenter() {
        mPresenter = new MaterialAllListPresenter(getActivity(), this);
    }

    @Override
    public void initData() {
        showLoading();
//       获取列表信息
        mPresenter.getTabList();
    }


    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }

    private void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, 1);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        mPresenter.getContentList(0, tabCode, false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getContentList(start, tabCode, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showTabList(List<CategoryBean> lists) {
        if (lists.size() > 0) {
            tabAdapter.setNewData(lists);
            tabAdapter.setSelected(0);
            tabCode = lists.get(0).categoryCode;
            autoRefresh();
        } else {
            showContentError("数据错误");
        }
    }

    @Override
    public void showContentList(PageInfo<MaterialListBean> info) {
        this.data = info;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            multipleStatusView1.showContent();
            mAdapter.setNewData(info.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(info.list);
        }
    }

    @Override
    public void showContentError(String msg) {
        if (multipleStatusView1 != null) {
            if (this.getString(R.string.no_net).equals(msg)) {
                multipleStatusView1.showNoNetwork();
            } else {
                multipleStatusView1.showError();
            }
        }
    }

    public String getCategoryWord() {
        return tabCode;
    }
}
