/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:38
 */

package com.wadezhang.milkbottle.new_post;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nanchen.compresshelper.CompressHelper;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.theme.Theme;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class NewPostActivity extends BaseActivity {

    @BindView(R.id.activity_new_post_text_cancel) TextView mCancel;
    @BindView(R.id.activity_new_post_text_title) TextView mTitle;
    @BindView(R.id.activity_new_post_text_send) TextView mSend;
    @BindView(R.id.activity_new_post_text_theme_name) TextView mThemeName;
    @BindView(R.id.activity_new_post_text_select_theme) TextView mSelectTheme;
    @BindView(R.id.activity_new_post_text_change_theme) TextView mChangeTheme;
    @BindView(R.id.activity_new_post_edit_content) EditText mEditContent;
    @BindView(R.id.activity_new_post_img_photo) ImageView mPhoto;
    @BindView(R.id.activity_new_post_imgbtn_delete_photo) ImageButton mBtnDeletePhoto;
    @BindView(R.id.activity_new_post_text_tips) TextView mTips;
    @BindView(R.id.activity_new_post_linearlayout) LinearLayout mLinearLayout;
    @BindView(R.id.activity_new_post_text_wordcount) TextView mWordCount;

    Context mContext;

    private final int IMAGE_PICKER = 99;

    private String themeId;
    private String themeName;
    private String photoPath;

    private IntentFilter intentFilter;
    private SelectThemeReceiver selectThemeReceiver;

    private int hasTheme = 0;

    public static void actionStart(Context context, String themeId, String themeName){
        Intent mIntent = new Intent(context, NewPostActivity.class);
        mIntent.putExtra("themeId", themeId);
        mIntent.putExtra("themeName", themeName);
        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        init();
        cancelBtnOnClick();
        sendBtnOnClick();
        selectThemeOnClick();
        editContentOnChange();
        selectPhotoOnClick();
        deletePhotoOnClick();
    }

    public void init(){
        Intent mIntent = getIntent();
        themeId = mIntent.getStringExtra("themeId");
        themeName = mIntent.getStringExtra("themeName");
        if(themeName != null){
            mTitle.setText(themeName);
            mLinearLayout.setVisibility(View.GONE);
            hasTheme = 1;
        }else{
            mThemeName.setVisibility(View.INVISIBLE);
            mChangeTheme.setVisibility(View.INVISIBLE);
            intentFilter = new IntentFilter();
            intentFilter.addAction("com.wadezhang.milkbottle.NEW_POST_SELECT_THEME");
            selectThemeReceiver = new SelectThemeReceiver();
            registerReceiver(selectThemeReceiver, intentFilter);
        }
        mSend.setEnabled(false);
        mWordCount.setVisibility(View.INVISIBLE);
        mBtnDeletePhoto.setVisibility(View.INVISIBLE);
    }

    public class SelectThemeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent){
            themeId = intent.getStringExtra("themeId");
            themeName = intent.getStringExtra("themeName");
            if(mThemeName.getVisibility() == View.INVISIBLE) mThemeName.setVisibility(View.VISIBLE);
            mThemeName.setText(themeName);
            if(mSelectTheme.getVisibility() == View.VISIBLE) mSelectTheme.setVisibility(View.INVISIBLE);
            if(mChangeTheme.getVisibility() == View.INVISIBLE) mChangeTheme.setVisibility(View.VISIBLE);
        }
    }

    public void cancelBtnOnClick(){
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                mAlertDialog.setMessage("取消本次编辑");
                mAlertDialog.setCancelable(true);
                mAlertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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

    public void sendBtnOnClick(){
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(themeId == null){
                    AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                    mAlertDialog.setTitle("还没有选择话题");
                    mAlertDialog.setMessage("帖子有了话题才可以发布哦~~");
                    mAlertDialog.setCancelable(true);
                    mAlertDialog.setPositiveButton("选择话题", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SelectThemeByCategoryActivity.actionStart(mContext);
                        }
                    });
                    mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    mAlertDialog.show();
                }else{
                    User me  = GetCurrentUser.getCurrentUser(mContext);
                    if(me == null) return;
                    final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                    mProgressDialog.setMessage("正在发布...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    File oldFile = new File(photoPath);
                    File newFile = CompressHelper.getDefault(mContext).compressToFile(oldFile);
                    photoPath = newFile.getPath();
                    Theme theme = new Theme();
                    theme.setObjectId(themeId);
                    User author = new User();
                    author.setObjectId(me.getObjectId());
                    Handler handler = new Handler(){
                        public void handleMessage(Message message){
                            mProgressDialog.dismiss();
                            switch (message.what){
                                case 1 :  //上传成功
                                    finish();
                                    break;
                                case 0 :  //图片上传失败
                                case 2 :  //帖子上传失败
                                    Toast.makeText(mContext, "发布失败，请重新发布~~", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    };
                    new SendPost(theme, photoPath, mEditContent.getText().toString(), author, handler);
                }
            }
        });
    }

    public void selectThemeOnClick(){
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectThemeByCategoryActivity.actionStart(mContext);
            }
        });
    }

    public void editContentOnChange(){
        mEditContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mWordCount.setText(Integer.toString(mEditContent.getText().length()));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWordCount.setText(Integer.toString(mEditContent.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!mEditContent.getText().toString().trim().equals("")){
                    if(mEditContent.getText().length() <= 2000){
                        mSend.setEnabled(true);
                        mWordCount.setText(Integer.toString(mEditContent.getText().length()));
                    } else{
                        mSend.setEnabled(false);
                        mWordCount.setText(Integer.toString(2000 - mEditContent.getText().length()));
                    }
                    mWordCount.setVisibility(View.VISIBLE);
                }else {
                    mSend.setEnabled(false);
                    mWordCount.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void selectPhotoOnClick(){
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        });
    }

    public void deletePhotoOnClick(){
        mBtnDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnDeletePhoto.setVisibility(View.INVISIBLE);
                mPhoto.setImageResource(R.mipmap.add_photo);
                photoPath = null;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        /*
        switch(requestCode){
            case 0 : //选择话题 返回结果
                if(resultCode == RESULT_OK){
                    themeId = data.getStringExtra("themeId");
                    themeName = data.getStringExtra("themeName");
                    mThemeName.setVisibility(View.VISIBLE);
                    mThemeName.setText(themeName);
                    mSelectTheme.setVisibility(View.INVISIBLE);
                    mChangeTheme.setVisibility(View.VISIBLE);
                }
                break;
            default:
        }   */
        if(resultCode == ImagePicker.RESULT_CODE_ITEMS){
            if(data != null && requestCode == IMAGE_PICKER){
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                photoPath = images.get(0).path;
                ImageLoader.with(mContext, photoPath, mPhoto);
                mBtnDeletePhoto.setVisibility(View.VISIBLE);
            }else{

            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(hasTheme == 0) unregisterReceiver(selectThemeReceiver);
    }
}
