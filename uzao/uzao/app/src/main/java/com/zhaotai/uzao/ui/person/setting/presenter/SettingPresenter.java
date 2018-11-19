package com.zhaotai.uzao.ui.person.setting.presenter;

import android.content.Context;

import com.umeng.message.IUmengCallback;
import com.umeng.message.PushAgent;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.app.MyApplication;
import com.zhaotai.uzao.ui.person.setting.contract.SettingConstract;
import com.zhaotai.uzao.utils.DataCleanManager;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.SPUtils;

/**
 * description:  设置页面Presenter
 * author : zp
 * date: 2017/7/20
 */

public class SettingPresenter extends SettingConstract.Presenter {
    public SettingConstract.View mView;

    public SettingPresenter(Context context, SettingConstract.View view) {
        mView = view;
        mContext = context;
    }

    @Override
    public void openPush() {
        // 设置推送本地存储值
        SPUtils.setSharedBooleanData(AppConfig.PUSH_SWITCH, true);
        PushAgent mPushAgent = PushAgent.getInstance(mContext);
        mPushAgent.enable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                LogUtils.logd("开启推送");
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.logd("开启推送 失败");
            }
        });
    }

    @Override
    public void closePush() {
        // 设置推送本地存储值
        SPUtils.setSharedBooleanData(AppConfig.PUSH_SWITCH, false);
        PushAgent mPushAgent = PushAgent.getInstance(mContext);
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                LogUtils.logd("关闭推送");
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.logd("关闭推送失败");
            }
        });

    }

    /**
     * 获取当前缓存大小
     */
    @Override
    public void getCacheSize() {
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(MyApplication.getAppContext());
            mView.showCacheSize(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除缓存
     */
    @Override
    public void cleanCache() {
        DataCleanManager.clearAllCache(MyApplication.getAppContext());
        getCacheSize();
    }

    /**
     * 根据网络状态设置对应修改密码区域文字
     */
    @Override
    public void setPasswordItem() {
            int passwordStatus = LoginHelper.getPasswordStatus();
            mView.setPasswordText(passwordStatus);
    }


}
