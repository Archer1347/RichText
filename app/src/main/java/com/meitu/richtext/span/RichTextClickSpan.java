package com.meitu.richtext.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.meitu.richtext.interfaces.ITouch;
import com.meitu.richtext.interfaces.RichTextClickListener;

/**
 * 文本点击监听
 *
 * @author Ljq 2017/12/5
 */
public class RichTextClickSpan extends ClickableSpan {
    private RichTextClickListener listener;
    private String content;
    private ITouch mTouch;


    public RichTextClickSpan(RichTextClickListener listener, String content, ITouch touch) {
        this.listener = listener;
        this.content = content;
        mTouch = touch;
    }

    @Override
    public void onClick(View widget) {
        if (listener != null) {
            listener.onRichTextClick((TextView) widget, content);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);//去除下划线
    }

    public ITouch getTouch() {
        return mTouch;
    }
}