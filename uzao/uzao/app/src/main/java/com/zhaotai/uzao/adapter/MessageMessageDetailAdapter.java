package com.zhaotai.uzao.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.MessageDetailBean;
import com.zhaotai.uzao.utils.TimeUtils;


/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 消息详情页
 */

public class MessageMessageDetailAdapter extends BaseQuickAdapter<MessageDetailBean, BaseViewHolder> {

    public MessageMessageDetailAdapter() {
        super(R.layout.item_message_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageDetailBean item) {

        helper.setText(R.id.tv_message_detail_time, TimeUtils.millis2String(Long.valueOf(item.getRecDate())));
        if ("N".equals(item.getHasReaded())) {
            helper.setText(R.id.tv_message_detail_title, "未读");
            helper.getView(R.id.tv_message_detail_title).setSelected(true);
        } else {
            helper.setText(R.id.tv_message_detail_title, "已读");
            helper.getView(R.id.tv_message_detail_title).setSelected(false);
        }
        helper.setText(R.id.tv_message_detail_content, item.getMessageContent());

    }
}
