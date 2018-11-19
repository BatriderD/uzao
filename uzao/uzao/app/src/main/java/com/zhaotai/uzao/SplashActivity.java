package com.zhaotai.uzao;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.message.PushAgent;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.base.BaseTopActivity;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.jessyan.autosize.internal.CancelAdapt;


/**
 * Time: 2017/6/23
 * Created by LiYou
 * Description : 欢迎界面
 */

public class SplashActivity extends BaseTopActivity implements CancelAdapt {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        PushAgent.getInstance(this).onAppStart();
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTransparent(this);
        setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        } else if (getActionBar() != null) {
            getActionBar().hide();
        }
        String baseUrl = SPUtils.getSharedStringData(GlobalVariable.BASE_URL);
        String baseImageUrl = SPUtils.getSharedStringData(GlobalVariable.BASE_IMAGE_URL);
        String baseDesignUrl = SPUtils.getSharedStringData(GlobalVariable.BASE_DESIGN_URL);
        if (!StringUtil.isEmpty(baseUrl)) {
            ApiConstants.UZAOCHINA_HOST = baseUrl;
        }
        if (!StringUtil.isEmpty(baseImageUrl)) {
            ApiConstants.UZAOCHINA_IMAGE_HOST = baseImageUrl;
        }
        if (!StringUtil.isEmpty(baseDesignUrl)) {
            ApiConstants.UZAOCHINA_IMAGE_HOST_DESIGN = baseDesignUrl;
        }

        //1.解决安装后直接打开，home键切换到后台再启动重复出现闪屏页面的问题
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                }
            }
        }
        ImageView iv_bg = (ImageView) findViewById(R.id.iv_splash_bg);
        Glide.with(this).load(R.mipmap.ic_start).into(iv_bg);

        Disposable disposable = new RxPermissions(SplashActivity.this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .flatMap(new Function<Boolean, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            return Observable.timer(GlobalVariable.SPLASH_COUNT, TimeUnit.SECONDS);
                        }
                        ToastUtil.showShort("打开存储全选才能进入app");
                        return null;
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        HomeActivity.launch(SplashActivity.this);
                        finish();
                    }
                });
        add(disposable);

        initData();
    }

    private void initData() {
        initLogin();
    }

    /**
     * 初始化登录信息,是否需要匿名token
     */
    private void initLogin() {
        if (!SPUtils.getSharedBooleanData(AppConfig.IS_LOGIN)) {
            LoginHelper.getAnonymous(this, new LoginHelper.loginListener() {
                @Override
                public void loginSuccess() {

                }

                @Override
                public void loginError() {

                }
            });
        }
    }
}
