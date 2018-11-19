package com.zhaotai.uzao.ui.main.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.RecommendBean;
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
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.transform.CircleTransform;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;

import java.util.List;

/**
 * description:  首页列表adapter
 * author : zp
 * date: 2017/7/26
 */

public class RecommendAdapter extends BaseMultiItemQuickAdapter<RecommendBean, BaseViewHolder> {

    private RecommendHotThemeAdapter recommendHotThemeAdapter;
    private Banner mBanner;

    public RecommendAdapter(List<RecommendBean> data) {
        super(data);
        //轮播
        addItemType(RecommendBean.BANNERS, R.layout.item_main_banner);
        //       一日一设
        addItemType(RecommendBean.DAY_DESIGN, R.layout.item_fragment_recommend_day_design);
        //       热门主题
        addItemType(RecommendBean.HOT_THEME, R.layout.item_fragment_recommend_hot_theme);
        //       设计达人
        addItemType(RecommendBean.GOOD_DESIGNER, R.layout.item_fragment_recommend_good_designer);
        //       title
        addItemType(RecommendBean.TITLE, R.layout.item_fragment_recommend_title);
//
        addItemType(RecommendBean.END, R.layout.item_main_line);
        //       原创商品
        addItemType(RecommendBean.ORIGINAL_PRODUCT, R.layout.item_fragment_recommend_original_porduct);
        //       最新商品
        addItemType(RecommendBean.NEW_PRODUCT, R.layout.item_fragment_recommend_new_porduct);
        //       最新素材
        addItemType(RecommendBean.NEW_MATERIAL, R.layout.item_fragment_recommend_new_material);
    }

    @Override
    protected void convert(BaseViewHolder helper, final RecommendBean item) {
        switch (helper.getItemViewType()) {
            case RecommendBean.BANNERS:
//                初始化轮播图
                initBanners(helper, item);
                break;

            case RecommendBean.DAY_DESIGN:
//                初始化一日一设
                initDayDesign(helper, item);
                break;
            case RecommendBean.TITLE:
//                初始化标题
                helper.setText(R.id.tv_title, item.getTitle());
                break;

            case RecommendBean.HOT_THEME:
//                初始化热门主题
                initHotTheme(helper, item);
                break;
            case RecommendBean.GOOD_DESIGNER:
//                初始化设计达人
                initGoodDesigner(helper, item);
                break;

            case RecommendBean.ORIGINAL_PRODUCT:
//                初始化原创商品
                initOriginalProduct(helper, item);
                break;
            case RecommendBean.NEW_PRODUCT:
//                初始化最新商品
                initNewProduct(helper, item);
                break;
            case RecommendBean.NEW_MATERIAL:
//                初始化最新素材
                initNewMaterial(helper, item);
                break;
        }
    }

