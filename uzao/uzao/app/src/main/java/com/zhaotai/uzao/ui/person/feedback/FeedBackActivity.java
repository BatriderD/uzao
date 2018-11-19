package com.zhaotai.uzao.ui.person.feedback;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.RequestFeedBackBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/7/19
 * Created by LiYou
 * Description : 意见反馈
 */

public class FeedBackActivity extends BaseActivity implements FeedBackContract.View {

    @BindView(R.id.tv_toolbar_right_btn)
    TextView mRightBtn;
    //标题
    @BindView(R.id.et_feedback_title)
    EditText mEtTitle;
    //反馈内容
    @BindView(R.id.et_feedback_content)
    EditText mContent;
    //电话
    @BindView(R.id.et_feedback_phone)
    EditText mPhone;
    //邮箱
    @BindView(R.id.et_feedback_email)
    EditText mEmail;

    private RequestFeedBackBean feedBackBean;
    private FeedBackPresenter mPresenter;

    public static void Launch(Context context) {
        context.startActivity(new Intent(context, FeedBackActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_feed_back);
        mTitle.setText("意见反馈");
        mRightBtn.setText("提交");
        mRightBtn.setVisibility(View.VISIBLE);
        mPresenter = new FeedBackPresenter(this, this);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    /**
     * 提交按钮点击事件
     */
    @OnClick(R.id.tv_toolbar_right_btn)
    public void postFeedBack() {
        String title = mEtTitle.getText().toString();
        String content = mContent.getText().toString();
        String contact = mPhone.getText().toString();
        String email = mEmail.getText().toString();
        mPresenter.checkAndPostFeedBack(title,content,contact,email);
    }


    @Override
    public void showSuccess() {
        finish();
    }
}
