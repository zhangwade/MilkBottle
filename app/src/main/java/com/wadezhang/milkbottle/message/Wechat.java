package com.wadezhang.milkbottle.message;

import com.wadezhang.milkbottle.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class Wechat extends BmobObject {

    private User from;
    private User to;
    private String content;
    private Integer isRead;

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
