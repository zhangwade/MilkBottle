/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-13 上午10:03
 */

package com.wadezhang.milkbottle;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.wadezhang.milkbottle.register_and_login.LoginActivity;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class GetCurrentUser {

    public static User getCurrentUser(Context context){
        final Context mContext = context;
        User user = BmobUser.getCurrentUser(User.class);
        if(user == null){  //获取缓存用户对象为空，需要重新登录
            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
            mAlertDialog.setMessage("登录信息过期，需要重新登录。");
            mAlertDialog.setCancelable(false);
            mAlertDialog.setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll();
                    LoginActivity.actionStart(mContext);
                }
            });
            mAlertDialog.show();
        }
        return user;
    }
}
