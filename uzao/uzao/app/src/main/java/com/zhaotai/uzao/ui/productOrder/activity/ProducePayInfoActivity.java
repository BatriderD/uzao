package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.utils.TimeUtils;

import butterknife.BindView;

/**
 * Time: 2017/8/23
 * Created by LiYou
 * Description : 支付明细
 */

public class ProducePayInfoActivity extends BaseActivity {

    @BindView(R.id.tv_produce_pay_order_no)
    TextView mPayNo;
    @BindView(R.id.tv_produce_pay_way)
    TextView mPayWay;
    @BindView(R.id.tv_produce_pay_time)
    TextView mPayTime;

    private static final String EXTRA_KEY_PAY_INFO_PAY_NO = "extra_key_pay_info_pay_no";
    private static final String EXTRA_KEY_PAY_INFO_PAY_WAY = "extra_key_pay_info_pay_way";
    private static final String EXTRA_KEY_PAY_INFO_PAY_TIME = "extra_key_pay_info_pay_time";

    public static void launch(Context context, String payNo, String payWay, String payTime) {
        Intent intent = new Intent(context, ProducePayInfoActivity.class);
        intent.putExtra(EXTRA_KEY_PAY_INFO_PAY_NO, payNo);
        intent.putExtra(EXTRA_KEY_PAY_INFO_PAY_WAY, payWay);
        intent.putExtra(EXTRA_KEY_PAY_INFO_PAY_TIME, payTime);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_produce_pay_info);
        mTitle.setText("支付明细");
    }

    @Override
    protected void initData() {
        String payWay =  getIntent().getStringExtra(EXTRA_KEY_PAY_INFO_PAY_WAY);
        if("现金".equals(payWay)){
            payWay = "转账汇款";
        }
        mPayNo.setText(getIntent().getStringExtra(EXTRA_KEY_PAY_INFO_PAY_NO));
        mPayWay.setText(payWay);
        mPayTime.setText(TimeUtils.millis2String(Long.parseLong(getIntent().getStringExtra(EXTRA_KEY_PAY_INFO_PAY_TIME))));
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
