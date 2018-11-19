package com.zhaotai.uzao.app;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.kf5.sdk.system.init.KF5SDKInitializer;
import com.kf5.sdk.system.utils.SPUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhaotai.uzao.BuildConfig;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.view.RefreshHeaderView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import me.jessyan.autosize.AutoSizeConfig;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import okhttp3.OkHttpClient;

/**
 * Time: 2017/5/8
 * Created by LiYou
 * Description :
 */

public class MyApplication extends MultiDexApplication {

    private static MyApplication myApplication;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new RefreshHeaderView(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //初始化logUtils
        LogUtils.logInit(true);
        //初始化异常捕获
//        initCrash();
        initBugly();
        //初始化glide
        initGlide();
        //初始化okhttp
        initOkHttp();
        //初始化推送
        initPush();
        //初始化分享
        initShare();
        //客服
        initCustomerService();
        //处理三星内存泄露
        delSanSungEx();
        //适配
        initAutoSize();
        initFragmentation();
        initGrowing();
    }

    /**
     * 适配
     */
    private void initAutoSize() {
        AutoSizeConfig.getInstance()
                .setCustomFragment(false)
                .setLog(false);
        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
        //AutoSize 会将屏幕总高度减去状态栏高度来做适配, 如果设备上有导航栏还会减去导航栏的高度
        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏以及导航栏高度
        //.setUseDeviceSize(false)
        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
        //.setBaseOnWidth(true);
    }

    private void initBugly() {
        Log.d("MYAPPLICATION", "initBugly: " + BuildConfig.BUILD_TYPE);
        if (BuildConfig.BUILD_TYPE.equals("release")) {
            CrashReport.initCrashReport(getApplicationContext(), "84fd547767", false);
        }
    }

    private void initGrowing() {
//        GrowingIO.startWithConfiguration(this, new Configuration()
//                .useID()
//                .trackAllFragments()
//                .setChannel("XXX应用商店")
//        );

    }

    /**
     * 初始化Fragmentation
     */
    private void initFragmentation() {
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(com.zhaotai.uzao.BuildConfig.DEBUG)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(@NonNull Exception e) {
                        LogUtils.logd("Fragmentation===" + e.getMessage());
                    }
                }).install();

    }

    //处理三星剪切板的内存泄露问题
    private void delSanSungEx() {
        try {
            Class cls = Class.forName("android.sec.clipboard.ClipboardUIManager");
            Method m = cls.getDeclaredMethod("getInstance", Context.class);
            m.setAccessible(true);
            m.invoke(null, this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        HttpHeaders headers = new HttpHeaders();
        headers.put("product", ApiConstants.PRODUCT);    //header不支持中文，不允许有特殊字符
        headers.put("Content-Type", "application/json;charset=utf-8");
        OkGo.getInstance()
                .setOkHttpClient(builder.build()) //必须调用初始化
                .addCommonHeaders(headers)
                .init(this);
    }

    private void initGlide() {
//        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient.Builder()
//                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
//                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
//                .build()));
    }

    private void initCustomerService() {
        KF5SDKInitializer.init(this);
        SPUtils.saveHelpAddress("soutetu.kf5.com");
        SPUtils.saveAppID("0015993c121ce2efc4ebe5c192cbbb4a14dd1446b880e5b6");
    }

    private void initPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.logd("DeviceToken_onSuccess: " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.logd("DeviceToken_onFailure: " + s + "///" + s1);
            }
        });
    }

    private void initCrash() {
        // 异常处理，不需要处理时注释掉这两句即可！
        AppCrashHandler crashHandler = AppCrashHandler.shareInstance(this);
    }

    //在这里初始化分享和三方登录
    private void initShare() {
        PlatformConfig.setWeixin("wx60ae856ef59779f4", "265f7c8ffe61f0910b9a264ed3b0e787");
        PlatformConfig.setQQZone("101411557", "d0a76d7dbcfcdcb4bb1505a159268eb3");
        PlatformConfig.setSinaWeibo("1106922893", "672d18b944eff53814f2ff5fbefdf9e0", "http://sns.whalecloud.com");
        UMShareAPI.get(this);
    }

    //读超时长，单位：毫秒
    private static final int READ_TIME_OUT = 7676;
    //连接时长，单位：毫秒
    private static final int CONNECT_TIME_OUT = 7676;

    public static Context getAppContext() {
        return myApplication;
    }

    public static Resources getAppResources() {
        return myApplication.getResources();
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
