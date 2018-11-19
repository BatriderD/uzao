package com.zhaotai.uzao.ui.design.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pingplusplus.android.Pingpp;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaopo.flying.sticker.BitmapUtils;
import com.xiaopo.flying.sticker.FontCache;
import com.xiaopo.flying.sticker.MyDrawableSticker;
import com.xiaopo.flying.sticker.MyTextSticker;
import com.xiaopo.flying.sticker.SourceRectBean;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerDataInfo;
import com.xiaopo.flying.sticker.StickerView;
import com.yalantis.ucrop.UCrop;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.AddEditorMaterialBean;
import com.zhaotai.uzao.bean.ArtFontTopicBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.EventBean.EventDownSuccessFont;
import com.zhaotai.uzao.bean.EventBean.EventFontToDownLoadBean;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.bean.EventBean.TechnologyEvent;
import com.zhaotai.uzao.bean.GoodsDetailMallBean;
import com.zhaotai.uzao.bean.LayerDataBean;
import com.zhaotai.uzao.bean.LayerMetaJsonBean;
import com.zhaotai.uzao.bean.MKUCarrierBean;
import com.zhaotai.uzao.bean.MaterialDetailBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ResultMetaDataBean;
import com.zhaotai.uzao.bean.StickerPicInfoBean;
import com.zhaotai.uzao.bean.TechnologyBean;
import com.zhaotai.uzao.bean.ThreeDimensionalBean;
import com.zhaotai.uzao.bean.UnPayMaterialBean;
import com.zhaotai.uzao.bean.ValidateProductBean;
import com.zhaotai.uzao.bean.post.TemplateImageInfo;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.design.adapter.LayerAdapter;
import com.zhaotai.uzao.ui.design.adapter.SelectedAreaAdapter;
import com.zhaotai.uzao.ui.design.adapter.TextArtFontAdapter;
import com.zhaotai.uzao.ui.design.adapter.TextColorAdapter;
import com.zhaotai.uzao.ui.design.catchWord.activity.CatchWordListActivity;
import com.zhaotai.uzao.ui.design.contract.EditorContract;
import com.zhaotai.uzao.ui.design.fragment.EditorFragment;
import com.zhaotai.uzao.ui.design.material.activity.MaterialDesignListActivity;
import com.zhaotai.uzao.ui.design.presenter.EditorPresenter;
import com.zhaotai.uzao.ui.design.widget.GuildView;
import com.zhaotai.uzao.utils.FileUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.MediaStoreUtil;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.StatusBarUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.BuyOrPutAwayDialog;
import com.zhaotai.uzao.widget.CustomChooseSpuDialog;
import com.zhaotai.uzao.widget.CustomSureDialog;
import com.zhaotai.uzao.widget.RenewalPutAwayDialog;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;
import com.zhaotai.uzao.widget.dialog.UITipDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * description:2d编辑器页面和3d编辑器的UV面设计页面
 * 编辑器的不同面的实现在EditorFragment
 * <p>
 * 编辑器原型为贴纸控件 https://github.com/wuapnjie/StickerView  根据需要做了一些变形处理
 * 技术功能主要实现了 添加图片（用户图片、素材） 和一些图片辅助功能如： 白色背景透明化（将图片的白色部分变成透明）、滤镜（调用滤镜接口获得新图片）、裁剪、适配和填充（图片调整合适的大小）
 * 添加文字（用户手打文字、流行语） 和一些文字辅助功能：修改文字字体颜色、修改文字横竖排、增加文字字间距。
 * 和一些整体的辅助功能：网格、撤销、复制，将设计好的元素保存成数据，和将数据还原成元素，并继续设计。
 * <p>
 * 注意事项：本功能操作图片较多，需要注意内存管理，尤其对于bitmap的回收处理。
 * <p>
 * author : ZP
 * date: 2018/4/3 0028.
 */
public class EditorActivity extends BaseFragmentActivity implements EditorContract.View {
    private static final String TAG = EditorActivity.class.toString();
    //3d模型数据
    private static final String SIMPLE_3d_MODEL = "simple_3d_Model";
    //编辑类型
    private static final String MODEL_TYPE = "model_type";
    //MKU id
    private static final String MKU_ID = "mku_id";
    //SPUid
    private static final String SPU_ID = "spu_id";
    //是否是模板商品
    private static final String IS_TEMPLATE = "is_Template";
    //数据json
    private static final String LAYER_JSON = "layer_json";
    //商品详情
    private static final String GOODS_DETAIL = "goods_detail";
    //素材详情
    private static final String MATERIAL_DETAIL = "material_detail";
    //素材详情
    private static final String DESIGN_ID = "design_id";

    //过滤数据
    private static final String VALIDATE_PRODUCT = "validate_product";

    private ValidateProductBean validateProductBean;

    private final int REQUEST_CODE_CHOOSE = 77;
    private final int REQUEST_CODE_TAKE_PHOTO = 78;
    private final int TYPE_OPEN_CAMERA = 0;
    private final int TYPE_OPEN_MEDIA = 1;

    //标题
    @BindView(R.id.tool_title)
    public TextView mTitle;
    //抽屉的主视图
    @BindView(R.id.rl_content_root)
    public RelativeLayout rl_content_root;
    //图层根图层 所有的fragment放在这父布局里
    @BindView(R.id.fl_on_carrier)
    public FrameLayout fl_onCarrier;

    //页面后退按钮
    @BindView(R.id.iv_design_material_back)
    public ImageView iv_back;
    //下一步按钮 2d模式下进行保存、上架，3d模式下返回数据给3d编辑器
    @BindView(R.id.iv_design_material_next)
    public ImageView tvNext;
    //底部栏
    @BindView(R.id.ll_design_bar)
    public LinearLayout ll_bar;
    //底边栏 图片按钮
    @BindView(R.id.tv_design_bar_picture)
    public TextView tv_picture;
    //底边栏素材按钮
    @BindView(R.id.tv_design_bar_sticker)
    public TextView tv_sticker;
    //底边栏文字按钮
    @BindView(R.id.tv_design_bar_text)
    public TextView tv_text;
    //文字编辑布局
    @BindView(R.id.ll_sticker_text_editor)
    public LinearLayout llTextEditor;
    //切换面的按钮
    @BindView(R.id.ll_change_side)
    public LinearLayout ll_changeSide;

    //文字的总调整布局  包括 换行，增加、减少行间距
    @BindView(R.id.ll_sticker_text_adjust)
    public LinearLayout llTextAdjust;
    //更改文字方向
    @BindView(R.id.iv_text_orientation_change)
    public ImageView btn_orientation;
    //增加文字间距
    @BindView(R.id.iv_text_size_add)
    public ImageView addLine;
    //减少文字间距
    @BindView(R.id.iv_text_size_jian)
    public ImageView jianLine;

    //艺术字字体选择
    @BindView(R.id.rc_text_editor_art_font)
    public RecyclerView rc_artFont;

    //文字颜色选择完成
    @BindView(R.id.rc_text_editor_color)
    public RecyclerView rc_textColor;
    //文字编辑
    @BindView(R.id.ll_sticker_img_editor)
    public LinearLayout llImgEditor;
    //白色背景透明
    @BindView(R.id.tv_design_bar_alph)
    public TextView tv_design_bar_alph;

    //抽屉控件
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;

    //删除图层
    @BindView(R.id.tv_clear_layer)
    public TextView tv_clear_layer;
    //图层
    @BindView(R.id.rc_layer)
    public RecyclerView rc_layer;

    //保存设计
    @BindView(R.id.tv_save)
    public TextView tv_SaveData;
    //图层按钮
    @BindView(R.id.tv_design_bar_layer)
    public TextView tvBarLayer;

    private EditorPresenter mPresenter;

    //是否是模板商品
    private String isTemplate;
    //商品的spuId
    private String spuId;
    //是2d还是3d类型
    private String modelType;
    //商品id
    private String mkuId;
    //设计id
    private String designId;
    //素材实体类
    private MaterialDetailBean materialDetailBean;
    //商品实体类
    private GoodsDetailMallBean goodsDetailMallBean;
    //3d数据实体类
    private ThreeDimensionalBean.Sample3dObjectModel simple3DModel;
    //图层数据信息类
    private String layerDataJson;

    //文字颜色adapter
    private TextColorAdapter textColorAdapter;
    //载体信息类
    private MKUCarrierBean mkuCarrierBean;
    //载体id
    private String simpleId;
    //fragment管理类
    private FragmentManager fm;
    //当前使用的最上层的fragment
    private EditorFragment handingFragment;
    //字体adapter
    private TextArtFontAdapter textArtFontAdapter;
    //未购买的素材实体类
    private UnPayMaterialBean unBoughtMaterials;
    //上传图片底部弹窗
    private UIBottomSheet addPicBottomSheet;
    //从图片库中可选择的图片
    private final int BITMAP_CAN_SELECTED = 1;

    //所有的使用过的素材id列表
    private ArrayList<String> allUsedMaterials;
    //购买素材的弹窗
    private UIBottomSheet uiBuyMaterialPop;
    //加载的弹窗
    private UITipDialog mLoadingDialog;
    //切换编辑面的弹窗
    private UIBottomSheet changSideBottomSheet;

    private ArrayList<EditorFragment> mFragments = new ArrayList();


