package com.zhaotai.uzao.ui.person.invite.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.ui.person.invite.model.RebateBean;

/**
 * Time: 2017/12/2
 * Created by LiYou
 * Description :
 */

public interface InviteContract {

    interface View extends BaseView {
        //分享返利数据
        void showRebate(RebateBean rebate);
    }

    interface DetailView extends BaseSwipeView {
        void showDetail(PageInfo<RebateBean> data);

        //分享返利数据
        void showRebate(RebateBean rebate);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getRebate();

        public abstract void getInviteDetail(int start);
    }
}
