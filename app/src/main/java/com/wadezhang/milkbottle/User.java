package com.wadezhang.milkbottle;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class User extends BmobUser {

    private Boolean sex;
    private String introduction;
    private BmobFile icon;
    private BmobRelation theme;
    private BmobRelation follow;
    private BmobRelation fans;
    private String nickname;

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public BmobRelation getTheme() {
        return theme;
    }

    public void setTheme(BmobRelation theme) {
        this.theme = theme;
    }

    public BmobRelation getFollow() {
        return follow;
    }

    public void setFollow(BmobRelation follow) {
        this.follow = follow;
    }

    public BmobRelation getFans() {
        return fans;
    }

    public void setFans(BmobRelation fans) {
        this.fans = fans;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