    /**
     * 2d模式
     * 携带有明确MKUid和素材进入编辑器
     *
     * @param mkuId              商品对应的蒙版组id
     * @param spuId              商品SPUid             spuId
     * @param isTemplate         是否模板               N非模板  Y 模板
     * @param materialDetailBean 素材详情实体类         素材id为必有字段 其他字段如果不全会请求
     */
    public static void launch2DWhitMaterial(Context context, String mkuId, String spuId, String isTemplate, MaterialDetailBean materialDetailBean) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(MKU_ID, mkuId);
        intent.putExtra(MODEL_TYPE, GlobalVariable.MODEL_2D);
        intent.putExtra(SPU_ID, spuId);
        intent.putExtra(IS_TEMPLATE, isTemplate.toUpperCase());
        materialDetailBean.sourceMaterialId = materialDetailBean.sequenceNBR;
        intent.putExtra(MATERIAL_DETAIL, materialDetailBean);
        context.startActivity(intent);
    }


    /**
     * 2d模式
     * 这种主要是商品有一个以上的spu信息，所以在编辑器内弹窗选择好规格后进后，获得选定mkuId。
     *
     * @param context            context
     * @param spuId              商品Id
     * @param isTemplate         是否是模板   Y  是模板商品  其他是非模板商品
     * @param detail             商品详情
     * @param materialDetailBean 素材详情  素材id为必有字段 其他所需字段如果不全会请求素材详情接口获取
     */
    public static void launch2DWhitMaterial(Context context, String spuId, String isTemplate, GoodsDetailMallBean detail, @NonNull MaterialDetailBean materialDetailBean) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(MODEL_TYPE, GlobalVariable.MODEL_2D);
        intent.putExtra(SPU_ID, spuId);
        intent.putExtra(IS_TEMPLATE, isTemplate.toUpperCase());
        intent.putExtra(GOODS_DETAIL, detail);
        materialDetailBean.sourceMaterialId = materialDetailBean.sequenceNBR;
        intent.putExtra(MATERIAL_DETAIL, materialDetailBean);
        context.startActivity(intent);
    }

    /**
     * 2d模式
     * 这种主要是商品有一个以上的spu信息，所以在编辑器内弹窗选择好规格后进后，获得选定mkuId。
     *
     * @param context    context
     * @param spuId      商品Id
     * @param isTemplate 是否是模板   Y  是模板商品  其他是非模板商品
     * @param detail     商品详情
     */
    public static void launch2D(Context context, String spuId, String isTemplate, GoodsDetailMallBean detail) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(MODEL_TYPE, GlobalVariable.MODEL_2D);
        intent.putExtra(SPU_ID, spuId);
        intent.putExtra(IS_TEMPLATE, isTemplate.toUpperCase());
        intent.putExtra(GOODS_DETAIL, detail);
        context.startActivity(intent);
    }


    /**
     * 新设计商品，从无到有，需要
     *
     * @param context    上下文
     * @param mkuId      MKUId
     * @param spuId      商品的id       商品id
     * @param isTemplate 是否是模板商品  Y  是模板商品  其他是非模板商品
     */
    public static void launch2D(Context context, @NonNull String mkuId, String spuId, String isTemplate) {
        launch2D(context, mkuId, spuId, isTemplate.toUpperCase(), "");
    }

    /**
     * 带有保存的数据继续进行编辑
     *
     * @param context    上下文
     * @param mkuId      载体ID
     * @param spuId      商品id
     * @param isTemplate 是否是模板   Y  是模板商品  其他是非模板商品
     * @param layerId    保存的数据id  保存的设计id
     */
    public static void launch2D(Context context, String mkuId, String spuId, String isTemplate, String layerId) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(MKU_ID, mkuId);
        intent.putExtra(MODEL_TYPE, GlobalVariable.MODEL_2D);
        intent.putExtra(SPU_ID, spuId);
        intent.putExtra(IS_TEMPLATE, isTemplate.toUpperCase());
        intent.putExtra(DESIGN_ID, layerId);

        context.startActivity(intent);
    }


    /**
     * 加载3d面需要一些3d的数据
     *
     * @param context             context
     * @param sample3dModelOption 3d模型数据实体类
     */
    public static void launch3D(Context context, ThreeDimensionalBean.Sample3dObjectModel sample3dModelOption, String jsonLayer) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(MODEL_TYPE, GlobalVariable.MODEL_3D);
        intent.putExtra(SIMPLE_3d_MODEL, sample3dModelOption);
        intent.putExtra(LAYER_JSON, jsonLayer);
        context.startActivity(intent);
    }

    /**
     * 加载3d面需要一些3d的数据
     *
     * @param context             context
     * @param sample3dModelOption 3d模型数据实体类
     */
    public static void launch3D(Context context, ThreeDimensionalBean.Sample3dObjectModel sample3dModelOption, String jsonLayer, ValidateProductBean validateProductBean) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(MODEL_TYPE, GlobalVariable.MODEL_3D);
        intent.putExtra(SIMPLE_3d_MODEL, sample3dModelOption);
        intent.putExtra(LAYER_JSON, jsonLayer);
        intent.putExtra(VALIDATE_PRODUCT, validateProductBean);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        setContentView(R.layout.fix_activity_design_materal);
        //设计
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fl_onCarrier.getLayoutParams();
        layoutParams.width = ScreenUtils.getScreenWidth(mContext);
        layoutParams.height = ScreenUtils.getScreenWidth(mContext);
        fl_onCarrier.setLayoutParams(layoutParams);

        //初始化抽屉页面布局
        initDrawerLayout();
        // 初始化图层
        initLayer();

        //初始化文字面板
        initTextEditor();
        //初始化p层
        mPresenter = new EditorPresenter(this, this);
        //如果异常状态页 设置点击重试
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    @Override
    public void handleStatusBar() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_black));
    }

    /**
     * 艺术字按钮点击事件
     */
    @OnClick(R.id.tv_art_font)
    public void goArtFont() {
        //艺术字页面入口
        ArtFontMainListActivity.launch(EditorActivity.this);
    }


    /**
     * 底边栏的按钮点击事件
     */
    @OnClick({R.id.tv_design_bar_picture, R.id.tv_design_bar_sticker, R.id.tv_design_bar_text, R.id.tv_design_bar_catch_word, R.id.tv_design_bar_layer})
    public void onBarItemClick(TextView tvItem) {
        // 如果图层初始化失败的话这个功能不能用
        if (handingFragment == null) {
            ToastUtil.showShort("初始化失败，该功能不可用");
            return;
        }
        if (tvItem.getId() == R.id.tv_design_bar_layer && handingFragment.stickerView != null && handingFragment.stickerView.getStickers().size() == 0 || tvItem.getId() == R.id.tv_design_bar_text && handingFragment.isStickerSizeFull()) {
            //图层等于空 点击图层 无效 || 或者点击文字 控件个数已经满了的情况已经满了的情况
            return;
        }
        //清除选中控件的点击事件
        handingFragment.cleanHandingSticker();
        //隐藏文字和图片编辑工具
        hideAllEditTool();
        //更新选中控件状态
        barItemSelected(tvItem);
        //根据点击的按钮对应
        switch (tvItem.getId()) {
            case R.id.tv_design_bar_picture:
                //图片按钮
                if (handingFragment != null && !handingFragment.isStickerSizeFull()) {
                    initAddPicDialog();
                }
                break;
            case R.id.tv_design_bar_layer:
                //图层按钮
                changeDrawerState();
                break;
            case R.id.tv_design_bar_text:
                //选择文字
                if (handingFragment != null && !handingFragment.isStickerSizeFull()) {
                    initAddTextSticker();
                    showTextEditTool();
                }

                break;
            case R.id.tv_design_bar_sticker:
                //编辑器素材页面
                if (handingFragment != null && !handingFragment.isStickerSizeFull()) {
                    MaterialDesignListActivity.launch(mContext);
                }
                break;

            case R.id.tv_design_bar_catch_word:
                //进入编辑器流行词页面
                if (handingFragment != null && !handingFragment.isStickerSizeFull()) {
                    CatchWordListActivity.launch(EditorActivity.this);
                }
                break;

        }
    }


    /**
     * 文字工具点击事件处理
     *
     * @param view 点击的文字工具控件
     */
    @OnClick({R.id.iv_text_orientation_change, R.id.iv_text_size_add, R.id.iv_text_size_jian})
    public void textEditorToolClick(View view) {
        if (handingFragment == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_text_orientation_change:
                // 更改文字方向
                handingFragment.changeTextOrientation();
                break;
            case R.id.iv_text_size_add:
                //增加文字间距 +1
                handingFragment.changeTextSpacingExtra(1f);
                break;
            case R.id.iv_text_size_jian:
                //减少文字间距+1
                handingFragment.changeTextSpacingExtra(-1f);
                break;
        }
    }


    /**
     * 复制按钮点击事件
     * 复制当前选中控件
     */
    @OnClick(R.id.iv_copy)
    public void copySticker() {
        if (handingFragment != null) {
            //通知fragment进行复制
            handingFragment.copy();
        }
    }

    /**
     * 上一步按钮点击状态，撤销功能
     */
    @OnClick(R.id.iv_revocation)
    public void backUp() {
        if (handingFragment != null) {
            handingFragment.backStep();
        }
    }

    /**
     * 网格按钮，切换网格显示和隐藏
     */
    @OnClick(R.id.iv_grids)
    public void showLine() {
        if (handingFragment != null) {
            handingFragment.showLine();
        }
    }

    /**
     * 保存设计功能
     * 保存流程完整如下
     * 1.此步骤需要登录状态下才能进行，根据登录状态：1.1如果未登录就跳转登录页面  1.2已经登录进入 继续下一步
     * 2.是否此状态是否以保存  2.1如果已保存 退出  2.2如果未保存过此状态 进入下一步
     * 3.判断是否是模板商品 3.1如果是模板商品  提示不能保存 退出  3.2 普通商品 进入下一步
     * 4.停止保存计时器
     * 5.清除控件状态 （网格、选中框）
     * 6.获得首页截图  截图出来空白图
     * 7.恢复页面状态（网格、选中框）
     * 8.判断此设计有没有保存过 ：
     * --8.1如果保存过过 即更新这个设计的数据（通知presenter刷新设计数据），
     * --8.2如果没有保存过 保存一个新的设计（通知presenter保存数据）
     */
    @OnClick(R.id.tv_save)
    public void saveData() {
        //1.判断是否登录 如果如果未登录就跳转登录页面登录
        if (!LoginHelper.getLoginStatus()) {
            LoginHelper.goLogin(this);
            ToastUtil.showShort("请登录后再保存");
            return;
        }
        //2.是否已经保存
        if (!haschanged) {
            Log.d(TAG, "saveData: 此设计已经保存！");
            return;
        }
        //3.判断是否是模板商品
        if ("Y".equals(isTemplate)) {
            ToastUtil.showShort("模板商品不能保存");
            return;
        }
        //显示弹窗
        showLoadingDialog();
        //停止定时器 计时器有自动保存功能防止重复保存
        timerStop();
        //获得第一页fragment
        final EditorFragment firstPage = getFirstPage();
        //异常判断
        if (firstPage == null) {
            disMisLoadingDialog();
            return;
        }
        //清除第一页fragment的贴纸状态和网格等
        firstPage.cleanThumbnail();
        //延迟10毫秒清除了所有页面状态，截图正确
        Observable.timer(10, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Bitmap>() {
                    @Override
                    public Bitmap apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        Bitmap thumbnail = firstPage.getThumbnail();
                        if (thumbnail == null) {
                            Observable.error(new Throwable("图片保存错误"));
                        }
                        //恢复页面状态（网格，贴纸状态）
                        firstPage.resetThumbnail();
                        return thumbnail;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new RxSubscriber<Bitmap>(mContext) {
                    @Override
                    public void _onNext(Bitmap bitmap) {
                        //获得存储元数据
                        String layerJson = getLayerJson();
                        Log.d(TAG, "数据保存LayerJson: " + layerJson);
                        if (StringUtil.isEmpty(designId)) {
                            //此设计为保存过 更新设计此id下的内容
                            mPresenter.saveArtWorkAll(bitmap, layerJson, simpleId, mkuId, spuId);
                        } else {
                            //此设计保存过 保存新的设计
                            mPresenter.chaneArtWork(designId, layerJson, bitmap);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        disMisLoadingDialog();
                    }
                });


    }

    /**
     * 获得首页的fragment
     *
     * @return 首页fragment
     */
    public EditorFragment getFirstPage() {
        if (mFragments.size() != 0) {
            return mFragments.get(0);
        } else {
            return null;
        }
    }


    /**
     * 通知当前fragment增加一个默认的文字控件
     */
    private void initAddTextSticker() {
        handingFragment.addDefaultTextSticker();
    }

    /**
     * 开关抽屉
     */
    private void changeDrawerState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
//            设置图层数据
            barItemSelected(tvBarLayer);
            hideAllEditTool();
            if (handingFragment != null) {
                ArrayList<LayerDataBean> layerDataBeans;
                try {
                    layerDataBeans = handingFragment.getLayerDataBeans();
                    layerAdapter.setNewData(layerDataBeans);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            drawerLayout.openDrawer(GravityCompat.END);
        }
    }


    /**
     * 弹出增加底图 弹窗
     */
    public void initAddPicDialog() {

        if (addPicBottomSheet == null) {
            addPicBottomSheet = new UIBottomSheet(mContext);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.item_vetor_editor_add_pic, null, false);
            //属性 图片
            bottomSheetView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPicBottomSheet.dismiss();
                }
            });
            bottomSheetView.findViewById(R.id.tv_add_local).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPicBottomSheet.dismiss();
                    openMedia(TYPE_OPEN_MEDIA);
                }
            });

            bottomSheetView.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPicBottomSheet.dismiss();
                    openMedia(TYPE_OPEN_CAMERA);
                }
            });
            bottomSheetView.findViewById(R.id.tv_my_artwork).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPicBottomSheet.dismiss();
                    //选择贴纸
                    if (LoginHelper.getLoginStatus()) {
                        HistoryPictureActivity.launch(EditorActivity.this);
                    } else {
                        LoginHelper.goLogin(EditorActivity.this);
                    }
                }
            });
            addPicBottomSheet.setContentView(bottomSheetView);
        }
        addPicBottomSheet.show();
    }


    /**
     * 打开  拍照 / 相册
     */
    public void openMedia(final int type) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            if (type == TYPE_OPEN_CAMERA) {
//                                打开照相机
                                MediaStoreUtil.openCamera(EditorActivity.this, REQUEST_CODE_TAKE_PHOTO);
                            } else {
//                                打开相册
                                Matisse.from(EditorActivity.this)
                                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                        .capture(false)
                                        .captureStrategy(new CaptureStrategy(true, "com.zhaotai.uzao.fileprovider"))
                                        .countable(true)
                                        .maxSelectable(BITMAP_CAN_SELECTED)
                                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                        .thumbnailScale(1f)
                                        .imageEngine(new GlideEngine())
                                        .forResult(REQUEST_CODE_CHOOSE);
                            }
                        } else {
                            ToastUtil.showShort("打开权限才能选择图片哦~");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @OnClick({R.id.tv_design_bar_alph, R.id.tv_design_bar_filter, R.id.tv_design_bar_Clip, R.id.tv_design_bar_fate, R.id.tv_design_bar_fill})
    public void picFunctionClick(View view) {
//        打开对应功能页面
        if (handingFragment != null) {
            Sticker handingSticker = handingFragment.getHandingSticker();
            if (handingSticker instanceof MyDrawableSticker) {
                MyDrawableSticker drawableSticker;
                StickerDataInfo info;
                switch (view.getId()) {

                    case R.id.tv_design_bar_alph:
                        //透明修改页面
                        drawableSticker = (MyDrawableSticker) handingSticker;
                        info = drawableSticker.getInfo();
                        if (StringUtil.isEmpty(info.materialId)) {
                            WhiteToAlphActivity.launch(this, info);
                        } else {
                            ToastUtil.showShort(getString(R.string.MATERIAL_CANT_CHANGE));
                        }
                        break;

                    case R.id.tv_design_bar_filter:
                        //滤镜页面
                        drawableSticker = (MyDrawableSticker) handingSticker;
                        info = drawableSticker.getInfo();

                        if (StringUtil.isEmpty(info.materialId)) {
                            MyFilterActivity.launch(this, info);
                        } else {
                            ToastUtil.showShort(getString(R.string.MATERIAL_CANT_CHANGE));
                        }

                        break;
                    case R.id.tv_design_bar_Clip:
                        //裁剪页面
                        info = ((MyDrawableSticker) handingSticker).getInfo();
                        if (StringUtil.isEmpty(info.materialId)) {
                            mPresenter.imageCrop(ApiConstants.UZAOCHINA_IMAGE_HOST + info.url);
                        } else {
                            ToastUtil.showShort(getString(R.string.MATERIAL_CANT_CHANGE));
                        }
                        break;

                    case R.id.tv_design_bar_fate:
                        //适配
                        handingFragment.changeStickerFate();
                        break;

                    case R.id.tv_design_bar_fill:
                        //填充
                        handingFragment.changeStickerFill();
                        break;
                }

            }
        }

    }


    @OnClick(R.id.tv_clear_layer)
    public void clearLayer() {
        if (handingFragment != null) {
            new CustomSureDialog(EditorActivity.this)
                    .setContent(getString(R.string.sure_del_all_layer))
                    .setNegativeButton(getResources().getString(R.string.cancel))
                    .setPositiveButton(getResources().getString(R.string.sure))
                    .setListener(new CustomSureDialog.OnCloseListener() {
                        @Override
                        public void onClick(Dialog dialog, boolean confirm) {
                            if (confirm) {
                                handingFragment.removeAllLayer();
                                List<LayerDataBean> datas = layerAdapter.getData();
                                ArrayList<LayerDataBean> newDatas = new ArrayList<>();
                                for (int i = 0; i < datas.size(); i++) {
                                    LayerDataBean layerDataBean = datas.get(i);
                                    if (layerDataBean.isLock()) {
                                        newDatas.add(layerDataBean);
                                    }
                                }
                                layerAdapter.setNewData(newDatas);
                            }
                            dialog.dismiss();
                        }
                    }).show();

        }
    }

    //    返回
    @OnClick(R.id.iv_design_material_back)
    public void back() {
        finish();
    }

    //    工艺页面
    @OnClick(R.id.iv_technology)
    public void showTechnology() {
        if (GlobalVariable.MODEL_2D.equals(modelType)) {
            if (mkuCarrierBean != null && handingFragment != null) {
                ArrayList<TechnologyBean> craftGroups = (ArrayList<TechnologyBean>) mkuCarrierBean.getMkus().getCraftGroups().get(handingFragment.getPos()).getCraftModels();
                String json = GsonUtil.getGson().toJson(craftGroups);
                TechnologyActivity.launch(this, json, handingFragment.getTechnology());
            }
        } else {
            //3d
            ArrayList<TechnologyBean> craftGroups = (ArrayList<TechnologyBean>) simple3DModel.craftModels;
            String json = GsonUtil.getGson().toJson(craftGroups);
            TechnologyActivity.launch(this, json, handingFragment.getTechnology());
        }

    }

    /**
     * 换面按钮换面
     */
    @OnClick(R.id.iv_change_side)
    public void toChangeSide() {
        if (mkuCarrierBean != null) {
            initChangSideDialog();
        }
    }


    /**
     * 换面控件
     */
    private void initChangSideDialog() {


        if (changSideBottomSheet == null) {
            changSideBottomSheet = new UIBottomSheet(mContext);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.item_selected_area, null);
            //属性 图片
            bottomSheetView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changSideBottomSheet.dismiss();
                }
            });
            RecyclerView mRcSelectView = (RecyclerView) bottomSheetView.findViewById(R.id.rc_select_area);
            mRcSelectView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            SelectedAreaAdapter selectedAreaAdapter = new SelectedAreaAdapter();
            selectedAreaAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    ToastUtil.showShort("第" + position + "条被点击了");
                    changSideBottomSheet.dismiss();
                    switchPage(position);
                }
            });
            selectedAreaAdapter.setNewData(mkuCarrierBean.getMkus().getMaskGroups());
            mRcSelectView.setAdapter(selectedAreaAdapter);
            changSideBottomSheet.setContentView(bottomSheetView);
        }
        changSideBottomSheet.show();
    }

    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔

    //是否被改变了 ，true 就是未保存的数据，false就是已经保存了
    private boolean haschanged;

    /**
     * 下一步按钮点击事件
     * 保存设计进入购买或者上架按钮，本功能需要登录状态才能进行下一步，
     * <p>
     * 顺序：
     * 1.获取所有fragment使用的素材，汇总 并判断有没有使用过素材：
     * --1.1如果有 那么调用筛选素材接口 进入步骤2，
     * --1.2如果没有 素材进入步骤3
     * 2.获得的筛选过未付费的素材
     * --2.1将未付费的素材到成员变量,判断当前如果是2d模式那么进入步骤3，
     * --2.2如果是3d那么构建3d编辑器所需的数据，发送eventBus并关闭本页面
     * 3.弹出上架or购买弹窗，
     * --3.1如果选择是“购买”  那么保存设计数据，构建订单页面所需数据进入购买页面，
     * --3.2如果选择的是上架需要先判断是不是模板商品，
     * ----3.2.1提示模板商品不能上架，
     * ----3.2.2 判断是不是3d
     * -----3.2.2.1 如果是3d 收集3d数据  交付给3d编辑器
     * -----3.2.2.2 如果是2d 判断有没有未支付的素材，
     * ------3.2.2.2.1如果有那么弹出 购买未付费的素材弹窗，
     * ------3.2.2.2.2如果没有或者支付成功进入上架页面
     */
    @OnClick(R.id.iv_design_material_next)
    public void toNext() {
        //防止连点的问题
        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        //保存登录状态
        if (!LoginHelper.getLoginStatus()) {
            ToastUtil.showShort("请登录后再保存");
            LoginHelper.goLogin(this);
            return;
        }
        //获得所有使用过的素材
        unBoughtMaterials = null;
        allUsedMaterials = getAllUsedMaterials();
        if (GlobalVariable.MODEL_3D.equals(modelType)) {
            asModelToShowDialogOrCollect3dData();
        } else {
            if (allUsedMaterials.size() > 0) {
                //有使用过素材
                mPresenter.getUnBoughtMaterial(allUsedMaterials);
            } else {
                //没有使用过素材 根据2d还是3d
                asModelToShowDialogOrCollect3dData();
            }
        }

    }

    /**
     * 获得所有素材数组
     *
     * @return 所有使用过的素材id数组
     */
    private ArrayList<String> getAllUsedMaterials() {
        ArrayList<String> materials = new ArrayList<>();
        for (int i = 0; i < mFragments.size(); i++) {
            EditorFragment fragment = mFragments.get(i);
            ArrayList<String> materialId = fragment.getMaterialId();
            materials.addAll(materialId);
        }
        Log.d(TAG, "getAllUsedMaterials: 获取所有使用的素材" + materials.toString());
        return materials;
    }

    /**
     * 文字编辑功能初始化
     */
    private void initTextEditor() {
        textColorAdapter = new TextColorAdapter();
        rc_textColor.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rc_textColor.setAdapter(textColorAdapter);
        textColorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (handingFragment != null) {
                    handingFragment.changTextColor(Color.parseColor(textColorAdapter.getData().get(position)));
                }

            }
        });

