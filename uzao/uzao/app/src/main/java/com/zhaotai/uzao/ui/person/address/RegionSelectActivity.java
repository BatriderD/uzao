package com.zhaotai.uzao.ui.person.address;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.RegionBean;
import com.zhaotai.uzao.ui.person.address.adapter.RegionAdapter;
import com.zhaotai.uzao.ui.person.address.contract.ReginSelectContract;
import com.zhaotai.uzao.ui.person.address.presenter.ReginSelectPresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Time: 2017/5/11
 * Created by LiYou
 * Description :  地区选择类
 */

public class RegionSelectActivity extends BaseFragmentActivity implements ReginSelectContract.View {

    @BindView(R.id.rc_address_cityselect)
    RecyclerView mRecyclerView;

    private final static int PROVINCE_LV = 0;
    private final static int CITY_LV = 1;
    private final static int REGION_LV = 2;

    private RegionAdapter mAdapter;


    //省份数据集合
    private List<RegionBean> dataProvinceList = new ArrayList<>();
    //城市数据集合
    private List<RegionBean> dataCityList = new ArrayList<>();
    //县区数据集合
    private List<RegionBean> dataRegionList = new ArrayList<>();

    private RegionBean provinceData = new RegionBean();
    private RegionBean cityData = new RegionBean();
    private RegionBean regionData = new RegionBean();

    private ReginSelectPresenter reginSelectPresenter;

    private int currentLv = PROVINCE_LV;

    public static void launch(Activity context) {
        context.startActivity(new Intent(context, RegionSelectActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_region_select);
        mTitle.setText(R.string.selected_address);
        //初始化recycle
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new RegionAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goBack();
            }

        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isFastDoubleClick()) {
                    ToastUtil.showShort("您点的太快了");
                    return;
                }
                if (adapter.getData().isEmpty()) {
                    return;
                }
                if (currentLv == REGION_LV) {
                    if (dataRegionList != null && dataRegionList.size() >= position) {
                        regionData = dataRegionList.get(position);
                        //返回上一页
                        ArrayList<RegionBean> list = new ArrayList<>();
                        list.add(0, provinceData);
                        list.add(1, cityData);
                        list.add(2, regionData);
                        EventBus.getDefault().post(list);
                        finish();
                    }
                } else if (currentLv == CITY_LV) {
                    //获取县区页面
                    if (dataCityList != null && dataCityList.size() >= position) {
                        cityData = dataCityList.get(position);
                        reginSelectPresenter.getRegion(cityData.locationCode);
                        //                    切换到下一级
                        currentLv = REGION_LV;
                    }
                } else {
                    //获取城市页面
                    if (dataProvinceList != null && dataProvinceList.size() >= position) {
                        provinceData = dataProvinceList.get(position);
                        reginSelectPresenter.getRegion(provinceData.locationCode);
//                    切换到下一级
                        currentLv = CITY_LV;
                    }

                }

            }
        });
    }

    private long lastClickTime;

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 400) {       //500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    //返回
    private void goBack() {
        switch (currentLv) {
            case PROVINCE_LV:
                finish();
                break;
            case CITY_LV:
                mAdapter.setNewData(dataProvinceList);
//                        回到上一级
                currentLv = PROVINCE_LV;
                break;
            case REGION_LV:
                mAdapter.setNewData(dataCityList);
                //                        回到上一级
                currentLv = CITY_LV;
                break;

        }
    }

    @Override
    protected void initData() {

        reginSelectPresenter = new ReginSelectPresenter(this, this);
        reginSelectPresenter.getProvinces();
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    /**
     * 显示列表数据
     *
     * @param regionBeen 数据列表
     */
    @Override
    public void showRegin(List<RegionBean> regionBeen) {
        if (REGION_LV == currentLv) {
            //当前级别地区
            dataRegionList = regionBeen;
        } else if (CITY_LV == currentLv) {
            //当前级别是城市
            dataCityList = regionBeen;
        } else {
            //当前级别是省
            dataProvinceList = regionBeen;
        }
        //显示数据
        mAdapter.setNewData(regionBeen);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
