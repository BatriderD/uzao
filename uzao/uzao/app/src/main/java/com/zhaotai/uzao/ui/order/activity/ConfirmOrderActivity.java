package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.ConfirmOrderExpandableListAdapter;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.CreateOrderBean;
import com.zhaotai.uzao.bean.EventBean.ModifyAddressInfo;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.OrderInvoiceBean;
import com.zhaotai.uzao.bean.ShoppingCartBean;
import com.zhaotai.uzao.bean.ShoppingGoodsBean;
import com.zhaotai.uzao.ui.order.contract.ConfirmOrderContract;
import com.zhaotai.uzao.ui.order.presenter.ConfirmOrderPresenter;
import com.zhaotai.uzao.ui.person.address.AddAddressActivity;
import com.zhaotai.uzao.ui.person.address.AddressFromOrderActivity;
import com.zhaotai.uzao.utils.DecimalUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Time: 2017/5/31
 * Created by LiYou
 * Description : 确认订单页面
 */

public class ConfirmOrderActivity extends BaseActivity implements ConfirmOrderContract.View {

    private ShoppingCartBean data;
    //有默认地址
    private LinearLayout mHasAddress;
    //添加地址
    private TextView mAddAddress;
    //收货人姓名
    private TextView mReceiveName;
    //收货人电话
    private TextView mReceivePhone;
    //收货人地址
    private TextView mReceiveAddress;

    //发票信息
    @BindView(R.id.confirm_order_bill)
    TextView mBillWay;

    //商品总计
    @BindView(R.id.tv_confirm_order_account)
    TextView mAccount;
    //底部显示需支付
    @BindView(R.id.order_confirm_need_pay_num)
    TextView mNeedPayNum;
    //优惠
    @BindView(R.id.tv_confirm_order_activity_cut)
    TextView mCut;

    @BindView(R.id.sl_confirm_order)
    ScrollView mScrollView;

    @BindView(R.id.expandableListView)
    ExpandableListView mListView;

    private CreateOrderBean orderInfo = new CreateOrderBean();

    private ConfirmOrderPresenter mPresenter;
    private ConfirmOrderExpandableListAdapter adapter;

    private static final String EXTRA_KEY_BUY_DESIGN_SPU = "extra_key_buy_design_spu";