//        艺术字
        textArtFontAdapter = new TextArtFontAdapter();
        ((SimpleItemAnimator) rc_artFont.getItemAnimator()).setSupportsChangeAnimations(false);
        rc_artFont.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rc_artFont.setAdapter(textArtFontAdapter);
        textArtFontAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemClick:  我第" + position + "条字体被点击了");
                ArtFontTopicBean artFontBean = textArtFontAdapter.getItem(position);
                if (artFontBean != null) {
                    String name = artFontBean.getWordartName();
                    String file = artFontBean.getWordartFileName();
                    int version = artFontBean.getVersion();
                    if (!StringUtil.isEmpty(name) && !StringUtil.isEmpty(file)) {
                        mPresenter.changeTypeFace(name, file, "优造中国", version, artFontBean.getWordartId());
                    } else {
                        ToastUtil.showShort(getResources().getString(R.string.error_add_text));
                    }
                } else {
                    ToastUtil.showShort(getResources().getString(R.string.error_add_text));
                }


            }
        });
    }

    /**
     * 抽屉的adapter
     */
    private LayerAdapter layerAdapter;

    /**
     * 初始化图层抽屉
     * 这里将图层功能用DrawerLayout实现
     */
    private void initDrawerLayout() {
        DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.d(TAG, "onDrawerSlide: " + slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
//                当关闭抽屉的时候销毁 图层列表数据
                List<LayerDataBean> data = layerAdapter.getData();
                layerAdapter.setNewData(new ArrayList<LayerDataBean>());
                //回收其中图片
                for (int i = 0; i < data.size(); i++) {
                    Bitmap bitmapContent = data.get(i).getBitmapContent();
                    BitmapUtils.recycleBitmap(bitmapContent);
                }
                data.clear();
                //清除底边栏状态
                barItemSelected(null);
                Log.d(TAG, "onDrawerClosed: ");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d(TAG, "onDrawerStateChanged: state = " + newState);
            }
        };
        drawerLayout.addDrawerListener(drawerListener);
        //设置不可触摸拖出侧边栏
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * 初始化图层RecycleView数据
     */
    private void initLayer() {
        layerAdapter = new LayerAdapter();
        rc_layer.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rc_layer.setAdapter(layerAdapter);
        //图层图片闪动的处理
        ((SimpleItemAnimator) rc_layer.getItemAnimator()).setSupportsChangeAnimations(false);
        /*
         * 图层列表需要注意的是：
         * 看到的顺序是从上到下StickerView中StickerList的顺序和绘制的顺序是从下到上,而图层中的顺序是从最上层到最下层，
         * <p>
         *     比如图A在图B的上面，
         *     在stickerView的StickerList 的例子里面是{StickerB，StickerA}按顺序先绘制B在绘制上层的A
         *     而在图层列表就要反过来 A在最上层B在下层
         * </p>
         * 在其他的功能找到对应的只是通知Fragment做对应的事情
         *
         */
        layerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.iv_hide_layer:
                        //隐藏当前图层
                        if (handingFragment != null) {
                            handingFragment.hideLayer(layerAdapter.getData().size() - 1 - position);
                            LayerDataBean layerDataBean = layerAdapter.getData().get(position);
                            layerDataBean.setVisable(!layerDataBean.isVisable());
                            layerAdapter.notifyItemChanged(position);
                        }
                        break;
                    case R.id.iv_del_layer:
                        //删除当前图层
                        if (handingFragment != null) {
                            new CustomSureDialog(EditorActivity.this)
                                    .setContent("确定删除该图层？")
                                    .setNegativeButton("取消")
                                    .setPositiveButton("确定")
                                    .setListener(new CustomSureDialog.OnCloseListener() {
                                        @Override
                                        public void onClick(Dialog dialog, boolean confirm) {
                                            if (confirm) {
                                                delLayer(position);
                                            }
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                        break;
                }
            }
        });
        layerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //删除设置对应控件选中
                layerAdapter.setselected(position);
                List<Sticker> stickers = handingFragment.getStickers();
                int pos = layerAdapter.getData().size() - 1 - position;
                Sticker sticker = stickers.get(pos);
                String locked = sticker.getLocked();
                if (!"Y".equals(locked)) {
                    handingFragment.setHandingSticker(pos);
                }
            }
        });
        doDragAndSwipeDelete();
    }

    /**
     * 图层列表拖拽和删除
     * 需要注意的也是图层顺序
     */
    private void doDragAndSwipeDelete() {
        //拖拽监听
        OnItemDragListener onItemDragListener = new OnItemDragListener() {

            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
                //拖拽过程中 改变位置
                int size = layerAdapter.getData().size();
                if (handingFragment != null) {
                    handingFragment.moveSticker(size - 1 - from, size - 1 - to);
                }

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "onItemDragEnd: pos->" + pos);
            }
        };


        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(layerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(rc_layer);
        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);//设置双向滑动功能

        //开启长按拖拽功能
        layerAdapter.enableDragItem(itemTouchHelper);
        layerAdapter.setOnItemDragListener(onItemDragListener);
        //关闭侧滑删除按钮
        layerAdapter.disableSwipeItem();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收拍照的图片
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            //获取本地图片地址
            String url = MediaStoreUtil.getCurrentPhotoPath();
            //上传图片到我的图片库
            showLoadingDialog();
            //特别小的图片
            int[] size = BitmapUtils.calculateBitmap(url);
            if (size[1] > GlobalVariable.MINSIZE && size[0] > GlobalVariable.MINSIZE) {
                mPresenter.upLoadBitmap(url);
            } else {
                ToastUtil.showShort("您上传的图片质量较低，建议使用高清图片");
                disMisLoadingDialog();
            }

        } else if (resultCode == RESULT_OK && REQUEST_CODE_CHOOSE == requestCode) {
            //接受相册获取的图片
            String url = Matisse.obtainPathResult(data).get(0);
            //上传图片到我的图片库
            showLoadingDialog();
            int[] size = BitmapUtils.calculateBitmap(url);
            if (size[1] > GlobalVariable.MINSIZE && size[0] > GlobalVariable.MINSIZE) {
                mPresenter.upLoadBitmap(url);
            } else {
                ToastUtil.showShort("您上传的图片质量较低，建议使用高清图片");
                disMisLoadingDialog();
            }
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //接受过裁剪的图片
            boolean shouldClip = data.getBooleanExtra(UCrop.EXTRA_OUTPUT_SHOULD_CROP, false);

            int left = data.getIntExtra(UCrop.EXTRA_OUTPUT_LEFT, 0);
            int top = data.getIntExtra(UCrop.EXTRA_OUTPUT_TOP, 0);
            int width = data.getIntExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, 0);
            int height = data.getIntExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, 0);
            if (shouldClip) {
//                被裁剪过 去裁剪当前选中图片
                changeClipStickerDrawable(new SourceRectBean(left, top, width, height));
            } else {
//                未裁剪
                changeClipStickerDrawable(null);
            }
        } else if (requestCode == Pingpp.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
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
                putAway();
            } else {
                //支付失败  拉起重新支付弹窗
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


    @Override
    public void showProgress() {
        showLoadingDialog();
    }

    @Override
    public void stopProgress() {
        disMisLoadingDialog();
    }

    /**
     * 过滤获得的未付费的素材列表
     *
     * @param materialListBean 未付费的素材
     */
    @Override
    public void showUnBoughtMaterial(UnPayMaterialBean materialListBean) {
        this.unBoughtMaterials = materialListBean;
        asModelToShowDialogOrCollect3dData();
    }

    /**
     * for3d 上传缩略图图片成功
     *
     * @param url uV面截图保存
     */
    @Override
    public void showSave3DSuccess(String url) {
        disMisLoadingDialog();
        if (!StringUtil.isEmpty(url)) {
            LayerMetaJsonBean fragmentData = getFragmentData();
            fragmentData.setThumbnail(url);
            Log.d(TAG, "showSave3DSuccess: " + fragmentData.toString());
            EventBus.getDefault().post(new EventBean<>(fragmentData, EventBusEvent.DESIGN_3D_UV_FINISH));
            finish();
        }
    }

    @Override
    public void showFinishSaveFinish() {
        disMisLoadingDialog();
    }

    /**
     * 保存修改我的设计
     *
     * @param bean 修改成功返回类
     */
    @Override
    public void saveArtWorkSuccess(ResultMetaDataBean bean) {
//        关闭弹窗
        disMisLoadingDialog();
//        开启定时器
        timerSave();
        if (bean != null) {
            ToastUtil.showShort("保存成功");
            haschanged = false;
            designId = bean.getSequenceNBR();
        } else {
            ToastUtil.showShort("保存失败");
        }
    }

    /**
     * 模板数据 来实例化图层
     *
     * @param layerJSon 模板数据
     */
    @Override
    public void showTemplateInfo(String layerJSon) {
        this.layerDataJson = layerJSon;
        List<LayerMetaJsonBean> layerMetaJsonBeen = jsonToLayerDataBean(layerJSon);
        init2DFragment(layerMetaJsonBeen);
    }

    /**
     * 显示过期弹窗
     *
     * @param resultMetaDataBean 过期数据
     */
    @Override
    public void showOverdue(ValidateProductBean resultMetaDataBean) {
        StringBuilder wordTip = new StringBuilder();
//        1过期素材 ： 您使用的素材%@已过期，请及时续费素材
        if (resultMetaDataBean.material.expiredPublished != null && resultMetaDataBean.material.expiredPublished.size() != 0) {
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
        }
    }

    /**
     * json 转为实体类
     *
     * @param layerDataJson 图层数据json
     * @return 图层数据实体类
     */
    private List<LayerMetaJsonBean> jsonToLayerDataBean(String layerDataJson) {
        List<LayerMetaJsonBean> jsonBean = null;

        if (!StringUtil.isEmpty(layerDataJson)) {
//            没有已经保存的设计
            Type mType = new TypeToken<List<LayerMetaJsonBean>>() {
            }.getType();
            try {
                jsonBean = GsonUtil.getGson().fromJson(layerDataJson, mType);
            } catch (Exception e) {
                ToastUtil.showShort("存储数据恢复异常");
                jsonBean = null;
            }
        }
        return jsonBean;
    }


    private Disposable timer;

    /**
     * 停止计时器
     */
    public void timerStop() {
        if (timer != null) {
            timer.dispose();
        }
    }

    /**
     * 开启定时保存
     */
    public synchronized void timerSave() {
        if (GlobalVariable.MODEL_2D.equals(modelType)) {
            if (timer != null) {
                timer.dispose();
            }
            if (LoginHelper.getLoginStatus()) {
//                自动登录状态下保存 1分钟保存一次
                Observable.interval(3, 1, TimeUnit.MINUTES)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new RxSubscriber<Long>(this) {
                            @Override
                            public void onSubscribe(Disposable d) {
                                super.onSubscribe(d);
                                timer = d;
                            }

                            @Override
                            public void _onNext(Long aLong) {
                                saveData();
                            }

                            @Override
                            public void _onError(String message) {

                            }
                        });
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerSave();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerStop();
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new UITipDialog.Builder(this)
                .setIconType(UITipDialog.Builder.ICON_TYPE_DESIGNER_LOADING)
                .setTipWord(getString(R.string.loading))
                .create();
        if (!isFinishing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void disMisLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }


    /**
     * 获得fragment的素材集合
     *
     * @return 元数据的类的集合
     */

    public LayerMetaJsonBean getFragmentData() {

        LayerMetaJsonBean jsonBean = null;
        if (!StringUtil.isEmpty(layerDataJson)) {
//            没有保存的作品
            try {
                jsonBean = GsonUtil.getGson().fromJson(layerDataJson, LayerMetaJsonBean.class);
            } catch (Exception e) {
                ToastUtil.showShort("存储数据恢复异常");
                jsonBean = null;
            }
        }
        ArrayList<String> allUsedMaterials = getAllUsedMaterials();
        if (jsonBean != null) {
            ArrayList<LayerMetaJsonBean.LayerMetaBean> layerMetaBeanArrayList = handingFragment.saveData();
            jsonBean.setLayerMeta(layerMetaBeanArrayList);
            jsonBean.setMaterialIds(allUsedMaterials);
            jsonBean.setCraftId(simple3DModel.craftModels.get(handingFragment.getTechnology()).getSequenceNBR());
        } else {
            jsonBean = new LayerMetaJsonBean();
            jsonBean.setAspectId("正面");
            jsonBean.setMaterialIds(allUsedMaterials);
            ArrayList<LayerMetaJsonBean.LayerMetaBean> layerMetaBeanArrayList = handingFragment.saveData();
            jsonBean.setLayerMeta(layerMetaBeanArrayList);
        }


        return jsonBean;
    }

    /**
     * 根据模式去显示弹窗或者收集3d编辑器的数据
     */
    private void asModelToShowDialogOrCollect3dData() {
        if (GlobalVariable.MODEL_2D.equals(modelType)) {
            //2d 模式
            if ("Y".equals(isTemplate)) {
                //模板商品只能购买
                bugOrPutAway(0);
                return;
            }
            //如果不是模板商品可以 根据弹窗选择购买或者上架
            BuyOrPutAwayDialog dialog = new BuyOrPutAwayDialog(mContext, R.style.CommonDialog, getString(R.string.design_finish_notice), new BuyOrPutAwayDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    dialog.dismiss();
                    //上架或者购买 0 购买  1上架
                    bugOrPutAway(confirm ? 1 : 0);
                }
            });
            dialog.setTitle(getString(R.string.warm_prompt)).show();
            Window dialogWindow = dialog.getWindow();
            if (dialogWindow != null) {
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                lp.width = (int) (ScreenUtils.getScreenWidth(mContext) - PixelUtil.dp2px(37 * 2));
                dialogWindow.setAttributes(lp);
            }
        } else if (GlobalVariable.MODEL_3D.equals(modelType)) {
            //3d模式
            //清除状态
            handingFragment.cleanThumbnail();
            //延迟20毫秒保证状态清除 截图保存状态
            Observable.timer(20, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RxSubscriber<Long>(this) {
                        @Override
                        public void _onNext(Long aLong) {
                            Bitmap thumbnail = null;
                            try {
                                thumbnail = handingFragment.getThumbnail();
                            } catch (Exception e) {
                                ToastUtil.showShort("UV面保存失败");
                            }
                            //恢复状态 编辑状态 如控件选中 显示网格等
                            handingFragment.resetThumbnail();
                            if (thumbnail != null) {
                                //通知fragment上传截图并保存数据
                                mPresenter.finishToSaveFor3d(thumbnail);
                            } else {
                                ToastUtil.showShort("UV面保存失败");
                            }
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("UV面保存失败");
                        }
                    });
        }
    }


    /**
     * 根据弹窗选择上架或者购买
     *
     * @param doWhat 上架或者购买 0 购买  1上架
     */
    private void bugOrPutAway(final int doWhat) {
        //模板商品不能上架
        if (doWhat == 1 && "Y".equals(isTemplate)) {
            ToastUtil.showShort("模板商品不能上架");
            return;
        }
        //上架商品需要先购买未付费的素材
        if (doWhat == 1 && unBoughtMaterials != null && unBoughtMaterials.getSourceMaterialModels() != null && unBoughtMaterials.getSourceMaterialModels().size() > 0) {
            initBuyMaterialPop();
            return;
        }
        //收集数据
        //显示弹窗
        showLoadingDialog();
        final ArrayList<TemplateImageInfo> templateImageInfos = getTemplateImageInfo();
        //获得界面元数据
        final String layerJson = getLayerJson();

        //这里让所有fragment都显示 并清除他们的状态，因为发现fragment隐藏状态不能更改页面状态。
        final FragmentTransaction ft = fm.beginTransaction();
        for (EditorFragment fragment : mFragments) {
            ft.show(fragment);
            fragment.cleanThumbnail();
        }
        //同步提交时间
        fm.executePendingTransactions();
        ft.commit();

        Observable.timer(20, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, List<Bitmap>>() {
                    @Override
                    public ArrayList<Bitmap> apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        //在主线程获得截图
                        ArrayList<Bitmap> bitmaps = getAllBitmaps();
                        for (EditorFragment fragment : mFragments) {
                            fragment.resetThumbnail();
                        }
                        //显示首页
                        switchPage(0);
                        return bitmaps;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new RxSubscriber<List<Bitmap>>(mContext) {
                    @Override
                    public void _onNext(List<Bitmap> bitmaps) {
                        ArrayList<Bitmap> mBitmaps = (ArrayList<Bitmap>) bitmaps;
                        //是新设计
                        if (StringUtil.isEmpty(designId)) {
                            simpleId = mkuCarrierBean.getSampleSpuModel().getSequenceNBR();
                        }
                        if ("Y".equals(isTemplate)) {
                            //是模板商品去购买这个商品
                            mPresenter.finishToBuyDesign(mkuId, spuId, simpleId, mBitmaps, designId, layerJson, templateImageInfos, getIntent().getStringExtra("isTemplate"), allUsedMaterials, unBoughtMaterials, doWhat);
                        } else {
                            if (haschanged) {
                                //这个字段是设计如果保存过 而且未再动过，就可以不在保存了，已经保存过了 无需再保存一次
                                mPresenter.finishToSaveDesign(mkuId, spuId, simpleId, mBitmaps, designId, layerJson, templateImageInfos, isTemplate, allUsedMaterials, unBoughtMaterials, doWhat);
                            } else {
                                //不保存直接 上架或者购买
                                mPresenter.finishToNextWithoutSave(mkuId, spuId, mBitmaps, designId, templateImageInfos, isTemplate, allUsedMaterials, unBoughtMaterials, doWhat);
                            }
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        Log.d(TAG, "chooseToDoNext_onError: " + message);
                        ToastUtil.showShort("保存错误");
                        disMisLoadingDialog();
                    }
                });


    }

    /**
     * 初始化并显示购买素材弹窗
     */
    private void initBuyMaterialPop() {
        uiBuyMaterialPop = new UIBottomSheet(mContext);
        View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.pop_item_buy_material, null);

        //关闭
        ImageView ivClose = (ImageView) bottomSheetView.findViewById(R.id.iv_buy_material_close);
        final ImageView ivWx = (ImageView) bottomSheetView.findViewById(R.id.pay_way_wx_image);
        final ImageView ivzfb = (ImageView) bottomSheetView.findViewById(R.id.pay_way_zfb_image);
//       支付宝
        final RelativeLayout rlzfb = (RelativeLayout) bottomSheetView.findViewById(R.id.pay_way_zfb);
        RelativeLayout rlwx = (RelativeLayout) bottomSheetView.findViewById(R.id.pay_way_wx);

        TextView tv_to_pay = (TextView) bottomSheetView.findViewById(R.id.tv_to_pay);
        TextView tv_price = (TextView) bottomSheetView.findViewById(R.id.tv_price);
        tv_price.setText(getResources().getString(R.string.account, unBoughtMaterials.getTotalAmountY()));

        // 购买素材弹窗的点击事件
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
                            buyMaterialByWay(0);
                        } else {
                            buyMaterialByWay(1);
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
        //显示在底部
        uiBuyMaterialPop.setContentView(bottomSheetView);
        uiBuyMaterialPop.show();

    }

    /**
     * 支付成功后上架
     */
    public void putAway() {
        //显示loading弹窗
        showLoadingDialog();
        //收集数据
        final ArrayList<TemplateImageInfo> templateImageInfos = getTemplateImageInfo();
        //元数据
        final String layerJson = getLayerJson();


        FragmentTransaction ft = fm.beginTransaction();
        //将fragment显示 并清除Fragment上的选中状态网格等
        for (EditorFragment fragment : mFragments) {
            ft.show(fragment);
            fragment.cleanThumbnail();
        }
        fm.executePendingTransactions();
        ft.commit();


        Observable.timer(20, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, List<Bitmap>>() {
                    @Override
                    public ArrayList<Bitmap> apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        ArrayList<Bitmap> bitmaps = getAllBitmaps();
                        for (EditorFragment fragment : mFragments) {
                            fragment.resetThumbnail();
                        }
                        switchPage(0);
                        return bitmaps;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new RxSubscriber<List<Bitmap>>(mContext) {
                    @Override
                    public void _onNext(List<Bitmap> bitmaps) {
                        ArrayList<Bitmap> mBitmaps = (ArrayList<Bitmap>) bitmaps;

                        if (StringUtil.isEmpty(designId)) {
                            simpleId = mkuCarrierBean.getSampleSpuModel().getSequenceNBR();
                        }
                        if (haschanged) {
                            //已经保存过了 无需再保存一次
                            mPresenter.finishToSaveDesign(mkuId, spuId, simpleId, mBitmaps, designId, layerJson, templateImageInfos, isTemplate, allUsedMaterials, unBoughtMaterials, 1);
                        } else {
                            mPresenter.finishToNextWithoutSave(mkuId, spuId, mBitmaps, designId, templateImageInfos, isTemplate, allUsedMaterials, unBoughtMaterials, 1);
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        Log.d(TAG, "chooseToDoNext_onError: " + message);
                        disMisLoadingDialog();
                    }
                });
    }

    /**
     * 获取所有元数据的json串
     */
    private String getLayerJson() {
        ArrayList<LayerMetaJsonBean> layerJsonBeanList = new ArrayList<>();
        for (int i = 0; i < mFragments.size(); i++) {
            EditorFragment myFragment = mFragments.get(i);
            LayerMetaJsonBean layerMetaJsonBean = new LayerMetaJsonBean();
            //构建json数据
            List<MKUCarrierBean.MkusBean.MaskGroupsBean> maskGroups = mkuCarrierBean.getMkus().getMaskGroups();
            if (maskGroups != null && maskGroups.size() >= i) {
                //填写面名称
                layerMetaJsonBean.setAspectId(maskGroups.get(i).getAspectId());
                MKUCarrierBean.MkusBean.CraftGroupsBean craftGroupsBean = mkuCarrierBean.getMkus().getCraftGroups().get(i);
                //设置工艺
                List<TechnologyBean> craftModels = craftGroupsBean.getCraftModels();
                int technology = myFragment.getTechnology();
                if (craftModels != null && craftModels.size() >= technology && technology != -1 && craftModels.get(technology) != null) {
                    layerMetaJsonBean.setCraftId(craftGroupsBean.getCraftModels().get(technology).getSequenceNBR());
                }
                //设置面元数据
                ArrayList<LayerMetaJsonBean.LayerMetaBean> layerMetaBeanArrayList = myFragment.saveData();
                layerMetaJsonBean.setLayerMeta(layerMetaBeanArrayList);
                // 拼装所有面
                layerJsonBeanList.add(layerMetaJsonBean);
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(layerJsonBeanList);
        System.out.println("保存数据：" + json);
        return json;
    }

    /**
     * 每个fragment获取截图
     *
     * @return bitmap列表
     */
    @NonNull
    private ArrayList<Bitmap> getAllBitmaps() {
        ArrayList<Bitmap> thumbnails = new ArrayList<>();

        for (int i = 0; i < mFragments.size(); i++) {
            EditorFragment fragment = mFragments.get(i);
            Bitmap thumbnail = null;
            try {
                //获得单面截图
                thumbnail = fragment.getThumbnail();
            } catch (Exception e) {
                ToastUtil.showShort("获得图片出错");
            }
            fragment.resetThumbnail();
            thumbnails.add(thumbnail);

        }
        return thumbnails;
    }


    /**
     * 创建带有临时图片信息类的列表
     *
     * @return 图片信息类的列表 上架购买页面用
     */
    @NonNull
    private ArrayList<TemplateImageInfo> getTemplateImageInfo() {
        List<MKUCarrierBean.MkusBean.MaskGroupsBean> maskGroups = mkuCarrierBean.getMkus().getMaskGroups();
        ArrayList<TemplateImageInfo> templateImageInfos = new ArrayList<>();
        for (MKUCarrierBean.MkusBean.MaskGroupsBean bean : maskGroups) {
            String aspectName = bean.getAspectName();
            TemplateImageInfo templateImageInfo = new TemplateImageInfo(aspectName);
            templateImageInfos.add(templateImageInfo);
        }
        return templateImageInfos;
    }


    /**
     * 将当前sticker替换裁剪图片地址
     *
     * @param sourceRectBean 裁剪信息类
     */
    private void changeClipStickerDrawable(SourceRectBean sourceRectBean) {
        if (handingFragment != null) {
            handingFragment.changClipStickerDrawable(sourceRectBean);
        }
    }


    /**
     * 删除指定位置图层
     *
     * @param position adapter的位置
     */
    private void delLayer(int position) {
        handingFragment.removeLayer(layerAdapter.getData().size() - 1 - position);
        layerAdapter.remove(position);
    }


    @Override
    protected void initData() {
        initIntentData();

        showLoading();
        mPresenter.getColorList();
        mPresenter.getFontList();
        // 获取默认字体
        mPresenter.getDefaultFont();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        //素材详情信息
        materialDetailBean = (MaterialDetailBean) intent.getSerializableExtra(MATERIAL_DETAIL);
        //是否是模板商品
        isTemplate = intent.getStringExtra(IS_TEMPLATE);
        //商品是2d还是3d
        modelType = intent.getStringExtra(MODEL_TYPE);
        //模板spuId
        spuId = intent.getStringExtra(SPU_ID);
        //模板MKU信息
        mkuId = intent.getStringExtra(MKU_ID);
        //设计信息
        designId = intent.getStringExtra(DESIGN_ID);

        goodsDetailMallBean = (GoodsDetailMallBean) intent.getSerializableExtra(GOODS_DETAIL);

        validateProductBean = (ValidateProductBean) intent.getSerializableExtra(VALIDATE_PRODUCT);
//        spuId = intent.getStringExtra(SPU_ID);
        mkuId = intent.getStringExtra(MKU_ID);

        simple3DModel = (ThreeDimensionalBean.Sample3dObjectModel) intent.getSerializableExtra(SIMPLE_3d_MODEL);
        layerDataJson = intent.getStringExtra(LAYER_JSON);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public void showColoList(List<String> colorList) {
        textColorAdapter.setNewData(colorList);
    }


    /**
     * 顺序：
     * modelType
     * |-----modelType = 2d
     * |         Mkuid 是否为空
     * |            |-- mkuId = null
     * |            |      选择MKU弹窗  获得MKUid  进入到MKU_ID不为空的
     * |            |-- mkuid != null
     * |            |     |---没有设计信息  DesignId 是为空
     * |            |     |               初始化空白fragment
     * |            |---isTemplate   是否有模板
     * |                  |     初始化有内容的fragment
     * |                  |---根据设计id获取设计信息  DesignId 不为空
     * |                  |     初始化有内容的fragment
     * |
     * |-----modelType = 3d
     * |         |---没有设计信息  DesignId 是为空
     * |         |      初始化空白fragment
     * |         |---根据设计id获取设计信息  DesignId 不为空
     * |         |     初始化有内容的fragment
     * |
     */
    @Override
    public void initCarrieData() {
        if (GlobalVariable.MODEL_2D.equals(modelType)) {
            //是2d
            if (StringUtil.isEmpty(mkuId)) {
                //如果让我自己选择mku信息
                if (goodsDetailMallBean != null) {
                    //弹出SPU选择框
                    showSpuChooseDialog();
                }
            } else {
                //已有根据mkuId信息
                mPresenter.getCarrierInfo(mkuId);
            }

        } else if (GlobalVariable.MODEL_3D.equals(modelType)) {
            //3d
            //隐藏切面按钮
            ll_changeSide.setVisibility(View.GONE);
            tv_SaveData.setVisibility(View.GONE);
            init3dFragment();
        }
    }

    /**
     * 初始化3d
     */
    private void init3dFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LayerMetaJsonBean jsonBean = null;
        if (!StringUtil.isEmpty(layerDataJson)) {
//            没有保存的作品
            try {
                jsonBean = GsonUtil.getGson().fromJson(layerDataJson, LayerMetaJsonBean.class);
                if (validateProductBean != null && jsonBean != null) {
                    //过滤3d数据
                    jsonBean = mPresenter.filterData(validateProductBean, jsonBean);
                }
            } catch (Exception e) {
                ToastUtil.showShort("存储数据恢复异常");
                jsonBean = null;
            }
        }
        int craftIdPos;
        ArrayList<TechnologyBean> craftGroups = (ArrayList<TechnologyBean>) simple3DModel.craftModels;
        if (jsonBean == null || jsonBean.getCraftId() == null) {
            //没有保存过
            craftIdPos = mPresenter.getCraftIdPos(simple3DModel.craftId, craftGroups);
        } else {
//            保存过
            craftIdPos = mPresenter.getCraftIdPos(jsonBean.getCraftId(), craftGroups);
        }
        if (craftGroups.size() > 0 && craftGroups.size() >= craftIdPos) {
            setTechnologyName(craftGroups.get(craftIdPos).getCraftName());
        }

        Fragment fragment = EditorFragment.newInstance("3d", simple3DModel.sample3dOptionModel, jsonBean, craftIdPos);
        ft.add(R.id.fl_on_carrier, fragment);
        handingFragment = (EditorFragment) fragment;
        mFragments.add(handingFragment);
        ft.show(fragment);
        ft.commit();
        fm.executePendingTransactions();

    }


    /**
     * 获取载体信息 处理mku信息
     *
     * @param bean MKU信息
     */
    @Override
    public void showCarrierData(MKUCarrierBean bean) {
        if (bean == null) {
            //载体信息查询失败
            ToastUtil.showShort("数据获得失败");
            showNetworkFail("数据获得失败");
            return;
        }
        this.mkuCarrierBean = bean;
        simpleId = bean.getSampleSpuModel().getSpuModel().getSequenceNBR();
        if (!StringUtil.isEmpty(this.designId)) {
//            根据设计id获取设计信息
            mPresenter.getDesignMetaData(this.designId);
        } else if ("Y".equals(isTemplate)) {
            //获取模板商品设计信息
            mPresenter.getTemplateInfo(spuId);
        } else {
            init2DFragment(null);
        }
    }

    /**
     * 根据查询的设计数据恢复2dFragment
     *
     * @param bean 设计数据
     */
    @Override
    public void showDesignMetaData(List<LayerMetaJsonBean> bean) {
        init2DFragment(bean);
    }

    /**
     * 付费购买素材方式
     *
     * @param buyWay 1 支付宝 0 微信
     */
    private void buyMaterialByWay(int buyWay) {
        if (buyWay == 0) {
//            微信购买素材
            mPresenter.getMaterialPayInfo("wx", unBoughtMaterials.getSourceMaterialModels());
        } else {
//            支付宝购买素材
            mPresenter.getMaterialPayInfo("alipay", unBoughtMaterials.getSourceMaterialModels());
        }
    }

    /**
     * 根据数据初始化2d的Fragment
     */
    private void init2DFragment(List<LayerMetaJsonBean> jsonBean) {
        String defaultTechnologyName = "";
        if (fm == null) {
            fm = getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        MKUCarrierBean.MkusBean mkus = mkuCarrierBean.getMkus();
        List<MKUCarrierBean.MkusBean.MaskGroupsBean> maskGroups = mkus.getMaskGroups();
        List<MKUCarrierBean.MkusBean.CraftGroupsBean> craftGroups = mkus.getCraftGroups();
        for (int i = 0; i < maskGroups.size(); i++) {
            Fragment fragment;
            int craftIdPos = -1;
            if (jsonBean != null) {
                // 初始化有信息的
                MKUCarrierBean.MkusBean.CraftGroupsBean craftGroupsBean = craftGroups.get(i);
                LayerMetaJsonBean layerMetaJsonBean = jsonBean.get(i);
                String craftId = layerMetaJsonBean.getCraftId();
                List<TechnologyBean> craftModels = craftGroupsBean.getCraftModels();
                if (craftModels != null && craftModels.size() > 0) {
                    craftIdPos = mPresenter.getCraftIdPos(craftId, craftGroupsBean.getCraftModels());
                    if (i == 0) {
                        if (craftGroups.size() > 0) {
                            defaultTechnologyName = craftGroups.get(0).getCraftModels().get(craftIdPos).getCraftName();
                        }
                    }
                }
                //创建一个有设计信息的fragment
                fragment = EditorFragment.newInstance("2d", maskGroups.get(i), jsonBean.get(i), i, craftIdPos);

            } else {
                //初始化新的
                MKUCarrierBean.MkusBean.CraftGroupsBean craftGroupsBean = craftGroups.get(i);
                String craftId = craftGroupsBean.getCraftId();
                List<TechnologyBean> craftModels = craftGroupsBean.getCraftModels();
                if (craftModels != null && craftModels.size() > 0) {
                    craftIdPos = mPresenter.getCraftIdPos(craftId, craftModels);
                    if (i == 0) {
                        if (craftGroups.size() > 0) {
                            defaultTechnologyName = craftGroups.get(0).getCraftModels().get(craftIdPos).getCraftName();
                        }
                    }
                }
                //创建一个有没有设计信息的fragment
                fragment = EditorFragment.newInstance("2d", maskGroups.get(i), null, i, craftIdPos);
            }
            ft.add(R.id.fl_on_carrier, fragment);
            mFragments.add((EditorFragment) fragment);
            if (i == 0) {
                handingFragment = (EditorFragment) fragment;
            }
            //fragment不是show状态下的话不能更新一些页面数据
            ft.show(fragment);
        }
        ft.commit();
        fm.executePendingTransactions();
        //切换到首页
        switchPage(0);
        setTechnologyName(defaultTechnologyName);

    }

    /**
     * 切换页面
     *
     * @param pos 页面选择序号
     */
    public void switchPage(int pos) {
        if (fm == null) {
            fm = getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment currentFragment = mFragments.get(i);
            if (pos == i) {
                ft.show(currentFragment);
                handingFragment = (EditorFragment) currentFragment;
                if (handingFragment.getPos() != -1 && handingFragment.getTechnology() != -1) {
                    String craftName;
                    ArrayList<TechnologyBean> craftGroups;
                    if (GlobalVariable.MODEL_2D.equals(modelType)) {
                        //2d工艺组
                        craftGroups = (ArrayList<TechnologyBean>) mkuCarrierBean.getMkus().getCraftGroups().get(handingFragment.getPos()).getCraftModels();
                    } else {
                        //3d工艺组
                        craftGroups = (ArrayList<TechnologyBean>) simple3DModel.craftModels;
                    }
                    if (craftGroups.size() != 0 && craftGroups.size() >= handingFragment.getTechnology()) {
                        craftName = craftGroups.get(handingFragment.getTechnology()).getCraftName();
                        setTechnologyName(craftName);
                    }
                }
            } else {
                ft.hide(currentFragment);
            }
        }
        fm.executePendingTransactions();
        ft.commit();
    }


    /**
     * 显示spu选择弹窗
     */
    private void showSpuChooseDialog() {
        CustomChooseSpuDialog dialog = new CustomChooseSpuDialog(mContext);
        dialog.setSpuData(goodsDetailMallBean);
        dialog.setCancelable(false);
        dialog.setListener(new CustomChooseSpuDialog.OnCloseListener() {
            @Override
            public void dialogClosed() {
                finish();
            }

            @Override
            public void chooseMku(String mku) {
                mkuId = mku;
                mPresenter.getCarrierInfo(mku);
            }
        });
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            lp.width = (int) (ScreenUtils.getScreenWidth(mContext) - PixelUtil.dp2px(15 * 2));
            dialogWindow.setAttributes(lp);

        }
    }

    /**
     * 设置工艺标题
     *
     * @param technologyName 工艺名称
     */
    private void setTechnologyName(String technologyName) {
        mTitle.setText(getResources().getString(R.string.show_technology, technologyName));
    }


    /**
     * EventBus
     * 保存数据成功，更新当前数据是否保存过的标识
     *
     * @param event 消息类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage event) {
        if (EventBusEvent.EDITOR_HAS_CHANGED.equals(event.eventType)) {
            haschanged = true;
        } else if (EventBusEvent.EDITOR_HAS_SAVED.equals(event.eventType)) {
            haschanged = false;
        } else if (EventBusEvent.EDITOR_INIT_FAILED.equals(event.eventType)) {
            showNetworkFail("数据错误");
        }
    }


    @Override
    public boolean hasTitle() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
        switch (event.getEventType()) {
            case EventBusEvent.RECEIVE_WHITE_TO_ALPH_URL:
//                白色背景透明化页面
                Boolean isAlph = (Boolean) event.getEventObj();
//                白色背景透明化页面
                if (handingFragment != null) {
                    handingFragment.changStickerDrawable(isAlph);
                }
                break;
            case EventBusEvent.NOTIFIED_STICKER_CHANGED:
//          通知当前sticker更新 滤镜
                StickerDataInfo info = (StickerDataInfo) event.getEventObj();
                if (handingFragment != null) {
                    handingFragment.notifiedStickerChanged(info);
                }
                break;
            case EventBusEvent.RECEIVE_NETWORK_PICTURE_INFO:
//                获取网络图片
                showLoadingDialog();
                StickerPicInfoBean stickerPicInfoBean = (StickerPicInfoBean) event.getEventObj();
                if (stickerPicInfoBean != null) {
                    getNetPicSuccess(stickerPicInfoBean);
                } else {
                    disMisLoadingDialog();
                }
                break;

            case EventBusEvent.DESIGN_MATERIAL_FRAGMENT_INIT_FINISH:
//                当前选中fragment初始化成功
                Object eventObj = event.getEventObj();
                if (eventObj instanceof EditorFragment) {
                    EditorFragment fragment = (EditorFragment) eventObj;

                    if (fragment == handingFragment) {
//                    如果当前fragment加载完成
                        Log.d(TAG, "onEvent: 我初始化完成了");
                        if (materialDetailBean != null) {
                            if (!StringUtil.isEmpty(materialDetailBean.fileMime) && !StringUtil.isEmpty(materialDetailBean.scale) && Float.valueOf(materialDetailBean.scale) != 0) {
                                //所需字段全
                                showMaterialDetail(materialDetailBean);
                            } else {
                                mPresenter.addMaterialDetail(materialDetailBean.sourceMaterialId);
                            }

                        } else {
                            showMaterialDetail(null);
                        }
                    }
//                注册stickerViewLister
                    initStickerViewListener(fragment);
                }
                break;

            case EventBusEvent.SELECTED_TECHNOLOGY:
//               工艺
                TechnologyEvent bean = (TechnologyEvent) event.getEventObj();
                if (handingFragment != null) {
                    handingFragment.setTechnology(bean.technologyPos);
                    setTechnologyName(bean.technologyName);
                }
                break;

            case EventBusEvent.CHANGE_ART_FONT:
                //更换字体
                if (handingFragment != null) {
                    ArtFontTopicBean artFontTopicBean = (ArtFontTopicBean) event.getEventObj();
                    mPresenter.changeTypeFace(artFontTopicBean.getWordartName(), artFontTopicBean.getWordartFileName(), artFontTopicBean.text, artFontTopicBean.getVersion(), artFontTopicBean.getWordartId());
                }
                break;

            case EventBusEvent.ADD_EDITOR_WORD:
                //增加流行语
                if (handingFragment != null) {
                    ArtFontTopicBean artFontTopicBean = (ArtFontTopicBean) event.getEventObj();
                    mPresenter.changeTypeFace(artFontTopicBean.getWordartName(), artFontTopicBean.getWordartFileName(), artFontTopicBean.text, artFontTopicBean.getVersion(), artFontTopicBean.getWordartId());
                }
                break;

        }
    }

    /**
     * EventBus通知
     * 通知去下载字体
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventFontToDownLoadBean event) {
        // url
        final String fileName = event.fileName;
        final String fontName = event.fontName;
        mPresenter.releaseDownLoadFont(fileName, fontName);
    }

    /**
     * EventBus通知
     * 通知添加素材
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final AddEditorMaterialBean event) {
        addMaterial(event);
    }

    /**
     * EventBus通知
     * 字体下载完成 并通知所有使用这个字体的字体控件更新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventDownSuccessFont event) {
        final String fileName = event.fileName;
        final String fontName = event.fontName;
        final String wordartId = event.wordartId;
        final int version = event.version;
        Typeface typeface = event.typeface;
        //通知所有用这个字体的我下载失败了
        for (int i = 0; i < mFragments.size(); i++) {
            EditorFragment fragment = mFragments.get(i);
            fragment.reSetStickerTypeFace(fileName, fontName, typeface, version, wordartId);

        }
    }


    /**
     * 将为fragment的StickerView 设置监听
     */
    private void initStickerViewListener(EditorFragment fragment) {
        if (fragment != null) {
            StickerView.OnStickerOperationListener onStickerOperationListener = new StickerView.OnStickerOperationListener() {
                @Override
                public void onStickerAdded(@NonNull Sticker sticker) {
                    //添加元素了
                    Log.d(TAG, "onStickerAdded");
                    if (sticker instanceof MyDrawableSticker) {
                        showImageEditTool();
                    } else if (sticker instanceof MyTextSticker) {
                        showTextEditTool();
                    }
                    toSaveStep();
                }

                @Override
                public void onStickerClicked(@NonNull final Sticker sticker) {
                    //元素被点击了 就要更新对应工具条
                    Log.d(TAG, "onStickerClicked: ");
                    //stickerView.removeAllSticker();
                    if (sticker instanceof MyDrawableSticker) {
                        llImgEditor.setVisibility(View.VISIBLE);
                        KeyboardUtils.hideSoftInput(EditorActivity.this);
                    } else if (sticker instanceof MyTextSticker) {
                        //并且在文字编辑界面
                        showTextEditTool();
                    }

                }

                @Override
                public void onStickerDeleted(@NonNull Sticker sticker) {
                    //元素被删除了
                    Log.d(TAG, "onStickerDeleted");
                    System.out.println("Sticker选中 ->null");
                    //清除回收sticker数据
                    sticker.release();
                    hideAllEditTool();
                    //保存镜像
                    toSaveStep();
                }

                @Override
                public void onStickerDragFinished(@NonNull Sticker sticker) {
                    //元素拖拽完成了
                    Log.d(TAG, "onStickerDragFinished");
//                拖拽完成保存一下
                    toSaveStep();
                }

                @Override
                public void onStickerTouchedDown(@NonNull Sticker sticker) {
                    //元素被按下了
                    Log.d(TAG, "onStickerTouchedDown: ");
                }

                @Override
                public void onStickerZoomFinished(@NonNull Sticker sticker) {
                    //控件被缩放结束
                    Log.d(TAG, "onStickerZoomFinished");
                    toSaveStep();
                }

                @Override
                public void onStickerFlipped(@NonNull Sticker sticker) {
                    Log.d(TAG, "onStickerFlipped");
                }

                @Override
                public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                    Log.d(TAG, "onDoubleTapped: double tap will be with two click");
                }

                @Override
                public void onStickerSelected(Sticker sticker) {
                    //元素被选中了
                    if (sticker == null) {
                        System.out.println("Sticker选中 ->null");
                        hideAllEditTool();
                        barItemSelected(null);
                    }
                    if (sticker instanceof MyDrawableSticker) {
                        System.out.println("Sticker选中 ->MyDrawableSticker" + sticker.toString());
                        showImageEditTool();
                        barItemSelected(null);
                    } else if (sticker instanceof MyTextSticker) {
                        System.out.println("Sticker选中 ->MyTextSticker" + sticker.toString());
                        showTextEditTool();
                    }
                }

                @Override
                public void onStickerZoom(Sticker sticker) {
                    //元素缩放过程中的回调
                    //拖拽过程回调
                    if (sticker instanceof MyDrawableSticker) {
                        StickerDataInfo info = ((MyDrawableSticker) sticker).getInfo();
                        if (info.materialId == null || !info.isVector) {
                            float currentScale = sticker.getCurrentScale();
                            if (currentScale > info.localStanderScale) {
                                //尺寸模糊了
                                ToastUtil.showShort("该尺寸打印效果不好");
                            }
                        }

                    }
                }
            };

            fragment.setOnStickerOperationListener(onStickerOperationListener);
        }
    }


    /**
     * 字体下载失败 将文字透明度修改成默认的
     */
    @Override
    public void handingTextFontChangeEndFailed() {
        ToastUtil.showShort("字体下载失败");
        disMisLoadingDialog();
        handingFragment.endChangeHandingText();
    }

    /**
     * 更新底边栏状态
     * 将选中控件变成选中状态 其他控件置未选中状态
     *
     * @param tvItem 选中的文字控件
     */
    private void barItemSelected(TextView tvItem) {
        int childCount = ll_bar.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ll_bar.getChildAt(i).setSelected(tvItem == ll_bar.getChildAt(i));
        }
    }

    /**
     * 保存当前步骤
     */
    private void toSaveStep() {
        if (handingFragment != null) {
            handingFragment.saveStep();
        }
    }

    /**
     * 显示文本编辑工具栏
     */
    private void showTextEditTool() {
        llTextEditor.setVisibility(View.VISIBLE);
        llTextAdjust.setVisibility(View.VISIBLE);
        llImgEditor.setVisibility(View.GONE);
        KeyboardUtils.hideSoftInput(this);
    }

    /**
     * 显示图片编辑工具栏
     */
    private void showImageEditTool() {
        llImgEditor.setVisibility(View.VISIBLE);
        llTextEditor.setVisibility(View.GONE);
        llTextAdjust.setVisibility(View.GONE);
        KeyboardUtils.hideSoftInput(this);
    }

    /**
     * 隐藏所有编辑工具栏（文本 图片）
     */
    private void hideAllEditTool() {
        llImgEditor.setVisibility(View.GONE);
        llTextEditor.setVisibility(View.GONE);
        llTextAdjust.setVisibility(View.GONE);
        KeyboardUtils.hideSoftInput(this);
    }


    /**
     * 成功获得网络图片数据
     *
     * @param stickerPicInfoBean 一个图片上传成功的接受实体类
     */
    private void getNetPicSuccess(StickerPicInfoBean stickerPicInfoBean) {
        createMyDrawableSticker(stickerPicInfoBean);
    }

    /**
     * 创建图片sticker new
     *
     * @param stickerPicInfoBean 显示的图片信息
     */
    private void createMyDrawableSticker(StickerPicInfoBean stickerPicInfoBean) {
        disMisLoadingDialog();
        if (handingFragment != null) {
            handingFragment.createDrawableSticker(stickerPicInfoBean);
        }
    }

    /**
     * 加载带来的素材 ，初始化结束
     *
     * @param materialDetail 素材详情类
     */
    @Override
    public void showMaterialDetail(MaterialDetailBean materialDetail) {
        if (materialDetail != null) {
            AddEditorMaterialBean addEditorMaterialBean = new AddEditorMaterialBean();
            if (StringUtil.isEmpty(materialDetail.scale) || Float.valueOf(materialDetail.scale) == 0) {
                addEditorMaterialBean.resizeScale = 1;
            } else {
                addEditorMaterialBean.resizeScale = Float.valueOf(materialDetail.scale);
            }
            addEditorMaterialBean.thumbnail = materialDetail.thumbnail;
            addEditorMaterialBean.fileMime = materialDetail.fileMime;
            addEditorMaterialBean.sourceMaterialId = materialDetail.sourceMaterialId;
            addMaterial(addEditorMaterialBean);
        }
        // 开启定时器
        timerSave();
        showContent();
        GuildView guildView = new GuildView(this, rl_content_root);
        guildView.showGuildView();

    }

    /**
     * 艺术字信息
     *
     * @param artFontBeanPageInfo 艺术字信息列表
     */
    @Override
    public void showArtFonts(PageInfo<ArtFontTopicBean> artFontBeanPageInfo) {
        textArtFontAdapter.addData(artFontBeanPageInfo.list);
    }

    /**
     * 开始处理MyTextSticker切换字体的开始
     *
     * @param text 文本内容
     */
    @Override
    public void handingTextFontChangeStart(String text) {
        showLoadingDialog();
        handingFragment.startChangeHandingTextFont(text);
    }

    /**
     * 开始处理MyTextSticker切换字体的开始
     *
     * @param text 文本内容
     */
    @Override
    public void handingTextFontChangeEnd(String fontName, String fileName, String text, int version, String wordartId) {
        String filePath = FileUtil.getFontFile(this, fileName).getAbsolutePath();
        Typeface typeface = FontCache.getFromFile(fileName, filePath);
        handingFragment.changeTextTypeface(fontName, fileName, typeface, text, version, wordartId);
        handingFragment.endChangeHandingText();
        disMisLoadingDialog();
    }

    /**
     * 设置素材对象
     *
     * @param detailBean 素材对象类
     */
    private void addMaterial(AddEditorMaterialBean detailBean) {
        if (handingFragment != null) {
            handingFragment.addMaterial(detailBean);
        }
    }


}
