package com.zhaotai.uzao.ui.designer.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.designer.contract.NewDesignerListContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/3/16 0016.
 */

public class NewDesignerListPresenter extends NewDesignerListContract.Presenter {

    private NewDesignerListContract.View mView;

    public NewDesignerListPresenter(Context context, NewDesignerListContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getDesignerList(final int start, final boolean isLoadingfinal) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("sort", "default_");
        Api.getDefault().getDesignerList(params)
                .compose(RxHandleResult.<PageInfo<DesignerBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DesignerBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<DesignerBean> data) {
                        //更新我的足迹数量
                        mView.showDesignList(data);
                        if (isLoadingfinal && start == 0) {
                            mView.showContent();
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                        System.out.println("足迹查询失败" + message);
                    }
                });

    }


    /**
     * 关注设计师
     *
     * @param id 设计师id
     */
    @Override
    public void attentionDesigner(final int pos, String id) {
        Api.getDefault().attentionDesigner(id)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.changeDesigner(pos, true);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 取消关注设计师
     *
     * @param id 设计师id
     */
    @Override
    public void cancelDesigner(final int pos, String id) {
        List<String> idList = new ArrayList<>();
        idList.add(id);
        Api.getDefault().cancelAttentionDesigner(idList)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        mView.changeDesigner(pos, false);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("数据请求失败");
                    }
                });
    }


}
