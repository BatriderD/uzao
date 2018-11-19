package com.zhaotai.uzao.ui.designer.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.DesignerBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.designer.contract.NewDesignerListSearchContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class NewDesignerListSearchPresenter extends NewDesignerListSearchContract.Presenter {
    private NewDesignerListSearchContract.View mView;

    public NewDesignerListSearchPresenter(NewDesignerListSearchContract.View view, Context context) {
        super(view, context);
        mView = view;
    }

    @Override
    public void getCommodityList(final int start, HashMap<String, String> params) {
        Api.getDefault().getDesignerList(params)
                .compose(RxHandleResult.<PageInfo<DesignerBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DesignerBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<DesignerBean> data) {

                        mView.showCommodityList(data);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            ToastUtil.showShort("网络请求失败");
                        } else {
                            mView.loadingMoreFail();
                        }
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
