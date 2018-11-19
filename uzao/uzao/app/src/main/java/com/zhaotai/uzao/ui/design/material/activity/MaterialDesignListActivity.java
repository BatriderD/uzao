package com.zhaotai.uzao.ui.design.material.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.ui.design.material.fragment.MaterialAllListFragment;
import com.zhaotai.uzao.ui.design.material.fragment.MaterialMyFragment;
import com.zhaotai.uzao.ui.person.collection.CollectMaterialSearchActivity;
import com.zhaotai.uzao.ui.search.MaterialAndSearchActivity;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 编辑器的素材列表页
 * 包括全部素材fragment 和我的fragment
 * zp
 */

public class MaterialDesignListActivity extends BaseFragmentActivity {

    //标题
    @BindView(R.id.tv_title)
    public TextView tv_title;

    //搜索
    @BindView(R.id.iv_search)
    public ImageView iv_search;
    //搜索
    @BindView(R.id.fl_root)
    public FrameLayout flRoot;

    @BindView(R.id.btn_my)
    public Button btnMy;


    @BindView(R.id.btn_all)
    public Button btnAll;
    //所有素材fragment
    private MaterialAllListFragment materialAllListFragment;
    //我的素材fragment
    private MaterialMyFragment materialMyFragment;

    private FragmentManager fm;


    //    调起本页面
    public static void launch(Context context) {
        Intent intent = new Intent(context, MaterialDesignListActivity.class);
        context.startActivity(intent);
    }

    //    返回按钮点击
    @OnClick(R.id.iv_back)
    public void toBack() {
        finish();
    }


    // 搜索
    @OnClick(R.id.iv_search)
    public void toSearch() {
        if (materialAllListFragment.isVisible()) {
            String categoryWord = materialAllListFragment.getCategoryWord();
            MaterialAndSearchActivity.launch(this, "editor", categoryWord);
        } else {
            int type = materialMyFragment.getType();
            // 0 已收藏 1 已购买 2 已上传
            switch (type) {
                case 0:
                    CollectMaterialSearchActivity.launch(this,"design");
                    break;
                case 1:
                    MaterialBoughtSearchActivity.launch(mContext);
                    break;
                case 2:
                    MaterialUploadSearchActivity.launch(mContext);
                    break;
            }
        }
    }

    // 我的
    @OnClick(R.id.btn_my)
    public void toMyMaterialList() {
        if (LoginHelper.getLoginStatus()) {
            btnAll.setSelected(false);
            btnMy.setSelected(true);
            changFragment(1);
        } else {
            LoginHelper.goLogin(this);
        }

    }

    // 切换到所有素材的fragment
    @OnClick(R.id.btn_all)
    public void toAllMaterialList() {
        btnAll.setSelected(true);
        btnMy.setSelected(false);
        changFragment(0);
    }


    protected void initView() {
        setContentView(R.layout.activity_material_list);
        //素材列表
        tv_title.setText(R.string.material);

    }
    @Override
    public void handleStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_black));
    }


    protected void initData() {
        EventBus.getDefault().register(this);
        // 初始化全部素材页面
        materialAllListFragment = MaterialAllListFragment.newInstance();
        // 初始化我的素材页面
        materialMyFragment = MaterialMyFragment.newInstance();
        fm = getSupportFragmentManager();
        //切换到全部标签
        btnAll.setSelected(true);
        btnMy.setSelected(false);
        changFragment(0);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return false;
    }


    /**
     * 切换fragment
     *
     * @param i 0是所有素材， 1是我的素材
     */
    private void changFragment(int i) {

        FragmentTransaction ft = this.fm.beginTransaction();

        if (!materialMyFragment.isAdded() && !materialAllListFragment.isAdded()) {
            ft.add(R.id.fl_root, materialAllListFragment);
            ft.add(R.id.fl_root, materialMyFragment);
        }
        if (i == 0) {
            ft.show(materialAllListFragment);
            ft.hide(materialMyFragment);
        } else {
            ft.hide(materialAllListFragment);
            ft.show(materialMyFragment);
        }
        ft.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AddEditorMaterialBean event) {
        //用户可能在素材搜索页面， 选择了素材 所以这个页面需要被关闭。
        finish();
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
