package com.zhaotai.uzao.ui.design.contract;

import android.webkit.WebView;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ThreeDimensionalBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.ui.design.bean.DesignMetaBean;

import java.util.List;

/**
 * Time: 2017/9/19
 * Created by LiYou
 * Description :
 */

public interface BlenderDesignContract {

    interface View extends BaseView {
        void showDetail(ThreeDimensionalBean data);

        void successUploadImage(List<String> imageName);


        void showProgress();

        void stopProgress();


        void showMetaDetail(List<DesignMetaBean> metaData);

        void showBuyMaterialDialog(UnPayMaterialBean materialListBean);

        void showDesignImage(String fileId);

        boolean showOverdue(ValidateProductBean bean, boolean overdueAndUnShelve);

        void saveDesignSuccess(String designId);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void get3dDetail(String templateId);

        public abstract void shotSingle(WebView mWebView, String s);

        public abstract ValidateProductBean getCheckedDate(List<DesignMetaBean> metaData);
    }
}
