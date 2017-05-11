package com.wadezhang.milkbottle.register_and_login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/11 0011.
 */

public class SelectSexActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.activity_select_sex_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_select_sex_girl_item) RelativeLayout mGirlItem;
    @BindView(R.id.activity_select_sex_boy_item) RelativeLayout mBoyItem;
    @BindView(R.id.activity_select_sex_text_girl_selected) TextView mGirlSelected;
    @BindView(R.id.activity_select_sex_text_boy_selected) TextView mBoySelected;

    Context mContext;

    private String mOldSex;

    private final int SEX_GIRL = 0;
    private final int SEX_BOY = 1;

    public static void actionStart(Context context, String sex){
        Intent intent = new Intent(context, EditIntroductionActivity.class);
        intent.putExtra("sex", sex);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sex);
        ButterKnife.bind(this);
        mContext = this;
        Intent mIntent = getIntent();
        mOldSex = mIntent.getStringExtra("sex");
        if(mOldSex.equals("女")){
            mBoySelected.setVisibility(View.INVISIBLE);
        } else if(mOldSex.equals("男")){
            mGirlSelected.setVisibility(View.INVISIBLE);
        } else{
            mGirlSelected.setVisibility(View.INVISIBLE);
            mBoySelected.setVisibility(View.INVISIBLE);
        }
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mGirlItem.setOnClickListener(this);
        mBoyItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.activity_select_sex_girl_item :
                if(!mOldSex.equals("女")) updateSex(SEX_GIRL);
                    else finish();
                break;
            case R.id.activity_select_sex_boy_item :
                if(!mOldSex.equals("男")) updateSex(SEX_BOY);
                    else finish();
                break;
            default:
                break;
        }
    }

    public void updateSex(int newSex){
        final String mNewSex;
        if(newSex == SEX_GIRL) mNewSex = "女";
            else mNewSex = "男";
        User user = GetCurrentUser.getCurrentUser(mContext);
        if(user == null) return;
        final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("正在保存...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        User newUser = new User();
        newUser.setSex(mNewSex);
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mProgressDialog.dismiss();
                if(e == null){
                    Intent intent = new Intent();
                    intent.putExtra("newSex", mNewSex);
                    setResult(RESULT_OK, intent);
                    if(mNewSex.equals("女")){
                        mGirlSelected.setVisibility(View.VISIBLE);
                        mBoySelected.setVisibility(View.INVISIBLE);
                    }else{
                        mGirlSelected.setVisibility(View.INVISIBLE);
                        mBoySelected.setVisibility(View.VISIBLE);
                    }
                    finish();
                }else{
                    Toast.makeText(mContext, "保存失败，请检查网络是否开启。", Toast.LENGTH_LONG).show();
                    Log.d(getClass().getSimpleName(), "bmob更新性别失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
