package com.zhaotai.uzao.ui.person.setting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.EventBean;
import com.zhaotai.uzao.ui.person.setting.contract.ChangeIntroduceConstract;
import com.zhaotai.uzao.ui.person.setting.presenter.ChangeIntroducePresenter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * description: 修改个人信息页面activity
 * author : ZP
 * date: 2018/1/22 0022.
 */

public class ChangeIntroduceActivity extends BaseActivity implements ChangeIntroduceConstract.View {
    private static final String AboutMe = "aboutMe";
    @BindView(R.id.ed_introduce)
    public EditText edIntroduce;


    @BindView(R.id.right_btn)
    public Button btnRight;
    private ChangeIntroducePresenter mPresenter;
    private String introduce;

    public static void launch(Context context, String aboutMe) {
        Intent intent = new Intent(context, ChangeIntroduceActivity.class);
        intent.putExtra(AboutMe, aboutMe);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_introduce);
        mTitle.setText(getString(R.string.chang_info));
        btnRight.setText(R.string.save);
        btnRight.setVisibility(View.VISIBLE);
        mPresenter = new ChangeIntroducePresenter(this, this);

    }

    @Override
    protected void initData() {
        String word = getIntent().getStringExtra(AboutMe);
        edIntroduce.setText(word);
        edIntroduce.setSelection(word.length());
    }


    @Override
    public boolean hasTitle() {
        return true;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.right_btn)
    public void onSave() {
        introduce = edIntroduce.getText().toString();
        mPresenter.setIntroduce(introduce);
    }

    @Override
    public void showChangeIntroduceSuccess(String aboutMe) {
        finish();
        EventBus.getDefault().post(new EventBean<>(introduce, EventBusEvent.CHOOSE_INTRODUCE));
    }
}
