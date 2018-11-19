package com.zhaotai.uzao.ui.category.material.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.RecommendMaterialAdapter;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.DictionaryBean;
import com.zhaotai.uzao.bean.EventBean.MaterialBuySuccessEvent;
import com.zhaotai.uzao.bean.EventBean.MaterialLoginSuccessEvent;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.MaterialListBean;
import com.zhaotai.uzao.bean.MyTrackRequestBean;
import com.zhaotai.uzao.bean.TagBean;
import com.zhaotai.uzao.bean.post.RewardInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.brand.BrandActivity;
import com.zhaotai.uzao.ui.category.goods.adapter.RewardAdapter;
import com.zhaotai.uzao.ui.category.material.contract.MaterialDetailContract;
import com.zhaotai.uzao.ui.category.material.presenter.MaterialDetailPresenter;
import com.zhaotai.uzao.ui.designer.activity.DesignerActivity;
import com.zhaotai.uzao.ui.discuss.activity.DiscussMainListActivity;
import com.zhaotai.uzao.ui.login.activity.LoginActivity;
import com.zhaotai.uzao.ui.login.activity.ProtocolActivity;
import com.zhaotai.uzao.ui.order.activity.MaterialPayConfirmOrderActivity;
import com.zhaotai.uzao.ui.order.activity.MaterialRewardPayConfirmOrderActivity;
import com.zhaotai.uzao.ui.poster.PosterActivity;
import com.zhaotai.uzao.ui.theme.activity.AddThemeActivity;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/8
 * Created by LiYou
 * Description : 素材详情页面
 */

public class MaterialDetailActivity extends BaseActivity implements MaterialDetailContract.View, View.OnClickListener {

    @BindView(R.id.root)
    FrameLayout mRoot;

    //素材图片
    ImageView mMaterialImage;
    //素材名字
    TextView mMaterialName;
    //设计师
    TextView mMaterialDesigner;
    //费用
    TextView mMaterialPrice;
    //标签
    TagFlowLayout mTagLayout;
    //使用数
    TextView mUseCount;
    //收藏数
    TextView mCollectCount;
    //点赞
    TextView mLikeCount;
    //评论数
    TextView mCommentCount;
    //分类
    TextView mCategory;
    //授权时长
    TextView mTime;
    //理念
    TextView mIdea;
    //品牌
    TextView mBrand;
    //品牌布局
    LinearLayout mLlBrand;

    ImageView mImageCollect;
    ImageView mImageLike;

    ImageView iv_welfare;

    @BindView(R.id.tv_material_detail_bottom_left_btn)
    TextView mLeftBtn;
    @BindView(R.id.tv_material_detail_bottom_right_btn)
    TextView mRightBtn;
    @BindView(R.id.right_btn)
    ImageView mMoreView;

    @BindView(R.id.iv_share)
    ImageView mShare;

    @BindView(R.id.recycler_recommend)
    RecyclerView mRecyclerRecommend;

    /**
     * 素材Id
     */
    private static final String EXTRA_KEY_ID = "extra_key_id";
    private MaterialDetailPresenter mPresenter;
    private String materialId;
    private boolean isCollect;
    private boolean isLike;
    private int collectCount = 0;
    private int likeCount = 0;
    private MaterialDetailBean data;
    private PopupWindow window;
    private PopupWindow mMorePopWindow;
    private String designerId;//设计师Id
    private String brandId;//品牌Id

    //立即购买
    private static final int BUY = 0;
    //已购买
    private static final int BUYED = 1;
    private int type;
    private View mHeadView;

