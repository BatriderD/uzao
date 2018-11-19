package com.zhaotai.uzao.ui.discuss.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.DiscussCenterBean;
import com.zhaotai.uzao.bean.MaterialDiscussBean;
import com.zhaotai.uzao.bean.MaterialDiscussCommitRequestBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.discuss.contract.DiscussCenterDialogContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description: 评论的评论中心presenter
 * author : ZP
 * date: 2018/1/11 0011.
 */

public class DiscussCenterDialogPresenter extends DiscussCenterDialogContract.Presenter {
    private DiscussCenterDialogContract.View mView;

    public DiscussCenterDialogPresenter(Context context, DiscussCenterDialogContract.View view) {
        mContext = context;
        mView = view;
    }


    @Override
    public void like(String materialId, final int pos) {
        Api.getDefault()
                .likeMaterial("comment", materialId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.showLikeSuccess(pos);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("请求失败");
                    }
                });
    }

    @Override
    public void disLike(String materialId, final int pos) {
        Api.getDefault()
                .cancelLike("comment", materialId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.showDisLikeSuccess(pos);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("请求失败");
                    }
                });
    }

    @Override
    public void commitDiscussDiscuss(String type,String materialId, String parentId, String firstCommentId, String word) {
        MaterialDiscussCommitRequestBean materialDiscussCommitRequestBean = new MaterialDiscussCommitRequestBean(word, firstCommentId, parentId);

        Api.getDefault().commitDiscuss(type, materialId, materialDiscussCommitRequestBean)
                .compose(RxHandleResult.<MaterialDiscussBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDiscussBean>(mContext) {
                    @Override
                    public void _onNext(MaterialDiscussBean s) {
                        ToastUtil.showShort("评论成功！");
                        mView.showCommitDiscussSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        if ("您不能回复自己的评论！".equals(message)) {
                            ToastUtil.showShort(message);
                        } else {
                            ToastUtil.showShort("评论失败");
                        }
                    }
                });




    }

    @Override
    public void getDiscussList(String firstCommentId, final int start, final boolean inLoginstatus) {
        HashMap<String, String> params = new HashMap<>();
//        start=0&length=10
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        Api.getDefault()
                .DiscussCenterList(firstCommentId, params)
                .compose(RxHandleResult.<DiscussCenterBean>handleResult())
                .subscribe(new RxSubscriber<DiscussCenterBean>(mContext) {
                    @Override
                    public void _onNext(DiscussCenterBean centerBean) {
                        if (inLoginstatus && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showCenterList(centerBean);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    @Override
    public void del(String dicussType, String sequenceNBR, final int pos) {
        Api.getDefault().deleteDiscuss(dicussType, sequenceNBR)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String string) {
                        ToastUtil.showShort("删除成功");
                        mView.showDelCommentSuccess(pos);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("删除失败");
                    }
                });

    }

}
