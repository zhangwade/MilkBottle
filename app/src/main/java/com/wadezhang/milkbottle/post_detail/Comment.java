/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-13 下午3:28
 */

package com.wadezhang.milkbottle.post_detail;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class Comment extends BmobObject {

    private Post post;
    private String content;
    private User author;
    private User toWho;
    private Integer isRead;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public User getToWho() {
        return toWho;
    }

    public void setToWho(User toWho) {
        this.toWho = toWho;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
