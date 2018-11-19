package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.address.AddressActivity;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/25
 * Created by LiYou
 * Description :  分批地址选择
 */

public class ProduceAddressSelectActivity extends BaseActivity {

    @BindView(R.id.rl_address_first)
    RelativeLayout mRlAddressFirst;
    @BindView(R.id.rl_address_second)
    RelativeLayout mRlAddressSecond;
    @BindView(R.id.rl_address_third)
    RelativeLayout mRlAddressThird;

    @BindView(R.id.tv_product_order_detail_receive_people1)
    TextView mPeople1;
    @BindView(R.id.tv_product_order_detail_receive_people2)
    TextView mPeople2;
    @BindView(R.id.tv_product_order_detail_receive_people3)
    TextView mPeople3;

    @BindView(R.id.tv_product_order_detail_receive_phone1)
    TextView mPhone1;
    @BindView(R.id.tv_product_order_detail_receive_phone2)
    TextView mPhone2;
    @BindView(R.id.tv_product_order_detail_receive_phone3)
    TextView mPhone3;

    @BindView(R.id.tv_product_order_detail_receive_address1)
    TextView mAddress1;
    @BindView(R.id.tv_product_order_detail_receive_address2)
    TextView mAddress2;
    @BindView(R.id.tv_product_order_detail_receive_address3)
    TextView mAddress3;

    @BindView(R.id.et_batch_address_first)
    EditText mEtFirst;
    @BindView(R.id.et_batch_address_second)
    EditText mEtSecond;
    @BindView(R.id.et_batch_address_third)
    EditText mEtThird;

    private static final String EXTRA_KEY_COUNT = "extra_key_count";
    private static final String EXTRA_KEY_ORDER_ID = "extra_key_order_id";
    private List<BatchAddress> data;

    public static void launch(Context context, String orderId, String count) {
        Intent intent = new Intent(context, ProduceAddressSelectActivity.class);
        intent.putExtra(EXTRA_KEY_ORDER_ID, orderId);
        intent.putExtra(EXTRA_KEY_COUNT, count);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_produce_address_select);

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        data = new ArrayList<>();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.tv_batch_address_first)
    public void selectFirstAddress() {
        AddressActivity.launch(mContext, AddressActivity.BATCH_ADDRESS, 1);
    }

    @OnClick(R.id.tv_batch_address_second)
    public void selectSecondAddress() {
        AddressActivity.launch(mContext, AddressActivity.BATCH_ADDRESS, 2);
    }

    @OnClick(R.id.tv_batch_address_third)
    public void selectThirdAddress() {
        AddressActivity.launch(mContext, AddressActivity.BATCH_ADDRESS, 3);
    }

    @OnClick(R.id.tv_batch_next_step)
    public void nextStep() {
        int packageCount1 = 0;
        int packageCount2 = 0;
        int packageCount3 = 0;
        //批量地址1
        if (View.VISIBLE == mRlAddressFirst.getVisibility()) {
            if (StringUtil.isEmpty(mEtFirst.getText())) {
                ToastUtil.showShort("请填写批次包装数量");
                return;
            }
            packageCount1 = Integer.parseInt(mEtFirst.getText().toString());
        }

        //批量地址2
        if (View.VISIBLE == mRlAddressSecond.getVisibility()) {
            if (StringUtil.isEmpty(mEtSecond.getText())) {
                ToastUtil.showShort("请填写批次包装数量");
                return;
            }
            packageCount2 = Integer.parseInt(mEtSecond.getText().toString());
        }

        //批量地址3
        if (View.VISIBLE == mRlAddressThird.getVisibility()) {
            if (StringUtil.isEmpty(mEtThird.getText())) {
                ToastUtil.showShort("请填写批次包装数量");
                return;
            }
            packageCount3 = Integer.parseInt(mEtThird.getText().toString());
        }

        if(mRlAddressFirst.getVisibility() == View.GONE && mRlAddressSecond.getVisibility() == View.GONE && mRlAddressSecond.getVisibility() == View.GONE){
            ToastUtil.showShort("请选择收货地址");
            return;
        }

        int sum = Integer.parseInt(getIntent().getStringExtra(EXTRA_KEY_COUNT));
        int count = packageCount1 + packageCount2 + packageCount3;
        if (sum != count) {
            ToastUtil.showShort("您填写的分批次数量与总数不相等,请核对");
            return;
        }
        //批量地址1
        if (View.VISIBLE == mRlAddressFirst.getVisibility()) {
            data.add(new BatchAddress(mPeople1.getText().toString(),mPhone1.getText().toString(),mAddress1.getText().toString(),packageCount1));
        }

        //批量地址2
        if (View.VISIBLE == mRlAddressSecond.getVisibility()) {
            data.add(new BatchAddress(mPeople2.getText().toString(),mPhone2.getText().toString(),mAddress2.getText().toString(),packageCount2));
        }

        //批量地址3
        if (View.VISIBLE == mRlAddressThird.getVisibility()) {
            data.add(new BatchAddress(mPeople3.getText().toString(),mPhone3.getText().toString(),mAddress3.getText().toString(),packageCount3));
        }

        final String orderNo = getIntent().getStringExtra(EXTRA_KEY_ORDER_ID);
        Api.getDefault().commitBatchAddress(orderNo,data)
                .compose(RxHandleResult.<Object>handleResult())
                .subscribe(new RxSubscriber<Object>(mContext,true) {
                    @Override
                    public void _onNext(Object s) {
                        ProductOrderSelectPayWayActivity.launch(mContext,orderNo, "lastPay");
                        finish();
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMessageEvent(AddressBean address) {
        switch (address.position) {
            case 1:
                mRlAddressFirst.setVisibility(View.VISIBLE);
                mPeople1.setText(address.recieverName);
                mAddress1.setText(address.addrAlias.trim());
                mPhone1.setText(address.recieverPhone);
                break;
            case 2:
                mRlAddressSecond.setVisibility(View.VISIBLE);
                mPeople2.setText(address.recieverName);
                mAddress2.setText(address.addrAlias.trim());
                mPhone2.setText(address.recieverPhone);
                break;
            case 3:
                mRlAddressThird.setVisibility(View.VISIBLE);
                mPeople3.setText(address.recieverName);
                mAddress3.setText(address.addrAlias.trim());
                mPhone3.setText(address.recieverPhone);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class BatchAddress {
        public BatchAddress(String receiverName, String receiverMobile, String receiverAddress,int packageCount) {
            this.receiverName = receiverName;
            this.receiverMobile = receiverMobile;
            this.receiverAddress = receiverAddress;
            this.packageCount = packageCount;
        }

        public String receiverAddress;
        public String receiverMobile;
        public String receiverName;
        public int packageCount;
    }
}
