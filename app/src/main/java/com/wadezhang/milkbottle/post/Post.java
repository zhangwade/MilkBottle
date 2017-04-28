package com.wadezhang.milkbottle.post;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.theme.Theme;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class Post extends BmobObject {

    private Theme theme;
    private BmobFile photo;
    private String content;
    private User author;

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
