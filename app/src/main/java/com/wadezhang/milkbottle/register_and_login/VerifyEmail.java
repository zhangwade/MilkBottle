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
