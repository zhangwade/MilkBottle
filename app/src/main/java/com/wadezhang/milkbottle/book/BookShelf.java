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
