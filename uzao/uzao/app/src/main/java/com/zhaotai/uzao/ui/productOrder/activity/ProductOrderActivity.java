package com.zhaotai.uzao.ui.productOrder.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.ui.productOrder.fragment.AllProductOrderFragment;
import com.zhaotai.uzao.ui.productOrder.fragment.ClosedProductOrderFragment;
import com.zhaotai.uzao.ui.productOrder.fragment.CompleteProductOrderFragment;
import com.zhaotai.uzao.ui.productOrder.fragment.WaitHandleProductOrderFragment;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.widget.MyPagerAdapter;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/6/6
 * Created by LiYou
 * Description : 生产订单
 */

public class ProductOrderActivity extends BaseFragmentActivity {

    @BindView(R.id.order_tab)
    SlidingTabLayout mTab;
    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @BindView(R.id.tool_bar_right_img)
    ImageView mRightBtn;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private UITipDialog tipDialog;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ProductOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_my_order);
        mTitle.setText("生产订单");
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setImageResource(R.drawable.service);
        mFragments.add(AllProductOrderFragment.newInstance());
        mFragments.add(WaitHandleProductOrderFragment.newInstance());
        mFragments.add(CompleteProductOrderFragment.newInstance());
        mFragments.add(ClosedProductOrderFragment.newInstance());
        mTitles.add("全部订单");
        mTitles.add("待处理");
        mTitles.add("已完成");
        mTitles.add("已关闭");
        mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
        mTab.setViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }

    private void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext)
                .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
    }

    private void stopProgress() {
        tipDialog.dismiss();
    }

    @OnClick(R.id.tool_bar_right_img)
    public void goToIm() {
        showProgress();
        final Map<String, String> map = new ArrayMap<>();
        map.put(Field.PHONE, LoginHelper.getLoginId());

        UserInfoAPI.getInstance().loginUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    final JSONObject jsonObject = SafeJson.parseObj(result);
                    int resultCode = SafeJson.safeInt(jsonObject, "error");
                    if (resultCode == 0) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                        if (userObj != null) {
                            String userToken = userObj.getString(Field.USERTOKEN);
                            int id = userObj.getInt(Field.ID);
                            SPUtils.saveUserToken(userToken);
                            SPUtils.saveUserId(id);
                            stopProgress();
                            mContext.startActivity(new Intent(mContext, KF5ChatActivity.class));
                        }
                    } else if (resultCode == 10050) {
                        createImUser(mContext, map);
                    }
                } catch (JSONException e) {
                    stopProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                showProgress();
            }
        });
    }

    private void createImUser(final Context context, Map<String, String> map) {
        //用户不存在
        UserInfoAPI.getInstance().createUser(map, new HttpRequestCallBack() {
            @Override
            public void onSuccess(String result1) {
                final JSONObject jsonObject = SafeJson.parseObj(result1);
                int resultCode1 = SafeJson.safeInt(jsonObject, "error");
                try {
                    if (resultCode1 == 0) {
                        JSONObject dataObj = SafeJson.safeObject(jsonObject, Field.DATA);
                        JSONObject userObj = SafeJson.safeObject(dataObj, Field.USER);
                        if (userObj != null) {
                            String userToken = userObj.getString(Field.USERTOKEN);
                            int id = userObj.getInt(Field.ID);
                            SPUtils.saveUserToken(userToken);
                            SPUtils.saveUserId(id);
                            stopProgress();
                            context.startActivity(new Intent(context, KF5ChatActivity.class));
                        }
                    }
                } catch (JSONException e) {
                    stopProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                stopProgress();
            }
        });
    }

}
