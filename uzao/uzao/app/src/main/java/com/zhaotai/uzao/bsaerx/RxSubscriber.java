package com.zhaotai.uzao.bsaerx;

import android.content.Context;

import com.zhaotai.uzao.HomeActivity;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.app.MyApplication;
import com.zhaotai.uzao.base.BaseActivity;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.base.RxContractInterface;
import com.zhaotai.uzao.bean.EventBean.EventMessage;
import com.zhaotai.uzao.utils.LogUtils;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.NetWorkUtils;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.dialog.UITipDialog;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * time:2017/4/10
 * description:
 * author: LiYou
 */

public abstract class RxSubscriber<T> implements Observer<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog = false;
    private UITipDialog tipDialog;

    public RxSubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog = showDialog;
    }

    protected RxSubscriber(Context context) {
        this(context, MyApplication.getAppContext().getString(R.string.loading), false);
    }

    public RxSubscriber(Context context, boolean showDialog) {
        this(context, MyApplication.getAppContext().getString(R.string.loading), showDialog);
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).add(d);
        } else if (mContext instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) mContext).add(d);
        }

        if (showDialog) {
            try {
                if (tipDialog == null) {
                    tipDialog = new UITipDialog.Builder(mContext)
                            .setIconType(UITipDialog.Builder.ICON_TYPE_LOADING)
                            .setTipWord("正在加载").create();
                }
                tipDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable t) {
        LogUtils.logd(t.getMessage());
        if (showDialog) {
            if (tipDialog != null) {
                tipDialog.dismiss();
            }
        }
        //t.printStackTrace();
        //网络
        if (!NetWorkUtils.isNetConnected(MyApplication.getAppContext())) {
            ToastUtil.showShort(MyApplication.getAppContext().getString(R.string.no_net));
            _onError(MyApplication.getAppContext().getString(R.string.no_net));
        } else if (t instanceof TokenException) {
            //token 过期
            ToastUtil.showShort("请重新登录");
            if (mContext instanceof RxContractInterface) {
                ((RxContractInterface) mContext).stopRequest();
                HomeActivity.launch(mContext);
                LoginHelper.getAnonymous(mContext);
                EventBus.getDefault().post(new EventMessage(EventBusEvent.ReLoading));
            }
        } else {
            _onError(t.getMessage());
        }
    }

    @Override
    public void onComplete() {
        if (showDialog) {
            if (tipDialog != null) {
                tipDialog.dismiss();
            }
        }
    }

    public abstract void _onNext(T t);

    public abstract void _onError(String message);
}
