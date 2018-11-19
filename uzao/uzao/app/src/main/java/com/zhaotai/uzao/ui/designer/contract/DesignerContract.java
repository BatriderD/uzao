package com.zhaotai.uzao.ui.designer.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.PersonBean;

import java.util.List;

/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 设计师主页
 */

public interface DesignerContract {

    interface View extends BaseView {
        /**
         * 显示设计师基本信息
         */
        void showDesignerInfo(PersonBean info);

        /**
         * 关注设计师
         */
        void attentionDesigner();

        /**
         * 取消关注设计师
         */
        void cancelDesigner();

        /**
         * 改变设计师状态
         */
        void changeDesigner(boolean add);

        /**
         * 显示打赏金额
         */
        void showReward(List<DictionaryBean> price);
        /**
         * 显示分享面板
         */
        void openShareBoard(boolean hasPoster);
    }

    abstract class Presenter extends BasePresenter {
        /**
         * 获取设计师基本信息
         */
        public abstract void getDesignerInfo(String id);

        /**
         * 关注设计师
         */
        public abstract void attentionDesigner(String id);

        /**
         * 取消关注设计师
         */
        public abstract void cancelDesigner(String id);

        /**
         * 判断设计师是否被我关注
         */
        public abstract void isPayAttention(String id);

        /**
         * 判断是否登录
         */
        public abstract boolean isLogin();

        public abstract void hasPoster();
    }
}
