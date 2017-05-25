/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-27 上午8:24
 */

package com.wadezhang.milkbottle.book;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class GetWordsLimit {

    public static int getPageWordsLimit(int height, int lineHeight, int width, int textwidth){
        int linecount,linewords;
        linecount = height / lineHeight;
        linewords = width / textwidth;
        return linecount * linewords;
    }
}
