package com.wadezhang.milkbottle.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.wadezhang.milkbottle.ActivityCollector;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.Settings;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.register_and_login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.activity_settings_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_settings_text_about) TextView mAbout;
    @BindView(R.id.activity_settings_text_quit) TextView mQuit;

    Context mContext;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mContext = this;
        //init();
        onClick();
    }
/*
    public void init(){
        if(mSettings.getTheme() == 0)
            mToggleBtnNight.setChecked(false);
        else mToggleBtnNight.setChecked(true);
        if(mSettings.getTextSize() == 0 || mSettings.getTextSize() == 1)
            mToggleBtnTextSize.setChecked(false);
        else mToggleBtnTextSize.setChecked(true);
        mToggleBtnNight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("milkbottlepref", MODE_PRIVATE).edit();
                if(isChecked == true){
                    editor.putInt("theme", 1);
                }else{
                    editor.putInt("theme", 0);
                }
                editor.apply();
                recreate();
            }
        });
        mToggleBtnTextSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences("milkbottlepref", MODE_PRIVATE).edit();
                if(isChecked == true){
                    editor.putInt("textsize", 1);
                }else {
                    editor.putInt("textsize", 0);
                }
                editor.apply();
            }
        });
    }  */

    public void onClick(){
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsAboutActivity.actionStart(mContext);
            }
        });
        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                mAlertDialog.setMessage("确定退出登录?");
                mAlertDialog.setCancelable(true);
                mAlertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User.logOut();
                        ActivityCollector.finishAll();
                        LoginActivity.actionStart(mContext);
                    }
                });
                mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                mAlertDialog.show();
            }
        });
    }
}
