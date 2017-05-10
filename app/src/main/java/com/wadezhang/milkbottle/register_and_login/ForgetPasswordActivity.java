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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.activity_forget_password_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_forget_password_textinputlayout_email) TextInputLayout mTextInputLayoutEmail;
    @BindView(R.id.activity_forget_password_edit_email) TextInputEditText mEditEmail;
    @BindView(R.id.activity_forget_password_btn_send_email) Button mBtnSendEmail;

    Context mContext;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        mContext = this;
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inputEmail();
        sendEmailConfirm();
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
    }

    public void sendEmailConfirm(){
        mBtnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!VerifyEmail.verify(mEditEmail.getText().toString())){
                    mTextInputLayoutEmail.setError("请输入正确的邮箱地址");
                    mTextInputLayoutEmail.setErrorEnabled(true);
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("正在发送...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                BmobUser.resetPasswordByEmail(mEditEmail.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        progressDialog.dismiss();
                        if(e == null){
                            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                            mAlertDialog.setTitle("发送成功");
                            mAlertDialog.setMessage("请查收邮件进行密码重置操作。注意：密码长度最少6位，最多18位，只能用英文字母和数字，否则无法登录应用。");
                            mAlertDialog.setCancelable(false);
                            mAlertDialog.setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoginActivity.actionStart(mContext);
                                    finish();
                                }
                            });
                            mAlertDialog.show();
                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("发送失败");
                            if(e.getErrorCode() == 9010 || e.getErrorCode() == 9016){
                                alertDialog.setMessage("请检查网络是否开启");
                            }else{
                                alertDialog.setMessage("请检查邮箱是否有误");
                            }
                            alertDialog.setCancelable(true);
                            alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            alertDialog.show();
                            Log.d(getClass().getSimpleName(), "bmob发送重置密码邮件失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }
}
