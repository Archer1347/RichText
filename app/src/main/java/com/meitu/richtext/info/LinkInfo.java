package com.meitu.richtext.info;

/**
 * 一个外链
 *
 * @author Ljq 2017/12/6
 */
public class LinkInfo {

    //外链url
    private String url;
    //文本
    private String title;
    //icon地址
    private String icon;
    //背景颜色
    private int background;
    //外链文本位于整个富文本的起始位置
    private int start;
    //外链文本位于整个富文本的末尾位置
    private int end;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public LinkInfo(String url, String title, String icon, int background) {
        this.url = url;
        this.title = title;
        this.icon = icon;
        this.background = background;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
