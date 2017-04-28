package com.wadezhang.milkbottle.theme;

import com.wadezhang.milkbottle.User;

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
}
