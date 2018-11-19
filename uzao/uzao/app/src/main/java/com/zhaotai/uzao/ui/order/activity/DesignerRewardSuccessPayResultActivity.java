package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/13
 * Created by LiYou
 * Description : 设计师打赏结果成功成功页面
 */

public class DesignerRewardSuccessPayResultActivity extends BaseActivity {

    @BindView(R.id.right_btn)
    Button mRightBtn;

    @BindView(R.id.tv_pay_price)
    TextView mPrice;

    private String price;


    public static void launch(Context context, String price) {
        Intent intent = new Intent(context, DesignerRewardSuccessPayResultActivity.class);
        intent.putExtra("price", price);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_designer_reward_success);
        mTitle.setText("支付结果");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("完成");
    }

    @Override
    protected void initData() {
        price = getIntent().getStringExtra("price");
        mPrice.setText(price);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.right_btn)
    public void onClickRightBtn() {
        finish();
    }

}
