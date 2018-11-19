package com.zhaotai.uzao.ui.designer.contract;

import android.content.Context;

import com.zhaotai.uzao.ui.search.contract.SimpleBaseSearchContract;
import com.zhaotai.uzao.ui.search.presenter.SimpleBaseSearchPresenter;

/**
 * Time: 2017/12/1
 * Created by zp
 * Description :  足迹管理类
 */

public interface NewDesignerListSearchContract {

    interface View extends SimpleBaseSearchContract.View {

        void changeDesigner(int pos, boolean b);
    }

    abstract class Presenter extends SimpleBaseSearchPresenter {

        public Presenter(SimpleBaseSearchContract.View view, Context context) {
            super(view, context);
        }

        public abstract void attentionDesigner(int pos, String id);

        public abstract void cancelDesigner(int pos, String id);

    }
}
