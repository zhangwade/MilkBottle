package com.wadezhang.milkbottle.register_and_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class EditPersonalInfoActivity extends BaseActivity {

    @BindView(R.id.activity_edit_personal_info_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_edit_personal_info_icon_item) RelativeLayout mIconItem;
    @BindView(R.id.activity_edit_personal_info_nickname_item) RelativeLayout mNicknameItem;
    @BindView(R.id.activity_edit_personal_info_sex_item) RelativeLayout mSexItem;
    @BindView(R.id.activity_edit_personal_info_introduction_item) RelativeLayout mIntroductionItem;
    @BindView(R.id.activity_edit_personal_info_change_password_item) RelativeLayout mChangePasswordItem;
    @BindView(R.id.activity_edit_personal_info_img_icon) ImageView mImgIcon;
    @BindView(R.id.activity_edit_personal_info_text_nickname) TextView mTextNickname;
    @BindView(R.id.activity_edit_personal_info_text_sex) TextView mTextSex;
    @BindView(R.id.activity_edit_personal_info_text_introduction) TextView mTextIntroduction;

    Context mContext;

    public static void actionStart(Context context, User user){
        Intent intent = new Intent(context, EditPersonalInfoActivity.class);
        intent.putExtra("icon", user.getIcon().getFileUrl());
        intent.putExtra("nickname", user.getNickname());
        intent.putExtra("sex", user.getSex());
        intent.putExtra("introduction", user.getIntroduction());
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        ButterKnife.bind(this);
        mContext = this;
        init();
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init(){
        Intent mIntent = getIntent();
        //mImgIcon TODO
        mTextNickname.setText(mIntent.getStringExtra("nickname"));
        mTextSex.setText(mIntent.getStringExtra("sex"));
        mTextIntroduction.setText(mIntent.getStringExtra("introduction"));
    }
}
