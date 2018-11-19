package com.zhaotai.uzao.ui.post.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.SceneManagerPostBean;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.post.contract.PublishPostContract;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

/**
 * Time: 2018/7/27 0027
 * Created by LiYou
 * Description :
 */
public class PublishPostPresenter extends PublishPostContract.Presenter {

    private PublishPostContract.View mView;

    public PublishPostPresenter(PublishPostContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    /**
     * 发布帖子
     *
     * @param title   标题
     * @param html    内容
     * @param themeId 场景id
     */
    public void post(String title, String html, String themeId) {
        if (StringUtil.isEmpty(title)) {
            ToastUtil.showShort("请填写标题");
            return;
        }
        if (StringUtil.isEmpty(html)) {
            ToastUtil.showShort("请填写帖子内容");
            return;
        }
        SceneManagerPostBean postBean = new SceneManagerPostBean();
        postBean.setContentBody(html);
        postBean.setTitle(title);
        Api.getDefault().publishPost(themeId, postBean)
                .compose(RxHandleResult.<SceneManagerPostBean>handleResult())
                .subscribe(new RxSubscriber<SceneManagerPostBean>(mContext, true) {
                    @Override
                    public void _onNext(SceneManagerPostBean postBean) {
                        ToastUtil.showShort("发帖成功");
                        mView.finishView(postBean);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("发帖失败");
                    }
                });
    }

    /**
     * 修改帖子
     */
    public void modifyPost(String title, String html, String themeId, String _id) {
        if (StringUtil.isEmpty(title)) {
            ToastUtil.showShort("请填写标题");
            return;
        }
        if (StringUtil.isEmpty(html)) {
            ToastUtil.showShort("请填写帖子内容");
            return;
        }
        SceneManagerPostBean postBean = new SceneManagerPostBean();
        postBean.setContentBody(html);
        postBean.setTitle(title);
        postBean.setThemeId(themeId);
        postBean.id = _id;
        Api.getDefault().modifyPost(postBean)
                .compose(RxHandleResult.<SceneManagerPostBean>handleResult())
                .subscribe(new RxSubscriber<SceneManagerPostBean>(mContext, true) {
                    @Override
                    public void _onNext(SceneManagerPostBean postBean) {
                        ToastUtil.showShort("编辑帖子成功");
                        mView.finishView(postBean);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort(message);
                    }
                });
    }

    /**
     * 上传图片
     *
     * @param files 图片文件
     */
    private void uploadImages(List<File> files) {
        Api.getDefault().uploadTheFiles(files2Parts(files))
                .compose(RxHandleResult.<List<UploadFileBean>>handleResult())
                .subscribe(new RxSubscriber<List<UploadFileBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<UploadFileBean> o) {
                        for (UploadFileBean fileBean : o) {
                            mView.insertImage(ApiConstants.UZAOCHINA_IMAGE_HOST + fileBean.fileId);
                        }
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    private ArrayList<MultipartBody.Part> files2Parts(List<File> files) {
        ArrayList<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            // 根据类型及File对象创建RequestBody（okhttp的类）
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // 将RequestBody封装成MultipartBody.Part类型（同样是okhttp的）
            MultipartBody.Part part = MultipartBody.Part.
                    createFormData("files", file.getName(), requestBody);
            // 添加进集合
            parts.add(part);
        }
        return parts;
    }


    /**
     * 压缩图片
     *
     * @param paths 图片地址
     */
    @SuppressLint("CheckResult")
    public void compressImage(final List<String> paths) {
        Flowable.just(paths)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(List<String> strings) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(mContext).load(strings).get();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        uploadImages(files);
                    }
                });
    }
}
