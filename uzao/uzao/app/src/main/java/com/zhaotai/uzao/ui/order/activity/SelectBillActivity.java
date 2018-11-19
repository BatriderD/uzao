package com.zhaotai.uzao.ui.order.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.OrderInvoiceBean;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/1
 * Created by LiYou
 * Description : 发票填写页面
 */

public class SelectBillActivity extends BaseActivity {

    @BindView(R.id.ll_bill_content)
    LinearLayout mLlPaperBill;
    @BindView(R.id.no_bill)
    TextView mNoBill;
    @BindView(R.id.bill_public)
    TextView mPaperBill;
    //公司抬头
    @BindView(R.id.et_bill_company_name)
    EditText mBillTitle;
    @BindView(R.id.et_bill_company_id_card)
    EditText mBillCompanyIdCard;
    @BindView(R.id.et_bill_name)
    EditText mBillPersonalName;
    @BindView(R.id.et_bill_id_card)
    EditText mBillPersonalIdCard;

    @BindView(R.id.ll_bill_person)
    LinearLayout mPersonalContent;
    @BindView(R.id.ll_bill_company)
    LinearLayout mCompanyContent;

    @BindView(R.id.iv_bill_personal)
    ImageView mPersonal;
    @BindView(R.id.iv_bill_company)
    ImageView mCompany;

    @BindView(R.id.bill_content)
    TextView mBillContent;
    @BindView(R.id.right_btn)
    Button mRightBtn;

    //发票类型
    private int PAY_TYPE;
    private final int NO_BILL = 4; //不开具发票
    private final int PAPER_BILL = 5;//纸质发票
    private static final String USER_TYPE_PERSONAL = "personal";
    private static final String USER_TYPE_COMPANY = "company";

