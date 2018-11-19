package com.zhaotai.uzao.ui.poster;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.PosterItemBean;

import java.util.List;

/**
 * Time: 2017/8/23
 * Created by zp
 * Description :
 */

public interface PosterContract {

    interface View extends BaseView {

        void setBgImage(String url);

        void setViewData(List<PosterItemBean.DesignMetaBean> beans);

        void setText(String content, int fontSize, String fontColor, PosterItemBean.DesignMetaBean.LocationBean ret);

        void setCode(String content, PosterItemBean.DesignMetaBean.LocationBean ret);

        void setInput(String content, int fontSize, String fontColor, PosterItemBean.DesignMetaBean.LocationBean ret);

        void setImage(String content, PosterItemBean.DesignMetaBean.LocationBean ret);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getPosterData();

        public abstract void getTemplate(String s, String contentId);
    }
}
