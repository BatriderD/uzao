package com.zhaotai.uzao.ui.poster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PosterItemBean;
import com.zhaotai.uzao.utils.AntiShake;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.PixelUtil;
import com.zhaotai.uzao.utils.ScreenUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.transform.CornerTransform;
import com.zhaotai.zxing.QRCodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.zhaotai.uzao.bean.PosterItemBean.FileCode_Code;
import static com.zhaotai.uzao.bean.PosterItemBean.TYPE_IMAGE;
import static com.zhaotai.uzao.bean.PosterItemBean.TYPE_TEXT;

/**
 * Time: 2017/8/23
 * Created by zp
 * Description : 海报分享页面
 */
public class PosterActivity extends BaseActivity implements PosterContract.View {


    public static final String TYPE_MATERIAL = "material";
    public static final String TYPE_MATERIAL_BENEFIT = "material_benefit";
    public static final String TYPE_DESIGNER = "designer";
    public static final String TYPE_THEME = "theme";
    public static final String TYPE_SPU = "spu";
    public static final String TYPE = "type";
    public static final String CONTENT_ID = "CONTENT_ID";
    public static final String TEMPLATE_ID = "templateid";

    @BindView(R.id.rl_poster)
    RelativeLayout rlPost;

    @BindView(R.id.rl_shout)
    RelativeLayout rl_shout;


    @BindView(R.id.ll_friend_circle)
    LinearLayout llFriendCircle;
    @BindView(R.id.ll_wx_friend)
    LinearLayout llWxFriend;

    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    @BindView(R.id.tool_back)
    ImageView back;

    @BindView(R.id.bg_poster)
    ImageView bg_poster;
    @BindView(R.id.right_btn)
    Button btnRight;
    private PosterPresenter mPresenter;
    private RelativeLayout.LayoutParams layoutParams;
    private AntiShake util = new AntiShake();
    private float scale;
    private String TAG = "AVAVAVA";
    private boolean loadFinish = false;


    /**
     * 启动本页面
     */
    private static void launch(Context context, String type, String contentId) {
        Intent intent = new Intent(context, PosterActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(CONTENT_ID, contentId);
        context.startActivity(intent);
    }

    /**
     * 启动 商品海报页
     */
    public static void launchSpu(Context context, String spuId) {
        launch(context, TYPE_SPU, spuId);
    }

    /**
     * 启动 设计师海报页
     */
    public static void launchDesigner(Context context, String designerId) {
        launch(context, TYPE_DESIGNER, designerId);
    }

    /**
     * 启动 素材海报页
     */
    public static void launchMaterial(Context context, String materialId) {
        launch(context, TYPE_MATERIAL, materialId);
    }  /**
     * 启动 素材海报页
     */
    public static void launchMaterialBenefit(Context context, String materialId) {
        launch(context, TYPE_MATERIAL_BENEFIT, materialId);
    }

    /**
     * 启动 场景海报页
     */
    public static void launchTheme(Context context, String themeId) {
        launch(context, TYPE_THEME, themeId);
    }
    /**
     * 启动 场景海报页
     */
    public static void launchWithId(Context context,String templateId,String contentId) {
        Intent intent = new Intent(context, PosterActivity.class);
        intent.putExtra(TEMPLATE_ID, templateId);
        intent.putExtra(CONTENT_ID, contentId);
        context.startActivity(intent);
    }
//
//    /**
//     * 启动 福利海报
//     */
//    public static void launchWeal(Context context, String materialId) {
//        launch(context, TYPE_MATERIAL, materialId);
//    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_poster);
//        StatusBarUtil.setTranslucent(this, 0);
        // 设置titlebar
        mTitle.setText("分享海报");
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        back.setImageResource(R.drawable.back);

        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        btnRight.setVisibility(View.VISIBLE);
        btnRight.setTextColor(ContextCompat.getColor(this, R.color.editor_switch_selected));
        btnRight.setText("保存到手机");

        this.layoutParams = (RelativeLayout.LayoutParams) rlPost.getLayoutParams();
        this.layoutParams.width = (int) (ScreenUtils.getScreenWidth(this) - PixelUtil.dp2px(20));
        this.layoutParams.height = (int) (this.layoutParams.width * 4.0f / 3f);
        this.layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlPost.setLayoutParams(this.layoutParams);
//        rlPost.setBackgroundResource(Color.WHITE);

//        计算比例系数
        scale = layoutParams.width / 480.0f;
    }


