package com.meitu.richtext.common;

/**
 * 常亮
 *
 * @author Ljq 2017/12/6
 */
public class Constant {
    //URL正则
    public static final String URL_MATCH_REGULAR = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
    //话题正则
    public static final String TOPIC_MATCH_REGULAR = "\\#(\\w+)\\#";
    //@正则
    public static final String AT_MATCH_REGULAR = "\\@(\\w+)\\@| ";
    //富文本点击透明度效果百分比
    public static final float PRESS_ALPHA = 0.2f;
    //外链的icon圆角大小
    public static final int ICON_CORNER_SIZE = 8;
}
