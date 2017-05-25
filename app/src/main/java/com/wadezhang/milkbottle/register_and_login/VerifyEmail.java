/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-10 下午1:38
 */

package com.wadezhang.milkbottle.register_and_login;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class VerifyEmail {

    public static boolean verify(String email){
        String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";  //TODO： 需要支持符号 "-"
        return email.matches(regex);
    }
}
