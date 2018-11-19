package com.zhaotai.uzao.ui.search.fragment;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.bean.ProductOptionBean.FilterBean;
import com.zhaotai.uzao.bean.ProductOptionBean;
import com.zhaotai.uzao.listener.OnFilterTagClickListener;
import com.zhaotai.uzao.ui.search.adapter.FilterBrandAdapter;
import com.zhaotai.uzao.ui.search.adapter.FilterCategoryAdapter;
import com.zhaotai.uzao.ui.search.adapter.FilterModeAdapter;
import com.zhaotai.uzao.ui.search.adapter.FilterTagAdapter;
import com.zhaotai.uzao.ui.search.adapter.FilterTypeAdapter;
import com.zhaotai.uzao.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/4/13
 * Created by LiYou
 * Description : 筛选
 */

public class FilterFragment extends BaseFragment {

    @BindView(R.id.et_price_min)
    EditText mEtPriceMin;
    @BindView(R.id.et_price_max)
    EditText mEtPriceMax;
    @BindView(R.id.recycler_tag)
    RecyclerView mRecyclerTag;
    @BindView(R.id.recycler_category)
    RecyclerView mRecyclerCategory;
    @BindView(R.id.recycler_type)
    RecyclerView mRecyclerType;
    @BindView(R.id.recycler_mode)
    RecyclerView mRecyclerMode;
    @BindView(R.id.recycler_brand)
    RecyclerView mRecyclerBrand;

    //价格类型
    @BindView(R.id.ll_filter_mode)
    LinearLayout mFilterMode;
    @BindView(R.id.ll_filter_category_all)
    LinearLayout mFilterCategory;
    //品牌
    @BindView(R.id.ll_filter_brand)
    LinearLayout mFilterBrand;
    //标签
    @BindView(R.id.ll_filter_tag)
    LinearLayout mFilterTag;

    //价格区间
    @BindView(R.id.ll_price_range)
    LinearLayout mLlPriceRange;
    //类型
    @BindView(R.id.ll_filter_type)
    LinearLayout mLlType;
    //分类更多
    @BindView(R.id.tv_filter_category_more)
    TextView mCategoryMore;
    //标签更多
    @BindView(R.id.tv_tag_more)
    TextView mTagMore;
    //品牌更多
    @BindView(R.id.tv_filter_brand_more)
    TextView mBrandMore;
    @BindView(R.id.tv_bottom_ok)
    TextView mBottomOk;

    private FilterTagAdapter mTagAdapter;
    private FilterTypeAdapter mTypeAdapter;
    private FilterCategoryAdapter mCategoryAdapter;
    private FilterBrandAdapter mBrandAdapter;
    private FilterModeAdapter mModeAdapter;
    private List<FilterBean> tags = new ArrayList<>();
    private List<FilterBean> types = new ArrayList<>();
    private List<FilterBean> brands = new ArrayList<>();
    private List<FilterBean> modes = new ArrayList<>();
    private ProductOptionBean optData;

    private Map<String, String> params = new HashMap<>();
    private OnFilterTagClickListener mListener;

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.view_goods_filter;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    public void setOnFilterListener(OnFilterTagClickListener listener) {
        this.mListener = listener;
    }

    /**
     * 重置价格
     */
    public void setPriceReset() {
        mEtPriceMin.setText("");
        mEtPriceMax.setText("");
    }

    /**
     * 标签更多
     */
    @OnClick(R.id.ll_filter_tag)
    public void onClickTagMore() {
        if (tags != null) {
            if (tags.size() > 6) {
                if (optData.openTags) {
                    optData.openTags = false;
                    Drawable drawable = getContext().getDrawable(R.drawable.ic_filter_down);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mTagMore.setCompoundDrawables(null, null, drawable, null);
                    mTagAdapter.setNewData(tags.subList(0, 6));
                } else {
                    optData.openTags = true;
                    Drawable drawable = getContext().getDrawable(R.drawable.ic_filter_up);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mTagMore.setCompoundDrawables(null, null, drawable, null);
                    mTagAdapter.setNewData(tags);
                }
            }
        }
    }

