package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.ArtFontTabBean;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.contract.ArtFontMainContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class ArtFontMainPresenter extends ArtFontMainContract.Presenter {

    private ArtFontMainContract.View mView;

    public ArtFontMainPresenter(Context context, ArtFontMainContract.View view) {
        mContext = context;
        this.mView = view;
    }


    @Override
    public void getArtTabList(final int Start) {
        HashMap<String, String> params = new HashMap<>();
        Api.getDefault().getArtFontTabList(params)
                .compose(RxHandleResult.<PageInfo<ArtFontTabBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ArtFontTabBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ArtFontTabBean> info) {
                        ArtFontTabBean artFontTabBean = new ArtFontTabBean();
                        artFontTabBean.setTopicCode("");
                        artFontTabBean.setTopicName("全部");
                        info.list.add(0, artFontTabBean);
                        parseTab(info);
                        mView.showTabList(info);
                    }


                    @Override
                    public void _onError(String message) {
                        if (Start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            ToastUtil.showShort("加载失败");
                        }
                    }
                });
    }

    /**
     * 过滤tab数据
     * @param info 字体tab条目信息
     */
    private void parseTab(PageInfo<ArtFontTabBean> info) {
        List<ArtFontTabBean> list = info.list;
        Iterator<ArtFontTabBean> iter = list.iterator();
        while (iter.hasNext()) {
            ArtFontTabBean bean = iter.next();
            //名字是否合适
            if ("Y".equals(bean.getLockStatus())) {
                iter.remove();
                continue;
            }
        }
    }

    @Override
    public void getTopicFontList(final int start, String topicCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("topicCode", topicCode);
        params.put("start", String.valueOf(start));
        params.put("getLockStatus", "N");
        Api.getDefault()
                .getArtFontTopicList(params)
                .compose(RxHandleResult.<PageInfo<ArtFontTopicBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ArtFontTopicBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ArtFontTopicBean> info) {
                        if (start == Constant.PAGING_HOME) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        filter(info);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showTopicListError(message);
                        } else {
                            mView.loadingFail();
                        }
                        ToastUtil.showShort("列表数据获取失败");
                    }
                });
    }

    /**
     * 过滤具体条目数据
     * @param info 字体条目分页数据信息
     */
    private void filter(PageInfo<ArtFontTopicBean> info) {
        List<ArtFontTopicBean> list = info.list;
        Iterator<ArtFontTopicBean> iter = list.iterator();
        while (iter.hasNext()) {
            ArtFontTopicBean bean = iter.next();
            //名字是否合适
            if ("Y".equals(bean.getLockStatus())) {
                iter.remove();
                continue;
            }
        }
        mView.showTopicList(info);
    }
}
