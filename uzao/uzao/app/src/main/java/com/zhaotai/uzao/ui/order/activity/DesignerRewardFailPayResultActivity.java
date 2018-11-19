package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MaterialDetailBean;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/9
 * Created by LiYou
 * Description : 素材 支付结果页  失败
 */

public class DesignerRewardFailPayResultActivity extends BaseActivity {
    @BindView(R.id.tv_pay_price)
    TextView mPrice;

    public static void launch(Context context,String price) {
        Intent intent = new Intent(context, DesignerRewardFailPayResultActivity.class);
        intent.putExtra("price", price);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_fail_pay_result);
        mTitle.setText("支付结果");
        findViewById(R.id.return_main).setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        mPrice.setText(getIntent().getStringExtra("price"));
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 重新支付
     */
    @OnClick(R.id.repay)
    public void onClickRepay() {
        finish();
    }
}
