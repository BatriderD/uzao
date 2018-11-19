package com.zhaotai.uzao.ui.design.contract;

import android.graphics.Bitmap;

import com.xiaopo.flying.sticker.StickerDataInfo;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ArtWorkBean;

import java.util.List;

/**
 * 滤镜页面控制类
 */

public interface MyFilterContract {

    interface View extends BaseView {

        void showFilterList(List<ArtWorkBean> list);

        void showPic(String s,String type);

        void showCurrentBitmap(Bitmap bitmap);

    }

    abstract class Presenter extends BasePresenter {

        public abstract void getFilterList(String url);


        public abstract void getCurrentBitmap(StickerDataInfo info);


        public abstract void getFilterPic(final String type, String url);
    }

}
