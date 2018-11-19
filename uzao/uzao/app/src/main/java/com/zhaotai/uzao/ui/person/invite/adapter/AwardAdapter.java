package com.zhaotai.uzao.ui.person.invite.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.ui.person.invite.model.RebateBean;
import com.zhaotai.uzao.utils.TimeUtils;

/**
 * Time: 2017/12/2
 * Created by LiYou
 * Description : 邀请明细
 */

public class AwardAdapter extends BaseQuickAdapter<RebateBean, BaseViewHolder> {

    public AwardAdapter() {
        super(R.layout.item_award_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, RebateBean item) {

        helper.setText(R.id.tv_invite_detail_name, item.fromUserName)//用户昵称
                .setText(R.id.tv_invite_detail_time, TimeUtils.millis2String(Long.valueOf(item.createTime)))//返点时间
                .setText(R.id.tv_invite_detail_rebate, item.ruleDetail)//返点
                .setText(R.id.tv_invite_detail_rebate_account, item.rebateValueY)//返现金额
                .setText(R.id.tv_invite_detail_close_account, item.orderPriceY);
    }
}
