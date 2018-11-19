package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CompanyRemitBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/26
 * Created by LiYou
 * Description : 企业汇款
 */

public class CompanyRemitActivity extends BaseActivity {

    @BindView(R.id.btn_company_remit_get_code)
    TextView mBtnGetCode;

    @BindView(R.id.ll_company_remit_code)
    LinearLayout mLlCode;
    @BindView(R.id.tv_company_remit_code)
    TextView mCode;

    //注意
    @BindView(R.id.ll_company_remit_attention)
    LinearLayout mLlAttention;

    //汇款信息
    @BindView(R.id.ll_company_remit_info)
    LinearLayout mLlInfo;
    //户名
    @BindView(R.id.tv_company_remit_title)
    TextView mInfoTitle;
    //账户
    @BindView(R.id.tv_company_remit_num)
    TextView mNum;
    //开户行
    @BindView(R.id.tv_company_remit_bank)
    TextView mBank;
    //识别码
    @BindView(R.id.tv_company_remit_info_code)
    TextView mInfoCode;

    private static final String EXTRA_KEY_ORDER_ID = "extra_key_order_id";
    private static final String EXTRA_KEY_SOURCE = "extra_key_source";

    /**
     *
     * @param orderNo 订单id
     * @param source  首款（firstPay）,尾款（lastPay）、全部(pay)
     */
    public static void launch(BaseActivity context,String orderNo,String source) {
        Intent intent = new Intent(context,CompanyRemitActivity.class);
        intent.putExtra(EXTRA_KEY_ORDER_ID,orderNo);
        intent.putExtra(EXTRA_KEY_SOURCE,source);
        context.startActivityForResult(intent,4321);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_company_remit);
        mTitle.setText("支付方式");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 复制汇款识别码
     */
    @OnClick(R.id.tv_company_remit_copy)
    public void copyCode() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", mCode.getText());
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        ToastUtil.showShort("复制成功");
    }

    /**
     * 获取汇款识别码
     */
    @OnClick(R.id.btn_company_remit_get_code)
    public void getCode() {
        CompanyRemitBean info = new CompanyRemitBean();
        info.orderNo = getIntent().getStringExtra(EXTRA_KEY_ORDER_ID);
        info.source = getIntent().getStringExtra(EXTRA_KEY_SOURCE);
        Api.getDefault().getRemitCode(info)
                .compose(RxHandleResult.<CompanyRemitBean>handleResult())
                .subscribe(new RxSubscriber<CompanyRemitBean>(mContext,true) {
                    @Override
                    public void _onNext(CompanyRemitBean companyRemitBean) {
                        mLlCode.setVisibility(View.VISIBLE);
                        mCode.setText(companyRemitBean.tradeNo);
                        mBtnGetCode.setText("已获取汇款识别码");
                        mBtnGetCode.setBackgroundResource(R.drawable.shape_button_grey_radius_20);
                        mLlAttention.setVisibility(View.GONE);
                        mLlInfo.setVisibility(View.VISIBLE);
                        mInfoTitle.setText("户名："+companyRemitBean.bankInfo.openingBank);
                        mNum.setText("账户："+companyRemitBean.bankInfo.account);
                        mBank.setText("开户行："+companyRemitBean.bankInfo.userName);
                        mInfoCode.setText("汇款识别码    "+companyRemitBean.tradeNo);
                        findViewById(R.id.ll_company_remit_bottom_attention).setVisibility(View.VISIBLE);
                        setResult(4321);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
