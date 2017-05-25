/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-25 下午7:51
 */

package com.wadezhang.milkbottle.book;

import com.wadezhang.milkbottle.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class BookShelf extends BmobObject {

    private User user;
    private Book book;
    private String hasReadPosition;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getHasReadPosition() {
        return hasReadPosition;
    }

    public void setHasReadPosition(String hasReadPosition) {
        this.hasReadPosition = hasReadPosition;
    }
}
