package com.zhaotai.uzao.ui.search.presenter;

import android.content.Context;

import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description :
 */

public abstract class SimpleBaseSearchPresenter extends SimpleBaseSearchContract.Presenter {

    protected SimpleBaseSearchContract.View mView;


    public SimpleBaseSearchPresenter(SimpleBaseSearchContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

}
