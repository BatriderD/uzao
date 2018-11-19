package com.zhaotai.uzao.ui.person.message.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MessageCenterBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.message.contract.MessageCenterContract;

import java.util.List;

/**
 * description: 消息中心列表 presenter
 * author : zp
 * date: 2017/7/21
 */

public class MessageCenterPresenter extends MessageCenterContract.Presenter {
    private MessageCenterContract.View mView;

    public MessageCenterPresenter(Context context, MessageCenterContract.View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getMessageCenterList() {
        Api.getDefault().getMessageCenterList()
                .compose(RxHandleResult.<List<MessageCenterBean>>handleResult())
                .subscribe(new RxSubscriber<List<MessageCenterBean>>(mContext, false) {
                    @Override
                    public void _onNext(List<MessageCenterBean> data) {
                        mView.showContent();
                        mView.showCenterList(data);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }

}
