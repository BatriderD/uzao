package com.zhaotai.uzao.ui.theme.presenter;

import android.content.Context;

import com.zhaotai.uzao.api.Api;
import com.zhaotai.uzao.api.ApiConstants;
import com.zhaotai.uzao.bean.TemplateJsonBean;
import com.zhaotai.uzao.bean.ThemeBean;
import com.zhaotai.uzao.bean.ThemeModuleBean;
import com.zhaotai.uzao.bsaerx.RxHandleResult;
import com.zhaotai.uzao.bsaerx.RxSubscriber;
import com.zhaotai.uzao.ui.theme.contract.ThemeSettingContract;
import com.zhaotai.uzao.utils.GsonUtil;
import com.zhaotai.uzao.utils.LoginHelper;
import com.zhaotai.uzao.utils.StringUtil;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Time: 2018/1/19
 * Created by LiYou
 * Description :  主题设置presenter
 */

public class ThemeSettingPresenter extends ThemeSettingContract.Presenter {
    private ThemeSettingContract.View mView;

    public ThemeSettingPresenter(Context context, ThemeSettingContract.View mView) {
        this.mView = mView;
        mContext = context;
    }

    /**
     * 获取主题模块数据
     * @param themeId 主题id
     */
    @Override
    public void getThemeContent(String themeId) {
        Api.getDefault().getThemeContent(themeId)
                .compose(RxHandleResult.<List<ThemeModuleBean>>handleResult())
                .subscribe(new RxSubscriber<List<ThemeModuleBean>>(mContext) {
                    @Override
                    public void _onNext(List<ThemeModuleBean> themeModuleBeans) {
                        mView.showThemeContent(themeModuleBeans);
                    }

                    @Override
                    public void _onError(String message) {

                    }
                });
    }

    /**
     * 保存模块数据
     * @param themeId 模板id
     * @param position 下标
     * @param themeModuleBean 主题模块数据
     */
    @Override
    public void saveModule(String themeId, final int position, ThemeModuleBean themeModuleBean) {
        ThemeModuleBean upSaveModule = new ThemeModuleBean();
        upSaveModule.setPartNo(String.valueOf(position));
        upSaveModule.setDescription(themeModuleBean.getDescription());
        upSaveModule.setThemeId(themeId);
        ArrayList<ThemeModuleBean.ThemeContentModel> upModule = new ArrayList<>();
        List<ThemeModuleBean.ThemeContentModel> themeContentModels = themeModuleBean.getThemeContentModels();
        for (int i = 0; i < themeContentModels.size(); i++) {
            ThemeModuleBean.ThemeContentModel themeContentModel = themeContentModels.get(i);
            String entityType = themeContentModel.getEntityType();
            if (!StringUtil.isEmpty(entityType)) {
                ThemeModuleBean.ThemeContentModel newModule = new ThemeModuleBean.ThemeContentModel();
                newModule.setEntityType(themeContentModel.getEntityType());
                newModule.setEntityId(themeContentModel.getEntityId());
                newModule.setOrderNum(String.valueOf(i));
                upModule.add(newModule);
            }
        }
        upSaveModule.setThemeContentModels(upModule);

        if (StringUtil.isEmpty(themeModuleBean.getPartNo())) {
            String s = GsonUtil.t2Json2(upSaveModule);
            System.out.println("保存的对象json" + s);
            Api.getDefault().saveThemeContent(upSaveModule)
                    .compose(RxHandleResult.<ThemeModuleBean>handleResult())
                    .subscribe(new RxSubscriber<ThemeModuleBean>(mContext, true) {
                        @Override
                        public void _onNext(ThemeModuleBean themeModuleBean) {
                            mView.showSaveModuleSuccess(position);
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("保存失败");
                        }
                    });
        } else {
            //修改
            String s = GsonUtil.t2Json2(upSaveModule);
            System.out.println("修改的的对象json" + s);
            Api.getDefault().changeThemeContent(upSaveModule)
                    .compose(RxHandleResult.<ThemeModuleBean>handleResult())
                    .subscribe(new RxSubscriber<ThemeModuleBean>(mContext, true) {
                        @Override
                        public void _onNext(ThemeModuleBean themeModuleBean) {
                            mView.showSaveModuleSuccess(position);
                        }

                        @Override
                        public void _onError(String message) {
                            ToastUtil.showShort("保存失败");
                        }
                    });
        }


    }

    @Override
    public void delModule(String themeId, final int position) {
        Api.getDefault().delThemeContent(themeId, String.valueOf(position))
                .compose(RxHandleResult.<String>handleResult())
                .subscribe(new RxSubscriber<String>(mContext, true) {
                    @Override
                    public void _onNext(String themeModuleBean) {
                        mView.showDeleteSuccess(position);
                    }

                    @Override
                    public void _onError(String message) {
                        ToastUtil.showShort("删除失败");
                    }
                });

    }

