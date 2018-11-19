package com.zhaotai.uzao.ui.person.message.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.bean.MessageCenterBean;

import java.util.List;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息中心
 */

public interface MessageCenterContract {

    interface View extends BaseView {
        void showCenterList(List<MessageCenterBean> datas);
    }

    abstract class Presenter extends BasePresenter {
        public abstract void getMessageCenterList();
    }
}
