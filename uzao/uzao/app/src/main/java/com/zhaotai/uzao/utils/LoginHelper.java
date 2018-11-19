package com.zhaotai.uzao.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.app.MyApplication;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.TokenBean;
import com.zhaotai.uzao.bean.TokenInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.activity.LoginMsgActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * description: 登录帮助类
 * author : zp
 * date: 2017/7/20
 */

public class LoginHelper {
    //     *  0 匿名登录
//     *  1 没有密码
//     *  2 有密码
    public static final int PASSWORD_STATUS_ANONYMOUS = 0;
    public static final int PASSWORD_STATUS_NO_PASSWORD = 1;
    public static final int PASSWORD_STATUS_HAS_PASSWORD = 2;

    /**
     * 存token
     *
     * @param token token
     */
    public static void setToken(String token) {
        //保存token
        SPUtils.setSharedStringData(AppConfig.USER_TOKEN, token);
    }

    /**
     * 取token
     *
     * @return 取token
     */
    public static String getToken() {
        //保存token
        return SPUtils.getSharedStringData(AppConfig.USER_TOKEN);
    }

    /**
     * 存用户名
     *
     * @param userId 用户名
     */
    public static void setUserId(String userId) {
        //保存userId
        SPUtils.setSharedStringData(AppConfig.USER_ID, userId);
    }

    /**
     * 取用户名
     *
     * @return 取userId
     */
    public static String getUserId() {
        //保存token
        return SPUtils.getSharedStringData(AppConfig.USER_ID);
    }

    /**
     * 取昵称
     */
    public static String getUserName() {
        return SPUtils.getSharedStringData(AppConfig.USER_NAME);
    }

    /**
     * 存昵称
     */
    public static void setUserName(String userName) {
        SPUtils.setSharedStringData(AppConfig.USER_NAME, userName);
    }

    /**
     * 作品数量
     */
    public static void setUserWorkNum(String workNum) {
        SPUtils.setSharedStringData(AppConfig.USER_WORK, workNum);
    }

    /**
     * 获取作品数量
     */
    public static String getUserWorkNum() {
        return SPUtils.getSharedStringData(AppConfig.USER_WORK);
    }


    /**
     * 存电话
     */
    public static void setLoginId(String loginId) {
        //保存电话
        SPUtils.setSharedStringData(AppConfig.USER_PHONE, loginId);
    }

    /**
     * 取电话
     */
    public static String getLoginId() {
        //保存token
        return SPUtils.getSharedStringData(AppConfig.USER_PHONE);
    }
    /**
     * 存头像
     */
    public static void setAvatar(String loginId) {
        //保存电话
        SPUtils.setSharedStringData(AppConfig.USER_AVATAR, loginId);
    }

    /**
     * 取头像
     */
    public static String getAvatar() {
        //保存token
        return SPUtils.getSharedStringData(AppConfig.USER_AVATAR);
    }

    /**
     * 已登录状态
     */
    public static void setLoginStatus() {
        SPUtils.setSharedBooleanData(AppConfig.IS_LOGIN, true);
    }

    /**
     * 退出登录状态
     */
    public static void quiteLoginStatus() {
        SPUtils.setSharedBooleanData(AppConfig.IS_LOGIN, false);
    }

    /**
     * 获取当前登录状态
     */
    public static boolean getLoginStatus() {
        return SPUtils.getSharedBooleanData(AppConfig.IS_LOGIN);
    }


    public static void exitLogin(AppCompatActivity context) {

        //获取匿名token
            Api.getDefault().anonymousRxLogin(new TokenBean())
                    .compose(RxHandleResult.<TokenInfo>handleResult())
                    .subscribe(new RxSubscriber<TokenInfo>(context, true) {
                        @Override
                        public void _onNext(TokenInfo tokenInfo) {
                            //退出登录状态
                            quiteLoginStatus();
                            //设置密码状态
                            setPasswordStatus(PASSWORD_STATUS_ANONYMOUS);
                            //更换匿名token
                            setToken(tokenInfo.token);
                            //清空userid
                            setUserId("");
                            // 重置api
                            Api.resetManager(tokenInfo.token);

                            //发送更新匿名登录ui信息
                            exitLoginUiChange();
                            //提示退出登陆成功
                            ToastUtil.showShort(MyApplication.getAppContext().getString(R.string.exit_login_success));
                        }

                        @Override
                        public void _onError(String message) {
                            //退出登录状态
                            quiteLoginStatus();
                            //设置密码状态
                            setPasswordStatus(PASSWORD_STATUS_ANONYMOUS);
                            //清空userid
                            setUserId("");
                            //发送更新匿名登录ui信息
                            exitLoginUiChange();
                            //提示退出登陆成功
                            ToastUtil.showShort(MyApplication.getAppContext().getString(R.string.exit_login_success));
                        }
                    });

    }

