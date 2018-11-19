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
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/7/18
 * Created by LiYou
 * Description : 修改 职业
 */

public class ChangeProfessionActivity extends BaseActivity {

    @BindView(R.id.et_profession_edit)
    EditText mProfession;

    public static void launch(Context context, String nickName) {
        Intent intent = new Intent(context, ChangeProfessionActivity.class);
        intent.putExtra("profession", nickName);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_profession);
        mTitle.setText("职业");
        String profession = getIntent().getStringExtra("profession");
        if (!"请填写".equals(profession)) {
            mProfession.setText(profession);
            mProfession.setSelection(profession.length());
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @OnClick(R.id.btn_profession_save)
    public void saveProfession() {
        final PersonInfo info = new PersonInfo();
        info.code = EventBusEvent.CHANGE_PROFESSION;
        info.profession = mProfession.getText().toString();

        //修改昵称
        Api.getDefault().changePersonInfo(info)
                .compose(RxHandleResult.<PersonBean>handleResult())
                .subscribe(new RxSubscriber<PersonBean>(this, true) {
                    @Override
                    public void _onNext(PersonBean personBean) {
//                        修改个人信息缓存
                        ACache.get(mContext).put(GlobalVariable.PERSONINFO, personBean);
                        ToastUtil.showShort("修改成功");
                        info.city = personBean.cityName;
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
