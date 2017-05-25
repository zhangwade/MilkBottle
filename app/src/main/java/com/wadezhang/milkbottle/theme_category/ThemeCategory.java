/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-6 下午6:57
 */

package com.wadezhang.milkbottle.theme_category;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class ThemeCategory extends BmobObject {

    private String title;
    private String subTitle;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getSubTitle(){
        return subTitle;
    }

    public void setSubTitle(String subTitle){
        this.subTitle = subTitle;
    }
}
