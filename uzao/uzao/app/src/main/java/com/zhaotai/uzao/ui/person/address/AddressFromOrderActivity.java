package com.zhaotai.uzao.ui.person.address;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.AddressBean;
import com.zhaotai.uzao.bean.EventBean.ModifyAddressInfo;
import com.zhaotai.uzao.ui.person.address.adapter.AddressListAdapter;
import com.zhaotai.uzao.ui.person.address.contract.AddressContract;
import com.zhaotai.uzao.ui.person.address.presenter.AddressPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/11
 * Created by LiYou
 * Description : 收货地址
 */
public class AddressFromOrderActivity extends BaseActivity implements AddressContract.View, BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.address_recycler)
    RecyclerView mRecyclerView;

    private AddressPresenter mPresenter;

    private AddressListAdapter addressAdapter;
    private List<AddressBean> datas;

    public static void launch(Activity context, int position) {
        Intent intent = new Intent(context, AddressFromOrderActivity.class);
        intent.putExtra("changePosition", position);
        context.startActivityForResult(intent,1);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_address);

        //设置标题
        mTitle.setText(R.string.manage_address);
        //初始化设置recycleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPresenter = new AddressPresenter(this, this);
        addressAdapter = new AddressListAdapter();
        mRecyclerView.setAdapter(addressAdapter);
        addressAdapter.setOnItemChildClickListener(this);

        EventBus.getDefault().register(this);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(6);
                finish();
            }
        });
    }

    @Override
    protected void initData() {

        //获取地址列表
        mPresenter.getAddressList();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    /**
     * 添加地址
     */
    @OnClick(R.id.btn_address_addaddress)
    public void addAddress() {
        Intent intent = new Intent(this, AddAddressActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            //更新列表
            AddressBean addressBean = (AddressBean) data.getExtras().get("addaddress");
            if (addressBean != null) {
                //判断是否默认地址
                if (addressBean.isDefault.equals("Y")) {
                    for (AddressBean info : datas) {
                        info.isDefault = "N";
                    }
                    //添加到第一个位置
                    datas.add(0, addressBean);
                } else {
                    datas.add(addressBean);
                }
                addressAdapter.setNewData(datas);
            }
        }
    }


    @Override
    public void showAddressList(List<AddressBean> list) {
        datas = list;
        addressAdapter.addData(datas);
    }

    /**
     * 删除指定条目
     *
     * @param pos
     */
    @Override
    public void delItemSuccess(int pos) {
        addressAdapter.remove(pos);
    }

    /**
     * 设置本条为默认地址
     *
     * @param pos
     */
    @Override
    public void setItemDefault(int pos) {
        //修改数据中 默认属性
        for (int i = 0; i < datas.size(); i++) {
            AddressBean info = datas.get(i);
            if (info.isDefault.equals("Y")) {
                info.isDefault = "N";
            }
        }
        datas.get(pos).isDefault = "Y";
        addressAdapter.notifyDataSetChanged();
        ToastUtil.showShort("设置默认地址成功");
    }

    /**
     * 提示删除dialog
     */
    public void showDeleteItemDialog(final String id, final int postition) {
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setMessage(R.string.sure_to_del_address);
        alertDialog.setPositiveButton(R.string.del, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.delAddressItem(id, postition);

            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ModifyAddressInfo event) {
        AddressBean addressBean = (AddressBean) event.data;
        //判断是否需求更换默认地址
        if (addressBean.isDefault.equals("Y")) {
            for (AddressBean info : datas) {
                info.isDefault = "N";
            }
        }
        datas.set(event.position, addressBean);
        addressAdapter.setNewData(datas);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        datas = adapter.getData();
        AddressBean item = (AddressBean) adapter.getItem(position);
        switch (view.getId()) {
            //删除
            case R.id.address_delete:
                showDeleteItemDialog(item.sequenceNBR, position);
                break;
            //编辑
            case R.id.tv_address_address_modify:
                ModifyAddressActivity.launch(mContext, datas.get(position), position);
                break;
            //切换默认地址
            case R.id.ll_is_default:
                //设为默认地址
                mPresenter.setItemDefault(item, position);
                break;
            //返回地址
            case R.id.address_ll:
                AddressBean addressBean = datas.get(position);
                addressBean.position = getIntent().getIntExtra("changePosition", 0);
                EventBus.getDefault().post(addressBean);
                finish();
                break;
        }
    }


}
