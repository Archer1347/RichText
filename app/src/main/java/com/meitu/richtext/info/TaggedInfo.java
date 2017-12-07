package com.meitu.richtext.info;

/**
 * 记录每个文本项的位置、内容
 * @author Ljq 2017/12/5
 */
public class TaggedInfo {

    public int start;
    public int end;
    public String content;

    public TaggedInfo(int b, int e, String text) {
        start = b;
        end = e;
        content = text;
    }
}
