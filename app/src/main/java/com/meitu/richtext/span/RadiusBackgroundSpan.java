package com.meitu.richtext.span;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.util.Log;

import com.meitu.richtext.common.Constant;
import com.meitu.richtext.interfaces.ITouch;

/**
 * 外链icon、背景绘制
 *
 * @author Ljq 2017/12/6
 */
public class RadiusBackgroundSpan extends ReplacementSpan implements ITouch {

    private static final String TAG = RadiusBackgroundSpan.class.getSimpleName();
    private static final int BACKGROUND_SPACE = 5; //背景边缘宽度
    private int mSize;
    private int mColor;
    private int mRadius;
    private Bitmap mBitmap;
    private float mIconMaxSize;
    private int mAlpha = 255;

    /**
     * @param radius 圆角半径
     * @param color  背景颜色
     * @author Ljq 2017/12/6
     */
    public RadiusBackgroundSpan(int color, int radius) {
        mColor = color;
        mRadius = radius;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius); //文本长度
        return mSize;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int color = paint.getColor();//保存文字颜色
        paint.setColor(Color.argb(mAlpha, Color.red(mColor), Color.green(mColor), Color.blue(mColor)));//设置背景颜色
        paint.setAntiAlias(true);// 设置画笔的锯齿效果

        RectF oval = new RectF(x - 5, y + paint.ascent() - BACKGROUND_SPACE, x + mSize, y + paint.descent() + BACKGROUND_SPACE);
        canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形
        if (mBitmap != null) {
            mIconMaxSize = paint.getTextSize();
            //绘制icon
            int iconTop = (int) ((oval.height() - mIconMaxSize) / 2 + oval.top);//计算icon的坐标使其居中
            Log.d(TAG, "iconTop:" + iconTop);
            canvas.drawBitmap(mBitmap, new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight()), new RectF(x, iconTop, x + mIconMaxSize, iconTop + mIconMaxSize), null);
        }
        paint.setColor(Color.argb(mAlpha, Color.red(color), Color.green(color), Color.blue(color)));//恢复画笔的文字颜色
        canvas.drawText(text, start, end, x + mRadius, y, paint);//绘制文字

    }

    public void setAlpha(int alpha) {
        Log.d(TAG, "setAlpha:" + alpha);
        mAlpha = alpha;
    }

    public int getAlpha() {
        return mAlpha;
    }

    /**
     * 设置icon
     *
     * @author Ljq 2017/12/6
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public void onTouchDown() {
        setAlpha((int) (255 * Constant.PRESS_ALPHA));
    }

    @Override
    public void onTouchUp() {
        setAlpha(255);
    }

}