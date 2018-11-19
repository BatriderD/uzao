package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.DesignBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.contract.MyDesignContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/9/5
 * Created by LiYou
 * Description :
 */

public class MyDesignPresenter extends MyDesignContract.Presenter {

    private MyDesignContract.View view;
    private List<String> ids;

    public MyDesignPresenter(Context context, MyDesignContract.View view) {
        mContext = context;
        this.view = view;
        ids = new ArrayList<>();
    }

    @Override
    public void getMyDesign(final int start, final boolean isLoading) {
        Api.getDefault().getMyDesignList(start)
                .compose(RxHandleResult.<PageInfo<DesignBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DesignBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<DesignBean> designBean) {
                        if (isLoading && start == 0) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showDesignList(designBean);
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

    /**
     * 全选 和取消全选
     */
    public void changeSelectState(List<DesignBean> goods, boolean isSelect) {
        for (int i = 0; i < goods.size(); i++) {
            goods.get(i).isSelected = isSelect;
        }
    }

    /**
     * 删除 我的设计
     */
    public void deleteMyDesign(List<DesignBean> goods) {
        ids.clear();
        for (int i = 0; i < goods.size(); i++) {
            if(goods.get(i).isSelected){
                ids.add(goods.get(i).sequenceNBR);
            }
        }
        if(ids.size() > 0){
            Api.getDefault().deleteMyDesign(ids)
                    .compose(RxHandleResult.<String>handleResult())
                    .subscribe(new RxSubscriber<String>(mContext,true) {
                        @Override
                        public void _onNext(String s) {
                            getMyDesign(0,false);
                            ToastUtil.showShort("删除成功");
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("删除失败");
                        }
                    });
        }
    }

}
