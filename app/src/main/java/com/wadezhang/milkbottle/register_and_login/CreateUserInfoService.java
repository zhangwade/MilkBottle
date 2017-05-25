/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-21 下午6:45
 */

package com.wadezhang.milkbottle.register_and_login;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.UserInfo;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/5/21 0021.
 */

public class CreateUserInfoService extends IntentService {

    public CreateUserInfoService(){
        super("CreateUserInfoService");
    }

    @Override
    public void onHandleIntent(Intent intent){
        String userId = intent.getStringExtra("userId");
        User user = new User();
        user.setObjectId(userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setFansCount(0);
        userInfo.setFollowCount(0);
        userInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){

                }else{
                    Log.d(getClass().getSimpleName(), "bmob创建 UserInfo 失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
