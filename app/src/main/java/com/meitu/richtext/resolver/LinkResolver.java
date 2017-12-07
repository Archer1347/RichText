package com.meitu.richtext.resolver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.DynamicDrawableSpan;
import android.util.SparseArray;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.meitu.richtext.R;
import com.meitu.richtext.common.Constant;
import com.meitu.richtext.info.LinkInfo;
import com.meitu.richtext.info.TaggedInfo;
import com.meitu.richtext.interfaces.IResolver;
import com.meitu.richtext.interfaces.ITouch;
import com.meitu.richtext.interfaces.RichTextClickListener;
import com.meitu.richtext.rich.RichTexts;
import com.meitu.richtext.span.RadiusBackgroundSpan;
import com.meitu.richtext.span.RichTextClickSpan;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 外链
 *
 * @author Ljq 2017/12/5
 */
public class LinkResolver implements IResolver {

    private static final String TAG = LinkResolver.class.getSimpleName();
    public static final int KEY_DATA = 1001;
    public static Pattern PATTERN = Pattern.compile(Constant.URL_MATCH_REGULAR);
    private SpannableStringBuilder mSp;
    private ArrayList<LinkInfo> mLinkList;
    private String mStr;

    @Override
    public void resolve(final TextView textView, SpannableStringBuilder sp, SparseArray<Object> extra, final RichTextClickListener listener) {
        mLinkList = (ArrayList<LinkInfo>) extra.get(KEY_DATA);
        if (mLinkList == null || mLinkList.size() == 0)
            return;
        mSp = sp;
        mStr = textView.getText().toString();
        Matcher matcher = PATTERN.matcher(mStr);
        while (matcher.find()) {
            String content = matcher.group();
            LinkInfo linkInfo = getLinkInfo(content);
            if (linkInfo != null) {
                linkInfo.setTitle("\u3000" + linkInfo.getTitle()); //增加缩进用于放置icon
                mStr = mStr.replaceAll(content, linkInfo.getTitle()); //替换原始文本的url
                mSp = sp.replace(matcher.start(), matcher.end(), linkInfo.getTitle());//替换spannable的url

                final TaggedInfo backgroundInfo = new TaggedInfo(matcher.start(), matcher.start() + linkInfo.getTitle().length(), linkInfo.getUrl());
                final RadiusBackgroundSpan backgroundSpan = new RadiusBackgroundSpan(linkInfo.getBackground(), Constant.ICON_CORNER_SIZE);
                //先设置一个默认icon
                backgroundSpan.setBitmap(BitmapFactory.decodeResource(textView.getResources(), R.drawable.ic_launcher));

                RichTexts.setBackgroundSpan(mSp, backgroundInfo, backgroundSpan);
                Glide.with(textView.getContext()).load(linkInfo.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //下载完icon重新设置span
                        backgroundSpan.setBitmap(resource);
                        RichTexts.setBackgroundSpan(mSp, backgroundInfo, backgroundSpan);
                        textView.setText(mSp);
                    }
                });
                if (listener != null) {
                    RichTextClickSpan span = new RichTextClickSpan(listener, backgroundInfo.content, new ITouch() {
                        @Override
                        public void onTouchDown() {
                            backgroundSpan.setAlpha((int) (255 * Constant.PRESS_ALPHA));
                            RichTexts.setBackgroundSpan(mSp, backgroundInfo, backgroundSpan);
                            textView.setText(mSp);
                        }

                        @Override
                        public void onTouchUp() {
                            backgroundSpan.setAlpha(255);
                            RichTexts.setBackgroundSpan(mSp, backgroundInfo, backgroundSpan);
                            textView.setText(mSp);
                        }
                    });
                    RichTexts.setRichTextClickSpan(mSp, backgroundInfo, span);
                }
                matcher = PATTERN.matcher(mStr); //由于文本已经有变化，需重新匹配
            }
        }


//        if (extra.get(KEY_DATA) != null) {
//            ArrayList<LinkInfo> linkList = (ArrayList<LinkInfo>) extra.get(KEY_DATA);
//            Context context = textView.getContext();
//            for (LinkInfo link : linkList) {
//                final TaggedInfo backgroundInfo = new TaggedInfo(link.getStart(), link.getEnd(), link.getUrl());
//                final RadiusBackgroundSpan backgroundSpan = new RadiusBackgroundSpan(link.getBackground(), Constant.ICON_CORNER_SIZE);
//                //先设置一个默认icon
//                backgroundSpan.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
//                RichTexts.setBackgroundSpan(sp, backgroundInfo, backgroundSpan);
//                Glide.with(textView.getContext()).load(link.getIcon()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        //下载完icon重新设置span
//                        backgroundSpan.setBitmap(resource);
//                        RichTexts.setBackgroundSpan(sp, backgroundInfo, backgroundSpan);
//                        textView.postInvalidate();
//                    }
//                });
//                if (listener != null) {
//                    RichTextClickSpan span =  new RichTextClickSpan(listener, backgroundInfo.content, new ITouch() {
//                        @Override
//                        public void onTouchDown() {
//                            backgroundSpan.setAlpha((int) (255 * Constant.PRESS_ALPHA));
//                            RichTexts.setBackgroundSpan(sp, backgroundInfo, backgroundSpan);
//                        }
//
//                        @Override
//                        public void onTouchUp() {
//                            backgroundSpan.setAlpha(255);
//                            RichTexts.setBackgroundSpan(sp, backgroundInfo, backgroundSpan);
//                        }
//                    });
//                    RichTexts.setRichTextClickSpan(sp, backgroundInfo, span);
//                }
//            }
//        }
    }

    public LinkInfo getLinkInfo(String content) {
        for (LinkInfo link : mLinkList) {
            if (content.equals(link.getUrl())) {
                return link;
            }
        }
        return null;
    }

    /**
     * 通过DynamicDrawableSpan设置icon，无法设置背景，废弃先
     *
     * @author Ljq 2017/12/6
     */
    public static class StickerSpan extends DynamicDrawableSpan {

        private int mMaxWidth;
        private int mMaxHeight;
        private Drawable mDrawable;

        public StickerSpan(Context context, Drawable drawable, int maxWidth, int maxHeight) {
            super(DynamicDrawableSpan.ALIGN_BASELINE);
            mMaxWidth = maxWidth;
            mMaxHeight = maxHeight;
            setDrawable(drawable);
        }

        public void setDrawable(Drawable drawable) {
            this.mDrawable = drawable;
            int width = mDrawable.getIntrinsicWidth();
            int height = mDrawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, width > mMaxWidth ? mMaxWidth : width, height > mMaxHeight ? mMaxHeight : height);
        }

        @Override
        public Drawable getDrawable() {
            return mDrawable;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable b = mDrawable;
            canvas.save();

            canvas.translate(x, top);
            b.draw(canvas);
            canvas.restore();
        }
    }
}
