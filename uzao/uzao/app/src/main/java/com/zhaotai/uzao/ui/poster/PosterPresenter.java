package com.zhaotai.uzao.ui.poster;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.PosterItemBean;
import com.zhaotai.uzao.bean.PosterTemplateBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Time: 2017/8/22
 * Created by zp
 * Description :  海报页面Poster
 */

public class PosterPresenter extends PosterContract.Presenter {

    //使用点类型（spu、sourceMaterial、designer、theme）
    private static final String POINT_TYPE_SPU = "spu";
    private static final String POINT_TYPE_material_benefit = "material_benefit";
    private static final String POINT_TYPE_MATERIAL = "sourceMaterial";
    private static final String POINT_TYPE_DESIGNER = "designer";
    private static final String POINT_TYPE_THEME = "theme";
    //海报类型：平台(platform)/海报(theme)
    private static final String PLATFORM_TYPE_PLATFORM = "platform";
    private static final String PLATFORM_TYPE_THEME = "theme";


    private PosterContract.View mView;

    public PosterPresenter(Context context, PosterContract.View view) {
        mContext = context;
        this.mView = view;
    }

    @Override
    public void getPosterData() {

//        String data = createData();
//        Type mType = new TypeToken<List<PosterItemBean>>() {
//        }.getType();
//        List<PosterItemBean> posterItemBeans = GsonUtil.getGson().fromJson(data, mType);
//        mView.setViewData(posterItemBeans);
    }

    @Override
    public void getTemplate(String type, final String contentId) {
        mView.showLoading();
        String posterType = null;
        String usePointType = null;
        if (type.equals("material")) {
            posterType = PLATFORM_TYPE_PLATFORM;
            usePointType = POINT_TYPE_MATERIAL;
        } else if (type.equals("designer")) {
            posterType = PLATFORM_TYPE_PLATFORM;
            usePointType = POINT_TYPE_DESIGNER;
        } else if (type.equals("theme")) {
            posterType = PLATFORM_TYPE_THEME;
            usePointType = POINT_TYPE_THEME;
        } else if (type.equals("spu")) {
            posterType = PLATFORM_TYPE_PLATFORM;
            usePointType = POINT_TYPE_SPU;
        } else if (type.equals("material_benefit")) {
            posterType = PLATFORM_TYPE_PLATFORM;
            usePointType = POINT_TYPE_material_benefit;
        }


        Api.getDefault()
                .getPosterTemplate(posterType, usePointType,"")
                .compose(RxHandleResult.<List<PosterTemplateBean>>handleResultMap())
                .flatMap(new Function<List<PosterTemplateBean>, ObservableSource<BaseResult<PosterItemBean>>>() {
                    @Override
                    public ObservableSource<BaseResult<PosterItemBean>> apply(List<PosterTemplateBean> posterTemplateBeans) throws Exception {
                        if (posterTemplateBeans != null && posterTemplateBeans.size() != 0) {
                            PosterTemplateBean posterTemplateBean = posterTemplateBeans.get(0);
                            return Api.getDefault().getPosterData(posterTemplateBean.get_id(), contentId);
                        } else {
                            return Observable.error(new Throwable("模板列表数据错误"));
                        }
                    }
                })
                .compose(RxHandleResult.<PosterItemBean>handleResult())
                .subscribe(new RxSubscriber<PosterItemBean>(mContext) {
                    @Override
                    public void _onNext(PosterItemBean bean) {
                        analyzeData(bean);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showLong("海报数据错误");
                    }
                });
    }

    private void analyzeData(PosterItemBean bean) {
        //设置背景
        String posterBackgroundUrl = bean.getPosterBackgroundUrl();
        mView.setBgImage(ApiConstants.UZAOCHINA_IMAGE_HOST + posterBackgroundUrl);
        //
        List<PosterItemBean.DesignMetaBean> designMeta = bean.getDesignMeta();
        mView.setViewData(designMeta);
    }
}
