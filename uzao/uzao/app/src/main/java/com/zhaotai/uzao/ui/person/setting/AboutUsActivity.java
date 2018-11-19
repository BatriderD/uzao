package com.zhaotai.uzao.ui.person.setting;

import android.content.Context;
import android.content.Intent;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseActivity;

/**
 * Time: 2017/5/24
 * Created by LiYou
 * Description : 关于我们
 */

public class AboutUsActivity extends BaseActivity {

    public static void launch(Context context){
        context.startActivity(new Intent(context,AboutUsActivity.class));
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about_us);
        mTitle.setText("关于优造");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

}
