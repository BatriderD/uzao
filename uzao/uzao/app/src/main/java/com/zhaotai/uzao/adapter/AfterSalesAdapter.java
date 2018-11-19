package com.zhaotai.uzao.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.AfterSalesBean;
import com.zhaotai.uzao.ui.order.activity.ApplyAfterSalesDetailActivity;
import com.zhaotai.uzao.ui.order.activity.ApplyAfterSalesDetailAddTransportActivity;

/**
 * Time: 2018/8/6
 * Created by LiYou
 * Description : 售后列表
 */

public class AfterSalesAdapter extends BaseRecyclerAdapter<AfterSalesBean, BaseViewHolder> {

    public AfterSalesAdapter() {
        super(R.layout.item_after_sales);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AfterSalesBean item) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_my_after_sales_item_goods);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        AfterSalesGoodsAdapter adapter = new AfterSalesGoodsAdapter(item.afterSaleApplyDetailModels);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //等待顾客发货
                if ("waitCustDeliver".equals(item.status)) {
                    ApplyAfterSalesDetailAddTransportActivity.launch((Activity) mContext, item.applyNo);
                } else {
                    ApplyAfterSalesDetailActivity.launch(mContext, item.applyNo);
                }
            }
        });

//        编号
        helper.setText(R.id.tv_my_after_sales_item_num, mContext.getString(R.string.after_number, item.applyNo))
//                状态
                .setText(R.id.tv_my_after_sales_item_status, getOrderStatus(item.status))
//                售后方式
                .setText(R.id.tv_my_after_sales_item_conduct, mContext.getString(R.string.after_sales_order_type, getOrderType(item.type)));
//        if (item.status.equals("replace")) {
//            helper.setVisible(R.id.tv_my_after_sales_item_logistics, true);
//        } else {
//
//        }
        helper.setVisible(R.id.tv_my_after_sales_item_logistics, false);
    }

    /**
     * 获取售后方式
     */
    private String getOrderType(String type) {
        switch (type) {
            case "Returned":
                return "退款";
            case "Replacement":
                return "换货";
            default:
                return "";
        }
    }

    /**
     * 获取订单状态
     */
    private String getOrderStatus(String status) {
        switch (status) {
            case "returned":
                return "已退款";
            case "waitConfirm":
                return "待处理";
            case "confirming":
                return "处理中";
            case "waitCustDeliver":
                return "等待退货";
            case "reject":
                return "售后失败";
            case "replace":
                return "已换货";
            default:
                return "处理中";
        }
    }
}
