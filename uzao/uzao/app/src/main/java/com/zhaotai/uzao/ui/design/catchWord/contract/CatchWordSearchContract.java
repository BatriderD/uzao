package com.zhaotai.uzao.ui.design.catchWord.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseSwipeView;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description :
 */

public interface CatchWordSearchContract {

    interface View extends BaseSwipeView {

    }

    abstract class Presenter extends BasePresenter {

        public abstract void searchCatchWord(String KeyWord);
    }

}
