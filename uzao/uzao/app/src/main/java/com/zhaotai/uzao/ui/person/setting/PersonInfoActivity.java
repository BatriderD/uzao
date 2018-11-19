package com.zhaotai.uzao.ui.person.setting;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.app.AppConfig;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.CategoryTagsBean;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bean.RegionBean;
import com.zhaotai.uzao.bean.TagBean;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.ui.person.address.RegionSelectActivity;
import com.zhaotai.uzao.utils.ACache;
import com.zhaotai.uzao.utils.GlideCircleTransform;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.SPUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.UCropUtils;
import com.zhaotai.uzao.widget.dialog.UITipDialog;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Time: 2017/5/11
 * Created by LiYou
 * Description : 个人信息
 */
public class PersonInfoActivity extends BaseActivity {

    private static final int REQUEST_CODE_CHOOSE = 23;
    private String[] items = {"女", "男"};

    //头像
    @BindView(R.id.personal_head)
    ImageView mHeadImage;
    //昵称
    @BindView(R.id.personal_name)
    TextView mName;
    //真实姓名布局
    @BindView(R.id.personal_ll_real_name)
    LinearLayout mLlRealName;
    //真实姓名
    @BindView(R.id.personal_real_name)
    TextView mRealName;

    //出生年月
    @BindView(R.id.personal_birthday)
    TextView mBirthday;
    //性别
    @BindView(R.id.personal_sex)
    TextView mSex;
    //地区
    @BindView(R.id.personal_region)
    TextView mRegion;
    //职业
    @BindView(R.id.personal_profession)
    TextView mProfession;

    //个人标签
    @BindView(R.id.personal_tags)
    TextView tvTags;

    //个人简介
    @BindView(R.id.personal_introduce)
    TextView person_introduce;
    //手机号码
    @BindView(R.id.personal_phone)
    TextView mPhone;
    @BindView(R.id.tool_title)
    TextView mTitle;
    private UITipDialog tipDialog;
    private List<Uri> mSelectedImage;

    @OnClick(R.id.tool_back)
    public void back() {
        finish();
    }

    public String aboutMe;

