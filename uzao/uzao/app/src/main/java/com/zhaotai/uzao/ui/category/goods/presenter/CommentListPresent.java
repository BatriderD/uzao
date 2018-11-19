package com.zhaotai.uzao.ui.category.goods.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.CommentListBean;
import com.zhaotai.uzao.bean.StartCountBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.goods.contract.CommentListContract;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Time: 2017/8/18
 * Created by LiYou
 * Description : 评价列表
 */

public class CommentListPresent extends CommentListContract.Presenter {

    private CommentListContract.View view;

    public CommentListPresent(Context context, CommentListContract.View view) {
        mContext = context;
        this.view = view;
    }

    /**
     * 获取评价列表
     *
     * @param start     开始位置
     * @param isLoading 是否loading
     * @param entityId  商品id
     * @param haveImage 是否包含照片
     * @param starScore 评价几星
     */
    @Override
    public void getCommentList(final int start, final boolean isLoading, String entityId, boolean haveImage, String starScore) {
        Api.getDefault().getStateCommentList(entityId, haveImage ? "Y" : "", start, starScore)
                .compose(RxHandleResult.<CommentListBean>handleResult())
                .subscribe(new RxSubscriber<CommentListBean>(mContext) {
                    @Override
                    public void _onNext(CommentListBean commentBeen) {
                        if (isLoading && start == 0) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showCommentList(commentBeen.pageInfo);
                        //评价星星
                        if (commentBeen.start.startCount.size() > 0) {
                            //根据星级 排序
                            Collections.sort(commentBeen.start.startCount, new Comparator<StartCountBean>() {
                                @Override
                                public int compare(StartCountBean o1, StartCountBean o2) {
                                    return Integer.valueOf(o2.STAR_SCORE).compareTo(o1.STAR_SCORE);
                                }
                            });
                            List<StartCountBean> startData = commentBeen.start.startCount;
                            StartCountBean startCountBean = new StartCountBean();
                            startCountBean.COUNT1 = commentBeen.start.all;
                            startData.add(0, startCountBean);
                            view.showCommentStart(startData);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }
}
