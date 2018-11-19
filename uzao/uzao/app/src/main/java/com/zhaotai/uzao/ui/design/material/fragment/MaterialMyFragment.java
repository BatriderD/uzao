package com.zhaotai.uzao.ui.design.material.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.design.material.adapter.MaterialMyShowListAdapter;
import com.zhaotai.uzao.ui.design.material.contract.MaterialListMyContract;
import com.zhaotai.uzao.ui.design.material.presenter.MaterialMyListPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的素材fragment
 * 包括已购素材，收藏的素材，我上传的素材。
 * author : ZP
 * date: 2018/3/9 0009.
 */

public class MaterialMyFragment extends BaseFragment implements MaterialListMyContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , OnRefreshListener {

    //二级目录关键字 标识
    private static final String CODE = "STICKER_TAG";
    private static final int SPANCOUNT = 2;


    @BindView(R.id.swipe)
    RefreshLayout mSwipe;

    // 中心recycleView
    @BindView(R.id.recycler)
    public RecyclerView recycler;

    @BindView(R.id.rl_collection)
    public RelativeLayout rlCollection;

    @BindView(R.id.rl_bought_material)
    public RelativeLayout rlBought;

    @BindView(R.id.rl_upload_material)
    public RelativeLayout rlUpload;

    @BindView(R.id.tv_collection_tab_name)
    public TextView collectionName;

    @BindView(R.id.tv_bought_material_tab_name)
    public TextView boughtName;

    @BindView(R.id.tv_tv_upload_material_tab_name)
    public TextView upLoadName;

    @BindView(R.id.v_collection_tab_line)
    public View collectionLine;

    @BindView(R.id.v_bought_material_tab_line)
    public View boughtLine;

    @BindView(R.id.v_upload_material_tab_line)
    public View upLoadLine;

    // 0 已收藏 1 已购买 2 已上传
    private int type = 0;
    private MaterialMyListPresenter mPresenter;

    private PageInfo<MaterialListBean> data;
    private MaterialMyShowListAdapter mAdapter;


    /**
     * 返回当前选中分类
     * @return 选中分类
     */
    public int getType() {
        return type;
    }

    public static MaterialMyFragment newInstance() {
        return new MaterialMyFragment();
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
        return R.layout.fragment_material_my;
    }

    @Override
    public void initView() {
        mSwipe.setOnRefreshListener(this);

        mAdapter = new MaterialMyShowListAdapter();
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter.setOnLoadMoreListener(this, recycler);
        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //使用这个素材
                MaterialDetailBean materialDetailBean = mAdapter.getData().get(position);

                AddEditorMaterialBean addEditorMaterialBean = new AddEditorMaterialBean();
                addEditorMaterialBean.thumbnail = materialDetailBean.thumbnail;
                addEditorMaterialBean.sourceMaterialId = materialDetailBean.sourceMaterialId;
                addEditorMaterialBean.fileMime = materialDetailBean.fileMime;
                addEditorMaterialBean.resizeScale = Float.valueOf(materialDetailBean.scale);
                EventBus.getDefault().post(addEditorMaterialBean);
                getActivity().finish();
            }
        });
        //分割线
        recycler.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
        mAdapter.setEmptyStateView(getActivity(), R.mipmap.ic_state_empty_1, "抱歉没有内容,去别处逛逛吧");
    }

    @Override
    public void initPresenter() {

        mPresenter = new MaterialMyListPresenter(getActivity(), this);
    }

    @Override
    public void initData() {
        showLoading();
//        获取已购素材列表
        collectionName.setSelected(true);
        upLoadName.setSelected(false);
        collectionLine.setSelected(false);
        collectionLine.setVisibility(View.VISIBLE);
        boughtLine.setVisibility(View.INVISIBLE);
        upLoadLine.setVisibility(View.INVISIBLE);
        getCurrentList(0);
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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getCurrentList(0);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            getCurrentList(start);
        } else {
            mAdapter.loadMoreEnd();
        }
    }


    /**
     * 切换标签 收藏的素材，以后素材，上传的素材
     *
     * @param view 点击的素材
     */
    @OnClick({R.id.rl_collection, R.id.rl_bought_material, R.id.rl_upload_material})
    public void changeTabList(View view) {
        switch (view.getId()) {
            case R.id.rl_collection:
                collectionName.setSelected(true);
                upLoadName.setSelected(false);
                boughtName.setSelected(false);
                collectionLine.setVisibility(View.VISIBLE);
                boughtLine.setVisibility(View.INVISIBLE);
                upLoadLine.setVisibility(View.INVISIBLE);

                type = 0;
                getCurrentList(0);
                break;
            case R.id.rl_bought_material:
                collectionName.setSelected(false);
                boughtName.setSelected(true);
                upLoadName.setSelected(false);
                collectionLine.setVisibility(View.INVISIBLE);
                boughtLine.setVisibility(View.VISIBLE);
                upLoadLine.setVisibility(View.INVISIBLE);
                type = 1;
                getCurrentList(0);
                break;
            case R.id.rl_upload_material:
                collectionName.setSelected(false);
                boughtName.setSelected(false);
                upLoadName.setSelected(true);
                collectionLine.setVisibility(View.INVISIBLE);
                boughtLine.setVisibility(View.INVISIBLE);
                upLoadLine.setVisibility(View.VISIBLE);
                type = 2;
                getCurrentList(0);
                break;
        }
    }

    /**
     * 获取指定的列表
     * type 0 已收藏 1 已购买 2 已上传
     *
     * @param start 列表起始位置
     */
    public void getCurrentList(int start) {
        mAdapter.setType(type);
        switch (type) {
            case 0:
                mPresenter.getMyCollectMaterial(start, false);
                break;

            case 1:
                mPresenter.getMyBoughtMaterial(start, false);
                break;
            case 2:
                mPresenter.getUpLoadMaterialList(start, false);
                break;


        }
    }

    /**
     * 设置列表
     *
     * @param info 分页的列表信息
     */
    @Override
    public void showContentList(PageInfo info) {
        this.data = info;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
            //如果是首页 就设置新数据
            mAdapter.setNewData(info.list);
        } else {
            //不是第一页 就刷新
            mAdapter.addData(info.list);
        }
    }
}
