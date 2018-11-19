package com.zhaotai.uzao.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.ScreenUtils;

import cn.hugeterry.coordinatortablayout.listener.LoadHeaderImagesListener;
import cn.hugeterry.coordinatortablayout.listener.OnTabSelectedListener;
import cn.hugeterry.coordinatortablayout.utils.SystemView;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :
 */

public class SceneCoordinatorTabLayout extends CoordinatorLayout {

    private Context mContext;
    private Toolbar mToolbar;
    private ActionBar mActionbar;
    private SlidingTabLayout mTabLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private LoadHeaderImagesListener mLoadHeaderImagesListener;
    private OnTabSelectedListener mOnTabSelectedListener;
    private ImageView mBack;
    private TextView mTitle;
    //场景人名
    private TextView tvAuthorName;

    //场景人名
    private TextView tvThemeTitle;
    //场景描述
    private TextView tvThemeContent;
    //场景头图
    private ImageView ivHeader;
    // 信息类
    private LinearLayout ll_info;

    private boolean state = false;

    public SceneCoordinatorTabLayout(Context context) {
        super(context);
        mContext = context;
    }

    public SceneCoordinatorTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    public SceneCoordinatorTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        if (!isInEditMode()) {
            initView(context);
            initWidget(context, attrs);
        }
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_scene_coordinatortablayout, this, true);
        initToolbar();
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbarlayout);
        mTabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout);
        mBack = (ImageView) findViewById(R.id.back);
        mTitle = (TextView) findViewById(R.id.title);

        ll_info = (LinearLayout) findViewById(R.id.ll_info);
        tvAuthorName = (TextView) findViewById(R.id.tv_name);
        tvThemeTitle = (TextView) findViewById(R.id.tv_theme_title);
        tvThemeContent = (TextView) findViewById(R.id.tv_content);
        ivHeader = (ImageView) findViewById(R.id.iv_header);

        RelativeLayout.LayoutParams ivHeaderLayoutParams = (RelativeLayout.LayoutParams) ivHeader.getLayoutParams();
        ivHeaderLayoutParams.width = ScreenUtils.getScreenWidth(mContext);
        //这个比例是计算来的 表明了大致宽度
        ivHeaderLayoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) * 0.672);
        ivHeader.setLayoutParams(ivHeaderLayoutParams);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_info.getLayoutParams();
        layoutParams.topMargin = (int) (ScreenUtils.getScreenWidth(mContext) * 0.672);
        ll_info.setLayoutParams(layoutParams);

        AppBarLayout mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBar.addOnOffsetChangedListener(
                new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        if (Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange() / 3) {
                            if (!state) {
                                state = true;
                                mBack.setImageResource(R.drawable.back_black);
                                mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                            }
                        } else {
                            if (state) {
                                state = false;
                                mBack.setImageResource(R.drawable.back);
                                mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                            }
                        }
                    }
                }
        );

    }


    private void initWidget(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs
                , cn.hugeterry.coordinatortablayout.R.styleable.CoordinatorTabLayout);

        TypedValue typedValue = new TypedValue();
        //mContext.getTheme().resolveAttribute(cn.hugeterry.coordinatortablayout.R.attr.colorPrimary, typedValue, true);
        int contentScrimColor = typedArray.getColor(
                cn.hugeterry.coordinatortablayout.R.styleable.CoordinatorTabLayout_contentScrim, Color.WHITE);
        mCollapsingToolbarLayout.setContentScrimColor(contentScrimColor);

//        int tabIndicatorColor = typedArray.getColor(cn.hugeterry.coordinatortablayout.R.styleable.CoordinatorTabLayout_tabIndicatorColor, Color.WHITE);
//        mTabLayout.setSelectedTabIndicatorColor(tabIndicatorColor);
//
//        int tabTextColor = typedArray.getColor(cn.hugeterry.coordinatortablayout.R.styleable.CoordinatorTabLayout_tabTextColor, Color.WHITE);
//        mTabLayout.setTabTextColors(ColorStateList.valueOf(tabTextColor));


        typedArray.recycle();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) mContext).setSupportActionBar(mToolbar);
        mActionbar = ((AppCompatActivity) mContext).getSupportActionBar();
        if (mActionbar != null)
            mActionbar.setDisplayShowTitleEnabled(false);
    }

    /**
     * 设置Toolbar标题
     *
     * @param title 标题
     * @return CoordinatorTabLayout
     */
    public SceneCoordinatorTabLayout setTitle(String title) {
        if (mActionbar != null) {
            mActionbar.setTitle(title);
        }
        return this;
    }

    /**
     * 设置Toolbar显示返回按钮及标题
     *
     * @param canBack 是否返回
     * @return CoordinatorTabLayout
     */
    public SceneCoordinatorTabLayout setBackEnable(Boolean canBack) {
        if (canBack && mActionbar != null) {
            mActionbar.setDisplayHomeAsUpEnabled(true);
            mActionbar.setHomeAsUpIndicator(cn.hugeterry.coordinatortablayout.R.drawable.ic_arrow_white_24dp);
        }
        return this;
    }


    public ImageView getIvHeader() {
        return ivHeader;
    }

    public TextView getTvAuthorName() {
        return tvAuthorName;
    }

    public TextView getTvThemeTitle() {
        return tvThemeTitle;
    }

    public TextView getTvThemeContent() {
        return tvThemeContent;
    }

    public TextView getToolBar() {
        return tvThemeContent;
    }


    /**
     * 设置与该组件搭配的ViewPager
     *
     * @param viewPager 与TabLayout结合的ViewPager
     * @return CoordinatorTabLayout
     */
    public SceneCoordinatorTabLayout setupWithViewPager(ViewPager viewPager) {
        mTabLayout.setViewPager(viewPager);
        return this;
    }


    /**
     * 获取该组件中的ActionBar
     */
    public ActionBar getActionBar() {
        return mActionbar;
    }

    /**
     * 获取该组件中的TabLayout
     */
    public SlidingTabLayout getTabLayout() {
        return mTabLayout;
    }


    public ImageView getBack() {
        return mBack;
    }


    public void setToolTitle(String title) {
        mTitle.setText(title);
    }

    /**
     * 设置LoadHeaderImagesListener
     *
     * @param loadHeaderImagesListener 设置LoadHeaderImagesListener
     * @return CoordinatorTabLayout
     */
    public SceneCoordinatorTabLayout setLoadHeaderImagesListener(LoadHeaderImagesListener loadHeaderImagesListener) {
        mLoadHeaderImagesListener = loadHeaderImagesListener;
        return this;
    }

    /**
     * 设置onTabSelectedListener
     *
     * @param onTabSelectedListener 设置onTabSelectedListener
     * @return CoordinatorTabLayout
     */
    public SceneCoordinatorTabLayout addOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mOnTabSelectedListener = onTabSelectedListener;
        return this;
    }

    /**
     * 设置透明状态栏
     *
     * @param activity 当前展示的activity
     * @return CoordinatorTabLayout
     */
    public SceneCoordinatorTabLayout setTranslucentStatusBar(@NonNull Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return this;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (mToolbar != null) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) mToolbar.getLayoutParams();
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin + SystemView.getStatusBarHeight(activity),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        }

        return this;
    }

    /**
     * 设置沉浸式
     *
     * @param activity 当前展示的activity
     * @return CoordinatorTabLayout
     */
    public SceneCoordinatorTabLayout setTranslucentNavigationBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return this;
        } else {
            mToolbar.setPadding(0, SystemView.getStatusBarHeight(activity) >> 1, 0, 0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return this;
    }
}
