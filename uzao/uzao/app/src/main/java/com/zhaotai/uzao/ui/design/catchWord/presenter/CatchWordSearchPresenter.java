package com.zhaotai.uzao.ui.design.catchWord.presenter;

import android.content.Context;

import com.zhaotai.uzao.ui.design.catchWord.contract.CatchWordSearchContract;

/**
 * Time: 2017/11/30
 * Created by LiYou
 * Description :
 */

public class CatchWordSearchPresenter extends CatchWordSearchContract.Presenter {

    private CatchWordSearchContract.View mView;

    public CatchWordSearchPresenter(CatchWordSearchContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 流行词关键词 搜索关键词
     *
     * @param KeyWord
     */
    @Override
    public void searchCatchWord(String KeyWord) {

    }
}
