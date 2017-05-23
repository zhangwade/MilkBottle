package com.wadezhang.milkbottle.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class SettingsAboutActivity extends BaseActivity {

    @BindView(R.id.activity_settings_about_imgbtn_back) ImageButton mBtnBack;

    Context mContext;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, SettingsAboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_about);
        ButterKnife.bind(this);
        mContext = this;
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
