package com.meitu.richtext.span;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/**
 * 可改变透明度的ForegroundColorSpan
 * @author Ljq 2017/12/6
 */
public class MutableForegroundColorSpan extends ForegroundColorSpan{
    private int mAlpha = 255;
    private int mForegroundColor;

    public MutableForegroundColorSpan(int alpha, int color) {
        super(color);
        mAlpha = alpha;
        mForegroundColor = color;
    }

    public MutableForegroundColorSpan(Parcel src) {
        super(src);
        mForegroundColor = src.readInt();
        mAlpha = src.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(mForegroundColor);
        dest.writeFloat(mAlpha);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(getForegroundColor());
    }

    /**
     * 设置透明度
     * @param alpha from 0 to 255
     */
    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    public void setForegroundColor(int foregroundColor) {
        mForegroundColor = foregroundColor;
    }

    public float getAlpha() {
        return mAlpha;
    }

    @Override
    public int getForegroundColor() {
        return Color.argb(mAlpha, Color.red(mForegroundColor), Color.green(mForegroundColor), Color.blue(mForegroundColor));
    }
}