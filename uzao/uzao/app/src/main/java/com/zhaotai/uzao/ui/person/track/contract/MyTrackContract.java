package com.zhaotai.uzao.ui.person.track.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.MyTrackBean;
import com.zhaotai.uzao.bean.MyTrackResultBean;
import com.zhaotai.uzao.bean.PageInfo;

import java.util.List;

/**
 * Time: 2017/12/1
 * Created by zp
 * Description :  足迹管理类
 */

public interface MyTrackContract {

    interface View extends BaseSwipeView {

        void showMyTrackList(List<MyTrackBean> lists, PageInfo<MyTrackResultBean> hasNext);
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getMyTrackList(final int start, final boolean isLoadingfinal, List<MyTrackBean> lists, boolean isSelected);

        public abstract void parseData(List<MyTrackBean> lists, List<MyTrackResultBean> newList,
                                       PageInfo<MyTrackResultBean> stringPageInfo, boolean isSelected);
    }
}
