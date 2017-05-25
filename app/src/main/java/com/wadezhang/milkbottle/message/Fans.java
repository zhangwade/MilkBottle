/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-13 下午4:07
 */

package com.wadezhang.milkbottle.message;

import com.wadezhang.milkbottle.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class Fans extends BmobObject {

    private User from;
    private User to;
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

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
