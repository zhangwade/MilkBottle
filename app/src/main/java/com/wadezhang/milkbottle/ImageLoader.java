/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-25 上午11:13
 */

package com.wadezhang.milkbottle;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class ImageLoader {

    public static void with(Context context, String imageUrl, ImageView imageView){
        Glide.with(context).load(imageUrl).into(imageView);
    }
}
