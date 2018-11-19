package com.zhaotai.uzao.ui.main.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.DynamicBodyBean;
import com.zhaotai.uzao.bean.DynamicValuesBean;
import com.zhaotai.uzao.bean.MultiMainBean;
import com.zhaotai.uzao.constants.DynamicType;
import com.zhaotai.uzao.ui.brand.BrandActivity;
import com.zhaotai.uzao.ui.category.goods.activity.ActivityListActivity;
import com.zhaotai.uzao.ui.category.goods.activity.CommodityDetailMallActivity;
import com.zhaotai.uzao.ui.category.goods.activity.NavigateProductListActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.ui.web.SpecialActivityWebActivity;
import com.zhaotai.uzao.ui.web.WebActivity;
import com.zhaotai.uzao.utils.GlideCircleTransform;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.List;

/**
 * Time: 2018/3/22
 * Created by LiYou
 * Description : 子首页的Adapter
 */

public class MainChildAdapter extends BaseMultiItemQuickAdapter<MultiMainBean, BaseViewHolder> {

    private Gson gson;
    private MZBannerView<DynamicValuesBean> mMZBanner;
    private Banner mBanner;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MainChildAdapter(List<MultiMainBean> data) {
        super(data);
        gson = new Gson();
        addItemType(MultiMainBean.TYPE_BANNER, R.layout.item_main_banner);
        addItemType(MultiMainBean.TYPE_INSPIRATION_GALLERY_TITLE, R.layout.item_main_child_title);
        addItemType(MultiMainBean.TYPE_INSPIRATION_GALLERY, R.layout.item_main_child_inspiration_gallery);
        addItemType(MultiMainBean.TYPE_POPULAR_DESIGN_TITLE, R.layout.item_main_child_title);
        addItemType(MultiMainBean.TYPE_POPULAR_DESIGN, R.layout.item_main_child_popular_design);
        addItemType(MultiMainBean.TYPE_RECOMMEND_SPU_TITLE, R.layout.item_main_child_title);
        addItemType(MultiMainBean.TYPE_RECOMMEND_SPU, R.layout.item_main_child_recommend_spu);
        addItemType(MultiMainBean.TYPE_MORE, R.layout.item_main_child_bottom_more);
        addItemType(MultiMainBean.TYPE_RECOMMEND_MATERIAL_TITLE, R.layout.item_main_child_title);
        addItemType(MultiMainBean.TYPE_RECOMMEND_MATERIAL, R.layout.item_main_child_recommend_material);
        addItemType(MultiMainBean.TYPE_BRAND_TITLE, R.layout.item_main_child_title);
        addItemType(MultiMainBean.TYPE_BRAND_THEME, R.layout.item_main_child_brand_theme);
        addItemType(MultiMainBean.TYPE_BRAND, R.layout.item_main_child_brand);
        addItemType(MultiMainBean.TYPE_ABOUT_US_TITLE, R.layout.item_main_child_title);
        addItemType(MultiMainBean.TYPE_ABOUT_US, R.layout.item_main_child_about_us);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiMainBean item) {
        switch (helper.getItemViewType()) {
            case MultiMainBean.TYPE_BANNER:
                //初始化轮播图
                initBanner(helper, item);
                break;
            case MultiMainBean.TYPE_INSPIRATION_GALLERY_TITLE:
                helper.setText(R.id.tv_main_child_title, "灵感画廊");
                break;
            case MultiMainBean.TYPE_INSPIRATION_GALLERY:
                //初始化灵感画廊
                initInspirationGallery(helper, item);
                break;
            case MultiMainBean.TYPE_POPULAR_DESIGN_TITLE:
                helper.setText(R.id.tv_main_child_title, "人气设计");
                break;
            case MultiMainBean.TYPE_POPULAR_DESIGN:
                //初始化人气设计
                initPopularDesign(helper, item);
                break;
            case MultiMainBean.TYPE_RECOMMEND_SPU_TITLE:
                helper.setText(R.id.tv_main_child_title, "推荐商品");
                break;
            case MultiMainBean.TYPE_RECOMMEND_SPU:
                //初始化推荐商品
                initRecommendSpu(helper, item);
                break;
            case MultiMainBean.TYPE_MORE:
                helper.addOnClickListener(R.id.tv_main_child_bottom_more);
                break;
            case MultiMainBean.TYPE_RECOMMEND_MATERIAL_TITLE:
                helper.setText(R.id.tv_main_child_title, "推荐素材");
                break;
            case MultiMainBean.TYPE_RECOMMEND_MATERIAL:
                //初始化推荐素材
                initRecommendMaterial(helper, item);
                break;
            case MultiMainBean.TYPE_BRAND_TITLE:
                helper.setText(R.id.tv_main_child_title, "合作品牌");
                break;
            case MultiMainBean.TYPE_BRAND_THEME:
                //初始化品牌主题
                initBrandTheme(helper, item);
                break;
            case MultiMainBean.TYPE_BRAND:
                //初始化品牌
                initBrand(helper, item);
                break;
            case MultiMainBean.TYPE_ABOUT_US_TITLE://了解我们标题
                helper.setText(R.id.tv_main_child_title, "了解我们");
                break;
            case MultiMainBean.TYPE_ABOUT_US:
                //初始化关于我们
                initAboutUs(helper, item);
                break;
        }
    }

