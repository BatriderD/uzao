package com.zhaotai.uzao.ui.post.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.SceneManagerPostBean;
import com.zhaotai.uzao.richeditor.RichEditor;
import com.zhaotai.uzao.ui.post.contract.PublishPostContract;
import com.zhaotai.uzao.ui.post.fragment.InsertLinkDialogFragment;
import com.zhaotai.uzao.ui.post.presenter.PublishPostPresenter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/7/27 0027
 * Created by LiYou
 * Description : 发布帖子
 */
public class PublishPostActivity extends BaseActivity implements PublishPostContract.View, InsertLinkDialogFragment.Callback {

    private static final int REQUEST_CODE_CHOOSE = 233;

    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.tv_post_title)
    EditText mEtTitle;

    @BindView(R.id.right_btn)
    Button mRightBtn;

    private PublishPostPresenter mPresenter;
    private InsertLinkDialogFragment mDialogFragment;

    //发布新帖
    private static final String EXTRA_KEY_TYPE_NEW_POST = "extra_key_type_new_post";
    //编辑帖子
    private static final String EXTRA_KEY_TYPE_EDIT_POST = "extra_key_type_edit_post";
    private SceneManagerPostBean themeBean;//编辑帖子数据

    /**
     * 发布新帖
     *
     * @param context 上下文
     * @param themeId 场景Id
     */
    public static void launch(Activity context, String themeId) {
        Intent intent = new Intent(context, PublishPostActivity.class);
        intent.putExtra("type", EXTRA_KEY_TYPE_NEW_POST);
        intent.putExtra("themeId", themeId);
        context.startActivityForResult(intent, 1);
    }

    /**
     * 编辑帖子
     *
     * @param context 上下文
     * @param item    数据
     */
    public static void launch(Activity context, SceneManagerPostBean item) {
        Intent intent = new Intent(context, PublishPostActivity.class);
        intent.putExtra("type", EXTRA_KEY_TYPE_EDIT_POST);
        intent.putExtra("themeBean", item);
        context.startActivityForResult(intent, 1);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_publish_post);
        mTitle.setText("发布帖子");
        mEditor.setEditorFontSize(14);
        mEditor.setPadding(12, 10, 12, 0);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.focusEditor();
        mDialogFragment = InsertLinkDialogFragment.newInstance();
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("发帖");
        switch (getIntent().getStringExtra("type")) {
            case EXTRA_KEY_TYPE_NEW_POST:
                break;
            case EXTRA_KEY_TYPE_EDIT_POST:
                themeBean = (SceneManagerPostBean) getIntent().getSerializableExtra("themeBean");
                mEtTitle.setText(themeBean.getTitle());
                mEditor.setHtml(themeBean.getContentBody());
                break;
        }
    }

    @Override
    protected void initData() {
        if (mPresenter == null) {
            mPresenter = new PublishPostPresenter(this, this);
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.right_btn)
    public void onClickPost() {
        switch (getIntent().getStringExtra("type")) {
            case EXTRA_KEY_TYPE_NEW_POST:
                mPresenter.post(mEtTitle.getText().toString(), mEditor.getHtml(), getIntent().getStringExtra("themeId"));
                break;
            case EXTRA_KEY_TYPE_EDIT_POST:
                mPresenter.modifyPost(mEtTitle.getText().toString(), mEditor.getHtml(), themeBean.getThemeId(), themeBean.get_id());
                break;
        }
    }

    /**
     * 撤销
     */
    @OnClick(R.id.action_undo)
    public void actionUndo() {
        mEditor.undo();
    }

    /**
     * 回退
     */
    @OnClick(R.id.action_redo)
    public void actionRedo() {
        mEditor.redo();
    }

    /**
     * 加粗
     */
    @OnClick(R.id.action_bold)
    public void actionBold() {
        mEditor.setBold();
    }

    /**
     * 斜体
     */
    @OnClick(R.id.action_italic)
    public void actionItalic() {
        mEditor.setItalic();
    }

    /**
     * 下划线
     */
    @OnClick(R.id.action_underline)
    public void actionUnderLine() {
        mEditor.setUnderline();
    }

    /**
     * 居左
     */
    @OnClick(R.id.action_align_left)
    public void actionAlignLeft() {
        mEditor.setAlignLeft();
    }

    /**
     * 居中
     */
    @OnClick(R.id.action_align_center)
    public void actionAlignCenter() {
        mEditor.setAlignCenter();
    }

    /**
     * 局右
     */
    @OnClick(R.id.action_align_right)
    public void actionAlignRight() {
        mEditor.setAlignRight();
    }

    /**
     * H1
     */
    @OnClick(R.id.action_heading1)
    public void actionH1() {
        mEditor.setHeading(1);
    }

    /**
     * H2
     */
    @OnClick(R.id.action_heading2)
    public void actionH2() {
        mEditor.setHeading(2);
    }

    /**
     * H3
     */
    @OnClick(R.id.action_heading3)
    public void actionH3() {
        mEditor.setHeading(3);
    }

    /**
     * H4
     */
    @OnClick(R.id.action_heading4)
    public void actionH4() {
        mEditor.setHeading(4);
    }

    /**
     * H5
     */
    @OnClick(R.id.action_heading5)
    public void actionH5() {
        mEditor.setHeading(5);
    }

    /**
     * H6
     */
    @OnClick(R.id.action_heading6)
    public void actionH6() {
        mEditor.setHeading(6);
    }

    /**
     * action_insert_bullets
     */
    @OnClick(R.id.action_insert_bullets)
    public void actionInsertBullets() {
        mEditor.setBullets();
    }

    /**
     * action_insert_numbers
     */
    @OnClick(R.id.action_insert_numbers)
    public void actionInsertNumbers() {
        mEditor.setNumbers();
    }

    /**
     * 添加链接
     */
    @OnClick(R.id.action_insert_link)
    public void actionInsertLink() {

        if (mDialogFragment != null)
            mDialogFragment.show(getFragmentManager());
    }

    /**
     * 添加图片
     */
    @OnClick(R.id.action_insert_image)
    public void actionInsertImage() {
        Matisse.from(PublishPostActivity.this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.zhaotai.uzao.fileprovider"))
                .countable(true)
                .maxSelectable(4)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收图片
        if (resultCode == RESULT_OK) {
            mPresenter.compressImage(Matisse.obtainPathResult(data));
        }
    }


    /**
     * 插入图片
     *
     * @param imagePath 图片地址
     */
    @Override
    public void insertImage(String imagePath) {
        mEditor.insertImage(imagePath, "image");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void stopProgress() {

    }

    /**
     * 插入超链接回调
     *
     * @param linkAddress 超链接地址
     * @param linkName    超链接名字
     */
    @Override
    public void onClick(String linkAddress, String linkName) {
        mEditor.insertLink(linkAddress, linkName);
    }

    @Override
    public void finishView(SceneManagerPostBean postBean) {
        Intent intent = new Intent();
        intent.putExtra("themeBean", postBean);
        setResult(98, intent);
        finish();
    }
}
