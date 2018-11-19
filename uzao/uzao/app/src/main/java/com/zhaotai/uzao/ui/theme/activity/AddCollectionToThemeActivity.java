package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.ui.theme.fragment.CollectionMaterialToThemeFragment;
import com.zhaotai.uzao.ui.theme.fragment.CollectionProductToThemeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 添加收藏到主题页面
 */

public class AddCollectionToThemeActivity extends BaseFragmentActivity {


    @BindView(R.id.tv_collect_product)
    TextView tvProduct;
    @BindView(R.id.tv_collect_material)
    TextView tvMaterial;

    @BindView(R.id.v_material_line)
    View vMaterialLine;
    @BindView(R.id.v_product_line)
    View vProductLine;

    private FragmentManager fm;

    private boolean selectedProduct = true;
    private CollectionProductToThemeFragment collectionProductFragment;
    private CollectionMaterialToThemeFragment collectionMaterialFragment;

    public static void launch(Context context) {
        Intent intent = new Intent(context, AddCollectionToThemeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_collect_add_to_theme_list);
        mTitle.setText("添加素材进入主题");


        tvProduct.setSelected(selectedProduct);
        tvMaterial.setSelected(!selectedProduct);
        vMaterialLine.setVisibility(selectedProduct ? View.GONE : View.VISIBLE);
        vProductLine.setVisibility(selectedProduct ? View.VISIBLE : View.GONE);

        fm = getSupportFragmentManager();

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        selectedProduct = true;
        collectionProductFragment = CollectionProductToThemeFragment.newInstance();
        collectionMaterialFragment = CollectionMaterialToThemeFragment.newInstance();
        fm.beginTransaction()
                .add(R.id.fm_content, collectionProductFragment)
                .add(R.id.fm_content, collectionMaterialFragment)
                .show(collectionProductFragment)
                .hide(collectionMaterialFragment)
                .commit();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.iv_material_search)
    public void onSearch() {
        if (selectedProduct) {
            AddCollectionProductToThemeSearchActivity.launch(mContext);
        } else {
            AddCollectionMaterialToThemeSearchActivity.launch(mContext);
        }
    }

    private void changSelectedTag() {
        tvProduct.setSelected(selectedProduct);
        tvMaterial.setSelected(!selectedProduct);
        vMaterialLine.setVisibility(selectedProduct ? View.GONE : View.VISIBLE);
        vProductLine.setVisibility(selectedProduct ? View.VISIBLE : View.GONE);

        if (selectedProduct) {
            fm.beginTransaction()
                    .show(collectionProductFragment)
                    .hide(collectionMaterialFragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .hide(collectionProductFragment)
                    .show(collectionMaterialFragment)
                    .commit();
        }

    }


    @OnClick({R.id.rl_product, R.id.rl_material})
    public void onSelectedTabChange(View view) {
        switch (view.getId()) {
            case R.id.rl_product:
                selectedProduct = true;
                break;
            case R.id.rl_material:
                selectedProduct = false;
                break;
        }
        changSelectedTag();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
        switch (event.getEventType()) {
            case EventBusEvent.ADD_MODULE_TO_THEME:
                finish();
                break;
        }
    }
}




