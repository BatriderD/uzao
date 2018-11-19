package com.zhaotai.uzao.ui.order.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ApplyAfterSalesMultiBean;
import com.zhaotai.uzao.ui.order.adapter.ApplyAfterSalesMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.ApplyAfterSalesContract;
import com.zhaotai.uzao.ui.order.presenter.ApplyAfterSalesPresenter;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UITipDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Time: 2018/7/31
 * Created by LiYou
 * Description : 申请售后
 */

public class ApplyAfterSalesActivity extends BaseActivity implements ApplyAfterSalesContract.View,
        BaseQuickAdapter.OnItemChildClickListener {

    private static final int REQUEST_CODE_CHOOSE = 231;

    @BindView(R.id.right_btn)
    Button mApplyData;

    @BindView(R.id.recycler_apply_after_sales)
    RecyclerView mRecycler;

    private ApplyAfterSalesPresenter mPresenter;
    private ApplyAfterSalesMultiAdapter mAdapter;
    /**
     * 存放从相册选择的照片
     */
    private List<Uri> imgList;

    //默认最多选择9张图片
    private UITipDialog tipDialog;

    /**
     * @param packageOrderId 包裹订单Id
     * @param packageNum     包裹数
     */
    public static void launch(Context context, String packageOrderId, String packageNum) {
        Intent intent = new Intent(context, ApplyAfterSalesActivity.class);
        intent.putExtra("packageOrderId", packageOrderId);
        intent.putExtra("packageNum", packageNum);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_apply_after_sales);
        mTitle.setText("申请售后");

        mPresenter = new ApplyAfterSalesPresenter(this, this);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecycler.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        imgList = new ArrayList<>();
        //添加默认的+号
        Uri add = Uri.parse("add");
        imgList.add(add);
        mAdapter = new ApplyAfterSalesMultiAdapter(new ArrayList<ApplyAfterSalesMultiBean>());
        mRecycler.setAdapter(mAdapter);


        mAdapter.setOnItemChildClickListener(this);
        mApplyData.setVisibility(View.VISIBLE);
        mApplyData.setText("提交");

        mAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                ApplyAfterSalesMultiBean item = mAdapter.getItem(position);
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
    }

    @Override
    protected void initData() {
        String packageOrderId = getIntent().getStringExtra("packageOrderId");
        String packageNum = getIntent().getStringExtra("packageNum");
        mPresenter.getOrderDetail(packageOrderId, packageNum);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void showData(List<ApplyAfterSalesMultiBean> data) {
        mAdapter.setNewData(data);
    }

    /**
     * 判断申请售后的商品是否是自己的商品
     */
    public void isMyProduct() {
//        Api.getDefault().isMyProduct(data.sequenceNBR)
//                .compose(RxHandleResult.<ApplyAfterSaleDetailBean>handleResult())
//                .subscribe(new RxSubscriber<ApplyAfterSaleDetailBean>(ApplyAfterSalesActivity.this, true) {
//                    @Override
//                    public void _onNext(ApplyAfterSaleDetailBean applyAfterSaleDetailBean) {
//                        isHimSelf = "Y".equals(applyAfterSaleDetailBean.hasHimSelf);
//                    }
//
//                    @Override
//                    public void _onError(String message) {
//
//                    }
//                });
    }

    /**
     * 提交
     */
    @OnClick(R.id.right_btn)
    public void apply() {
        mPresenter.applyData(mAdapter);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ApplyAfterSalesMultiBean multiBean = (ApplyAfterSalesMultiBean) adapter.getItem(position);
        if (multiBean != null)
            switch (view.getId()) {
                case R.id.tv_apply_after_sales_return_money://退款
                    if (mPresenter.checkType()) {
                        return;
                    }
                    multiBean.applyType = ApplyAfterSalesMultiBean.APPLY_TYPE_RETURNED;
                    mAdapter.notifyItemChanged(position);
                    break;
                case R.id.tv_apply_after_sales_replace_goods://换货
                    multiBean.applyType = ApplyAfterSalesMultiBean.APPLY_TYPE_REPLACEMENT;
                    mAdapter.notifyItemChanged(position);
                    break;
                case R.id.iv_check_goods://选择商品
                    if (multiBean.availableSkuCount > 0) {
                        multiBean.isSelected = !multiBean.isSelected;
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtil.showShort("此商品已经再售后处理中");
                    }
                    break;
                case R.id.iv_goods_spu_add://数量添加
                    if (Integer.valueOf(multiBean.count) < multiBean.availableSkuCount) {
                        multiBean.count = String.valueOf(Integer.valueOf(multiBean.count) + 1);
                        mAdapter.notifyItemChanged(position);
                    } else {
                        ToastUtil.showShort("申请售后数量不能大于购买数量");
                    }
                    break;
                case R.id.iv_goods_spu_sub://数量减少
                    if (Integer.valueOf(multiBean.count) > 1) {
                        multiBean.count = String.valueOf(Integer.valueOf(multiBean.count) - 1);
                        mAdapter.notifyItemChanged(position);
                    }
                    break;
                case R.id.image:
                    if (multiBean.isAddImage) {
                        RxPermissions rxPermissions = new RxPermissions(this);
                        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                                .subscribe(new Observer<Boolean>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        if (aBoolean) {
                                            Matisse.from(ApplyAfterSalesActivity.this)
                                                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                                    .capture(true)
                                                    .captureStrategy(new CaptureStrategy(true, "com.zhaotai.uzao.fileprovider"))
                                                    .countable(true)
                                                    .maxSelectable(9 - mPresenter.getImageSize(mAdapter))
                                                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                                    .thumbnailScale(0.85f)
                                                    .imageEngine(new GlideEngine())
                                                    .forResult(REQUEST_CODE_CHOOSE);
                                        } else {
                                            ToastUtil.showShort("打开权限才能选择图片哦~");
                                        }

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                    break;
                case R.id.close:
                    //删除图片
                    mPresenter.closeImage(mAdapter, position);
                    break;
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收图片
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mPresenter.notifyAddImage(mAdapter, Matisse.obtainResult(data), Matisse.obtainPathResult(data));
        }
    }

    /**
     * 显示加载框
     */
    @Override
    public void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传").create();
        tipDialog.show();
    }

    /**
     * 停止加载框
     */
    @Override
    public void stopProgress() {
        //imgCompressFiles.clear();
        if (tipDialog != null)
            tipDialog.dismiss();
    }

    @Override
    public void finishView() {
        stopProgress();
        //提交数据成功
        Intent intent = new Intent();
        intent.putExtra("position123", getIntent().getIntExtra("position", 0));
        setResult(200, intent);
        finish();
    }
}
