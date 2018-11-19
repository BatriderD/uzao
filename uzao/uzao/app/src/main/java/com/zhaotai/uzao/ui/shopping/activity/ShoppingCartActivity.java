package com.zhaotai.uzao.ui.shopping.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.ui.shopping.fragment.ShoppingCartFragment;

/**
 * Time: 2017/8/2
 * Created by LiYou
 * Description : 购物车界面
 */

public class ShoppingCartActivity extends BaseFragmentActivity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, ShoppingCartActivity.class));
    }

    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_shopping_cart);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_shopping_cart, ShoppingCartFragment.newInstance(ShoppingCartFragment.FROM_DETAIL));
        ft.commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

}