    //时间选择器
    private TimePickerView pvTime;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, PersonInfoActivity.class));
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    /**
     * 出生日期
     */
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(1990, 1, 1);
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();

        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(final Date date, View v) {//选中事件回调
                if (!StringUtil.isEmpty(getTime(date))) {
                    PersonInfo info = new PersonInfo();
                    info.birthDate = getTime(date);
                    Api.getDefault().changePersonInfo(info)
                            .compose(RxHandleResult.<PersonBean>handleResult())
                            .subscribe(new RxSubscriber<PersonBean>(PersonInfoActivity.this, true) {
                                @Override
                                public void _onNext(PersonBean personBean) {
                                    ToastUtil.showShort("修改成功");
                                    mBirthday.setText(getTime(date));
                                }

                                @Override
                                public void _onError(String message) {
                                    ToastUtil.showShort("修改失败");
                                }
                            });
                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setSubmitText("确定")
                .setSubmitColor(ContextCompat.getColor(PersonInfoActivity.this, R.color.red))
                .setCancelText("取消")
                .setCancelColor(ContextCompat.getColor(PersonInfoActivity.this, R.color.c656565))
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(date);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_person_info);
        mTitle.setText("个人信息");
        //初始化时间选择器
        initTimePicker();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        //获取个人信息
        Api.getDefault().getPersonInfo()
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(PersonInfoActivity.this, false) {


                    @Override
                    public void _onNext(PersonBean personBean) {
                        aboutMe = personBean.aboutMe;
                        //头像
                        Glide.with(PersonInfoActivity.this).load(ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(personBean.avatar))
                                .placeholder(R.drawable.ic_default_head)
                                .transform(new GlideCircleTransform(PersonInfoActivity.this)).into(mHeadImage);
                        //电话
                        mPhone.setText(personBean.mobile);
                        //昵称
                        if (!personBean.nickName.isEmpty()) {
                            mName.setText(personBean.nickName);
                        }
                        //真实姓名
                        if (!personBean.realName.isEmpty()) {
                            mRealName.setText(personBean.realName);
                        }
                        //出生日期
                        if (personBean.birthDate != null) {
                            mBirthday.setText(TimeUtils.millis2String(Long.parseLong(personBean.birthDate),
                                    new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));
                        }
                        //性别
                        if (StringUtil.isEmpty(personBean.gender) || Integer.valueOf(personBean.gender) == 1) {
                            mSex.setText("男");
                        } else {
                            mSex.setText("女");
                        }
                        //职业
                        if (!personBean.profession.isEmpty()) {
                            mProfession.setText(personBean.profession);
                        }
                        //地区
                        if (!StringUtil.isEmpty(personBean.provinceName)) {
                            mRegion.setText(personBean.provinceName + " " + personBean.cityName + " " + personBean.regionName);
                        }
                        //标签选择
                        if (personBean.tags != null && personBean.tags.size() != 0) {
                            List<TagBean> tags = personBean.tags;
                            StringBuilder tag = new StringBuilder();
                            for (int i = 0; i < tags.size(); i++) {
                                TagBean tagsBean = tags.get(i);
                                if (tagsBean != null) {
                                    tag.append(tagsBean.tagName);
                                    if (i != tags.size() - 1) {
                                        tag.append(",");
                                    }
                                }

                            }
                            tvTags.setText(tag);
                        } else {
                            tvTags.setText(R.string.please_choose);
                        }
                        //个人简介选择
                        if (StringUtil.isEmpty(personBean.aboutMe)) {
                            person_introduce.setText(R.string.please_choose);
                        } else {
                            person_introduce.setText(personBean.aboutMe);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 修改头像
     */
    @OnClick(R.id.personal_ll_head)
    public void changeHeadImage() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Matisse.from(PersonInfoActivity.this)
                                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                                    .capture(true)
                                    .captureStrategy(new CaptureStrategy(true, "com.zhaotai.uzao.fileprovider"))
                                    .countable(true)
                                    .maxSelectable(1)
                                    .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .thumbnailScale(0.85f)
                                    .imageEngine(new GlideEngine())
                                    .forResult(REQUEST_CODE_CHOOSE);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收图片
//        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
//            final Uri resultUri = UCrop.getOutput(data);
//        } else if (resultCode == UCrop.RESULT_ERROR) {
//            final Throwable cropError = UCrop.getError(data);
//        }

        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelectedImage = Matisse.obtainResult(data);
            UCropUtils.startUCrop(this, Matisse.obtainPathResult(data).get(0), UCrop.REQUEST_CROP, 16, 16,50);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
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
                compressImage(saveFile.getAbsolutePath());
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

    /**
     * 压缩图片
     *
     * @param path 图片地址
     */
    private void compressImage(String path) {
        showProgress();
        uploadImage(new File(path));
    }


    /**
     * 上传图片到服务器
     *
     * @param file 图片地址
     */
    private void uploadImage(File file) {
        //上传头像
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Api.getDefault().uploadTheFile(body)
                .compose(RxHandleResult.<UploadFileBean>handleResult())
                .subscribe(new RxSubscriber<UploadFileBean>(PersonInfoActivity.this, false) {

                    @Override
                    public void _onNext(UploadFileBean s) {
                        //上传头像图片名字
                        changeHeadImage(s.fileId);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("上传失败");
                        stopProgress();
                    }
                });
    }

    /**
     * 显示加载框
     */
    public void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在上传")
                .create();
        tipDialog.show();
    }

    /**
     * 停止加载框
     */
    public void stopProgress() {
        if (tipDialog != null) {
            tipDialog.dismiss();
        }
    }

    /**
     * 昵称
     */
    @OnClick(R.id.personal_ll_nick_name)
    public void changeNickName() {
        ChangeNickNameActivity.launch(this, mName.getText().toString());
    }

    /**
     * 真实姓名
     */
    @OnClick(R.id.personal_ll_real_name)
    public void changeRealName() {
        ChangeRealNameActivity.launch(this, mRealName.getText().toString());
    }

    /**
     * 出生年月日
     */
    @OnClick(R.id.personal_ll_birthday)
    public void birthday() {
        pvTime.show();
    }

    /**
     * 性别
     */
    @OnClick(R.id.personal_ll_sex)
    public void chooseGender() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("选择性别")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeGender(which);
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 地区
     */
    @OnClick(R.id.personal_ll_region)
    public void region() {
        RegionSelectActivity.launch(this);
    }


    /**
     * 职业
     */
    @OnClick(R.id.personal_ll_profession)
    public void profession() {
        ChangeProfessionActivity.launch(this, mProfession.getText().toString());
    }

    /**
     * 标签
     */
    @OnClick(R.id.personal_ll_tag)
    public void toChangeTag() {
        ChangeTagsActivity.launch(this, ChangeTagsActivity.TAG_TYPE_designer);
    }

    /**
     * 个人简介
     */
    @OnClick(R.id.personal_ll_introduce)
    public void toChangeIntroduce() {
        if (aboutMe == null) {
            aboutMe = "";
        }
        ChangeIntroduceActivity.launch(this, aboutMe);
    }

    /**
     * 修改性别
     */
    private void changeGender(final int gender) {
        PersonInfo info = new PersonInfo();
        info.gender = gender;
        Api.getDefault().changePersonInfo(info)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(this, true) {

                    @Override
                    public void _onNext(PersonBean personBean) {
                        ACache.get(mContext).put(GlobalVariable.PERSONINFO, personBean);
                        ToastUtil.showShort("修改成功");
                        if (gender == 1) {
                            mSex.setText("男");
                        } else {
                            mSex.setText("女");
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("修改失败");
                    }
                });
    }

    /**
     * 修改地区
     */
    private void changeRegion(PersonInfo personInfo) {
        Api.getDefault().changePersonInfo(personInfo)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(this, true) {
                    @Override
                    public void _onNext(PersonBean personBean) {
//                        修改个人信息缓存
                        ACache.get(mContext).put(GlobalVariable.PERSONINFO, personBean);
                        ToastUtil.showShort("修改成功");


//                        向上一级发送修改地区
                        PersonInfo info = new PersonInfo();
                        info.code = EventBusEvent.CHANGE_REGIN;
                        info.profession = personBean.profession;
                        info.cityName = personBean.cityName;
                        EventBus.getDefault().post(info);
                    }

                    @Override
                    public void _onError(String message) {
                    }
                });
    }

    /**
     * 修改头像
     */
    private void changeHeadImage(final String avatar) {
        PersonInfo info = new PersonInfo();
        info.avatar = avatar;
        Api.getDefault().changePersonInfo(info)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(this, false) {
                    @Override
                    public void _onNext(PersonBean presonBean) {
                        stopProgress();
                        Glide.with(PersonInfoActivity.this).load(ApiConstants.UZAOCHINA_IMAGE_HOST + avatar)
                                .transform(new GlideCircleTransform(PersonInfoActivity.this)).into(mHeadImage);
//                            缓存个人信息
                        ACache.get(mContext).put(GlobalVariable.PERSONINFO, presonBean);

                        ToastUtil.showShort("上传成功");
                        SPUtils.setSharedStringData(AppConfig.USER_IAMGE, ApiConstants.UZAOCHINA_IMAGE_HOST + avatar);
                        PersonInfo info = new PersonInfo();
                        info.code = EventBusEvent.CHANGE_HEAD_IMAGE;
                        info.avatar = avatar;
                        //通知我的页面 头像更新
                        EventBus.getDefault().post(info);

                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("上传失败");
                        stopProgress();
                    }
                });
    }

    /**
     * 回调 更新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(PersonInfo info) {
        if (info != null) {
            switch (info.code) {
                //修改昵称
                case EventBusEvent.CHANGE_NICK_NAME:
                    mName.setText(info.nickName);
                    break;
                //修改昵称
                case EventBusEvent.CHANGE_REAL_NAME:
                    mRealName.setText(info.realName);
                    break;
                //修改职业
                case EventBusEvent.CHANGE_PROFESSION:
                    mProfession.setText(info.profession);
                    break;

            }
        }
    }

    /**
     * 地址修改回调
     *
     * @param list
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArrayList<RegionBean> list) {
        if (list.size() > 0) {
            String regionNameT = "";
            for (RegionBean data : list) {
                regionNameT += data.locationName + " ";
            }
            if (list.size() > 2) {
                PersonInfo personInfo = new PersonInfo();
                personInfo.provinceName = list.get(0).locationName;
                personInfo.province = list.get(0).locationCode;
                personInfo.cityName = list.get(1).locationName;
                personInfo.city = list.get(1).locationCode;
                personInfo.regionName = list.get(2).locationName;
                personInfo.region = list.get(2).locationCode;
                changeRegion(personInfo);
            }
            mRegion.setText(regionNameT);

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBean event) {
        switch (event.getEventType()) {
            case EventBusEvent.CHOOSE_TAG_Person_Tag_FINISH:
                List<CategoryTagsBean.ChildrenBean.TagsBean> tags = (List<CategoryTagsBean.ChildrenBean.TagsBean>) event.getEventObj();
                StringBuilder tag = new StringBuilder();
                for (int i = 0; i < tags.size(); i++) {
                    CategoryTagsBean.ChildrenBean.TagsBean tagsBean = tags.get(i);
                    if (tagsBean != null) {
                        tag.append(tagsBean.getTagName());
                        if (i != tags.size() - 1) {
                            tag.append(",");
                        }
                    }

                }
                if (tag.length() == 0) {
                    tvTags.setText(R.string.please_choose);
                } else {
                    tvTags.setText(tag);
                }
                break;

            case EventBusEvent.CHOOSE_INTRODUCE:
                this.aboutMe = (String) event.getEventObj();
                if (StringUtil.isEmpty(aboutMe)) {
                    person_introduce.setText(R.string.please_write);
                } else {
                    person_introduce.setText(this.aboutMe);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (pvTime.isShowing()) {
            pvTime.dismiss();
        }
    }
}