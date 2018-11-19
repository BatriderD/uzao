package com.zhaotai.uzao.ui.design.contract;

import android.graphics.Typeface;

import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerDataBean;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;

import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/4/3 0003.
 */

public interface EditorFragmentContract {
    interface View extends BaseView {

        void stickerAllInitFinish();

        void addStickerSingleFinish();

        void addSticker(Sticker sticker);

        void reSetStickerTypeFace(String fileName, String fontName, Typeface fromFile,int version,String wordartId);

        void reShow(List<Sticker> stickers);
    }

    abstract class Presenter extends BasePresenter {

        public abstract List<StickerDataBean> saveStep(List<Sticker> stickers);

        public abstract void parseLayerMeta(List<LayerMetaJsonBean.LayerMetaBean> layerMeta, float scale);

        public abstract void reShowData(List<StickerDataBean> step);
    }
}
