package com.zhaotai.uzao.base;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * time:2017/5/4
 * description:
 * author: LiYou
 */

public abstract class BasePresenter {

    public Context mContext;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onStart(){}

    public void add(Disposable d){
        compositeDisposable.add(d);
    }

    public void onDestory(){
        compositeDisposable.clear();
    }

}
