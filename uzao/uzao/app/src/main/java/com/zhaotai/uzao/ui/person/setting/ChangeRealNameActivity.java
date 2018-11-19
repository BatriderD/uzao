package com.zhaotai.uzao.ui.person.setting;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Time: 2017/5/15
 * Created by LiYou
 * Description : 修改真实名字
 */

public class ChangeRealNameActivity extends BaseActivity {

    @BindView(R.id.edit_real_name)
    EditText mRealName;


    public static void launch(Context context,String realName) {
        Intent intent = new Intent(context,ChangeRealNameActivity.class);
        intent.putExtra("realName",realName);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_real_name);
        mTitle.setText("填写真实姓名");
        String realName = getIntent().getStringExtra("realName");
        if (!"请填写".equals(realName)) {
            mRealName.setText(realName);
            mRealName.setSelection(realName.length());
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    @OnClick(R.id.save_real_name)
    public void saveRealName() {
        if(StringUtil.isTrimEmpty(mRealName.getText().toString())){
            ToastUtil.showShort("请输入姓名");
            return;
        }
        final PersonInfo info = new PersonInfo();
        info.code = EventBusEvent.CHANGE_REAL_NAME;
        info.realName = mRealName.getText().toString();

        Api.getDefault().changePersonInfo(info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseResult<PersonBean>>(this,true) {
                    @Override
                    public void _onNext(BaseResult<PersonBean> personBeanBaseResult) {
                        if(personBeanBaseResult.getStatus() == 200){
                            //成功
                            ToastUtil.showShort("修改成功");
                            EventBus.getDefault().post(info);
                            finish();
                        }else {
                            ToastUtil.showShort("修改失败");
                        }
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("修改失败");
                    }
                });

    }
}
