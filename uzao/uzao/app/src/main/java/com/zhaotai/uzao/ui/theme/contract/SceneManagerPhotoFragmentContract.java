package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BaseNoSwipeView;
import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.SceneManagerAlbumBean;

public interface SceneManagerPhotoFragmentContract {
    interface View extends BaseNoSwipeView {

        void showPhotoData(PageInfo<SceneManagerAlbumBean> data);

        void addAlbumSuccess(String name, String albumId);

        void delAlbumSuccess();
    }

    abstract class Presenter extends BasePresenter {

        public abstract void getPhotoList(int i, boolean b,String themeId);

        public abstract void addAlbum(String themeId, String name);

        public abstract void delAlbum(String sequenceNBR);
    }
}
