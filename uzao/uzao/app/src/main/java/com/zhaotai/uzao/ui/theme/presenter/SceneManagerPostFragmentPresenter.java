package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.SceneManagerPostBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.SceneManagerPostFragmentContract;

import java.util.HashMap;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  场景帖子fragment
 */

public class SceneManagerPostFragmentPresenter extends SceneManagerPostFragmentContract.Presenter {
    private SceneManagerPostFragmentContract.View mView;
    private HashMap<String, String> params = new HashMap();

    public SceneManagerPostFragmentPresenter(Context context, SceneManagerPostFragmentContract.View mView) {
        this.mView = mView;
        mContext = context;
    }


    @Override
    public void getPostList(final int start, final boolean isLoadingStatus, String themeId) {
        params.clear();
        params.put("themeId", themeId);
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        params.put("sidx", "topTime");
        params.put("sort", "desc");
        Api.getDefault().getMyManagerScenePostList(params)
                .compose(RxHandleResult.<PageInfo<SceneManagerPostBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<SceneManagerPostBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<SceneManagerPostBean> data) {
                        if (isLoadingStatus && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        }  else {
                            mView.stopLoadingMore();
                        }
                        mView.showPostData(data);
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

    @Override
    public void delPost(String id, final int mSelectedPos) {
        Api.getDefault().delScenePost(id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext,true) {
                    @Override
                    public void _onNext(String data) {
                        mView.showDelPostSuccess(mSelectedPos);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showDelPostFailed();
                    }
                });
    }

    @Override
    public void topPost(String id) {
        Api.getDefault().topScenePost(id)
                .compose(RxHandleResult.<SceneManagerPostBean>handleResult())
                .subscribe(new RxSubscriber<SceneManagerPostBean>(mContext,true) {
                    @Override
                    public void _onNext(SceneManagerPostBean data) {
                        mView.showTopPostSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showTopPostFailed();
                    }
                });
    }

    @Override
    public void essencePost(String id, final int mSelectedPos) {
        Api.getDefault().essenceScenePost(id)
                .compose(RxHandleResult.<SceneManagerPostBean>handleResult())
                .subscribe(new RxSubscriber<SceneManagerPostBean>(mContext,true) {
                    @Override
                    public void _onNext(SceneManagerPostBean data) {
                        mView.showEssencePostSuccess(mSelectedPos);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showEssencePostFailed();
                    }
                });
    }
}
