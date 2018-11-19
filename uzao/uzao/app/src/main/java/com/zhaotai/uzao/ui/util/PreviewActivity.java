package com.zhaotai.uzao.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.zhaotai.uzao.R;
import com.zhaotai.uzao.utils.GlideLoadImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2017/6/15
 * Created by LiYou
 * Description : 图片预览
 */

public class PreviewActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private TextView num_tv;
    public List<String> imageList;
    private int page;
    private int size;

    private static final String EXTRA_KEY_IMAGE_LIST = "extra_key_image_list";
    private static final String EXTRA_KEY_CLICKED_PAGE = "extra_key_clicked_page";
    private static final String EXTRA_KEY_IMAGE_PATH = "extra_key_image_path";

    /**
     * @param context     上下文
     * @param imageList   图片数据
     * @param clickedPage 点击位置
     * @param imagePath   图片服务器位置
     */
    public static void launch(Context context, ArrayList<String> imageList, int clickedPage, String imagePath) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_KEY_IMAGE_LIST, imageList);
        intent.putExtra(EXTRA_KEY_CLICKED_PAGE, clickedPage);
        intent.putExtra(EXTRA_KEY_IMAGE_PATH, imagePath);
        context.startActivity(intent);
    }

    private static final String OPTION_IMAGE = "image";

    public static void StartOptionsActivity(AppCompatActivity activity, View transitionView, ArrayList<String> imageList, int clickedPage, String imagePath) {
        Intent intent = new Intent(activity, PreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_KEY_IMAGE_LIST, imageList);
        intent.putExtra(EXTRA_KEY_CLICKED_PAGE, clickedPage);
        intent.putExtra(EXTRA_KEY_IMAGE_PATH, imagePath);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView,  OPTION_IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.preview_vp);

        num_tv = (TextView) findViewById(R.id.num_tv);

        mViewPager.addOnPageChangeListener(this);


    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        if (intent.getStringArrayListExtra(EXTRA_KEY_IMAGE_LIST) != null) {
            imageList = intent.getStringArrayListExtra(EXTRA_KEY_IMAGE_LIST);
            num_tv.setText(1 + "/" + imageList.size());
            mViewPager.setOffscreenPageLimit(imageList.size());
            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter();
            mViewPager.setAdapter(mViewPagerAdapter);
            size = imageList.size();
            mViewPagerAdapter.notifyDataSetChanged();
        }
        page = intent.getIntExtra(EXTRA_KEY_CLICKED_PAGE, 1);
        mViewPager.setCurrentItem(page); //position为-1
        ViewCompat.setTransitionName(mViewPager, OPTION_IMAGE);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        page = position + 1;
        num_tv.setText(page + "/" + size);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageList != null ? imageList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(PreviewActivity.this).inflate(R.layout.item_preview_img, null);
            PhotoView mView = (PhotoView) view.findViewById(R.id.preview_imgview);
            String image = imageList.get(position);
            ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();
            layoutParams.height = getWindowManager().getDefaultDisplay().getHeight();
            mView.setLayoutParams(layoutParams);
            GlideLoadImageUtil.load(PreviewActivity.this, getIntent().getStringExtra(EXTRA_KEY_IMAGE_PATH) + image, mView);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    public void finish() {
        super.finish();

    }
}
