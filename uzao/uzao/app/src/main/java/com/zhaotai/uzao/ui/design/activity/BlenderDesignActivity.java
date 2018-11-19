package com.zhaotai.uzao.ui.design.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pingplusplus.android.Pingpp;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;
import com.zhaotai.uzao.bean.ThreeDimensionalBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.adapter.MaterialAdapter;
import com.zhaotai.uzao.ui.design.adapter.MaterialColorAdapter;
import com.zhaotai.uzao.ui.design.bean.DesignMetaBean;
import com.zhaotai.uzao.ui.design.bean.MaterialBean;
import com.zhaotai.uzao.ui.design.bean.MaterialColorBean;
import com.zhaotai.uzao.ui.design.contract.BlenderDesignContract;
import com.zhaotai.uzao.ui.design.presenter.BlenderDesignPresenter;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.BuyOrPutAwayDialog;
import com.zhaotai.uzao.widget.RenewalPutAwayDialog;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/9/19
 * Created by LiYou
 * Description :  3D 制造页面
 */

public class BlenderDesignActivity extends BaseActivity implements BlenderDesignContract.View {

    @BindView(R.id.ll_blender_design_content)
    LinearLayout mContentView;
    //材质颜色
    @BindView(R.id.ll_blender_material)
    LinearLayout mLlMaterial;
    //动效
    @BindView(R.id.tv_3d_change)
    TextView tv3dChange;

    @BindView(R.id.recycler_material)
    RecyclerView mMaterialRecycler;

    @BindView(R.id.recycler_material_color)
    RecyclerView mMaterialColorRecycler;

    @BindView(R.id.webview_blender_design)
    WebView mWebView;

    //素材购买弹出框
    private UIBottomSheet uiBuyMaterialPop;

    private Gson gson = new Gson();

    private ThreeDimensionalBean data;
    private BlenderDesignPresenter mPresenter;
    private static final String EXTRA_KEY_SPU_ID = "extra_key_template_id";
    private static final String EXTRA_KEY_DESIGN_ID = "extra_key_design_id";

    //区域面的名字
    private String areasName;
    private String spuId;
    //选中面
    private String selectableName = "";
    //未选中面
    private String unSelectableName = "[";

    private List<DesignMetaBean> metaData = new ArrayList<>();

    private boolean nextStep;//true //购买 false 上架

    // 0 是保存 1是购买上架
    int saveType = 0;
    /**
     * 设计Id 用于判断是否恢复元数据
     */
    private String designId;
    private boolean isLoadComplete = false;//判断模型是否加载完成
    private MaterialAdapter mMaterialAdapter;
    private MaterialColorAdapter mMaterialAdapterColor;
    private StringBuilder mSelectableBuilder;

    /**
     * 3d 定制
     *
     * @param context 上下文
     * @param spuId   商品Id
     */
    public static void launch(Context context, String spuId) {
        Intent intent = new Intent(context, BlenderDesignActivity.class);
        intent.putExtra(EXTRA_KEY_SPU_ID, spuId);
        context.startActivity(intent);
    }

    /**
     * 我的设计
     *
     * @param context  上下文
     * @param spuId    商品Id
     * @param designId 设计Id
     */
    public static void launch(Context context, String spuId, String designId) {
        Intent intent = new Intent(context, BlenderDesignActivity.class);
        intent.putExtra(EXTRA_KEY_SPU_ID, spuId);
        intent.putExtra(EXTRA_KEY_DESIGN_ID, designId);
        context.startActivity(intent);
    }

