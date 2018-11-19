package com.zhaotai.uzao.ui.designer.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.PageInfo;

/**
 * Time: 2017/12/1
 * Created by zp
 * Description :  足迹管理类
 */

public interface NewDesignerListContract {

    interface View extends BaseSwipeView {

        void showDesignList(PageInfo<DesignerBean> stringPageInfo);

        void changeDesigner(int pos, boolean b);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getDesignerList(int i, boolean b);

        public abstract void attentionDesigner(int pos, String id);

        public abstract void cancelDesigner(int pos, String id);

    }
}