    //发票用户类型
    private String userType = USER_TYPE_PERSONAL;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_bill);
        mTitle.setText("发票信息填写");
        mRightBtn.setText("保存");
        mRightBtn.setVisibility(View.VISIBLE);
        OrderInvoiceBean orderInvoice = (OrderInvoiceBean) getIntent().getSerializableExtra("orderInvoice");
        if (orderInvoice.invoiceType.equals(getString(R.string.no_bill))) {
            PAY_TYPE = NO_BILL;
            //不开具发票
            showNoBillView();
        } else {
            //有发票信息
            PAY_TYPE = PAPER_BILL;
            showBillView();
            mBillContent.setText(orderInvoice.invoiceContent);
            switch (orderInvoice.invoiceUserType) {
                case USER_TYPE_PERSONAL://个人
                    mBillPersonalName.setText(orderInvoice.userName);
                    mBillPersonalIdCard.setText(orderInvoice.idNumber);
                    userType = USER_TYPE_PERSONAL;
                    mPersonal.setImageResource(R.drawable.icon_circle_selected);
                    mCompany.setImageResource(R.drawable.icon_circle_unselected);
                    mPersonalContent.setVisibility(View.VISIBLE);
                    mCompanyContent.setVisibility(View.GONE);
                    break;
                case USER_TYPE_COMPANY://公司
                    mBillCompanyIdCard.setText(orderInvoice.invoiceNumber);
                    mBillTitle.setText(orderInvoice.invoiceTitle);
                    userType = USER_TYPE_COMPANY;
                    mCompany.setImageResource(R.drawable.icon_circle_selected);
                    mPersonal.setImageResource(R.drawable.icon_circle_unselected);
                    mPersonalContent.setVisibility(View.GONE);
                    mCompanyContent.setVisibility(View.VISIBLE);
                    break;
            }

        }

    }

    @OnClick(R.id.right_btn)
    public void billSure() {
        Intent intent = new Intent();
        OrderInvoiceBean orderInvoice = new OrderInvoiceBean();
        if (PAY_TYPE == PAPER_BILL) {

            switch (userType) {
                case USER_TYPE_PERSONAL://个人
                    if (StringUtil.isTrimEmpty(mBillPersonalName.getText().toString())) {
                        ToastUtil.showShort("请填写姓名");
                        return;
                    }
                    if (!StringUtil.isIDCard18(mBillPersonalIdCard.getText())) {
                        ToastUtil.showShort("请填写正确的身份证号码");
                        return;
                    }
                    //姓名
                    //身份证号码
                    orderInvoice.userName = mBillPersonalName.getText().toString();
                    orderInvoice.invoiceTitle = mBillPersonalName.getText().toString();
                    orderInvoice.idNumber = mBillPersonalIdCard.getText().toString();
                    break;
                case USER_TYPE_COMPANY://公司
                    if (StringUtil.isTrimEmpty(mBillTitle.getText().toString())) {
                        ToastUtil.showShort("请填写公司名称");
                        return;
                    }
                    if (StringUtil.isTrimEmpty(mBillCompanyIdCard.getText().toString())) {
                        ToastUtil.showShort("请填写纳税人识别号");
                        return;
                    }
                    intent = new Intent();
                    orderInvoice = new OrderInvoiceBean();
                    //抬头
                    //纳税人识别号

                    orderInvoice.invoiceTitle = mBillTitle.getText().toString();
                    orderInvoice.invoiceNumber = mBillCompanyIdCard.getText().toString();
                    break;
            }
            if (StringUtil.isTrimEmpty(mBillContent.getText().toString())) {
                ToastUtil.showShort("请选择发票内容");
                return;
            }
            //发票内容
            orderInvoice.invoiceContent = mBillContent.getText().toString();
            //纸质发票
            orderInvoice.invoiceType = getString(R.string.bill_public);
            orderInvoice.invoiceUserType = userType;
            intent.putExtra("orderInvoice", orderInvoice);
            setResult(PAY_TYPE, intent);
        } else {
            //不开具发票
            orderInvoice.invoiceType = getString(R.string.no_bill);
            intent.putExtra("orderInvoice", orderInvoice);
            setResult(PAY_TYPE, intent);
        }
        finish();
    }

    /**
     * 个人
     */
    @OnClick(R.id.iv_bill_personal)
    public void onClickPersonal() {
        userType = USER_TYPE_PERSONAL;
        mPersonal.setImageResource(R.drawable.icon_circle_selected);
        mCompany.setImageResource(R.drawable.icon_circle_unselected);
        mPersonalContent.setVisibility(View.VISIBLE);
        mCompanyContent.setVisibility(View.GONE);
    }

    /**
     * 公司
     */
    @OnClick(R.id.iv_bill_company)
    public void onClickCompany() {
        userType = USER_TYPE_COMPANY;
        mCompany.setImageResource(R.drawable.icon_circle_selected);
        mPersonal.setImageResource(R.drawable.icon_circle_unselected);
        mPersonalContent.setVisibility(View.GONE);
        mCompanyContent.setVisibility(View.VISIBLE);
    }

    /**
     * 不开具发票
     */
    @OnClick(R.id.no_bill)
    public void noBill() {
        PAY_TYPE = NO_BILL;
        showNoBillView();
    }

    /**
     * 纸质普通发票
     */
    @OnClick(R.id.bill_public)
    public void paperBill() {
        PAY_TYPE = PAPER_BILL;
        showBillView();
    }

    /**
     * 不开具发票布局
     */
    public void showNoBillView() {
        mLlPaperBill.setVisibility(View.GONE);
        mNoBill.setBackgroundResource(R.drawable.shape_red_bg_rect_line);
        mNoBill.setTextColor(ContextCompat.getColor(this, R.color.red));
        mPaperBill.setBackgroundResource(R.drawable.shape_rec_line_grey_dark);
        mPaperBill.setTextColor(ContextCompat.getColor(this, R.color.grey));
    }

    /**
     * 开具发票布局
     */
    public void showBillView() {
        mLlPaperBill.setVisibility(View.VISIBLE);
        mPaperBill.setBackgroundResource(R.drawable.shape_red_bg_rect_line);
        mPaperBill.setTextColor(ContextCompat.getColor(this, R.color.red));
        mNoBill.setBackgroundResource(R.drawable.shape_rec_line_grey_dark);
        mNoBill.setTextColor(ContextCompat.getColor(this, R.color.grey));
    }

    @OnClick(R.id.bill_content)
    public void billContentRl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //创建数据源
        final String[] array = {"日用品", "家具用品", "服饰", "化妆品"};
        //创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
        //设置对话框为适配器对话框
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            /**
             * 第一个参数：此Dialog
             * 第二个参数：条目的位置，从0开始
             * 每个条目被点击都会触发改回调函数
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBillContent.setText(array[which]);
            }
        });
        builder.show();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

}
