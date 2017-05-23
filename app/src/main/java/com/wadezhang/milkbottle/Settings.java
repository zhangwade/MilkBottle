package com.wadezhang.milkbottle;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class Settings {

    Context mContext;
    int mTheme;  //白天 OR 黑夜 主题
    int mTextSize;  //瓶子文章字号大小

    private final int THEME_DAY = 0;
    private final int THEME_NIGHT = 1;
    private final int TEXT_SMALL = 0;
    private final int TEXT_BIG = 1;

    public Settings(Context context){
        mContext = context;
        SharedPreferences pref = mContext.getSharedPreferences("milkbottlepref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        mTheme = pref.getInt("theme", 2);
        if(mTheme == 2) editor.putInt("theme", THEME_DAY);
        mTextSize = pref.getInt("textsize", 2);
        if(mTextSize == 2) editor.putInt("textsize", TEXT_SMALL);
        editor.apply();
    }

    public int getTheme(){
        if(mTheme == THEME_DAY) return 0;
            else return 1;
    }

    public int getTextSize(){
        if(mTextSize == TEXT_SMALL){
            if(mTheme == THEME_DAY) return 0;
                else return 1;
        }else{
            if(mTheme == THEME_DAY) return 2;
            else return 3;
        }
    }
}