    /**
     * 获取匿名token
     */
    public static void getAnonymous(Context context) {
        //获取匿名token
        Api.getDefault().anonymousRxLogin(new TokenBean())
                .compose(RxHandleResult.<TokenInfo>handleResult())
                .subscribe(new RxSubscriber<TokenInfo>(context) {
                    @Override
                    public void _onNext(TokenInfo tokenInfo) {
                        //退出登录状态
                        quiteLoginStatus();
                        //清空token
                        setToken(tokenInfo.token);
                        //清空userid
                        setUserId("");
                        //设置密码状态
                        setPasswordStatus(PASSWORD_STATUS_ANONYMOUS);
                        // 重置api
                        Api.resetManager(tokenInfo.token);
                        //发送更新匿名登录ui信息
                        exitLoginUiChange();
                    }

                    @Override
                    public void _onError(String message) {
                        System.out.println(message);
                    }
                });
    }

    /**
     * 获取匿名token
     */
    public static void getAnonymous(AppCompatActivity context, final loginListener listener) {
        //获取匿名token
        Api.getDefault().anonymousRxLogin(new TokenBean())
                .compose(RxHandleResult.<TokenInfo>handleResult())
                .subscribe(new RxSubscriber<TokenInfo>(context) {
                    @Override
                    public void _onNext(TokenInfo tokenInfo) {
                        //退出登录状态
                        quiteLoginStatus();
                        //清空token
                        setToken(tokenInfo.token);
                        //清空userid
                        setUserId("");
                        //设置密码状态
                        setPasswordStatus(PASSWORD_STATUS_ANONYMOUS);
                        // 重置api
                        Api.resetManager(tokenInfo.token);
                        //发送更新匿名登录ui信息
                        exitLoginUiChange();

                        listener.loginSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        listener.loginError();
                    }
                });
    }

    /**
     * 设置登录
     *
     * @param userId  userid
     * @param token   toekn
     * @param loginId loginId
     */
    public static void setLoginInfo(String userId, String token, String loginId) {
        setLoginId(loginId);
        //已登录状态
        setLoginStatus();
        //设置token
        setToken(token);
        //设置userid
        setUserId(userId);
//        重置api
        Api.resetManager(token);
        //保存图片
        saveUnloadingPic();

        //登录成功界面变化
        LoginSuccessUiChange();
    }

    public static void goLogin(Context mActivity) {
        LoginMsgActivity.launch(mActivity);
    }

    /**
     * 登录成功
     */
    public interface loginListener {
        void loginSuccess();

        void loginError();
    }

    /**
     * 设置用户密码状态
     * 0 匿名登录
     * 1 没有密码
     * 2 有密码
     *
     * @param passwordStatus 用户密码状态
     */
    public static void setPasswordStatus(int passwordStatus) {
        SPUtils.setSharedIntData(AppConfig.PASSWORD_STATUS, passwordStatus);
    }

    /**
     * 获得用户密码状态
     */
    public static int getPasswordStatus() {
        return SPUtils.getSharedIntData(AppConfig.PASSWORD_STATUS);
    }

    /**
     * 发送退出登录界面变换请求
     */
    public static void exitLoginUiChange() {
        // 发送退出登录信息
        PersonInfo info = new PersonInfo();
        info.code = EventBusEvent.LOG_OUT;
        EventBus.getDefault().post(info);
    }

    /**
     * 发送登录界面变换请求
     */
    public static void LoginSuccessUiChange() {
        //发送登录成功请求
        PersonInfo info = new PersonInfo();
        info.code = EventBusEvent.REQUEST_PERSON_INFO;
        EventBus.getDefault().post(info);
    }


    public static void saveUnloadingPic() {
        String sharedStringData = SPUtils.getSharedStringData(GlobalVariable.UNLOADDESIGNPICS);
        String[] split = sharedStringData.split(",");
        Api.getDefault().upCookiePics(split)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("数据持久化失败" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
