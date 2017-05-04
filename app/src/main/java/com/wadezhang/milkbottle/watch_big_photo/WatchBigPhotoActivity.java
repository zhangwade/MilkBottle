package com.wadezhang.milkbottle.watch_big_photo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class WatchBigPhotoActivity extends BaseActivity {

    @BindView(R.id.activity_watch_big_photo_img) ImageView mPhoto;

    private String mPhotoUrl;

    public static void actionStart(Context context, String photoUrl){
        Intent mIntent = new Intent(context, WatchBigPhotoActivity.class);
        mIntent.putExtra("photoUrl", photoUrl);
        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_big_photo);
        ButterKnife.bind(this);
        Intent mIntent = getIntent();
        mPhotoUrl = mIntent.getStringExtra("photoUrl");
        ImageLoader.with(this, mPhotoUrl, mPhoto);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
