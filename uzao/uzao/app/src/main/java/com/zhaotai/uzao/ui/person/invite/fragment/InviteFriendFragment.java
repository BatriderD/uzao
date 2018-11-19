package com.zhaotai.uzao.ui.person.invite.fragment;

import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.ui.person.invite.contract.InviteContract;
import com.zhaotai.uzao.ui.person.invite.model.RebateBean;
import com.zhaotai.uzao.ui.person.invite.presenter.InvitePresenter;
import com.zhaotai.uzao.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2017/12/1
 * Created by LiYou
 * Description : 邀请好友
 */

public class InviteFriendFragment extends BaseFragment implements InviteContract.View {

    @BindView(R.id.tv_invite_one)
    TextView mTvOne;
    @BindView(R.id.tv_invite_two)
    TextView mTvTwo;

    private InvitePresenter mPresenter;
    private String url = "";

    public static InviteFriendFragment newInstance() {
        return new InviteFriendFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_invite_friend;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {
        mPresenter = new InvitePresenter(this, _mActivity);
    }

    @Override
    public void initData() {
        mPresenter.getRebate();
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    public void showRebate(RebateBean data) {
        mTvOne.setText("邀请好友，完成首单现金返现" + data.myProfit);
        mTvTwo.setText("好友注册成功，获得优惠券" + data.friendProfit);
        url = data.invitationURL + "?userPromoteCode=" + data.userPromoteCode;
    }

    @OnClick(R.id.btn_invite)
    public void invite() {
        UMImage image = new UMImage(getActivity(), R.mipmap.ic_launcher);//bitmap文件

        UMWeb umWeb = new UMWeb(url, "欢迎使用优造中国", "欢迎使用优造中国，最全的定制网站", image);
        new ShareAction(getActivity())
                .withText("uzao")
                .withMedia(umWeb)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA)
                .setCallback(shareListener).open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         *  SHARE_MEDIA 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         *  分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.showShort("分享成功");
        }

        /**
         *  分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.showShort("分享失败");
//            Toast.makeText(SettingActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         *  分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.showShort("取消分享");
//            Toast.makeText(SettingActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };
}
