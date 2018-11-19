package com.zhaotai.uzao.ui.design.utils;

import android.support.annotation.NonNull;

import com.xiaopo.flying.sticker.StickerDataBean;
import com.zhaotai.uzao.global.GlobalVariable;
import com.zhaotai.uzao.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 保存镜像工具类
 * author : ZP
 * date: 2018/4/9 0009.
 */

public class StickerViewStepUtils {
    private int maxSize = GlobalVariable.MAX_STEP;
    private List<List<StickerDataBean>> dataBeanList = new ArrayList<>();

    public StickerViewStepUtils(int maxSize) {
        this.maxSize = maxSize;
    }


    public void saveStep(@NonNull List<StickerDataBean> step) {
        if (dataBeanList.size() == GlobalVariable.MAX_STEP + 1) {
            //保存指定步数
            dataBeanList.remove(0);
        }
        dataBeanList.add(step);
    }

    public List<StickerDataBean> getStep() {
        if (!dataBeanList.isEmpty()) {
            int saveStep = dataBeanList.size() - 1;
            if (saveStep - 1 < 0) {
                ToastUtil.showShort("不能再后退了");
                return null;
            } else {
                dataBeanList.remove(saveStep);
                return dataBeanList.get(saveStep - 1);
            }

        } else {
            return null;
        }

    }

    public boolean isEmpty() {
        return dataBeanList.size() - 2 < 0;
    }
}