    /**
     * 素材详情
     *
     * @param context 上下文
     * @param id      素材详情Id
     */
    public static void launch(Context context, String id) {
        Intent intent = new Intent(context, MaterialDetailActivity.class);
        intent.putExtra(EXTRA_KEY_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_material_detail);
        mTitle.setText("素材详情");
        EventBus.getDefault().register(this);
        mHeadView = View.inflate(this, R.layout.activity_material_detail_head, null);
        mMaterialImage = (ImageView) mHeadView.findViewById(R.id.iv_material_image);
        mMaterialName = (TextView) mHeadView.findViewById(R.id.tv_material_name);
        mMaterialDesigner = (TextView) mHeadView.findViewById(R.id.tv_designer);
        mMaterialPrice = (TextView) mHeadView.findViewById(R.id.tv_material_price);
        mTagLayout = (TagFlowLayout) mHeadView.findViewById(R.id.id_flowlayout);
        mUseCount = (TextView) mHeadView.findViewById(R.id.tv_material_detail_use_count);
        mCollectCount = (TextView) mHeadView.findViewById(R.id.tv_material_detail_collect_count);
        mLikeCount = (TextView) mHeadView.findViewById(R.id.tv_material_detail_like_count);

        mCommentCount = (TextView) mHeadView.findViewById(R.id.tv_material_detail_comment_count);
        mCategory = (TextView) mHeadView.findViewById(R.id.tv_material_detail_category);
        mTime = (TextView) mHeadView.findViewById(R.id.tv_material_detail_time);
        mIdea = (TextView) mHeadView.findViewById(R.id.tv_material_detail_idea);

        mBrand = (TextView) mHeadView.findViewById(R.id.tv_brand);
        mLlBrand = (LinearLayout) mHeadView.findViewById(R.id.ll_brand);
        mImageCollect = (ImageView) mHeadView.findViewById(R.id.iv_material_detail_collect);
        mImageLike = (ImageView) mHeadView.findViewById(R.id.iv_material_detail_like);
        iv_welfare = (ImageView) mHeadView.findViewById(R.id.iv_welfare);

        iv_welfare.setOnClickListener(this);
        mHeadView.findViewById(R.id.tv_material_detail_use_way).setOnClickListener(this);
        mHeadView.findViewById(R.id.ll_material_detail_like).setOnClickListener(this);
        mHeadView.findViewById(R.id.ll_material_detail_collect).setOnClickListener(this);
        mHeadView.findViewById(R.id.ll_designer).setOnClickListener(this);
        mLlBrand.setOnClickListener(this);
        mHeadView.findViewById(R.id.ll_material_comment).setOnClickListener(this);

        mToolbar.getBackground().mutate().setAlpha(0);
        mTitle.setAlpha(0);
        mRecyclerRecommend.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("dy=   " + getScrollYDistance());

                int alpha = (int) (((float) getScrollYDistance() / 1000) * 255);
                float textAlpha = (float) getScrollYDistance() / 1000;
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
    }

