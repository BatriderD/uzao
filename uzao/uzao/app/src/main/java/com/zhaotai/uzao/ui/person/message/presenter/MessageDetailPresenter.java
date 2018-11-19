package com.zhaotai.uzao.ui.person.message.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.MessageDetailBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.message.contract.MessageDetailContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description :
 */

public class MessageDetailPresenter extends MessageDetailContract.Presenter {

    private MessageDetailContract.View view;


    public MessageDetailPresenter(MessageDetailContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    @Override
    public void getMessageList(String type, final int start, final boolean inLoginStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("messageTargetType", type);
        params.put("start", String.valueOf(start));
        Api.getDefault().getMessageDetailList(params)
                .compose(RxHandleResult.<PageInfo<MessageDetailBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MessageDetailBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<MessageDetailBean> messageBeanPageInfo) {
                        if (inLoginStatus && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showMessageList(messageBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showNetworkFail(message);
                    }
                });
    }


    @Override
    public void getCommentMessageList(String type, final int start, final boolean inLoginStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("messageTargetType", type);
        params.put("start", String.valueOf(start));
        Api.getDefault().getMessageCommentDetailList(params)
                .compose(RxHandleResult.<PageInfo<MessageDetailBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MessageDetailBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<MessageDetailBean> messageBeanPageInfo) {
                        if (inLoginStatus && start == Constant.PAGING_HOME) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showMessageList(messageBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showNetworkFail(message);
                    }
                });
    }


}
