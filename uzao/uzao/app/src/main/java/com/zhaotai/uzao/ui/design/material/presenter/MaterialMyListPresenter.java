package com.zhaotai.uzao.ui.design.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.design.material.contract.MaterialListMyContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * description: 我的素材的搜索presenter
 * author : ZP
 * date: 2018/3/9 0009.
 */
public class MaterialMyListPresenter extends MaterialListMyContract.Presenter {
    private MaterialListMyContract.View mView;

    public MaterialMyListPresenter(Context context, MaterialListMyContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获得购买素材列表
     *
     * @param start         起始
     * @param loadingStatus 是第一次进来 还是刷新
     */
    @Override
    public void getMyBoughtMaterial(final int start, final boolean loadingStatus) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("status", "inExpired");
        params.put("length", String.valueOf(15));
        Api.getDefault().getMyBoughtMaterial(params)
                .compose(RxHandleResult.<PageInfo<MyMaterialBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyMaterialBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MyMaterialBean> data) {
                        mView.showContent();
                        if (loadingStatus && start == Constant.PAGING_HOME) {
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        //因为素材列表字段不统一 所以重新整理到一个里面
                        PageInfo<MaterialDetailBean> pageInfo = releaseBoughtList(data);
                        mView.showContentList(pageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    /**
     * 请求我的上传素材列表
     *
     * @param start 开始序号
     */
    @Override
    public void getUpLoadMaterialList(final int start, final boolean loadingStatus) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("length", String.valueOf(15));
        params.put("status", "published");
        Api.getDefault().getUploadMaterial(params)
                .compose(RxHandleResult.<PageInfo<MyUploadMaterialBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyUploadMaterialBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MyUploadMaterialBean> info) {
                        mView.showContent();
                        if (loadingStatus && start == Constant.PAGING_HOME) {
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        //因为素材列表字段不统一 所以重新整理到一个里面
                        PageInfo<MaterialDetailBean> pageInfo = releaseUploadList(info);
                        mView.showContentList(pageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    /**
     * 获得收藏的素材列表
     *
     * @param start         起始
     * @param loadingStatus 当前状态 是状态页true  还是下拉刷新false
     */
    @Override
    public void getMyCollectMaterial(final int start, final boolean loadingStatus) {
        final HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        Api.getDefault().getCollectionMaterialList(params)
                .compose(RxHandleResult.<PageInfo<MaterialListBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MaterialListBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MaterialListBean> goodsBean) {
                        mView.showContent();
                        if (loadingStatus && start == Constant.PAGING_HOME) {
                            mView.stopLoadingMore();
                        } else if (start == 0) {
                            mView.stopRefresh();
                        } else {
                            mView.stopLoadingMore();
                        }
                        //因为素材列表字段不统一 所以重新整理到一个里面
                        PageInfo<MaterialDetailBean> pageInfo = releaseCollectionList(goodsBean);
                        mView.showContentList(pageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.stopRefresh();
                            mView.showNetworkFail(message);
                        } else {
                            mView.loadingFail();
                        }
                    }
                });
    }

    /**
     * 重新处理收藏的素材列表
     *
     * @param data 请求来的数据
     * @return 处理之后的素材列表
     */
    private PageInfo<MaterialDetailBean> releaseCollectionList(PageInfo<MaterialListBean> data) {
        PageInfo<MaterialDetailBean> pageInfo = new PageInfo<>();
        pageInfo.currentPage = data.currentPage;
        pageInfo.hasNextPage = data.hasNextPage;
        pageInfo.pageRecorders = data.pageRecorders;
        pageInfo.pageStartRow = data.pageStartRow;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalRows = data.totalRows;

        List<MaterialListBean> list = data.list;
        ArrayList<MaterialDetailBean> showList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            MaterialDetailBean myMaterialShowBean;
            for (int i = 0; i < list.size(); i++) {
                MaterialListBean myMaterialBean = list.get(i);
                myMaterialShowBean = new MaterialDetailBean();
                myMaterialShowBean.priceY = myMaterialBean.priceY;
                myMaterialShowBean.thumbnail = myMaterialBean.thumbnail;
                myMaterialShowBean.sourceMaterialId = myMaterialBean.materialId;
                myMaterialShowBean.sourceMaterialName = myMaterialBean.sourceMaterialName;
                myMaterialShowBean.fileMime = myMaterialBean.fileMime;
                myMaterialShowBean.scale = myMaterialBean.scale;
                showList.add(myMaterialShowBean);
            }
        }
        pageInfo.list = showList;

        return pageInfo;
    }

    /**
     * 上传素材的数据类型
     *
     * @param data 分页的上传素材数据类型
     * @return 显示的分页数据
     */
    public PageInfo<MaterialDetailBean> releaseUploadList(PageInfo<MyUploadMaterialBean> data) {
        PageInfo<MaterialDetailBean> pageInfo = new PageInfo<>();
        pageInfo.currentPage = data.currentPage;
        pageInfo.hasNextPage = data.hasNextPage;
        pageInfo.pageRecorders = data.pageRecorders;
        pageInfo.pageStartRow = data.pageStartRow;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalRows = data.totalRows;

        List<MyUploadMaterialBean> list = data.list;
        ArrayList<MaterialDetailBean> showList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            MaterialDetailBean myMaterialShowBean;
            for (int i = 0; i < list.size(); i++) {
                MyUploadMaterialBean myMaterialBean = list.get(i);
                myMaterialShowBean = new MaterialDetailBean();
                myMaterialShowBean.priceY = myMaterialBean.getPriceY();
                myMaterialShowBean.thumbnail = myMaterialBean.getThumbnail();
                myMaterialShowBean.sourceMaterialId = myMaterialBean.getSequenceNbr();
                myMaterialShowBean.sourceMaterialName = myMaterialBean.getSourceMaterialName();
                myMaterialShowBean.fileMime = myMaterialBean.getFileMime();
                myMaterialShowBean.scale = String.valueOf(myMaterialBean.getScale());
                showList.add(myMaterialShowBean);
            }
        }
        pageInfo.list = showList;

        return pageInfo;

    }

    /**
     * 格式已购买的素材的数据类型
     *
     * @param data 分页的上传素材数据类型
     * @return 显示的分页数据
     */
    public PageInfo<MaterialDetailBean> releaseBoughtList(PageInfo<MyMaterialBean> data) {
        PageInfo<MaterialDetailBean> pageInfo = new PageInfo<>();
        pageInfo.currentPage = data.currentPage;
        pageInfo.hasNextPage = data.hasNextPage;
        pageInfo.pageRecorders = data.pageRecorders;
        pageInfo.pageStartRow = data.pageStartRow;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalPages = data.totalPages;
        pageInfo.totalRows = data.totalRows;

        List<MyMaterialBean> list = data.list;
        ArrayList<MaterialDetailBean> showList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            MaterialDetailBean myMaterialShowBean;
            for (int i = 0; i < list.size(); i++) {
                MyMaterialBean myMaterialBean = list.get(i);
                myMaterialShowBean = new MaterialDetailBean();
                myMaterialShowBean.priceY = String.valueOf(myMaterialBean.getPrice());
                myMaterialShowBean.thumbnail = myMaterialBean.getThumbnail();
                myMaterialShowBean.sourceMaterialId = myMaterialBean.getSourceMaterialId();
                myMaterialShowBean.sourceMaterialName = myMaterialBean.getSourceMaterialName();
                myMaterialShowBean.fileMime = myMaterialBean.getFileMime();
                myMaterialShowBean.scale = String.valueOf(myMaterialBean.getScale());
                showList.add(myMaterialShowBean);
            }
        }
        pageInfo.list = showList;

        return pageInfo;
    }

}
