package com.zhaotai.uzao.ui.design.presenter;

import android.content.Context;

import com.zhaotai.uzao.ui.design.contract.StrengthenContract;

/**
 * description:  增强的presenter
 * author : ZP
 * date: 2017/9/28 0028.
 */

public class StrengthenPresenter extends StrengthenContract.Presenter {
    private final String pathName = "white";
    private StrengthenContract.View mView;

    public StrengthenPresenter(Context context, StrengthenContract.View view) {
        mContext = context;
        mView = view;
    }

}
