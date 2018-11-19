package com.zhaotai.uzao.ui.person.address.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.RegionBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.address.contract.ReginSelectContract;

import java.util.List;

/**
 * description: 地区选择
 * author : zp
 * date: 2017/7/14
 */

public class ReginSelectPresenter extends ReginSelectContract.Presenter {
    private Context mContext;
    private ReginSelectContract.View mView;


    public ReginSelectPresenter(Context context, ReginSelectContract.View view) {
        mContext = context;
        mView = view;
    }

    /**
     * 获得城市列表数据
     */
    @Override
    public void getProvinces() {
        mView.showLoading();
        Api.getDefault().getProvinces()
                .compose(RxHandleResult.<List<RegionBean>>handleResult())
                .subscribe(new RxSubscriber<List<RegionBean>>(mContext, false) {
                    @Override
                    public void _onNext(List<RegionBean> regionBeen) {
                        mView.showContent();
                        mView.showRegin(regionBeen);
                    }

                    @Override
                    public void _onError(String message) {
                        mView.showNetworkFail(message);
                    }
                });
    }

    /**
     * 获得城市列表数据
     */
    @Override
    public void getRegion(String code) {
        //请求地区数据
        Api.getDefault().getCityList(code)
                .compose(RxHandleResult.<List<RegionBean>>handleResult())
                .subscribe(new RxSubscriber<List<RegionBean>>(mContext, false) {
                    @Override
                    public void _onNext(List<RegionBean> regionBeen) {
                        mView.showRegin(regionBeen);
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }
}