    public int getScrollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerRecommend.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();
        return (position) * itemHeight - firstVisibleChildView.getTop();

    }

    @Override
    protected void initData() {
        materialId = getIntent().getStringExtra(EXTRA_KEY_ID);
        mPresenter = new MaterialDetailPresenter(this, this);
        //获取素材详情
        mPresenter.getMaterialDetail(materialId);
        //获取推荐素材
        mPresenter.getRecommendMaterial(materialId);
        //判断是否收藏
        mPresenter.isCollect(materialId);
        //判断是否点赞
        mPresenter.isLikeMaterial(materialId);
    }

    /**
     * 是否开启状态页
     *
     * @return true开启 false 关闭
     */
    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 显示素材详情
     *
     * @param data 素材详情数据
     */
    @Override
    public void showMaterialDetail(final MaterialDetailBean data) {
        mMoreView.setVisibility(View.VISIBLE);
        mShare.setVisibility(View.VISIBLE);
        this.data = data;
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + data.thumbnail, mMaterialImage);
        mMaterialName.setText(data.sourceMaterialName);
        //判断是否有品牌
        if (!StringUtil.isEmpty(data.brandId)) {
            brandId = data.brandId;
            mBrand.setText("品牌:    " + data.brandName);
            mLlBrand.setVisibility(View.VISIBLE);
        }
        //判断是否有设计师
        if (data.designer != null) {
            designerId = data.userId;
            mMaterialDesigner.setText("设计师:    " + data.designer.nickName);
        } else {
            if (data.assignDesigner != null) {
                designerId = data.designerId;
                mMaterialDesigner.setText("设计师:    " + data.assignDesigner.nickName);
            } else {
                mMaterialDesigner.setText("设计师:    " + GlobalVariable.UZAO_MATERIAL_NAME);
            }
        }
        if (GlobalVariable.MATERIAL_MODE_CHARGE.equals(data.saleMode)) {
            //收费素材
            mMaterialPrice.setText("¥" + data.priceY);
            mLeftBtn.setVisibility(View.GONE);
            //判断是否已获取该素材
            if (data.obtained) {
                type = BUYED;
                //已购买
                hasMaterial();
            } else {
                type = BUY;
                //未购买
                mRightBtn.setText("立即购买");
                mRightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                mRightBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            }
            if (LoginHelper.getLoginStatus()) {
                String userId = com.zhaotai.uzao.utils.SPUtils.getSharedStringData(AppConfig.USER_ID);
                if (userId.equals(designerId)) {
                    type = BUYED;
                    hasMaterial();
                }
            }
        } else {
            //免费素材
            mMaterialPrice.setText("免费");
            if (GlobalVariable.MATERIAL_MODE_FREE.equals(data.saleMode)) {
                mLeftBtn.setVisibility(View.GONE);
            }
            //判断是否已获取该素材
            if (data.obtained) {
                //已获取
                type = BUYED;
                hasMaterial();
            } else {
                //未获取
                type = BUY;
                mRightBtn.setText("获取");
                mRightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                mRightBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            }

            if (LoginHelper.getLoginStatus()) {
                String userId = com.zhaotai.uzao.utils.SPUtils.getSharedStringData(AppConfig.USER_ID);
                if (userId.equals(designerId)) {
                    type = BUYED;
                    hasMaterial();
                }
            }
        }
        //打赏次数
        if (data.rewardCount == 0) {
            mLeftBtn.setText("打赏");
        } else if (data.rewardCount > 999) {
            mLeftBtn.setText("打赏(999+次)");
        } else {
            mLeftBtn.setText("打赏(" + data.rewardCount + "次)");
        }
        //使用次数
        int useCount = Integer.valueOf(data.useCount);
        if (useCount > 999) {
            mUseCount.setText("999+");
        } else {
            mUseCount.setText(String.valueOf(useCount));
        }
        //收藏次数
        collectCount = Integer.valueOf(data.favoriteCount);
        if (collectCount > 999) {
//            mCollectCount.setText((float) ((collectCount / 1000)) / 10 + "万");
            mCollectCount.setText("999+");
        } else {
            mCollectCount.setText(String.valueOf(collectCount));
        }
        //点赞数
        likeCount = Integer.valueOf(data.upvoteCount);
        if (likeCount > 999) {
            mLikeCount.setText("999+");
        } else {
            mLikeCount.setText(String.valueOf(likeCount));
        }
        //评论数
        int commentCount = Integer.valueOf(data.commentCount);
        if (commentCount > 999) {
            mCommentCount.setText("999+");
        } else {
            mCommentCount.setText(String.valueOf(commentCount));
        }

        //分类
        mCategory.setText("分类:    " + data.categoryNames);
        //授权时长
        String periodUnit = "";
        if ("month".equals(data.periodUnit)) {
            periodUnit = "个月";
        } else if ("year".equals(data.periodUnit)) {
            periodUnit = "年";
        } else if ("forever".equals(data.periodUnit)) {
            periodUnit = "永久";
            data.authPeriod = "";
        }
        mTime.setText("授权时长:    " + data.authPeriod + periodUnit);

        //设计理念
        mIdea.setText("设计理念:    " + data.designIdea);
        //标签
        mTagLayout.setAdapter(new TagAdapter<TagBean>(data.tags) {
            @Override
            public View getView(FlowLayout parent, int position, TagBean s) {
                TextView tagView = new TextView(mContext);
                tagView.setPadding((int) PixelUtil.dp2px(6), 0, (int) PixelUtil.dp2px(6), 0);
                tagView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_bg_tag));
                tagView.setTextColor(Color.parseColor("#fc5900"));
                tagView.setTextSize(12);
                tagView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                params.setMarginEnd(8);
                tagView.setLayoutParams(params);
                tagView.setText(s.tagName);
                return tagView;
            }
        });

        mTagLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                MaterialCategoryListActivity.launchTag(mContext, data.tags.get(position).tagCode, data.tags.get(position).tagName);
                return false;
            }
        });
        //发送足迹
        MyTrackRequestBean myTrackRequestBean = new MyTrackRequestBean();
        myTrackRequestBean.setEntityId(materialId);
        myTrackRequestBean.setPrice(data.price);
        myTrackRequestBean.setEntityType("SourceMaterial");
        myTrackRequestBean.setThumbnail(data.thumbnail);
        myTrackRequestBean.setEntityName(data.sourceMaterialName);
        mPresenter.addMyTrack(myTrackRequestBean);
    }

    /**
     * 分享
     */
    @OnClick(R.id.iv_share)
    public void OnClickShare() {
        if (LoginHelper.getLoginStatus()) {
            mPresenter.hasPoster();
        } else {
            LoginHelper.goLogin(this);
        }
    }

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

    @Override
    public void showHasWelfare(boolean hasWelfare) {
        iv_welfare.setVisibility(hasWelfare ? View.VISIBLE : View.GONE);
    }

    /**
     * 收藏
     *
     * @param isCollect 是否收藏
     */
    @Override
    public void collect(boolean isCollect) {
        this.isCollect = isCollect;
        if (isCollect) {
            collectCount = collectCount + 1;
            mImageCollect.setImageResource(R.drawable.ic_material_collect);
        } else {
            collectCount = collectCount - 1;
            mImageCollect.setImageResource(R.drawable.ic_material_un_collect);
        }
        if (collectCount > 999) {
            mCollectCount.setText("999+");
        } else {
            mCollectCount.setText(String.valueOf(collectCount));
        }
    }


    /**
     * 点赞
     *
     * @param isLike 是否点赞
     */
    @Override
    public void like(boolean isLike) {
        this.isLike = isLike;
        if (isLike) {
            likeCount = likeCount + 1;
            mImageLike.setImageResource(R.drawable.ic_like);
        } else {
            likeCount = likeCount - 1;
            mImageLike.setImageResource(R.drawable.ic_un_like);
        }
        if (likeCount > 999) {
            mLikeCount.setText("999+");
        } else {
            mLikeCount.setText(String.valueOf(likeCount));
        }
    }

    /**
     * 改变收藏状态
     *
     * @param isCollect 是否收藏
     */
    @Override
    public void isCollect(boolean isCollect) {
        this.isCollect = isCollect;
        if (isCollect) {
            mImageCollect.setImageResource(R.drawable.ic_material_collect);
        } else {
            mImageCollect.setImageResource(R.drawable.ic_material_un_collect);
        }
    }

    @Override
    public void isLike(boolean isLike) {
        this.isLike = isLike;
        if (isLike) {
            mImageLike.setImageResource(R.drawable.ic_like);
        } else {
            mImageLike.setImageResource(R.drawable.ic_un_like);
        }
    }

    /**
     * 获得素材
     */
    @Override
    public void hasMaterial() {
        if (GlobalVariable.MATERIAL_MODE_CHARGE.equals(data.saleMode)) {
            mRightBtn.setText("已购买");
        } else {
            mRightBtn.setText("已获取");
        }
        mRightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.c656565));
        mRightBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cdcdcd));
    }

    /**
     * 底部左边按钮
     */
    @OnClick(R.id.tv_material_detail_bottom_left_btn)
    public void onClickLeftBtn() {
        if (!LoginHelper.getLoginStatus()) {
            LoginActivity.launch(mContext);
            return;
        }
        if (data != null && data.designerId != null && mPresenter.isMyMaterial(data.designerId)) {
            ToastUtil.showShort("不能打赏自己的素材");
        } else {
            //打赏
            mPresenter.getRewardPrice();
        }
    }

    @OnClick(R.id.right_btn)
    public void onClickMore() {
        if (mMorePopWindow == null) {
            View itemView = View.inflate(mContext, R.layout.pop_goods_detail_tab_item, null);
            LinearLayout mService = (LinearLayout) itemView.findViewById(R.id.ll_pop_custom_service);
            LinearLayout mShare = (LinearLayout) itemView.findViewById(R.id.ll_pop_share);
            LinearLayout mTheme = (LinearLayout) itemView.findViewById(R.id.ll_pop_add_theme);
            mMorePopWindow = new PopupWindow(itemView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mMorePopWindow.setFocusable(true);
            mMorePopWindow.setOutsideTouchable(true);
            mMorePopWindow.setBackgroundDrawable(new ColorDrawable());
            mMorePopWindow.showAsDropDown(mMoreView);
            //客服
            mService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginHelper.getLoginStatus()) {
                        LoginActivity.launch(mContext);
                        return;
                    }
                    loginIm();
                }
            });
            //分享
            mShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginHelper.getLoginStatus()) {
                        LoginActivity.launch(mContext);
                        return;
                    }
                }
            });
            //添加主题
            mTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LoginHelper.getLoginStatus()) {
                        LoginActivity.launch(mContext);
                        return;
                    }
                    AddThemeActivity.launch(MaterialDetailActivity.this, materialId, AddThemeActivity.TYPE_MATERIAL);
                    mMorePopWindow.dismiss();
                }
            });
        } else {
            mMorePopWindow.showAsDropDown(mMoreView);
        }
    }

    /**
     * 显示打赏金额
     *
     * @param price 打赏金额列表
     */
    @Override
    public void showReward(List<DictionaryBean> price) {
        if (window == null) {
            window = new PopupWindow(mContext);
            View popView = View.inflate(mContext, R.layout.pop_material_reward, null);
            final RelativeLayout popRoot = (RelativeLayout) popView.findViewById(R.id.pop_root);
            final TextView mText1 = (TextView) popView.findViewById(R.id.pop_text);
            final EditText mEtPrice = (EditText) popView.findViewById(R.id.et_reward_price);
            final RecyclerView mRecycler = (RecyclerView) popView.findViewById(R.id.recycler);
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false));
            RewardAdapter mRewardAdapter = new RewardAdapter(price);
            mRecycler.setAdapter(mRewardAdapter);
            final TextView mText2 = (TextView) popView.findViewById(R.id.pop_text1);
            window.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            window.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            window.setContentView(popView);
            window.setFocusable(true);
            window.setOutsideTouchable(true);
            window.setBackgroundDrawable(new ColorDrawable());
            window.showAtLocation(mRoot, Gravity.CENTER, 0, 0);
            window.update();
            //背景点击关闭
            popRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    window.dismiss();
                }
            });

            mRewardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    for (int i = 0; i < adapter.getData().size(); i++) {
                        DictionaryBean item = (DictionaryBean) adapter.getItem(i);
                        if (item != null) {
                            item.isSelect = position == i;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    DictionaryBean item = (DictionaryBean) adapter.getItem(position);
                    if (item != null) {
                        RewardInfo info = new RewardInfo();
                        info.materialId = materialId;
                        info.tipOption = item.entryKey;
                        List<MaterialDetailBean> list = new ArrayList<>();
                        list.add(data);
                        MaterialRewardPayConfirmOrderActivity.launch((Activity) mContext, list, item.entryValue, info);
                    }
                }
            });

            mText2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("自定义打赏金额".equals(mText2.getText().toString())) {
                        mText2.setText("确定打赏");
                        mText1.setText("填写打赏金额");
                        mRecycler.setVisibility(View.GONE);
                        mEtPrice.setVisibility(View.VISIBLE);
                    } else if ("确定打赏".equals(mText2.getText().toString())) {
                        RewardInfo info = new RewardInfo();
                        info.materialId = materialId;
                        info.tipOption = "ohters";
                        info.amount = mEtPrice.getText().toString();
                        List<MaterialDetailBean> list = new ArrayList<>();
                        list.add(data);
                        MaterialRewardPayConfirmOrderActivity.launch((Activity) mContext, list, mEtPrice.getText().toString(), info);
                    }

                }
            });
            //监听关闭 重置按钮
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mText1.setText("选择打赏金额");
                    mText2.setText("自定义打赏金额");
                    mRecycler.setVisibility(View.VISIBLE);
                    mEtPrice.setVisibility(View.GONE);
                }
            });
        } else {
            window.showAtLocation(mRoot, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 显示推荐素材
     *
     * @param data 素材列表
     */
    @Override
    public void showRecommendMaterial(List<MaterialListBean> data) {
        mRecyclerRecommend.setNestedScrollingEnabled(false);
        mRecyclerRecommend.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerRecommend.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int childPosition = parent.getChildAdapterPosition(view);
                if (childPosition > 0) {
                    if (childPosition % 2 == 0) {
                        outRect.left = 10;
                        outRect.right = 20;
                    } else {
                        outRect.left = 20;
                        outRect.right = 10;
                    }
                }
                outRect.bottom = 10;
            }
        });
        RecommendMaterialAdapter recommendMaterialAdapter = new RecommendMaterialAdapter(data);
        recommendMaterialAdapter.addHeaderView(mHeadView);
        mRecyclerRecommend.setAdapter(recommendMaterialAdapter);

        recommendMaterialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MaterialListBean item = (MaterialListBean) adapter.getItem(position);
                if (item != null)
                    MaterialDetailActivity.launch(mContext, item.sequenceNBR);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //打赏成功
        if (resultCode == RESULT_OK) {
            if (window != null) {
                window.dismiss();
            }
        }
    }

    /**
     * 素材购买成功
     *
     * @param event 素材购买成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MaterialBuySuccessEvent event) {
        type = BUYED;
        mRightBtn.setText("已购买");
        mRightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.c656565));
        mRightBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cdcdcd));
    }

    /**
     * 登录成功 刷新 是否购买 是否关注
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MaterialLoginSuccessEvent event) {
        if (mPresenter != null) {
            mPresenter.getMaterialDetail(materialId);
            //判断是否收藏
            mPresenter.isCollect(materialId);
            //判断是否点赞
            mPresenter.isLikeMaterial(materialId);
        }
    }

    /**
     * 底部右边按钮
     */
    @OnClick(R.id.tv_material_detail_bottom_right_btn)
    public void onClickRightBtn() {
//        MaterialSuccessPayResultActivity.launch(mContext, String.valueOf("2"), this.data);
        switch (type) {
            case BUY://素材购买
                //判断是否登录
                if (!LoginHelper.getLoginStatus()) {
                    LoginActivity.launch(mContext);
                    return;
                }
                if (this.data != null) {
                    switch (data.saleMode) {
                        case GlobalVariable.MATERIAL_MODE_CHARGE://立即购买
                            List<MaterialDetailBean> list = new ArrayList<>();
                            list.clear();
                            list.add(this.data);
                            MaterialPayConfirmOrderActivity.launch(MaterialDetailActivity.this, list);
                            break;
                        case GlobalVariable.MATERIAL_MODE_FREE://获取
                            mPresenter.getFreeMaterial(materialId);
                            break;
                        case GlobalVariable.MATERIAL_MODE_FREE_CHARGE://获取
                            mPresenter.getFreeMaterial(materialId);
                            break;
                    }
                }
                break;
            case BUYED:
                break;
        }
    }

    /**
     * 登录im
     */
    private void loginIm() {
        showLoadingDialog();
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

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
            if (share_media == null) {
                //根据key来区分自定义按钮的类型，并进行对应的操作
                if (snsPlatform.mKeyword.equals("umeng_sharebutton_poster")) {
                    PosterActivity.launchMaterial(MaterialDetailActivity.this, materialId);
                }
            }
        }
    };


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
            Toast.makeText(MaterialDetailActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         *  分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MaterialDetailActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         *  分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MaterialDetailActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_welfare://福利
                if (LoginHelper.getLoginStatus()) {
                    PosterActivity.launchMaterialBenefit(MaterialDetailActivity.this, materialId);
                } else {
                    LoginHelper.goLogin(this);
                }
                break;
            case R.id.ll_brand://品牌
                if (data != null && brandId != null) {
                    BrandActivity.launch(mContext, brandId);
                }
                break;
            case R.id.ll_designer://设计师
                if (data != null && designerId != null)
                    DesignerActivity.launch(mContext, designerId);
                break;
            case R.id.ll_material_detail_collect://点击收藏
                if (!LoginHelper.getLoginStatus()) {
                    LoginActivity.launch(mContext);
                    return;
                }
                if (isCollect) {
                    mPresenter.cancelCollectMaterial(materialId);
                } else {
                    mPresenter.collectMaterial(materialId);
                }
                break;
            case R.id.ll_material_detail_like://点击点赞
                if (!LoginHelper.getLoginStatus()) {
                    LoginActivity.launch(mContext);
                    return;
                }
                if (isLike) {
                    mPresenter.cancelLikeMaterial(materialId);
                } else {
                    mPresenter.likeMaterial(materialId);
                }
                break;
            case R.id.tv_material_detail_use_way://素材使用方式
                ProtocolActivity.launch(mContext, GlobalVariable.PROTOCOL_MATERIAL_USE);
                break;
            case R.id.ll_material_comment://评论
                //评论xx
                if (LoginHelper.getLoginStatus()) {
                    DiscussMainListActivity.launch(this, materialId, DiscussMainListActivity.TYPE_MATERIAL, data.designerId);
                } else {
                    LoginActivity.launch(this);
                }
                break;
        }
    }
}