    /**
     * 构造 了解我们
     */
    private void initAboutUs(final BaseViewHolder helper, MultiMainBean item) {
        TextView mTvDesign = helper.getView(R.id.tv_main_child_bottom_tab_design);
        TextView mTvTheme = helper.getView(R.id.tv_main_child_bottom_tab_theme);
        TextView mTvDesigner = helper.getView(R.id.tv_main_child_bottom_tab_designer);

        mTvDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.setVisible(R.id.view_main_child_bottom_tab_line_1, true)
                        .setVisible(R.id.view_main_child_bottom_tab_line_2, false)
                        .setVisible(R.id.view_main_child_bottom_tab_line_3, false);
            }
        });
        mTvTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.setVisible(R.id.view_main_child_bottom_tab_line_1, false)
                        .setVisible(R.id.view_main_child_bottom_tab_line_2, true)
                        .setVisible(R.id.view_main_child_bottom_tab_line_3, false);
            }
        });
        mTvDesigner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.setVisible(R.id.view_main_child_bottom_tab_line_1, false)
                        .setVisible(R.id.view_main_child_bottom_tab_line_2, false)
                        .setVisible(R.id.view_main_child_bottom_tab_line_3, true);
            }
        });
    }

    /**
     * 构造 品牌
     */
    private void initBrand(BaseViewHolder helper, final MultiMainBean item) {
        if (item.values != null && item.values.size() > 0) {
            if (item.values.size() > 8) {
                item.values = item.values.subList(0, 8);
            }
            RecyclerView mRecyclerBrand = helper.getView(R.id.recycler_brand);
            mRecyclerBrand.setLayoutManager(new GridLayoutManager(mContext, 4));
            MainChildBrandAdapter mBrandAdapter = new MainChildBrandAdapter(item.values);
            mRecyclerBrand.setAdapter(mBrandAdapter);
            mBrandAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    DynamicBodyBean body = gson.fromJson(item.values.get(position).contentBody, DynamicBodyBean.class);
                    BrandActivity.launch(mContext, body.sequenceNBR);
                }
            });
        }
    }

    /**
     * 构造 品牌主题
     */
    private void initBrandTheme(BaseViewHolder helper, final MultiMainBean item) {
        if (item.values != null && item.values.size() > 0) {
            RecyclerView recyclerView = helper.getView(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            MainChildBrandThemeAdapter mBrandThemeAdapter = new MainChildBrandThemeAdapter(item.values);
            recyclerView.setAdapter(mBrandThemeAdapter);
            mBrandThemeAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    DynamicValuesBean info = item.values.get(position);
                    onClickBanner(info, item.groupType);
                }
            });
        }
    }

    /**
     * 构造 素材推荐
     */
    private void initRecommendMaterial(BaseViewHolder helper, MultiMainBean item) {
        if (item.value != null) {
            DynamicBodyBean body = gson.fromJson(item.value.contentBody, DynamicBodyBean.class);
            ImageView mIvMaterial = helper.getView(R.id.iv_main_child_recommend_material_pic);
            if (item.value.referType.equals(DynamicType.MATERIAL_DETAIL)) {
                helper.setVisible(R.id.tv_main_child_recommend_material_btn_design, true)
                        .addOnClickListener(R.id.tv_main_child_recommend_material_btn_design);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(body.thumbnail), mIvMaterial);
            } else if (item.value.referType.equals(DynamicType.THEME_DETAIL)) {
                helper.setVisible(R.id.tv_main_child_recommend_material_btn_design, false);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(body.cover), mIvMaterial);
            }
            helper.setText(R.id.tv_main_child_recommend_material_name, item.value.alias)
                    .addOnClickListener(R.id.iv_main_child_recommend_material_pic);//图片
        }
    }

    /**
     * 构造 商品推荐
     */
    private void initRecommendSpu(BaseViewHolder helper, MultiMainBean item) {
        if (item.value != null) {
            DynamicBodyBean body = gson.fromJson(item.value.contentBody, DynamicBodyBean.class);
            ImageView mIvSpu = helper.getView(R.id.iv_main_child_recommend_spu_pic);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(body.thumbnail), mIvSpu);
            helper.setText(R.id.tv_main_child_recommend_spu_name, item.value.alias)
                    .addOnClickListener(R.id.tv_main_child_recommend_spu_btn_design)//定制
                    .addOnClickListener(R.id.iv_main_child_recommend_spu_pic);//图片
        }
    }

    /**
     * 构造 人气设计
     */
    private void initPopularDesign(final BaseViewHolder helper, final MultiMainBean item) {
        if (item.values != null && item.values.size() > 0) {
            helper.getView(R.id.iv_popular_design_view_pager_go_to_design).setTag(item.values.get(0).contentBody);
            helper.setText(R.id.tv_popular_design_view_pager_spu_name, item.values.get(0).alias)
                    .addOnClickListener(R.id.iv_popular_design_view_pager_go_to_design);
            mMZBanner = helper.getView(R.id.vp_banner);
            ViewGroup.LayoutParams layoutParams = mMZBanner.getLayoutParams();
            layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext) * 0.8);
            mMZBanner.setLayoutParams(layoutParams);
            mMZBanner.setIndicatorVisible(false);
            mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
                @Override
                public void onPageClick(View view, int i) {
                    DynamicBodyBean body = gson.fromJson(item.values.get(i).contentBody, DynamicBodyBean.class);
                    CommodityDetailMallActivity.launch(mContext, body.sequenceNBR);
                }
            });
            mMZBanner.addPageChangeLisnter(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    helper.setText(R.id.tv_popular_design_view_pager_spu_name, item.values.get(position).alias);
                    helper.getView(R.id.iv_popular_design_view_pager_go_to_design).setTag(item.values.get(position).contentBody);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            // 设置数据
            mMZBanner.setPages(item.values, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }
            });


        }
    }

    private static class BannerViewHolder implements MZViewHolder<DynamicValuesBean> {

        private ImageView mIvSpuPic;
        private ImageView mIvDesignerPic;
        private TextView mTvSaleCount;
        private TextView mTvCollectCount;
        private TextView mTvDesignerName;
        private Gson gsons = new Gson();

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.item_main_child_popular_design_view_pager, null);
            mIvSpuPic = (ImageView) view.findViewById(R.id.iv_popular_design_pic);
            mIvDesignerPic = (ImageView) view.findViewById(R.id.iv_popular_design_designer_pic);
            mTvSaleCount = (TextView) view.findViewById(R.id.tv_popular_design_sale_count);
            mTvCollectCount = (TextView) view.findViewById(R.id.tv_popular_design_collect_count);
            mTvDesignerName = (TextView) view.findViewById(R.id.tv_popular_design_designer_name);
            int screenWidth = ScreenUtils.getScreenWidth(context);
            int size = (int) (screenWidth * 0.8);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
            view.setLayoutParams(lp);
            return view;
        }

        @Override
        public void onBind(Context context, int position, DynamicValuesBean item) {
            // 数据绑定
            final DynamicBodyBean body = gsons.fromJson(item.contentBody, DynamicBodyBean.class);
            GlideLoadImageUtil.load(context, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(body.thumbnail), mIvSpuPic);
            if (body.designer != null) {
                mTvDesignerName.setText(body.designer.nickName);
                Glide.with(context).load(ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(body.designer.avatar))
                        .transform(new GlideCircleTransform(context)).into(mIvDesignerPic);
            }
            mTvSaleCount.setText("销量    " + body.salesCount);
            mTvCollectCount.setText(body.favoriteCount);
        }
    }


    /**
     * 构造 灵感画廊
     */
    private void initInspirationGallery(BaseViewHolder helper, MultiMainBean item) {
        if (item.values != null && item.values.size() > 1) {

            DynamicValuesBean gallery1 = item.values.get(0);
            DynamicValuesBean gallery2 = item.values.get(1);
            //素材
            helper.setText(R.id.tv_inspiration_gallery_material_name_1, gallery1.alias1);
            final DynamicBodyBean material1 = gson.fromJson(gallery1.contentBody1, DynamicBodyBean.class);
            ImageView mIvMaterial1 = helper.getView(R.id.iv_inspiration_gallery_material_1);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(material1.thumbnail), mIvMaterial1);
            mIvMaterial1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDetailActivity.launch(mContext, material1.sequenceNBR);
                }
            });
            //载体
            helper.setText(R.id.tv_inspiration_gallery_model_name_1, gallery1.alias2);
            final DynamicBodyBean model1 = gson.fromJson(gallery1.contentBody2, DynamicBodyBean.class);
            ImageView mIvModel1 = helper.getView(R.id.iv_inspiration_gallery_model_1);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(model1.thumbnail), mIvModel1);
            mIvModel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommodityDetailMallActivity.launch(mContext, model1.sequenceNBR);
                }
            });


            //商品
            helper.setText(R.id.tv_inspiration_gallery_spu_name_1, gallery1.alias3);
            final DynamicBodyBean spu1 = gson.fromJson(gallery1.contentBody3, DynamicBodyBean.class);
            ImageView mIvSpu1 = helper.getView(R.id.iv_inspiration_gallery_spu_1);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(spu1.thumbnail), mIvSpu1);
            mIvSpu1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommodityDetailMallActivity.launch(mContext, spu1.sequenceNBR);
                }
            });

            //素材
            final DynamicBodyBean material2 = gson.fromJson(gallery2.contentBody1, DynamicBodyBean.class);
            ImageView mIvMaterial2 = helper.getView(R.id.iv_inspiration_gallery_material_2);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(material2.thumbnail), mIvMaterial2);
            mIvMaterial2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDetailActivity.launch(mContext, material2.sequenceNBR);
                }
            });

            //载体
            final DynamicBodyBean model2 = gson.fromJson(gallery2.contentBody2, DynamicBodyBean.class);
            ImageView mIvModel2 = helper.getView(R.id.iv_inspiration_gallery_model_2);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(model2.thumbnail), mIvModel2);
            mIvModel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommodityDetailMallActivity.launch(mContext, model2.sequenceNBR);
                }
            });

            //商品
            helper.setText(R.id.tv_inspiration_gallery_spu_name_2, gallery2.alias3);
            final DynamicBodyBean spu2 = gson.fromJson(gallery2.contentBody3, DynamicBodyBean.class);
            ImageView mIvSpu2 = helper.getView(R.id.iv_inspiration_gallery_spu_2);
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(spu2.thumbnail), mIvSpu2);
            mIvSpu2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommodityDetailMallActivity.launch(mContext, spu2.sequenceNBR);
                }
            });

            helper.setText(R.id.tv_inspiration_gallery_name_2, "(" + gallery2.alias1 + "+" + gallery2.alias2 + ")");

        }
    }

    /**
     * 构造 轮播图
     */
    private void initBanner(BaseViewHolder helper, final MultiMainBean item) {
        if (item.values != null && item.values.size() > 0) {

            //初始化banner
            mBanner = helper.getView(R.id.main_banner);

            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片集合
            mBanner.setImages(item.values);
            //设置图片加载器
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    DynamicValuesBean valuesBean = (DynamicValuesBean) path;
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(valuesBean.image), imageView);
                }
            });
            //设置自动轮播，默认为true
            mBanner.isAutoPlay(true);
            //设置轮播时间
            //mBanner.setDelayTime(3000);
            //banner设置方法全部调用完毕时最后调用
            mBanner.start();
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    DynamicValuesBean info = item.values.get(position);
                    onClickBanner(info, item.groupType);
                }
            });
        }
    }

    /**
     * 开始轮播
     */
    public void startBannerPlay() {
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    /**
     * 停止轮播
     */
    public void stopBannerPlay() {
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    /**
     * 轮播图 点击事件
     */
    private void onClickBanner(DynamicValuesBean info, String groupType) {
        DynamicBodyBean item = gson.fromJson(info.contentBody, DynamicBodyBean.class);

        switch (info.referType) {
            case DynamicType.ALL_PRODUCT_DETAIL:
            case DynamicType.PRODUCT_DETAIL:
            case DynamicType.CARRIER_DETAIL:
            case DynamicType.PRODUCT_BY_DESIGNER:
            case DynamicType.MALL_DETAIL:
                if (item == null) return;
                CommodityDetailMallActivity.launch(mContext, item.sequenceNBR);
                break;
            case DynamicType.ACTIVITY_DETAIL://活动
                ActivityListActivity.launch(mContext);
                break;
            case DynamicType.TOPIC_ACTIVITY_DETAIL://专题
                if (item == null) return;
                SpecialActivityWebActivity.launch(mContext, item.webUrl);
                break;
            case DynamicType.THEME_DETAIL:
                if (item == null) return;
                //主题
                ThemeDetailActivity.launch(mContext, item.sequenceNBR);
                break;
            case DynamicType.DESIGNER_HOME:
                if (item == null) return;
                //设计师
                DesignerActivity.launch(mContext, item.userId);
                break;
            case DynamicType.MATERIAL_DETAIL:
            case DynamicType.MATERIAL_BY_DESIGNER:
                if (item == null) return;
                //素材
                MaterialDetailActivity.launch(mContext, item.sequenceNBR);
                break;
            case DynamicType.LINK_ADDRESS://网页
                WebActivity.launch(mContext, info.referName);
                break;
            case DynamicType.CATEGORY://导航商品列表
                NavigateProductListActivity.launchMain(mContext, groupType, info.GROUP_CODE, info.entityType, "contentBody", info._id);
                break;
            case DynamicType.BRAND_HOME://导航商品列表
                BrandActivity.launch(mContext, item.sequenceNBR);
                break;
        }
    }
}
