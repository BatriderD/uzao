package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ApplyAfterSalesBean;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.OrderGoodsBean;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * Time: 2017/8/16
 * Created by LiYou
 * Description :
 */

public interface CommentOrderContract {

    interface View extends BaseView {
        void finishView();

        void showProgress(String message);

        void stopProgress();

        void showCommentDetailList(List<CommentBean> data);
    }

    abstract class Presenter extends BasePresenter {

        //提交申请
        //public abstract void applyData(List<OrderGoodsBean> spuInfo);

        //转换数据
        public abstract List<MultipartBody.Part> files2Parts(List<File> files);

        //上传图片
        public abstract void uploadImages(List<File> files,List<String> img);

        //获取商品详情
        public abstract void getCommentList(String orderId);
    }
}
