package com.zhaotai.uzao.ui.person.message.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.MessageCommentBean;
import com.zhaotai.uzao.ui.brand.BrandActivity;
import com.zhaotai.uzao.ui.category.material.activity.MaterialDetailActivity;
import com.zhaotai.uzao.ui.person.message.contract.MessageCommentContract;
import com.zhaotai.uzao.ui.person.message.presenter.MessageCommentPresenter;
import com.zhaotai.uzao.ui.theme.activity.ThemeDetailActivity;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.ImageSizeUtil;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.transform.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Time: 2018/1/12
 * Created by LiYou
 * Description : 评论消息页面
 */

public class MessageCommentActivity extends BaseActivity implements MessageCommentContract.View {

    private static final String DISCUSS_TYPE = "type";
    private static final String DISCUSS_TYPE_SOURCE_MATERIAL = "SourceMaterial";
    private static final String DISCUSS_TYPE_THEME = "Theme";
    private static final String DISCUSS_TYPE_BRAND = "Brand";

    private MessageCommentPresenter mPresenter;

    //评论素材
    @BindView(R.id.ll_comment_material)
    public LinearLayout ll_material;

    //评论评论
    @BindView(R.id.ll_comment_comment)
    public LinearLayout ll_comment;

    @BindView(R.id.iv_discuss_head_photo)
    public ImageView ivHeadPhoto;

    @BindView(R.id.iv_discuss_to_talk)
    public ImageView iv_discuss_to_talk;

    @BindView(R.id.iv_material_pic)
    public ImageView iv_material_pic;

    @BindView(R.id.tv_discuss_nick_name)
    public TextView tv_discuss_nick_name;

    @BindView(R.id.tv_discuss_time)
    public TextView tv_discuss_time;

    @BindView(R.id.tv_discuss_content)
    public TextView tv_discuss_content;
    @BindView(R.id.tv_comment_content)
    public TextView tv_comment_content;

    @BindView(R.id.rl_discuss)
    public RelativeLayout rl_discuss;

    @BindView(R.id.tv_discuss_like)
    public TextView tv_discuss_like;

    @BindView(R.id.et_talk_word)
    EditText etTalkWord;
    private String discussType;
    private MessageCommentBean bean;

    /**
     * 启动消息评论页面
     *
     * @param context     context
     * @param discussType 消息的评论类型
     * @param entityId    消息评论id
     * @param sequenceNBR 消息的id
     */
    public static void launch(Context context, String discussType, String entityId, String sequenceNBR) {
        Intent intent = new Intent(context, MessageCommentActivity.class);
        intent.putExtra(DISCUSS_TYPE, discussType);
        intent.putExtra("sequenceNBR", sequenceNBR);
        intent.putExtra("entityId", entityId);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_message_comment);
        mTitle.setText("消息详情");
        //获取presenter
        mPresenter = new MessageCommentPresenter(this, this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        //讨论的类型
        discussType = intent.getStringExtra(DISCUSS_TYPE);
        //讨论的id
        String id = intent.getStringExtra("entityId");
        //消息的id
        String sequenceNBR = intent.getStringExtra("sequenceNBR");
        //显示状态页
        showLoading();
        //获得消息评论数据
        mPresenter.getCommentData(id);
        //设置消息已读
        mPresenter.putMessageRead(sequenceNBR);

    }

    @Override
    protected boolean hasBaseLayout() {
        return true;
    }

    @Override
    public boolean hasTitle() {
        return true;
    }


    /**
     * 显示回复框 并弹出软键盘
     */
    @OnClick(R.id.iv_discuss_to_talk)
    public void toDiscuss() {
        rl_discuss.setVisibility(View.VISIBLE);
        KeyboardUtils.showSoftInput(etTalkWord);
    }

    /**
     * 评论点赞
     */
    @OnClick(R.id.tv_discuss_like)
    public void toLike() {
        if (!tv_discuss_like.isSelected()) {
            mPresenter.like(bean.getSequenceNBR());
        } else {
            mPresenter.disLike(bean.getSequenceNBR());
        }
    }


    /**
     * 发送评论
     */
    @OnClick(R.id.tv_publish)
    public void publishDiscuss() {
        String word = etTalkWord.getText().toString();
        KeyboardUtils.hideSoftInput(this);
        commitDiscuss(word);
    }

