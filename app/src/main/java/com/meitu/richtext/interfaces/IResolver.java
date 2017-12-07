package com.meitu.richtext.interfaces;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.widget.TextView;

/**
 * 富文本样式接口
 * @author Ljq 2017/12/6
 */
public interface IResolver {

    void resolve(TextView textView, SpannableStringBuilder sp, SparseArray<Object> extra, RichTextClickListener listener);
}
