package com.zhaotai.uzao.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.app.MyApplication;
import com.zhaotai.uzao.utils.SPUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * time:2017/4/7
 * description:
 * author: LiYou
 */

public class ApiTT {
    //读超时长，单位：毫秒
    private static final int READ_TIME_OUT = 7000;
    //连接时长，单位：毫秒
    private static final int CONNECT_TIME_OUT = 7000;
    private Retrofit retrofit;
    private ApiService apiService;
    private static ApiTT retrofitManager;

    /*************************缓存设置*********************/
    /*
   1. noCache 不使用缓存，全部走网络

    2. noStore 不使用缓存，也不存储缓存

    3. onlyIfCached 只使用缓存

    4. maxAge 设置最大失效时间，失效则不使用 需要服务器配合

    5. maxStale 设置最大失效时间，失效则不使用 需要服务器配合 感觉这两个类似 还没怎么弄清楚，清楚的同学欢迎留言

    6. minFresh 设置有效时间，依旧如上

    7. FORCE_NETWORK 只走网络

    8. FORCE_CACHE 只走缓存*/

    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";

    //构造方法私有
    private ApiTT() {
        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
        File cacheFile = new File(MyApplication.getAppContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100MB

        //添加请求头
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String tokenData = SPUtils.getSharedStringData(AppConfig.USER_TOKEN);
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("product", ApiConstants.PRODUCT)
                        .addHeader("token", tokenData)
                        .build();
                return chain.proceed(build);
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                .cache(cache)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();//使用 gson coverter，统一日期请求格式

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    //乔乔
    private static final String url = "http://192.168.2.192:8080";
    //马腾
//    private static final String url  = "http://192.168.2.155:8080";
    //郑婷
//    private static final String url  = "http://192.168.2.129:8085";
    //曲坤
//    private static final String url  = "http://192.168.2.233:8080";
    //高原疼
//    private static final String url  = "http://192.168.2.233:8080";
    //老宋
//    private static final String url  = "http://192.168.2.250:8080";

    /**
     * 重载函数 baseurl 是 uzaochina
     */
    public static ApiService getDefault() {
        if (retrofitManager == null) {
            retrofitManager = new ApiTT();
        }
        return retrofitManager.apiService;
    }

}
