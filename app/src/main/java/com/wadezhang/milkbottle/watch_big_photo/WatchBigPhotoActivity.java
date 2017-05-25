/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:52
 */

package com.wadezhang.milkbottle.watch_big_photo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;

import java.io.File;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class WatchBigPhotoActivity extends BaseActivity {

    @BindView(R.id.activity_watch_big_photo_img) ImageView mPhoto;

    private String mPhotoUrl;

    private Context mContext;

    public static void actionStart(Context context, String photoUrl){
        Intent mIntent = new Intent(context, WatchBigPhotoActivity.class);
        mIntent.putExtra("photoUrl", photoUrl);
        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_big_photo);
        ButterKnife.bind(this);
        mContext = this;
        Intent mIntent = getIntent();
        mPhotoUrl = mIntent.getStringExtra("photoUrl");
        ImageLoader.with(this, mPhotoUrl, mPhoto);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPhoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initPopupWindow(v);
                return true;
            }
        });
    }

    public void initPopupWindow(View v){
        View view = this.getLayoutInflater().inflate(R.layout.activity_watch_big_photo_popupwindow, null);
        TextView mPopupWindowSave = (TextView) view.findViewById(R.id.activity_watch_big_photo_popupwindow_text_save);
        TextView mPopupWindowCancel = (TextView)view.findViewById(R.id.activity_watch_big_photo_popupwindow_text_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dayWindowBackground)));
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        mPopupWindowSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("保存中...");
                mProgressDialog.setCancelable(false);
                String fileName = new Date().toString()+".png";
                BmobFile bmobFile = new BmobFile(fileName, "", mPhotoUrl);
                File saveFile = new File(Environment.getExternalStorageDirectory(), bmobFile.getFilename());
                bmobFile.download(saveFile, new DownloadFileListener() {
                    @Override
                    public void onStart(){
                        mProgressDialog.show();
                    }

                    @Override
                    public void done(String s, BmobException e) {
                        mProgressDialog.dismiss();
                        if(e == null){
                            Toast.makeText(mContext, "已保存到以下目录："+s, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(mContext, "保存失败，请重试~~", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        });
        mPopupWindowCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
}
