package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.PropertyAdapter;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.PropertyBean;
import com.zhaotai.uzao.bean.TemplateInfoBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
import com.zhaotai.uzao.bean.post.TemplateDesignInfo;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.presenter.TemplatePurchasePresenter;
import com.zhaotai.uzao.ui.login.activity.ProtocolActivity;
import com.zhaotai.uzao.ui.order.contract.TemplatePurchaseContract;
import com.zhaotai.uzao.utils.GlideImageLoader;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/9/8
 * Created by LiYou
 * Description : 模板商品 购买
 */

public class TemplateDesignPurchaseActivity extends BaseActivity implements TemplatePurchaseContract.View, View.OnClickListener, TextWatcher {

    @BindView(R.id.banner_template_purchase)
    Banner mBanner;
    @BindView(R.id.tv_template_purchase_mku_value)
    TextView mMkuValue;
    @BindView(R.id.rl_template_purchase_select_property)
    RelativeLayout mRlProperty;
    @BindView(R.id.tv_template_purchase_market_price)
    TextView mMarketPrice;
    @BindView(R.id.tv_template_purchase_num)
    EditText mBuyNum;

    @BindView(R.id.tv_template_purchase_name)
    EditText tv_template_purchase_name;

    @BindView(R.id.iv_template_purchase_select_protocol)
    ImageView mIvSelectProtocol;
    @BindView(R.id.tv_template_purchase_protocol)
    TextView mProtocol;

    private RecyclerView mDialogRecycler;
    private UIBottomSheet uiBottomSheet;
    private PropertyAdapter mPropertyAdapter;
    private GoodsDetailMallBean data;
    private String skuId;
    private int buyBum = 1;
    private boolean isSelectProtocol = false;
    //蒙版id
    private static final String EXTRA_KEY_SKU_IMAGES = "extra_key_sku_images";
    private static final String EXTRA_KEY_SPU_IMAGES = "extra_key_spu_images";
    private static final String EXTRA_KEY_DESIGN_ID = "extra_key_design_id";
    private static final String EXTRA_KEY_IS_TEMPLATE = "extra_key_is_template";
    private static final String EXTRA_KEY_IS_TEMPLATE_DESIGN = "extra_key_is_template_design";
    private static final String EXTRA_KEY_MATERIAL_ID = "extra_key_material_id";//素材id
    private static final String EXTRA_KEY_MATERIAL_NEED_BUY_INFO = "extra_key_need_buy_info";//素材收费
    private static final String EXTRA_KEY_SPU_ID = "extra_key_spu_id";//商品id

    private TemplatePurchasePresenter mPresenter;
    private GoodsDetailMallBean.Sku sku;
    private List<TemplateImageInfo> skuImages;
    private List<String> spuImages;
    private UnPayMaterialBean materialListBean;
    private TemplateDesignInfo templateDesignInfo;
    private String spuId;

    /**
     * 获取2d 待发布商品信息 - 模板商品 带素材
     */
    public static void launch(Context context, List<TemplateImageInfo> skuImages, List<String> spuImages, UnPayMaterialBean materialListBean, TemplateDesignInfo templateDesignInfo, String spuId) {
        Intent intent = new Intent(context, TemplateDesignPurchaseActivity.class);
        intent.putExtra(EXTRA_KEY_SKU_IMAGES, (Serializable) skuImages);
        intent.putExtra(EXTRA_KEY_SPU_IMAGES, (Serializable) spuImages);
        intent.putExtra(EXTRA_KEY_MATERIAL_NEED_BUY_INFO, materialListBean);
        intent.putExtra(EXTRA_KEY_IS_TEMPLATE_DESIGN, templateDesignInfo);
        intent.putExtra(EXTRA_KEY_SPU_ID, spuId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_template_purchase);
        mTitle.setText("购买");
        findViewById(R.id.rl_template_purchase_select_property).setOnClickListener(this);
        findViewById(R.id.tv_template_purchase_buy).setOnClickListener(this);
        findViewById(R.id.iv_template_purchase_sub).setOnClickListener(this);
        findViewById(R.id.iv_template_purchase_add).setOnClickListener(this);
        initSkuProperty();
        mProtocol.setText("《版权协议》");
        mBuyNum.addTextChangedListener(this);
    }