    /**
     * 初始化一日一设
     */
    private void initDayDesign(BaseViewHolder helper, final RecommendBean item) {

        ImageView ivDayDesign = helper.getView(R.id.iv_day_design);
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.valueBean.getImage(), ivDayDesign);
        ivDayDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DynamicType.CATEGORY.equals(item.valueBean.getReferType())) {
                    //此种类型 导航商品列表
                    NavigateProductListActivity.launchMain(mContext, item.groupType, item.valueBean.GROUP_CODE, item.valueBean.entityType, "contentBody", item.valueBean._id);
                } else {
                    onValueBeanClick(item.valueBean);
                }
            }
        });
    }

    /**
     * 初始化最新素材
     */
    private void initNewMaterial(BaseViewHolder helper, RecommendBean item) {
        final RecommendBean.ValueBean valueBean = item.valueBean;
        ImageView ivPic = helper.getView(R.id.iv_material_pic);
        ViewGroup.LayoutParams layoutParams = ivPic.getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext)/2-PixelUtil.dp2px(10));
        ivPic.setLayoutParams(layoutParams);
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onValueBeanClick(valueBean);
            }
        });
        if (valueBean != null) {
            String strContentBody = valueBean.getContentBody();
            if (strContentBody != null) {
                String referType = valueBean.getReferType();
                if ("themeDetail".equals(referType)) {
                    RecommendBean.ThemeContentBody contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.ThemeContentBody.class);
                    String thumbnail = contentBody.getCover();
                    if (thumbnail != null) {
                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivPic);
                    }
                    String name = contentBody.getName();
                    if (name != null) {
                        helper.setText(R.id.tv_material_name, name);
                    }
                    int viewCount = contentBody.getViewCount();
                    helper.setText(R.id.tv_product_visited, String.valueOf(viewCount));

                    int favoriteCount = contentBody.getFavoriteCount();
                    helper.setText(R.id.tv_product_collected, String.valueOf(favoriteCount));

                } else if ("materialDetail".equals(referType) || "materialByDesigner".equals(referType)) {
                    RecommendBean.MaterialContentBody contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.MaterialContentBody.class);
                    if (contentBody != null) {
                        String thumbnail = contentBody.getThumbnail();
                        if (thumbnail != null) {
                            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivPic);
                        }
                        String name = contentBody.getName();
                        if (name != null) {
                            helper.setText(R.id.tv_material_name, name);
                        }
                        int viewCount = contentBody.getViewCount();
                        helper.setText(R.id.tv_product_visited, String.valueOf(viewCount));

                        int favoriteCount = contentBody.getFavoriteCount();
                        helper.setText(R.id.tv_product_collected, String.valueOf(favoriteCount));
                    }
                }
            }
        }
    }

    /**
     * 初始化最新商品
     */
    private void initNewProduct(BaseViewHolder helper, RecommendBean item) {
        final RecommendBean.ValueBean valueBeen = item.valueBean;
        ImageView ivPic = helper.getView(R.id.iv_product_image);
        ViewGroup.LayoutParams layoutParams = ivPic.getLayoutParams();
        layoutParams.height = (int) (ScreenUtils.getScreenWidth(mContext)/2-PixelUtil.dp2px(10));
        ivPic.setLayoutParams(layoutParams);
        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onValueBeanClick(valueBeen);
            }
        });
        if (valueBeen != null) {
            if (!StringUtil.isEmpty(valueBeen.alias)) {
                helper.setText(R.id.tv_product_name, valueBeen.alias);
            }
            String referType = valueBeen.getReferType();
            //这几种类型是商品
            if ("allProductDetail".equals(referType) || "mallDetail".equals(referType) || "productDetail".equals(referType)) {
                String strContentBody = valueBeen.getContentBody();
                if (strContentBody != null) {
                    RecommendBean.ProductContentBody contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.ProductContentBody.class);
                    if (contentBody != null) {
                        String thumbnail = contentBody.getThumbnail();
                        if (thumbnail != null) {
                            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivPic);
                        }

                        String spuName = contentBody.getSpuName();
                        if (StringUtil.isEmpty(valueBeen.alias) && !StringUtil.isEmpty(spuName)) {
                            helper.setText(R.id.tv_product_name, spuName);
                        }
                    }
                }
            } else if ("themeDetail".equals(referType)) {
                //这是主体类型
                String strContentBody = valueBeen.getContentBody();
                if (strContentBody != null) {
                    RecommendBean.ThemeContentBody contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.ThemeContentBody.class);
                    if (contentBody != null) {
                        String thumbnail = contentBody.getCover();
                        if (thumbnail != null) {
                            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivPic);
                        }

                        String themeName = contentBody.getName();
                        if (StringUtil.isEmpty(valueBeen.alias) && !StringUtil.isEmpty(themeName)) {
                            helper.setText(R.id.tv_product_name, themeName);
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化原创商品
     */
    private void initOriginalProduct(BaseViewHolder helper, RecommendBean item) {
        final RecommendBean.ValueBean valueBeen = item.valueBean;

        if (valueBeen != null) {
            String referType = valueBeen.getReferType();
            if (!StringUtil.isEmpty(valueBeen.alias)) {
                helper.setText(R.id.tv_product_name, valueBeen.alias);
            }
            if ("allProductDetail".equals(referType) || "mallDetail".equals(referType) || "productDetail".equals(referType)) {
                String strContentBody = valueBeen.getContentBody();
                if (strContentBody != null) {
                    RecommendBean.ProductContentBody contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.ProductContentBody.class);
                    if (contentBody != null) {
                        String thumbnail = contentBody.getThumbnail();
                        if (thumbnail != null) {
                            ImageView ivPic = helper.getView(R.id.iv_product_image);
                            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivPic);
                            ivPic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onValueBeanClick(valueBeen);
                                }
                            });
                        }

                        String spuName = contentBody.getSpuName();
                        if (StringUtil.isEmpty(valueBeen.alias) && !StringUtil.isEmpty(spuName)) {
                            helper.setText(R.id.tv_product_name, spuName);
                        }
                        RecommendBean.ProductContentBody.DesignerBean designer = contentBody.getDesigner();
                        if (designer != null) {
                            helper.setText(R.id.tv_product_designer, "设计师:" + designer.nickName);
                        } else {
                            helper.setText(R.id.tv_product_designer, valueBeen.designer);
                        }
                    }
                }
            } else if ("themeDetail".equals(referType)) {
                String strContentBody = valueBeen.getContentBody();
                if (strContentBody != null) {
                    RecommendBean.ThemeContentBody contentBody = null;
                    try {
                        contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.ThemeContentBody.class);
                    } catch (Exception e) {
                        Log.e(TAG, "initOriginalProduct: ", e);
                    }
                    if (contentBody == null) {
                        return;
                    }
                    if (!StringUtil.isEmpty(valueBeen.alias)) {
                        helper.setText(R.id.tv_product_name, valueBeen.alias);
                    }
                    contentBody = GsonUtil.getGson().fromJson(strContentBody, RecommendBean.ThemeContentBody.class);
                    if (contentBody != null) {
                        String thumbnail = contentBody.getCover();
                        if (thumbnail != null) {
                            ImageView ivPic = helper.getView(R.id.iv_product_image);
                            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivPic);
                            ivPic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onValueBeanClick(valueBeen);
                                }
                            });
                        }

                        String themeName = contentBody.getName();
                        if (StringUtil.isEmpty(valueBeen.alias) && themeName != null) {
                            helper.setText(R.id.tv_product_name, themeName);
                        }
                        String designerName = contentBody.nickName;
                        if (designerName != null) {
                            helper.setText(R.id.tv_product_designer, "设计师:" + designerName);
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化设计达人
     */
    private void initGoodDesigner(BaseViewHolder helper, RecommendBean item) {
        final RecommendBean.ValueBean valueBean = item.valueBean;
        if (valueBean != null) {
            final String contentBody = valueBean.getContentBody();
            final RecommendBean.AuthorContentBody goodDesignerAuthorContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.AuthorContentBody.class);
            if (goodDesignerAuthorContentBody != null) {
                String nickName = goodDesignerAuthorContentBody.getNickName();
                if (nickName != null) {
                    helper.setText(R.id.tv_name, nickName);
                }
                int myDesignSpuCount = goodDesignerAuthorContentBody.getMyDesignSpuCount();
                helper.setText(R.id.tv_product_count, "已设计" + myDesignSpuCount + "件");

                String aboutMe = goodDesignerAuthorContentBody.getAboutMe();
                if (aboutMe != null) {
                    helper.setText(R.id.tv_about_me, aboutMe);
                }
                String avatar = goodDesignerAuthorContentBody.getAvatar();
                ImageView ivHead = helper.getView(R.id.iv_head);
                GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(avatar), ivHead, R.drawable.ic_default_head, R.drawable.ic_default_head, new CircleTransform(mContext));
                helper.addOnClickListener(R.id.iv_head);

                TextView tvAttention = helper.getView(R.id.tv_attention);
//                String isAttention = goodDesignerAuthorContentBody.getIsAttention();
//                System.out.println(item.inSidePos + "initGoodDesigner 状态是" + isAttention.toString());
                if ("Y".equals(goodDesignerAuthorContentBody.getIsAttention())) {
                    tvAttention.setSelected(true);
                    tvAttention.setText("已关注");
                } else {
                    tvAttention.setSelected(false);
                    tvAttention.setText("关注");
                }
                helper.addOnClickListener(R.id.tv_attention);
            }


            //设置第一个小图标
            RecommendBean.BaseContentBody baseContentBody1 = strToContentBody(valueBean.contentBody1, valueBean.referType1);
            if (baseContentBody1 != null) {
                if (baseContentBody1 instanceof RecommendBean.ProductContentBody) {
                    String thumbnail = ((RecommendBean.ProductContentBody) baseContentBody1).getThumbnail();
                    if (thumbnail != null) {
                        ImageView ivDesign1 = helper.getView(R.id.iv_design_1);
                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivDesign1);
                        ivDesign1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onValueBeanClick(valueBean.contentBody1, valueBean.referType1, null);
                            }
                        });
                    }
                } else if (baseContentBody1 instanceof RecommendBean.MaterialContentBody) {
                    String thumbnail = ((RecommendBean.MaterialContentBody) baseContentBody1).getThumbnail();
                    if (thumbnail != null) {
                        ImageView ivDesign1 = helper.getView(R.id.iv_design_1);
                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivDesign1);
                        ivDesign1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onValueBeanClick(valueBean.contentBody1, valueBean.referType1, null);
                            }
                        });
                    }
                }

            }
            //设置第二个小图标
            ImageView ivDesign2 = helper.getView(R.id.iv_design_2);
            RecommendBean.BaseContentBody baseContentBody2 = strToContentBody(valueBean.contentBody2, valueBean.referType2);
            if (baseContentBody2 != null) {

                if (baseContentBody2 instanceof RecommendBean.ProductContentBody) {
                    String thumbnail = ((RecommendBean.ProductContentBody) baseContentBody2).getThumbnail();
                    if (thumbnail != null) {
                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivDesign2);
                        ivDesign2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onValueBeanClick(valueBean.contentBody2, valueBean.referType2, null);
                            }
                        });
                    }
                } else if (baseContentBody2 instanceof RecommendBean.MaterialContentBody) {
                    String thumbnail = ((RecommendBean.MaterialContentBody) baseContentBody2).getThumbnail();
                    if (thumbnail != null) {
                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivDesign2);
                        ivDesign2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onValueBeanClick(valueBean.contentBody2, valueBean.referType2, null);
                            }
                        });
                    }
                }

            }
            //设置第三个图标
            ImageView ivDesign3 = helper.getView(R.id.iv_design_3);
            RecommendBean.BaseContentBody baseContentBody3 = strToContentBody(valueBean.contentBody3, valueBean.referType3);
            if (baseContentBody3 != null) {

                if (baseContentBody3 instanceof RecommendBean.ProductContentBody) {
                    String thumbnail = ((RecommendBean.ProductContentBody) baseContentBody3).getThumbnail();
                    if (thumbnail != null) {
                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivDesign3);
                        ivDesign3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onValueBeanClick(valueBean.contentBody3, valueBean.referType3, null);
                            }
                        });
                    }
                } else if (baseContentBody3 instanceof RecommendBean.MaterialContentBody) {
                    String thumbnail = ((RecommendBean.MaterialContentBody) baseContentBody3).getThumbnail();
                    if (thumbnail != null) {
                        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(thumbnail), ivDesign3);
                        ivDesign3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onValueBeanClick(valueBean.contentBody3, valueBean.referType3, null);
                            }
                        });
                    }
                }
            }
            //设置尾部
            if (item.getContentType() == 2) {
                helper.addOnClickListener(R.id.rl_more);
                helper.getView(R.id.rl_more).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.rl_more).setVisibility(View.GONE);
            }
        }

    }

    /**
     * 初始化热门主题
     */
    private void initHotTheme(BaseViewHolder helper, RecommendBean item) {
        RecyclerView recyclerView = helper.getView(R.id.rc_hot_theme);
        if (recommendHotThemeAdapter == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            recommendHotThemeAdapter = new RecommendHotThemeAdapter();
            recommendHotThemeAdapter.setNewData(item.valueBeans);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(0, (int) PixelUtil.dp2px(9)));
            recommendHotThemeAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    RecommendBean.ValueBean valueBean = recommendHotThemeAdapter.getData().get(position);
                    onValueBeanClick(valueBean);
                }
            });
        }
        helper.addOnClickListener(R.id.rl_theme_more);
        recyclerView.setAdapter(recommendHotThemeAdapter);
    }

    /**
     * 初始化banner
     */
    private void initBanners(BaseViewHolder helper, final RecommendBean item) {
        if (item.valueBeans != null && item.valueBeans.size() > 0) {

            //初始化banner
            mBanner = helper.getView(R.id.main_banner);
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片集合
            mBanner.setImages(item.valueBeans);
            //设置图片加载器
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    RecommendBean.ValueBean valuesBean = (RecommendBean.ValueBean) path;
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + valuesBean.getImage(), imageView);
                }
            });
            //设置自动轮播，默认为true
            mBanner.isAutoPlay(true);
            //设置轮播时间
            mBanner.setDelayTime(3000);
            //banner设置方法全部调用完毕时最后调用
            mBanner.start();
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    RecommendBean.ValueBean valueBean = item.valueBeans.get(position);
                    if (DynamicType.CATEGORY.equals(valueBean.getReferType())) {
                        NavigateProductListActivity.launchMain(mContext, item.groupType, valueBean.GROUP_CODE, valueBean.entityType, "contentBody", valueBean._id);
                    } else {
                        onValueBeanClick(valueBean);
                    }

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
     * 根据Bean类型跳转
     *
     * @param valueBean valueBean Adapter包装类
     */
    private void onValueBeanClick(RecommendBean.ValueBean valueBean) {
        String contentBody = valueBean.getContentBody();
        String referType = valueBean.getReferType();
        if (referType == null) {
            return;
        }
        onValueBeanClick(contentBody, referType, valueBean);

    }

    /**
     * 点击根据type类型
     *
     * @param contentBody contentBody的json串
     * @param referType   类型
     * @param valueBean   bean
     */
    private void onValueBeanClick(String contentBody, String referType, RecommendBean.ValueBean valueBean) {
        System.out.println("点击了 点击的类型是" + referType);
        switch (referType) {
            case DynamicType.ALL_PRODUCT_DETAIL://所有商品
            case DynamicType.MALL_DETAIL://成品
            case DynamicType.PRODUCT_DETAIL://设计商品
            case DynamicType.CARRIER_DETAIL://载体
            case DynamicType.PRODUCT_BY_DESIGNER://根据设计师获取设计师商品
            case DynamicType.PRODUCT_BY_CARRIER://根据载体获取设计商品
                RecommendBean.ProductContentBody mProductBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.ProductContentBody.class);
                String productSequenceNBR = mProductBody.getSequenceNBR();
                CommodityDetailMallActivity.launch(mContext, productSequenceNBR);
                break;
            case DynamicType.DESIGNER_HOME://设计师主页
                RecommendBean.AuthorContentBody authorContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.AuthorContentBody.class);
                String userId = authorContentBody.getUserId();
                DesignerActivity.launch(mContext, userId);
                break;
            case DynamicType.TOPIC_ACTIVITY_DETAIL://专题
                RecommendBean.TopicContentContentBody topicContentContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.TopicContentContentBody.class);
                if (topicContentContentBody != null) {
                    SpecialActivityWebActivity.launch(mContext, topicContentContentBody.getWebUrl());
                } else {
                    ToastUtil.showShort("数据异常");
                }
                break;
            case DynamicType.BRAND_HOME://品牌
                RecommendBean.BrandContentBody brandContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.BrandContentBody.class);
                if (brandContentBody != null) {
                    BrandActivity.launch(mContext, brandContentBody.getSequenceNBR());
                }
                break;
            case DynamicType.THEME_DETAIL://主题
                RecommendBean.ThemeContentBody themeContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.ThemeContentBody.class);
                String themeContentBodySequenceNBR = themeContentBody.getSequenceNBR();
                if (themeContentBodySequenceNBR != null) {
                    ThemeDetailActivity.launch(mContext, themeContentBodySequenceNBR);
                }
                break;
            case DynamicType.MATERIAL_DETAIL://素材
            case DynamicType.MATERIAL_BY_DESIGNER://素材
                RecommendBean.MaterialContentBody materialContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.MaterialContentBody.class);
                String materialId = materialContentBody.getSequenceNBR();
                if (materialId != null) {
                    MaterialDetailActivity.launch(mContext, materialId);
                }
                break;
            case DynamicType.ACTIVITY_DETAIL://活动
                ActivityListActivity.launch(mContext);
                break;
            case DynamicType.LINK_ADDRESS://网页
                WebActivity.launch(mContext, valueBean.getReferName());
                break;

