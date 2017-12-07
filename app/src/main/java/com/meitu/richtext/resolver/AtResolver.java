package com.meitu.richtext.resolver;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.widget.TextView;

import com.meitu.richtext.common.Constant;
import com.meitu.richtext.info.TaggedInfo;
import com.meitu.richtext.interfaces.IResolver;
import com.meitu.richtext.interfaces.ITouch;
import com.meitu.richtext.interfaces.RichTextClickListener;
import com.meitu.richtext.rich.RichTexts;
import com.meitu.richtext.span.MutableForegroundColorSpan;
import com.meitu.richtext.span.RichTextClickSpan;

/**
 * 富文本@
 *
 * @author Ljq 2017/12/6
 */
public class AtResolver implements IResolver {

    @Override
    public void resolve(final TextView textView, final SpannableStringBuilder sp, SparseArray<Object> extra, RichTextClickListener listener) {
        String s = textView.getText().toString();
        int index = 0;
        while (index >= 0) {
            index = s.indexOf("@", index);
            if (index < 0) {
                return;
            }
            int nextIndex = getNextIndex(s, index + 1);

            String content = s.substring(index, nextIndex);
            final TaggedInfo info = new TaggedInfo(index, nextIndex, content);

            final MutableForegroundColorSpan foregroundColorSpan = new MutableForegroundColorSpan(255, Color.BLUE);
            if (listener != null) {
                RichTextClickSpan span = new RichTextClickSpan(listener, info.content, new ITouch() {
                    @Override
                    public void onTouchDown() {
                        foregroundColorSpan.setAlpha((int) (255 * Constant.PRESS_ALPHA));
                        RichTexts.setColorSpan(sp, info, foregroundColorSpan);
                        textView.setText(sp);
                    }

                    @Override
                    public void onTouchUp() {
                        foregroundColorSpan.setAlpha(255);
                        RichTexts.setColorSpan(sp, info, foregroundColorSpan);
                        textView.setText(sp);
                    }
                });
                RichTexts.setRichTextClickSpan(sp, info, span);
            }
            RichTexts.setColorSpan(sp, info, foregroundColorSpan);
            index = nextIndex;
        }
    }

    /**
     * 获取一个@字符串的的终点（下一个空格/@/#）
     *
     * @author Ljq 2017/12/6
     */
    public int getNextIndex(String s, int fromIndex) {
        int indexSpace = s.indexOf(" ", fromIndex + 1);
        int indexAt = s.indexOf("@", fromIndex + 1);
        int indexTopic = s.indexOf("#", fromIndex + 1);
        int indexLine = s.indexOf("\n", fromIndex + 1);
        int nextIndex = s.length();
        if (indexSpace >= 0) {
            nextIndex = indexSpace;
        }
        if (indexAt >= 0) {
            nextIndex = Math.min(nextIndex, indexAt);
        }
        if (indexTopic >= 0) {
            nextIndex = Math.min(nextIndex, indexTopic);
        }
        return nextIndex;
    }
}
