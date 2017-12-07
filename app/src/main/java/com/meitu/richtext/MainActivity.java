package com.meitu.richtext;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.meitu.richtext.common.Constant;
import com.meitu.richtext.info.LinkInfo;
import com.meitu.richtext.interfaces.RichTextClickListener;
import com.meitu.richtext.resolver.AtResolver;
import com.meitu.richtext.resolver.LinkResolver;
import com.meitu.richtext.resolver.TopicResolver;
import com.meitu.richtext.rich.RichTextWrapper;
import com.meitu.richtext.util.ToastUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ljq 2017/12/7
 */
public class MainActivity extends Activity {

    private String mStr = "@美图 每次发福利都感慨万千...#福利#总之支持我的相信你们会一直陪伴我，这次福利除了直播以外其余的直接通过福利社抽取赶快来戳链接呀！https://www.baidu.com http://corp.meitu.com我好想赶快发百万粉丝福利呀@百度@美拍 #粉丝福利##粉丝福利来啦#";
    private ArrayList<LinkInfo> mLinkList = new ArrayList<>();
    private ArrayList<LinkInfo> mCollectionLinkList = new ArrayList<>();
    public static Pattern PATTERN = Pattern.compile(Constant.URL_MATCH_REGULAR);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        ToastUtils.init(getApplicationContext());
        initData();

        initRichTextView();
    }

    private void initRichTextView() {
        RichTextWrapper richTextWrapper = new RichTextWrapper((TextView) findViewById(R.id.tv_rich));
        richTextWrapper.addResolver(LinkResolver.class, TopicResolver.class, AtResolver.class);

        richTextWrapper.putExtra(LinkResolver.KEY_DATA, mLinkList);

        richTextWrapper.setOnRichTextListener(LinkResolver.class, new RichTextClickListener() {
            @Override
            public void onRichTextClick(TextView v, String content) {
                ToastUtils.showToast("外链：" + content);
            }
        });
        richTextWrapper.setOnRichTextListener(TopicResolver.class, new RichTextClickListener() {
            @Override
            public void onRichTextClick(TextView v, String content) {
                ToastUtils.showToast("话题：" + content);
            }
        });
        richTextWrapper.setOnRichTextListener(AtResolver.class, new RichTextClickListener() {
            @Override
            public void onRichTextClick(TextView v, String content) {
                ToastUtils.showToast("@：" + content);
            }
        });
        richTextWrapper.setText(mStr);
    }

    /**
     * 添加模拟数据
     *
     * @author Ljq 2017/12/7
     */
    private void initData() {
        mLinkList.add(new LinkInfo("https://www.baidu.com", "百度", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1512553419348&di=1a29a0e858d829ace5e70fd02b244910&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D3397394342%2C3250084641%26fm%3D214%26gp%3D0.jpg", Color.BLUE));
        mLinkList.add(new LinkInfo("http://corp.meitu.com", "美图官网", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1513133726&di=c5d02cd0a5942d5d43ae7d1a9bb214fa&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.icosky.com%2Ficon%2Fpng%2FSystem%2FAesthetica%25202%2FAttachment.png", Color.YELLOW));
//        replaceData();
    }

    /**
     * 重新排列数据放入mCollectionLinkList，先筛选出url地址，并替换为对应的外链文本，并记录替换后该外链的起始位置，文本增加缩进用于放置icon
     *
     * @author Ljq 2017/12/6
     */
    private void replaceData() {
        Matcher matcher = PATTERN.matcher(mStr);
        if (matcher.find()) {
            String url = matcher.group();
            for (LinkInfo link : mLinkList) {
                if (mStr.contains(link.getUrl())) {
                    link.setTitle("\u3000" + link.getTitle());
                    mStr = mStr.replaceAll(link.getUrl(), link.getTitle());
                    link.setStart(matcher.start());
                    link.setEnd(matcher.start() + link.getTitle().length());
                    mCollectionLinkList.add(link);
                    replaceData();
                }
            }
        }
    }

}