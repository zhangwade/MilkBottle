package com.wadezhang.milkbottle.register_and_login;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.wadezhang.milkbottle.ActivityCollector;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class ChangePasswordActivity extends BaseActivity{

    @BindView(R.id.activity_change_password_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_change_password_text_save) TextView mSave;
    @BindView(R.id.activity_change_password_textinputlayout_old_password) TextInputLayout mTextInputLayoutOldPassword;
    @BindView(R.id.activity_change_password_edit_old_password) TextInputEditText mEditOldPassword;
    @BindView(R.id.activity_change_password_text_show_hide_old_password) TextView mShowOrHideOldPassword;
    @BindView(R.id.activity_change_password_edit_new_password) TextInputEditText mEditNewPassword;
    @BindView(R.id.activity_change_password_text_show_hide_new_password) TextView mShowOrHideNewPassword;

    Context mContext;

    private final int PSD_MIN_NUM = 6;
    private final int PSD_MAX_NUM = 18;
    private boolean oldPsdShow = false;
    private boolean newPsdShow = false;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        mContext = this;
        mSave.setEnabled(false);
        mShowOrHideOldPassword.setEnabled(false);
        mShowOrHideNewPassword.setEnabled(false);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editOldPassword();
        editNewPassword();
        save();
    }

    public void editOldPassword(){
        mEditOldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextInputLayoutOldPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                int oldLen = mEditOldPassword.getText().length();
                int newLen = mEditNewPassword.getText().length();
                if(oldLen >= PSD_MIN_NUM && oldLen <= PSD_MAX_NUM && newLen >= PSD_MIN_NUM && newLen <= PSD_MAX_NUM){
                    mSave.setEnabled(true);
                }else {
                    mSave.setEnabled(false);
                }
                if(oldLen > 0){
                    mShowOrHideOldPassword.setEnabled(true);
                }else{
                    mShowOrHideOldPassword.setEnabled(false);
                }
            }
        });
        mEditOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int len = mEditOldPassword.getText().length();
                if( !hasFocus && ( len < PSD_MIN_NUM || len > PSD_MAX_NUM ) ){
                    mTextInputLayoutOldPassword.setError("密码最少 6 位，最多 18 位。");
                    mTextInputLayoutOldPassword.setErrorEnabled(true);
                }
            }
        });
        mShowOrHideOldPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!oldPsdShow){
                    mEditOldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mShowOrHideOldPassword.setText("隐藏");
                    oldPsdShow = true;
                }else{
                    mEditOldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mShowOrHideOldPassword.setText("显示");
                    oldPsdShow = false;
                }
            }
        });
    }

    public void editNewPassword(){
        mEditNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int oldLen = mEditOldPassword.getText().length();
                int newLen = mEditNewPassword.getText().length();
                if(oldLen >= PSD_MIN_NUM && oldLen <= PSD_MAX_NUM && newLen >= PSD_MIN_NUM && newLen <= PSD_MAX_NUM){
                    mSave.setEnabled(true);
                }else {
                    mSave.setEnabled(false);
                }
                if(newLen > 0){
                    mShowOrHideOldPassword.setEnabled(true);
                }else{
                    mShowOrHideOldPassword.setEnabled(false);
                }
            }
        });
        mShowOrHideNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newPsdShow){
                    mEditNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mShowOrHideNewPassword.setText("隐藏");
                    newPsdShow = true;
                }else{
                    mEditNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mShowOrHideNewPassword.setText("显示");
                    newPsdShow = false;
                }
            }
        });
    }

    public void save(){
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = BmobUser.getCurrentUser(User.class);
                if(user == null) return;
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("正在修改...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                BmobUser.updateCurrentUserPassword(mEditOldPassword.getText().toString(), mEditNewPassword.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        progressDialog.dismiss();
                        if(e == null){
                            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                            mAlertDialog.setTitle("修改密码成功");
                            mAlertDialog.setMessage("请重新登录");
                            mAlertDialog.setCancelable(false);
                            mAlertDialog.setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCollector.finishAll();
                                    LoginActivity.actionStart(mContext);
                                }
                            });
                            mAlertDialog.show();
                        }else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("修改密码失败");
                            if(e.getErrorCode() == 9010 || e.getErrorCode() == 9016){
                                alertDialog.setMessage("请检查网络是否开启");
                            }else{
                                alertDialog.setMessage("旧密码有误");
                            }
                            alertDialog.setCancelable(true);
                            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                            Log.d(getClass().getSimpleName(), "bmob修改密码失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }
}
