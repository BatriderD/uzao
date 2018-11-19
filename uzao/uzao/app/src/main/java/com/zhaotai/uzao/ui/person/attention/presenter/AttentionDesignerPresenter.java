package com.zhaotai.uzao.ui.person.attention.presenter;

import android.content.Context;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.attention.contract.AttentionDesignerContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/5/22
 * Created by LiYou
 * Description : 关注的设计师
 */

public class AttentionDesignerPresenter extends AttentionDesignerContract.Presenter {

    private AttentionDesignerContract.View view;

    public AttentionDesignerPresenter(AttentionDesignerContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
    }

    /**
     * 关注的设计师列表
     *
     * @param start 开始位置
     */
    @Override
    public void getDesignerList(int start) {
        Api.getDefault().getMyAttentionDesignerList(start, "")
                .compose(RxHandleResult.<PageInfo<PersonBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<PersonBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<PersonBean> personBeanPageInfo) {
                        view.showDesignerList(personBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        view.showNetworkFail(message);
                    }
                });
    }

    /**
     * 关注的设计师搜索
     *
     * @param start        开始位置
     * @param designerName 设计师名字
     */
    public void getDesignerList(final int start, String designerName) {
        Api.getDefault().getMyAttentionDesignerList(start, designerName)
                .compose(RxHandleResult.<PageInfo<PersonBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<PersonBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<PersonBean> personBeanPageInfo) {
                        if (personBeanPageInfo.totalRows > 0) {
                            view.showContent();
                            view.showDesignerList(personBeanPageInfo);
                        } else {
                            view.showEmpty(mContext.getString(R.string.empty_search));
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    /**
     * 取消关注设计师
     *
     * @param id       设计师Id
     * @param position 位置
     */
    @Override
    public void cancelAttentionDesigner(String id, final int position) {
        List<String> list = new ArrayList<>();
        list.add(id);
        Api.getDefault().cancelAttentionDesigner(list)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String s) {
                        view.cancelAttention(position);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
