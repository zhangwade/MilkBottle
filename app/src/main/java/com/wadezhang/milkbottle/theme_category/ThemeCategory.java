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
