package com.zhaotai.uzao.ui.category.material.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.CategoryBean;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.listener.OnFilterTagClickListener;
import com.zhaotai.uzao.ui.category.material.fragment.MaterialListFragment;
import com.zhaotai.uzao.ui.search.MaterialAndSearchActivity;
import com.zhaotai.uzao.ui.search.fragment.FilterFragment;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.widget.MyPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材分类列表
 */

public class MaterialCategoryListActivity extends BaseFragmentActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.order_tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    ArrayList<Fragment> mFragments = new ArrayList<>();
    private Map<String, ProductOptionBean> optMap = new HashMap<>();
    /**
     * 分级分类Id
     */
    private FilterFragment mFilterFragment;

    /**
     * 类型
     */
    private static final String EXTRA_KEY_TYPE = "extra_key_type";
    /**
     * 分类
     */
    private static final String EXTRA_KEY_TYPE_FROM_CATEGORY = "extra_key_type_category";
    /**
     * 标签
     */
    private static final String EXTRA_KEY_TYPE_FROM_TAG = "extra_key_type_tag";

    /**
     * 素材分类列表
     *
     * @param context      上下文
     * @param categoryBean 分类
     * @param categoryName 分类名称
     */
    public static void launch(Context context, ArrayList<CategoryBean> categoryBean, String categoryName) {
        Intent intent = new Intent(context, MaterialCategoryListActivity.class);
        intent.putExtra("categoryBean", categoryBean);
        intent.putExtra("categoryName", categoryName);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_FROM_CATEGORY);
        context.startActivity(intent);
    }

    /**
     * 素材分类列表
     *
     * @param context 上下文
     * @param tagCode 标签code
     * @param tagName 标签名称
     */
    public static void launchTag(Context context, String tagCode, String tagName) {
        Intent intent = new Intent(context, MaterialCategoryListActivity.class);
        intent.putExtra("tagCode", tagCode);
        intent.putExtra("tagName", tagName);
        intent.putExtra(EXTRA_KEY_TYPE, EXTRA_KEY_TYPE_FROM_TAG);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_category_list);

        //取消侧滑
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mFilterFragment = FilterFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_filter_drawer, mFilterFragment).commit();
        //监听筛选
        mFilterFragment.setOnFilterListener(new OnFilterTagClickListener() {
            @Override
            public void onTagSelect(Map<String, String> paramss) {
                int currentTab = mTab.getCurrentTab();
                if (!mFragments.isEmpty()) {
                    ((MaterialListFragment) mFragments.get(currentTab)).filterTagSelect(paramss);
                }
            }

            @Override
            public void reset() {
                int currentTab = mTab.getCurrentTab();
                if (!mFragments.isEmpty()) {
                    ((MaterialListFragment) mFragments.get(currentTab)).filterReset();
                }
            }

            @Override
            public void closeDrawer() {
                KeyboardUtils.hideSoftInput(MaterialCategoryListActivity.this);
                mDrawerLayout.closeDrawers();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("qqqq=" + position);
                if (!mFragments.isEmpty()) {
                    String categoryCode = mFragments.get(position).getArguments().getString("categoryCode");
                    if (categoryCode != null) {
                        ProductOptionBean filterData = optMap.get(categoryCode);
                        if (filterData != null) {
                            mFilterFragment.showFilter(filterData);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        switch (getIntent().getStringExtra(EXTRA_KEY_TYPE)) {
            case EXTRA_KEY_TYPE_FROM_CATEGORY://分类
                //显示二级分类
                ArrayList<CategoryBean> categoryBean = (ArrayList<CategoryBean>) getIntent().getSerializableExtra("categoryBean");
                if (categoryBean == null) return;
                mFragments.clear();
                ArrayList<String> mTitles = new ArrayList<>();
                for (CategoryBean category : categoryBean) {
                    mFragments.add(MaterialListFragment.newInstance(category.categoryCode));
                    mTitles.add(category.categoryName);
                }
                MyPagerAdapter pageAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
                mViewPager.setAdapter(pageAdapter);
                mTab.setViewPager(mViewPager);
                break;
            case EXTRA_KEY_TYPE_FROM_TAG://标签
                //mRecyclerTab.setVisibility(View.GONE);
                mTitle.setText(getIntent().getStringExtra("tagName"));
                //params.put("tags", getIntent().getStringExtra("tagCode"));
                break;
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    public void showFilterBottomOkText(int size) {
        mFilterFragment.setBottomOKText("确定(" + size + "个素材)");
    }

    public void showFilter(ProductOptionBean opt, String categoryCode) {
        optMap.put(categoryCode, opt);
        mFilterFragment.showFilter(opt);
    }

    /**
     * 搜索
     */
    @OnClick(R.id.iv_material_search)
    public void onClickMaterialSearch() {
        MaterialAndSearchActivity.launch(mContext);
    }

    /**
     * 筛选
     */
    @OnClick(R.id.iv_material_filter)
    public void onClickMaterialFilter() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }


}
