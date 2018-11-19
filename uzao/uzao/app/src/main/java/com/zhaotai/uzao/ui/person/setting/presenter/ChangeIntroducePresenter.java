package com.zhaotai.uzao.ui.person.setting.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.setting.contract.ChangeIntroduceConstract;
import com.zhaotai.uzao.utils.ToastUtil;

/**
 * description: 修改个人信息presenter
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class ChangeIntroducePresenter extends ChangeIntroduceConstract.Presenter {
    private ChangeIntroduceConstract.View mView;

    public ChangeIntroducePresenter(Context context, ChangeIntroduceConstract.View view) {
        mContext = context;
        this.mView = view;
    }


    @Override
    public void setIntroduce(String introduce) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.aboutMe = introduce;
        Api.getDefault().changePersonInfo(personInfo)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(mContext, true) {
                    @Override
                    public void _onNext(PersonBean personBean) {
                        System.out.println("我修改成功" + personBean.aboutMe);
                        mView.showChangeIntroduceSuccess(personBean.aboutMe);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("修改失败");
                    }
                });
    }
}
