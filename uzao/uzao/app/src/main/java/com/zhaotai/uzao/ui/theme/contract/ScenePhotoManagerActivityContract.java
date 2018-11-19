package com.zhaotai.uzao.ui.theme.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ScenePhotoManagerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/7/27
 * Created by LiYou
 * Description :场景管理Contract
 */

public interface ScenePhotoManagerActivityContract {

    interface View extends BaseSwipeView {

        void showManagerPhotoPageInfo(PageInfo<ScenePhotoManagerBean> pageInfo);

        void showUploadSuccess();

        void showChangeNameSuccess(String titleName);

        void delSuccess();
    }

    abstract class Presenter extends BasePresenter {

        protected abstract void changeSelectState(List<ScenePhotoManagerBean> data, boolean status);

        public abstract void getPhotoData(boolean isLoading, int start, String albumiId);

        public abstract void upLoadBitmap(String path,String albumId);

        public abstract void deletePhoto(String album,List<ScenePhotoManagerBean> data);

        public abstract void changeAlbumName(String albumId, String titleName);
    }
}
