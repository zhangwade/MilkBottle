package com.wadezhang.milkbottle;

import android.nfc.Tag;
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
        Log.d("BaseActivity", getClass().getSimpleName()+"onDestroy");
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d(getClass().getSimpleName(), "onRestart");
    }
}
