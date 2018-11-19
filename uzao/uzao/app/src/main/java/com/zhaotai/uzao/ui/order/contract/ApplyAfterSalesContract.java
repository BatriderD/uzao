package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ApplyAfterSalesBean;
import com.zhaotai.uzao.bean.ApplyAfterSalesMultiBean;
import com.zhaotai.uzao.ui.order.adapter.ApplyAfterSalesMultiAdapter;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;

/**
 * Time: 2017/6/12
 * Created by LiYou
 * Description :
 */

public interface ApplyAfterSalesContract {

    interface View extends BaseView {
        void finishView();

        void showProgress();

        void stopProgress();

        void showData(List<ApplyAfterSalesMultiBean> data);
    }

    abstract class Presenter extends BasePresenter {

        //提交申请
        public abstract void applyData(ApplyAfterSalesMultiAdapter adapter);

        //转换数据
        public abstract List<MultipartBody.Part> files2Parts(List<File> files);

        //上传图片
       // public abstract void uploadImages(List<File> files, ApplyAfterSalesBean data);
    }

}