    /**
     * 分类更多
     */
    @OnClick(R.id.ll_filter_category)
    public void onClickCategory() {
        if (optData.cat != null) {
            if (optData.cat.size() > 6) {
                if (optData.openCat) {
                    optData.openCat = false;
                    Drawable drawable = getContext().getDrawable(R.drawable.ic_filter_down);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mCategoryMore.setCompoundDrawables(null, null, drawable, null);
                    mCategoryAdapter.setNewData(optData.cat.subList(0, 6));
                } else {
                    optData.openCat = true;
                    Drawable drawable = getContext().getDrawable(R.drawable.ic_filter_up);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mCategoryMore.setCompoundDrawables(null, null, drawable, null);
                    mCategoryAdapter.setNewData(optData.cat);
                }
            }
        }
    }

    /**
     * 品牌更多
     */
    @OnClick(R.id.ll_filter_brand)
    public void onClickBrandMore() {
        if (optData.brand != null) {
            if (optData.brand.size() > 6) {
                if (optData.openBrand) {
                    optData.openBrand = false;
                    Drawable drawable = getContext().getDrawable(R.drawable.ic_filter_down);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mBrandMore.setCompoundDrawables(null, null, drawable, null);
                    mBrandAdapter.setNewData(optData.brand.subList(0, 6));
                } else {
                    optData.openBrand = true;
                    Drawable drawable = getContext().getDrawable(R.drawable.ic_filter_up);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mBrandMore.setCompoundDrawables(null, null, drawable, null);
                    mBrandAdapter.setNewData(optData.brand);
                }
            }
        }
    }

    /**
     * 重置
     */
    @OnClick(R.id.tv_bottom_reset)
    public void onClickFilterReset() {
        mListener.reset();
        setPriceReset();
        mListener.closeDrawer();
    }

    /**
     * 完成
     */
    @OnClick(R.id.tv_bottom_ok)
    public void onClickFilterOk() {
        searchFilter();
        mListener.closeDrawer();
    }

