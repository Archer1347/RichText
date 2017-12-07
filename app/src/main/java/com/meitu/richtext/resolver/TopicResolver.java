package com.meitu.richtext.resolver;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.util.Log;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本话题
 *
 * @author Ljq 2017/12/6
 */
public class TopicResolver implements IResolver {

    private static final String TAG = TopicResolver.class.getSimpleName();
    public static Pattern PATTERN = Pattern.compile(Constant.TOPIC_MATCH_REGULAR);

    @Override
    public void resolve(final TextView textView, final SpannableStringBuilder sp, SparseArray<Object> extra, RichTextClickListener listener) {
        Log.e(TAG, "resolve");
        Matcher matcher = PATTERN.matcher(sp);
        while (matcher.find()) {
            String content = matcher.group();

            final TaggedInfo info = new TaggedInfo(matcher.start(), matcher.end(), content);

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
        }
    }

}
