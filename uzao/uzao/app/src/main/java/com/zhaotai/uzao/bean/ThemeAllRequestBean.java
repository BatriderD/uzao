package com.zhaotai.uzao.bean;

import java.util.List;

/**
 * description:
 * author : ZP
 * date: 2018/2/5 0005.
 */

public class ThemeAllRequestBean {

    public ThemeAllRequestBean(String templateId, String templateJson, List<ThemeModuleBean> themeContentModels) {
        this.templateId = templateId;
        this.templateJson = templateJson;
        this.themePartModels = themeContentModels;
    }

    private String templateId;
    private String templateJson;

    public List<ThemeModuleBean> getThemeContentModels() {
        return themePartModels;
    }

    public void setThemeContentModels(List<ThemeModuleBean> themeContentModels) {
        this.themePartModels = themeContentModels;
    }

    private List<ThemeModuleBean> themePartModels;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateJson() {
        return templateJson;
    }

    public void setTemplateJson(String templateJson) {
        this.templateJson = templateJson;
    }
}
