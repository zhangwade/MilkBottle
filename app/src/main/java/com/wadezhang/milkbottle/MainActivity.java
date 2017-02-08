package com.wadezhang.milkbottle;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangxix on 2017/1/24.
 */

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar) BottomNavigationBar mBottomNavigationBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayTheme);
        super.onCreate(savedInstanceState);
        SystemClock.sleep(2000);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomNavigationBar();
    }

    private void initBottomNavigationBar(){
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(,R.string.bottom_navigation_bar_1).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(,R.string.bottom_navigation_bar_2).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(,R.string.bottom_navigation_bar_3).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(,R.string.bottom_navigation_bar_4).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(,R.string.bottom_navigation_bar_5).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .setFirstSelectedPosition(0)
                .initialise();
    }
}
