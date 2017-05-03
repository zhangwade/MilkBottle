package com.wadezhang.milkbottle.new_post;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    Context mContext;

    private String themeId;
    private String themeName;
    private boolean isThemeSelected = false;

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
        sendBtnOnclick();
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

    public void sendBtnOnclick(){
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isContentEmpty = ("".equals(mEditContent.getText().toString().trim()));
                if(!isThemeSelected || !isContentEmpty){

                }
            }
        });
    }
}
