package com.zhaotai.uzao.ui.person.message.presenter;

import android.content.Context;
import android.util.Log;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MaterialDiscussBean;
import com.zhaotai.uzao.bean.MaterialDiscussCommitRequestBean;
import com.zhaotai.uzao.bean.MessageCommentBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.message.contract.MessageCommentContract;
import com.zhaotai.uzao.utils.ToastUtil;

/**
 * description: 评论消息 presenter
 * author : zp
 * date: 2017/7/21
 */

public class MessageCommentPresenter extends MessageCommentContract.Presenter {
    private MessageCommentContract.View mView;

    public MessageCommentPresenter(Context context, MessageCommentContract.View view) {
        mView = view;
        mContext = context;
    }

    /**
     * 关注
     * @param materialId 关注id
     */
    @Override
    public void like(String materialId) {
        Api.getDefault()
                .likeMaterial("comment", materialId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.showLikeSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("请求失败");
                    }
                });
    }

    /**
     * 取消关注
     * @param materialId 关注id
     */
    @Override
    public void disLike(String materialId) {
        Api.getDefault()
                .cancelLike("comment", materialId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.showDisLikeSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("请求失败");
                    }
                });
    }

    /**
     * 获取评论内容
     * @param id 评论id
     */
    @Override
    public void getCommentData(final String id) {
        Api.getDefault().getCommentData(id)
                .compose(RxHandleResult.<MessageCommentBean>handleResult())
                .subscribe(new RxSubscriber<MessageCommentBean>(mContext) {
                    @Override
                    public void _onNext(MessageCommentBean s) {
                        mView.showData(s);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }


    /**
     * 评论评论
     * @param type 评论类型
     * @param materialId 评论id
     * @param parentId 评论父id
     * @param firstCommentId 评论的
     * @param word 品论内容
     */
    @Override
    public void commitDiscussDiscuss(String type, String materialId, String parentId, String firstCommentId, String word) {
        MaterialDiscussCommitRequestBean materialDiscussCommitRequestBean = new MaterialDiscussCommitRequestBean(word, firstCommentId, parentId);
        Log.d("Tag", "commitDiscussDiscuss: " + materialDiscussCommitRequestBean.toString());
        Api.getDefault().commitDiscuss(type, materialId, materialDiscussCommitRequestBean)
                .compose(RxHandleResult.<MaterialDiscussBean>handleResult())
                .subscribe(new RxSubscriber<MaterialDiscussBean>(mContext, true) {
                    @Override
                    public void _onNext(MaterialDiscussBean s) {
                        ToastUtil.showShort("评论成功！");
                        mView.showCommitDiscussDiscussSuccess();
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
     * 设置消息已读
     * @param id 消息id
     */
    public void putMessageRead(String id) {
        String[] ids = {id};
        Api.getDefault().putMessageRead(ids)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        System.out.println(1);
                    }

                    @Override
                    public void _onError(String message) {
                        System.out.println("tellMe" + message);
                    }
                });
    }
}
