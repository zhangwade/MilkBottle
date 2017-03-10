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
