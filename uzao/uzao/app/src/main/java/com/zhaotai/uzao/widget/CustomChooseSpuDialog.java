package com.zhaotai.uzao.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.DesignPropertyAdapter;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.PropertyBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomChooseSpuDialog extends Dialog implements View.OnClickListener {

    private GoodsDetailMallBean detail = null;
    private Context mContext;
    private OnCloseListener listener;
    private DesignPropertyAdapter mAdapter;

    public CustomChooseSpuDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public CustomChooseSpuDialog setListener(OnCloseListener cancelListener) {
        this.listener = cancelListener;
        return this;
    }

    public void setSpuData(GoodsDetailMallBean detail) {
        this.detail = detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_spu);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        ImageView ivPic = (ImageView) findViewById(R.id.iv_product_image);
        ImageView ivCancel = (ImageView) findViewById(R.id.iv_cancel);

        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + detail.basicInfo.spuModel.thumbnail, ivPic);

        List<PropertyBean> skuProperty = new ArrayList<>();
        for (PropertyBean properties : detail.properties) {
            //提取sku数据
            if ("Y".equals(properties.isSku)) {
                skuProperty.add(properties);
            }
        }
        detail.properties = skuProperty;

        RecyclerView recycle = (RecyclerView) findViewById(R.id.rc_content);
        recycle.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new DesignPropertyAdapter();
        mAdapter.setNewData(detail.properties);
        recycle.setAdapter(mAdapter);
        TextView tvSure = (TextView) findViewById(R.id.tv_sure);
        ivCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                GoodsDetailMallBean.Sku sku = getSkuId(mAdapter, detail);
                if (sku != null) {
                    getMkuId(sku.sequenceNBR);
                }
                break;

            case R.id.iv_cancel:
                dismiss();
                if (listener != null) {
                    listener.dialogClosed();
                }
                break;
        }
    }

    public interface OnCloseListener {
        void dialogClosed();

        void chooseMku(String mku);
    }

    /**
     * 获得mku信息
     *
     * @param skuId  skuId
     */
    public void getMkuId(String skuId) {
        Api.getDefault().getMkuId(skuId)
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String mkuId) {
                        if (listener != null) {
                            listener.chooseMku(mkuId);
                            dismiss();
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("获取失败");
                    }
                });
    }

    /**
     * 获取skuId
     */
    public GoodsDetailMallBean.Sku getSkuId(DesignPropertyAdapter propertyAdapter, GoodsDetailMallBean data) {
        List<String> skuKeys = new ArrayList<>();
        String skuName = "";
        if (propertyAdapter != null && data != null) {

            List<PropertyBean> list = propertyAdapter.getData();
            for (int i = 0; i < list.size(); i++) {
                PropertyBean property = list.get(i);
                if (!property.isSelect) {
                    ToastUtil.showShort("请选择" + property.propertyTypeName);
                    return null;
                }

                for (PropertyBean ww : property.spuProperties) {
                    if (ww.isSelect) {
                        skuKeys.add(ww.propertyCode);
                        skuName += ww.propertyValue + "  ";
                    }
                }
            }

            for (GoodsDetailMallBean.Sku sku : data.skus) {
                boolean isHave = true;
                for (String skuKey : skuKeys) {
                    if (!sku.skuKey.contains(skuKey)) {
                        isHave = false;
                    }
                }
                if (isHave) {
                    sku.skuName = skuName;
                    return sku;
                }
            }
            ToastUtil.showShort("此商品不能购买");
            return null;
        }
        return null;
    }
}