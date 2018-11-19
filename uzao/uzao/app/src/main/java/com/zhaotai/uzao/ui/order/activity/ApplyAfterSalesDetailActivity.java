package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ApplyAfterSalesDetailMultiBean;
import com.zhaotai.uzao.bean.ApplyAfterSalesMultiBean;
import com.zhaotai.uzao.ui.order.adapter.ApplyAfterSalesDetailMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.ApplyAfterSalesDetailContract;
import com.zhaotai.uzao.ui.order.presenter.ApplyAfterSalesDetailPresenter;
import com.zhaotai.uzao.ui.util.PreviewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2018/8/1
 * Created by LiYou
 * Description : 售后详情页面
 */

public class ApplyAfterSalesDetailActivity extends BaseActivity implements ApplyAfterSalesDetailContract.View,
        BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.after_sales_detail_recycler)
    RecyclerView mRecycler;

    private ApplyAfterSalesDetailMultiAdapter mAdapter;
    private ApplyAfterSalesDetailPresenter mPresenter;

    /**
     * 售后详情
     *
     * @param context 上下文
     * @param applyNo 售后Id
     */
    public static void launch(Context context, String applyNo) {
        Intent intent = new Intent(context, ApplyAfterSalesDetailActivity.class);
        intent.putExtra("applyNo", applyNo);
        context.startActivity(intent);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_apply_after_sales_detail);
        mTitle.setText("售后信息");
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ApplyAfterSalesDetailMultiAdapter(new ArrayList<ApplyAfterSalesDetailMultiBean>());
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                ApplyAfterSalesDetailMultiBean item = mAdapter.getItem(position);
                if (item != null)
                    switch (item.getItemType()) {
                        case ApplyAfterSalesMultiBean.TYPE_ITEM_IMAGE:
                            return 1;
                        default:
                            return 3;
                    }
                return 3;
            }
        });
    }

    @Override
    protected void initData() {
        String applyNo = getIntent().getStringExtra("applyNo");
        if (mPresenter == null) {
            mPresenter = new ApplyAfterSalesDetailPresenter(this, this);
        }
        mPresenter.getData(applyNo);
    }

    @Override
    public void showData(List<ApplyAfterSalesDetailMultiBean> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void finishView() {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ApplyAfterSalesDetailMultiBean item = (ApplyAfterSalesDetailMultiBean) adapter.getItem(position);
        if (item != null)
            switch (view.getId()) {
                case R.id.image:
                    PreviewActivity.launch(mContext, (ArrayList<String>) item.picList, item.picPosition, ApiConstants.UZAOCHINA_IMAGE_HOST);
                    break;
                case R.id.tv_transport_check:
                    LogisticsActivity.launch(mContext, LogisticsActivity.TYPE_TRANSPORT, item.transportNo);
                    break;
            }
    }
}
