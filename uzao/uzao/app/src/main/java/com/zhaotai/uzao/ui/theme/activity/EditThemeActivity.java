package com.zhaotai.uzao.ui.theme.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryTagsBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.person.setting.ChangeTagsActivity;
import com.zhaotai.uzao.ui.theme.contract.EditThemeContract;
import com.zhaotai.uzao.ui.theme.presenter.EditThemePresenter;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.MediaStoreUtil;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.UCropUtils;
import com.zhaotai.uzao.widget.dialog.UIBottomSheet;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description : 添加主题页面
 */

public class EditThemeActivity extends BaseActivity implements EditThemeContract.View {

    private final int REQUEST_CODE_CHOOSE = 77;
    private final int REQUEST_CODE_TAKE_PHOTO = 78;
    private final int TYPE_OPEN_CAMERA = 0;
    private final int TYPE_OPEN_MEDIA = 1;


    @BindView(R.id.right_btn)
    public Button btnRight;


    @BindView(R.id.tv_theme_close)
    public TextView tvThemeClose;

    @BindView(R.id.tv_theme_open)
    public TextView tvThemeOpen;

    @BindView(R.id.tv_add_theme_tag)
    public TextView tv_add_tag;


    @BindView(R.id.rl_theme_tag)
    public RelativeLayout rl_tag;

    @BindView(R.id.iv_cover)
    public ImageView iv_cover;

    @BindView(R.id.et_theme_intro)
    public EditText etIntro;

    @BindView(R.id.ed_add_theme_name)
    public EditText etName;

    @BindView(R.id.tv_theme_intro_left)
    public TextView tv_theme_intro_left;
    private UIBottomSheet addPicBottomSheet;
    private EditThemePresenter editThemePresenter;
    private String url;
    private List<ThemeBean.TagsBean> tags;
    private String themeId;

    /**
     * 修改主题
     *
     * @param context context
     * @param themeId 主题id
     */
    public static void launch(Context context, String themeId) {
        Intent intent = new Intent(context, EditThemeActivity.class);
        intent.putExtra("themeId", themeId);
        context.startActivity(intent);
    }

