package com.zhaotai.uzao.ui.order.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryTagsBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bean.TemplateInfoBean;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.presenter.TemplatePutawayPresenter;
import com.zhaotai.uzao.ui.login.activity.ProtocolActivity;
import com.zhaotai.uzao.ui.order.adapter.TemplateSkuPriceAdapter;
import com.zhaotai.uzao.ui.order.contract.TemplatePutawayContract;
import com.zhaotai.uzao.ui.person.setting.ChangeTagsActivity;
import com.zhaotai.uzao.utils.GlideImageLoader;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.view.NestListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/9/8
 * Created by LiYou
 * Description : 上架商城
 */

public class TemplatePutawayActivity extends BaseActivity implements TemplatePutawayContract.View {

    //轮播图
    @BindView(R.id.banner_template_purchase)
    Banner mBanner;
    //成本价
//    @BindView(R.id.tv_template_price)
//    TextView mPrice;
    //商品编号
    @BindView(R.id.tv_template_id)
    TextView mId;
    //售卖价格列表
    @BindView(R.id.lv_template_putaway)
    NestListView mListView;
    //作品名称
    @BindView(R.id.et_template_putaway_name)
    EditText mEtName;
    //作品简介
    @BindView(R.id.et_template_putaway_des)
    EditText mEtDes;
    //设计理念
    @BindView(R.id.et_template_putaway_idea)
    EditText mEtIdea;
    //建议价格
    @BindView(R.id.tv_recommended_price)
    TextView mRecommendPrice;
    //协议
    @BindView(R.id.iv_template_purchase_select_protocol)
    ImageView mIvSelectProtocol;
    @BindView(R.id.tv_template_purchase_protocol)
    TextView mProtocol;

    //标签
    @BindView(R.id.tv_tag)
    TextView tv_tag;

    private List<TemplateBean.TagsBean> tags = new ArrayList<>();

    private static final String EXTRA_KEY_SKU_IMAGES = "extra_key_sku_images";
    //主图
    private static final String EXTRA_KEY_SPU_IMAGES = "extra_key_spu_images";
    private static final String EXTRA_KEY_IS_TEMPLATE = "extra_key_is_template";
    private static final String EXTRA_KEY_DESIGN_ID = "extra_key_design_id";
    private static final String EXTRA_KEY_MKU_ID = "extra_key_mku_id";
    private static final String EXTRA_KEY_TYPE = "extra_key_type"; // 2d 3d
    private static final String EXTRA_SPU_ID = "extra_spu_id";
    private static final String EXTRA_MATERIAL_IDS = "extra_key_material_ids";
    private TemplatePutawayPresenter mPresenter;
    private List<String> spuImages;
    private List<TemplateImageInfo> skuImages;
    private TemplateInfoBean datas;
    private TemplateSkuPriceAdapter mAdapter;
    private String designId;
    private String isTemplate;
    private String type;
    private List<String> materialIds;
    private boolean isSelectProtocol = false;

    /**
     * 上架商品带素材
     *
     * @param context     上下文
     * @param mkuId       mkuId
     * @param spuImages   效果图
     * @param skuImages   sku图片
     * @param designId    设计id
     * @param isTemplate  是否模板
     * @param type        类型
     * @param materialIds 素材id
     */
    public static void launch(Context context, String mkuId, List<String> spuImages, List<TemplateImageInfo> skuImages, String designId, String isTemplate, String type,
                              List<String> materialIds) {
        Intent intent = new Intent(context, TemplatePutawayActivity.class);
        intent.putExtra(EXTRA_KEY_SPU_IMAGES, (Serializable) spuImages);
        intent.putExtra(EXTRA_KEY_SKU_IMAGES, (Serializable) skuImages);
        intent.putExtra(EXTRA_KEY_MKU_ID, mkuId);
        intent.putExtra(EXTRA_KEY_IS_TEMPLATE, isTemplate);
        intent.putExtra(EXTRA_KEY_DESIGN_ID, designId);
        intent.putExtra(EXTRA_KEY_TYPE, type);
        intent.putExtra(EXTRA_MATERIAL_IDS, (Serializable) materialIds);
        context.startActivity(intent);
    }

