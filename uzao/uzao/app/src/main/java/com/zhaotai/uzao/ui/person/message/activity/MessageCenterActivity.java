package com.zhaotai.uzao.ui.person.message.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.adapter.MessageCenterAdapter;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.MessageCenterBean;
import com.zhaotai.uzao.ui.person.message.contract.MessageCenterContract;
import com.zhaotai.uzao.ui.person.message.presenter.MessageCenterPresenter;
import com.zhaotai.uzao.utils.LoginHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息中心列表
 */

public class MessageCenterActivity extends BaseFragmentActivity implements MessageCenterContract.View, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tool_bar_right_img)
    ImageView mRightImg;


    private MessageCenterAdapter mAdapter;
    private MessageCenterPresenter mPresenter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context, MessageCenterActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_message);
        mTitle.setText(R.string.message_center);
        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.service);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MessageCenterAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setEmptyStateView(this, R.mipmap.ic_status_empty_message, "外界还未跟你联系呢");
    }

    @Override
    protected void initData() {
        //请求消息列表
        mPresenter = new MessageCenterPresenter(this, this);
    }

    @Override
    protected void onResume() {
        showLoading();
        super.onResume();
        mPresenter.getMessageCenterList();
    }

    @OnClick(R.id.tool_bar_right_img)
    public void onClickRightIm() {
        loginIm();
    }

    private void loginIm() {
        showLoadingDialog();
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
                            disMisLoadingDialog();
                            mContext.startActivity(new Intent(mContext, KF5ChatActivity.class));
                        }
                    } else if (resultCode == 10050) {
                        createImUser(mContext, map);
                    }
                } catch (JSONException e) {
                    disMisLoadingDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                disMisLoadingDialog();
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
                            disMisLoadingDialog();
                            context.startActivity(new Intent(context, KF5ChatActivity.class));
                        }
                    }
                } catch (JSONException e) {
                    disMisLoadingDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                disMisLoadingDialog();
            }
        });
    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }


    @Override
    public void showCenterList(List<MessageCenterBean> data) {
        mAdapter.setNewData(data);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MessageCenterBean item = mAdapter.getItem(position);
        if (item != null && item.getExtend1() != null) {
            MessageDetailActivity.launch(this, item.getExtend1());
        }
    }

}
