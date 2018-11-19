package com.zhaotai.uzao.ui.shopping.contract;

import com.zhaotai.uzao.base.BasePresenter;
import com.zhaotai.uzao.base.BaseView;
import com.zhaotai.uzao.ui.shopping.adapter.MultiCartItem;

import java.util.List;

/**
 * Time: 2017/5/25
 * Created by LiYou
 * Description :购物车契约类
 */

public interface ShoppingCartContract {

    interface View extends BaseView {

        void showCartData(List<MultiCartItem> data);

        void stopRefresh();

        void showShoppingContentView();

        void showLoginView();

        void showUpdatePrice(String countPrice, String finalPrice, String cutPrice);

        void showSelectAll(boolean isSelectAll);

        void notifyItem(int position);

        void notifyDataChanged();

        void showBottomAccount(boolean isVisible);

        void setModifyBtnVisible(boolean isVisible);

        void setModifyBtnReset();
    }

    abstract class Presenter extends BasePresenter {
        //获取购物车列表
        public abstract void getCartList(boolean isLoading);
    }

}
