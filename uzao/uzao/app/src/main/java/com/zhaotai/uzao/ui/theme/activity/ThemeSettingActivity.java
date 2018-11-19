package com.zhaotai.uzao.ui.theme.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.TemplateJsonBean;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.ui.category.goods.activity.DesignProductListActivity;
import com.zhaotai.uzao.ui.designer.activity.NewDesignerListActivity;
import com.zhaotai.uzao.ui.theme.adapter.ThemeSettingAdapter;
import com.zhaotai.uzao.ui.theme.adapter.ThemeSettingModuleAdapter;
import com.zhaotai.uzao.ui.theme.contract.ThemeSettingContract;
import com.zhaotai.uzao.ui.theme.presenter.ThemeSettingPresenter;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 主题设置页面
 * author : ZP
 * date: 2018/1/23 0023.
 */

public class ThemeSettingActivity extends BaseActivity implements ThemeSettingContract.View, ThemeSettingAdapter.OnModuleClickListener, BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener {

    private static final String THEME_BEAN = "theme";
    private static final String THEME_ID = "theme_id";
    @BindView(R.id.right_btn)
    public Button btnRight;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_add_module)
    public TextView tvAddModule;


    private ThemeSettingAdapter mAdapter;
    private ThemeSettingPresenter mPresenter;
    private UIBottomSheet selectedModuleTypeUnderDialog;
    private String themeId;
    //主题设置的adapter
    private ThemeSettingModuleAdapter mModuleAdapter;
    private int mPos;

    private boolean isAdd;
    private ThemeBean themeBean;
    //模块的位置
    private int mModulePos = -1;
    private UIBottomSheet selectEntityDialog;

    public static void launch(Context context, ThemeBean bean, String themeId) {
        Intent intent = new Intent(context, ThemeSettingActivity.class);
        intent.putExtra(THEME_ID, themeId);
        intent.putExtra(THEME_BEAN, bean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting_theme);

        mTitle.setText("场景设置");
        btnRight.setText(R.string.next_step);
        btnRight.setVisibility(View.VISIBLE);

        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ThemeSettingAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecycler);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnModuleClickListener(this);
        mPresenter = new ThemeSettingPresenter(this, this);

    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        themeId = intent.getStringExtra(THEME_ID);
        themeBean = (ThemeBean) intent.getSerializableExtra(THEME_BEAN);
        themeBean.avatar = LoginHelper.getAvatar();
        mPresenter.getThemeContent(themeId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    //下一步
    @OnClick(R.id.right_btn)
    public void onNextStepClick() {
        //处理所有数据
        showLoadingDialog();
        //处理数据
        List<ThemeModuleBean> data = mAdapter.getData();
        //解析处理数据
        mPresenter.resultData(themeBean, themeId, data);
    }


    /**
     * 内层模板的点击事件
     *
     * @param entityType 模块类型
     * @param adapter    adapter
     * @param view       view
     * @param position   位置
     */
    @Override
    public void onModuleClick(String entityType, boolean ableAdd, ThemeSettingModuleAdapter adapter, View view, int position) {
        if (ableAdd) {
            ThemeModuleBean.ThemeContentModel themeContentModel = adapter.getData().get(position);
            changeModuleData(adapter, position, StringUtil.isEmpty(themeContentModel.getEntityId()));
            editorEntity(entityType, position, isAdd);
        } else {
            ToastUtil.showShort("不可修改");
        }
    }

    @OnClick(R.id.tv_add_module)
    public void AddModule() {
        addNewModule(0);
    }

    /**
     * 新增模块
     * 弹出底部弹窗 选择模块类型
     *
     * @param pos 模块位置
     */
    public void addNewModule(int pos) {
        mModulePos = pos;
        if (selectedModuleTypeUnderDialog == null) {
            selectedModuleTypeUnderDialog = new UIBottomSheet(mContext);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.item_theme_selecte_module_type, null, false);
            // 增加定制商品模块
            bottomSheetView.findViewById(R.id.tv_add_custom_product).setOnClickListener(this);
            //增加商品模块
            bottomSheetView.findViewById(R.id.tv_add_product).setOnClickListener(this);
            //增加素材模块
            bottomSheetView.findViewById(R.id.tv_add_material).setOnClickListener(this);
            //设计师模块
            bottomSheetView.findViewById(R.id.tv_add_designer).setOnClickListener(this);
            //最近新增商品模块
            bottomSheetView.findViewById(R.id.tv_add_new_spu).setOnClickListener(this);
            //最近新增素材模块
            bottomSheetView.findViewById(R.id.tv_add_new_material).setOnClickListener(this);
            //取消
            bottomSheetView.findViewById(R.id.tv_cancel).setOnClickListener(this);
            selectedModuleTypeUnderDialog.setContentView(bottomSheetView);
        }
        selectedModuleTypeUnderDialog.show();
    }

    /**
     * 模块adapter的子view点击事件
     *
     * @param adapter  模块adapter
     * @param view     选中view
     * @param position 位置
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_add_module:
                //增加模块里面的商品/素材
                addNewModule(position + 1);
                break;
            case R.id.tv_edit_module:
                //保存或者编辑
                ThemeModuleBean themeModuleBean = mAdapter.getData().get(position);
                List<ThemeModuleBean.ThemeContentModel> moduleBeans = themeModuleBean.getThemeContentModels();
                int moduleSize = moduleBeans.size();
                if (themeModuleBean.isCanBeEdit()) {
                    //编辑状态 ，可以被保存
                    ThemeModuleBean.ThemeContentModel lastThemeContentModel = moduleBeans.get(moduleBeans.size() - 1);
                    if (StringUtil.isEmpty(lastThemeContentModel.getEntityType())) {
                        moduleSize = moduleSize - 1;
                    }
                    EditText tvIntro = (EditText) mAdapter.getViewByPosition(position, R.id.tv_intro);
                    String intro = tvIntro.getText().toString();
                    themeModuleBean.setDescription(intro);
                    if (moduleSize == 0 || intro.length() == 0) {
                        ToastUtil.showShort("请完善简介和商品内容");
                    } else {
                        mPresenter.saveModule(themeId, position, themeModuleBean);
                    }
                } else {
                    //准备编辑
                    if (moduleSize < 9) {
                        moduleBeans.add(new ThemeModuleBean.ThemeContentModel());
                    }
                    //设置编辑状态
                    themeModuleBean.setCanBeEdit(true);
                    mAdapter.notifyItemChanged(position);
                }


                break;
            case R.id.tv_del_module:
                //删除指定模块
                mPresenter.delModule(themeId, position);
                break;
        }

    }


    /**
     * 弹出增加元素方式选择 弹窗
     * 类型  empty 分为可定制商品custom_product，商品 product ,素材 material ，设计师 designer， 最近新增商品 new_product,最近新增素材 new_material
     */
    public void editorEntity(String entityType, final int pos, final boolean isAdd) {
        if (isAdd) {
            //是新增的 根据新模块进入对应页面
            enterWithType(entityType);
        } else {
            //是修改的 弹窗进入对应页面
            changeWithType(entityType, pos);
        }
    }

    View.OnClickListener onChangeEntityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof String) {
                enterWithType((String) tag);
            }
            selectEntityDialog.dismiss();
        }
    };

    private void changeWithType(String entityType, final int pos) {
        selectEntityDialog = new UIBottomSheet(mContext);
        View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.item_theme_selecte_entity, null, false);
        bottomSheetView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectEntityDialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.tv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModuleAdapter.remove(pos);
                if (pos == 8) {
                    mModuleAdapter.addData(new ThemeModuleBean.ThemeContentModel());
                }
                selectEntityDialog.dismiss();
            }
        });
        TextView selectedView = null;
        switch (entityType) {
            case ThemeModuleBean.TYPE_CUSTOM_SPU:
                //可定制商品
                // 增加商品
                selectedView = (TextView) bottomSheetView.findViewById(R.id.tv_add_product);
                break;

            case ThemeModuleBean.TYPE_SPU:
                //商品
                selectedView = (TextView) bottomSheetView.findViewById(R.id.tv_add_product);
                break;
            case ThemeModuleBean.TYPE_MATERIAL:
                // 素材
                selectedView = (TextView) bottomSheetView.findViewById(R.id.tv_add_material);
                break;
            case ThemeModuleBean.TYPE_DESIGNER:
                //设计师
                selectedView = (TextView) bottomSheetView.findViewById(R.id.tv_add_designer);
                break;
            case ThemeModuleBean.TYPE_NEW_SPU:
                //新增商品
                selectedView = (TextView) bottomSheetView.findViewById(R.id.tv_add_new_spu);
                break;
            case ThemeModuleBean.TYPE_NEW_MATERIAL:
                //新增素材
                selectedView = (TextView) bottomSheetView.findViewById(R.id.tv_add_new_material);
                break;
        }
        if (selectedView != null) {
            selectedView.setTag(entityType);
            selectedView.setOnClickListener(onChangeEntityListener);
            selectedView.setVisibility(View.VISIBLE);
        }
        selectEntityDialog.setContentView(bottomSheetView);
        selectEntityDialog.show();
    }

    //进入对应页面
    private void enterWithType(String entityType) {
        switch (entityType) {
            case ThemeModuleBean.TYPE_CUSTOM_SPU:
                //可定制商品
                DesignProductListActivity.launchAddToTheme(this);
                break;

            case ThemeModuleBean.TYPE_SPU:
                //商品
                AddProductToThemeActivity.launch(ThemeSettingActivity.this);
                break;
            case ThemeModuleBean.TYPE_MATERIAL:
                // 素材
                AddMaterialToThemeActivity.launch(ThemeSettingActivity.this);
                break;
            case ThemeModuleBean.TYPE_DESIGNER:
                //设计师  weijiejue
                NewDesignerListActivity.launchAddTheme(ThemeSettingActivity.this);
                break;
            case ThemeModuleBean.TYPE_NEW_SPU:
                //新增商品
                NewAddListActivity.launch(ThemeSettingActivity.this, themeId, "addSpu");
                break;
            case ThemeModuleBean.TYPE_NEW_MATERIAL:
                //新增素材
                NewAddListActivity.launch(ThemeSettingActivity.this, themeId, "addMaterial");
                break;
        }
    }

    /**
     * 更改全局变量 记录选中模块位置和记住adapter
     * 后面接收到EventBus回调的时候 知道处理哪个adapter的那个位置。
     */

    private void changeModuleData(ThemeSettingModuleAdapter moduleAdapter, int pos, boolean isAdd) {
        this.isAdd = isAdd;
        this.mModuleAdapter = moduleAdapter;
        this.mPos = pos;
    }

    /**
     * 正确显示模块数据
     *
     * @param themeModuleBean 模块数据
     */
    @Override
    public void showThemeContent(List<ThemeModuleBean> themeModuleBean) {
        for (ThemeModuleBean bean : themeModuleBean) {
            bean.setCanBeEdit(false);
        }
        if (themeModuleBean.size() > 0) {
            tvAddModule.setVisibility(View.GONE);
        } else {
            tvAddModule.setVisibility(View.VISIBLE);
        }
        mAdapter.setNewData(themeModuleBean);
    }

    /**
     * 提示模块保存成功，并修改模块状态为保存状态，和修改相应ui界面
     *
     * @param position 位置
     */
    @Override
    public void showSaveModuleSuccess(int position) {
        ThemeModuleBean item = mAdapter.getItem(position);
        item.setPartNo(String.valueOf(position));
        item.setCanBeEdit(!item.isCanBeEdit());
        List<ThemeModuleBean.ThemeContentModel> themeContentModels = item.getThemeContentModels();
        ThemeModuleBean.ThemeContentModel lastModule = item.getThemeContentModels().get(themeContentModels.size() - 1);
        if (StringUtil.isEmpty(lastModule.getEntityType())) {
            themeContentModels.remove(themeContentModels.size() - 1);
        }
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void showDeleteSuccess(int position) {
        ToastUtil.showShort("删除成功");
        if (mAdapter.getData().size() == 1) {
            tvAddModule.setVisibility(View.VISIBLE);
        }
        mAdapter.remove(position);
    }

    /**
     * 收集模块数据成功
     *
     * @param templateJsonBean 模块数据的json
     * @param upSaveModuleList 上传样式的模块数据
     */
    @Override
    public void collectDataSuccess(TemplateJsonBean templateJsonBean, ArrayList<ThemeModuleBean> upSaveModuleList) {
        disMisLoadingDialog();
        ThemeTemplateActivity.launch(this, templateJsonBean, themeId, upSaveModuleList);
    }

    /**
     * 收集模块数据失败
     */
    @Override
    public void showCollectFailed() {
        disMisLoadingDialog();
    }

    /**
     * 接收到其他页面选择商品或者素材的时间
     *
     * @param event 商品的信息包装类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
        switch (event.getEventType()) {
            case EventBusEvent.ADD_MODULE_TO_THEME:
                if (mModuleAdapter == null || mPos == -1) {
                    return;
                }
                ThemeModuleBean.ThemeContentModel contentModel = (ThemeModuleBean.ThemeContentModel) event.getEventObj();
                List<ThemeModuleBean.ThemeContentModel> data = mModuleAdapter.getData();
                if (isAdd) {
                    //是新增的
                    if (data.size() == 9) {
                        //不能添加更多了
                        mModuleAdapter.remove(mPos);
                        mModuleAdapter.addData(contentModel);
                    } else {
                        mModuleAdapter.addData(mPos, contentModel);
                    }
                } else {
                    //是修改的
                    data.set(mPos, contentModel);
                    //  通知adapter刷新指定位置数据
                    mModuleAdapter.notifyItemChanged(mPos);
                }

                mModuleAdapter = null;
                mPos = -1;
                break;

            case EventBusEvent.SAVE_THEME_TEMPLATE_SUCCESS:
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 底部模块类别的点击事件处理
     *
     * @param v 点击view
     */
    @Override
    public void onClick(View v) {
        ThemeModuleBean themeModule = mPresenter.createThemeModule();
        switch (v.getId()) {
            case R.id.tv_add_custom_product:
                //新增定制商品
                themeModule.entityType = ThemeModuleBean.TYPE_CUSTOM_SPU;
                break;
            case R.id.tv_add_product:
                //新增商品
                themeModule.entityType = ThemeModuleBean.TYPE_SPU;
                break;
            case R.id.tv_add_material:
                //新增素材
                themeModule.entityType = ThemeModuleBean.TYPE_MATERIAL;
                break;
            case R.id.tv_add_designer:
                //新增设计师
                themeModule.entityType = ThemeModuleBean.TYPE_DESIGNER;
                break;
            case R.id.tv_add_new_spu:
                //新增商品
                themeModule.entityType = ThemeModuleBean.TYPE_NEW_SPU;
                break;
            case R.id.tv_add_new_material:
                //新增素材
                themeModule.entityType = ThemeModuleBean.TYPE_NEW_MATERIAL;
                break;
            case R.id.tv_cancel:
                //新增素材
                selectedModuleTypeUnderDialog.dismiss();
                return;
        }
        mAdapter.addData(mModulePos, themeModule);
        tvAddModule.setVisibility(View.GONE);
        selectedModuleTypeUnderDialog.dismiss();
    }
}
