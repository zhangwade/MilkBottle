package com.wadezhang.milkbottle.register_and_login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
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

public class EditNicknameActivity extends BaseActivity {

    @BindView(R.id.activity_edit_nickname_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_edit_nickname_text_save) TextView mSave;
    @BindView(R.id.activity_edit_nickname_textinputlayout) TextInputLayout mTextInputLayout;
    @BindView(R.id.activity_edit_nickname_edit) TextInputEditText mEdit;

    Context mContext;

    private String mOldNickName;

    private final int WORD_MIN_NUM = 2;
    private final int WORD_MAX_NUM = 20;

    public static void actionStart(Context context, String nickname){
        Intent intent = new Intent(context, EditNicknameActivity.class);
        intent.putExtra("nickname", nickname);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
        ButterKnife.bind(this);
        mContext = this;
        Intent mIntent = getIntent();
        mOldNickName = mIntent.getStringExtra("nickname");
        mEdit.setText(mOldNickName);
        mSave.setEnabled(false);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        inputNickname();
        save();
    }

    public void inputNickname(){
        mEdit.setFilters(new InputFilter[]{getInputFilterForSpaceAndeEnter()});
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mEdit.getText().length() >= WORD_MIN_NUM && mEdit.getText().length() <= WORD_MAX_NUM && !mEdit.getText().equals(mOldNickName)){
                    mSave.setEnabled(true);
                }else{
                    mSave.setEnabled(false);
                }
            }
        });
    }

    public void save(){
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = GetCurrentUser.getCurrentUser(mContext);
                if(user == null) return;
                final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("正在保存...");
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                User newUser = new User();
                newUser.setNickname(mEdit.getText().toString());
                newUser.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        mProgressDialog.dismiss();
                        if(e == null){
                            Intent intent = new Intent();
                            intent.putExtra("newNickname", mEdit.getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }else{
                            Toast.makeText(mContext, "保存失败，请检查网络是否开启。", Toast.LENGTH_LONG).show();
                            Log.d(getClass().getSimpleName(), "bmob更新昵称失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
    }

    public static InputFilter getInputFilterForSpaceAndeEnter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
                if (source.equals("-") || source.toString().matches("[\\u4e00-\\u9fa5]+") || source.toString().matches("\\w+"))
                    return null;
                else
                    return "";
            }
        };
    }
}
