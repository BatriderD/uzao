package com.zhaotai.uzao.ui.category.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.youth.banner.Banner;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.PropertyAdapter;
import com.zhaotai.uzao.adapter.RecommendSpuAdapter;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.ActivityModelBean;
import com.zhaotai.uzao.bean.CommentBean;
import com.zhaotai.uzao.bean.GoodsBean;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.MyTrackRequestBean;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.PropertyBean;
import com.zhaotai.uzao.bean.ShoppingCartBean;
import com.zhaotai.uzao.bean.ShoppingGoodsBean;
import com.zhaotai.uzao.bean.ShoppingPropertyBean;
import com.zhaotai.uzao.bean.TagBean;
import com.zhaotai.uzao.listener.ItemChangeListenerInterface;
import com.zhaotai.uzao.ui.brand.BrandActivity;
import com.zhaotai.uzao.ui.category.goods.contract.CommodityDetailContract;
import com.zhaotai.uzao.ui.category.goods.presenter.CommodityDetailPresenter;
import com.zhaotai.uzao.ui.design.activity.BlenderDesignActivity;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.order.activity.ConfirmOrderActivity;
import com.zhaotai.uzao.ui.poster.PosterActivity;
import com.zhaotai.uzao.ui.shopping.activity.ShoppingCartActivity;
import com.zhaotai.uzao.ui.theme.activity.AddThemeActivity;
import com.zhaotai.uzao.utils.GlideImageLoader;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GlideUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.view.MyScrollView;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/13
 * Created by LiYou
 * Description : 商品详情
 */

public class CommodityDetailMallActivity extends BaseActivity implements CommodityDetailContract.View, View.OnClickListener {

    private CommodityDetailPresenter mPresenter;
    //属性id
    private String skuId;
    //商品id
    private String spuId;
    //是否收藏
    private boolean isCollect = false;
    @BindView(R.id.right_btn)
    ImageView mRightBtn;
    //商品名
    @BindView(R.id.detail_goods_name)
    TextView mGoodsName;
    //价格
    @BindView(R.id.detail_goods_price)
    TextView mPrice;
    @BindView(R.id.my_scrollview)
    MyScrollView mScrollView;
    //轮播图
    @BindView(R.id.detail_banner)
    Banner mBanner;
    //商品基本信息布局
    @BindView(R.id.goods_base_info_ll)
    LinearLayout mLayoutGoodsBaseInfo;

    //收藏按钮
    @BindView(R.id.detail_collect_product)
    ImageView mCollection;

    //规格属性
    @BindView(R.id.detail_choose_type_num)
    TextView mChooseTypeNum;
    @BindView(R.id.tv_goods_detail_comment_left)
    TextView mCommentTabLeftText;
    @BindView(R.id.tv_goods_detail_comment_right)
    TextView mCommentTabRightText;
    //评论用户头像
    @BindView(R.id.tv_goods_detail_comment_pic)
    ImageView mCommentUserPic;
    //评论用户昵称
    @BindView(R.id.tv_goods_detail_comment_name)
    TextView mCommentUserName;
    //评论时间
    @BindView(R.id.tv_goods_detail_comment_time)
    TextView mCommentTime;
    //评论内容
    @BindView(R.id.tv_goods_detail_comment_content)
    TextView mCommentContent;
    @BindView(R.id.ll_goods_detail_comment_list)
    LinearLayout mLlCommentList;

    //品牌
    @BindView(R.id.tv_brand)
    TextView mBrand;
    @BindView(R.id.ll_brand)
    LinearLayout mLlBrand;
    @BindView(R.id.ll_designer)
    LinearLayout mLlDesigner;
    //设计师名字
    @BindView(R.id.tv_designer)
    TextView mDesignerName;
    //设计师理念
    @BindView(R.id.tv_goods_detail_designer_idea)
    TextView mDesignIdea;
    @BindView(R.id.btn_custom_design)
    ImageView mCustomDesign;

