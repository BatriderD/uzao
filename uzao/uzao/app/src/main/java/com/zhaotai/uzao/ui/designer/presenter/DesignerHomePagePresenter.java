package com.zhaotai.uzao.ui.designer.presenter;


import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.PosterTemplateBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.designer.contract.DesignerContract;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 设计师主页 presenter
 */

public class DesignerHomePagePresenter extends DesignerContract.Presenter {

    private DesignerContract.View view;
    private Context context;
    public DesignerHomePagePresenter(DesignerContract.View view, Context context){
        this.view = view;
        this.context = context;
    }

    /**
     * 获取设计师基本信息
     * @param designerId 设计师id
     */
    @Override
    public void getDesignerInfo(String designerId) {
        Api.getDefault().getDesignerInfo(designerId)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(context, false) {
                    @Override
                    public void _onNext(PersonBean personBean) {
                        //显示设计师基本信息
//                        view.showContent();
                        view.showDesignerInfo(personBean);
                    }

                    @Override
                    public void _onError(String message) {
                        LogUtils.logd(message);
                    }
                });
    }

    /**
     *  关注设计师
     * @param designerId 设计师id
     */
    @Override
    public void attentionDesigner(String designerId) {
        if (!isLogin()) return;
        Api.getDefault().attentionDesigner(designerId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(context, true) {
                    @Override
                    public void _onNext(String s) {
                        view.attentionDesigner();
                        view.changeDesigner(true);
                        EventBus.getDefault().post(new EventMessage(EventBusEvent.REFRESH_ATTENTION));
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 取消关注设计师
     * @param designerId 设计师id
     */
    @Override
    public void cancelDesigner(String designerId) {
        if (!isLogin()) return;
        List<String> idList = new ArrayList<>();
        idList.add(designerId);
        Api.getDefault().cancelAttentionDesigner(idList)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(context, true) {
                    @Override
                    public void _onNext(String s) {
                        view.cancelDesigner();
                        view.changeDesigner(false);
                        EventBus.getDefault().post(new EventMessage(EventBusEvent.REFRESH_ATTENTION));
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("数据请求失败");
                    }
                });
    }

    /**
     * 判断设计师是否被我 关注
     * @param designerId 设计师id
     */
    @Override
    public void isPayAttention(String designerId) {
        Api.getDefault().isDesignerPayAttention(designerId)
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(context, false) {
                    @Override
                    public void _onNext(Boolean aBoolean) {
                        if(aBoolean){
                            //关注设计师
                            view.cancelDesigner();
                        } else {
                            //取消关注设计师
                            view.attentionDesigner();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("数据请求失败");
                    }
                });
    }

    @Override
    public boolean isLogin() {
        if (LoginHelper.getLoginStatus()) {
            return true;
        } else {
            ToastUtil.showShort("请登录");
            return false;
        }
    }


    private static final String PLATFORM_TYPE_PLATFORM = "platform";
    //使用点类型（spu、sourceMaterial、designer、theme）
    private static final String POINT_TYPE_DESIGNER = "designer";
    @Override
    public void hasPoster() {

        String posterType = PLATFORM_TYPE_PLATFORM;
        String usePointType = POINT_TYPE_DESIGNER;


        Api.getDefault()
                .getPosterTemplate(posterType, usePointType,"")
                .compose(RxHandleResult.<List<PosterTemplateBean>>handleResult())
                .subscribe(new RxSubscriber<List<PosterTemplateBean>>(mContext,true) {
                    @Override
                    public void _onNext(List<PosterTemplateBean> posterTemplateBeans) {
                        view.openShareBoard(posterTemplateBeans != null && posterTemplateBeans.size() > 0);
                    }

                    @Override
                    public void _onError(String message) {
                        view.openShareBoard(false);
                    }
                });
    }

    /**
     * 获取打赏金额
     */
    public void getRewardPrice() {
        Api.getDefault().getAllDictionary("tipOption")
                .compose(RxHandleResult.<List<DictionaryBean>>handleResult())
                .subscribe(new RxSubscriber<List<DictionaryBean>>(mContext,true) {
                    @Override
                    public void _onNext(List<DictionaryBean> dictionaryBeen) {
                        view.showReward(dictionaryBeen);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
