package com.zhaotai.uzao.ui.order.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.CommentPostAdapter;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.EventBean.RefreshOrderEvent;
import com.zhaotai.uzao.ui.order.contract.CommentOrderContract;
import com.zhaotai.uzao.ui.order.listener.OnCommentImageListener;
import com.zhaotai.uzao.ui.order.presenter.CommentPresenter;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UITipDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 订单评价
 */
public class CommentOrderActivity extends BaseActivity implements CommentOrderContract.View, OnCommentImageListener {

    private static final int REQUEST_CODE_CHOOSE = 233;

    @BindView(R.id.right_btn)
    Button mApplyData;

    @BindView(R.id.recycler_comment_order)
    RecyclerView mRecycler;

    private CommentPresenter mPresenter;
    private CommentPostAdapter mAdapter;

    //private List<File> imgFiles = new ArrayList<>();

    private List<CommentBean> spuInfo;


    private static final String EXTRA_KEY_ORDER_ID = "extra_key_order_id";
    private static final String EXTRA_KEY_PACKAGE_ORDER_ID = "extra_key_package_order_id";
    private UITipDialog tipDialog;

    /**
     * 打开评价页面
     *
     * @param orderId        订单id
     * @param packageOrderId 包裹订单id
     */
    public static void launch(Context context, String orderId, String packageOrderId) {
        Intent intent = new Intent(context, CommentOrderActivity.class);
        intent.putExtra(EXTRA_KEY_ORDER_ID, orderId);
        intent.putExtra(EXTRA_KEY_PACKAGE_ORDER_ID, packageOrderId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_comment_order);
        mTitle.setText("发表评价");
        mPresenter = new CommentPresenter(this, this);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        mApplyData.setVisibility(View.VISIBLE);
        mApplyData.setText("发布");
    }

    @Override
    protected void initData() {
        mPresenter.getOrderDetail(getIntent().getStringExtra(EXTRA_KEY_ORDER_ID), getIntent().getStringExtra(EXTRA_KEY_PACKAGE_ORDER_ID));
    }


    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @Override
    public void showCommentDetailList(List<CommentBean> data) {
        spuInfo = data;
        mAdapter = new CommentPostAdapter(spuInfo);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnCommentImageListener(this);
    }

    /**
     * 提交
     */
    @OnClick(R.id.right_btn)
    public void apply() {
        mPresenter.applyData(spuInfo);
    }

    private int imagePosition;

    @Override
    public void OnItemClick(final BaseQuickAdapter adapter, final View view, final int position, final int groupPosition) {
        RxPermissions rxPermissions = new RxPermissions(CommentOrderActivity.this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            imagePosition = groupPosition;
                            int maxSelected = 10 - spuInfo.get(groupPosition).imgList.size();
                            switch (view.getId()) {
                                case R.id.image:
                                    if (maxSelected > 0 && "add".equals(adapter.getItem(position).toString())) {
                                        Matisse.from(CommentOrderActivity.this)
                                                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                                .capture(true)
                                                .captureStrategy(new CaptureStrategy(true, "com.zhaotai.uzao.fileprovider"))
                                                .countable(true)
                                                .maxSelectable(maxSelected)
                                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                                .thumbnailScale(0.85f)
                                                .imageEngine(new GlideEngine())
                                                .forResult(REQUEST_CODE_CHOOSE);
                                    }
                                    break;
                                case R.id.close:
                                    if (spuInfo.get(imagePosition).imgFiles.size() > 0) {
                                        spuInfo.get(imagePosition).imgFiles.remove(position);
                                    }
                                    if (adapter.getData().size() == 9 && !"add".equals(adapter.getItem(8).toString())) {
                                        Uri add = Uri.parse("add");
                                        if (spuInfo.get(imagePosition).imgList.size() > 0) {
                                            spuInfo.get(imagePosition).imgList.remove(position);
                                        }
                                        spuInfo.get(imagePosition).imgList.add(add);
                                        mAdapter.notifyItemChanged(imagePosition);

                                    } else {
                                        adapter.remove(position);
                                    }
                                    break;
                            }
                        } else {
                            ToastUtil.showShort("请打开拍照权限");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收图片
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            List<Uri> imgList = spuInfo.get(imagePosition).imgList;
            spuInfo.get(imagePosition).imgList.addAll(imgList.size() - 1, mSelected);
            //删除add按钮
            if (spuInfo.get(imagePosition).imgList.size() == 10) {
                spuInfo.get(imagePosition).imgList.remove(9);
            }
            compressImage(Matisse.obtainPathResult(data));
            mAdapter.notifyItemChanged(imagePosition);
        }
    }

    /**
     * 压缩图片
     *
     * @param paths 图片地址
     */
    @SuppressLint("CheckResult")
    private void compressImage(final List<String> paths) {
        showProgress("正在压缩图片...");
        Flowable.just(paths)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(List<String> strings) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(mContext).load(strings).get();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        stopProgress();
                    }
                })
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        spuInfo.get(imagePosition).imgFiles.addAll(files);
                    }
                });
    }

    /**
     * 显示加载框
     */
    @Override
    public void showProgress(String message) {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(message)
                .create();
        tipDialog.show();
    }

    /**
     * 停止加载框
     */
    @Override
    public void stopProgress() {
        //imgFiles.clear();
        if (tipDialog != null)
            tipDialog.dismiss();
    }

    @Override
    public void finishView() {
        EventBus.getDefault().post(new RefreshOrderEvent());
        stopProgress();
        finish();
    }
}
