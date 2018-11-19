package com.xiaopo.flying.sticker;

import android.content.Context;
import android.util.AttributeSet;

import java.util.List;

/**
 * Sticker View
 *
 * @author wupanjie
 */
public class FixStickerView extends StickerView {

    public FixStickerView(Context context) {
        super(context);
    }

    public FixStickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixStickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Sticker getHandingSticker() {
        return handlingSticker;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public void moveSticker(int oldPos, int newPos) {
        List<Sticker> stickers = getStickers();
        if (oldPos == newPos) return;
        Sticker remove = stickers.remove(oldPos);
        stickers.add(newPos, remove);
        invalidate();
    }
}