    /**
     * 新建主题
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, EditThemeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit_theme);
        btnRight.setText(R.string.next_step);
        btnRight.setVisibility(View.VISIBLE);
        //设置最多字数
        etIntro.setFilters(new InputFilter[]{new InputFilter.LengthFilter(GlobalVariable.THEME_ABOUT_ME_SIZE)});
        //字数监听，并显示剩余字数
        etIntro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = GlobalVariable.THEME_ABOUT_ME_SIZE - s.length();
                tv_theme_intro_left.setText("剩余" + length + "字");
            }
        });
        editThemePresenter = new EditThemePresenter(this, this);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        themeId = getIntent().getStringExtra("themeId");
        if (StringUtil.isEmpty(themeId)) {
            mTitle.setText("新建场景");
            onChangeThemeStatus(tvThemeOpen);
            tv_theme_intro_left.setText("剩余200字");
        } else {
            mTitle.setText("编辑场景");
            editThemePresenter.getThemeData(themeId);
        }

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return true;
    }

    //全局定义
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 500;  // 快速点击间隔
    /**
     * 保存
     * 防止连续点击
     */
    @OnClick(R.id.right_btn)
    public void onSave() {
        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME){
            return;
        }
        lastClickTime = System.currentTimeMillis();
        //名称
        String themeName = etName.getText().toString();
        String description = etIntro.getText().toString();
        String isPublic = tvThemeOpen.isSelected() ? "Y" : "N";
        //检查数据 ，正常就保存
        editThemePresenter.checkData(themeId, themeName, url, tags, description, isPublic);

    }

    @OnClick(R.id.sc_root)
    public void rootClick() {
        //关闭弹窗
        KeyboardUtils.hideSoftInput(this);
    }

    @OnClick(R.id.iv_cover)
    public void onSelectPic() {
        initAddPicDialog();
    }

    @OnClick(R.id.rl_theme_tag)
    public void selectThemeTag() {
//        选择tag
        ChangeTagsActivity.launch(this, ChangeTagsActivity.TAG_TYPE_theme);
    }

    @OnClick({R.id.tv_theme_open, R.id.tv_theme_close})
    public void onChangeThemeStatus(View view) {
        switch (view.getId()) {
            case R.id.tv_theme_open:
                tvThemeClose.setSelected(false);
                tvThemeOpen.setSelected(true);
                break;

            case R.id.tv_theme_close:
                tvThemeClose.setSelected(true);
                tvThemeOpen.setSelected(false);
                break;
        }

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
                                MediaStoreUtil.openCamera(EditThemeActivity.this, REQUEST_CODE_TAKE_PHOTO);
                            } else {
//                                打开相册
                                Matisse.from(EditThemeActivity.this)
                                        .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                        .capture(true)
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


    /**
     * 处理图像页面
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String url ;
        //接收图片
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            url = MediaStoreUtil.getCurrentPhotoPath();
            UCropUtils.startUCrop(this,url, UCrop.REQUEST_CROP, 82, 55,80);
        } else if (resultCode == RESULT_OK && REQUEST_CODE_CHOOSE == requestCode) {
            url = Matisse.obtainPathResult(data).get(0);
            UCropUtils.startUCrop(this,url, UCrop.REQUEST_CROP, 82, 55,80);
        }


        else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            Uri croppedFileUri = UCrop.getOutput(data);
            //获取默认的下载目录
            String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());
            File saveFile = new File(downloadsDirectoryPath, filename);
            //保存下载的图片
            FileInputStream inStream = null;
            FileOutputStream outStream = null;
            FileChannel inChannel = null;
            FileChannel outChannel = null;
            try {
                inStream = new FileInputStream(new File(croppedFileUri.getPath()));
                outStream = new FileOutputStream(saveFile);
                inChannel = inStream.getChannel();
                outChannel = outStream.getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                editThemePresenter.upThemePic(saveFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    outChannel.close();
                    outStream.close();
                    inChannel.close();
                    inStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //裁切失败
        else if (requestCode == UCrop.REQUEST_CROP && resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "裁切图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showThemeCover(String url) {
        this.url = url;
        String changeSizeUrl = ImageSizeUtil.changeImageSize(url);
        Glide.with(mContext)
                .load(ApiConstants.UZAOCHINA_IMAGE_HOST + changeSizeUrl)
                .into(iv_cover);
    }

    @Override
    public void showSaveSuccess(ThemeBean bean, String themeId) {
        this.themeId = themeId;
        ThemeSettingActivity.launch(this, bean, themeId);
    }

    @Override
    public void showTheme(ThemeBean themeBean) {
        etIntro.setText(themeBean.description);
        etName.setText(themeBean.name);
        tags = themeBean.tags;
        url = themeBean.cover;
        Glide.with(mContext)
                .load(ApiConstants.UZAOCHINA_IMAGE_HOST + url)
                .into(iv_cover);

        //设置标签
        StringBuilder tag = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            tag.append(tags.get(i).tagName);
            if (i != tags.size() - 1) {
                tag.append(",");
            }
        }
        tv_add_tag.setText(tag);

        boolean open = "Y".equals(themeBean.isPublic);
        tvThemeClose.setSelected(!open);
        tvThemeOpen.setSelected(open);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
        switch (event.getEventType()) {
            case EventBusEvent.CHOOSE_TAG_THEME_TAG_FINISH:
                ArrayList<CategoryTagsBean.ChildrenBean.TagsBean> tags = (ArrayList<CategoryTagsBean.ChildrenBean.TagsBean>) event.getEventObj();
                ArrayList<ThemeBean.TagsBean> myTag = new ArrayList<>();
                StringBuilder tag = new StringBuilder();
                for (int i = 0; i < tags.size(); i++) {
                    tag.append(tags.get(i).getTagName());
                    if (i != tags.size() - 1) {
                        tag.append(",");
                    }

                    CategoryTagsBean.ChildrenBean.TagsBean tagsBean = tags.get(i);
                    ThemeBean.TagsBean myTagBean = new ThemeBean.TagsBean(tagsBean.getTagCode(), tagsBean.getTagName());
                    myTag.add(myTagBean);
                }
                this.tags = myTag;
                tv_add_tag.setText(tag);
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
}
