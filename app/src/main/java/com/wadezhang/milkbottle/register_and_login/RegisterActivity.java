/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:48
 */

package com.wadezhang.milkbottle.register_and_login;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.MainActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.UserInfo;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.activity_register_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_register_text_login) TextView mTurnToLogin;
    @BindView(R.id.activity_register_textinputlayout_email) TextInputLayout mTextInputLayoutEmail;
    @BindView(R.id.activity_register_edit_email) TextInputEditText mEditEmail;
    @BindView(R.id.activity_register_textinputlayout_password) TextInputLayout mTextInputLayoutPassword;
    @BindView(R.id.activity_register_edit_password) TextInputEditText mEditPassword;
    @BindView(R.id.activity_register_text_password_show_hide) TextView mShowOrHidePassword;
    @BindView(R.id.activity_register_btn_register_confirm) Button mBtnRegisterConfirm;

    private final int PSD_MIN_NUM = 6;
    private final int PSD_MAX_NUM = 18;
    private boolean psdShow = false;

    private String defaultIconPath = "http://bmob-cdn-10919.b0.upaiyun.com/2017/05/22/80f5c39240bbc83780687e9e70a7b439.png";

    Context mContext;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        mBtnRegisterConfirm.setEnabled(false);
        mShowOrHidePassword.setEnabled(false);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTurnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(mContext);
                finish();
            }
        });
        inputEmail();
        inputPassword();
        registerConfirm();
    }

    public void inputEmail(){
        mEditEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextInputLayoutEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !VerifyEmail.verify(mEditEmail.getText().toString())){
                    mTextInputLayoutEmail.setError("请输入正确的邮箱地址");
                    mTextInputLayoutEmail.setErrorEnabled(true);
                }
            }
        });
    }

    public void inputPassword(){
        mEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int len = mEditPassword.getText().length();
                if(VerifyEmail.verify(mEditEmail.getText().toString()) && len >= PSD_MIN_NUM && len <= PSD_MAX_NUM){
                    mBtnRegisterConfirm.setEnabled(true);
                }else{
                    mBtnRegisterConfirm.setEnabled(false);
                }
                if(len > 0){
                    mShowOrHidePassword.setEnabled(true);
                }else{
                    mShowOrHidePassword.setEnabled(false);
                }
            }
        });
        mShowOrHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!psdShow){
                    mEditPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mShowOrHidePassword.setText("隐藏");
                    psdShow = true;
                }else{
                    mEditPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mShowOrHidePassword.setText("显示");
                    psdShow = false;
                }
            }
        });
    }

    public void registerConfirm(){
        mBtnRegisterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!VerifyEmail.verify(mEditEmail.getText().toString())){
                    mTextInputLayoutEmail.setError("请输入正确的邮箱地址");
                    mTextInputLayoutEmail.setErrorEnabled(true);
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("正在注册...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                User user = new User();
                user.setUsername(mEditEmail.getText().toString());
                user.setPassword(mEditPassword.getText().toString());
                user.setEmail(mEditEmail.getText().toString());
                user.setNickname(mEditEmail.getText().toString());
                user.setSex("未知");
                user.setIntroduction("");
                //user.setFollowCount(0);
                //user.setFansCount(0);
                BmobFile icon = new BmobFile(new Date().toString()+".jpg", "", defaultIconPath);
                user.setIcon(icon);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e){
                        progressDialog.dismiss();
                        if(e == null){
                            Intent intent = new Intent(mContext, CreateUserInfoService.class);
                            intent.putExtra("userId", user.getObjectId());
                            startService(intent);
                            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                            mAlertDialog.setTitle("注册成功");
                            mAlertDialog.setMessage("请尽快登录您的邮箱去验证，有助于以后找回密码");
                            mAlertDialog.setCancelable(false);
                            mAlertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditPersonalInfoActivity.actionStart(mContext, 0);
                                    finish();
                                }
                            });
                            mAlertDialog.show();
                        }else{
                            progressDialog.dismiss();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("注册失败");
                            if(e.getErrorCode() == 9019 || e.getErrorCode() == 9015){
                                alertDialog.setMessage("邮箱已被注册，请选择其它邮箱");
                            }else{
                                alertDialog.setMessage("请检查网络是否开启");
                            }
                            alertDialog.setCancelable(true);
                            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                            Log.d(getClass().getSimpleName(), "bmob注册失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }
}
