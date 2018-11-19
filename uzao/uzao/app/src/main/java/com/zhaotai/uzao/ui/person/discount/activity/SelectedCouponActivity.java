package com.zhaotai.uzao.ui.person.discount.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.SelectedCouponAdapter;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.ui.person.discount.contract.SelectedCouponContract;
import com.zhaotai.uzao.ui.person.discount.presenter.SelectedCouponPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * description: 选择优惠券页面
 * author : zp
 * date: 2017/7/25
 */

public class SelectedCouponActivity extends BaseActivity implements SelectedCouponContract.View, OnRefreshListener {

    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private SelectedCouponPresenter mPresenter;
    private SelectedCouponAdapter mAdapter;
    public int mPos;//记录选中位置
    private View footView;
    private String payPrice;

    /**
     * @param context  上下文
     * @param pos      选中的位置
     * @param payPrice 支付的金额
     */
    public static void launch(Activity context, int pos, String payPrice) {
        Intent intent = new Intent(context, SelectedCouponActivity.class);
        intent.putExtra("discountPosition", pos);
        intent.putExtra("payPrice", payPrice);
        context.startActivityForResult(intent, 123);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_coupon);
        mTitle.setText(R.string.selected_coupon);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new SelectedCouponAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecycler);
        //下拉刷新
        mSwipe.setOnRefreshListener(this);
        //条目点击
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DiscountCouponBean discountCouponBean = (DiscountCouponBean) adapter.getItem(position);
                if (discountCouponBean == null) return;
                Intent intent = new Intent();
                intent.putExtra("discountId", discountCouponBean.sequenceNBR);
                intent.putExtra("discountSelectPosition", position);
                intent.putExtra("discountPrice", discountCouponBean.secondPrice);
                setResult(4, intent);
                finish();
            }
        });
        //设置空页面
        mAdapter.setEmptyView(R.layout.vw_discount_empty);

        mPresenter = new SelectedCouponPresenter(this, this);
    }

    @Override
    protected void initData() {
        mPos = getIntent().getIntExtra("discountPosition", -1);
        payPrice = getIntent().getStringExtra("payPrice");
        showLoading();
        mPresenter.getCouponList(true, payPrice);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    @Override
    public void showDiscountList(List<DiscountCouponBean> list) {
        if (list != null && list.size() > 0) {
            if (mPos != -1) {
                mAdapter.setNewData(list, mPos);
            } else {
                mAdapter.setNewData(list);
            }
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            if (footView == null) {
                footView = layoutInflater.inflate(R.layout.item_foot_selected_coupon, null);
                footView.findViewById(R.id.btn_selected_coupon_no_used).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //不使用优惠券
                        setResult(5);
                        finish();
                    }
                });
                mAdapter.addFooterView(footView);
            }
        } else {
            if (footView != null) {
                mAdapter.removeFooterView(footView);
                footView = null;
            }
        }
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getCouponList(false, payPrice);
    }
}