    public static void launch(Context context, ShoppingCartBean GoodsList, String orderWay) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra("GoodsList", GoodsList);
        intent.putExtra("orderWay", orderWay);
        context.startActivity(intent);
    }

    /**
     * @param context     上下文
     * @param GoodsList   商品信息
     * @param isDesignSpu 是否 立即购买 设计商品
     */
    public static void launch(Context context, ShoppingCartBean GoodsList, boolean isDesignSpu) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra("GoodsList", GoodsList);
        intent.putExtra(EXTRA_KEY_BUY_DESIGN_SPU, isDesignSpu);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_confirm_order);
        mTitle.setText("订单确认");

        mHasAddress = (LinearLayout) findViewById(R.id.order_confirm_ll_has_address);
        mAddAddress = (TextView) findViewById(R.id.confirm_order_add_address);
        mReceiveName = (TextView) findViewById(R.id.confirm_order_receive_people);
        mReceivePhone = (TextView) findViewById(R.id.confirm_order_receive_phone);
        mReceiveAddress = (TextView) findViewById(R.id.confirm_order_receive_address);

        adapter = new ConfirmOrderExpandableListAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setGroupIndicator(null);

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        //筛选选中的数据
        data = (ShoppingCartBean) getIntent().getSerializableExtra("GoodsList");
        adapter.setList(data.carts);
        adapter.notifyDataSetChanged();
        int goodsNum = 0;
        List<ShoppingGoodsBean> goods = new ArrayList<>();
        List<MaterialListBean> materialId = new ArrayList<>();
        for (int i = 0; i < data.carts.size(); i++) {
            mListView.expandGroup(i);
            goodsNum += data.carts.get(i).cartModels.size();
            for (int j = 0; j < data.carts.get(i).cartModels.size(); j++) {
                if (data.carts.get(i).cartModels.get(j).spuType != null && "material".equals(data.carts.get(i).cartModels.get(j).spuType)) {
                    MaterialListBean materialListBean = new MaterialListBean();
                    materialListBean.materialId = data.carts.get(i).cartModels.get(j).spuId;
                    materialId.add(materialListBean);
                }else {
                    goods.add(data.carts.get(i).cartModels.get(j));
                }
            }
        }
        orderInfo.orderDetailModels = goods;
        orderInfo.materialOrderDetailModels = materialId;
        if (getIntent().getBooleanExtra(EXTRA_KEY_BUY_DESIGN_SPU, false)) {
            orderInfo.orderWay = "buyNow";
        } else {
            orderInfo.orderWay = getIntent().getStringExtra("orderWay");
        }

        mPresenter = new ConfirmOrderPresenter(this, this);
        mPresenter.getAddress();
        String money = DecimalUtil.getPrice(data.finalPrice);
        //小计
        mAccount.setText("共" + goodsNum + "件商品，小计¥" + money);
        mNeedPayNum.setText(getString(R.string.need_pay_num, money));
        mCut.setText("减" + DecimalUtil.getPrice(data.preferentiaPrice) + "元");

        //初始化发票
        OrderInvoiceBean orderInvoic = new OrderInvoiceBean();
        orderInvoic.invoiceType = getString(R.string.no_bill);
        orderInfo.orderInvoiceModel = orderInvoic;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    /**
     * 显示地址默认地址
     */
    @Override
    public void showDefaultAddress(AddressBean addressBeen) {
        mScrollView.scrollTo(0, 0);
        orderInfo.receiverName = addressBeen.recieverName;
        orderInfo.receiverMobile = addressBeen.recieverPhone;
        orderInfo.receiverAddress = addressBeen.addrAlias;
        mHasAddress.setVisibility(View.VISIBLE);
        mAddAddress.setVisibility(View.GONE);
        mReceiveName.setText(addressBeen.recieverName);
        mReceivePhone.setText(addressBeen.recieverPhone);
        mReceiveAddress.setText(addressBeen.addrAlias);
        mHasAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressFromOrderActivity.launch(ConfirmOrderActivity.this, 0);
            }
        });
    }

    /**
     * 显示去 添加地址
     */
    @Override
    public void showAddAddress() {
        orderInfo.receiverName = "";
        orderInfo.receiverMobile = "";
        orderInfo.receiverAddress = "";
        mHasAddress.setVisibility(View.GONE);
        mAddAddress.setVisibility(View.VISIBLE);
        mAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddAddressActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }


    //创建订单失败
    @Override
    public void createOrderFail() {
    }

    @Override
    public void finishView() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 4://不开具发票
                orderInfo.orderInvoiceModel = (OrderInvoiceBean) data.getSerializableExtra("orderInvoice");
                mBillWay.setText(R.string.no_bill);
                break;
            case 5://纸质普通发票
                orderInfo.orderInvoiceModel = (OrderInvoiceBean) data.getSerializableExtra("orderInvoice");
                mBillWay.setText(R.string.bill_public);
                break;
            case 6://重新获取默认地址
                mPresenter.getAddress();
                break;
        }
    }

    /**
     * 接受重新选择的地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddressBean addressBean) {
        showDefaultAddress(addressBean);
    }

    /**
     * 接受 添加的地址
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ModifyAddressInfo event) {
        AddressBean addressBean = (AddressBean) event.data;
        showDefaultAddress(addressBean);
    }


    /**
     * 选择发票
     */
    @OnClick(R.id.confirm_order_bill_rl)
    public void goToBill() {
        Intent intent1 = new Intent(mContext, SelectBillActivity.class);
        intent1.putExtra("orderInvoice", orderInfo.orderInvoiceModel);
        startActivityForResult(intent1, 123);
    }

    /**
     * 去结算
     */
    @OnClick(R.id.order_confirm_pay)
    public void pay() {
        if (getIntent().getBooleanExtra(EXTRA_KEY_BUY_DESIGN_SPU, false)) {
            //创建设计商品订单
            mPresenter.createDesignOrder(orderInfo);
        } else {
            //创建订单
            mPresenter.createOrder(orderInfo);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