    /**
     * 查看详情
     */
    @OnClick(R.id.tv_discuss_to_all)
    public void showDetail() {
        String entityType = this.bean.getEntityType();
        if (entityType != null && entityType.contains(DISCUSS_TYPE_SOURCE_MATERIAL)) {
//            素材类型 跳转到素材详情
            MaterialDetailActivity.launch(this, bean.getEntityId());
        } else if (entityType != null && entityType.contains(DISCUSS_TYPE_THEME)) {
            //            主题类型 跳转到主题详情
            ThemeDetailActivity.launch(this, bean.getEntityId());
        } else if (entityType != null && entityType.contains(DISCUSS_TYPE_BRAND)) {
            //品牌详情
            BrandActivity.launch(this, bean.getEntityId());
        }
    }

    /**
     * 展示消息数据
     *
     * @param s 消息数据实体类
     */
    @Override
    public void showData(MessageCommentBean s) {
        this.bean = s;
        String userInfo = s.getUserInfo();
        MessageCommentBean.UserInfo userInfoBean = GsonUtil.getGson().fromJson(userInfo, MessageCommentBean.UserInfo.class);
        if (userInfoBean.getUserType() == 100) {
            tv_discuss_nick_name.setText(userInfoBean.getNickName());
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + userInfoBean.getAvatar(), ivHeadPhoto, R.drawable.ic_uzao_default_head, new CircleTransform(mContext));
        } else if (300 == userInfoBean.getUserType()) {
            tv_discuss_nick_name.setText("优造中国");
            GlideLoadImageUtil.load(mContext, ApiConstants.UZAOCHINA_IMAGE_HOST + userInfoBean.getAvatar(), ivHeadPhoto, R.drawable.ic_uzao_default_head, new CircleTransform(mContext));
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        //设置时间
        tv_discuss_time.setText(TimeUtils.dateFormat(Long.valueOf(s.getRecDate()), simpleDateFormat));
        //显示关注数
        tv_discuss_like.setText(String.valueOf(s.getUpvoteCount()));
        //显示我的点赞状态
        tv_discuss_like.setSelected(s.isUpvote());
        //显示i评论内容
        tv_discuss_content.setText(s.getCommentBody());

        if ("ROOT".equals(s.getParentId())) {
            //素材评论
            ll_material.setVisibility(View.VISIBLE);
            MessageCommentBean.EntityProfile entityProfile = GsonUtil.getGson().fromJson(s.entityProfile, MessageCommentBean.EntityProfile.class);
            if (entityProfile != null) {
                GlideLoadImageUtil.load(this, ApiConstants.UZAOCHINA_IMAGE_HOST + ImageSizeUtil.changeImageSize(entityProfile.thumbnail), iv_material_pic);
            }
        } else {
            //评论评论
            ll_comment.setVisibility(View.VISIBLE);
            tv_comment_content.setText(s.getUpCommentBody());
        }
        showContent();
    }

    /**
     * 将ui置成不喜欢
     */
    @Override
    public void showDisLikeSuccess() {
        tv_discuss_like.setSelected(false);
        int count = Integer.valueOf(tv_discuss_like.getText().toString());
        count = count - 1;
        bean.setUpvoteCount(count);
        tv_discuss_like.setText(String.valueOf(count));
    }

    /**
     * 将ui置成喜欢
     */
    @Override
    public void showLikeSuccess() {
        tv_discuss_like.setSelected(true);
        int count = Integer.valueOf(tv_discuss_like.getText().toString());
        count = count + 1;
        bean.setUpvoteCount(count);
        tv_discuss_like.setText(String.valueOf(count));
    }

    @Override
    public void showCommitDiscussDiscussSuccess() {
        rl_discuss.setVisibility(View.GONE);
        etTalkWord.setText("");
        KeyboardUtils.hideSoftInput(this);
    }

    /**
     * 提交评论
     *
     * @param word 评论内容
     */
    public void commitDiscuss(String word) {
        if (StringUtil.isEmpty(word)) {
            ToastUtil.showShort("评论不能为空");
        } else {
            mPresenter.commitDiscussDiscuss(discussType, bean.getEntityId(), bean.getSequenceNBR(), bean.getFirstCommentId(), word);
        }
    }
}
