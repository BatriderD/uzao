package com.zhaotai.uzao.ui.person.myproduct;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.FilterBean;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.person.myproduct.adapter.MyProductDetailSkuPriceAdapter;
import com.zhaotai.uzao.utils.GlideImageLoader;
import com.zhaotai.uzao.view.NestListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2017/9/12
 * Created by LiYou
 * Description : 我的商品详情
 */

public class MyProductDetailActivity extends BaseActivity {

    //轮播图
    @BindView(R.id.banner_my_product_detail)
    Banner mBanner;
    //商品编号
    @BindView(R.id.tv_template_id)
    TextView mId;

//    //建议价格
    @BindView(R.id.tv_recommended_price)
    TextView mRecommendPrice;

    //售卖规格列表
    @BindView(R.id.lv_my_product_detail)
    NestListView mListView;
    //作品名称
    @BindView(R.id.tv_my_product_detail_name)
    TextView mTvName;
    //作品简介
    @BindView(R.id.tv_my_product_detail_des)
    TextView mTvDes;
    //设计理念
    @BindView(R.id.tv_my_product_detail_idea)
    TextView mTvIdea;

    //标签
    @BindView(R.id.tv_tag)
    TextView tv_tag;

    private String spuId;

    private static final String EXTRA_KEY_TEMPLATE_SPU_ID = "EXTRA_KEY_TEMPLATE_SPU_ID";

    public static void launch(Context context, String spuId) {
        Intent intent = new Intent(context, MyProductDetailActivity.class);
        intent.putExtra(EXTRA_KEY_TEMPLATE_SPU_ID, spuId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_product_detail);
        mTitle.setText("商品详情");
    }

    @Override
    protected void initData() {
        spuId = getIntent().getStringExtra(EXTRA_KEY_TEMPLATE_SPU_ID);
        Api.getDefault().getMyProductDetail(spuId)
                .compose(RxHandleResult.<TemplateBean>handleResult())
                .subscribe(new RxSubscriber<TemplateBean>(mContext, true) {
                    @Override
                    public void _onNext(TemplateBean templateBean) {
                        bindData(templateBean);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    private void bindData(TemplateBean data) {
        //轮播图
        List<String> image = new ArrayList<>();
        if (data.basicInfo.spuImages != null) {
            for (String img : data.basicInfo.spuImages) {
                image.add(ApiConstants.UZAOCHINA_IMAGE_HOST + img);
            }
        }
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(image);
        mBanner.isAutoPlay(false);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        //订单id
        mId.setText("商品编号    " + spuId);
        //建议价格
        mRecommendPrice.setText("售卖价格建议不超过" + data.sampleSpu.recommendedPriceY + "元");

        //售卖规格
        MyProductDetailSkuPriceAdapter mAdapter = new MyProductDetailSkuPriceAdapter(data.skus, data.sampleSpu.recommendedPriceY, mContext);
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(0);
        //商品名字
        mTvName.setText(data.basicInfo.spuName);
        //商品简介
        mTvDes.setText(data.basicInfo.spuModel.description);
        //商品理念
        mTvIdea.setText(data.basicInfo.designIdea);
//        标签
        List<FilterBean> tags = data.basicInfo.tags;
        if (tags != null) {
            StringBuilder tag = new StringBuilder();
            for (int i = 0; i < tags.size(); i++) {
                FilterBean tagsBean = tags.get(i);
                if (tagsBean != null) {
                    tag.append(tagsBean.tagName);
                    if (i != tags.size() - 1) {
                        tag.append(",");
                    }
                }
            }
            tv_tag.setText(tag);
        }

    }
}
