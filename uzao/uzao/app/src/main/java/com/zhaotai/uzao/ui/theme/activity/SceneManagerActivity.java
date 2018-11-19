package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.ui.post.activity.PublishPostActivity;
import com.zhaotai.uzao.ui.theme.contract.SceneManagerContract;
import com.zhaotai.uzao.ui.theme.fragment.SceneManagerPhotoFragment;
import com.zhaotai.uzao.ui.theme.fragment.SceneManagerPostFragment;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.utils.StatusBarUtilNew;
import com.zhaotai.uzao.widget.MyPagerAdapter;
import com.zhaotai.uzao.widget.SceneCoordinatorTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SceneManagerActivity extends BaseFragmentActivity implements SceneManagerContract.View {

    private static final String THEME = "THEME_BEAN";
    private static final String ABLE_MANAGE = "ABLE_MANAGE";
    @BindView(R.id.coordinatortablayout)
    SceneCoordinatorTabLayout mCoordinatorTabLayout;

    @BindView(R.id.vp)
    ViewPager viewPager;


    @BindView(R.id.tv_post)
    TextView tvPost;


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private ThemeBean themeBean;
    private boolean ableManage;


    public static void launch(Context context, ThemeBean item, boolean ableManage) {
        Intent intent = new Intent(context, SceneManagerActivity.class);
        intent.putExtra(THEME, item);
        intent.putExtra(ABLE_MANAGE, ableManage);
        context.startActivity(intent);

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_scene_manager_new);


    }

    @Override
    public void handleStatusBar() {
                StatusBarUtilNew.darkMode(this);
                StatusBarUtilNew.immersive(this);
//        StatusBarUtil.setTranslucent(this,0);
    }

    @Override
    public boolean hasTitle() {
        return false;
    }

    private void initViewPager() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        viewPager.setAdapter(mAdapter);
    }

    private void initFragments() {
        SceneManagerPostFragment sceneManagerPostFragment = SceneManagerPostFragment.newInstance(themeBean.sequenceNBR, ableManage);
        mFragments.add(sceneManagerPostFragment);
        mTitles.add("帖子");


        SceneManagerPhotoFragment sceneManagerPhotoFragment = SceneManagerPhotoFragment.newInstance(themeBean.sequenceNBR, ableManage);
        mFragments.add(sceneManagerPhotoFragment);
        mTitles.add("相册");


    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        themeBean = (ThemeBean) intent.getSerializableExtra(THEME);
        ableManage = intent.getBooleanExtra(ABLE_MANAGE, false);
        if (themeBean == null) {
            showEmpty();
            return;
        }
        if (ableManage) {
            mCoordinatorTabLayout.setToolTitle("管理场景");
        } else {
            mCoordinatorTabLayout.setToolTitle(themeBean.name);
        }
        tvPost.setVisibility("Y".equals(themeBean.canComment) ? View.VISIBLE : View.GONE);
        mCoordinatorTabLayout.getTvAuthorName().setText(themeBean.userName);
        mCoordinatorTabLayout.getTvThemeTitle().setText(themeBean.name + "/");
        mCoordinatorTabLayout.getTvThemeContent().setText(themeBean.description);
        mCoordinatorTabLayout.setTranslucentStatusBar(this);
        ImageView ivHeader = mCoordinatorTabLayout.getIvHeader();
        GlideLoadImageUtil.load(this, ApiConstants.UZAOCHINA_IMAGE_HOST + themeBean.cover, ivHeader, R.mipmap.ic_place_holder, R.mipmap.ic_error);


        initFragments();
        initViewPager();
        SlidingTabLayout tabLayout = mCoordinatorTabLayout.getTabLayout();
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (ableManage) {
                    if (position == 0) {
                        tvPost.setVisibility(View.VISIBLE);
                    } else {
                        tvPost.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mCoordinatorTabLayout
                .setupWithViewPager(viewPager);


    }

    @OnClick(R.id.tv_post)
    public void toPost() {
        PublishPostActivity.launch(this, themeBean.sequenceNBR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fm : mFragments) {
            fm.onActivityResult(requestCode, resultCode, data);
        }
    }
}