    /**
     * 创建一个主题模块
     * @return 主题模块实体类
     */
    public ThemeModuleBean createThemeModule() {
        ThemeModuleBean themeModule = new ThemeModuleBean();
        ArrayList<ThemeModuleBean.ThemeContentModel> contentModels = new ArrayList<>();
        ThemeModuleBean.ThemeContentModel themeContentModel = new ThemeModuleBean.ThemeContentModel();
        contentModels.add(themeContentModel);
        themeModule.setThemeContentModels(contentModels);
        return themeModule;
    }

    /**
     * 解析处理主题信息
     * @param themeBean  主题bean
     * @param themeId 主题id
     * @param data  主题模块信息
     */
    @Override
    public void resultData(ThemeBean themeBean, String themeId, List<ThemeModuleBean> data) {
        //主题模块不能不能为空
        if (data == null || data.size() == 0) {
            ToastUtil.showShort("主题模块不能为空");
            mView.showCollectFailed();
            return;
        }
        //主题主题模块必须保存状态
        for (ThemeModuleBean themeModuleBean : data) {
            if (themeModuleBean.isCanBeEdit()) {
                ToastUtil.showShort("请保存主题模块");
                mView.showCollectFailed();
                return;
            }
        }

        // 重新构建主题模块 模块的列表
        ArrayList<ThemeModuleBean> upSaveModuleList = new ArrayList<>();
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                //模块数据
                ThemeModuleBean themeModuleBean = data.get(i);
                //新建
                ThemeModuleBean upSaveModule = new ThemeModuleBean();
                //处理主题模块list数据 和顺序
                upSaveModule.setPartNo(String.valueOf(i));
                upSaveModule.setDescription(themeModuleBean.getDescription());
                upSaveModule.setThemeId(themeId);
                //模块里面的素材和商品
                ArrayList<ThemeModuleBean.ThemeContentModel> upModule = new ArrayList<>();
                List<ThemeModuleBean.ThemeContentModel> themeContentModels = themeModuleBean.getThemeContentModels();

                for (int j = 0; j < themeContentModels.size(); j++) {
                    ThemeModuleBean.ThemeContentModel themeContentModel = themeContentModels.get(j);
                    String entityType = themeContentModel.getEntityType();
                    if (!StringUtil.isEmpty(entityType)) {
                        ThemeModuleBean.ThemeContentModel newModule = new ThemeModuleBean.ThemeContentModel();
                        newModule.setEntityType(themeContentModel.getEntityType());
                        newModule.setEntityId(themeContentModel.getEntityId());
                        newModule.setOrderNum(String.valueOf(j));
                        newModule.setEntityPic(themeContentModel.getEntityPic());
                        newModule.setEntityPriceY(themeContentModel.getEntityPriceY());
                        upModule.add(newModule);
                    }
                }
                upSaveModule.setThemeContentModels(upModule);

                upSaveModuleList.add(upSaveModule);
            }


        }

        //创建TemplateJsonBean的实体类
        TemplateJsonBean templateJsonBean = new TemplateJsonBean();
        templateJsonBean.creator = new TemplateJsonBean.Creator(ApiConstants.UZAOCHINA_IMAGE_HOST +LoginHelper.getAvatar(),  LoginHelper.getUserName());

        templateJsonBean.themeHeader = new TemplateJsonBean.ThemeHeader();
        templateJsonBean.themeHeader.name = themeBean.name;
        templateJsonBean.themeHeader.intro = themeBean.description;
        templateJsonBean.themeHeader.thumbnail = ApiConstants.THEME_TEMPLATE + themeBean.cover;
        templateJsonBean.themeHeader.tags =themeBean.tags;
        templateJsonBean.themeHeader.favoriteCounts = themeBean.favoriteCount;
        templateJsonBean.themeHeader.commentCounts = themeBean.commentCount;


        templateJsonBean.themeContent = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            TemplateJsonBean.ThemeContent themeContent = new TemplateJsonBean.ThemeContent();
            ThemeModuleBean themeModuleBean = data.get(i);
            themeContent.text = themeModuleBean.getDescription();
            themeContent.elem = new ArrayList<>();
            List<ThemeModuleBean.ThemeContentModel> themeContentModels = themeModuleBean.getThemeContentModels();
            for (int j = 0; j < themeContentModels.size(); j++) {
                ThemeModuleBean.ThemeContentModel themeContentModel = themeContentModels.get(j);
                if (themeContentModel == null) {
                    continue;
                }
                TemplateJsonBean.Elem elem = new TemplateJsonBean.Elem();
                elem.id = themeContentModel.getEntityId();
                elem.type = themeContentModel.getEntityType();
                elem.thumbnail = ApiConstants.UZAOCHINA_IMAGE_HOST + themeContentModel.getEntityPic();
                elem.name = themeContentModel.getEntityName();
                elem.buyCounts = themeContentModel.getBuyCounts();
                elem.viewCounts = themeContentModel.getViewCounts();

                themeContent.elem.add(elem);
            }
            templateJsonBean.themeContent.add(themeContent);
        }
        //数据收集成功
        mView.collectDataSuccess(templateJsonBean, upSaveModuleList);
    }
}
