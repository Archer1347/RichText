package com.meitu.richtext.rich;


import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.meitu.richtext.span.RichTextClickSpan;

/**
 * RTMovementMethod is used instead of using LinkMovementMethod is for respond cleverly to click.
 * Limit up-down duration to avoid unexpected click response.
 *
 * @author Bingding.
 */
public class RTMovementMethod extends LinkMovementMethod {

    private long mLastTouchTime;
    private static final long DELAY_TIME = 500l;
    private ClickableSpan mSelectSpan;

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);
        if (isOutsideSpan(widget, x, y, line)) {
            if (mSelectSpan != null) {
                touchUp(mSelectSpan);
                mSelectSpan = null;
            }
            return true;
        }

        ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

        if (link.length != 0) {
            if (action == MotionEvent.ACTION_UP) {
                if (mSelectSpan == null) {
                    return true;
                }
                link[0].onClick(widget);
                touchUp(link[0]);
                mSelectSpan = null;
            } else if (action == MotionEvent.ACTION_DOWN) {
                touchDown(link[0]);
                mSelectSpan = link[0];
                Selection.setSelection(buffer,
                        buffer.getSpanStart(link[0]),
                        buffer.getSpanEnd(link[0]));
            } else {
                if (mSelectSpan != null && mSelectSpan != link[0]) {
                    touchUp(mSelectSpan);
                    mSelectSpan = null;
                }
            }
            return true;
        } else {
            if (mSelectSpan != null) {
                touchUp(mSelectSpan);
                mSelectSpan = null;
            }
            Selection.removeSelection(buffer);
        }

        return super.onTouchEvent(widget, buffer, event);
    }


    /**
     * 判断是否点击或者移出点击的span范围
     *
     * @param widget
     * @param x      ：触摸点x坐标
     * @param y      ：触摸点y坐标
     * @param line   ：触摸的行数
     * @author Ljq 2017/12/7
     */
    private boolean isOutsideSpan(TextView widget, int x, int y, int line) {
        //y坐标上超出view的范围
        Layout layout = widget.getLayout();
        if (y < layout.getLineTop(0) || y > layout.getLineTop(layout.getLineCount())) {
            return true;
        }
        //x坐标上超出view的范围
        if (x > widget.getMeasuredWidth() || x < 0) {
            return true;
        }
        //超出最后一行x坐标的范围
        if (line == widget.getLineCount() - 1) {
            if (x > widget.getLayout().getLineWidth(line)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 触摸
     *
     * @author Ljq 2017/12/7
     */
    private void touchDown(ClickableSpan link) {
        if (link instanceof RichTextClickSpan) {
            RichTextClickSpan rechTextClickSpan = (RichTextClickSpan) link;
            if (rechTextClickSpan.getTouch() != null)
                rechTextClickSpan.getTouch().onTouchDown();
        }
    }

    /**
     * 离开触摸状态
     *
     * @author Ljq 2017/12/7
     */
    private void touchUp(ClickableSpan link) {
        if (link instanceof RichTextClickSpan) {
            RichTextClickSpan rechTextClickSpan = (RichTextClickSpan) link;
            if (rechTextClickSpan.getTouch() != null)
                rechTextClickSpan.getTouch().onTouchUp();
        }
    }


    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new RTMovementMethod();

        return sInstance;
    }

    private static LinkMovementMethod sInstance;


}
