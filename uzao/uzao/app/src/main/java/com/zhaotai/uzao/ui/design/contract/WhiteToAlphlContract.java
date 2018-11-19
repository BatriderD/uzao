package com.zhaotai.uzao.ui.design.contract;

import android.graphics.Bitmap;

import com.xiaopo.flying.sticker.StickerDataInfo;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;

/**
 * 白色背景透明化页面控制类
 */

public interface WhiteToAlphlContract {

    interface View extends BaseView {


        void showChangedBitmap(Bitmap bitmap);

    }

    abstract class Presenter extends BasePresenter {



        public abstract void getCurrentBitmap(StickerDataInfo info, boolean isChanged);
    }

}
