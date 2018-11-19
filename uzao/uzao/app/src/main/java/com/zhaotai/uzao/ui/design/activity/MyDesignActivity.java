package com.zhaotai.uzao.ui.design.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DesignBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.category.goods.activity.DesignProductListActivity;
import com.zhaotai.uzao.ui.design.adapter.MyDesignAdapter;
import com.zhaotai.uzao.ui.design.contract.MyDesignContract;
import com.zhaotai.uzao.ui.design.presenter.MyDesignPresenter;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/9/5
 * Created by LiYou
 * Description : 我的设计
 */

public class MyDesignActivity extends BaseActivity implements MyDesignContract.View, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.right_btn)
    Button mTitleManager;
    @BindView(R.id.ll_my_design_bottom)
    RelativeLayout mLlBottom;
    @BindView(R.id.iv_my_design_all_choose)
    ImageView mImageAllChoose;

    private MyDesignAdapter mAdapter;
    private MyDesignPresenter mPresenter;
    private boolean allSelectState = false;//记录是否全选
    private boolean isCanSelect = false;

    private PageInfo<DesignBean> data;
    private boolean isFirst = true;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MyDesignActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_design);
        mTitle.setText("我的设计");
        mTitleManager.setVisibility(View.VISIBLE);
        mTitleManager.setText("管理");
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyDesignAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //设置上拉刷新监听
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        //设置子view监听
        mAdapter.setOnItemChildClickListener(this);
        //设置空页面点击
        mAdapter.setEmptyStateView(this, R.mipmap.ic_status_empty_design, "这里一片荒土，啥都没有马上去设计", "去设计", new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                DesignProductListActivity.launch(mContext);
            }
        });
        //设置间距
        SimpleDividerItemDecoration simpleDividerItemDecoration = new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
        mRecyclerView.addItemDecoration(simpleDividerItemDecoration);
        //上拉刷新
        mSwipe.setOnRefreshListener(this);
        mPresenter = new MyDesignPresenter(this, this);
    }

    @Override
    protected void initData() {
        showLoading();
        mPresenter.getMyDesign(0, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
        } else {
            autoRefresh();
        }
    }

    /**
     * 自动刷新回调
     */
    private void autoRefresh() {
        mSwipe.autoRefresh(GlobalVariable.SWIPE_DELAYED, GlobalVariable.SWIPE_DURATION, GlobalVariable.SWIPE_DRAG_RATE);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 管理按钮和取消按钮
     */
    @OnClick(R.id.right_btn)
    public void onClickManage() {
        if (!isCanSelect) return;

        if (mTitleManager.getText().equals("管理")) {
            mTitleManager.setText("取消");
            if (data != null && data.list.size() > 0) {
                mLlBottom.setVisibility(View.VISIBLE);
                mAdapter.setSelectState(true);
            }
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
     * 全选
     */
    @OnClick(R.id.ll_my_design_bottom)
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

    /**
     * 删除
     */
    @OnClick(R.id.tv_my_design_delete)
    public void onClickDelete() {
        final List<DesignBean> data = mAdapter.getData();
        boolean isEmpty = true;
        for (DesignBean bean : data) {
            if (bean.isSelected) {
                isEmpty = false;
                break;
            }
        }
        if (isEmpty) {
            ToastUtil.showShort("请选择商品");
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MyDesignActivity.this);
            alert.setMessage("是否删除此设计");
            alert.setNeutralButton("取消", null);
            alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //点击删除
                    mPresenter.deleteMyDesign(data);
                }
            });
            alert.show();
        }

    }

    /**
     * 条目子view点击事件
     * @param adapter adapter
     * @param view 点击的view
     * @param position 位置
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DesignBean info = (DesignBean) adapter.getItem(position);
        if (info == null) return;
        switch (view.getId()) {
            //作品详情
            case R.id.iv_design_image:
                //跳转到详情页面
                MyDesignDetailActivity.launch(mContext, info);
                break;
            //是否再次编辑
            case R.id.tv_to_design:
                switch (info.designType) {
                    //根据是2d 还是3d跳转跳转形页面
                    case "2d":
                        String isTemplate = StringUtil.isEmpty(info.templateSpuId) ? "N" : "Y";
                        EditorActivity.launch2D(mContext, info.mkuId, info.templateSpuId, isTemplate, info.sequenceNBR);
                        break;
                    case "3d":
                        BlenderDesignActivity.launch(mContext, info.sampleId, info.sequenceNBR);
                        break;
                }
                break;
            case R.id.ll_my_design_select:
                //编辑状态的选中
                info.isSelected = !info.isSelected;
                mAdapter.notifyItemChanged(position);
                break;
        }
    }

    @Override
    public void showDesignList(PageInfo<DesignBean> data) {
        this.data = data;
        if (data.list.size() > 0) {
            isCanSelect = true;
            if (allSelectState) mPresenter.changeSelectState(this.data.list, true);
        } else {
            isCanSelect = false;
            mTitleManager.setText("管理");
            mLlBottom.setVisibility(View.GONE);
        }
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(this.data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(this.data.list);
        }
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

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        allSelectState = false;
        //变换到未选中
        mImageAllChoose.setImageResource(R.drawable.icon_circle_unselected);
        mPresenter.getMyDesign(0, false);
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getMyDesign(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
