package com.zhaotai.uzao.ui.order.presenter;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.OrderDetailBean;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.OrderItemBean;
import com.zhaotai.uzao.bean.PackageBean;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.contract.CommentOrderContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Time: 2017/6/12
 * Created by LiYou
 * Description :
 */

public class CommentPresenter extends CommentOrderContract.Presenter {

    private CommentOrderContract.View view;
    private int count = 0;

    public CommentPresenter(Context context, CommentOrderContract.View view) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取订单详情 取到包裹信息
     *
     * @param orderId        订单id
     * @param packageOrderId 包裹订单id
     */
    public void getOrderDetail(String orderId, final String packageOrderId) {
        Api.getDefault().getOrderDetail(orderId)
                .compose(RxHandleResult.<OrderDetailBean>handleResult())
                .subscribe(new RxSubscriber<OrderDetailBean>(mContext, true) {
                    @Override
                    public void _onNext(OrderDetailBean orderDetailBean) {

                        if (orderDetailBean.orderModel != null && orderDetailBean.orderModel.orderPackage != null
                                && orderDetailBean.orderModel.orderPackage.packageBig != null &&
                                orderDetailBean.orderModel.orderPackage.packageBig.size() > 0) {
                            Gson gson = new Gson();
                            List<CommentBean> commentList = new ArrayList<>();
                            for (PackageBean packageBean : orderDetailBean.orderModel.orderPackage.packageBig) {
                                if (packageOrderId.equals(packageBean.orderNo)) {
                                    for (OrderItemBean orderGoodsBean : packageBean.skus) {
                                        CommentBean commentBean = new CommentBean();
                                        List<Uri> imgList = new ArrayList<>();
                                        List<String> imgName = new ArrayList<>();
                                        Uri add = Uri.parse("add");
                                        imgList.add(add);
                                        commentBean.imgList = imgList;
                                        commentBean.imgName = imgName;
                                        commentBean.imgFiles = new ArrayList<>();

                                        OrderGoodsDetailBean detail = gson.fromJson(orderGoodsBean.detail, OrderGoodsDetailBean.class);
                                        //商品信息
                                        commentBean.orderDetailId = orderGoodsBean.sequenceNBR;
                                        commentBean.commentBody = "";
                                        commentBean.entityId = orderGoodsBean.spuId;
                                        commentBean.starScore = 5;
                                        commentBean.pic = detail.pic;
                                        commentBean.spuName = orderGoodsBean.spuName;
                                        commentBean.commentImage = new ArrayList<>();
                                        commentList.add(commentBean);
                                    }
                                }
                            }
                            view.showCommentDetailList(commentList);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    public void applyData(final List<CommentBean> applyData) {
        //1# 检查数据是否完整
        for (int i = 0; i < applyData.size(); i++) {
            CommentBean commentBean = applyData.get(i);
            if (StringUtil.isEmpty(commentBean.commentBody)) {
                ToastUtil.showShort("请填写评价");
                return;
            }
            if (commentBean.commentBody.length() > 500) {
                ToastUtil.showShort("评价内容不能超过500个字");
                return;
            }
        }

        //3# 将需要上传照片的评论添加到请求ob集合中
        count = 0;
        final List<Integer> position = new ArrayList<>();
        List<Observable<BaseResult<List<UploadFileBean>>>> ob = new ArrayList<>();
        for (int i = 0; i < applyData.size(); i++) {
            if (applyData.get(i).imgFiles.size() > 0) {
                position.add(i);
                ArrayList<MultipartBody.Part> parts = files2Parts(applyData.get(i).imgFiles);
                Observable<BaseResult<List<UploadFileBean>>> comment = Api.getDefault().uploadTheFiles(parts);
                ob.add(comment);
            }
        }
        //4# 判断是否有需要上传图片的评论  有的话 先上传图片
        if (ob.size() > 0) {
            view.showProgress("正在上传");
            Observable.concat(ob)
                    .subscribeOn(Schedulers.io())
                    .compose(RxHandleResult.<List<UploadFileBean>>handleResultMap())
                    .collect(new Callable<List<CommentBean>>() {
                        @Override
                        public List<CommentBean> call() throws Exception {
                            return applyData;
                        }
                    }, new BiConsumer<List<CommentBean>, List<UploadFileBean>>() {
                        @Override
                        public void accept(@NonNull List<CommentBean> commentBeen, @NonNull List<UploadFileBean> uploadFileBeanList) throws Exception {
                            List<String> strings = new ArrayList<>();
                            for (UploadFileBean file : uploadFileBeanList) {
                                strings.add(file.fileId);
                            }
                            //将图片id添加到评论数据中
                            applyData.get(position.get(count)).commentImage.addAll(strings);
                            count += 1;
                        }
                    })
                    .flatMapObservable(new Function<List<CommentBean>, ObservableSource<BaseResult<List<CommentBean>>>>() {
                        @Override
                        public ObservableSource<BaseResult<List<CommentBean>>> apply(@NonNull List<CommentBean> commentBeen) throws Exception {
                            return Api.getDefault().applyComment(commentBeen);
                        }
                    })
                    .compose(RxHandleResult.<List<CommentBean>>handleResult())
                    .subscribe(new RxSubscriber<List<CommentBean>>(mContext) {
                        @Override
                        public void _onNext(List<CommentBean> commentBeen) {
                            ToastUtil.showShort("提交成功");
                            view.finishView();
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("提交失败");
                            view.stopProgress();
                        }
                    });
        } else {
            Api.getDefault().applyComment(applyData)
                    .compose(RxHandleResult.<List<CommentBean>>handleResult())
                    .subscribe(new RxSubscriber<List<CommentBean>>(mContext, true) {
                        @Override
                        public void _onNext(List<CommentBean> commentBeen) {
                            ToastUtil.showShort("提交成功");
                            view.finishView();
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("提交失败");
                        }
                    });
        }
    }


    /**
     * 转换数据
     */
    @Override
    public ArrayList<MultipartBody.Part> files2Parts(List<File> files) {
        ArrayList<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            // 根据类型及File对象创建RequestBody（okhttp的类）
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // 将RequestBody封装成MultipartBody.Part类型（同样是okhttp的）
            MultipartBody.Part part = MultipartBody.Part.
                    createFormData("files", file.getName(), requestBody);
            // 添加进集合
            parts.add(part);
        }
        return parts;
    }

    @Override
    public void uploadImages(List<File> files, List<String> img) {

    }

    /**
     * 获取评论详情列表
     *
     * @param orderId 订单Id
     */
    @Override
    public void getCommentList(String orderId) {
        Api.getDefault().getOrderCommentList(orderId)
                .compose(RxHandleResult.<List<CommentBean>>handleResult())
                .subscribe(new RxSubscriber<List<CommentBean>>(mContext) {
                    @Override
                    public void _onNext(List<CommentBean> s) {
                        view.showCommentDetailList(s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

}
