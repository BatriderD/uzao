package com.zhaotai.uzao.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.ProtocolBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

/**
 * Time: 2017/9/27
 * Created by LiYou
 * Description : 用户协议
 */

public class ProtocolActivity extends BaseActivity {

    @BindView(R.id.tv_protocol)
    TextView mTextView;

    public static void launch(Context context, String protocolType) {
        Intent intent = new Intent(context, ProtocolActivity.class);
        intent.putExtra("protocol", protocolType);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_protocol);
    }

    @Override
    protected void initData() {
        String protocolType = getIntent().getStringExtra("protocol");

        Api.getDefault().getRegisterProtocol(protocolType)
                .compose(RxHandleResult.<PageInfo<ProtocolBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ProtocolBean>>(mContext, true) {
                    @Override
                    public void _onNext(PageInfo<ProtocolBean> s) {
                        if (s.list != null && s.list.size() > 0) {
                            mTitle.setText(s.list.get(0).title);
                            RichText.fromHtml(s.list.get(0).contentBody).into(mTextView);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }
}
