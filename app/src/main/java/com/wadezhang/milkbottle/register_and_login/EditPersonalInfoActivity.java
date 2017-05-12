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
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

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

    private final int RETURN_ICON = 0;
    private final int RETURN_NICKNAME = 1;
    private final int RETURN_SEX = 2;
    private final int RETURN_INTRODUCTION = 3;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, EditPersonalInfoActivity.class);
        //intent.putExtra("icon", user.getIcon().getFileUrl());
        //intent.putExtra("nickname", user.getNickname());
        //intent.putExtra("sex", user.getSex());
        //intent.putExtra("introduction", user.getIntroduction());
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
        //mIconItem  TODO
        mNicknameItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditNicknameActivity.class);
                intent.putExtra("nickname", mTextNickname.getText().toString());
                startActivityForResult(intent, RETURN_NICKNAME);
            }
        });
        mSexItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectSexActivity.class);
                intent.putExtra("sex", mTextSex.getText().toString());
                startActivityForResult(intent, RETURN_SEX);
            }
        });
        mIntroductionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditIntroductionActivity.class);
                intent.putExtra("introduction", (String) BmobUser.getObjectByKey("introduction"));
                startActivityForResult(intent, RETURN_INTRODUCTION);
            }
        });
        mChangePasswordItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.actionStart(mContext);
            }
        });
    }

    public void init(){
        if(GetCurrentUser.getCurrentUser(mContext) == null) return;
        Intent mIntent = getIntent();
        //mImgIcon TODO
        mTextNickname.setText((String) BmobUser.getObjectByKey("nickname"));
        mTextSex.setText((String) BmobUser.getObjectByKey("sex"));
        if(BmobUser.getObjectByKey("introduction") != null)
            mTextIntroduction.setText((String) BmobUser.getObjectByKey("introduction"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case RETURN_ICON: //TODO
                break;
            case RETURN_NICKNAME:
                if(resultCode == RESULT_OK){
                    mTextNickname.setText(data.getStringExtra("newNickname"));
                }
                break;
            case RETURN_SEX:
                if(resultCode == RESULT_OK){
                    mTextSex.setText(data.getStringExtra("newSex"));
                }
                break;
            case RETURN_INTRODUCTION:
                if(resultCode == RESULT_OK){
                    mTextIntroduction.setText(data.getStringExtra("newIntroduction"));
                }
                break;
            default:
        }
    }
}