    /**
     * 显示筛选
     */
    public void showFilter(final ProductOptionBean opt) {
        optData = opt;
        //收费模式
        if (opt.saleModes != null) {
            mFilterMode.setVisibility(View.VISIBLE);
            if (mModeAdapter == null) {
                mModeAdapter = new FilterModeAdapter();
                mRecyclerMode.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                mRecyclerMode.setAdapter(mModeAdapter);
            }
            modes.clear();
            for (int i = 0; i < opt.saleModes.length; i++) {
                FilterBean filterBean = new FilterBean();
                filterBean.name = opt.saleModes[i];
                modes.add(filterBean);
            }
            mModeAdapter.setNewData(modes);
            mModeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    modes.get(position).isSelected = !modes.get(position).isSelected;
                    mModeAdapter.notifyDataSetChanged();
                    searchFilter();
                    for (int i = 0; i < modes.size(); i++) {
                        if (modes.get(i).isSelected && modes.get(i).name.equals("charge")) {
                            mLlPriceRange.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (modes.get(i).isSelected && modes.get(i).name.equals("free")) {
                            mLlPriceRange.setVisibility(View.GONE);
                            return;
                        }
                    }
                    mLlPriceRange.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mFilterMode.setVisibility(View.GONE);
        }

        //类型
        if (opt.types != null) {
            mLlType.setVisibility(View.VISIBLE);
            if (mTypeAdapter == null) {
                mTypeAdapter = new FilterTypeAdapter();
                mRecyclerType.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                mRecyclerType.setAdapter(mTypeAdapter);
            }
            types = new ArrayList<>();
            for (int i = 0; i < opt.types.length; i++) {
                FilterBean filterBean = new FilterBean();
                filterBean.name = opt.types[i];
                types.add(filterBean);
            }
            mTypeAdapter.setNewData(types);
            mTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    types.get(position).isSelected = !types.get(position).isSelected;
                    mTypeAdapter.notifyDataSetChanged();
                    searchFilter();
                }
            });
        } else {
            mLlType.setVisibility(View.GONE);
        }

        //标签
        if (opt.tags != null) {
            mFilterTag.setVisibility(View.VISIBLE);
            if (mTagAdapter == null) {
                mTagAdapter = new FilterTagAdapter();
                mRecyclerTag.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                mRecyclerTag.setAdapter(mTagAdapter);
            }
            tags = opt.tags;
            if (tags.size() > 6) {
                mTagMore.setVisibility(View.VISIBLE);
                mTagAdapter.setNewData(tags.subList(0, 6));
            } else {
                mTagMore.setVisibility(View.GONE);
                mTagAdapter.setNewData(tags);
            }
            mTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    tags.get(position).isSelected = !tags.get(position).isSelected;
                    mTagAdapter.notifyDataSetChanged();
                    searchFilter();
                }
            });
        } else {
            mFilterTag.setVisibility(View.GONE);
        }
        //分类
        if (opt.cat != null) {
            if (mCategoryAdapter == null) {
                mCategoryAdapter = new FilterCategoryAdapter();
                mRecyclerCategory.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                mRecyclerCategory.setAdapter(mCategoryAdapter);
            }
            if (opt.cat.size() > 6) {
                mCategoryMore.setVisibility(View.VISIBLE);
                mCategoryAdapter.setNewData(opt.cat.subList(0, 6));
            } else {
                mCategoryMore.setVisibility(View.GONE);
                mCategoryAdapter.setNewData(opt.cat);
            }

            mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int i = 0; i < opt.cat.size(); i++) {
                        opt.cat.get(i).isSelected = position == i && !opt.cat.get(i).isSelected;
                    }

                    mCategoryAdapter.notifyDataSetChanged();
                    searchFilter();
                }
            });
        } else {
            mFilterCategory.setVisibility(View.GONE);
        }

        //品牌
        if (opt.brand != null && opt.brand.size() > 0) {
            mFilterBrand.setVisibility(View.VISIBLE);
            if (mBrandAdapter == null) {
                mBrandAdapter = new FilterBrandAdapter();
                mRecyclerBrand.setLayoutManager(new GridLayoutManager(_mActivity, 3));
                mRecyclerBrand.setAdapter(mBrandAdapter);
            }
            brands = opt.brand;
            if (opt.brand.size() > 6) {
                mBrandMore.setVisibility(View.VISIBLE);
                mBrandAdapter.setNewData(opt.brand.subList(0, 6));
            } else {
                mBrandMore.setVisibility(View.GONE);
                mBrandAdapter.setNewData(opt.brand);
            }

            mBrandAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    brands.get(position).isSelected = !brands.get(position).isSelected;
                    mBrandAdapter.notifyDataSetChanged();
                    searchFilter();
                }
            });
        } else {
            mFilterBrand.setVisibility(View.GONE);
        }
    }

    /**
     * 根据条件筛选
     */
    public void searchFilter() {
        params.clear();
        //价格
        if (!StringUtil.isTrimEmpty(mEtPriceMin.getText().toString())) {
            params.put("minPriceY", mEtPriceMin.getText().toString().trim());
        }
        if (!StringUtil.isTrimEmpty(mEtPriceMax.getText().toString())) {
            params.put("maxPriceY", mEtPriceMax.getText().toString().trim());
        }

        //收费模式
        StringBuilder saleMode = new StringBuilder();
        if (optData.saleModes != null && modes.size() > 0) {
            for (int i = 0; i < modes.size(); i++) {
                if (modes.get(i).isSelected) {
                    saleMode.append(modes.get(i).name).append(",");
                }
            }
            params.put("saleModes", saleMode.toString());
        }

        //分类
        StringBuilder type = new StringBuilder();
        if (optData.types != null && types.size() > 0) {
            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).isSelected) {
                    type.append(types.get(i).name).append(",");
                }
            }
            params.put("types", type.toString());
        }

        StringBuilder tag = new StringBuilder();
        //标签
        if (optData.tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                if (tags.get(i).isSelected) {
                    tag.append(tags.get(i).code).append(",");
                }
            }
            params.put("tags", tag.toString());
        }

        if (optData.cat != null) {
            //分类
            for (int i = 0; i < optData.cat.size(); i++) {
                if (optData.cat.get(i).isSelected) {
                    params.put("categoryCode3", optData.cat.get(i).code);
                }
            }
        }

        //品牌
        StringBuilder brand = new StringBuilder();
        if (optData.brand != null && brands.size() > 0) {
            for (int i = 0; i < brands.size(); i++) {
                if (brands.get(i).isSelected) {
                    brand.append(brands.get(i).id).append(",");
                }
            }
            params.put("brands", brand.toString());
        }
        mListener.onTagSelect(params);
    }

    public void setBottomOKText(String text) {
        mBottomOk.setText(text);
    }
}
