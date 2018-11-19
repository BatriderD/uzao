package com.zhaotai.uzao.bsaerx;


import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.bean.BaseResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * time:2017/4/12
 * description: 对返回数据预处理
 * author: LiYou
 */

public class RxHandleResult {
    /**
     * Rx处理服务器返回
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseResult<T>, T> handleResult() {
        return new ObservableTransformer<BaseResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseResult<T>> upstream) {
                return upstream.flatMap(new Function<BaseResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull BaseResult<T> tBaseResult) throws Exception {
                        if(tBaseResult.getCode().equals("OK")){
                         return createData(tBaseResult.getResult());
                        }else{
                            if (isTokenException(tBaseResult.getMessage())) {
                                return Observable.error(new TokenException(tBaseResult.getMessage()));
                            }
                            return Observable.error(new Exception(tBaseResult.getMessage()));
                        }
                    }
                }).subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<BaseResult<T>, T> handleResultMap() {
        return new ObservableTransformer<BaseResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseResult<T>> upstream) {
                return upstream.flatMap(new Function<BaseResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull BaseResult<T> tBaseResult) throws Exception {
                        if(tBaseResult.getCode().equals("OK")){
                            return createData(tBaseResult.getResult());
                        }else{
                            if (isTokenException(tBaseResult.getMessage())) {
                                return Observable.error(new TokenException(tBaseResult.getMessage()));
                            }
                            return Observable.error(new Exception(tBaseResult.getMessage()));
                        }
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<BaseResult<T>, T> handleResult(final BasePresenter basePresenter) {
        return new ObservableTransformer<BaseResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<BaseResult<T>> upstream) {
                return upstream.flatMap(new Function<BaseResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@NonNull BaseResult<T> tBaseResult) throws Exception {
                        if(tBaseResult.getCode().equals("OK")){
                            return createData(tBaseResult.getResult());
                        }else{
                            if (isTokenException(tBaseResult.getMessage())) {
                                return Observable.error(new TokenException(tBaseResult.getMessage()));
                            }
                            return Observable.error(new Exception(tBaseResult.getMessage()));
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                basePresenter.add(disposable);
                            }
                        });
            }
        };
    }

    private static  <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(t);
                    e.onComplete();
                }catch (Exception ex){
                    e.onError(ex);
                }
            }
        });
    }

    private static Observable<String> createData(final String t) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                try {
                    e.onNext(t);
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });
    }

    public static boolean isTokenException(String message) {
        return message.contains("TOKEN_NOT_FOUND") || message.equals("TOKEN_EXPIRED--token已过期");
    }

}
