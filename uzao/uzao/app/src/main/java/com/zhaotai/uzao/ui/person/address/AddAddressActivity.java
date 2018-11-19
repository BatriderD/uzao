package com.zhaotai.uzao.ui.person.address;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.EventBean.ModifyAddressInfo;
import com.zhaotai.uzao.bean.RegionBean;
import com.zhaotai.uzao.ui.person.address.contract.AddAddressContract;
import com.zhaotai.uzao.ui.person.address.presenter.AddAddressPresenter;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * time:2017/4/17
 * description: 添加新地址
 * author: LiYou
 */

public class AddAddressActivity extends BaseActivity implements AddAddressContract.View {

    //收货人
    @BindView(R.id.et_address_modify_name)
    EditText name;
    //手机号码
    @BindView(R.id.et_address_modify_phone)
    EditText phone;
    //所在地区
    @BindView(R.id.modify_address_city)
    TextView city;
    //详细地址
    @BindView(R.id.modify_address_detail)
    EditText detail;

//    //默认地址的布局
    @BindView(R.id.tv_default)
    TextView tvDefault;
    //保存
    @BindView(R.id.right_btn)
    Button mRightBtn;


    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String regionCode;
    private String regionName;
    private AddAddressPresenter addAddressPresenter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AddAddressActivity.class));
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_modify_address);
        //设置标题
        mTitle.setText("新增地址");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("保存");

    }

    @Override
    public void initData() {
        //注册EventBus
        EventBus.getDefault().register(this);
        addAddressPresenter = new AddAddressPresenter(this, this);

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }


    /**
     * 地区选择
     */
    @OnClick(R.id.modify_address_ll_city)
    public void goToCitySelect() {
        RegionSelectActivity.launch(this);
    }


    /**
     * 修改默认地址
     */
    @OnClick(R.id.tv_default)
    public void checkDefault() {
        tvDefault.setSelected(!tvDefault.isSelected());
    }



    /**
     * 完成按钮
     */
    @OnClick(R.id.right_btn)
    public void putAddress() {
        if (verifyText()) {
            AddressBean addressBean = new AddressBean();
            addressBean.region = regionCode;
            addressBean.regionName = regionName;
            addressBean.recieverPhone = phone.getText().toString();
            addressBean.recieverName = name.getText().toString();
            addressBean.addrDetail = detail.getText().toString();
            addressBean.province = provinceCode;
            addressBean.provinceName = provinceName;
            addressBean.city = cityCode;
            addressBean.cityName = cityName;
            if (tvDefault.isSelected()) {
                addressBean.isDefault = "Y";
            } else {
                addressBean.isDefault = "N";
            }
            addAddressPresenter.uploadAddress(addressBean);
        }
    }

    /**
     * 检验数据
     */
    private boolean verifyText() {
        if (StringUtil.isTrimEmpty(name.getText().toString())) {
            ToastUtil.showShort("姓名不能为空");
            return false;
        }
        if (!StringUtil.checkPhoneNumber(phone.getText().toString())) {
            ToastUtil.showShort("手机号码格式不对");
            return false;
        }
        if (StringUtil.isTrimEmpty(detail.getText().toString())) {
            ToastUtil.showShort("请输入详细地址");
            return false;
        }
        if (StringUtil.isTrimEmpty(city.getText().toString())) {
            ToastUtil.showShort("请选择地区");
            return false;
        }
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ArrayList<RegionBean> list) {
        if (list.size() > 0) {
            String regionNameT = "";
            for (RegionBean data : list) {
                regionNameT += data.locationName + " ";
            }
            if (list.size() > 2) {
                provinceName = list.get(0).locationName;
                provinceCode = list.get(0).locationCode;
                cityName = list.get(1).locationName;
                cityCode = list.get(1).locationCode;
                regionName = list.get(2).locationName;
                regionCode = list.get(2).locationCode;
            }

            city.setText(regionNameT);
        }
    }


    /**
     * 上传地址成功
     *
     * @param addressBean 地址实体类
     */
    @Override
    public void uploadAddressSuccess(AddressBean addressBean) {
        ToastUtil.showShort(getString(R.string.add_address_success));
        ModifyAddressInfo<AddressBean> info = new ModifyAddressInfo<>();
        info.data = addressBean;
        info.position = -1;
        EventBus.getDefault().post(info);
        finish();
    }

    /**
     * 上传地址失败
     *
     * @param message 返回错误信息
     */
    @Override
    public void uploadAddressError(String message) {
        ToastUtil.showShort(getString(R.string.add_address_failed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
