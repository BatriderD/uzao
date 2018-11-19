package com.zhaotai.uzao.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.bean.DiscountCouponBean;
import com.zhaotai.uzao.utils.TimeUtils;
import com.zhaotai.uzao.widget.DashView;
import com.zhaotai.uzao.widget.couponview.CouponView;


/**
 * Time: 2017/7/19
 * Created by LiYou
 * Description : 我的优惠券适配器
 */

public class MyDiscountAdapter extends BaseQuickAdapter<DiscountCouponBean, BaseViewHolder> {

    private static final String EXPIRED_FOREVER = "0";
    private static final String EXPIRED_FIXED_TERM = "1";
    private static final String EXPIRED_NON_FIXED = "2";

    // 0 未使用  1 已使用   2 已过期
    private int mType = 0;

    public MyDiscountAdapter() {
        this(0);
    }

    public MyDiscountAdapter(int type) {
        super(R.layout.item_my_discount);
        mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscountCouponBean item) {
        //优惠券金额
        helper.setText(R.id.tv_coupon_price, String.valueOf(item.secondPrice / 100));
        //设置使用条件
        helper.setText(R.id.tv_coupon_full_use, "满" + String.valueOf(item.firstPrice / 100) + "可用");
        helper.setText(R.id.tv_discount_name,item.name);

        //设置期限
        TextView tv_validity = helper.getView(R.id.tv_coupon_bottom_time);

        //优惠券戳
        ImageView iv_stamp = helper.getView(R.id.iv_coupons_stamp);
        switch (item.expiredType) {
            case EXPIRED_FOREVER:
                //永久
                tv_validity.setText(R.string.coupon_used_forever);
                break;

            case EXPIRED_FIXED_TERM:
                String time = TimeUtils.dateFormatToYear_Month_Day(Long.valueOf(item.startTime))
                        + "至" + TimeUtils.dateFormatToYear_Month_Day(Long.valueOf(item.endTime));
                tv_validity.setText(time);
                break;

            case EXPIRED_NON_FIXED:
                tv_validity.setText(item.validDay);
                break;
        }

        //背景
        View view = helper.getView(R.id.fl_coupon_bg);
        switch (mType) {
            case 0://未使用
                view.setBackgroundColor(Color.parseColor(item.color));
                iv_stamp.setVisibility(View.GONE);
                break;
            case 1://已使用
                view.setBackgroundColor(Color.parseColor("#cdcdcd"));
                iv_stamp.setVisibility(View.VISIBLE);
                iv_stamp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_coupon_used));
                break;
            default://已失效
                view.setBackgroundColor(Color.parseColor("#cdcdcd"));
                iv_stamp.setVisibility(View.VISIBLE);
                iv_stamp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_coupon_overdue));
                break;
        }
    }

}
