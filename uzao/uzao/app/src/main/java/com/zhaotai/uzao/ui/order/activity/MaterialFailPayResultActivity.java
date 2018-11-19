package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MaterialDetailBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description : 素材 支付结果页  失败
 */

public class MaterialFailPayResultActivity extends BaseActivity {
    @BindView(R.id.tv_pay_price)
    TextView mPrice;

    private List<MaterialDetailBean> data;
    private ArrayList<String> orderIds;

    /**
     * @param context  上下文
     * @param data     素材数据
     * @param price    价格
     * @param orderIds orderId
     * @param type     刷新订单 判断类型
     */
    public static void launch(Context context, List<MaterialDetailBean> data, String price, ArrayList<String> orderIds, boolean type) {
        Intent intent = new Intent(context, MaterialFailPayResultActivity.class);
        intent.putExtra("material", (Serializable) data);
        intent.putExtra("price", price);
        intent.putStringArrayListExtra("orderIds", orderIds);
        intent.putExtra("filterName", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_fail_pay_result);
        mTitle.setText("支付结果");

    }

    @Override
    protected void initData() {
        data = (List<MaterialDetailBean>) getIntent().getSerializableExtra("material");
        orderIds = getIntent().getStringArrayListExtra("orderIds");
        mPrice.setText(getIntent().getStringExtra("price"));
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.return_main)
    public void onClickReturnMain() {
        //返回首页
        HomeActivity.launch(mContext, 0);
        finish();
    }

    /**
     * 重新支付
     */
    @OnClick(R.id.repay)
    public void onClickRepay() {
        MaterialPayConfirmOrderActivity.launch(this, data, orderIds, getIntent().getBooleanExtra("filterName", true));
        finish();
    }
}
