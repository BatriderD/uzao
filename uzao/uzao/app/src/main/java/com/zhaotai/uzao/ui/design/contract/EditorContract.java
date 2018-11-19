package com.zhaotai.uzao.ui.design.contract;

import android.graphics.Bitmap;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;
import com.zhaotai.uzao.bean.MKUCarrierBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ResultMetaDataBean;
import com.zhaotai.uzao.bean.TechnologyBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/4/3 0003.
 */

public interface EditorContract {
    interface View extends BaseView {
        void showColoList(List<String> colorList);

        //初始化载体信息
        void initCarrieData();

        //显示载体信息
        void showCarrierData(MKUCarrierBean bean);

        //显示设计信息
        void showDesignMetaData(List<LayerMetaJsonBean> beans);

        //处理material详情
        void showMaterialDetail(MaterialDetailBean materialDetail);

        void showArtFonts(PageInfo<ArtFontTopicBean> artFontBeanPageInfo);

        // 开始切换字体
        void handingTextFontChangeStart(String text);

        // 切换字体结束
        void handingTextFontChangeEnd(String fontName, String fileName, String text, int version, String wordartId);

        //字体下载失败的回调
        void handingTextFontChangeEndFailed();

        //
        void showProgress();

        void stopProgress();

        void showUnBoughtMaterial(UnPayMaterialBean materialListBean);

        void showSave3DSuccess(String fileId);

        void showFinishSaveFinish();

        void saveArtWorkSuccess(ResultMetaDataBean bean);

        void showTemplateInfo(String layerJSon);

        void showOverdue(ValidateProductBean resultMetaDataBean);
    }

    abstract class Presenter extends BasePresenter {
        //获得color列表
        public abstract void getColorList();

        //获得字体color列表
        public abstract void getFontList();

        //获得默认字体
        public abstract void getDefaultFont();

        //获得载体信息
        public abstract void getCarrierInfo(String mkuId);

        //获得我的设计信息
        public abstract void getDesignMetaData(String designId);

        //获得模本信息
        public abstract void getTemplateInfo(String spuId);

        //比较工艺列表的和默认工艺 得出默认工艺的位置
        public abstract int getCraftIdPos(String craftId, List<TechnologyBean> craftModels);

        public abstract void addMaterialDetail(String materialId);

        public abstract void changeTypeFace(String wordArtName, String wordArtFileName, String text, int version, String wordartId);

        //恢复下载的文字
        public abstract void releaseDownLoadFont(String fileName, String fontName);

        public abstract void upLoadBitmap(final String originalAddressPath);

        public abstract void callback();

        public abstract void repayInfo();

        public abstract void getUnBoughtMaterial(ArrayList<String> allUsedMaterials);

        public abstract void finishToSaveFor3d(Bitmap thumbnail);

        public abstract void finishToSaveDesign(String mkuId, String spuId, String simpleId, ArrayList<Bitmap> thumbnails, String designId, String layerJson, ArrayList<TemplateImageInfo> templateImageInfos, String isTemplate, ArrayList<String> allUsedMaterials, UnPayMaterialBean unBoughtMaterials, int i);


        public abstract void finishToBuyDesign(final String mkuId, final String templateSpuId, final String sampleId, final ArrayList<Bitmap> thumbnails,
                                               String sequenceNBR, final String layerJson, final List<TemplateImageInfo> templateImageInfos, final String isTemplate, final ArrayList<String> materialIds, final UnPayMaterialBean materialListBean, final int doWhat);

        public abstract void imageCrop(String s);

        public abstract void saveArtWorkAll(Bitmap thumbnail, String layerJson, String simpleId, String mkuId, String spuId);

        public abstract void chaneArtWork(String designId, String layerJson, Bitmap thumbnail);

        public abstract void finishToNextWithoutSave(String mkuId, String spuId, ArrayList<Bitmap> thumbnails, String designId, ArrayList<TemplateImageInfo> templateImageInfos, String isTemplate, ArrayList<String> allUsedMaterials, UnPayMaterialBean unBoughtMaterials, int doWhat);
    }
}
