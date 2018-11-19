package com.zhaotai.uzao.ui.person.setting;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.ACache;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangeNickNameActivity extends BaseActivity {

    @BindView(R.id.edit_nick_name)
    EditText mNickName;
    private String nickName;

    public static void launch(Context context, String nickName) {
        Intent intent = new Intent(context, ChangeNickNameActivity.class);
        intent.putExtra("nickName", nickName);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_nick_name);
        mTitle.setText("填写昵称");
        nickName = getIntent().getStringExtra("nickName");
        mNickName.setText(nickName);
        mNickName.setSelection(nickName.length());
        StringUtil.setEtFilterAndLength(mNickName, 8);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.save_nick_name)
    public void saveNickName() {
        if (StringUtil.isTrimEmpty(mNickName.getText().toString())) {
            ToastUtil.showShort("请输入昵称");
            return;
        }
        //检查是否被占用
        if (nickName.equals(mNickName.getText().toString())) {
            ToastUtil.showShort("保存成功");
            finish();
        } else {
            availableName();
        }
    }

    /**
     * 检查名字是否被占用
     */
    private void availableName() {
        Api.getDefault().availableNickName(mNickName.getText().toString())
                .compose(RxHandleResult.<Boolean>handleResult())
                .subscribe(new RxSubscriber<Boolean>(this, false) {
                    @Override
                    public void _onNext(Boolean personBeanBaseResult) {

                        if (personBeanBaseResult) {
                            changeNickName();
                        } else {
                            ToastUtil.showShort("昵称被占用!");
                        }

                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("修改失败");
                    }
                });
    }

    /**
     * 修改名字
     */
    private void changeNickName() {
        final PersonInfo info = new PersonInfo();
        info.code = EventBusEvent.CHANGE_NICK_NAME;
        info.nickName = mNickName.getText().toString();

        //修改昵称
        Api.getDefault().changePersonInfo(info)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(this, true) {
                    @Override
                    public void _onNext(PersonBean personBean) {

                        //成功
                        ACache.get(mContext).put(GlobalVariable.PERSONINFO, personBean);
                        ToastUtil.showShort("修改成功");
                        EventBus.getDefault().post(info);
                        finish();
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("修改失败");
                    }
                });
    }
}
