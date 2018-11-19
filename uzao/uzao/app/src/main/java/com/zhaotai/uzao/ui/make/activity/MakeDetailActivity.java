package com.zhaotai.uzao.ui.make.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kf5.sdk.im.ui.KF5ChatActivity;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.init.UserInfoAPI;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.system.utils.SafeJson;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MakeBean;
import com.zhaotai.uzao.constants.OrderStatus;
import com.zhaotai.uzao.utils.GlideUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/8/26
 * Created by LiYou
 * Description :  制造详情页
 */

public class MakeDetailActivity extends BaseActivity {

    @BindView(R.id.tv_make_order_spu_img)
    ImageView mSpuImg;

    @BindView(R.id.tv_make_order_spu_name)
    TextView mSpuName;

    @BindView(R.id.tv_make_order_spu_category)
    TextView mSpuCategory;

    @BindView(R.id.iv_make_state_img1)
    ImageView mImage1;
    @BindView(R.id.iv_make_state_img2)
    ImageView mImage2;
    @BindView(R.id.iv_make_state_img3)
    ImageView mImage3;
    @BindView(R.id.iv_make_state_img4)
    ImageView mImage4;

    @BindView(R.id.iv_make_detail_img)
    ImageView mResultImg;
    @BindView(R.id.iv_make_detail_result_text)
    TextView mResultText;
    @BindView(R.id.iv_make_detail_reason)
    TextView mReason;

    @BindView(R.id.tool_bar_right_img)
    ImageView mRightBtn;

    private static final String EXTRA_KEY_MAKE_INFO = "extra_key_make_info";
    private UITipDialog tipDialog;

    public static void launch(Context context, MakeBean makeBean) {
        Intent intent = new Intent(context, MakeDetailActivity.class);
        intent.putExtra(EXTRA_KEY_MAKE_INFO, makeBean);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_make_detail);
        mTitle.setText("我的智造");

        //客服
        mRightBtn.setVisibility(View.VISIBLE);
        mRightBtn.setImageResource(R.drawable.service);
    }

    @Override
    protected void initData() {
        MakeBean makeBean = (MakeBean) getIntent().getSerializableExtra(EXTRA_KEY_MAKE_INFO);

        GlideUtil.getInstance().loadImage(this, mSpuImg, ApiConstants.UZAOCHINA_IMAGE_HOST + makeBean.thumbnail);
        mSpuName.setText(makeBean.designName);
        mSpuCategory.setText(getString(R.string.belong_category, makeBean.extend3));

        switch (makeBean.produceStatus) {
            case OrderStatus.DESIGN_UNAPPROVED://基本信息审核不通过
                mImage1.setImageResource(R.drawable.ic_make_un_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_fail);
                mResultText.setText("很遗憾,审核未能通过!");
                mResultText.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                mReason.setText(getString(R.string.reason,makeBean.reason));
                break;
            case OrderStatus.VBP_WAIT_CONFIRM:///生产确认待审核
            case OrderStatus.VBP_NO_TPAY://生产 服务 支付
            case OrderStatus.WAIT_PRODUCE://基本信息审核通过
                mImage1.setImageResource(R.drawable.ic_make_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_success);
                mResultText.setText("恭喜您,已通过基本信息的审核");
                break;
            case OrderStatus.VBP_UNAPPROVED://生产确认审核不通过
                mImage1.setImageResource(R.drawable.ic_make_pass);
                mImage2.setImageResource(R.drawable.ic_make_un_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_fail);
                mResultText.setText("很遗憾,审核未能通过!");
                mResultText.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                mReason.setText(getString(R.string.reason,makeBean.reason));
                break;
            case OrderStatus.WAIT_CONFIRM_SUPPLIER://生产确认审核通过
            case OrderStatus.WAIT_CONFIRM_PRODUCE://生产信息待确认
                mImage1.setImageResource(R.drawable.ic_make_pass);
                mImage2.setImageResource(R.drawable.ic_make_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_success);
                mResultText.setText("恭喜您,已通过生产确认的审核");
                break;
            case OrderStatus.PRODUCE_UNAPPROVED://生产信息审核不通过
                mImage1.setImageResource(R.drawable.ic_make_pass);
                mImage2.setImageResource(R.drawable.ic_make_pass);
                mImage3.setImageResource(R.drawable.ic_make_un_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_fail);
                mResultText.setText("很遗憾,审核未能通过!");
                mResultText.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                mReason.setText(getString(R.string.reason,makeBean.reason));
                break;
            case OrderStatus.WAIT_CONFIRM_CONTRACT://生产信息审核通过
            case OrderStatus.CONTRACT_WAIT_APPROVED://合同待审核
                mImage1.setImageResource(R.drawable.ic_make_pass);
                mImage2.setImageResource(R.drawable.ic_make_pass);
                mImage3.setImageResource(R.drawable.ic_make_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_success);
                mResultText.setText("恭喜您,已通过生产信息的审核");
                break;
            case OrderStatus.CONTRACT_UNAPPROVED://合同审核不通过
                mImage1.setImageResource(R.drawable.ic_make_pass);
                mImage2.setImageResource(R.drawable.ic_make_pass);
                mImage3.setImageResource(R.drawable.ic_make_pass);
                mImage4.setImageResource(R.drawable.ic_make_un_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_fail);
                mResultText.setText("很遗憾,审核未能通过!");
                mResultText.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                mReason.setText(getString(R.string.reason,makeBean.reason));
                break;
            case OrderStatus.WAIT_SUBMIT_PRODUCE://合同审核通过
            case "finish":
                mImage1.setImageResource(R.drawable.ic_make_pass);
                mImage2.setImageResource(R.drawable.ic_make_pass);
                mImage3.setImageResource(R.drawable.ic_make_pass);
                mImage4.setImageResource(R.drawable.ic_make_pass);
                mResultImg.setImageResource(R.drawable.ic_produce_success);
                mResultText.setText("恭喜您,已通过合同审核的审核");
                break;
        }
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    private void showProgress() {
        tipDialog = new UITipDialog.Builder(mContext).setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
    }

    private void stopProgress() {
        tipDialog.dismiss();
    }

    @OnClick(R.id.tool_bar_right_img)
    public void goToIm() {
        loginIm();
    }

    private void loginIm() {
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
                stopProgress();
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
