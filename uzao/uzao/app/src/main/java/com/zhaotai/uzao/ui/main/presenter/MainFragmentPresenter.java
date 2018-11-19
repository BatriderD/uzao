package com.zhaotai.uzao.ui.main.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MainTabBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.main.contract.MainFragmentNewContract;

import org.greenrobot.eventbus.EventBus;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description : 请求一级页面
 */

public class MainFragmentPresenter extends MainFragmentNewContract.Presenter {
    private MainFragmentNewContract.View mView;

    public MainFragmentPresenter(Context context, MainFragmentNewContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获取首页自定义标签列表
     */
    @Override
    public void getTabData() {
        Api.getDefault().getMainTabDataAsync("")
                .compose(RxHandleResult.<MainTabBean>handleResult())
                .subscribe(new RxSubscriber<MainTabBean>(mContext) {
                    @Override
                    public void _onNext(MainTabBean mainTabBeen) {
                        if (mainTabBeen.children.size() > 1) {
                            EventBus.getDefault().post(mainTabBeen.children.get(0));
                            mainTabBeen.children.remove(0);
                            mView.addTabList(mainTabBeen.children);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }


    /**
     * 请求未读消息
     */
    @Override
    public void getUnHandleMessage() {
        Api.getDefault().getUnReadCount()
                .compose(RxHandleResult.<Integer>handleResult())
                .subscribe(new RxSubscriber<Integer>(mContext, false) {
                    @Override
                    public void _onNext(Integer integer) {
                        mView.showUnHandleMessage(integer);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
