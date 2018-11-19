package com.zhaotai.uzao.ui.make.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;

import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.bean.MakeBean;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.make.contract.MakeContract;
import com.zhaotai.uzao.utils.LoginHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Time: 2017/8/26
 * Created by LiYou
 * Description :
 */

public class MakePresenter extends MakeContract.MakePresenter {

    private MakeContract.View view;

    public MakePresenter(Context context, MakeContract.View view) {
        mContext = context;
        this.view = view;
    }

    @Override
    public void getMakeList(final int start, final boolean isLoading) {
        Api.getDefault().getMakeList(start)
                .compose(RxHandleResult.<PageInfo<MakeBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<MakeBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<MakeBean> orderBeanPageInfo) {
                        if (isLoading && start == 0) {
                            view.showContent();
                            view.stopLoadingMore();
                        } else if (start == 0) {
                            view.stopRefresh();
                        } else {
                            view.stopLoadingMore();
                        }
                        view.showMakeList(orderBeanPageInfo);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            view.stopRefresh();
                            view.showNetworkFail(message);
                        } else {
                            view.loadingFail();
                        }
                    }
                });
    }

    @Override
    public void loginIm() {
        view.showProgress();
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
                            view.stopProgress();
                            mContext.startActivity(new Intent(mContext, KF5ChatActivity.class));
                        }
                    } else if (resultCode == 10050) {
                        createImUser(mContext, map);
                    }
                } catch (JSONException e) {
                    view.stopProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                view.showProgress();
            }
        });
    }

    @Override
    public void createImUser(final Context context, Map<String, String> map) {
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
                            view.stopProgress();
                            context.startActivity(new Intent(context, KF5ChatActivity.class));
                        }
                    }
                } catch (JSONException e) {
                    view.stopProgress();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {
                view.stopProgress();
            }
        });
    }
}