    @OnClick(R.id.right_btn)
    public void saveImage(View view) {
        if (loadFinish) {
            cancelCursor();
            if (util.check(view.getId())) return;
            saveBitmap(saveBitmapFromView(), "poster" + System.currentTimeMillis());
        } else {
            ToastUtil.showShort("加载中请稍后");
        }
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        String type = intent.getStringExtra(TYPE);
        String contentId = intent.getStringExtra(CONTENT_ID);
        if (type.equals(TYPE_MATERIAL_BENEFIT)){
            mTitle.setText("赠送好友");
        }else {
            mTitle.setText("分享海报");
        }
        mPresenter = new PosterPresenter(this, this);
//        mPresenter.getPosterData();
        mPresenter.getTemplate(type, contentId);
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick({R.id.ll_wx_friend, R.id.ll_friend_circle})
    public void shareImage(View view) {
        if (!loadFinish) {
            ToastUtil.showShort("请等待海报生成");
            return;
        }
        cancelCursor();
        UMShareListener shareListener = new UMShareListener() {
            /**
             * @descrption 分享开始的回调
             * @param platform 平台类型
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {
                Log.i("TAG", "onStart: " + "KAISHI LE ");
            }

            /**
             * @descrption 分享成功的回调
             * @param platform 平台类型
             */
            @Override
            public void onResult(SHARE_MEDIA platform) {
                ToastUtil.showLong("分享成功！");
            }

            /**
             * @descrption 分享失败的回调
             * @param platform 平台类型
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                ToastUtil.showShort("分享失败！");
            }

            /**
             * @descrption 分享取消的回调
             * @param platform 平台类型
             */
            @Override
            public void onCancel(SHARE_MEDIA platform) {
                ToastUtil.showShort("取消分享！");

            }
        };

        new ShareAction(this)
                .withText(LoginHelper.getUserName() + "分享给你一个勋章") //文本
                .withMedia(new UMImage(this, saveBitmapFromView()))
                .setPlatform(view.getId() == R.id.ll_friend_circle ? SHARE_MEDIA.WEIXIN_CIRCLE : SHARE_MEDIA.WEIXIN)//分享平台
                .setCallback(shareListener)
                .share();
    }

    /**
     * 设置背景图片
     *
     * @param url
     */
    @Override
    public void setBgImage(String url) {
//        设置外层的
        Log.d(TAG, "setBgImage: " + url);
        Glide.with(this)
                .load(url)
                .crossFade(1000)
                .skipMemoryCache(true)
                .bitmapTransform(new BlurTransformation(this, 23, 4)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        bg_poster.setBackground(resource.getCurrent());
                    }
                });
//      设置海报的边框
        Glide.with(this)
                .load(url)
                .skipMemoryCache(true)
                .bitmapTransform(new CornerTransform(this, 5))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        rlPost.setBackground(resource.getCurrent());
                        loadFinish = true;
                    }
                });

    }

    /**
     * 设置view数据
     *
     * @param beans
     */
    @Override
    public void setViewData(List<PosterItemBean.DesignMetaBean> beans) {
        for (PosterItemBean.DesignMetaBean bean : beans) {
            switch (bean.getDataType()) {
                case TYPE_TEXT:
                    //不可编辑文字
                    setText(bean.getFieldValue(), 14, "#FFFFFF", bean.getLocation());
                    break;
//                case Type_inPut:
//                    //输入文本
//                    setInput(bean.content, bean.fontSize, bean.fontColor, bean.ret);
//                    break;
                case TYPE_IMAGE:
                    //图片
                    if (bean.getFieldCode().equals(FileCode_Code)) {
                        Log.d(TAG, "setViewData: 我就是二维码"+ApiConstants.UZAOCHINA_IMAGE_HOST + bean.getFieldValue());
                        setCode(ApiConstants.UZAOCHINA_IMAGE_HOST + bean.getFieldValue(), bean.getLocation());
                    } else {
                        setImage(ApiConstants.UZAOCHINA_IMAGE_HOST + bean.getFieldValue(), bean.getLocation());
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.rl_root)
    public void onRootClick() {
        KeyboardUtils.hideSoftInput(this);
        cancelCursor();
    }

    /**
     * 关闭里面的输入光标
     */
    private void cancelCursor() {
        int childCount = rlPost.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = rlPost.getChildAt(i);
            if (childAt instanceof EditText) {
                ((EditText) childAt).setCursorVisible(false);
            }
        }
    }


    @Override
    public void setText(String content, int fontSize, String fontColor, PosterItemBean.DesignMetaBean.LocationBean ret) {
        TextView textView = new TextView(this);
        textView.setText(content);
        textView.setTextColor(Color.parseColor(fontColor));
        textView.setTextSize(fontSize);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (ret.getWidth() * scale), (int) (ret.getHeight() * scale));
        params.setMargins((int) (ret.getLeft() * scale), (int) (ret.getTop() * scale), 0, 0);
        rlPost.addView(textView, params);
    }

    @Override
    public void setCode(String content, final PosterItemBean.DesignMetaBean.LocationBean ret) {
        Log.d(TAG, "setCode: " + content);
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (ret.getWidth() * scale), (int) (ret.getHeight() * scale));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (ret.getWidth() * scale), (int) (ret.getWidth() * scale));
        params.setMargins((int) (ret.getLeft() * scale), (int) (ret.getTop() * scale), 0, 0);
        rlPost.addView(imageView, params);
        Observable.just(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new io.reactivex.functions.Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(String s) throws Exception {
                        return QRCodeEncoder.syncEncodeQRCode(s, (int) (ret.getWidth() * scale));
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.functions.Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        imageView.setImageBitmap(bitmap);
                    }
                });
    }

    /**
     * 增加一个输入文本 控件
     *
     * @param content   文本内容
     * @param fontSize  文本字号
     * @param fontColor 文本颜色
     * @param ret       文本区域
     */
    @Override
    public void setInput(String content, int fontSize, String fontColor, PosterItemBean.DesignMetaBean.LocationBean ret) {
        final EditText textView = new EditText(this);
        textView.setText(content);
        textView.setSelection(content.length());
        textView.setGravity(Gravity.TOP | Gravity.LEFT);
        int pix = (int) PixelUtil.dp2px(5);
        textView.setPadding(pix, pix, pix, pix);
        textView.setTextColor(Color.parseColor(fontColor));
        textView.setTextSize(fontSize);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setCursorVisible(true);
            }
        });
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_poster_input));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (ret.getWidth() * scale), (int) (ret.getHeight() * scale));
        params.setMargins((int) (ret.getLeft() * scale), (int) (ret.getTop() * scale), 0, 0);
        params.height = (int) (ret.getHeight() * scale);
        rlPost.addView(textView, params);
    }

    /**
     * 设置图片控件
     *
     * @param content 图片url
     * @param ret     图片区域
     */
    @Override
    public void setImage(String content, PosterItemBean.DesignMetaBean.LocationBean ret) {
        Log.d(TAG, "setImage: " + content);
        final ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (ret.getWidth() * scale), (int) (ret.getHeight() * scale));
        params.setMargins((int) (ret.getLeft() * scale), (int) (ret.getTop() * scale), 0, 0);
        rlPost.addView(imageView, params);
        GlideLoadImageUtil.load(this, content, imageView);
    }


    //    保存文件的方法：
    public Bitmap saveBitmapFromView() {
//        ViewGroup.LayoutParams layoutParams = rl_shout.getLayoutParams();
//        Bitmap viewShot = ScreenShotUtils.getViewShot(rl_shout);
//        rl_shout.setLayoutParams(layoutParams);
//        return viewShot;
        View view = rlRoot;
        int margin = (int) PixelUtil.dp2px(15);
//        int top = rl_shout.getTop() - margin;
        int top = rl_shout.getTop();
        int bottom = rl_shout.getHeight() + margin;
        int w = view.getWidth();
        int h = view.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmp);
//        view.layout(0, 0, w, h);
        view.draw(c);
        // 缩小图片
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        bmp = Bitmap.createBitmap(bmp, 0, top, bmp.getWidth(), bottom, matrix, true);
        return bmp;
    }

    /*
     * 保存文件，文件名为当前日期
     */
    public void saveBitmap(Bitmap bitmap, String bitName) {
        String fileName;
        File file;
        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
// 插入图库
                MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送广播，通知刷新图库的显示
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
        ToastUtil.showShort("保存成功");
    }

}
