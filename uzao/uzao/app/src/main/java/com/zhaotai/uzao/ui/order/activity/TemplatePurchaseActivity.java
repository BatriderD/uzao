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
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.TemplateInfoBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
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
 * Description : 载体设计完成 购买
 */

public class TemplatePurchaseActivity extends BaseActivity implements TemplatePurchaseContract.View, View.OnClickListener, TextWatcher {

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
    private TemplateBean data;
    private TemplateInfoBean dataInfo;
    private String skuId;
    private int buyBum = 1;
    private boolean isSelectProtocol = false;
    //蒙版id
    private static final String EXTRA_KEY_MKU_ID = "extra_key_mku_id";
    private static final String EXTRA_KEY_SKU_IMAGES = "extra_key_sku_images";
    private static final String EXTRA_KEY_SPU_IMAGES = "extra_key_spu_images";
    private static final String EXTRA_KEY_TEMPLATE_ID = "extra_key_template_id";
    private static final String EXTRA_KEY_DESIGN_ID = "extra_key_design_id";
    private static final String EXTRA_KEY_IS_TEMPLATE = "extra_key_is_template";
    private static final String EXTRA_KEY_MATERIAL_ID = "extra_key_material_id";//素材id
    private static final String EXTRA_KEY_MATERIAL_NEED_BUY_INFO = "extra_key_need_buy_info";//素材收费
    private static final String EXTRA_KEY_SPU_ID = "extra_key_spu_id";//商品id

    private TemplatePurchasePresenter mPresenter;
    private TemplateBean.Sku sku;
    private List<TemplateImageInfo> skuImages;
    private List<String> spuImages;
    private String designId;
    private String mkuId;
    private String isTemplate;
    private List<String> materialIds;
    private UnPayMaterialBean materialListBean;

    /**
     * 载体设计完成 购买 - 带素材
     *
     * @param context          上下文
     * @param mkuId            蒙版组id
     * @param skuImages        sku图片
     * @param spuImages        商品效果图
     * @param designId         设计id
     * @param isTemplate       是否模板
     * @param materialIds      素材id
     * @param materialListBean 素材数据
     * @param spuId            商品id
     */
    public static void launch(Context context, String mkuId, List<TemplateImageInfo> skuImages, List<String> spuImages, String designId, String isTemplate,
                              List<String> materialIds, UnPayMaterialBean materialListBean, String spuId) {
        Intent intent = new Intent(context, TemplatePurchaseActivity.class);
        intent.putExtra(EXTRA_KEY_MKU_ID, mkuId);
        intent.putExtra(EXTRA_KEY_SKU_IMAGES, (Serializable) skuImages);
        intent.putExtra(EXTRA_KEY_SPU_IMAGES, (Serializable) spuImages);
        intent.putExtra(EXTRA_KEY_DESIGN_ID, designId);
        intent.putExtra(EXTRA_KEY_IS_TEMPLATE, isTemplate);
        intent.putExtra(EXTRA_KEY_MATERIAL_ID, (Serializable) materialIds);
        intent.putExtra(EXTRA_KEY_MATERIAL_NEED_BUY_INFO, materialListBean);
        intent.putExtra(EXTRA_KEY_SPU_ID, spuId);
        context.startActivity(intent);
    }


    /**
     * 载体设计完成 购买 - 不带素材
     *
     * @param context    上下文
     * @param mkuId      蒙版组id
     * @param skuImages  sku规格图片
     * @param spuImages  效果图片
     * @param designId   设计id
     * @param isTemplate 是否模板
     * @param spuId      商品id
     */
    public static void launch(Context context, String mkuId, List<TemplateImageInfo> skuImages, List<String> spuImages, String designId, String isTemplate, String spuId) {
        Intent intent = new Intent(context, TemplatePurchaseActivity.class);
        intent.putExtra(EXTRA_KEY_MKU_ID, mkuId);
        intent.putExtra(EXTRA_KEY_SKU_IMAGES, (Serializable) skuImages);
        intent.putExtra(EXTRA_KEY_SPU_IMAGES, (Serializable) spuImages);
        intent.putExtra(EXTRA_KEY_DESIGN_ID, designId);
        intent.putExtra(EXTRA_KEY_IS_TEMPLATE, isTemplate);
        intent.putExtra(EXTRA_KEY_SPU_ID, spuId);
        context.startActivity(intent);
    }

