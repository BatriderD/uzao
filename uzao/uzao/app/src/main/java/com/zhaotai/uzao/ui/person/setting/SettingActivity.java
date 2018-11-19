package com.zhaotai.uzao.ui.person.setting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.zhaotai.uzao.BuildConfig;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.VersionBean;
import com.zhaotai.uzao.ui.login.activity.ChangePasswordActivity;
import com.zhaotai.uzao.ui.login.activity.SetNewPasswordActivity;
import com.zhaotai.uzao.ui.person.feedback.FeedBackActivity;
import com.zhaotai.uzao.ui.person.setting.contract.SettingConstract;
import com.zhaotai.uzao.ui.person.setting.presenter.SettingPresenter;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.downLoad;
import com.zhaotai.uzao.widget.ChangeServerDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 设置页面
 */

public class SettingActivity extends BaseActivity implements SettingConstract.View {


    @BindView(R.id.logout)
    public TextView mLoginOut;

    @BindView(R.id.swh_setting_push)
    public Switch swh_push;

    @BindView(R.id.tv_setting_version)
    public TextView tv_viewsion;

    @BindView(R.id.tv_setting_cache_size)
    public TextView tv_cache_size;

    @BindView(R.id.tv_modify_password)
    public TextView tv_modify_password;


    private SettingPresenter settingPresenter;
    private int passwordStatus;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        mTitle.setText(R.string.setting);
        EventBus.getDefault().register(this);

        if (SPUtils.getSharedBooleanData(AppConfig.IS_LOGIN)) {
            mLoginOut.setVisibility(View.VISIBLE);
        } else {
            mLoginOut.setVisibility(View.GONE);
        }

        settingPresenter = new SettingPresenter(this, this);
    }

    @Override
    protected void initData() {

        //设置推送状态开关
        Boolean pushStatus = SPUtils.getSharedBooleanData(AppConfig.PUSH_SWITCH, true);
        swh_push.setChecked(pushStatus);
//        设置点击监听
        swh_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingPresenter.openPush();
                } else {
                    settingPresenter.closePush();
                }
            }
        });

//        设置版本号
        tv_viewsion.setText(getString(R.string.version_number, BuildConfig.VERSION_NAME));
        settingPresenter.getCacheSize();
        settingPresenter.setPasswordItem();


    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 意见反馈
     */
    @OnClick(R.id.tv_feed_back)
    public void feedBack() {
        FeedBackActivity.Launch(this);
    }

    /**
     * 关于我们
     */
    @OnClick(R.id.about_us)
    public void aboutUs() {
        AboutUsActivity.launch(this);
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.clean_cache)
    public void cleanCache() {
        settingPresenter.cleanCache();
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.tv_modify_password)
    public void modifyPassword() {
        switch (passwordStatus) {
            case LoginHelper.PASSWORD_STATUS_NO_PASSWORD:
                SetNewPasswordActivity.launch(this);
                break;
            case LoginHelper.PASSWORD_STATUS_HAS_PASSWORD:
                ChangePasswordActivity.launch(this);
                break;
        }

    }

    /**
     * 分享app
     */
    @OnClick(R.id.tv_share)
    public void shareApp() {
        UMImage image = new UMImage(this, R.drawable.aaa_1);//bitmap文件
        new ShareAction(this)
                .withText("uzao")
                .withMedia(image)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(shareListener).open();
    }

    /**
     * 检查新版本
     */
    @OnClick(R.id.rl_setting_check_version)
    public void checkVersion() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            new UpdateAppManager
                                    .Builder()
                                    //当前Activity
                                    .setActivity(SettingActivity.this)
                                    //实现httpManager接口的对象
                                    .setHttpManager(new downLoad())
                                    //更新地址
                                    .setUpdateUrl("321321")
                                    .build()
                                    .checkNewApp(new UpdateCallback() {
                                                     @Override
                                                     protected UpdateAppBean parseJson(String json) {
                                                         UpdateAppBean updateAppBean = new UpdateAppBean();
                                                         Gson gson = new Gson();
                                                         boolean constraint;//是否强制更新
                                                         String isUpdate;
                                                         VersionBean versionBean = gson.fromJson(json, VersionBean.class);
                                                         if (versionBean.vnumber.equals(BuildConfig.VERSION_NAME)) {
                                                             isUpdate = "No";
                                                         } else {
                                                             isUpdate = "Yes";
                                                         }
                                                         constraint = versionBean.vupload.equals("Y");

                                                         updateAppBean
                                                                 //（必须）是否更新Yes,No
                                                                 .setUpdate(isUpdate)
                                                                 //（必须）新版本号，
                                                                 .setNewVersion(versionBean.vnumber)
                                                                 //（必须）下载地址
                                                                 .setApkFileUrl(versionBean.vaddress)
                                                                 //（必须）更新内容
                                                                 .setUpdateLog(versionBean.vdescription)
                                                                 //是否强制更新，可以不设置
                                                                 .setConstraint(constraint);
                                                         return updateAppBean;
                                                     }

                                                     @Override
                                                     protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                                                         updateAppManager.showDialogFragment();
                                                     }

                                                     /**
                                                      * 网络请求之前
                                                      */
                                                     @Override
                                                     protected void onBefore() {
                                                     }

                                                     @Override
                                                     protected void onAfter() {
                                                     }

                                                     @Override
                                                     protected void noNewApp() {
                                                         Toast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                    );
                        } else {
                            ToastUtil.showShort("打开存储权限才能更新");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 切换服务器
     */
    @OnClick(R.id.tv_setting_server)
    public void onClickChangeServer() {
        ChangeServerDialog.create().show(getSupportFragmentManager(), "dialog");
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.logout)
    public void logout() {
        LoginHelper.exitLogin(this);
    }

    @Override
    public void showCacheSize(String size) {
        tv_cache_size.setText(size);
    }

    @Override
    public void setPasswordText(int passwordStatus) {
        this.passwordStatus = passwordStatus;
        switch (passwordStatus) {
            case LoginHelper.PASSWORD_STATUS_ANONYMOUS:
                tv_modify_password.setVisibility(View.GONE);
                break;
            case LoginHelper.PASSWORD_STATUS_NO_PASSWORD:
                tv_modify_password.setVisibility(View.VISIBLE);
                tv_modify_password.setText(getString(R.string.set_new_password));
                break;
            case LoginHelper.PASSWORD_STATUS_HAS_PASSWORD:
                tv_modify_password.setVisibility(View.VISIBLE);
                tv_modify_password.setText(getString(R.string.modify_password));
                break;
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         *  SHARE_MEDIA 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         *  分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(SettingActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         *  分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SettingActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         *  分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SettingActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PersonInfo info) {
        switch (info.code) {

            //退出登录
            case EventBusEvent.LOG_OUT:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