    /**
     * 初始化规格选择窗口
     */
    private void initSkuProperty() {
        View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.dialog_template_property, null);
        //属性 确定
        bottomSheetView.findViewById(R.id.iv_template_property_ok).setOnClickListener(this);
        bottomSheetView.findViewById(R.id.iv_template_property_back).setOnClickListener(this);
        //sku
        mDialogRecycler = (RecyclerView) bottomSheetView.findViewById(R.id.iv_template_property_recycler);
        mDialogRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        uiBottomSheet = new UIBottomSheet(mContext);
        uiBottomSheet.setContentView(bottomSheetView);
    }

    @Override
    protected void initData() {
        spuImages = getIntent().getStringArrayListExtra(EXTRA_KEY_SPU_IMAGES);
        mPresenter = new TemplatePurchasePresenter(this, this);
        spuId = getIntent().getStringExtra(EXTRA_KEY_SPU_ID);
        materialListBean = (UnPayMaterialBean) getIntent().getSerializableExtra(EXTRA_KEY_MATERIAL_NEED_BUY_INFO);
        templateDesignInfo = (TemplateDesignInfo) getIntent().getSerializableExtra(EXTRA_KEY_IS_TEMPLATE_DESIGN);
        skuImages = (List<TemplateImageInfo>) getIntent().getSerializableExtra(EXTRA_KEY_SKU_IMAGES);
        //请求2d待发布商品信息
        mPresenter.getTemplateDesignProductDetail(spuId);
    }

    @Override
    public void templateDetail(TemplateInfoBean data) {
    }

    @Override
    public void template3DDetail(TemplateInfoBean data) {
    }

    @Override
    public void templateDesignProductDetail(GoodsDetailMallBean goodsDetailMallBean) {
        this.data = goodsDetailMallBean;
        //轮播图
        List<String> image = new ArrayList<>();
        for (String img : spuImages) {
            image.add(ApiConstants.UZAOCHINA_IMAGE_HOST + img);
        }
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(image);
        mBanner.isAutoPlay(false);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

        //市场价格
        mMarketPrice.setText("价格    ¥" + data.basicInfo.spuModel.displayPriceY);
        if (data.properties.size() > 0) {
            //尺码规格  颜色 数据 列表
            List<PropertyBean> skuProperty = new ArrayList<>();
            for (PropertyBean properties : data.properties) {
                //提取sku数据
                skuProperty.add(properties);
            }
            mPropertyAdapter = new PropertyAdapter(skuProperty);
            mDialogRecycler.setAdapter(mPropertyAdapter);
        } else {
            mRlProperty.setVisibility(View.GONE);
        }
        //购买数量
        mBuyNum.setText("1");
        mBuyNum.setSelection(1);
    }

    private void showDialog() {
        uiBottomSheet.show();
    }

    private void dismissDialog() {
        uiBottomSheet.dismiss();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 选择协议
     */
    @OnClick(R.id.iv_template_purchase_select_protocol)
    public void onClickSelectProtocol() {
        if (isSelectProtocol) {
            isSelectProtocol = false;
            mIvSelectProtocol.setImageResource(R.drawable.icon_circle_unselected);
        } else {
            isSelectProtocol = true;
            mIvSelectProtocol.setImageResource(R.drawable.icon_circle_selected);
        }
    }

    /**
     * 协议
     */
    @OnClick(R.id.tv_template_purchase_protocol)
    public void onClickProtocol() {
        ProtocolActivity.launch(mContext, GlobalVariable.PROTOCOL_COPYRIGHT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //确定按钮
            case R.id.iv_template_property_ok:
                sku = mPresenter.getSkuId(mPropertyAdapter, data);
                if (sku != null) {
                    skuId = sku.sequenceNBR;
                    String skuValue = sku.skuValues;
                    mMkuValue.setText(skuValue);
                    dismissDialog();
                }
                break;
            //打开规格选择
            case R.id.rl_template_purchase_select_property:
                showDialog();
                break;
            //立即购买
            case R.id.tv_template_purchase_buy:
                if (StringUtil.isEmpty(tv_template_purchase_name.getText().toString().trim())) {
                    ToastUtil.showShort("请输入商品名称");
                    return;
                } else {
                    mPresenter.setSpuName(tv_template_purchase_name.getText().toString());
                }
                if (!isSelectProtocol) {
                    ToastUtil.showShort("请同意版权协议");
                    return;
                }
                //有多余的规格可以选择
                if (data.properties.size() > 0 && StringUtil.isEmpty(skuId) && sku == null) {
                    showDialog();
                } else {
                    //判断库存
                    if (StringUtil.isEmpty(skuId)) {
                        skuId = data.skus.get(0).sequenceNBR;
                    }
                    if (mPresenter.verifySampleStoreNum(data, skuId, buyBum)) {
                        return;
                    }
                    if (materialListBean != null && materialListBean.getSourceMaterialModels().size() > 0) {
                        //购买模板商品
                        mPresenter.buyTemplateSpu(data, sku.sequenceNBR, buyBum, sku.skuValues,
                                sku.marketPrice, spuImages, skuImages, materialListBean.getSourceMaterialModels(), templateDesignInfo, spuId);
                    } else {
                        //购买模板商品
                        mPresenter.buyTemplateSpu(data, sku.sequenceNBR, buyBum, sku.skuValues,
                                sku.marketPrice, spuImages, skuImages, null, templateDesignInfo, spuId);
                    }
                }
                break;
            case R.id.iv_template_property_back:
                uiBottomSheet.dismiss();
                break;
            case R.id.iv_template_purchase_add:
                ++buyBum;
                mBuyNum.setText(String.valueOf(buyBum));
                break;
            case R.id.iv_template_purchase_sub:
                if (buyBum > 1) {
                    --buyBum;
                    mBuyNum.setText(String.valueOf(buyBum));
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().isEmpty()) {
            if ("0".equals(s.toString())) {
                s = "1";
            }
            buyBum = Integer.valueOf(s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
