package com.zhaotai.uzao.ui.category.material.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.MaterialListBean;

import java.util.List;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材详情
 */

public interface MaterialDetailContract {

    interface View extends BaseView {
        void showMaterialDetail(MaterialDetailBean data);

        void collect(boolean isCollect);

        void like(boolean isLike);

        void isCollect(boolean isCollect);

        void isLike(boolean isLike);

        void hasMaterial();

        void showReward(List<DictionaryBean> price);

        void showRecommendMaterial(List<MaterialListBean> data);
         void openShareBoard(boolean hasPoster) ;

        void showHasWelfare(boolean hasWelfare);

    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取素材详情
         *
         * @param id 素材Id
         */
        public abstract void getMaterialDetail(String id);

        public abstract void hasPoster();
        public abstract void hasWelfare();
    }
}
