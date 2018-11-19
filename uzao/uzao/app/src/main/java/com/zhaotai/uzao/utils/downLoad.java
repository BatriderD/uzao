package com.zhaotai.uzao.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.vector.update_app.HttpManager;
import com.zhaotai.uzao.BuildConfig;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.VersionBean;
import com.zhaotai.uzao.global.GlobalVariable;

import java.io.File;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Time: 2017/8/8
 * Created by LiYou
 * Description :  更新app
 */

public class downLoad implements HttpManager {


    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        Api.getDefault().getNewVersion(BuildConfig.VERSION_NAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<VersionBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult<VersionBean> versionBeanBaseResult) {
                        Gson gson = new Gson();
                        String version = gson.toJson(versionBeanBaseResult.getResult());
                        if (versionBeanBaseResult.getStatus() == 200) {
                            callBack.onResponse(version);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.onError("异常");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {

    }

    @Override
    public void download(@NonNull final String url, @NonNull final String path, @NonNull final String fileName, @NonNull final FileCallback callback) {
        String baseImageUrl = SPUtils.getSharedStringData(GlobalVariable.BASE_IMAGE_URL);
        OkGo.<File>get(baseImageUrl + url).execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
                super.onStart(request);
                callback.onBefore();
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<File> response) {
                super.onError(response);
                callback.onError("异常");
            }

            @Override
            public void downloadProgress(Progress progress) {
                super.downloadProgress(progress);

                callback.onProgress(progress.fraction, progress.totalSize);
            }
        });
    }
}
