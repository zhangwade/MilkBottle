package com.wadezhang.milkbottle.register_and_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nanchen.compresshelper.CompressHelper;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.MainActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

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

    private String iconPath;
    private final int IMAGE_PICKER = 98;

    private final int RETURN_ICON = 0;
    private final int RETURN_NICKNAME = 1;
    private final int RETURN_SEX = 2;
    private final int RETURN_INTRODUCTION = 3;

    private int mType;
    private final int TYPE_AFTER_REGISTER = 0;
    private final int TYPE_NORMAL = 1;

    public static void actionStart(Context context, int type){
        Intent intent = new Intent(context, EditPersonalInfoActivity.class);
        intent.putExtra("type", type);
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
                if(mType == TYPE_AFTER_REGISTER) MainActivity.actionStart(mContext);
                finish();
            }
        });
        mIconItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
            }
        });
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
        User user = GetCurrentUser.getCurrentUser(mContext);
        if( user == null) return;
        Intent mIntent = getIntent();
        mType = mIntent.getIntExtra("type", 0);
        ImageLoader.with(mContext, user.getIcon().getFileUrl(), mImgIcon);
        mTextNickname.setText((String) BmobUser.getObjectByKey("nickname"));
        mTextSex.setText((String) BmobUser.getObjectByKey("sex"));
        if(BmobUser.getObjectByKey("introduction") != null)
            mTextIntroduction.setText((String) BmobUser.getObjectByKey("introduction"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == ImagePicker.RESULT_CODE_ITEMS){
            if(data != null && requestCode == IMAGE_PICKER){
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                iconPath = images.get(0).path;
                ImageLoader.with(mContext, iconPath, mImgIcon);
                File oldFile = new File(iconPath);
                File newFile = CompressHelper.getDefault(mContext).compressToFile(oldFile);
                iconPath = newFile.getPath();

                final BmobFile photoFile = new BmobFile(new File(iconPath));
                photoFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            User me = GetCurrentUser.getCurrentUser(mContext);
                            User user = new User();
                            if(photoFile != null) user.setIcon(photoFile);
                            user.update(me.getObjectId(),new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(mContext, "上传头像成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        mImgIcon.setImageResource(R.mipmap.default_icon);
                                        Toast.makeText(mContext, "上传头像失败！请重新选择图片", Toast.LENGTH_SHORT).show();
                                        Log.d(getClass().getSimpleName(), "bmob上传图片失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{
                            mImgIcon.setImageResource(R.mipmap.default_icon);
                            Toast.makeText(mContext, "上传头像失败！请重新选择图片", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "bmob上传图片失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }else{

            }
        }
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
        if(mType == TYPE_AFTER_REGISTER) MainActivity.actionStart(mContext);
        finish();
    }
}
