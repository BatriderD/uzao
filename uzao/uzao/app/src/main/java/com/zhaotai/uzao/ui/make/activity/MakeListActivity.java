package com.zhaotai.uzao.ui.make.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MakeBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.make.adapter.MakeAdapter;
import com.zhaotai.uzao.ui.make.contract.MakeContract;
import com.zhaotai.uzao.ui.make.presenter.MakePresenter;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/26
 * Created by LiYou
 * Description : 我的制造
 */

public class MakeListActivity extends BaseActivity implements MakeContract.View, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe)
    SwipeRefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tool_bar_right_img)
    ImageView mRightBtn;

    private UITipDialog tipDialog;

    private MakeAdapter mAdapter;
    private MakePresenter mPresenter;
    //存放订单数据
    private PageInfo<MakeBean> data = new PageInfo<>();

    public static void launch(Context context){
        context.startActivity(new Intent(context,MakeListActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_make_list);
        mTitle.setText("我的智造");
        mSwipe.setOnRefreshListener(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MakeAdapter();
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        //条目点击
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setEmptyView(R.layout.vw_make_empty);

        mRecycler.setAdapter(mAdapter);
        mPresenter = new MakePresenter(this,this);
        //客服
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setImageResource(R.drawable.service);
    }

    @Override
    protected void initData() {
        showLoading();
        mPresenter.getMakeList(0, true);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        MakeBean makeBean = (MakeBean) adapter.getItem(position);
        if(makeBean == null) return;
        switch (view.getId()){
            case R.id.ll_make_item:
                MakeDetailActivity.launch(mContext,makeBean);
                break;
            case R.id.tv_make_order_copy:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", makeBean.designNo);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtil.showShort("复制成功");
                break;
        }
    }

    @OnClick(R.id.tool_bar_right_img)
    public void goToIm() {
        mPresenter.loginIm();
    }

    @Override
    public void showMakeList(PageInfo<MakeBean> pageInfo) {
        this.data = pageInfo;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(this.data.list);
        } else {
//            不是第一页 就刷新
            mAdapter.addData(this.data.list);
        }
    }

    @Override
    public void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
    }

    @Override
    public void stopProgress() {
        tipDialog.dismiss();
    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }


    @Override
    public void onRefresh() {
        mPresenter.getMakeList(0, false);
    }


    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getMakeList(start, false);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
