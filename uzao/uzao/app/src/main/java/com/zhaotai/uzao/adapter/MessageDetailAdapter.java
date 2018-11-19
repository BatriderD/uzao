package com.zhaotai.uzao.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.MessageDetailBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.TimeUtils;


/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息详情页
 */

public class MessageDetailAdapter extends BaseQuickAdapter<MessageDetailBean, BaseViewHolder> {

    public MessageDetailAdapter() {
        super(R.layout.item_message_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageDetailBean item) {

        helper.setText(R.id.tv_message_detail_time, TimeUtils.millis2String(Long.valueOf(item.getRecDate())));
        if (GlobalVariable.NOTIFICATION_EMESSAGE.equals(item.getExtend1())) {
            helper.setText(R.id.tv_message_detail_title, R.string.notification_messages);
        } else if ("sourceMaterialMessages".equals(item.getExtend1())) {
            helper.setText(R.id.tv_message_detail_title, R.string.comment_messages);
        } else {
            helper.setText(R.id.tv_message_detail_title, R.string.system_messages);
        }
        helper.setText(R.id.tv_message_detail_content, item.getMessageContent());

    }
}
