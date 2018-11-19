package com.zhaotai.uzao.ui.discuss.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.BasePageWithCount;
import com.zhaotai.uzao.bean.DiscussBean;
import com.zhaotai.uzao.bean.MaterialDiscussBean;
import com.zhaotai.uzao.bean.MaterialDiscussCommitRequestBean;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.discuss.contract.DiscussMainListContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;

/**
 * description: 对主题讨论presenter
 * author : ZP
 * date: 2018/1/11 0011.
 */

public class DiscussMainListPresenter extends DiscussMainListContract.Presenter {
    private DiscussMainListContract.View mView;

    public DiscussMainListPresenter(Context context, DiscussMainListContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 取消关注
     * @param materialId 关注id
     * @param pos 关注内容
     */
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

    /**
     * 取消关注
     * @param materialId 评论内容
     * @param pos 评论位置
     */
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

    /**
     * 提交评论
     * @param type 评论类型
     * @param materialId 评论id
     * @param word 评论内容
     */
    @Override
    public void commitDiscuss(String type, String materialId, String word) {
        Api.getDefault().commitDiscuss(type, materialId, new MaterialDiscussCommitRequestBean(word))
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
     * @param type 评论类型
     * @param materialId 评论id
     * @param parentId 评论父评论的id
     * @param firstCommentId 父评论相关内容
     * @param word 评论id
     */
    @Override
    public void commitDiscussDiscuss(String type, String materialId, String parentId, String firstCommentId, String word) {
        MaterialDiscussCommitRequestBean materialDiscussCommitRequestBean = new MaterialDiscussCommitRequestBean(word, firstCommentId, parentId);
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

    /**
     * 获取评论列表
     * @param type 评论类型
     * @param discussObjId 评论id
     * @param start 开始位置
     * @param inLoginstatus 评论状态
      */
    @Override
    public void getDiscussList(String type, String discussObjId, final int start, final boolean inLoginstatus) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("lenght", "10");
        params.put("parentId", "ROOT");
        Api.getDefault()
                .getDiscussList(type, discussObjId, params)
                .compose(RxHandleResult.<BasePageWithCount<DiscussBean>>handleResult())
                .subscribe(new RxSubscriber<BasePageWithCount<DiscussBean>>(mContext) {
                    @Override
                    public void _onNext(BasePageWithCount<DiscussBean> withCountBean) {
                        if (inLoginstatus && start == Constant.PAGING_HOME) {
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
     * 删除评论
     * @param discussType 评论类型
     * @param sequenceNBR 评论id
     * @param pos 评论在列表中位置
     */
    @Override
    public void del(String discussType, String sequenceNBR, final int pos) {
        Api.getDefault().deleteDiscuss(discussType, sequenceNBR)
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

    /**
     * 获取主题信息
     * @param discussObjId 主题id
     */
    @Override
    public void getThemeData(String discussObjId) {
        Api.getDefault().checkTheme(discussObjId)
                .compose(RxHandleResult.<ThemeBean>handleResult())
                .subscribe(new RxSubscriber<ThemeBean>(mContext) {
                    @Override
                    public void _onNext(ThemeBean themeBean) {
                        mView.showThemeDetail(themeBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
