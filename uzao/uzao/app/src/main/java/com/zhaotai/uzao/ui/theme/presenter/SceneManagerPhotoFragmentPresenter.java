package com.zhaotai.uzao.ui.theme.presenter;

import android.support.v4.app.FragmentActivity;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.SceneManagerAlbumBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.SceneManagerPhotoFragmentContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  场景相册fragment
 */

public class SceneManagerPhotoFragmentPresenter extends SceneManagerPhotoFragmentContract.Presenter {
    private SceneManagerPhotoFragmentContract.View mView;
    private HashMap<String, String> params = new HashMap();

    public SceneManagerPhotoFragmentPresenter(FragmentActivity context, SceneManagerPhotoFragmentContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    @Override
    public void getPhotoList(final int start, final boolean loginStatus, String themeId) {
        params.clear();
        params.put("themeId", themeId);
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        Api.getDefault()
                .getMyManagerScenePhotoList(params)
                .compose(RxHandleResult.<PageInfo<SceneManagerAlbumBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<SceneManagerAlbumBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<SceneManagerAlbumBean> data) {
                        if (loginStatus && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showPhotoData(data);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingMoreFail();
                        }
                    }
                });
    }

    /**
     * 添加相册
     *
     * @param themeId 场景id
     * @param name    相册名称
     */
    @Override
    public void addAlbum(String themeId, final String name) {
        SceneManagerAlbumBean sceneManagerAlbumBean = new SceneManagerAlbumBean(name);

        Api.getDefault()
                .addMyManagerSceneAlbum(themeId, sceneManagerAlbumBean)
                .compose(RxHandleResult.<SceneManagerAlbumBean>handleResult())
                .subscribe(new RxSubscriber<SceneManagerAlbumBean>(mContext) {
                    @Override
                    public void _onNext(SceneManagerAlbumBean data) {
                        mView.addAlbumSuccess(name, data.getSequenceNBR());
//                        ScenePhotoManagerActivity.launch(mContext,name,data.getSequenceNBR());
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("相册添加失败");
                    }
                });
    }

    @Override
    public void delAlbum(String albumId) {
        Api.getDefault().deleteAlbum(albumId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.delAlbumSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("相册删除失败");
                    }
                });
    }
}