    @Override
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    protected void initView() {
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_blender_design);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setJavaScriptEnabled(true);
        mPresenter = new BlenderDesignPresenter(this, this);
    }

    @Override
    public void handleStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_black));
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        spuId = intent.getStringExtra(EXTRA_KEY_SPU_ID);
        designId = intent.getStringExtra(EXTRA_KEY_DESIGN_ID);
        mPresenter.get3dDetail(spuId);
    }


    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    /**
     * 初始化材质颜色
     */
    private void initMaterialAndColor(String optionMeta) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = gson.fromJson(optionMeta, type);
        //设置材质的数据
        final ArrayList<MaterialBean> materialList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            materialList.add(gson.fromJson(jsonObject, MaterialBean.class));
        }
        //材质
        if (mMaterialAdapter == null) {
            mMaterialAdapter = new MaterialAdapter(materialList);
            mMaterialRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mMaterialRecycler.setAdapter(mMaterialAdapter);
        } else {
            mMaterialAdapter.setNewData(materialList);
        }
        mMaterialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < materialList.size(); i++) {
                    materialList.get(i).isSelected = i == position;
                }
                mMaterialAdapter.notifyDataSetChanged();

                if (mMaterialAdapterColor != null) {
                    for (int i = 0; i < materialList.get(position).colours.size(); i++) {
                        materialList.get(position).colours.get(i).isSelected = false;
                    }
                    mMaterialAdapterColor.setNewData(materialList.get(position).colours);
                    //更换材质
                    mPresenter.changeMaterial(mWebView, materialList.get(position).textureImage, areasName, metaData);
                }
            }
        });


        //颜色
        if (mMaterialAdapterColor == null && materialList.size() > 0) {
            mMaterialAdapterColor = new MaterialColorAdapter(materialList.get(0).colours);
            mMaterialColorRecycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mMaterialColorRecycler.setAdapter(mMaterialAdapterColor);
        } else if (materialList.size() > 0) {
            mMaterialAdapterColor.setNewData(materialList.get(0).colours);
        }
        mMaterialAdapterColor.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<MaterialColorBean> colors = mMaterialAdapterColor.getData();
                for (int i = 0; i < colors.size(); i++) {
                    colors.get(i).isSelected = position == i;
                }
                mMaterialAdapterColor.notifyDataSetChanged();
                //更换颜色
                mPresenter.changeColor(mWebView, colors.get(position).colourImage, areasName, metaData);
            }
        });
    }


    /**
     * 保存设计
     * 调用WebView单张截图方法 上传单张图片 调用保存/修改设计接口
     */
    @OnClick(R.id.iv_save)
    public void onClickSaveDesign() {
        checkAndSave(true);
    }

    /**
     * 设计完成 选择上架或者购买
     * 1需要检查没有包含下架商品并保存
     */
    @OnClick(R.id.iv_ok)
    public void onClickSubmitDesign() {
        checkAndSave(false);
    }

    /**
     * 检查和保存
     * 设置参数 是单纯保存 还是保存成功之后是上架购买
     *
     * @param saveOrFinish true 保存  false 上架or购买
     */
    private void checkAndSave(boolean saveOrFinish) {
        //检查登录状态 未登录的话跳转到登录页面
        if (LoginHelper.getLoginStatus()) {
//            1检查目前的设计有没有下架的数据
            saveType = saveOrFinish ? 0 : 1;
            ValidateProductBean validateProductBean = mPresenter.getCheckedDate(metaData);
            boolean b = showOverdue(validateProductBean, false);
            if (!b) {
                mPresenter.shotSingle(mWebView, mSelectableBuilder.toString());
            }
        } else {
            LoginHelper.goLogin(this);
        }

    }

    /**
     * 弹出购买或者上架的弹窗
     */
    private void showBuyOrPutAwayDialog() {
        if (isLoadComplete) {
            BuyOrPutAwayDialog dialog = new BuyOrPutAwayDialog(mContext, R.style.CommonDialog, getString(R.string.design_finish_notice), new BuyOrPutAwayDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    nextStep = confirm;
                    //请求素材ids
                    mPresenter.getUnBoughtMaterial(mWebView, metaData, nextStep);
                    dialog.dismiss();

                }
            });
            dialog.setTitle(getString(R.string.warm_prompt)).show();
            Window dialogWindow = dialog.getWindow();
            if (dialogWindow != null) {
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                lp.width = (int) (ScreenUtils.getScreenWidth(mContext) - PixelUtil.dp2px(37 * 2));
                // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
                // dialog.onWindowAttributesChanged(lp);
                dialogWindow.setAttributes(lp);
            }
        }
    }

    /**
     * 初始化购买素材弹窗
     */
    @Override
    public void showBuyMaterialDialog(final UnPayMaterialBean materialListBean) {
        if (uiBuyMaterialPop == null) {
            uiBuyMaterialPop = new UIBottomSheet(mContext);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.pop_item_buy_material, null);

            //关闭
            ImageView ivClose = (ImageView) bottomSheetView.findViewById(R.id.iv_buy_material_close);
            final ImageView ivWx = (ImageView) bottomSheetView.findViewById(R.id.pay_way_wx_image);
            final ImageView ivzfb = (ImageView) bottomSheetView.findViewById(R.id.pay_way_zfb_image);
            //支付宝
            final RelativeLayout rlzfb = (RelativeLayout) bottomSheetView.findViewById(R.id.pay_way_zfb);
            RelativeLayout rlwx = (RelativeLayout) bottomSheetView.findViewById(R.id.pay_way_wx);

            TextView tv_to_pay = (TextView) bottomSheetView.findViewById(R.id.tv_to_pay);
            TextView tv_price = (TextView) bottomSheetView.findViewById(R.id.tv_price);
//            tv_price.setText("￥" + materialListBean.getTotalAmountY());
            tv_price.setText(getString(R.string.money_price, materialListBean.getTotalAmountY()));


            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.iv_buy_material_close:
                            uiBuyMaterialPop.dismiss();
                            break;

                        case R.id.pay_way_zfb:
                            ivWx.setSelected(false);
                            ivzfb.setSelected(true);
                            break;

                        case R.id.pay_way_wx:
                            ivWx.setSelected(true);
                            ivzfb.setSelected(false);
                            break;

                        case R.id.tv_to_pay:
                            if (ivWx.isSelected()) {
                                //微信支付被选中
                                mPresenter.getMaterialPayInfo("wx", materialListBean.getSourceMaterialModels());
                            } else {
                                //支付宝支付被选中
                                mPresenter.getMaterialPayInfo("alipay", materialListBean.getSourceMaterialModels());
                            }
                            uiBuyMaterialPop.dismiss();
                            break;
                    }
                }
            };

            ivClose.setOnClickListener(onClickListener);
            rlzfb.setOnClickListener(onClickListener);
            rlwx.setOnClickListener(onClickListener);
            tv_to_pay.setOnClickListener(onClickListener);
            uiBuyMaterialPop.setContentView(bottomSheetView);
            uiBuyMaterialPop.show();
        } else {
            uiBuyMaterialPop.show();
        }
    }

    /**
     * 单张截图成功 调用保存/修改设计方法
     *
     * @param thumbnail 设计id
     */
    @Override
    public void showDesignImage(String thumbnail) {
        if (!StringUtil.isEmpty(thumbnail)) {
            if (StringUtil.isEmpty(designId)) {
                mPresenter.saveMyDesign(spuId, thumbnail, metaData);
            } else {
                mPresenter.changeMyDesign(thumbnail, metaData);
            }
        } else {
            ToastUtil.showShort("数据保存失败");
        }
    }

    /**
     * 显示过期弹窗
     *
     * @param resultMetaDataBean 过期数据
     * @param overdueAndUnShelve 是否包含过期并且下架
     * @return 是否包含过滤出来的元素
     */
    @Override
    public boolean showOverdue(ValidateProductBean resultMetaDataBean, boolean overdueAndUnShelve) {
        StringBuilder wordTip = new StringBuilder();
//        1过期素材 ： 您使用的素材%@已过期，请及时续费素材
        if (resultMetaDataBean.material.expiredPublished != null && resultMetaDataBean.material.expiredPublished.size() != 0 && overdueAndUnShelve) {
            wordTip.append("您使用的素材<font color=\'#d30000\'>");
            for (int i = 0; i < resultMetaDataBean.material.expiredPublished.size(); i++) {
                wordTip.append(resultMetaDataBean.material.expiredPublished.get(i).sourceMaterialName);
                if (i != resultMetaDataBean.material.expiredPublished.size() - 1) {
                    wordTip.append(",");
                }
            }
            wordTip.append("</font>已过期，请及时续费素材");
        }
        //过期下架素材  ：您使用的素材%@已过期，并已下架，无法继续使用
        if (resultMetaDataBean.material.notAvailable != null && resultMetaDataBean.material.notAvailable.size() != 0) {
            if (!StringUtil.isEmpty(wordTip.toString())) {
                wordTip.append("<br><br>");
            }
            wordTip.append("您使用的素材<font color=\'#d30000\'>");
            for (int i = 0; i < resultMetaDataBean.material.notAvailable.size(); i++) {
                wordTip.append(resultMetaDataBean.material.notAvailable.get(i).sourceMaterialName);
                if (i != resultMetaDataBean.material.notAvailable.size() - 1) {
                    wordTip.append(",");
                }
            }
            wordTip.append("</font>已过期，并已下架，无法继续使用");
        }
        //有无过期文字  您使用的艺术字由于部分原因已下架，将不能在设计中继续使用，请知悉
        if (resultMetaDataBean.wordart != null && resultMetaDataBean.wordart.size() != 0) {
            if (!StringUtil.isEmpty(wordTip.toString())) {
                wordTip.append("<br><br>");
            }
            String artText = "<font color=\"#d30000\">艺术字</font>";
            wordTip.append("您使用的");
            wordTip.append(artText);
            wordTip.append("由于部分原因已下架，将不能在设计中继续使用，请知悉");
//            wordTip.append("您使用的<font color=\\'#d30000\\'>艺术字</font>由于部分原因已下架，将不能在设计中继续使用，请知悉");
        }
        //是否有过期数据
        if (!StringUtil.isEmpty(wordTip.toString())) {
            Log.d("xxoo", "showOverdue: " + wordTip.toString());
            RenewalPutAwayDialog dialog = RenewalPutAwayDialog.newInstance(0);
            dialog.show(this.getFragmentManager(), "RenewalDialog");
            dialog.setContentTxt(wordTip.toString());
            dialog.setListener(new RenewalPutAwayDialog.OnCloseListener() {
                @Override
                public void onClick(DialogFragment dialog, boolean confirm) {
                    //点击确定 取消
                    dialog.dismiss();
                }
            });
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存/修改设计成功
     *
     * @param designId 设计id
     */
    @Override
    public void saveDesignSuccess(String designId) {
        //保存设计按钮
        this.designId = designId;
        if (saveType == 0) {
            //保存设计 给提示就行
            ToastUtil.showShort("保存作品成功");
        }else {
            //上架或者购买 弹框
            showBuyOrPutAwayDialog();
        }

    }



    /**
     * 关闭页面
     */
    @OnClick(R.id.iv_blender_design_back)
    public void activityFinish() {
        finish();
    }


    /**
     * 去设计
     */
    @OnClick(R.id.tv_bottom_design)
    public void onClickDesign() {
        if (isLoadComplete) {
            mPresenter.gotoDesign(metaData, data, areasName);
        }
    }

    /**
     * 材质
     */
    @OnClick(R.id.tv_bottom_material)
    public void onClickMaterial() {
        if (!isLoadComplete) return;
        for (int i = 0; i < data.sample3dObjectModels.size(); i++) {
            if (areasName.equals(data.sample3dObjectModels.get(i).name) && "Y".equals(data.sample3dObjectModels.get(i).canChangeTexture)) {
                mLlMaterial.setVisibility(View.VISIBLE);
                tv3dChange.setVisibility(View.GONE);
                String optionMeta = data.sample3dObjectModels.get(i).sample3dOptionModel.optionMeta;
                if (!StringUtil.isEmpty(optionMeta)) {
                    initMaterialAndColor(data.sample3dObjectModels.get(i).sample3dOptionModel.optionMeta);
                }
                return;
            }
        }
        ToastUtil.showShort("此面不能更换材质");
    }

    /**
     * 还原3d数据
     *
     * @param data 3d数据
     */
    @Override
    public void showDetail(final ThreeDimensionalBean data) {
        this.data = data;
        //可选择面
        ArrayList<String> mSelectableName = new ArrayList<>();
        //不可选择面
        List<String> unSelectableList = new ArrayList<>();
        //添加不可选择面
        for (int i = 0; i < data.sample3dObjectModels.size(); i++) {
            if (data.sample3dObjectModels.get(i).selectable.equals("N")) {
                unSelectableList.add(data.sample3dObjectModels.get(i).name);
            }
        }

        for (int i = 0; i < data.sample3dObjectModels.size(); i++) {
            if (data.sample3dObjectModels.get(i).selectable.equals("Y")) {
                selectableName = data.sample3dObjectModels.get(i).name;
                mSelectableName.add(selectableName);
                areasName = selectableName;
                //判断是否恢复元数据
                DesignMetaBean meta = new DesignMetaBean();
                meta.aspectId = selectableName;
                metaData.add(meta);
            }
        }
        //拼接不可选择面String
        if (unSelectableList.size() > 0) {
            for (int i = 0; i < unSelectableList.size(); i++) {
                if (i != unSelectableList.size() - 1) {
                    unSelectableName += "'" + unSelectableList.get(i) + "',";
                } else {
                    unSelectableName += "'" + unSelectableList.get(i) + "']";
                }
            }
        } else {
            unSelectableName = "''";
        }
        //拼接 可选择面
        mSelectableBuilder = new StringBuilder("[");
        if (mSelectableName.size() > 0) {
            for (int i = 0; i < mSelectableName.size(); i++) {
                if (i != mSelectableName.size() - 1) {
                    mSelectableBuilder.append("'")
                            .append(mSelectableName.get(i))
                            .append("',");
                } else {
                    mSelectableBuilder.append("'")
                            .append(mSelectableName.get(i))
                            .append("']");
                }
            }
        } else {
            mSelectableBuilder.delete(0, mSelectableBuilder.length());
            mSelectableBuilder.append("''");
        }

        mWebView.addJavascriptInterface(new AndroidtoJs(), "Native");//AndroidtoJS类对象映射到js的app对象
        final String baseUrl = SPUtils.getSharedStringData(GlobalVariable.BASE_URL);
        mWebView.loadUrl(baseUrl + ApiConstants.MODEL_3D_HTML);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String jsonName = baseUrl + data.jsonName;
                String stringBuilder = "javascript:M.init('" +
                        jsonName + "',{'iconClick':'iconClickCallBack','selectableName': '"
                        + selectableName + "','unselectable':" + unSelectableName +
                        ",'recieveShotImages':'recieveShotImages','shotSingle':'shotSingle'})";
                mWebView.evaluateJavascript(stringBuilder
                        , new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                            }
                        });
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }
        });
    }


    /**
     * 恢复原数据
     *
     * @param designMetaBeen 设计数据
     */
    @Override
    public void showMetaDetail(List<DesignMetaBean> designMetaBeen) {
        this.metaData.clear();
        this.metaData = designMetaBeen;
        mPresenter.recoverDesign(mWebView, this.metaData);
    }

    /**
     * 上传截图成功
     *
     * @param imageNames 图片地址列表
     */
    @Override
    public void successUploadImage(List<String> imageNames) {
        mPresenter.putAwayOrBuy(nextStep, spuId, designId, metaData, imageNames);
    }

    @Override
    public void showProgress() {
        showLoadingDialog();
    }

    @Override
    public void stopProgress() {
        disMisLoadingDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
            String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
            String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
            String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            LogUtils.logd("支付错误信息,errorMsg:" + errorMsg + "---- extraMsg:" + extraMsg);
            mPresenter.callback();
            if ("success".equals(result)) {
                //支付成功
                mPresenter.finishDesign(mWebView);
            } else {
                //支付失败
                //支付失败
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("支付失败,是否重新支付?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.repayInfo();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }


    /**
     * 接受素材库  作品过来的
     *
     * @param event 数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean event) {
        if (event.getEventType().equals(EventBusEvent.DESIGN_3D_UV_FINISH)) {
            LayerMetaJsonBean data = (LayerMetaJsonBean) event.getEventObj();
            Log.d("eventbus", "onMessageEvent: " + data);
            for (int i = 0; i < metaData.size(); i++) {
                if (metaData.get(i).aspectId.equals(data.getAspectId())) {
                    metaData.get(i).layerMeta = data.getLayerMeta();
                    metaData.get(i).thumbnail = ApiConstants.UZAOCHINA_IMAGE_HOST + data.getThumbnail();
                    metaData.get(i).craftId = data.getCraftId();
                    metaData.get(i).materialIds = data.getMaterialIds();
                    //截图 贴图
                    mPresenter.postImageTo3DModel(mWebView, areasName, data.getThumbnail());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**
     * webView点击的回调接收
     */
    private class AndroidtoJs {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void iconClickCallBack(String objName) {
            CallBackBean callBackBean = gson.fromJson(objName, CallBackBean.class);
            areasName = callBackBean.parameters;
            //隐藏选择界面素材
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLlMaterial.setVisibility(View.GONE);
                    tv3dChange.setVisibility(View.VISIBLE);
                }
            });
            //请求原数据
            if (!StringUtil.isEmpty(designId) && !isLoadComplete) {
                mPresenter.setDesignId(designId);
                mPresenter.getMyDesign(designId);
            }
            isLoadComplete = true;
        }

        /**
         * 截图成功回调
         */
        @JavascriptInterface
        public void recieveShotImages(String json) {
            CallBackImageBean callBackBean = gson.fromJson(json, CallBackImageBean.class);
            //上传图片
            mPresenter.upload64Image(callBackBean.parameters);
        }

        /**
         * 单张截图成功
         */
        @JavascriptInterface
        public void shotSingle(String json) {
            CallBackBean callBackBean = gson.fromJson(json, CallBackBean.class);
            mPresenter.uploadSingle64Image(callBackBean.parameters);
        }
    }

    /**
     * 点击回调数据是单个bean
     */
    private class CallBackBean {
        public String handlerInterface;
        public String function;
        public String parameters;
    }

    /**
     * 点击回调数据是list的bean
     */
    private class CallBackImageBean {
        public String handlerInterface;
        public String function;
        public List<String> parameters;
    }
}
