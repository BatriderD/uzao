package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.TransportAdapter;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.TransportBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import butterknife.BindView;

/**
 * Time: 2017/6/5
 * Created by LiYou
 * Description : 物流信息
 */

public class LogisticsActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    public static final int TYPE_ORDER_ID = 1;
    public static final int TYPE_TRANSPORT = 2;
    public static final int TYPE_PRODUCE_ID = 3;
    //物流id
    private static final String EXTRA_KEY_LOGISTICS_ID = "extra_key_logistics_id";
    //物流类型
    private static final String EXTRA_KEY_TYPE = "extra_key_type";

    /**
     * 开启物流信息页面
     *
     * @param context 上下文环境
     * @param type    id类型
     * @param id      id
     */
    public static void launch(Context context, int type, String id) {
        Intent intent = new Intent(context, LogisticsActivity.class);
        intent.putExtra(EXTRA_KEY_TYPE, type);
        intent.putExtra(EXTRA_KEY_LOGISTICS_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_logistics);
        mTitle.setText("物流信息");
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void initData() {
        int type = getIntent().getIntExtra(EXTRA_KEY_TYPE, TYPE_ORDER_ID);
        if (type == TYPE_ORDER_ID) {
            //根据订单id查询物流
            String orderId = getIntent().getStringExtra(EXTRA_KEY_LOGISTICS_ID);
            Api.getDefault().getOrderTransportList(orderId)
                    .compose(RxHandleResult.<TransportBean>handleResult())
                    .subscribe(new RxSubscriber<TransportBean>(mContext, true) {
                        @Override
                        public void _onNext(TransportBean transportBean) {
                            showTransport(transportBean);
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        } else if (type == TYPE_TRANSPORT) {
            //根据物流id查询物流
            String transportId = getIntent().getStringExtra(EXTRA_KEY_LOGISTICS_ID);
            Api.getDefault().getTransportList(transportId)
                    .compose(RxHandleResult.<TransportBean>handleResult())
                    .subscribe(new RxSubscriber<TransportBean>(mContext, true) {
                        @Override
                        public void _onNext(TransportBean transportBean) {
                            showTransport(transportBean);
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        } else if (type == TYPE_PRODUCE_ID) {
            //根据大货id查询物流
            String produceId = getIntent().getStringExtra(EXTRA_KEY_LOGISTICS_ID);
            Api.getDefault().getProduceTransportList(produceId)
                    .compose(RxHandleResult.<TransportBean>handleResult())
                    .subscribe(new RxSubscriber<TransportBean>(mContext, true) {
                        @Override
                        public void _onNext(TransportBean transportBean) {
                            showTransport(transportBean);
                        }

                        @Override
                        public void _onError(String message) {

                        }
                    });
        }


    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 显示物流信息
     *
     * @param transportBean 物流信息数据
     */
    private void showTransport(TransportBean transportBean) {
        TransportAdapter mAdapter = new TransportAdapter(transportBean.transportInfo.data);
        mRecycler.setAdapter(mAdapter);
        View headView = getLayoutInflater().inflate(R.layout.head_transport, null);
        ImageView pic = (ImageView) headView.findViewById(R.id.transport_image);
        TextView company = (TextView) headView.findViewById(R.id.transport_company);
        TextView state = (TextView) headView.findViewById(R.id.transport_state);
        TextView num = (TextView) headView.findViewById(R.id.transport_num);
        TextView tel = (TextView) headView.findViewById(R.id.transport_tel);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + transportBean.icon, pic);
        company.setText(getString(R.string.transport_company, transportBean.transportName));
        state.setText(getString(R.string.transport_state, getState(transportBean.transportInfo.state)));
        num.setText(getString(R.string.transport_num, transportBean.transportNo));
        tel.setText(getString(R.string.transport_tel, transportBean.telephone));
        mAdapter.addHeaderView(headView);
    }

    /**
     * 物流状态
     *
     * @param state 0：在途中,1：已发货，2：疑难件，3： 已签收 ，4：已退货
     * @return 提示文字
     */
    private String getState(int state) {
        switch (state) {
            case 0:
                return "在途中";
            case 1:
                return "已发货";
            case 2:
                return "疑难件";
            case 3:
                return "已签收";
            case 4:
                return "已退签";
            case 5:
                return "派件中";
            case 6:
                return "退回中";
            default:
                return "未查询到";
        }
    }

}
