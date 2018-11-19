package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.ApplyAfterSalesDetailAdapter;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ApplyAfterSaleDetailBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.util.PreviewActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Time: 2017/6/15
 * Created by LiYou
 * Description : 售后详情页面
 */

public class MyAfterSalesDetailActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener{

    //售后单号
    private TextView mApplyNum;
    //申请时间
    private TextView mApplyTime;
    //订单编号
    private TextView mOrderNum;
    //售后状态
    private TextView mApplyStatus;
    //售后方式
    private TextView mWay;
    //退款金额
    private TextView mReturnPrice;
    //售后描述
    private TextView mDes;

    //照片布局
    @BindView(R.id.after_sales_detail_recycler)
    RecyclerView mRecycler;

    private ArrayList<String> imageList;
    private ImageView spuImage;
    private TextView spuTitle;
    private TextView spuType;
    private TextView spuNum;
    private TextView spuPrice;
    private ApplyAfterSalesDetailAdapter adapter;
    private TextView mUpload;
    private LinearLayout mReturnLL;

    public static void launch(Context context,String nbr) {
        Intent intent = new Intent(context,MyAfterSalesDetailActivity.class);
        intent.putExtra("afterSalesDetailNbr",nbr);
        context.startActivity(intent);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_apply_after_sales_detail);
        mTitle.setText("售后信息");
        mRecycler.setLayoutManager(new GridLayoutManager(this,3, GridLayoutManager.VERTICAL,false));
        adapter = new ApplyAfterSalesDetailAdapter();
        adapter.setOnItemClickListener(MyAfterSalesDetailActivity.this);
        View headView = View.inflate(mContext,R.layout.head_apply_after_sales_detail,null);
        adapter.addHeaderView(headView);

        mApplyNum = (TextView) headView.findViewById(R.id.tv_apply_after_sales_detail_num);
        mApplyTime = (TextView) headView.findViewById(R.id.tv_apply_after_sales_detail_time);
        mOrderNum = (TextView) headView.findViewById(R.id.tv_apply_after_sales_detail_order_num);
        mApplyStatus = (TextView) headView.findViewById(R.id.tv_apply_after_sales_detail_status);
        mWay = (TextView) headView.findViewById(R.id.tv_apply_after_sales_detail_type);
        mReturnLL = (LinearLayout) headView.findViewById(R.id.ll_apply_after_sales_detail_price);
        mReturnPrice = (TextView) headView.findViewById(R.id.tv_apply_after_sales_detail_price);
        mDes = (TextView) headView.findViewById(R.id.after_sales_detail_des);
        mUpload = (TextView) headView.findViewById(R.id.tv_apply_after_sales_detail_upload);

        spuImage = (ImageView) headView.findViewById(R.id.iv_pay_confirm_spu_image);
        spuTitle = (TextView) headView.findViewById(R.id.tv_pay_confirm_title);
        spuType = (TextView) headView.findViewById(R.id.tv_pay_confirm_properties);
        spuNum = (TextView) headView.findViewById(R.id.tv_confirm_order_child_num);
        spuPrice = (TextView) headView.findViewById(R.id.tv_pay_confirm_spu_price);
        headView.findViewById(R.id.tv_pay_confirm_activity).setVisibility(View.GONE);
        headView.findViewById(R.id.tv_pay_confirm_bottom_divider).setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        String nbr = getIntent().getStringExtra("afterSalesDetailNbr");
        Api.getDefault().myApplyAfterDetail(nbr)
                .compose(RxHandleResult.<ApplyAfterSaleDetailBean>handleResult())
                .subscribe(new RxSubscriber<ApplyAfterSaleDetailBean>(mContext,true) {
                    @Override
                    public void _onNext(ApplyAfterSaleDetailBean s) {
                        //商品图片
//                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + s.spuInfo.pic, spuImage);
//                        //商品标题
//                        spuTitle.setText(s.spuInfo.name);
//                        spuType.setText(s.spuInfo.category);
//                        spuNum.setText(mContext.getString(R.string.goods_number,s.productOrderDetailModel.count));
//                        spuPrice.setText(mContext.getString(R.string.account,s.productOrderDetailModel.unitPriceY));
//                        //售后单号
//                        mApplyNum.setText(mContext.getString(R.string.after_sales_order_number1,s.applyNo));
//                        //申请时间
//                        mApplyTime.setText(mContext.getString(R.string.after_sales_apply_time1, TimeUtils.millis2String(Long.parseLong(s.createTime))));
//                        //订单编号
//                        mOrderNum.setText(mContext.getString(R.string.order_num,s.orderNo));
//                        //售后状态
//                        switch (s.status){
//                            //处理中
//                            case OrderStatus.WAIT_CONFIRM:
//                                mApplyStatus.setText(mContext.getString(R.string.after_sales_order_status,"待处理"));
//                                break;
//                            case OrderStatus.CONFIRMING:
//                                mApplyStatus.setText(mContext.getString(R.string.after_sales_order_status,"处理中"));
//                                break;
//                            case OrderStatus.REJECT:
//                                mApplyStatus.setText(mContext.getString(R.string.after_sales_order_status,"售后驳回"));
//                                View footView = View.inflate(mContext,R.layout.foot_apply_after_sales_detail,null);
//                                adapter.addFooterView(footView);
//                                TextView reason = (TextView) footView.findViewById(R.id.tv_apply_after_sales_detail_reject_reason);
//                                if(s.images.size()>0){
//                                    footView.findViewById(R.id.tv_apply_after_sales_detail_reject_divider).setVisibility(View.VISIBLE);
//                                }else{
//                                    footView.findViewById(R.id.tv_apply_after_sales_detail_reject_divider).setVisibility(View.GONE);
//                                }
//                                reason.setText(s.handleOpinions);
//                                break;
//                            case OrderStatus.REPLACE:
//                                mApplyStatus.setText(mContext.getString(R.string.after_sales_order_status,"已换货"));
//                                break;
//                            case OrderStatus.RETURNED:
//                                mApplyStatus.setText(mContext.getString(R.string.after_sales_order_status,"已退款"));
//                                break;
//                        }
//                        //售后方式
//                        if("Returned".equals(s.type)){
//                            //退款
//                            mWay.setText(mContext.getString(R.string.after_sales_order_type,"退款"));
//                            mReturnPrice.setText(mContext.getString(R.string.account,s.refundAmountY));
//                        }else{
//                            mWay.setText(mContext.getString(R.string.after_sales_order_type,"换货"));
//                            mReturnLL.setVisibility(View.GONE);
//                        }
//                        //售后申请描述
//                        mDes.setText(s.description);
//                        if(s.images.size() > 0) {
//                            imageList = s.images;
//                            adapter.addData(s.images);
//                        }else {
//                            mUpload.setVisibility(View.GONE);
//                        }
//                        mRecycler.setAdapter(adapter);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        PreviewActivity.launch(mContext,imageList,position,ApiConstants.UZAOCHINA_IMAGE_HOST);
    }
}