//            case DynamicType.CATEGORY:
//                NavigateProductListActivity.launchMain(mContext, "TABLE", valueBean.GROUP_CODE, valueBean.entityType, "contentBody", valueBean._id);
//                break;
        }
    }


    /**
     * 根据类型解析str 返回基类
     */
    private RecommendBean.BaseContentBody strToContentBody(String contentBody, String referType) {
        System.out.println("点击了 点击的类型是" + referType);
        RecommendBean.BaseContentBody baseContentBody = null;
        switch (referType) {
            case DynamicType.ALL_PRODUCT_DETAIL://所有商品
            case DynamicType.MALL_DETAIL://成品
            case DynamicType.PRODUCT_DETAIL://设计商品
            case DynamicType.CARRIER_DETAIL://载体
            case DynamicType.PRODUCT_BY_DESIGNER://根据设计师获取设计师商品
            case DynamicType.PRODUCT_BY_CARRIER://根据载体获取设计商品
                baseContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.ProductContentBody.class);
                break;
            case DynamicType.DESIGNER_HOME://设计师主页
                RecommendBean.AuthorContentBody authorContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.AuthorContentBody.class);
                String userId = authorContentBody.getUserId();
                DesignerActivity.launch(mContext, userId);
                break;
            case DynamicType.TOPIC_ACTIVITY_DETAIL://专题
                break;
            case DynamicType.BRAND_HOME://品牌
                baseContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.BrandContentBody.class);
                break;
            case DynamicType.THEME_DETAIL://主题
                baseContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.ThemeContentBody.class);
                break;
            case DynamicType.MATERIAL_DETAIL://素材
            case DynamicType.MATERIAL_BY_DESIGNER://素材
                baseContentBody = GsonUtil.getGson().fromJson(contentBody, RecommendBean.MaterialContentBody.class);
                break;
        }
        return baseContentBody;
    }
}
