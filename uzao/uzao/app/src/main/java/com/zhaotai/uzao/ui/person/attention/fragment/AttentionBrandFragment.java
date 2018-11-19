package com.zhaotai.uzao.ui.person.attention.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.BrandBean;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.listener.BtnOnClickListener;
import com.zhaotai.uzao.ui.brand.BrandActivity;
import com.zhaotai.uzao.ui.person.attention.adapter.AttentionBrandAdapter;
import com.zhaotai.uzao.ui.person.attention.contract.AttentionBrandContract;
import com.zhaotai.uzao.ui.person.attention.presenter.AttentionBrandPresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 关注的品牌
 */

public class AttentionBrandFragment extends BaseFragment implements AttentionBrandContract.View, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rc_person_collection_product_content)
    RecyclerView mRecycler;

    private AttentionBrandAdapter mAdapter;
    private AttentionBrandPresenter mPresenter;
    private PageInfo<BrandBean> data = new PageInfo<>();
    private AlertDialog.Builder dialog;
    private boolean isChange = false;

    public static AttentionBrandFragment newInstance() {
        return new AttentionBrandFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_my_attention_designer;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new AttentionBrandAdapter();

        mRecycler.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final BrandBean info = (BrandBean) adapter.getItem(position);
                if (info == null) return;
                switch (view.getId()) {
                    case R.id.rl_attention:
                        //品牌主页
                        BrandActivity.launch(_mActivity, info.brandId);
                        break;
                    case R.id.tv_brand_attention:
                        //取消关注
                        if (dialog == null) {
                            dialog = new AlertDialog.Builder(_mActivity);
                            dialog.setMessage("确定不再关注此品牌?");
                            dialog.setCancelable(false);
                            dialog.setNegativeButton("取消", null);
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.cancelAttentionBrand(info.brandId, position);
                                }
                            });
                        }
                        dialog.show();
                        break;
                }

            }
        });

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(this, mRecycler);
        mAdapter.setEmptyStateView(_mActivity, R.mipmap.ic_status_empty_attention_brand, "您还没有关注任何品牌哦", "去逛逛", new BtnOnClickListener() {
            @Override
            public void btnOnClickListener() {
                HomeActivity.launch(_mActivity, 0);
            }
        });
    }

    @Override
    public void initPresenter() {
        mPresenter = new AttentionBrandPresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        showLoading();
        mPresenter.getBrandList(0);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showBrandList(PageInfo<BrandBean> list) {
        data = list;
        showContent();
        //下拉刷新
        mAdapter.addData(data.list);
        if (list.totalPages == 1) {
            mAdapter.loadMoreEnd();
            mAdapter.disableLoadMoreIfNotFullPage(mRecycler);
        }
        mAdapter.loadMoreComplete();
    }

    /**
     * 取消关注设计师
     *
     * @param position 取消的位置
     */
    @Override
    public void cancelAttention(int position) {
        isChange = true;
        mAdapter.remove(position);
    }


    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getBrandList(start);
        } else {
            mAdapter.loadMoreEnd();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isChange) {
            PersonInfo info = new PersonInfo();
            info.code = EventBusEvent.REQUEST_PERSON;
            EventBus.getDefault().post(info);
        }
    }

    @Override
    public void stopLoadingMore() {
    }

    @Override
    public void stopRefresh() {

    }

    @Override
    public void loadingFail() {

    }
}
