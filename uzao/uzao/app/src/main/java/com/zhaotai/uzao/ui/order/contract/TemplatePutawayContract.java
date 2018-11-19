package com.zhaotai.uzao.ui.order.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.TemplateInfoBean;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;

import java.util.List;

/**
 * Time: 2017/9/9
 * Created by LiYou
 * Description : 上架商城
 */

public interface TemplatePutawayContract {

    interface View extends BaseView {
        void finishView();

        void templateDetail(TemplateInfoBean templateBean);
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取2d 待发布商品详情
         *
         * @param mkuId 蒙版组Id
         */
        public abstract void getTemplateDetail(String mkuId);

        /**
         * 获取3d 待发布商品信息
         *
         * @param spuId 商品id
         */
        public abstract void getTemplatePutaway3DDetail(String spuId);

        /**
         * 上架商城 2d
         *
         * @param des        简介
         * @param desIdea    设计理念
         * @param datas      数据
         * @param designId   设计id
         * @param isTemplate 是否模板
         * @param tags       标签
         */
        public abstract void putawayToStore2D(String spuName, String des, String desIdea, TemplateBean datas, String designId, String isTemplate,
                                              List<String> spuImages, List<TemplateImageInfo> skuImages, List<String> materialIds, List<TemplateBean.TagsBean> tags);

        /**
         * 上架商城 3d
         *
         * @param des        简介
         * @param desIdea    设计理念
         * @param datas      数据
         * @param designId   设计id
         * @param isTemplate 是否模板
         * @param tags       标签
         */
        public abstract void putawayToStore3D(String spuName, String des, String desIdea, TemplateBean datas, String designId, String isTemplate,
                                     List<String> spuImages, List<String> materialIds, List<TemplateBean.TagsBean> tags);
    }

}