    /**
     * 3d载体设计完成 购买
     *
     * @param context          上下文
     * @param spuId            商品id
     * @param designId         设计id
     * @param spuImages        效果图
     * @param materialIds      素材id
     * @param materialListBean 素材数据
     */
    public static void launch(Context context, String spuId, String designId, List<String> spuImages, List<String> materialIds, UnPayMaterialBean materialListBean) {
        Intent intent = new Intent(context, TemplatePurchaseActivity.class);
        intent.putExtra(EXTRA_KEY_MKU_ID, "");//mkuId为空 3d商品
        intent.putExtra(EXTRA_KEY_TEMPLATE_ID, spuId);
        intent.putExtra(EXTRA_KEY_SKU_IMAGES, "");
        intent.putExtra(EXTRA_KEY_DESIGN_ID, designId);
        intent.putExtra(EXTRA_KEY_SPU_IMAGES, (Serializable) spuImages);
        intent.putExtra(EXTRA_KEY_MATERIAL_ID, (Serializable) materialIds);
        intent.putExtra(EXTRA_KEY_MATERIAL_NEED_BUY_INFO, materialListBean);
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
        designId = getIntent().getStringExtra(EXTRA_KEY_DESIGN_ID);
        mPresenter = new TemplatePurchasePresenter(this, this);
        mkuId = getIntent().getStringExtra(EXTRA_KEY_MKU_ID);
        isTemplate = getIntent().getStringExtra(EXTRA_KEY_IS_TEMPLATE);
        materialIds = (List<String>) getIntent().getSerializableExtra(EXTRA_KEY_MATERIAL_ID);
        materialListBean = (UnPayMaterialBean) getIntent().getSerializableExtra(EXTRA_KEY_MATERIAL_NEED_BUY_INFO);

        if (StringUtil.isEmpty(mkuId)) {
            //请求3d待发布商品信息
            mPresenter.getTemplatePurchase3DDetail(getIntent().getStringExtra(EXTRA_KEY_TEMPLATE_ID));
        } else {
            skuImages = (List<TemplateImageInfo>) getIntent().getSerializableExtra(EXTRA_KEY_SKU_IMAGES);
            //请求2d待发布商品信息
            mPresenter.getTemplatePurchaseDetail(getIntent().getStringExtra(EXTRA_KEY_MKU_ID));
        }
    }

    /**
     * 2d详情
     *
     * @param data 详情
     */
    @Override
    public void templateDetail(TemplateInfoBean data) {
        this.data = data.mkus;
        this.dataInfo = data;
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
        if (data.mkus.skus.get(0).marketPriceY == null || data.mkus.skus.get(0).marketPriceY.isEmpty()) {
            data.mkus.skus.get(0).marketPriceY = data.mkus.skus.get(0).priceY;
            data.mkus.skus.get(0).marketPrice = data.mkus.skus.get(0).price;
        }
        mMarketPrice.setText("价格    ¥" + data.mkus.skus.get(0).marketPriceY);

        if (data.mkus.spuPropertyTypes.size() > 0) {
            //尺码规格  颜色 数据 列表
            List<PropertyBean> skuProperty = new ArrayList<>();
            for (PropertyBean properties : data.mkus.spuPropertyTypes) {
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

    /**
     * 3d 详情
     *
     * @param data 详情
     */
    @Override
    public void template3DDetail(TemplateInfoBean data) {
        this.data = data.sampleSpuModel;
        this.dataInfo = data;
        this.data.spuId = data.sampleSpuModel.sequenceNBR;
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
        mMarketPrice.setText("价格    ¥" + data.sampleSpuModel.marketPriceY);

        if (data.sampleSpuModel.spuPropertyTypes.size() > 0) {
            //尺码规格  颜色 数据 列表
            List<PropertyBean> skuProperty = new ArrayList<>();
            for (PropertyBean properties : data.sampleSpuModel.spuPropertyTypes) {
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

    @Override
    public void templateDesignProductDetail(GoodsDetailMallBean goodsDetailMallBean) {

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
                //获取skuId
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
                if (data.skus.size() > 1 && StringUtil.isEmpty(skuId)) {
                    showDialog();
                } else {
                    //判断库存
                    if (StringUtil.isEmpty(skuId)) {
                        skuId = data.skus.get(0).sequenceNBR;
                    }
                    if (mPresenter.verifyStoreNum(data, skuId, buyBum)) {
                        return;
                    }
                    //没用多余的规格可以选择
                    if (sku == null && data.skus.size() > 0) {
                        if (materialListBean != null && materialListBean.getSourceMaterialModels() != null && materialListBean.getSourceMaterialModels().size() > 0) {
                            mPresenter.buyTemplate(data, data.skus.get(0).sequenceNBR, buyBum, data.skus.get(0).skuValues,
                                    data.skus.get(0).marketPrice, spuImages, skuImages, designId, mkuId, isTemplate, materialIds, materialListBean.getSourceMaterialModels());
                        } else {
                            mPresenter.buyTemplate(data, data.skus.get(0).sequenceNBR, buyBum, data.skus.get(0).skuValues,
                                    data.skus.get(0).marketPrice, spuImages, skuImages, designId, mkuId, isTemplate, materialIds, null);
                        }
                    } else {
                        if (materialListBean != null && materialListBean.getSourceMaterialModels() != null && materialListBean.getSourceMaterialModels().size() > 0) {
                            mPresenter.buyTemplate(data, skuId, buyBum, sku.skuValues, sku.marketPrice, spuImages, skuImages, designId, mkuId, isTemplate,
                                    materialIds, materialListBean.getSourceMaterialModels());
                        } else {
                            mPresenter.buyTemplate(data, skuId, buyBum, sku.skuValues, sku.marketPrice, spuImages, skuImages, designId, mkuId, isTemplate, materialIds, null);
                        }
                    }
                }
                break;
            case R.id.iv_template_property_back:
                uiBottomSheet.dismiss();
                break;
            case R.id.iv_template_purchase_add:
                ++buyBum;
                mBuyNum.setText(String.valueOf(buyBum));
                StringUtil.setEditTextSection(mBuyNum);
                break;
            case R.id.iv_template_purchase_sub:
                if (buyBum > 1) {
                    --buyBum;
                    mBuyNum.setText(String.valueOf(buyBum));
                    StringUtil.setEditTextSection(mBuyNum);
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
