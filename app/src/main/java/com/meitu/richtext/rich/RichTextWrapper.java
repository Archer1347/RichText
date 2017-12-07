package com.meitu.richtext.rich;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.meitu.richtext.interfaces.IResolver;
import com.meitu.richtext.interfaces.RichTextClickListener;

import java.util.HashMap;


/**
 * 富文本封装
 *
 * @author Ljq 2017/12/6
 */
public class RichTextWrapper {

    private HashMap<Class<? extends IResolver>, IResolver> mResolvers = new HashMap<>();
    private ArrayMap<String, RichTextClickListener> mListenerMap = new ArrayMap<>();
    private TextView mTextView;
    private SparseArray<Object> mExtra = new SparseArray<>();
    private SpannableStringBuilder mSpan;

    public RichTextWrapper(TextView textView) {
        if (textView == null) {
            return;
        }
        mTextView = textView;
        mTextView.setHighlightColor(textView.getResources().getColor(android.R.color.transparent));
        textView.setMovementMethod(RTMovementMethod.getInstance());

    }

    public void addResolver(Class<? extends IResolver>... clazzs) {
        for (Class<? extends IResolver> clazz : clazzs) {
            if (!mResolvers.containsKey(clazz)) {
                mResolvers.put(clazz, null);
            }
        }
    }

    public void setOnRichTextListener(Class<? extends IResolver> clazz, RichTextClickListener listener) {
        if (!mResolvers.containsKey(clazz)) {
            mResolvers.put(clazz, null);
        }
        mListenerMap.put(clazz.getSimpleName(), listener);
    }


    private void resolveText() {
        if (mSpan == null) {
            mSpan = new SpannableStringBuilder(mTextView.getText().toString());
            mTextView.setText(mSpan);
        }
//        if (!(mTextView.getText() instanceof Spannable)) {
//            mTextView.setText(new SpannableString(mTextView.getText()));
//        }
//        Spannable sp = (Spannable) mTextView.getText();
        for (Class<? extends IResolver> clazz : mResolvers.keySet()) {
            IResolver resolver = mResolvers.get(clazz);
            if (resolver == null) {
                try {
                    resolver = clazz.newInstance();
                    mResolvers.put(clazz, resolver);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    continue;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            RichTextClickListener listener = mListenerMap.get(resolver.getClass().getSimpleName());
            resolver.resolve(mTextView, mSpan, mExtra, listener);
            mTextView.setText(mSpan);
        }
    }

    public void putExtra(int key, Object value) {
        mExtra.put(key, value);
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
        resolveText();
    }
}
