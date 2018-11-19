package com.zhaotai.uzao.ui.person.address;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.EventBean.ModifyAddressInfo;
import com.zhaotai.uzao.bean.RegionBean;
import com.zhaotai.uzao.ui.person.address.contract.ModifyAddressContract;
import com.zhaotai.uzao.ui.person.address.presenter.ModifyAddressPresenter;
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
 * description: 修改地址页面
 * author: LiYou
 */
public class ModifyAddressActivity extends BaseFragmentActivity implements ModifyAddressContract.View {

    private static final String MODIFYDATA = "modifyData";
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

    @BindView(R.id.tv_default)
    TextView tvDefault;

    //保存
    @BindView(R.id.right_btn)
    Button mRightBtn;

    private AddressBean modifyData;
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String regionCode;
    private String regionName;
    private String region;
    private ModifyAddressPresenter modifyAddressPresenter;

    public static void launch(Context context, AddressBean addressBean, int position) {
        Intent intent = new Intent(context, ModifyAddressActivity.class);
        intent.putExtra("modifyData", addressBean);
        intent.putExtra("modifyPosition", position);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_modify_address);
        //设置标题
        mTitle.setText("编辑地址");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setText("保存");
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        modifyAddressPresenter = new ModifyAddressPresenter(this, this);

        modifyData = (AddressBean) getIntent().getSerializableExtra(MODIFYDATA);
        name.setText(modifyData.recieverName);
        phone.setText(modifyData.recieverPhone);
        region = modifyData.extend1 + " " + modifyData.extend2 + " " + modifyData.extend3;
        city.setText(region);
        detail.setText(modifyData.addrDetail);
        tvDefault.setSelected("Y".equals(modifyData.isDefault));
    }

    /**
     * 完成修改地址
     */
    @OnClick(R.id.right_btn)
    public void modifyAddress() {
        if (verifyText()) {
            modifyData.addrAlias = region + " " + detail.getText().toString();
            modifyData.region = regionCode;
            modifyData.regionName = regionName;
            modifyData.recieverPhone = phone.getText().toString();
            modifyData.recieverName = name.getText().toString();
            modifyData.addrDetail = detail.getText().toString();
            modifyData.province = provinceCode;
            modifyData.provinceName = provinceName;
            modifyData.city = cityCode;
            modifyData.cityName = cityName;
            modifyData.extend1 = provinceName;
            modifyData.extend2 = cityName;
            modifyData.extend3 = regionName;
            if (tvDefault.isSelected()) {
                modifyData.isDefault = "Y";
            } else {
                modifyData.isDefault = "N";
            }
            modifyAddressPresenter.upDataAddress(modifyData);
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
        return true;
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
     * 接收选择省市区数据
     *
     * @param list 地区数据
     */


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
            region = regionNameT;
            city.setText(regionNameT);
        }
    }


    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void upDataSuccess() {
        ToastUtil.showShort(getString(R.string.updata_address_success));
        ModifyAddressInfo<AddressBean> info = new ModifyAddressInfo<>();
        info.data = modifyData;
        info.position = getIntent().getIntExtra("modifyPosition", 0);
        EventBus.getDefault().post(info);
        finish();
    }

    @Override
    public void upDataError(String msg) {
        ToastUtil.showShort(getString(R.string.updata_address_error));
    }
}
