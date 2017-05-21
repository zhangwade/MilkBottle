package com.wadezhang.milkbottle.theme_post_list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    @BindView(R.id.activity_theme_detail_btn_addfollow) Button mBtnAddFollow; // TODO:判断是否已经关注
    @BindView(R.id.activity_theme_detail_author_item) LinearLayout mAuthorItem;

    String mThemeId;

    Context mContext;
    private String mAuthorId;

    private int mIsFollow = 0;

    public static void actionStart(Context context, String themeId){
        Intent intent = new Intent(context, ThemeDetailActivity.class);
        intent.putExtra("themeId", themeId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_detail);
        ButterKnife.bind(this);
        mContext = this;
        init();
        checkIsFollow(0);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAuthorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.actionStart(mContext, mAuthorId);
            }
        });
        mBtnAddFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtnAddFollow.getText().toString().equals("已关注")){
                    AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                    mAlertDialog.setMessage("确定取消关注?");
                    mAlertDialog.setCancelable(true);
                    mAlertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkIsFollow(1);
                        }
                    });
                    mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    mAlertDialog.show();
                }else{
                    checkIsFollow(1);
                }
            }
        });
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

    public void checkIsFollow(final int tag){  // 0 : 初始状态检查 1 : 单击
        User mUser = GetCurrentUser.getCurrentUser(mContext);
        if(mUser == null) return;
        BmobQuery<Theme> query = new BmobQuery<>();
        query.addWhereRelatedTo("theme", new BmobPointer(mUser));
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<Theme>() {
            @Override
            public void done(List<Theme> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()){
                        int hasFollowTheme = 0;
                        for(Theme theme : list){
                            if(mThemeId.equals(theme.getObjectId())){
                                hasFollowTheme = 1;
                                break;
                            }
                        }
                        if(hasFollowTheme == 1){
                            if(tag == 0) mBtnAddFollow.setText("已关注");
                                else cancelFollow();
                        }else{
                            if(tag == 1) addFollow();
                        }
                    }else{
                        if(tag == 1) addFollow();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差哦~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询关注话题列表失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void addFollow(){
        mBtnAddFollow.setText("已关注");
        User mUser = GetCurrentUser.getCurrentUser(mContext);
        if(mUser == null) return;
        User user = new User();
        user.setObjectId(mUser.getObjectId());
        Theme theme = new Theme();
        theme.setObjectId(mThemeId);
        BmobRelation relation =  new BmobRelation();
        relation.add(theme);
        user.setTheme(relation);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    Toast.makeText(mContext, "网络出了点小差哦~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob添加关注话题失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void cancelFollow(){
        mBtnAddFollow.setText("关注");
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
                if(e != null){
                    Toast.makeText(mContext, "网络出了点小差哦~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob取消关注话题失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }
}
