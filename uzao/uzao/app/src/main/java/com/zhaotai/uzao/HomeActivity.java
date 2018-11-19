package com.zhaotai.uzao;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.app.EventBusEvent;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.bean.EventBean.PersonInfo;
import com.zhaotai.uzao.bean.VersionBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.category.fragment.CategoryFragment;
import com.zhaotai.uzao.ui.main.MainFragment;
import com.zhaotai.uzao.ui.person.fragment.PersonFragment;
import com.zhaotai.uzao.ui.shopping.fragment.ShoppingCartFragment;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.downLoad;
import com.zhaotai.uzao.view.BottomBar;
import com.zhaotai.uzao.view.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.autosize.internal.CancelAdapt;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 主页
 */
public class HomeActivity extends BaseFragmentActivity implements CancelAdapt {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];
    private BottomBar mBottomBar;


    public static void launch(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, int position) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("showModule", position);
        context.startActivity(intent);
    }


    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected void initView() {
        //状态栏背景
        setContentView(R.layout.activity_home_tt);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        //初始化view
        initContentFragment();

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.addItem(new BottomBarTab(this, R.drawable.ic_home_home_normal, getString(R.string.main_page)))
                .addItem(new BottomBarTab(this, R.drawable.ic_home_mall_normal, getString(R.string.category)))
                .addItem(new BottomBarTab(this, R.drawable.ic_home_shopping_normal, getString(R.string.shopping_cart)))
                .addItem(new BottomBarTab(this, R.drawable.ic_home_my_normal, getString(R.string.my)));
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    //关键词
    private List<String> words;

    @Override
    public void initData() {
//        检查更新
        checkVersion();
//        请求预置关键词
        getSearchWord();

    }

    /**
     * 请求预置关键词 并延迟分发给首页和分类页面
     */
    private void getSearchWord() {
        Api.getDefault().getPresetSearchWord()
                .compose(RxHandleResult.<List<String>>handleResultMap())
                .flatMap(new Function<List<String>, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(@NonNull List<String> strings) throws Exception {
                        words = strings;
                        return Observable.interval(0, 5, TimeUnit.SECONDS);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (words != null && words.size() > 0) {
                            String word = words.get((int) (aLong % words.size()));
                            if (mFragments[FIRST] != null) {
                                ((MainFragment) mFragments[FIRST]).setPresetSearchWord(word);
                            }
                            if (mFragments[SECOND] != null) {
                                ((CategoryFragment) mFragments[SECOND]).setPresetSearchWord(word);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 检查更新
     */
    private void checkVersion() {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(HomeActivity.this)
                //实现httpManager接口的对象
                .setHttpManager(new downLoad())
                .setUpdateUrl("312")
                .build()
                .checkNewApp(new UpdateCallback() {
                                 @Override
                                 protected UpdateAppBean parseJson(String json) {
                                     UpdateAppBean updateAppBean = new UpdateAppBean();
                                     Gson gson = new Gson();
                                     boolean constraint;//是否强制更新
                                     String isUpdate;
                                     VersionBean versionBean = gson.fromJson(json, VersionBean.class);
                                     if (versionBean.vnumber.equals(BuildConfig.VERSION_NAME)) {
                                         isUpdate = "No";
                                     } else {
                                         isUpdate = "Yes";
                                     }
                                     constraint = versionBean.vupload.equals("Y");
                                     updateAppBean
                                             //（必须）是否更新Yes,No
                                             .setUpdate(isUpdate)
                                             //（必须）新版本号，
                                             .setNewVersion(versionBean.vnumber)
                                             //（必须）下载地址
                                             .setApkFileUrl(versionBean.vaddress)
                                             //（必须）更新内容
                                             .setUpdateLog(versionBean.vdescription)
                                             //是否强制更新，可以不设置
                                             .setConstraint(constraint);
                                     return updateAppBean;
                                 }

                                 @Override
                                 protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                                     updateAppManager.showDialogFragment();
                                 }

                                 /**
                                  * 网络请求之前
                                  */
                                 @Override
                                 protected void onBefore() {
                                 }

                                 @Override
                                 protected void onAfter() {
                                 }

                                 @Override
                                 protected void noNewApp() {
                                 }
                             }
                );
    }

    /**
     * 初始化fragment
     */
    private void initContentFragment() {
        SupportFragment firstFragment = findFragment(MainFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = MainFragment.newInstance();
            mFragments[SECOND] = CategoryFragment.newInstance();
            mFragments[THIRD] = ShoppingCartFragment.newInstance(ShoppingCartFragment.FROM_MAIN);
            mFragments[FOURTH] = PersonFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]
            );
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(CategoryFragment.class);
            mFragments[THIRD] = findFragment(ShoppingCartFragment.class);
            mFragments[FOURTH] = findFragment(PersonFragment.class);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switch (intent.getIntExtra("showModule", 0)) {
            case 0://首页
                mBottomBar.setCurrentItem(0);
                break;
            case 1://分类
                mBottomBar.setCurrentItem(1);
                break;
            case 3://个人中心
                mBottomBar.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                ToastUtil.showShort("再按一次退出程序");
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PersonInfo info) {
        switch (info.code) {
            //刷新消息未读消息数量
            case EventBusEvent.REFRESH_UNREAD_COUNT:
                getUnHandleMessage();
                break;
        }
    }


    public void getUnHandleMessage() {
        Api.getDefault().getUnReadCount()
                .compose(RxHandleResult.<Integer>handleResult())
                .subscribe(new RxSubscriber<Integer>(mContext, false) {
                    @Override
                    public void _onNext(Integer integer) {
                        ((MainFragment) mFragments[FIRST]).showUnHandleMessage(integer);
                        ((PersonFragment) mFragments[FOURTH]).showUnHandleMessage(integer);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }
}
