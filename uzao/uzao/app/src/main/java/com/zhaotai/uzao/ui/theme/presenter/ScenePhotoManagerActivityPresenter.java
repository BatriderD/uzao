package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.Constant;
import com.zhaotai.uzao.bean.BaseResult;
import com.zhaotai.uzao.bean.PageInfo;
import com.zhaotai.uzao.bean.SceneManagerAlbumBean;
import com.zhaotai.uzao.bean.ScenePhotoManagerBean;
import com.zhaotai.uzao.bean.UploadFileBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.ScenePhotoManagerActivityContract;
import com.zhaotai.uzao.utils.ToastUtil;
import com.zhaotai.uzao.utils.bitmap.BitmapLoadUtils;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  场景相册Activity
 */

public class ScenePhotoManagerActivityPresenter extends ScenePhotoManagerActivityContract.Presenter {
    private ScenePhotoManagerActivityContract.View mView;
    private HashMap<String, String> params = new HashMap();

    public ScenePhotoManagerActivityPresenter(Context context, ScenePhotoManagerActivityContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    @Override
    public void changeSelectState(List<ScenePhotoManagerBean> data, boolean status) {
        for (ScenePhotoManagerBean bean : data) {
            bean.selected = status;

        }
    }

    @Override
    public void getPhotoData(final boolean isLoading, final int start, String albumId) {
        params.clear();
        params.put("albumId", albumId);
        params.put("start", String.valueOf(start));
        params.put("length", "15");
        Api.getDefault()
                .getMyManagerSceneAlbumList(params)
                .compose(RxHandleResult.<PageInfo<ScenePhotoManagerBean>>handleResult())
                .subscribe(new RxSubscriber<PageInfo<ScenePhotoManagerBean>>(mContext) {
                    @Override
                    public void _onNext(PageInfo<ScenePhotoManagerBean> data) {
                        if (isLoading && start == Constant.PAGING_HOME) {
                            mView.showContent();
                            mView.stopRefresh();
                            mView.stopLoadingMore();
                        } else {
                            mView.stopRefresh();
                            mView.stopLoadingMore();
                        }
                        mView.showManagerPhotoPageInfo(data);
                    }

                    @Override
                    public void _onError(String message) {
                        if (start == 0) {
                            mView.showNetworkFail(message);
                        } else {
                            mView.stopLoadingMore();
                        }
                    }
                });

    }

    /**
     * 上传本地图片到指定相册
     *
     * @param path 本地地址
     */
    @Override
    public void upLoadBitmap(String path, final String albumId) {


        Bitmap bitmap = BitmapLoadUtils.decodeSampledBitmapFromFile(path, 1024, 1024);
        if (bitmap == null) {
            ToastUtil.showShort("数据上传失败");
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String timeStamp = simpleDateFormat.format(new Date());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), bos.toByteArray());

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", timeStamp + ".JPEG", requestFile);

        Api.getDefault().uploadTheFile(body)
                .compose(RxHandleResult.<UploadFileBean>handleResultMap())
                .flatMap(new Function<UploadFileBean, ObservableSource<BaseResult<List<ScenePhotoManagerBean>>>>() {
                    @Override
                    public ObservableSource<BaseResult<List<ScenePhotoManagerBean>>> apply(UploadFileBean uploadFileBean) throws Exception {
                        ArrayList<UploadFileBean> list = new ArrayList<>();
                        list.add(uploadFileBean);
                        return Api.getDefault().upAlbumPhoto(albumId, list);
                    }
                }).compose(RxHandleResult.<List<ScenePhotoManagerBean>>handleResult())
                .subscribe(new RxSubscriber<List<ScenePhotoManagerBean>>(mContext, true) {
                    @Override
                    public void _onNext(List<ScenePhotoManagerBean> scenePhotoManagerBean) {
                        ToastUtil.showShort("上传成功！");
                        mView.showUploadSuccess();
                    }

                    @Override
                    public void _onError(String message) {
                        Log.d("上传失败", "_onError: " + message);
                        ToastUtil.showShort("上传失败！");
                    }
                });
    }

    private List<String> ids = new ArrayList<>();

    @Override
    public void deletePhoto(final String albumId, List<ScenePhotoManagerBean> data) {
        ids.clear();

        for (ScenePhotoManagerBean bean : data) {
            if (bean.selected) {
                ids.add(bean.getSequenceNBR());
            }
        }
        String[] strings = new String[ids.size()];
        if (ids.size() > 0) {
            Api.getDefault().deleteAlbumPhoto(albumId, ids.toArray(strings))
                    .compose(RxHandleResult.<String>handleResult())
                    .subscribe(new RxSubscriber<String>(mContext, true) {
                        @Override
                        public void _onNext(String s) {
                            mView.delSuccess();
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("删除失败");
                        }
                    });
        } else {
            ToastUtil.showShort("删除内容不能为空！");
        }
    }

    @Override
    public void changeAlbumName(String albumId, final String titleName) {
        SceneManagerAlbumBean sceneManagerAlbumBean = new SceneManagerAlbumBean(titleName);

        Api.getDefault()
                .EditorMyManagerSceneAlbum(albumId, sceneManagerAlbumBean)
                .compose(RxHandleResult.<SceneManagerAlbumBean>handleResult())
                .subscribe(new RxSubscriber<SceneManagerAlbumBean>(mContext) {
                    @Override
                    public void _onNext(SceneManagerAlbumBean data) {
                        mView.showChangeNameSuccess(titleName);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("相册修改失败");
                    }
                });
    }
}