    @BindView(R.id.iv_share)
    ImageView iv_share;

    //推荐title
    @BindView(R.id.fl_recommend_title)
    FrameLayout mFlRecommendTitle;
    @BindView(R.id.recycler_recommend)
    RecyclerView mRecyclerRecommend;
    //商品详情
    @BindView(R.id.ll_content_detail)
    LinearLayout mLlcontent;
    //标签
    @BindView(R.id.ll_mall_tag)
    LinearLayout mLlTag;

    private int select_type;
    private static final int SELECT_NON = 231;
    private static final int SELECT_BUY = 232;
    private static final int SELECT_CART = 233;
    private static final int SELECT_DESIGN = 234;//定制

    private GoodsDetailMallBean data;
    private PropertyAdapter mPropertyAdapter;
    private UIBottomSheet uiBottomSheet;
    private boolean isTemplate = false;//是否模板商品

    /**
     * 成品 商品
     */
    private static final String TYPE_MALL = "mall";
    /**
     * 设计 商品
     */
    private static final String TYPE_DESIGN = "design";
    private String goodsId;
    private String designType;
    private PopupWindow mMorePopWindow;
    private String goodsType;//商品类型
    private String designerId;//设计师Id
    private String brandId;//品牌Id

    /**
     * 商品id
     *
     * @param context context
     * @param id      商品id
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, CommodityDetailMallActivity.class);
        intent.putExtra("GoodsId", id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_commodity_detail_mall);
        mTitle.setText("商品详情");
        mToolbar.getBackground().mutate().setAlpha(0);
        mTitle.setAlpha(0);
        mScrollView.setOnScrollChangedListener(new MyScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(NestedScrollView who, int l, int t, int oldl, int oldt) {
                int alpha = (int) (((float) t / 1200) * 255);
                float textAlpha = (float) t / 1200;
                if (textAlpha >= 1) {
                    textAlpha = 1;
                }
                if (textAlpha <= 0.1) {
                    textAlpha = 0;
                }
                if (alpha >= 255) {
                    alpha = 255;
                }
                if (alpha <= 10) {
                    alpha = 0;
                }

                mToolbar.getBackground().mutate().setAlpha(alpha);
                mTitle.setAlpha(textAlpha);
            }
        });

        initSkuPropertyPop();
        mRecyclerRecommend.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerRecommend.setNestedScrollingEnabled(false);
        mRecyclerRecommend.addItemDecoration(new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3)));
    }

    @Override
    public void handleStatusBar() {
        setWhiteState();
    }

    /**
     * 设置成透明的状态栏
     */
    private void setWhiteState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window mWindow = getWindow();
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mWindow.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            mWindow.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //属性 图片
    private ImageView mPopImage;
    //属性 价格
    private TextView mPopPrice;
    // sku
    private RecyclerView mPopRecycler;

    //数量
    private EditText mPopNumber;

    //购买数量
    private int buyNum = 1;

