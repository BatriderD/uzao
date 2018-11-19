package com.zhaotai.uzao.ui.order.presenter;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.ApplyAfterSaleDetailBean;
import com.zhaotai.uzao.bean.ApplyAfterSalesBean;
import com.zhaotai.uzao.bean.ApplyAfterSalesMultiBean;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.OrderGoodsDetailBean;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bean.post.AfterSalesGoodsInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.order.adapter.ApplyAfterSalesMultiAdapter;
import com.zhaotai.uzao.ui.order.contract.ApplyAfterSalesContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

/**
 * Time: 2017/6/12
 * Created by LiYou
 * Description :
 */

public class ApplyAfterSalesPresenter extends ApplyAfterSalesContract.Presenter {

    private ApplyAfterSalesContract.View view;
    private boolean hasHimSelf;//是否有自己设计商品

    public ApplyAfterSalesPresenter(Context context, ApplyAfterSalesContract.View view) {
        this.view = view;
        mContext = context;
    }

    /**
     * 获取商品详情
     *
     * @param packageOrderId 包裹订单Id
     */
    public void getOrderDetail(final String packageOrderId, final String packageNum) {
        Api.getDefault().getApplyAfterSalesProductInfo(packageOrderId)
                .compose(RxHandleResult.<ApplyAfterSaleDetailBean>handleResult())
                .subscribe(new RxSubscriber<ApplyAfterSaleDetailBean>(mContext, true) {
                    @Override
                    public void _onNext(ApplyAfterSaleDetailBean applyAfterSaleDetailBean) {
                        hasHimSelf = applyAfterSaleDetailBean.hasHimSelf != null && !applyAfterSaleDetailBean.hasHimSelf.isEmpty() && "Y".equals(applyAfterSaleDetailBean.hasHimSelf);
                        constructorData(applyAfterSaleDetailBean.productOrderDetailModels, packageOrderId, packageNum);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 构造数据
     */
    private void constructorData(List<ApplyAfterSaleDetailBean.SpuInfo> data, String packageOrderId, String packageNum) {
        Gson gson = new Gson();
        List<ApplyAfterSalesMultiBean> multiList = new ArrayList<>();
        ApplyAfterSalesMultiBean m1 = new ApplyAfterSalesMultiBean(ApplyAfterSalesMultiBean.TYPE_SECTION_ORDER_NUM);
        m1.orderId = packageOrderId;
        m1.packageNum = packageNum;
        multiList.add(m1);

        //添加包裹商品
        if (data != null && data.size() > 0) {
            for (ApplyAfterSaleDetailBean.SpuInfo orderItemBean : data) {
                OrderGoodsDetailBean detailBean = gson.fromJson(orderItemBean.orderDetail, OrderGoodsDetailBean.class);
                ApplyAfterSalesMultiBean m2 = new ApplyAfterSalesMultiBean(ApplyAfterSalesMultiBean.TYPE_ITEM_GOODS_SELECT);
                m2.pic = detailBean.pic;
                m2.category = detailBean.category;
                m2.name = detailBean.name;
                m2.price = orderItemBean.unitPriceY;
                m2.count = orderItemBean.count;
                m2.sequenceNBR = orderItemBean.sequenceNBR;
                m2.availableSkuCount = orderItemBean.availableSkuCount;
                multiList.add(m2);
            }
        }

        //添加申请内容
        ApplyAfterSalesMultiBean m3 = new ApplyAfterSalesMultiBean(ApplyAfterSalesMultiBean.TYPE_ITEM_APPLY);
        multiList.add(m3);

        //添加图片
        ApplyAfterSalesMultiBean m4 = new ApplyAfterSalesMultiBean(ApplyAfterSalesMultiBean.TYPE_ITEM_IMAGE);
        m4.isAddImage = true;
        multiList.add(m4);

        view.showData(multiList);
    }

    /**
     * 添加图片等下item
     *
     * @param adapter  适配器
     * @param imageUri image地址
     */
    public void notifyAddImage(ApplyAfterSalesMultiAdapter adapter, List<Uri> imageUri, List<String> imagePath) {
        List<ApplyAfterSalesMultiBean> adapterData = adapter.getData();
        for (int i = 0; i < imageUri.size(); i++) {
            ApplyAfterSalesMultiBean multi = new ApplyAfterSalesMultiBean(ApplyAfterSalesMultiBean.TYPE_ITEM_IMAGE);
            multi.isImage = true;
            multi.imageUri = imageUri.get(i);
            multi.imageFile = imagePath.get(i);
            adapterData.add(adapterData.size() - 1, multi);
        }
        int imageSize = 0;
        for (ApplyAfterSalesMultiBean multiBean : adapterData) {
            if (multiBean.isImage) {
                imageSize++;
            }
        }
        if (imageSize == 9) {
            adapterData.remove(adapterData.size() - 1);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除图片
     */
    public void closeImage(ApplyAfterSalesMultiAdapter adapter, int position) {
        if (getImageSize(adapter) == 9) {
            adapter.getData().remove(position);
            ApplyAfterSalesMultiBean m4 = new ApplyAfterSalesMultiBean(ApplyAfterSalesMultiBean.TYPE_ITEM_IMAGE);
            m4.isAddImage = true;
            adapter.getData().add(m4);
            adapter.notifyDataSetChanged();
        } else {
            adapter.remove(position);
        }
    }

    /**
     * 获取图片数量
     */
    public int getImageSize(ApplyAfterSalesMultiAdapter adapter) {
        List<ApplyAfterSalesMultiBean> data = adapter.getData();
        int imageSize = 0;
        if (!data.isEmpty()) {
            for (ApplyAfterSalesMultiBean multi : data) {
                if (multi.isImage) {
                    imageSize++;
                }
            }
        }
        return imageSize;
    }

    /**
     * 提交申请
     *
     * @param adapter 数据
     */
    @Override
    public void applyData(ApplyAfterSalesMultiAdapter adapter) {
        List<ApplyAfterSalesMultiBean> data = adapter.getData();
        ApplyAfterSalesBean applyAfterSalesBean = new ApplyAfterSalesBean();
        List<AfterSalesGoodsInfo> goodsInfoList = new ArrayList<>();
        String type = "";
        //选择商品
        for (ApplyAfterSalesMultiBean multi : data) {
            //售后商品
            if (multi.getItemType() == ApplyAfterSalesMultiBean.TYPE_ITEM_GOODS_SELECT && multi.isSelected) {
                AfterSalesGoodsInfo goodsInfo = new AfterSalesGoodsInfo();
                goodsInfo.count = multi.count;
                goodsInfo.orderDetailId = multi.sequenceNBR;
                goodsInfoList.add(goodsInfo);
            }
            //售后类型
            if (multi.getItemType() == ApplyAfterSalesMultiBean.TYPE_ITEM_APPLY) {
                type = multi.applyType;
            }
        }
        if (goodsInfoList.isEmpty()) {
            ToastUtil.showShort("请选择售后商品");
            return;
        } else {
            applyAfterSalesBean.productOrderDetailModels = goodsInfoList;
        }

        //售后原因
        if (data.size() > 0) {
            if (StringUtil.isEmpty(data.get(0).reason)) {
                ToastUtil.showShort("请填写售后原因");
                return;
            }
            applyAfterSalesBean.applyDescription = data.get(0).reason;
        }

        if (StringUtil.isEmpty(type)) {
            ToastUtil.showShort("请选择售后类型");
            return;
        }

        List<String> imagePaths = new ArrayList<>();
        for (ApplyAfterSalesMultiBean multi : data) {
            //售后类型
            if (multi.getItemType() == ApplyAfterSalesMultiBean.TYPE_ITEM_APPLY) {
                applyAfterSalesBean.type = multi.applyType;
            }
            if (multi.isImage) {
                imagePaths.add(multi.imageFile);
            }
        }

        if (!imagePaths.isEmpty()) {
            uploadImages(imagePaths, applyAfterSalesBean);
        } else {
            Api.getDefault().applyAfterSales(applyAfterSalesBean)
                    .compose(RxHandleResult.<ApplyAfterSalesBean>handleResult())
                    .subscribe(new RxSubscriber<ApplyAfterSalesBean>(mContext, false) {
                        @Override
                        public void _onNext(ApplyAfterSalesBean s) {
                            view.stopProgress();
                            ToastUtil.showShort("提交成功");
                            view.finishView();
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("提交失败");
                            view.stopProgress();
                        }
                    });
        }
    }

    /**
     * 转换数据
     *
     * @param files 图片
     * @return 表单数据
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

    /**
     * 上传图片 -- 申请售后 aftersale
     *
     * @param imagePaths 图片地址
     */
    private void uploadImages(List<String> imagePaths, final ApplyAfterSalesBean applyAfterSalesBean) {
        view.showProgress();
        Observable.just(imagePaths)
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(List<String> strings) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(mContext).load(strings).get();
                    }
                })
                .flatMap(new Function<List<File>, ObservableSource<BaseResult<List<UploadFileBean>>>>() {
                    @Override
                    public ObservableSource<BaseResult<List<UploadFileBean>>> apply(List<File> files) throws Exception {
                        return Api.getDefault().uploadTheFiles(files2Parts(files));
                    }
                })
                .compose(RxHandleResult.<List<UploadFileBean>>handleResultMap())
                .flatMap(new Function<List<UploadFileBean>, ObservableSource<BaseResult<ApplyAfterSalesBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<ApplyAfterSalesBean>> apply(List<UploadFileBean> uploadFileBeans) throws Exception {
                        List<String> image = new ArrayList<>();
                        for (UploadFileBean file : uploadFileBeans) {
                            image.add(file.fileId);
                        }
                        applyAfterSalesBean.images = image;
                        return Api.getDefault().applyAfterSales(applyAfterSalesBean);
                    }
                }).compose(RxHandleResult.<ApplyAfterSalesBean>handleResult())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        view.stopProgress();
                    }
                })
                .subscribe(new RxSubscriber<ApplyAfterSalesBean>(mContext) {
                    @Override
                    public void _onNext(ApplyAfterSalesBean applyAfterSalesBean) {
                        ToastUtil.showShort("提交成功");
                        view.finishView();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("提交失败");
                    }
                });
    }

    /**
     * 检查是否可以退款
     */
    public boolean checkType() {
        if (hasHimSelf) {
            ToastUtil.showShort("设计商品不可以退款");
        }
        return hasHimSelf;
    }
}
