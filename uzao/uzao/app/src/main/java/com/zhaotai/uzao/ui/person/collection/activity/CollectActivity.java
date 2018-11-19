package com.zhaotai.uzao.ui.person.collection.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.ui.person.collection.OnChangeFragmentInterface;
import com.zhaotai.uzao.ui.person.collection.fragment.CollectionMaterialFragment;
import com.zhaotai.uzao.ui.person.collection.fragment.CollectionProductFragment;
import com.zhaotai.uzao.ui.person.collection.fragment.CollectionThemeFragment;

import butterknife.BindView;

/**
 * Time: 2018/1/13
 * Created by LiYou
 * Description : 收藏页面 - 商品 素材
 */

public class CollectActivity extends BaseFragmentActivity implements OnChangeFragmentInterface {

    @BindView(R.id.framelayout)
    FrameLayout mFrameLayout;

    private FragmentManager fm;
    private CollectionProductFragment collectionProductFragment;
    private CollectionMaterialFragment collectionMaterialFragment;
    private CollectionThemeFragment collectionThemeFragment;

    public static void launch(Context context) {
        Intent intent = new Intent(context, CollectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected void initData() {
        collectionProductFragment = CollectionProductFragment.newInstance();
        collectionMaterialFragment = CollectionMaterialFragment.newInstance();
        collectionThemeFragment = CollectionThemeFragment.newInstance();

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.framelayout,collectionProductFragment)
                .add(R.id.framelayout,collectionMaterialFragment)
                .add(R.id.framelayout,collectionThemeFragment)
                .show(collectionProductFragment)
                .hide(collectionMaterialFragment)
                .hide(collectionThemeFragment)
                .commit();
    }

    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void changeFragment(int position) {
        switch (position) {
            case 0:
                fm.beginTransaction().show(collectionProductFragment).hide(collectionMaterialFragment).hide(collectionThemeFragment).commit();
                break;
            case 1:
                fm.beginTransaction().show(collectionMaterialFragment).hide(collectionProductFragment).hide(collectionThemeFragment).commit();
                break;
            case 2:
                fm.beginTransaction().show(collectionThemeFragment).hide(collectionProductFragment).hide(collectionMaterialFragment).commit();
                break;
        }
    }

    @Override
    public void finishView() {
        finish();
    }
}
