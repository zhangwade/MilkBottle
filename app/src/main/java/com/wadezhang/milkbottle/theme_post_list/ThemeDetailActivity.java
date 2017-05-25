/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:51
 */

package com.wadezhang.milkbottle.theme_post_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.me.UserDetailActivity;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.theme.Theme;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class ThemeDetailActivity extends BaseActivity {

    @BindView(R.id.activity_theme_detail_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_theme_detail_text_theme_name) TextView mThemeName;
    @BindView(R.id.activity_theme_detail_text_theme_introduction) TextView mThemeIntroduction;
    @BindView(R.id.activity_theme_detail_text_theme_postcount) TextView mThemePostCount;
    @BindView(R.id.activity_theme_detail_text_theme_createdat) TextView mThemeCreatedAt;
    @BindView(R.id.activity_theme_detail_img_author_icon) ImageView mAuthorIcon;
    @BindView(R.id.activity_theme_detail_text_author_name) TextView mAuthorName;
    @BindView(R.id.activity_theme_detail_text_author_sex) TextView mAuthorSex;
    @BindView(R.id.activity_theme_detail_btn_addfollow) Button mBtnAddFollow;
    @BindView(R.id.activity_theme_detail_author_item) LinearLayout mAuthorItem;
    @BindView(R.id.activity_theme_detail_text_add_follow_tip) TextView mTipAddFollow;

    String mThemeId;

    Context mContext;
    private String mAuthorId;

    private int isFollow = 0; //对用户是否已关注，0 代表未检查完，1 代表已关注，2 代表未关注
    private Handler initHandler;
    private Handler addFollowHandler;

    public static void actionStart(Context context, String themeId){
        Intent intent = new Intent(context, ThemeDetailActivity.class);
        intent.putExtra("themeId", themeId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_detail);
        ButterKnife.bind(this);
        mContext = this;
        init();
        isFollow = 0;
        mBtnAddFollow.setEnabled(false);
        initHandler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what == 1){
                    checkIsFollow();
                }
            }
        };
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        onClick();
    }

    public void init(){
        Intent mIntent = getIntent();
        mThemeId = mIntent.getStringExtra("themeId");
        BmobQuery<Theme> query = new BmobQuery<>();
        query.include("author[objectId|icon|nickname|sex]");
        query.getObject(mThemeId, new QueryListener<Theme>() {
            @Override
            public void done(Theme theme, BmobException e) {
                if(e == null){
                    if(theme != null){
                        mThemeName.setText(theme.getName());
                        mThemeIntroduction.setText(theme.getIntroduction());
                        mThemePostCount.setText("帖子总数: "+theme.getPostCount().toString());
                        mThemeCreatedAt.setText("创建时间: "+theme.getCreatedAt());
                        ImageLoader.with(mContext, theme.getAuthor().getIcon().getFileUrl(), mAuthorIcon);
                        mAuthorId = theme.getAuthor().getObjectId();
                        mAuthorName.setText(theme.getAuthor().getNickname());
                        mAuthorSex.setText(theme.getAuthor().getSex());
                        Message msg = new Message();
                        msg.what = 1;
                        initHandler.sendMessage(msg);
                    }else{
                        Toast.makeText(mContext, "话题不存在", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差哦~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void checkIsFollow(){
        User me = GetCurrentUser.getCurrentUser(mContext);
        if(me == null) return;
        if(me.getObjectId().equals(mAuthorId)){
            mBtnAddFollow.setVisibility(View.GONE);
            mTipAddFollow.setVisibility(View.GONE);
            return;
        }

        User user =  new User();
        user.setObjectId(me.getObjectId());
        BmobQuery<Theme> query = new BmobQuery<>();
        query.addWhereRelatedTo("theme", new BmobPointer(user));
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<Theme>() {
            @Override
            public void done(List<Theme> list, BmobException e) {
                if(e == null){
                    mBtnAddFollow.setEnabled(true);
                    if(!list.isEmpty()){
                        for(Theme theme : list){
                            if(mThemeId.equals(theme.getObjectId())){
                                isFollow = 1;
                                break;
                            }
                        }
                        if(isFollow == 1){
                            mBtnAddFollow.setText("已关注");
                        }else{
                            isFollow = 2;
                        }
                    }else{
                        isFollow = 2;
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差哦~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询关注话题列表失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void addFollow(){
        User me = GetCurrentUser.getCurrentUser(mContext);
        if(me == null) return;

        User user = new User();
        user.setObjectId(me.getObjectId());
        Theme theme = new Theme();
        theme.setObjectId(mThemeId);
        BmobRelation relation = new BmobRelation();
        relation.add(theme);
        user.setTheme(relation);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Message msg = new Message();
                if(e == null){
                   msg.what = 1;
                }else{
                    msg.what = 0;
                    Log.d(getClass().getSimpleName(), "bmob添加关注话题失败："+e.getMessage()+","+e.getErrorCode());
                }
                addFollowHandler.sendMessage(msg);
            }
        });
    }

    public void cancelFollow(){
        User mUser = GetCurrentUser.getCurrentUser(mContext);
        if(mUser == null) return;
        User user = new User();
        user.setObjectId(mUser.getObjectId());
        Theme theme = new Theme();
        theme.setObjectId(mThemeId);
        BmobRelation relation = new BmobRelation();
        relation.remove(theme);
        user.setTheme(relation);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Message msg = new Message();
                if(e == null){
                    msg.what = 1;
                }else {
                    msg.what = 0;
                    Log.d(getClass().getSimpleName(), "bmob取消关注话题失败："+e.getMessage()+","+e.getErrorCode());
                }
                addFollowHandler.sendMessage(msg);
            }
        });
    }

    public void onClick(){
        mAuthorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.actionStart(mContext, mAuthorId);
            }
        });
        mBtnAddFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("正在操作...");
                mProgressDialog.setCancelable(false);
                if(isFollow == 1){
                    AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                    mAlertDialog.setMessage("确定取消关注?");
                    mAlertDialog.setCancelable(true);
                    mAlertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressDialog.show();
                            cancelFollow();
                        }
                    });
                    mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    mAlertDialog.show();
                }else if(isFollow == 2){
                    mProgressDialog.show();
                    addFollow();
                }
                addFollowHandler = new Handler(){
                    public void handleMessage(Message msg){
                        mProgressDialog.dismiss();
                        switch (msg.what){
                            case 1 :
                                if(isFollow == 1){
                                    mBtnAddFollow.setText("关注");
                                    isFollow = 2;
                                }else if (isFollow == 2){
                                    mBtnAddFollow.setText("已关注");
                                    isFollow = 1;
                                }
                                break;
                            case 0 :
                                Toast.makeText(mContext, "操作失败! 请检查网络是否开启", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        });
    }
}
