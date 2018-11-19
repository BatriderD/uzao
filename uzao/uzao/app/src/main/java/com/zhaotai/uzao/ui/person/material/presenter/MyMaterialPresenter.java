package com.zhaotai.uzao.ui.person.material.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MyBoughtMaterialCategoryBean;
import com.zhaotai.uzao.bean.MyMaterialBean;
import com.zhaotai.uzao.bean.MyUploadMaterialBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.material.contract.MyMaterialContract;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

/**
 * description: 我的素材管理类
 * author : ZP
 * date: 2018/1/9 0009.
 */

public class MyMaterialPresenter extends MyMaterialContract.Presenter {
    MyMaterialContract.View mView;

    public MyMaterialPresenter(Context context, MyMaterialContract.View view) {
        mView = view;
        mContext = context;
    }

    /**
     * 我的已购素材
     * @param start 起始位置
     * @param code 类别关键字
     */
    @Override
    public void getMyBoughtMaterial(final int start, String code) {
        HashMap<String, String> params = new HashMap<>();
        params.put("start", String.valueOf(start));
        params.put("length", String.valueOf(10));
        params.put("categoryCode1", code);
        Api.getDefault().getMyBoughtMaterial(params)
                .compose(RxHandleResult.<PageInfo<MyMaterialBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MyMaterialBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MyMaterialBean> data) {
                        mView.showContent();
                        mView.showMaterialList(data);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        }
                    }
                });
    }

    /**
     * 获取已购素材都有哪些类别
     */
    @Override
    public void getMyBoughtMaterialCategory() {
        HashMap<String, String> params = new HashMap<>();
        Api.getDefault().getMyBoughtMaterialCategory(params)
                .compose(RxHandleResult.<List<MyBoughtMaterialCategoryBean>>handleResult())
                .subscribe(new RxSubscriber<List<MyBoughtMaterialCategoryBean>>(mContext) {
                    @Override
                    public void _onNext(List<MyBoughtMaterialCategoryBean> beans) {
                        mView.showCategoryList(beans);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 请求我的上传素材列表
     *
     * @param start 开始序号
     */
    @Override
    public void getUpLoadMaterialList(final int start) {
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
                        mView.showUploadList(info);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        }
                    }
                });
    }

    /**
     * 删除素材
     * @param sourceMaterialId 素材id
     * @param position 下标
     */
    @Override
    public void delMaterial(String sourceMaterialId, final int position) {

        Api.getDefault().DelMyBoughtMaterial(sourceMaterialId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    public void _onNext(String s) {
                        mView.showDelSuccess(position);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("删除失败");
                    }
                });

    }


}
