package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.BasePageWithCount;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.MaterialDiscussBean;
import com.zhaotai.uzao.bean.MaterialDiscussCommitRequestBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.PosterDetailContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

public class PostDetailPresenter extends PosterDetailContract.Presenter {
    private PosterDetailContract.View mView;

    public PostDetailPresenter(Context context, PosterDetailContract.View mView) {
        this.mView = mView;
        mContext = context;
    }


    @Override
    public void getPostDetail(String postId) {
        Api.getDefault().getPostDetail(postId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        System.out.println("啦啦啦" + s);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void getDiscussList(String posts, String postId, final int start, final boolean loadingStatus) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("lenght", "10");
        params.put("parentId", "ROOT");
        Api.getDefault()
                .getDiscussList(posts, postId, params)
                .compose(RxHandleResult.<BasePageWithCount<DiscussBean>>handleResult())
                .subscribe(new RxSubscriber<BasePageWithCount<DiscussBean>>(mContext) {
                    @Override
                    public void _onNext(BasePageWithCount<DiscussBean> withCountBean) {
                        if (loadingStatus && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        mView.showMaterialDiscussList(withCountBean.pageInfo);
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


    /**
     * 提交评论
     *
     * @param type       评论类型
     * @param materialId 评论id
     * @param word       评论内容
     */
    @Override
    public void commitDiscuss(String type, String materialId, String word, String themeId) {
        MaterialDiscussCommitRequestBean materialDiscussCommitRequestBean = new MaterialDiscussCommitRequestBean(word);
        materialDiscussCommitRequestBean.setExtend1(themeId);
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


    /**
     * 评论评论
     *
     * @param type           评论类型
     * @param materialId     评论id
     * @param parentId       评论父评论的id
     * @param firstCommentId 父评论相关内容
     * @param word           评论id
     */
    @Override
    public void commitDiscussDiscuss(String type, String materialId, String parentId, String firstCommentId, String word, String themeId) {
        MaterialDiscussCommitRequestBean materialDiscussCommitRequestBean = new MaterialDiscussCommitRequestBean(word, firstCommentId, parentId);
        materialDiscussCommitRequestBean.setExtend1(themeId);
        Api.getDefault().commitDiscuss(type, materialId, materialDiscussCommitRequestBean)
                .compose(RxHandleResult.<MaterialDiscussBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDiscussBean>(mContext) {
                    @Override
                    public void _onNext(MaterialDiscussBean s) {
                        ToastUtil.showShort("评论成功！");
                        mView.showCommitDiscussDicussSuccess();
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

}
