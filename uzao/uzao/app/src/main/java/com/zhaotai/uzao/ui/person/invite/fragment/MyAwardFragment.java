package com.zhaotai.uzao.ui.person.invite.fragment;

import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragment;
import com.zhaotai.uzao.ui.person.invite.contract.InviteContract;
import com.zhaotai.uzao.ui.person.invite.model.RebateBean;
import com.zhaotai.uzao.ui.person.invite.presenter.InvitePresenter;

import butterknife.BindView;

/**
 * Time: 2017/12/1
 * Created by LiYou
 * Description : 我的奖励
 */

public class MyAwardFragment extends BaseFragment implements InviteContract.View {

    //结算
    @BindView(R.id.tv_close_account)
    TextView mCloseAccount;
    //累计
    @BindView(R.id.tv_grand_total)
    TextView mGrandTotal;

    private InvitePresenter mPresenter;

    public static MyAwardFragment newInstance() {
        return new MyAwardFragment();
    }

    @Override
    protected int layoutId() {
        return R.layout.frag_my_award;
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
    public void showRebate(RebateBean rebate) {
        //结算
        mCloseAccount.setText(rebate.rebateMoneY);
        //累计
        mGrandTotal.setText(rebate.totalRebateMoneyY);
    }
}
