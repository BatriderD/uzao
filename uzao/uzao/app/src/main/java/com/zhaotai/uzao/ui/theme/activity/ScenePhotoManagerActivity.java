package com.zhaotai.uzao.ui.theme.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaopo.flying.sticker.BitmapUtils;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ScenePhotoManagerBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.theme.adapter.ScenePhotoManagerActivityAdapter;
import com.zhaotai.uzao.ui.theme.contract.ScenePhotoManagerActivityContract;
import com.zhaotai.uzao.ui.theme.presenter.ScenePhotoManagerActivityPresenter;
import com.zhaotai.uzao.ui.util.PreviewActivity;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.MediaStoreUtil;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.SimpleDividerItemDecoration;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ScenePhotoManagerActivity extends BaseActivity implements ScenePhotoManagerActivityContract.View, View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener, OnRefreshListener, View.OnKeyListener, BaseQuickAdapter.OnItemClickListener {
    private static final String ALBUM_NAME = "ALBUM_NAME";
    private static final String ALBUM_ID = "ALBUM_ID";
    private static final String ABLE_MANAGE = "ABLE_MANAGE";
    private static final int PHOTO_CHANGED = 99;

    private final int REQUEST_CODE_CHOOSE = 77;
    private final int REQUEST_CODE_TAKE_PHOTO = 78;
    private final int TYPE_OPEN_CAMERA = 0;
    private final int TYPE_OPEN_MEDIA = 1;
    @BindView(R.id.tool_bar_right_img)
    ImageView ivRight;
    @BindView(R.id.tv_photo_all)
    TextView tvPhotoAll;
    @BindView(R.id.swipe)
    RefreshLayout mSwipe;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.fl_bottom)
    FrameLayout fl_bottom;

    @BindView(R.id.right_btn)
    Button btnRight;
    @BindView(R.id.ll_photo_editor)
    LinearLayout llPhotoEditor;

    @BindView(R.id.ll_photo_del)
    LinearLayout ll_photo_del;
    private String titleName = "美好的一天";

    private EditText edThemeName;
    private PopupWindow mPopUpWindow;
    private boolean allSelectState = false;
    private ScenePhotoManagerActivityPresenter mPresenter;
    private ScenePhotoManagerActivityAdapter mAdapter;
    private String albumId;
    private PageInfo<ScenePhotoManagerBean> data;
    private UIBottomSheet addPicBottomSheet;
    private boolean ableManage;


    public static void launch(FragmentActivity context, String albumName, String sequenceNBR, boolean ableManage) {
        Intent intent = new Intent(context, ScenePhotoManagerActivity.class);
        intent.putExtra(ALBUM_NAME, albumName);
        intent.putExtra(ALBUM_ID, sequenceNBR);
        intent.putExtra(ABLE_MANAGE, ableManage);
        context.startActivityForResult(intent, 11);
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_scene_photo_manager);
        btnRight.setText(R.string.cancel);
        ivRight.setImageResource(R.drawable.icon_up_photo);
        mTitle.setText(titleName);
        mPresenter = new ScenePhotoManagerActivityPresenter(this, this);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mSwipe.setOnRefreshListener(this);
        mAdapter = new ScenePhotoManagerActivityAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //设置上拉刷新监听
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        //设置子view监听
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
//        //设置空页面点击
        mAdapter.setEmptyStateView(this,R.drawable.ic_empty_blaumphoto,getString(R.string.empty_album_photo));
        //设置间距
        SimpleDividerItemDecoration simpleDividerItemDecoration = new SimpleDividerItemDecoration((int) PixelUtil.dp2px(12), (int) PixelUtil.dp2px(3));
        mRecyclerView.addItemDecoration(simpleDividerItemDecoration);

        titleName = getIntent().getStringExtra(ALBUM_NAME);
        albumId = getIntent().getStringExtra(ALBUM_ID);
        ableManage = getIntent().getBooleanExtra(ABLE_MANAGE, false);
        mTitle.setText(titleName);
        if (ableManage) {
            setPhotoModel();
        }
        fl_bottom.setVisibility(ableManage ? View.VISIBLE : View.GONE);
        showLoading();
    }

    @Override
    protected void initData() {
        mPresenter.getPhotoData(true, 0, albumId);


    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    @OnClick(R.id.rl_photo_manager)
    public void setManagerModel() {
        mAdapter.ableSelected(true);
        mAdapter.notifyDataSetChanged();
        ivRight.setVisibility(View.GONE);
        btnRight.setVisibility(View.VISIBLE);
        llPhotoEditor.setVisibility(View.GONE);
        mBack.setVisibility(View.GONE);
        ll_photo_del.setVisibility(View.VISIBLE);
        mTitle.setText("批量管理");
    }

    public void setPhotoModel() {
        mAdapter.ableSelected(false);
        mAdapter.notifyDataSetChanged();
        ivRight.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.GONE);
        mBack.setVisibility(View.VISIBLE);
        llPhotoEditor.setVisibility(View.VISIBLE);
        ll_photo_del.setVisibility(View.GONE);
        mTitle.setText(titleName);
    }

    @OnClick(R.id.right_btn)
    public void cancelManager() {
        setPhotoModel();
    }

    @OnClick(R.id.tool_bar_right_img)
    public void up_photo() {
        initAddPicDialog();
    }


    /**
     * 弹出增加底图 弹窗
     */
    public void initAddPicDialog() {

        if (addPicBottomSheet == null) {
            addPicBottomSheet = new UIBottomSheet(mContext);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.item_edit_theme_add_pic, null, false);
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
                                MediaStoreUtil.openCamera(ScenePhotoManagerActivity.this, REQUEST_CODE_TAKE_PHOTO);
                            } else {
//                                打开相册
                                Matisse.from(ScenePhotoManagerActivity.this)
                                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                        .capture(false)
                                        .captureStrategy(new CaptureStrategy(true, "com.zhaotai.uzao.fileprovider"))
                                        .countable(true)
                                        .maxSelectable(1)
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


    @OnClick(R.id.ll_photo_editor)
    public void editorName() {
        showPopChangeName();
    }

    @OnClick(R.id.tv_photo_del)
    public void delSelectedPhoto() {
        mPresenter.deletePhoto(albumId, mAdapter.getData());
    }


    /**
     * popUpwindow 弹窗修改相册名称
     */
    private void showPopChangeName() {
        if (mPopUpWindow != null) {
            mPopUpWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        } else {
            View itemView = View.inflate(mContext, R.layout.pop_add_new_theme, null);
            itemView.findViewById(R.id.close).setOnClickListener(this);
            itemView.findViewById(R.id.sure).setOnClickListener(this);
            itemView.findViewById(R.id.ed_add_theme_name).setOnClickListener(this);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            title.setText("编辑相册");
            edThemeName = (EditText) itemView.findViewById(R.id.ed_add_theme_name);
            edThemeName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
            edThemeName.setText(titleName);
            edThemeName.setSelection(edThemeName.getText().toString().length());
            edThemeName.setOnKeyListener(this);
            int screenWidth = ScreenUtils.getScreenWidth(mContext);
            mPopUpWindow = new PopupWindow(itemView, (int) (screenWidth - PixelUtil.dp2px(40)), ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopUpWindow.setFocusable(true);
            mPopUpWindow.setOutsideTouchable(true);
            mPopUpWindow.setBackgroundDrawable(new ColorDrawable());
            mPopUpWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            lightOff();
            /**
             * 消失时屏幕变亮
             */
            mPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

                    layoutParams.alpha = 1.0f;

                    getWindow().setAttributes(layoutParams);
                }
            });
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            KeyboardUtils.hideSoftInput(this);
        }
        return false;
    }

    /**
     * 显示时屏幕变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        layoutParams.alpha = 0.3f;

        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                mPopUpWindow.dismiss();
                break;
            case R.id.sure:
                titleName = edThemeName.getText().toString();
                edThemeName.setText(titleName);
                mPopUpWindow.dismiss();
                mPresenter.changeAlbumName(albumId, titleName);
                break;
            case R.id.ed_add_theme_name:
                break;
        }
    }


    /**
     * 全选
     */
    @OnClick(R.id.tv_photo_all)
    public void onClickAllSelect() {
        if (allSelectState) {
            allSelectState = false;
            //变换到未选中
            tvPhotoAll.setSelected(allSelectState);
            mPresenter.changeSelectState(mAdapter.getData(), false);
            mAdapter.notifyDataSetChanged();
        } else {
            //变换到全选状态
            allSelectState = true;
            mPresenter.changeSelectState(mAdapter.getData(), true);
            mAdapter.notifyDataSetChanged();
        }
        tvPhotoAll.setSelected(allSelectState);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter.isAbleSelected() && view.getId() == R.id.iv_selector) {
            ScenePhotoManagerBean item = mAdapter.getItem(position);
            item.selected = !item.selected;
            mAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        if (data.hasNextPage) {
            int start = data.pageStartRow + data.pageRecorders;
            //加载列表数据
            mPresenter.getPhotoData(false, start, albumId);
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void stopLoadingMore() {
        mAdapter.loadMoreComplete();
    }

    @Override
    public void stopRefresh() {
        mSwipe.finishRefresh();
    }

    @Override
    public void loadingFail() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void showManagerPhotoPageInfo(PageInfo<ScenePhotoManagerBean> pageInfo) {
        this.data = pageInfo;
        if (data.currentPage == Constant.CURRENTPAGE_HOME) {
//            如果是首页 就设置新数据
            mAdapter.setNewData(this.data.list);
            if (data.totalRows < 15) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.loadMoreComplete();
            }
        } else {
//            不是第一页 就刷新
            mAdapter.addData(this.data.list);
        }
    }

    @Override
    public void showUploadSuccess() {
        setResult(PHOTO_CHANGED);
        mPresenter.getPhotoData(false, 0, albumId);
    }

    @Override
    public void showChangeNameSuccess(String titleName) {
        ToastUtil.showShort("修改成功");
        setResult(PHOTO_CHANGED);
        mTitle.setText(titleName);
    }

    @Override
    public void delSuccess() {
        mPresenter.getPhotoData(false, 0, albumId);
        setResult(PHOTO_CHANGED);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收拍照的图片
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            //获取本地图片地址
            String url = MediaStoreUtil.getCurrentPhotoPath();
            //上传图片到我的图片库
            //特别小的图片
            int[] size = BitmapUtils.calculateBitmap(url);
            if (size[1] > GlobalVariable.MINSIZE && size[0] > GlobalVariable.MINSIZE) {
                mPresenter.upLoadBitmap(url, albumId);
            } else {
                ToastUtil.showShort("您上传的图片质量较低，建议使用高清图片");
            }

        } else if (resultCode == RESULT_OK && REQUEST_CODE_CHOOSE == requestCode) {
            //接受相册获取的图片
            String url = Matisse.obtainPathResult(data).get(0);
            //上传图片到我的图片库
            int[] size = BitmapUtils.calculateBitmap(url);
            if (size[1] > GlobalVariable.MINSIZE && size[0] > GlobalVariable.MINSIZE) {
                mPresenter.upLoadBitmap(url, albumId);
            } else {
                ToastUtil.showShort("您上传的图片质量较低，建议使用高清图片");
            }
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getPhotoData(false, 0, albumId);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<ScenePhotoManagerBean> data = mAdapter.getData();
        ArrayList<String> urls = new ArrayList<>();
        for (ScenePhotoManagerBean bean : data) {
            urls.add(bean.getFileId());
        }
        PreviewActivity.StartOptionsActivity(this, view, urls, position, ApiConstants.UZAOCHINA_IMAGE_HOST);
    }
}
