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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.ActivityCollector;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class ReLoginActivity extends BaseActivity {

    @BindView(R.id.activity_login_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_login_text_register) TextView mTurnToRegister;
    @BindView(R.id.activity_login_textinputlayout_email) TextInputLayout mTextInputLayoutEmail;
    @BindView(R.id.activity_login_edit_email) TextInputEditText mEditEmail;
    @BindView(R.id.activity_login_textinputlayout_password) TextInputLayout mTextInputLayoutPassword;
    @BindView(R.id.activity_login_edit_password) TextInputEditText mEditPassword;
    @BindView(R.id.activity_login_text_password_show_hide) TextView mShowOrHidePassword;
    @BindView(R.id.activity_login_btn_login_confirm) Button mBtnLoginConfirm;
    @BindView(R.id.activity_login_text_forget_password) TextView mForgetPassword;

    private final int PSD_MIN_NUM = 6;
    private final int PSD_MAX_NUM = 18;
    private boolean psdShow = false;

    //private String defaultIconPath = "http://bmob-cdn-10919.b0.upaiyun.com/2017/04/30/a1902635402911b4806f3f7c8baac63b.jpg";

    private long clickTime = 0; // 第一次点击的时间

    Context mContext;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        mBtnLoginConfirm.setEnabled(false);
        mShowOrHidePassword.setEnabled(false);
        mBtnBack.setVisibility(View.INVISIBLE);
        mTurnToRegister.setVisibility(View.INVISIBLE);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPasswordActivity.actionStart(mContext);
            }
        });
        inputEmail();
        inputPassword();
        loginConfirm();
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
                    mBtnLoginConfirm.setEnabled(true);
                }else{
                    mBtnLoginConfirm.setEnabled(false);
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

    public void loginConfirm(){
        mBtnLoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!VerifyEmail.verify(mEditEmail.getText().toString())){
                    mTextInputLayoutEmail.setError("请输入正确的邮箱地址");
                    mTextInputLayoutEmail.setErrorEnabled(true);
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("正在登录...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                User user = new User();
                user.setUsername(mEditEmail.getText().toString());
                user.setPassword(mEditPassword.getText().toString());
                user.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e){
                        progressDialog.dismiss();
                        if(e == null){  //TODO:直接开启 MainActivity
                            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                            //mAlertDialog.setTitle("登录成功");
                            mAlertDialog.setMessage("登录成功");
                            mAlertDialog.setCancelable(false);
                            mAlertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //ThemePostListActivity.actionStart(mContext, mExistedTheme);
                                }
                            });
                            mAlertDialog.show();
                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                            alertDialog.setTitle("登录失败");
                            if(e.getErrorCode() == 9010 || e.getErrorCode() == 9016){
                                alertDialog.setMessage("请检查网络是否开启");
                            }else{
                                alertDialog.setMessage("帐号或密码有误");
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

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else { // 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }
    /* 注意：重写onKeyDown()和onBackPressed()方法都能捕获Back的点击事件，
    onKeyDown()兼容Android 1.0到Android 2.1，也是常规方法，Android 2.0开始又多出了
    一种新的方法onBackPressed()，可以单独获取Back键的按下事件， 方法二的代码将两
    种方法嵌套使用了，onBackPressed()方法会处理返回键的操作，不会向上传播，如果想
    向上传播，则需要使用onKeyDown() */

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
            android.os.Process.killProcess(android.os.Process.myPid()); //杀掉当前进程
        }
    }
}
