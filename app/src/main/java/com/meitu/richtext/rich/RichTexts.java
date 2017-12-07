package com.meitu.richtext.rich;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.meitu.richtext.info.TaggedInfo;
import com.meitu.richtext.span.MutableForegroundColorSpan;
import com.meitu.richtext.span.RichTextClickSpan;

/**
 * 设置文本样式
 * @author Ljq 2017/12/6
 */
public class RichTexts {

    private static final String TAG = RichTexts.class.getSimpleName();

    public static void setImageSpan(Spannable s, TaggedInfo info, Context context, int resourceId) {
        removeSpans(s, info.start, info.end, ImageSpan.class);
        safelySetSpan(s, new ImageSpan(context, resourceId, DynamicDrawableSpan.ALIGN_BASELINE), info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setImageSpan(Spannable s, TaggedInfo info, DynamicDrawableSpan span) {
        removeSpans(s, info, ImageSpan.class);
        safelySetSpan(s, span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setRichTextClickSpan(Spannable s, TaggedInfo info, RichTextClickSpan span) {
        removeSpans(s, info, RichTextClickSpan.class);
        safelySetSpan(s, span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setColorSpan(Spannable s, TaggedInfo info, MutableForegroundColorSpan span) {
        removeSpans(s, info, MutableForegroundColorSpan.class);
        safelySetSpan(s, span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public static void setBackgroundSpan(Spannable s, TaggedInfo info, ReplacementSpan span) {
        removeSpans(s, info, ReplacementSpan.class);
        safelySetSpan(s, span, info.start, info.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private static void safelySetSpan(Spannable s, Object what, int start, int end, int flags) {
        if ((start >= 0) && (end >= start) && (end <= s.length())) {
            s.setSpan(what, start, end, flags);
        } else {
            Log.e(TAG, "fail set spain,start:" + start + ",end:" + end + ",but lng is:" + s.length());
        }
    }

    public static <T> void removeSpans(Spannable s, TaggedInfo info, Class<T> clz) {
        removeSpans(s, info.start, info.end, clz);
    }

    public static <T> void removeSpans(Spannable s, int start, int end, Class<T> clz) {
        T[] spans = s.getSpans(start, end, clz);
        if (spans != null) {
            for (T span : spans) {
                try {
                    s.removeSpan(span);
                    Log.e(TAG,"remove");
                } catch (NullPointerException e) {
                    Log.e(TAG, "remove spans error", e);
                }
            }
        }
    }

}
