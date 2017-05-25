/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:34
 */

package com.wadezhang.milkbottle.bottle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.watch_big_photo.WatchBigPhotoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class EssayDetailActivity extends BaseActivity {

    @BindView(R.id.activity_essay_detail_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_essay_detail_text_time) TextView mTime;
    @BindView(R.id.activity_essay_detail_text_title) TextView mTitle;
    @BindView(R.id.activity_essay_detail_text_author) TextView mAuthor;
    @BindView(R.id.activity_essay_detail_img_photo) ImageView mPhoto;
    @BindView(R.id.activity_essay_detail_text_content) TextView mContent;

    Context mContext;

    String mEssayId;
    String mPhotoUrl;

    public static void actionStart(Context context, Essay essay){
        Intent intent = new Intent(context, EssayDetailActivity.class);
        intent.putExtra("id", essay.getObjectId());
        intent.putExtra("title", essay.getTitle());
        intent.putExtra("author", essay.getAuthor());
        intent.putExtra("photo", essay.getPhoto().getFileUrl());
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_detail);
        ButterKnife.bind(this);
        mContext = this;
        init();
        getContent();
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchBigPhotoActivity.actionStart(mContext, mPhotoUrl);
            }
        });
    }

    public void init(){
        Intent mIntent = getIntent();
        mEssayId = mIntent.getStringExtra("id");
        mTitle.setText(mIntent.getStringExtra("title"));
        mAuthor.setText(mIntent.getStringExtra("author"));
        mPhotoUrl = mIntent.getStringExtra("photo");
        ImageLoader.with(mContext, mPhotoUrl, mPhoto);
    }

    public void getContent(){
        BmobQuery<Essay> query = new BmobQuery<>();
        query.addQueryKeys("createdAt,content");
        query.getObject(mEssayId, new QueryListener<Essay>() {
            @Override
            public void done(Essay essay, BmobException e) {
                if(e == null){
                    mTime.setText(essay.getCreatedAt());
                    if(essay.getContent() != null) mContent.setText(essay.getContent());
                }else{
                    Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
