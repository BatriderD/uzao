package com.zhaotai.uzao.ui.main.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.DynamicBodyBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.MultiMainBean;
import com.zhaotai.uzao.constants.DynamicType;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.goods.activity.DesignProductListActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialCategoryActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.design.activity.BlenderDesignActivity;
import com.zhaotai.uzao.ui.main.adapter.MainChildAdapter;
import com.zhaotai.uzao.ui.main.contract.MainChildFragmentContract;
import com.zhaotai.uzao.ui.main.presenter.MainChildFragmentPresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.widget.decoration.MainChildItemDecoration;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import me.jessyan.autosize.internal.CancelAdapt;


/**
 * description: 首页
 * author : liYou
 * date: 2018/3/2
 */

public class MainChildFragment extends BaseFragment implements MainChildFragmentContract.View, OnRefreshListener,
        BaseQuickAdapter.OnItemChildClickListener, CancelAdapt {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private MainChildFragmentPresenter mPresenter;
    private MainChildAdapter mAdapter;
    private Gson gson = new Gson();
    private UITipDialog mLoadingDialog;


    public static MainChildFragment newInstance() {
        return new MainChildFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_main_child;
    }

    @Override
    public void initView() {
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                EventBus.getDefault().post(new EventMessage(EventBusEvent.REFRESH_MAIN_DATA));
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new MainChildFragmentPresenter(_mActivity, this);
    }

    @Override
    public void initData() {
        autoRefresh();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, GlobalVariable.SWIPE_DRAG_RATE);
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (mPresenter != null)
            mPresenter.getData();
    }

    @Override
    public void bindData(final List<MultiMainBean> data) {
        if (mAdapter == null) {
            mRecycler.addItemDecoration(new MainChildItemDecoration(20, 10, data));
            mAdapter = new MainChildAdapter(data);
            mAdapter.setOnItemChildClickListener(this);
            mRecycler.setAdapter(mAdapter);
            mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
                @Override
                public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                    switch (data.get(position).getItemType()) {
                        case MultiMainBean.TYPE_RECOMMEND_SPU://推荐商品
                        case MultiMainBean.TYPE_RECOMMEND_MATERIAL://推荐素材
                            return 1;
                        default:
                            return 2;
                    }
                }
            });
        } else {
            mAdapter.setNewData(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startBannerPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopBannerPlay();
        }
    }

    @Override
    public void showDLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new UITipDialog.Builder(_mActivity)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(getString(R.string.loading))
                .create();
        mLoadingDialog.show();
    }

    @Override
    public void dismissDLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 条目点击
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        MultiMainBean item = (MultiMainBean) adapter.getItem(position);
        if (item == null) return;
        switch (view.getId()) {
            case R.id.iv_main_child_recommend_spu_pic://推荐商品
                DynamicBodyBean body = gson.fromJson(item.value.contentBody, DynamicBodyBean.class);
                if (body != null) {
                    CommodityDetailMallActivity.launch(_mActivity, body.sequenceNBR);
                }
                break;
            case R.id.tv_main_child_recommend_spu_btn_design://商品定制
                DynamicBodyBean sampleBody = gson.fromJson(item.value.contentBody, DynamicBodyBean.class);
                if (sampleBody != null) {
                    if ("3d".equals(sampleBody.designType)) {
                        BlenderDesignActivity.launch(_mActivity, sampleBody.sequenceNBR);
                    } else {
                        mPresenter.toEdit(sampleBody.sequenceNBR);
                    }
                }
                break;
            case R.id.iv_main_child_recommend_material_pic://推荐素材
                DynamicBodyBean materialBody = gson.fromJson(item.value.contentBody, DynamicBodyBean.class);
                if (materialBody != null) {
                    if (item.value.referType.equals(DynamicType.MATERIAL_DETAIL)) {
                        MaterialDetailActivity.launch(_mActivity, materialBody.sequenceNBR);
                    } else if (item.value.referType.equals(DynamicType.THEME_DETAIL)) {
                        ThemeDetailActivity.launch(_mActivity, materialBody.sequenceNBR);
                    }
                }
                break;
            case R.id.tv_main_child_recommend_material_btn_design://素材定制
                DynamicBodyBean materialDesignBody = gson.fromJson(item.value.contentBody, DynamicBodyBean.class);
                if (item.value.referType.equals(DynamicType.MATERIAL_DETAIL)) {
//                    MaterialDetailBean detailBean = new MaterialDetailBean();
//                    detailBean.thumbnail = materialDesignBody.thumbnail;
//                    detailBean.sourceMaterialId = materialDesignBody.sequenceNBR;
//                    detailBean.sequenceNBR = materialDesignBody.sequenceNBR;
//                    DesignProductListActivity.launch(_mActivity, detailBean);
                    //根据素材id 获取素材详情
                    mPresenter.toDesignProductListWithMaterial(materialDesignBody.sequenceNBR);
                }
                break;
            case R.id.tv_main_child_bottom_more://更多
                switch (item.referType) {
                    case DynamicType.CARRIER_DETAIL://商品列表
                        DesignProductListActivity.launch(_mActivity);
                        break;
                    case DynamicType.MATERIAL_DETAIL://素材列表
                        MaterialCategoryActivity.launch(_mActivity);
                        break;
                }
                break;
            case R.id.iv_popular_design_view_pager_go_to_design://去设计
                String contentBody = (String) view.getTag();
                if (!StringUtil.isEmpty(contentBody)) {
                    DynamicBodyBean productBody = gson.fromJson(contentBody, DynamicBodyBean.class);
                    if (productBody != null)
                        mPresenter.toEdit(productBody.sequenceNBR);
                }
                break;
        }
    }
}
