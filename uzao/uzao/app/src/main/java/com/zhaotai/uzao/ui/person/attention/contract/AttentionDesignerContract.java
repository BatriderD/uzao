package com.zhaotai.uzao.ui.person.attention.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.PersonBean;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description :
 */

public interface AttentionDesignerContract {

    interface View extends BaseSwipeView {
        //显示设计师列表
        void showDesignerList(PageInfo<PersonBean> list);

        //未关注设计师 状态
        void cancelAttention(int position);

    }

    abstract class Presenter extends BasePresenter {

        /**
         * 获取设计师列表
         */
        public abstract void getDesignerList(int start);

        /**
         * 取消关注
         */
        public abstract void cancelAttentionDesigner(String id,int position);
    }
}
