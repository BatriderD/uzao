package com.zhaotai.uzao.ui.design.catchWord.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.CatchWordBean;
import com.zhaotai.uzao.bean.CatchWordTabBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.catchWord.contract.CatchWordContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description: 流行词主列表presenter
 * author : ZP
 * date:2018/1/8
 */

public class CatchWordPresenter extends CatchWordContract.Presenter {
    private CatchWordContract.View mView;

    public CatchWordPresenter(Context context, CatchWordContract.View view) {
        this.mContext = context;
        mView = view;
    }

    @Override
    public void getCatchWordTabList() {
        Api.getDefault().getCatchWordTabList()
                .compose(RxHandleResult.<List<CatchWordTabBean>>handleResult())
                .subscribe(new RxSubscriber<List<CatchWordTabBean>>(mContext) {
                    @Override
                    public void _onNext(List<CatchWordTabBean> tabList) {
                        mView.showContent();
                        mView.showTabList(tabList);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }

    @Override
    public void getCatchWordContentList(final int start, String parentCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("categoryCode1", parentCode);
        params.put("start", String.valueOf(start));
        params.put("length", "10");
        Api.getDefault().getCatchWordContentList(params)
                .compose(RxHandleResult.<PageInfo<CatchWordBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<CatchWordBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<CatchWordBean> catchWordBeanPageInfo) {
                        if (start == Constant.PAGING_HOME) {
                            mView.stopRefresh();
                            mView.showTopicListContent();
                        } else {
                            mView.stopLoadingMore();
                        }
                        releaseData(catchWordBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == Constant.PAGING_HOME) {
                            mView.showTopicListError(message);
                        }

                    }
                });
    }

    /**
     * 过滤数据未启用的数据
     *
     * @param data 分页流行词数据
     */
    private void releaseData(PageInfo<CatchWordBean> data) {
        PageInfo<CatchWordBean> pageInfo = new PageInfo<>();
        pageInfo.currentPage = data.currentPage;
        pageInfo.hasNextPage = data.hasNextPage;
        pageInfo.pageRecorders = data.pageRecorders;
        pageInfo.pageStartRow = data.pageStartRow;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalRows = data.totalRows;


        List<CatchWordBean> list = data.list;
        ArrayList<CatchWordBean> showList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CatchWordBean myMaterialBean = list.get(i);
                if (!"Y".equals(myMaterialBean.getLockStatus())) {
                    showList.add(myMaterialBean);
                }
            }
        }
        pageInfo.list = showList;

        mView.showCatchWordContentList(pageInfo);
    }


}
