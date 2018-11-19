package com.zhaotai.uzao.adapter;

import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.MessageCenterBean;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * description: 消息中心adapter
 * author : zp
 * date: 2017/7/21
 */

public class MessageCenterAdapter extends BaseRecyclerAdapter<MessageCenterBean, BaseViewHolder> {
    public MessageCenterAdapter() {
        super(R.layout.item_message_center_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageCenterBean item) {
        if ("notificationMessages".equals(item.getExtend1())) {
            helper.setText(R.id.tv_message_center_title, R.string.notification_messages);
            helper.setImageDrawable(R.id.iv_message_center_icon, ContextCompat.getDrawable(mContext, R.drawable.ic_notification_message));
        } else if ("sourceMaterialMessages".equals(item.getExtend1())) {
            helper.setText(R.id.tv_message_center_title, R.string.comment_messages);
            helper.setImageDrawable(R.id.iv_message_center_icon, ContextCompat.getDrawable(mContext, R.drawable.ic_comment_messages));
        } else {
            helper.setText(R.id.tv_message_center_title, R.string.system_messages);
            helper.setImageDrawable(R.id.iv_message_center_icon, ContextCompat.getDrawable(mContext, R.drawable.ic_system_messages));
        }
        ImageView mask = helper.getView(R.id.iv_message_center_mark);
        QBadgeView qBadgeView = new QBadgeView(mContext);
        Badge badge = qBadgeView.bindTarget(mask).setBadgeGravity(Gravity.CENTER).setBadgePadding(3, true)
                .setBadgeTextSize(12, true);
//        未读消息数目
        String noHasReadedCount = item.getNoReadCount();
        if (!StringUtil.isEmpty(noHasReadedCount) && Integer.parseInt(noHasReadedCount) != 0) {
            qBadgeView.setVisibility(View.VISIBLE);
            badge.setBadgeNumber( Integer.parseInt(noHasReadedCount));
        } else {
            qBadgeView.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_message_center_content, item.getMessageContent());
        String time = TimeUtils.dateFormatToHour_Min(Long.valueOf(item.getRecDate()));
        helper.setText(R.id.tv_message_center_time, time);
    }
}
