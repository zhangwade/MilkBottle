/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-6 下午6:59
 */

package com.wadezhang.milkbottle.theme;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.theme_category.ThemeCategory;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class Theme extends BmobObject {

    private String name;
    private String introduction;
    private BmobRelation followers;
    private User author;
    private Integer postCount;
    private ThemeCategory category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public BmobRelation getFollowers() {
        return followers;
    }

    public void setFollowers(BmobRelation followers) {
        this.followers = followers;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public ThemeCategory getCategory() {
        return category;
    }

    public void setCategory(ThemeCategory category) {
        this.category = category;
    }
}
