/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-3-23 下午7:44
 */

package com.wadezhang.milkbottle;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxix on 2017/3/10.
 */

public class ActivityCollector {

    public static List<Activity> mActivitiesList = new ArrayList<>();

    public static void addActivity(Activity activity){
        mActivitiesList.add(activity);
    }

    public static void removeActivity(Activity activity){
        mActivitiesList.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity : mActivitiesList){
            if(!activity.isFinishing()) activity.finish();
        }
    }
}
