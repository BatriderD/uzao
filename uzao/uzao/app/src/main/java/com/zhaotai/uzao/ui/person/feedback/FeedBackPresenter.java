package com.zhaotai.uzao.ui.person.feedback;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.RequestFeedBackBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

/**
 * Time: 2017/7/19
 * Created by LiYou
 * Description :
 */

public class FeedBackPresenter extends FeedBackContract.Presenter {

    private FeedBackContract.View view;
    private RequestFeedBackBean feedBackBean;

    public FeedBackPresenter(FeedBackContract.View view, Context context) {
        this.view = view;
        mContext = context;
    }

    /**
     * 检查并且提交反馈
     *
     * @param title
     * @param content
     * @param contact
     * @param email
     */
    public void checkAndPostFeedBack(String title, String content, String contact, String email) {
        if (StringUtil.isEmpty(title)) {
            ToastUtil.showShort("标题不能为空");
            return;
        }
        if (StringUtil.isEmpty(content)) {
            ToastUtil.showShort("反馈内容不能为空");
            return;
        }

        if (!StringUtil.checkPhoneNumber(contact)) {
            ToastUtil.showShort("手机号码格式不对");
            return;
        }

        if (!StringUtil.isEmpty(email) && !StringUtil.isEmail(email)) {
            ToastUtil.showShort("邮箱格式不正确");
            return;
        }
        
        
        feedBackBean = new RequestFeedBackBean();
        feedBackBean.title = title;
        feedBackBean.contact = contact;
        feedBackBean.content = content;
        feedBackBean.email = email;
        Api.getDefault().postFeedBack(feedBackBean)
                .compose(RxHandleResult.<RequestFeedBackBean>handleResult())
                .subscribe(new RxSubscriber<RequestFeedBackBean>(mContext, true) {
                    @Override
                    public void _onNext(RequestFeedBackBean s) {
                        ToastUtil.showShort("提交成功");
                        view.showSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("提交失败");
                    }
                });
    }
}
