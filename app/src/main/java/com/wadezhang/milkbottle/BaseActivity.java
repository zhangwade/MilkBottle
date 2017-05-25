/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-23 下午4:37
 */

package com.wadezhang.milkbottle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by zhangxix on 2017/3/10.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(getClass().getSimpleName(), "onCreate");
        ActivityCollector.addActivity(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(getClass().getSimpleName(), "onStart");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(getClass().getSimpleName(), "onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(getClass().getSimpleName(), "onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(getClass().getSimpleName(), "onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(getClass().getSimpleName(), "onDestroy");
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d(getClass().getSimpleName(), "onRestart");
    }
}