    /**
     * 上架商品
     *
     * @param context    上下文
     * @param mkuId      mkuId
     * @param spuImages  效果图
     * @param skuImages  sku图片
     * @param designId   设计id
     * @param isTemplate 是否模板
     * @param type       类型
     */
    public static void launch(Context context, String mkuId, List<String> spuImages, List<TemplateImageInfo> skuImages, String designId, String isTemplate, String type) {
        Intent intent = new Intent(context, TemplatePutawayActivity.class);
        intent.putExtra(EXTRA_KEY_SPU_IMAGES, (Serializable) spuImages);
        intent.putExtra(EXTRA_KEY_SKU_IMAGES, (Serializable) skuImages);
        intent.putExtra(EXTRA_KEY_MKU_ID, mkuId);
        intent.putExtra(EXTRA_KEY_IS_TEMPLATE, isTemplate);
        intent.putExtra(EXTRA_KEY_DESIGN_ID, designId);
        intent.putExtra(EXTRA_KEY_TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 3D 上架商品
     *
     * @param context     上下文
     * @param spuImages   效果图
     * @param designId    设计id
     * @param spuId       商品id
     * @param materialIds 素材id
     */
    public static void launch3D(Context context, List<String> spuImages, String designId, String spuId, List<String> materialIds) {
        Intent intent = new Intent(context, TemplatePutawayActivity.class);
        intent.putExtra(EXTRA_KEY_SPU_IMAGES, (Serializable) spuImages);
        intent.putExtra(EXTRA_KEY_DESIGN_ID, designId);
        intent.putExtra(EXTRA_KEY_TYPE, "3d");
        intent.putExtra(EXTRA_SPU_ID, spuId);
        intent.putExtra(EXTRA_MATERIAL_IDS, (Serializable) materialIds);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_template_putaway);
        mTitle.setText("上架商城");
        designId = getIntent().getStringExtra(EXTRA_KEY_DESIGN_ID);
        isTemplate = getIntent().getStringExtra(EXTRA_KEY_IS_TEMPLATE);
        mProtocol.setText("《版权协议》");
        EventBus.getDefault().register(this);
        mPresenter = new TemplatePutawayPresenter(this, this);
    }

    @Override
    protected void initData() {

        type = getIntent().getStringExtra(EXTRA_KEY_TYPE);
        materialIds = (List<String>) getIntent().getSerializableExtra(EXTRA_MATERIAL_IDS);
        switch (type) {
            case "2d":
                skuImages = (List<TemplateImageInfo>) getIntent().getSerializableExtra(EXTRA_KEY_SKU_IMAGES);
                mPresenter.getTemplateDetail(getIntent().getStringExtra(EXTRA_KEY_MKU_ID));
                break;
            case "3d":
                mPresenter.getTemplatePutaway3DDetail(getIntent().getStringExtra(EXTRA_SPU_ID));
                break;
        }
        //主图
        spuImages = getIntent().getStringArrayListExtra(EXTRA_KEY_SPU_IMAGES);


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
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @Override
    public void templateDetail(final TemplateInfoBean data) {
        this.datas = data;
        //订单id
        mId.setText("商品编号    " + data.sampleSpuModel.spuModel.spuCode);
        //建议价格
        mRecommendPrice.setText("售卖价格建议不超过" + data.sampleSpuModel.recommendedPriceY + "元");

        switch (type) {
            case "2d":
                //默认第一个选中
                if (this.datas.mkus.skus.size() > 0) {
                    this.datas.mkus.productSkus.get(0).isSelected = true;
                }
                mAdapter = new TemplateSkuPriceAdapter(this.datas.mkus.productSkus, mContext);
                mListView.setAdapter(mAdapter);
                break;
            case "3d":
                //默认第一个选中
                if (this.datas.sampleSpuModel.skus.size() > 0) {
                    this.datas.sampleSpuModel.skus.get(0).isSelected = true;
                }
                mAdapter = new TemplateSkuPriceAdapter(this.datas.sampleSpuModel.skus, mContext);
                mListView.setAdapter(mAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        datas.sampleSpuModel.skus.get(position).isSelected = !datas.sampleSpuModel.skus.get(position).isSelected;
                        mAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    /**
     * 申请上架
     */
    @OnClick(R.id.tv_template_putaway)
    public void putawayToStore() {
        if (!isSelectProtocol) {
            ToastUtil.showShort("请同意版权协议");
            return;
        }
        if (datas != null) {

            switch (type) {
                case "2d":
                    mPresenter.putawayToStore2D(mEtName.getText().toString(), mEtDes.getText().toString(), mEtIdea.getText().toString(),
                            datas.mkus, designId, isTemplate, spuImages, skuImages, materialIds, tags);
                    break;
                case "3d":
//                    mPresenter.putawayToStore3D(mEtName.getText().toString(), mEtDes.getText().toString(), mEtIdea.getText().toString(),
//                            mEtPrice.getText().toString(), datas.sampleSpuModel, designId, isTemplate, spuImages, materialIds, tags);
//
                    mPresenter.putawayToStore3D(mEtName.getText().toString(), mEtDes.getText().toString(), mEtIdea.getText().toString(),
                             datas.sampleSpuModel, designId, isTemplate, spuImages, materialIds, tags);
                    break;
            }
        }

    }


    /**
     * 选择标签
     */
    @OnClick(R.id.rl_add_arr)
    public void toTagePage() {

        ArrayList<String> myTags = new ArrayList<>();
        for (TemplateBean.TagsBean tags : datas.sampleSpuModel.tags) {
            myTags.add(tags.tagCode);
        }
        ChangeTagsActivity.launch(mContext, ChangeTagsActivity.TAG_TYPE_spu, myTags);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
        switch (event.getEventType()) {
            case EventBusEvent.CHOOSE_TAG_SPU_TAG_FINISH:
                if (tags == null) {
                    tags = new ArrayList<>();
                }
                ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> tagBeans = (ArrayList<CategoryTagsBean.ChildrenBean.TagsBean>) event.getEventObj();
                if (tagBeans.size() > 0) {
                    tags.clear();
                    StringBuilder tag = new StringBuilder();
                    for (int i = 0; i < tagBeans.size(); i++) {
                        CategoryTagsBean.ChildrenBean.TagsBean tagsBean = tagBeans.get(i);
                        if (tagsBean != null) {
                            TemplateBean.TagsBean mTagsBean = new TemplateBean.TagsBean();
                            mTagsBean.tagCode = tagsBean.getTagCode();
                            tags.add(mTagsBean);
                            tag.append(tagsBean.getTagName());
                            if (i != tagBeans.size() - 1) {
                                tag.append(",");
                            }
                        }
                    }
                    tv_tag.setText(tag);
                }
                break;
        }
    }


    @Override
    public void finishView() {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
