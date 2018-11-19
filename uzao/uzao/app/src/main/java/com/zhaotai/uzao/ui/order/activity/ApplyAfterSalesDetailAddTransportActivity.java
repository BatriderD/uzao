package com.zhaotai.uzao.ui.order.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ApplyAfterSalesDetailMultiBean;
import com.zhaotai.uzao.bean.ApplyAfterSalesMultiBean;
import com.zhaotai.uzao.bean.EventBean.ChangeAfterSalesStatusEvent;
import com.zhaotai.uzao.ui.order.adapter.ApplyAfterSalesDetailMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.ApplyAfterSalesDetailContract;
import com.zhaotai.uzao.ui.order.presenter.ApplyAfterSalesDetailPresenter;
import com.zhaotai.uzao.ui.util.PreviewActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/8/1
 * Created by LiYou
 * Description : 售后详情页面_填写物流
 */

public class ApplyAfterSalesDetailAddTransportActivity extends BaseActivity implements ApplyAfterSalesDetailContract.View,
        BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.after_sales_detail_recycler)
    RecyclerView mRecycler;
    @BindView(R.id.right_btn)
    Button mRightBtn;

    private ApplyAfterSalesDetailMultiAdapter mAdapter;
    private ApplyAfterSalesDetailPresenter mPresenter;
    private EditText mEditText;
    private TextView mTransportCompanyName;
    private String transportCompanyName;
    private String applyNo;

    public static void launch(Activity context, String applyNo) {
        Intent intent = new Intent(context, ApplyAfterSalesDetailAddTransportActivity.class);
        intent.putExtra("applyNo", applyNo);
        context.startActivityForResult(intent, 1);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_apply_after_sales_detail);
        mTitle.setText("申请售后");
        mRightBtn.setText("提交");
        mRightBtn.setVisibility(View.VISIBLE);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ApplyAfterSalesDetailMultiAdapter(new ArrayList<ApplyAfterSalesDetailMultiBean>());
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                ApplyAfterSalesDetailMultiBean item = mAdapter.getItem(position);
                if (item != null)
                    switch (item.getItemType()) {
                        case ApplyAfterSalesMultiBean.TYPE_ITEM_IMAGE:
                            return 1;
                        default:
                            return 3;
                    }
                return 3;
            }
        });
        View footView = View.inflate(this, R.layout.foot_after_sales_detail_add_transport, null);
        mEditText = (EditText) footView.findViewById(R.id.et_transport_num);
        mTransportCompanyName = (TextView) footView.findViewById(R.id.tv_transport_company_name);
        footView.findViewById(R.id.ll_choose_transport_company).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择物流
                Intent intent = new Intent(mContext, ChooseTransportCompanyActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        mAdapter.addFooterView(footView);
    }

    @Override
    protected void initData() {
        applyNo = getIntent().getStringExtra("applyNo");
        if (mPresenter == null) {
            mPresenter = new ApplyAfterSalesDetailPresenter(this, this);
        }
        mPresenter.getData(applyNo);
    }

    @OnClick(R.id.right_btn)
    public void onClickRightBtn() {
        //提交
        mPresenter.applyTransport(transportCompanyName, mEditText.getText().toString());
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        ApplyAfterSalesDetailMultiBean item = (ApplyAfterSalesDetailMultiBean) adapter.getItem(position);
        if (item != null && item.getItemType() == ApplyAfterSalesDetailMultiBean.TYPE_ITEM_IMAGE) {
            PreviewActivity.launch(mContext, (ArrayList<String>) item.picList, item.picPosition, ApiConstants.UZAOCHINA_IMAGE_HOST);
        }
    }

    @Override
    public void showData(List<ApplyAfterSalesDetailMultiBean> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void finishView() {
        EventBus.getDefault().post(new ChangeAfterSalesStatusEvent(applyNo));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            transportCompanyName = data.getStringExtra("entryKey");
            String entryName = data.getStringExtra("entryName");
            mTransportCompanyName.setText(entryName);
        }
    }
}
