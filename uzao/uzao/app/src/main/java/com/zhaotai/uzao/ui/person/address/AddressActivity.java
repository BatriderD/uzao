package com.zhaotai.uzao.ui.person.address;

import android.app.AlertDialog;
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
import com.zhaotai.uzao.widget.MultipleStatusView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Time: 2017/5/11
 * Created by LiYou
 * Description : 收货地址
 */
public class AddressActivity extends BaseActivity implements AddressContract.View, BaseQuickAdapter.OnItemChildClickListener ,CancelAdapt{

    @BindView(R.id.address_recycler)
    RecyclerView mRecyclerView;

    private AddressPresenter mPresenter;

    private AddressListAdapter addressAdapter;
    private List<AddressBean> datas;

    private static final String EXTRA_KEY_ADDRESS_TYPE = "extra_key_address_type";
    private static final String EXTRA_KEY_ADDRESS_POSITION = "extra_key_address_position";
    public static final String BATCH_ADDRESS = "batch_address";//用于 分批地址

    public static void launch(Context context) {
        context.startActivity(new Intent(context, AddressActivity.class));
    }

    public static void launch(Context context, String addressType, int position) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra(EXTRA_KEY_ADDRESS_TYPE, addressType);
        intent.putExtra(EXTRA_KEY_ADDRESS_POSITION, position);
        context.startActivity(intent);
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_address);
        //设置标题
        mTitle.setText(getString(R.string.manage_address));
        // 初始化Recycleview 并初始设置
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        addressAdapter = new AddressListAdapter();
        mRecyclerView.setAdapter(addressAdapter);
        addressAdapter.setOnItemChildClickListener(this);
        addressAdapter.setEmptyStateView(mContext, R.mipmap.ic_status_empty_address, "我们要给您送到哪里呢");
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        if (mPresenter == null)
            mPresenter = new AddressPresenter(this, this);
        //获取地址列表
        mPresenter.getAddressList();
    }


    /**
     * 进入新增地址页面
     */
    @OnClick(R.id.btn_address_addaddress)
    public void addAddress() {
        startActivity(new Intent(this, AddAddressActivity.class));
    }

    @Override
    public void showAddressList(List<AddressBean> list) {
        datas = list;
        addressAdapter.addData(datas);
    }


    /**
     * 删除成功 从界面删除指定item
     *
     * @param pos 位置
     */
    @Override
    public void delItemSuccess(int pos) {
        addressAdapter.remove(pos);
    }


    /**
     * 设置本条为默认地址
     *
     * @param pos 位置
     */
    @Override
    public void setItemDefault(int pos) {
        datas = addressAdapter.getData();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ModifyAddressInfo event) {
        AddressBean addressBean = (AddressBean) event.data;
        List<AddressBean> data = addressAdapter.getData();
        int index = 0;
        if (data.size() > 0) {
            //判断是否需求更换默认地址
            if (addressBean.isDefault.equals("Y")) {
                for (AddressBean info : datas) {
                    info.isDefault = "N";
                }
            } else {
                index = data.size();
            }
        }

        if (event.position == -1) {
            //代表是新增的
            data.add(0, addressBean);
            event.position = index;
        } else {
            data.set(event.position, addressBean);
        }
        if (multipleStatusView.getViewStatus() != MultipleStatusView.STATUS_CONTENT) {
            showContent();
        }
        //addressAdapter.notifyItemChanged(event.position);
        addressAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.onDestory();
    }

    /**
     * recyclerView 子view点击事件处理
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        datas = addressAdapter.getData();
        AddressBean item = (AddressBean) adapter.getItem(position);
        switch (view.getId()) {
            case R.id.address_delete:
                //删除默认修改
                showDeleteItemDialog(item.sequenceNBR, position);
                break;
            case R.id.ll_is_default:
                //设为默认
                mPresenter.setItemDefault(item, position);
                break;
            case R.id.tv_address_address_modify:
                //修改地址 进入修改页面
                ModifyAddressActivity.launch(this, item, position);
                break;
            case R.id.address_ll:
                if (BATCH_ADDRESS.equals(getIntent().getStringExtra(EXTRA_KEY_ADDRESS_TYPE))) {
                    //分批地址
                    AddressBean address = datas.get(position);
                    address.position = getIntent().getIntExtra(EXTRA_KEY_ADDRESS_POSITION, 0);
                    EventBus.getDefault().post(address);
                    finish();
                }
                break;
        }
    }

    //    显示删除dialog
    public void showDeleteItemDialog(final String id, final int position) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(R.string.sure_to_del_address);
        alertDialog.setPositiveButton(R.string.del, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.delAddressItem(id, position);

            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }
}