    /**
     * 初始化属性窗口
     */
    private void initSkuPropertyPop() {
        View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.pop_commodity_detail_type, null);
        //属性 图片
        mPopImage = (ImageView) bottomSheetView.findViewById(R.id.detail_type_img);
        //属性 价格
        mPopPrice = (TextView) bottomSheetView.findViewById(R.id.detail_type_price);
        //属性 确定
        bottomSheetView.findViewById(R.id.detail_type_ok).setOnClickListener(this);
        bottomSheetView.findViewById(R.id.detail_type_back).setOnClickListener(this);
        //sku
        mPopRecycler = (RecyclerView) bottomSheetView.findViewById(R.id.pop_goods_detail_recycler);
        mPopRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        uiBottomSheet = new UIBottomSheet(mContext);
        uiBottomSheet.setContentView(bottomSheetView);

    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new CommodityDetailPresenter(this, this);
        }
        //获取商品详情
        showLoading();
        Intent intent = getIntent();
        if (intent != null) {
            goodsId = intent.getStringExtra("GoodsId");
            mPresenter.getDetail(goodsId);
            mPresenter.isCollection(goodsId);
            mPresenter.getRecommendSpu(goodsId);
        }
        skuId = "";
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    private UMShareListener shareListener = new UMShareListener() {
        /**
         *  SHARE_MEDIA 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         *  分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(CommodityDetailMallActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         *  分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(CommodityDetailMallActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         *  分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(CommodityDetailMallActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                //根据key来区分自定义按钮的类型，并进行对应的操作
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_poster")) {
                    PosterActivity.launchSpu(CommodityDetailMallActivity.this, spuId);
                }
            }
        }
    };

    /**
     * 打开分享面板
     */
    @Override
    public void openShareBoard(boolean hasPoster) {
        SnsPlatform snsPlatform = SHARE_MEDIA.createSnsPlatform("umeng_sharebutton_poster", "umeng_sharebutton_poster", "ic_share_poster", "ic_share_poster", 0);
        ShareAction shareAction = new ShareAction(this)
                .withText("uzao")
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setShareboardclickCallback(shareBoardlistener)
                .setCallback(shareListener);
        if (hasPoster) {
            Class aClass = shareAction.getClass();
            try {
                java.lang.reflect.Field field = aClass.getDeclaredField("platformlist");
                field.setAccessible(true);
                List<SnsPlatform> platformlist = (List<SnsPlatform>) field.get(shareAction);
                platformlist.add(0, snsPlatform);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                shareAction.open();
            }
        } else {
            shareAction.open();
        }

    }


    @OnClick(R.id.iv_share)
    public void onShare() {
        if (LoginHelper.getLoginStatus()) {
            mPresenter.hasPoster();
        } else {
            LoginHelper.goLogin(this);
        }
    }

    /**
     * 侧边栏
     */
    @OnClick(R.id.right_btn)
    public void onClickRightBtn() {
        if (mMorePopWindow == null) {
            View itemView = View.inflate(mContext, R.layout.pop_goods_detail_tab_item, null);
            LinearLayout mService = (LinearLayout) itemView.findViewById(R.id.ll_pop_custom_service);
            LinearLayout mTheme = (LinearLayout) itemView.findViewById(R.id.ll_pop_add_theme);
            mMorePopWindow = new PopupWindow(itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mMorePopWindow.setFocusable(true);
            mMorePopWindow.setOutsideTouchable(true);
            mMorePopWindow.setBackgroundDrawable(new ColorDrawable());
            mMorePopWindow.showAsDropDown(mRightBtn);

            //客服
            mService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginIm();
                    mMorePopWindow.dismiss();
                }
            });
            //添加主题
            mTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddThemeActivity.launch(mContext, goodsId, AddThemeActivity.TYPE_SPU);
                    mMorePopWindow.dismiss();
                }
            });
        } else {
            mMorePopWindow.showAsDropDown(mRightBtn);
        }
    }

    /**
     * 显示商品详情
     *
     * @param data 数据
     */
    @Override
    public void showDetail(final GoodsDetailMallBean data) {
        this.data = data;
        goodsType = data.basicInfo.spuModel.spuType;
        //添加购物车时用到
        spuId = data.basicInfo.sequenceNBR;
        //模型类型
        designType = data.basicInfo.designType;
        //是否模板商品
        if (data.basicInfo.isTemplate != null && data.basicInfo.isTemplate.equals("Y")) {
            isTemplate = true;
        }
        //商品轮播图
        List<String> image = new ArrayList<>();
        List<String> spuImages = data.basicInfo.spuImages;

        //商品价格
        mPrice.setText(getString(R.string.account, data.basicInfo.spuModel.displayPriceY));
        switch (goodsType) {
            case TYPE_DESIGN:
                for (String img : spuImages) {
                    image.add(ApiConstants.UZAOCHINA_IMAGE_HOST + img);
                }
                break;
            case "sample":
                for (String img : data.basicInfo.spuImages) {
                    image.add(ApiConstants.UZAOCHINA_IMAGE_HOST + img);
                }
                break;
            case TYPE_MALL:
                for (String img : data.basicInfo.spuImages) {
                    image.add(ApiConstants.UZAOCHINA_IMAGE_HOST + img);
                }
                break;
        }
        //品牌
        if ("design".equals(goodsType)) {
            if (data.sampleSpu != null && data.sampleSpu.brandId != null) {
                brandId = data.sampleSpu.brandId;
                mLlBrand.setVisibility(View.VISIBLE);
                mBrand.setText("品牌:    " + data.sampleSpu.brandName);
            }
        } else {
            if (data.basicInfo.spuModel.brandId != null) {
                brandId = data.basicInfo.spuModel.brandId;
                mLlBrand.setVisibility(View.VISIBLE);
                mBrand.setText("品牌:    " + data.basicInfo.spuModel.brandName);
            }
        }
        //类型:productType 1有品牌有设计师，2有品牌无设计师，3无品牌有设计师
        if (data.designer == null) {
            if (data.basicInfo.spuModel.assignDesigner != null) {
                //有设计师
                designerId = data.basicInfo.spuModel.designerId;
                //设计师名字
                mDesignerName.setText("设计师:    " + data.basicInfo.spuModel.assignDesigner.nickName);
            } else {
                //无设计师
                mLlDesigner.setVisibility(View.GONE);
            }

        } else {
            //有设计师
            designerId = data.designer.userId;
            //设计师名字
            mDesignerName.setText("设计师:    " + data.designer.nickName);
        }

        //设计师理念
        if (StringUtil.isEmpty(data.basicInfo.designIdea)) {
            mDesignIdea.setVisibility(View.GONE);
        } else {
            mDesignIdea.setVisibility(View.VISIBLE);
            mDesignIdea.setText(data.basicInfo.designIdea);
        }


        //标签
        List<TagBean> tags = data.basicInfo.tags;
        if (tags != null && tags.size() > 0) {
            for (final TagBean tag : tags) {
                TextView tagView = new TextView(mContext);
                tagView.setPadding((int) PixelUtil.dp2px(6), 0, (int) PixelUtil.dp2px(6), 0);
                tagView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_bg_tag));
                tagView.setTextColor(Color.parseColor("#fc5900"));
                tagView.setTextSize(9);
                tagView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMarginEnd(8);
                tagView.setLayoutParams(params);
                tagView.setText(tag.tagName);
                tagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryListActivity.launch(mContext, tag.tagCode, tag.tagName);
                    }
                });
                mLlTag.addView(tagView);
            }
        }

        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(image);
        mBanner.isAutoPlay(false);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

        //商品名
        mGoodsName.setText(data.basicInfo.spuModel.spuName);
        //商品详情
        for (int i = 0; i < data.detailContent.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            Glide.with(this).load(ApiConstants.UZAOCHINA_IMAGE_HOST + data.detailContent.get(i)).into(imageView);
            mLlcontent.addView(imageView);
        }

        //商品基本信息
        //尺码规格  颜色 数据 列表
        List<PropertyBean> skuProperty = new ArrayList<>();
        for (PropertyBean properties : data.properties) {
            StringBuilder content = new StringBuilder();
            String title;
            View view = View.inflate(this, R.layout.vw_goods_base_properties, null);
            //属性名
            TextView propertiesTitle = (TextView) view.findViewById(R.id.goods_properties_title);
            //属性内容
            TextView propertiesContent = (TextView) view.findViewById(R.id.goods_properties_content);
            title = properties.propertyTypeName;

            //提取sku数据
            if ("Y".equals(properties.isSku)) {
                skuProperty.add(properties);
            }

            for (PropertyBean property : properties.spuProperties) {
                content.append(property.propertyValue).append(" ");
            }
            propertiesTitle.setText(title);
            propertiesContent.setText(content.toString());
            mLayoutGoodsBaseInfo.addView(view);
        }

        //pop照片  mPopImage
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(data.basicInfo.spuModel.thumbnail), mPopImage);

        //pop价格
        mPopPrice.setText(getString(R.string.account, data.basicInfo.spuModel.displayPriceY));
        //pop 颜色
        mPropertyAdapter = new PropertyAdapter(skuProperty);

        //数量
        View footView = View.inflate(mContext, R.layout.foot_detail_property, null);
        mPropertyAdapter.addFooterView(footView);
        //pop 尺码
        //购买数量
        mPopNumber = (EditText) footView.findViewById(R.id.detail_type_num);
        StringUtil.setEditTextSection(mPopNumber);
        mPopNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    buyNum = Integer.valueOf(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (buyNum == 0) {
                    mPopNumber.setText("1");
                }
            }
        });
        //数量 减少
        footView.findViewById(R.id.detail_type_sub).setOnClickListener(this);
        //数量 增加
        footView.findViewById(R.id.detail_type_add).setOnClickListener(this);
        mPopRecycler.setAdapter(mPropertyAdapter);

        MyTrackRequestBean myTrackRequestBean = new MyTrackRequestBean();
        myTrackRequestBean.setEntityId(goodsId);
        myTrackRequestBean.setPrice(data.basicInfo.spuModel.displayPrice);
        myTrackRequestBean.setEntityType("Spu");
        if (TYPE_DESIGN.equals(goodsType)) {
            if (spuImages.size() > 0) {
                myTrackRequestBean.setThumbnail(spuImages.get(0));
            }
        } else {
            if (data.basicInfo.spuImages.size() > 0) {
                myTrackRequestBean.setThumbnail(data.basicInfo.spuImages.get(0));
            }
        }
        myTrackRequestBean.setEntityName(data.basicInfo.spuModel.spuName);
        mPresenter.addMyTrack(myTrackRequestBean);

        mPropertyAdapter.setListener(new ItemChangeListenerInterface() {

            @Override
            public void OnItemChange() {
                GoodsDetailMallBean.Sku sku = mPresenter.getSku(mPropertyAdapter, data);
                if (sku != null) {
                    GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(sku.thumbnail), mPopImage);
                    mPopPrice.setText(sku.priceY);
                }
            }
        });
    }


    @Override
    public void hasCollect() {
        isCollect = true;
        mCollection.setImageResource(R.drawable.ic_collectd);
    }

    @Override
    public void unCollect() {
        isCollect = false;
        mCollection.setImageResource(R.drawable.ic_un_collect);
    }

    @Override
    public void showProgressDialog() {
        showLoadingDialog();
    }

    @Override
    public void stopProgressDialog() {
        disMisLoadingDialog();
    }

    /**
     * 跳转设计师主页
     */
    @OnClick(R.id.ll_designer)
    public void gotoDesignerHomePage() {
        //设计师主页
        if (!StringUtil.isEmpty(designerId)) {
            DesignerActivity.launch(mContext, designerId);
        }
    }

    /**
     * 品牌主页
     */
    @OnClick(R.id.ll_brand)
    public void onClickToBrand() {
        if (!StringUtil.isEmpty(brandId)) {
            BrandActivity.launch(mContext, brandId);
        }
    }

    /**
     * 跳转购物车
     */
    @OnClick(R.id.detail_goto_shopping_cart)
    public void gotoShoppingCart() {
        if (LoginHelper.getLoginStatus()) {
            ShoppingCartActivity.launch(mContext);
        } else {
            LoginHelper.goLogin(mContext);
        }
    }

    /**
     * 收藏商品
     */
    @OnClick(R.id.detail_collect_product)
    public void collectProduct() {
        //判断 收藏 还是取消收藏
        if (isCollect) {
            mPresenter.deleteCollect(spuId);
        } else {
            mPresenter.collectProduct(spuId);
        }
    }

    /**
     * 评价界面
     */
    @OnClick(R.id.ll_goods_detail_comment_list)
    public void commentList() {
        CommentListActivity.launch(mContext, spuId);
    }

    @OnClick(R.id.tv_goods_detail_comment_right)
    public void commentItem() {
        CommentListActivity.launch(mContext, spuId);
    }

    /**
     * 定制
     */
    @OnClick(R.id.btn_custom_design)
    public void onClickCustomDesign() {
        if (data != null) {
            //2d
            if ("2d".equals(designType)) {
                mPresenter.getMku(spuId, skuId, data);
            } else {
                //3d
                if (data.basicInfo.spuModel.spuType.equals("design")) {
                    BlenderDesignActivity.launch(mContext, data.basicInfo.sampleId);
                } else {
                    BlenderDesignActivity.launch(mContext, spuId);
                }
            }
        }

    }

    /**
     * 打开选择规格属性
     */
    @OnClick(R.id.ll_detail_choose_type)
    public void showPopFromBottom() {
        select_type = SELECT_NON;
        uiBottomSheet.show();
    }

    /**
     * 打开选择规格属性
     */
    @Override
    public void showBottomSheetToCustomDesign() {
        ToastUtil.showShort("请先选择要设计商品的规格");
        select_type = SELECT_DESIGN;
        uiBottomSheet.show();
    }

    /**
     * 显示隐藏 定制按钮
     *
     * @param visibility 是否显示
     */
    @Override
    public void setVisibilityCustomDesign(boolean visibility) {
        if (visibility) {
            mCustomDesign.setVisibility(View.VISIBLE);
        } else {
            mCustomDesign.setVisibility(View.GONE);
        }
    }

    /**
     * 登录im
     */
    private void loginIm() {
        showProgressDialog();
        final Map<String, String> map = new ArrayMap<>();
        map.put(Field.PHONE, LoginHelper.getLoginId());

        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    final JSONObject jsonObject = SafeJson.parseObj(result);
                    int resultCode = SafeJson.safeInt(jsonObject, "error");
                    if (resultCode == 0) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                        if (userObj != null) {
                            String userToken = userObj.getString(Field.USERTOKEN);
                            int id = userObj.getInt(Field.ID);
                            SPUtils.saveUserToken(userToken);
                            SPUtils.saveUserId(id);
                            disMisLoadingDialog();
                            mContext.startActivity(new Intent(mContext, KF5ChatActivity.class));
                        }
                    } else if (resultCode == 10050) {
                        createImUser(mContext, map);
                    }
                } catch (JSONException e) {
                    disMisLoadingDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                disMisLoadingDialog();
            }
        });
    }

    private void createImUser(final Context context, Map<String, String> map) {
        //用户不存在
        UserInfoAPI.getInstance().createUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result1) {
                final JSONObject jsonObject = SafeJson.parseObj(result1);
                int resultCode1 = SafeJson.safeInt(jsonObject, "error");
                try {
                    if (resultCode1 == 0) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                        if (userObj != null) {
                            String userToken = userObj.getString(Field.USERTOKEN);
                            int id = userObj.getInt(Field.ID);
                            SPUtils.saveUserToken(userToken);
                            SPUtils.saveUserId(id);
                            disMisLoadingDialog();
                            context.startActivity(new Intent(context, KF5ChatActivity.class));
                        }
                    }
                } catch (JSONException e) {
                    disMisLoadingDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                disMisLoadingDialog();
            }
        });
    }

    /**
     * 显示评论
     */
    @Override
    public void showComment(CommentBean commentBean) {
        //评价
        if (commentBean != null) {
            mCommentTabLeftText.setText(getString(R.string.comment_num, commentBean.totalCount));
            mCommentTabRightText.setText("查看全部");
            Gson gson = new Gson();
            PersonBean personInfo = gson.fromJson(commentBean.userInfo, PersonBean.class);
            GlideUtil.getInstance().loadCircleImage(mContext, mCommentUserPic, ApiConstants.UZAOCHINA_IMAGE_HOST + personInfo.avatar);
            mCommentUserName.setText(personInfo.nickName);
            mCommentTime.setText(TimeUtils.millis2String(Long.parseLong(commentBean.recDate)));
            mCommentContent.setText(commentBean.commentBody);
        } else {
            mCommentTabLeftText.setText("备茶，上座，快来抢沙发");
            mLlCommentList.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRecommendSpu(List<GoodsBean> goodsBean) {
        mFlRecommendTitle.setVisibility(goodsBean.isEmpty() ? View.GONE : View.VISIBLE);
        RecommendSpuAdapter recommendSpuAdapter = new RecommendSpuAdapter();
        recommendSpuAdapter.setNewData(goodsBean);
        mRecyclerRecommend.setAdapter(recommendSpuAdapter);
        recommendSpuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean item = (GoodsBean) adapter.getItem(position);
                if (item != null)
                    CommodityDetailMallActivity.launch(mContext, item.sequenceNBR);
            }
        });
    }

    @Override
    public void hasShowEmptyView() {
        ImageView emptyBack = (ImageView) findViewById(R.id.iv_tool_back);
        if (emptyBack != null)
            emptyBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    /**
     * 加入购物车
     */
    @OnClick(R.id.add_shopping_cart)
    public void addShoppingCart() {
        if (mPresenter.isLogin()) return;
        if (isTemplate) {
            ToastUtil.showShort("模板商品不能加入购物车");
            return;
        }
        if (StringUtil.isEmpty(mChooseTypeNum.getText().toString())) {
            select_type = SELECT_CART;
            uiBottomSheet.show();
        } else {
            if (spuId != null && skuId != null) {
                mPresenter.addShoppingCart(new ShoppingPropertyBean(spuId, skuId, buyNum));
            }
        }
    }


    /**
     * 立即购买
     */
    @OnClick(R.id.buy_now)
    public void buyNow() {
        if (mPresenter.isLogin()) return;
        if (StringUtil.isEmpty(mChooseTypeNum.getText().toString())) {
            select_type = SELECT_BUY;
            uiBottomSheet.show();
        } else {
            goToConfirmOrderActivity();
        }
    }

    /**
     * 确认订单
     */
    public void goToConfirmOrderActivity() {
        if (spuId != null && skuId != null) {
            //跳转确认订单页面
            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
            ShoppingCartBean.Cart cart = new ShoppingCartBean.Cart();
            List<ShoppingCartBean.Cart> cartList = new ArrayList<>();
            List<ShoppingGoodsBean> list = new ArrayList<>();
            ShoppingGoodsBean shoppingGoodsBean = new ShoppingGoodsBean();

            //商品单价
            int unitPrice = 0;
            for (GoodsDetailMallBean.Sku sku : data.skus) {
                if (skuId.equals(sku.sequenceNBR)) {
                    shoppingGoodsBean.spuPic = sku.thumbnail;//图片
                    shoppingGoodsBean.spuName = data.basicInfo.spuModel.spuName;
                    shoppingGoodsBean.count = String.valueOf(buyNum);
                    shoppingGoodsBean.spuId = sku.spuId;
                    shoppingGoodsBean.skuId = skuId;
                    shoppingGoodsBean.properties = sku.skuValues;
                    if (sku.price != null && !sku.price.isEmpty()) {
                        unitPrice = Integer.valueOf(sku.price);
                    }
                    shoppingGoodsBean.unitPrice = sku.price;
                }
            }

            list.add(shoppingGoodsBean);

            cart.cartModels = list;
            cartList.add(cart);
            shoppingCartBean.carts = cartList;
            //没活动
            if (data.activityInfo == null) {
                shoppingCartBean.finalPrice = String.valueOf(unitPrice * buyNum);
                shoppingCartBean.preferentiaPrice = "0";
            } else {
                ActivityModelBean activityModelBean = new ActivityModelBean();

                //有活动计算优惠价格
                int cutPrice = mPresenter.getCutPrice(data.activityInfo, unitPrice * buyNum);
                shoppingCartBean.finalPrice = String.valueOf(unitPrice * buyNum - cutPrice);
                shoppingCartBean.preferentiaPrice = String.valueOf(cutPrice);
                activityModelBean.icon = data.activityInfo.icon;
                shoppingCartBean.carts.get(0).activityModel = activityModelBean;
            }

            ConfirmOrderActivity.launch(this, shoppingCartBean, "buyNow");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //底部弹框ok
            case R.id.detail_type_ok:
                GoodsDetailMallBean.Sku sku = mPresenter.getSkuId(mPropertyAdapter, data, mPopNumber.getText().toString());
                if (sku == null) return;
                buyNum = Integer.valueOf(mPopNumber.getText().toString());
                switch (select_type) {
                    case SELECT_NON:
                        skuId = sku.sequenceNBR;
                        String sk = sku.skuName + getString(R.string.goods_number, String.valueOf(buyNum));
                        mChooseTypeNum.setText(sk);
                        uiBottomSheet.dismiss();
                        break;
                    case SELECT_CART:
                        //加入购物车
                        skuId = sku.sequenceNBR;
                        String sk1 = sku.skuName + getString(R.string.goods_number, String.valueOf(buyNum));
                        mChooseTypeNum.setText(sk1);
                        if (spuId != null && skuId != null) {
                            mPresenter.addShoppingCart(new ShoppingPropertyBean(spuId, skuId, buyNum));
                        }
                        if (uiBottomSheet != null) {
                            uiBottomSheet.dismiss();
                        }
                        break;
                    case SELECT_BUY:
                        //确认订单页
                        skuId = sku.sequenceNBR;
                        String sk2 = sku.skuName + getString(R.string.goods_number, String.valueOf(buyNum));
                        mChooseTypeNum.setText(sk2);
                        if (uiBottomSheet != null) {
                            uiBottomSheet.dismiss();
                        }
                        goToConfirmOrderActivity();
                        break;
                    case SELECT_DESIGN:
                        skuId = sku.sequenceNBR;
                        String sk3 = sku.skuName + getString(R.string.goods_number, String.valueOf(buyNum));
                        mChooseTypeNum.setText(sk3);
                        uiBottomSheet.dismiss();
                        mPresenter.getMkuId(skuId, spuId);
                        break;
                }

                break;
            //购买数量增加
            case R.id.detail_type_add:
                ++buyNum;
                mPopNumber.setText(String.valueOf(buyNum));
                StringUtil.setEditTextSection(mPopNumber);
                if (!StringUtil.isEmpty(mChooseTypeNum.getText().toString())) {
                    mChooseTypeNum.setText(getString(R.string.goods_number, String.valueOf(buyNum)));
                }
                break;
            //购买数量减少
            case R.id.detail_type_sub:
                if (buyNum > 1) {
                    --buyNum;
                    mPopNumber.setText(String.valueOf(buyNum));
                    StringUtil.setEditTextSection(mPopNumber);
                    if (!StringUtil.isEmpty(mChooseTypeNum.getText().toString())) {
                        mChooseTypeNum.setText(getString(R.string.goods_number, String.valueOf(buyNum)));
                    }
                }
                break;
            //关闭底部弹框
            case R.id.detail_type_back:
                uiBottomSheet.dismiss();
                break;
        }
    }
}
