package com.zhaotai.uzao.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseRecyclerAdapter;
import com.zhaotai.uzao.bean.PersonBean;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.transform.CircleTransform;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 我关注的设计师列表  应用地方: 个人中心
 */

public class MyAttentionDesignerListAdapter extends BaseRecyclerAdapter<PersonBean, BaseViewHolder> {

    private Context context;

    public MyAttentionDesignerListAdapter(Context context) {
        super(R.layout.item_my_attention_designer);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonBean item) {
        //设计师头像
        GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + item.avatar, (ImageView) helper.getView(R.id.item_designer_image)
                , R.drawable.ic_default_head, R.drawable.ic_default_head, new CircleTransform(mContext));

        //设计师名字
        helper.setText(R.id.tv_designer_name, item.nickName);
        //粉丝数
        helper.setText(R.id.tv_designer_fans, "粉丝  " + item.followCount);

        helper.addOnClickListener(R.id.rl_attention);
        helper.addOnClickListener(R.id.tv_designer_attention);
    }
}
