package com.zhaotai.uzao.ui.category.goods.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipePlusView;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2018/3/23
 * Created by LiYou
 * Description : 导航商品列表
 */

public interface NavigateProductListContract {
    interface View extends BaseSwipePlusView {
        void bindData(PageInfo<GoodsBean> data);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getNavigateData(int start, String code);

        public abstract void getMainData(int start, String groupType, String groupCode, String entityType, String fieldName, String id);

        public abstract void getTopicData(int start, String type, String mongoId, String imageId, String hotspotId);
    }
}
