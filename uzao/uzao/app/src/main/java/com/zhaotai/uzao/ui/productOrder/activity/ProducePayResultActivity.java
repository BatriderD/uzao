package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.ui.person.myproduct.fragment.PublishProductFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/13
 * Created by LiYou
 * Description :
 */

public class ProducePayResultActivity extends BaseActivity {

    @BindView(R.id.iv_produce_pay_result_img)
    ImageView mResultImg;

    @BindView(R.id.tv_produce_pay_result_text)
    TextView mResultText;

    @BindView(R.id.btn_produce_pay_result)
    TextView mBtn;

    private static final String EXTRA_KEY_RESULT_STATE = "extra_key_result_state";
    private static final String EXTRA_KEY_ORDER_ID = "extra_key_order_id";
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";


    public static void launch(Context context,String state,String orderId) {
        Intent intent = new Intent(context,ProducePayResultActivity.class);
        intent.putExtra(EXTRA_KEY_RESULT_STATE,state);
        intent.putExtra(EXTRA_KEY_ORDER_ID,orderId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_produce_pay_result);
        mTitle.setText("支付结果");
    }

    @Override
    protected void initData() {
        String state = getIntent().getStringExtra(EXTRA_KEY_RESULT_STATE);
        if(SUCCESS.equals(state)) {
            //支付成功
            mResultImg.setImageResource(R.drawable.ic_produce_success);
            mResultText.setText("恭喜你,支付成功");
            mBtn.setText("查看订单");
        }else {
            //支付失败
            mResultImg.setImageResource(R.drawable.ic_produce_fail);
            mResultText.setText("抱歉,支付出了点小问题");
            mBtn.setText("重新付款");
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.btn_produce_pay_result)
    public void btnPayResult() {
        String state = getIntent().getStringExtra(EXTRA_KEY_RESULT_STATE);
        if(SUCCESS.equals(state)) {
            //支付成功
            ProductOrderActivity.launch(mContext);
        }else {
            //支付失败
            ProductOrderSelectPayWayActivity.launch(mContext,getIntent().getStringExtra(EXTRA_KEY_ORDER_ID),"lastPay");
        }
        finish();
    }

}
