package com.zhaotai.uzao.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhaotai.uzao.R;
import com.zhaotai.uzao.base.BaseFragmentActivity;
import com.zhaotai.uzao.ui.search.fragment.BaseSearchFragment;
import com.zhaotai.uzao.ui.search.fragment.MyMaterialBoughtFragment;
import com.zhaotai.uzao.ui.search.fragment.SearchCommonMainFragment;
import com.zhaotai.uzao.utils.KeyboardUtils;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.widget.EditWithDelView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Time: 2017/5/19
 * Created by LiYou
 * Description : 搜索通用页面
 */

public class SearchCommonActivity extends BaseFragmentActivity implements TextView.OnEditorActionListener {


    @BindView(R.id.etd_text)
    EditWithDelView mSearchText;

    @BindView(R.id.fl_root)
    FrameLayout flRoot;

    private SearchCommonMainFragment mainFragment;


    private String searchWord;
    private FragmentManager fm;
    private int type;
    private BaseSearchFragment searchFragment;

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, SearchCommonActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_common);

        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchMainPage();
            }
        });
        mSearchText.setOnEditorActionListener(this);
    }


    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    protected boolean hasBaseLayout() {
        return false;
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", -1);
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mainFragment = (SearchCommonMainFragment) SearchCommonMainFragment.newInstance(type);
        initSearchListFragment(type);
        ft.add(R.id.fl_root, searchFragment);
        ft.add(R.id.fl_root, mainFragment);
        ft.show(searchFragment);
        ft.show(mainFragment);
        ft.commit();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch();
            return true;
        }
        return false;
    }


    /**
     * 搜索
     */
    @OnClick(R.id.tv_tool_bar_search)
    public void onClickSearch() {
        this.searchWord = mSearchText.getText().toString();
        if (StringUtil.isTrimEmpty(searchWord)) {
            ToastUtil.showShort("请输入关键字");
            return;
        }
        KeyboardUtils.hideSoftInput(this);

        mainFragment.addSearchWord(searchWord);
        goSearchPage(searchWord);
    }

    /**
     * 展示搜索主页面 移出其他搜索fragment
     */
    @OnClick(R.id.etd_text)
    public void showSearchMainPage() {
        if (mainFragment.isVisible()) {
            return;
        }
        if (searchFragment != null && mainFragment.isHidden()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.show(mainFragment);
            ft.hide(searchFragment);
            ft.commit();
        }

    }

    /**
     * 返回上一页
     */
    @OnClick(R.id.tool_back)
    public void goBack() {
        finish();
    }


    //根据类型跳转到相应页面
    public void goSearchPage(String keyWord) {

        mSearchText.setText(keyWord);
        mSearchText.setSelection(keyWord.length());
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(searchFragment);
        ft.hide(mainFragment);
        ft.commit();
        if (searchFragment instanceof MyMaterialBoughtFragment) {
            searchFragment.searchWord(keyWord);
        }


    }

    /**
     * 初始化搜索页
     *
     * @param type 页面类型
     */
    private void initSearchListFragment(int type) {
        switch (type) {
            case 1:
                searchFragment = MyMaterialBoughtFragment.newInstance();
                break;
        }
    }
}
