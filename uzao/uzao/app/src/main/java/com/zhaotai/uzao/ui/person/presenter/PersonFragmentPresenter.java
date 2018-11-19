package com.zhaotai.uzao.ui.person.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.EventBean.UnReadMessageEvent;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.person.contract.PersonFragmentContract;
import com.zhaotai.uzao.utils.ACache;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * description: 个人主页Presenter
 * author : zp
 * date: 2017/7/14
 */

public class PersonFragmentPresenter extends PersonFragmentContract.Presenter {
    PersonFragmentContract.View mView;

    public PersonFragmentPresenter(Context context, PersonFragmentContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getLoginStage() {
//        根据用户登录状态进行相应操作
        if (LoginHelper.getLoginStatus()) {
//            登录了
//            获取缓存的个人信息并显示
            PersonBean personInfo = (PersonBean) ACache.get(mContext).getAsObject(GlobalVariable.PERSONINFO);
            if (personInfo != null) {
                mView.showPersonInfo(personInfo);
            }
//            更新新用户信息 并显示
            getPersonInfo();
            getUnusedDiscountList();
            getUnHandleMessage();
            getGuessYouLike();
        } else {
            LoginHelper.setPasswordStatus(LoginHelper.PASSWORD_STATUS_ANONYMOUS);
            mView.showUnLogin();
        }
    }

    @Override
    public void getPersonInfo() {
        Api.getDefault().getPersonMobileInfo()
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(mContext, false) {
                    @Override
                    public void _onNext(PersonBean personBean) {
                        String isPasswordAuthed = personBean.isPasswordAuthed;
                        if ("Y".equals(isPasswordAuthed)) {
                            LoginHelper.setPasswordStatus(LoginHelper.PASSWORD_STATUS_HAS_PASSWORD);
                        } else {
                            LoginHelper.setPasswordStatus(LoginHelper.PASSWORD_STATUS_NO_PASSWORD);
                        }
                        //缓存bean数据
                        ACache.get(mContext).put(GlobalVariable.PERSONINFO, personBean);
                        LoginHelper.setUserName(personBean.nickName);
                        LoginHelper.setAvatar(personBean.avatar);
                        mView.showPersonInfo(personBean);
                        if (StringUtil.isEmpty(personBean.avatar)) {
                            upLoadPic();
                        }
                        //保存作品数
                        LoginHelper.setUserWorkNum(personBean.myDesignCount);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("网络错误，个人信息刷新失败");
                    }
                });
    }

    //上传默认头像
    private void upLoadPic() {
        PersonInfo info = new PersonInfo();
        info.avatar = GlobalVariable.DEFAULT_HEAD_PIC;
        Api.getDefault().changePersonInfo(info)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(mContext) {
                    @Override
                    public void _onNext(PersonBean presonBean) {
//                            缓存个人信息
                        ACache.get(mContext).put(GlobalVariable.PERSONINFO, presonBean);
                        SPUtils.setSharedStringData(AppConfig.USER_IAMGE, ApiConstants.UZAOCHINA_IMAGE_HOST + GlobalVariable.DEFAULT_HEAD_PIC);
                        PersonInfo info = new PersonInfo();
                        info.code = EventBusEvent.CHANGE_HEAD_IMAGE;
                        info.avatar = GlobalVariable.DEFAULT_HEAD_PIC;
                        //通知我的页面 头像更新
                        EventBus.getDefault().post(info);

                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("上传失败");
                    }
                });
    }


    @Override
    public void getUnHandleMessage() {
        Api.getDefault().getUnReadCount()
                .compose(RxHandleResult.<Integer>handleResult())
                .subscribe(new RxSubscriber<Integer>(mContext, false) {
                    @Override
                    public void _onNext(Integer integer) {
                        EventBus.getDefault().post(new UnReadMessageEvent(integer));
                        mView.showUnHandleMessage(integer);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    public void getUnusedDiscountList() {
        Api.getDefault().getDiscountList("N", 0)
                .compose(RxHandleResult.<PageInfo<DiscountCouponBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<DiscountCouponBean>>(mContext, false) {
                    @Override
                    public void _onNext(PageInfo<DiscountCouponBean> discountCouponBean) {

                        mView.showDiscountSize(discountCouponBean.totalRows);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 猜你喜欢
     */
    public void getGuessYouLike() {
        Api.getDefault().getRecommendLike()
                .compose(RxHandleResult.<List<GoodsBean>>handleResult())
                .subscribe(new RxSubscriber<List<GoodsBean>>(mContext) {
                    @Override
                    public void _onNext(List<GoodsBean> goodsBeen) {
                        mView.showRecommend(goodsBeen);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
