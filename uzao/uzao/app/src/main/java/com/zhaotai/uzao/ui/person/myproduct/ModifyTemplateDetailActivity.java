package com.zhaotai.uzao.ui.person.myproduct;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryTagsBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.FilterBean;
import com.zhaotai.uzao.bean.TemplateBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.login.activity.ProtocolActivity;
import com.zhaotai.uzao.ui.person.myproduct.adapter.ModifyTemplateSkuPriceAdapter;
import com.zhaotai.uzao.ui.person.myproduct.contract.ModifyTemplateDetailContract;
import com.zhaotai.uzao.ui.person.myproduct.presenter.ModifyTemplateDetailPresenter;
import com.zhaotai.uzao.ui.person.setting.ChangeTagsActivity;
import com.zhaotai.uzao.utils.GlideImageLoader;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.view.NestListView;
import com.zhaotai.uzao.widget.RenewalPutAwayDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/9/8
 * Created by LiYou
 * Description : 重新编辑-上架商城 信息
 */

public class ModifyTemplateDetailActivity extends BaseActivity implements ModifyTemplateDetailContract.View {

    //轮播图
    @BindView(R.id.banner_template_purchase)
    Banner mBanner;
    //订单编号
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

    //标签rl
    @BindView(R.id.rl_add_arr)
    RelativeLayout mRlArr;

    //标签
    @BindView(R.id.tv_tag)
    TextView tv_tag;

    @BindView(R.id.iv_template_purchase_select_protocol)
    ImageView mIvSelectProtocol;
    @BindView(R.id.tv_template_purchase_protocol)
    TextView mProtocol;
    private RenewalPutAwayDialog dialog;

    private static final String EXTRA_KEY_TEMPLATE_SPU_ID = "extra_key_template_spu_id";
    private static final String EXTRA_KEY_TEMPLATE_ADAPTER_POSITION = "extra_key_template_adapter_position";
    private ModifyTemplateDetailPresenter mPresenter;
    private TemplateBean data;
    private String spuId;
    private int position;
    private List<TemplateBean.TagsBean> tags = new ArrayList<>();
    private boolean isSelectProtocol = false;

    public static void launch(Context context, String spuId, int position) {
        Intent intent = new Intent(context, ModifyTemplateDetailActivity.class);
        intent.putExtra(EXTRA_KEY_TEMPLATE_SPU_ID, spuId);
        intent.putExtra(EXTRA_KEY_TEMPLATE_ADAPTER_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_template_putaway);
        mTitle.setText("编辑商品");
        mProtocol.setText("《版权协议》");
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mPresenter = new ModifyTemplateDetailPresenter(this, this);
        spuId = getIntent().getStringExtra(EXTRA_KEY_TEMPLATE_SPU_ID);
        position = getIntent().getIntExtra(EXTRA_KEY_TEMPLATE_ADAPTER_POSITION, 0);

        mPresenter.getData(spuId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void bindData(TemplateBean data) {
        this.data = data;
        //轮播图
        List<String> image = new ArrayList<>();
        for (String img : data.basicInfo.spuImages) {
            image.add(ApiConstants.UZAOCHINA_IMAGE_HOST + img);
        }
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(image);
        mBanner.isAutoPlay(false);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        //售卖价格
//        mEtPrice.setText(data.basicInfo.salesPriceY);
        //成本价
//        mPrice.setText("￥" + data.sampleSpu.marketPriceY);
        //订单id
        mId.setText("商品编号    " + spuId);
        //建议价格
        if (!StringUtil.isEmpty(data.sampleSpu.recommendedPriceY)) {
            mRecommendPrice.setText("售卖价格建议不超过" + data.sampleSpu.recommendedPriceY + "元");
        }
        //商品类型
        ModifyTemplateSkuPriceAdapter mAdapter = new ModifyTemplateSkuPriceAdapter(data.skus, mContext);
        mListView.setAdapter(mAdapter);
        //商品名字
        mEtName.setText(data.basicInfo.spuName);
        //简介
        mEtDes.setText(data.basicInfo.spuModel.description);
        //商品理念
        mEtIdea.setText(data.basicInfo.designIdea);
        //标签
        List<FilterBean> tags = data.basicInfo.tags;
        if (tags != null) {
            for (int i = 0; i <tags.size(); i++) {
                FilterBean filterBean = tags.get(i);
                this.tags.add(new TemplateBean.TagsBean(filterBean.tagCode, filterBean.tagName));
            }
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

    /**
     * 显示素材过期提醒
     *
     * @param spuId 商品id
     * @param tip   提醒信息
     */
    @Override
    public void showExpireTip(final String spuId, String tip) {
        dialog = RenewalPutAwayDialog.newInstance(1);
        dialog.setContentTxt(tip);
        dialog.setStrSure("续费");
        dialog.show(getFragmentManager(), "RenewalDialog");
        dialog.setListener(new RenewalPutAwayDialog.OnCloseListener() {
            @Override
            public void onClick(DialogFragment dialog, boolean confirm) {
                //续费
                mPresenter.getRenewMaterial(spuId, position);
            }
        });
    }

    /**
     * 温馨提示 素材 或者 艺术字续费 或者过期
     *
     * @param tip 提示语
     */
    @Override
    public void showTip(String tip) {
        dialog = RenewalPutAwayDialog.newInstance(0);
        dialog.show(getFragmentManager(), "RenewalDialog");
        dialog.setContentTxt(tip);
        dialog.setListener(new RenewalPutAwayDialog.OnCloseListener() {
            @Override
            public void onClick(DialogFragment dialog, boolean confirm) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 关闭续费dialog
     */
    @Override
    public void dismissTipDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 续费成功
     */
    @Override
    public void renewSuccess() {
        //申请上架
        putawayToStore();
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
        if (data != null) {
//            mPresenter.putawayToStore(spuId, this.data, mEtName.getText().toString(), mEtDes.getText().toString(),
//                    mEtIdea.getText().toString(), mEtPrice.getText().toString(), tags, position);
            mPresenter.putawayToStore(spuId, this.data, mEtName.getText().toString(), mEtDes.getText().toString(),
                    mEtIdea.getText().toString(), tags, position);
        }
    }

    /**
     * 选择标签
     */
    @OnClick(R.id.rl_add_arr)
    public void toTagPage() {
        ArrayList<String> myTags = new ArrayList<>();
        for (TemplateBean.TagsBean tags : tags) {
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


    /**
     * 接收 标签
     *
     * @param event 标签选择
     */
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
