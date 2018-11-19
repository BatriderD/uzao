package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/1
 * Created by LiYou
 * Description : 支付方式
 */

public class SelectPayWayActivity extends BaseFragmentActivity {

    @BindView(R.id.pay_way_zfb_image)
    ImageView mZfbImg;

    @BindView(R.id.pay_way_wx_image)
    ImageView mWxImg;

    public static void launch(Context context,int payWay){
        Intent intent = new Intent(context,SelectPayWayActivity.class);
        intent.putExtra("payWay",payWay);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_pay_way);
        if(getIntent().getIntExtra("payWay",1) == 1){
            mZfbImg.setVisibility(View.GONE);
            mWxImg.setVisibility(View.VISIBLE);
        }else{
            mZfbImg.setVisibility(View.VISIBLE);
            mWxImg.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.pay_way_wx)
    public void selectWx(){
        setResult(2);
        finish();
    }

    @OnClick(R.id.pay_way_zfb)
    public void selectZfb(){
        setResult(3);
        finish();
    }

    @Override
    protected void initData() {

    }


}
