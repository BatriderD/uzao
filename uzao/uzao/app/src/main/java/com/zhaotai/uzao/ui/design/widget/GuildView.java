package com.zhaotai.uzao.ui.design.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.SPUtils;

/**
 * Created by Administrator on 2018/4/12 0012.
 */

public class GuildView {
    private Context context;
    private ViewGroup rootView;
    private View guildView;

    public GuildView(Context context, ViewGroup rootView) {
        this.context = context;
        this.rootView = rootView;
    }

    public void showGuildView() {
        Boolean firstDesign = SPUtils.getSharedBooleanData(GlobalVariable.FIRST_DESIGN, true);
        if (firstDesign) {
            showGuildView(0);
        }
    }

    //导航view
    private void showGuildView(final int pos) {
        if (pos > 4) {
            rootView.removeView(guildView);
            SPUtils.setSharedBooleanData(GlobalVariable.FIRST_DESIGN, false);
            return;
        }
        if (guildView == null) {
            guildView = View.inflate(context, R.layout.design_design_guild, null);
            rootView.addView(guildView);
        }
        TextView tvContent = (TextView) guildView.findViewById(R.id.tv_guild_content);
        switch (pos) {
            case 0:
                tvContent.setText(R.string.design_guild_1);
                break;
            case 1:
                tvContent.setText(R.string.design_guild_2);
                break;
            case 2:
                tvContent.setText(R.string.design_guild_3);
                break;
            case 3:
                tvContent.setText(R.string.design_guild_4);
                break;
            case 4:
                tvContent.setText(R.string.design_guild_5);
                break;
        }
        LinearLayout llarr = (LinearLayout) guildView.findViewById(R.id.ll_guild_arr);
        LinearLayout llbar = (LinearLayout) guildView.findViewById(R.id.ll_design_bar);
        for (int i = 0; i < llarr.getChildCount(); i++) {
            View iv_arr = llarr.getChildAt(i);
            View iv_bar = llbar.getChildAt(i);

            iv_arr.setVisibility((i == pos) ? View.VISIBLE : View.INVISIBLE);
            iv_bar.setVisibility((i == pos) ? View.VISIBLE : View.INVISIBLE);
        }
        View.OnClickListener nextClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGuildView(pos + 1);
            }
        };

        guildView.findViewById(R.id.btn_know).setOnClickListener(nextClick);
        guildView.setOnClickListener(nextClick);
        guildView.findViewById(R.id.iknow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.setSharedBooleanData(GlobalVariable.FIRST_DESIGN, false);
                showGuildView(100);
            }
        });

    }

}
