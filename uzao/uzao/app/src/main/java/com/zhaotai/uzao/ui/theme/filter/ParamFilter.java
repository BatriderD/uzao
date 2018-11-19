package com.zhaotai.uzao.ui.theme.filter;

import java.util.HashMap;

/**
 * description: 筛选参数控制类
 * author : ZP
 * date: 2018/1/30 0030.
 */

public class ParamFilter {
    /**
     * 构造筛选条件
     */
    public HashMap<String, String> createBaseParam(HashMap<String, String> params, int start, String sort, String categoryCode1) {
        params.put("start", String.valueOf(start));
        params.put("needOption", start == 0 ? "Y" : "N");
        //默认排序
        params.put("sort", sort);
        params.put("categoryCode1", categoryCode1);
        return params;
    }

    /**
     * 构造筛选调教
     */
    public HashMap<String, String> createBaseParam(int start, String categoryCode1) {
        HashMap<String, String> params = new HashMap<>();
        return createBaseParam(params, start, "default_", categoryCode1);
    }
}
