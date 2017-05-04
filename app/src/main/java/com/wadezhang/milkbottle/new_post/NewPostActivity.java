package com.wadezhang.milkbottle.new_post;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.theme.Theme;

import java.io.File;

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
    @BindView(R.id.activity_new_post_text_tips) TextView mTips;
    @BindView(R.id.activity_new_post_linearlayout) LinearLayout mLinearLayout;
    @BindView(R.id.activity_new_post_text_wordcount) TextView mWordCount;

    Context mContext;

    private String themeId;
    private String themeName;
    private String photoPath; //TODO:图片路径

    public static void actionStart(Context context, String themeId, String themeName){
        Intent mIntent = new Intent(context, NewPostActivity.class);
        mIntent.putExtra("themeId", themeId);
        mIntent.putExtra("themeName", themeName);
        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_new_post);
        ButterKnife.bind(this);
        init();
        cancelBtnOnClick();
        sendBtnOnClick();
        selectThemeOnClick();
        editContentOnChange();
    }

    public void init(){
        Intent mIntent = getIntent();
        themeId = mIntent.getStringExtra("themeId");
        themeName = mIntent.getStringExtra("themeName");
        if(themeName != null){
            mTitle.setText(themeName);
            mLinearLayout.setVisibility(View.GONE);
        }else{
            mThemeName.setVisibility(View.INVISIBLE);
            mChangeTheme.setVisibility(View.INVISIBLE);
        }
        mSend.setEnabled(false);
        mWordCount.setVisibility(View.INVISIBLE);
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
                themeId = "zQ6F222e";
                if(themeId == null){
                    AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                    mAlertDialog.setTitle("还没有选择话题");
                    mAlertDialog.setMessage("帖子有了话题才可以发布哦~~");
                    mAlertDialog.setCancelable(true);
                    mAlertDialog.setPositiveButton("选择话题", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO:打开 选择话题 activity
                        }
                    });
                    mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    mAlertDialog.show();
                }else{
                    final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                    mProgressDialog.setMessage("正在发布...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    Theme theme = new Theme();
                    theme.setObjectId(themeId); //TODO:设置话题
                    User author = new User();
                    author.setObjectId("C0NeXXX3"); //TODO:设置作者
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
        mSelectTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivityForResult(, 0);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
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
        }
    }
}
