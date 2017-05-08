package com.wadezhang.milkbottle.theme_post_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme.Theme;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

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

    String mThemeId;

    Context mContext;

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
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init(){
        Intent mIntent = getIntent();
        mThemeId = mIntent.getStringExtra("themeId");
        BmobQuery<Theme> query = new BmobQuery<>();
        query.include("author[objectId|icon|username|sex]");
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
                        mAuthorName.setText(theme.getAuthor().getUsername());
                        mAuthorSex.setText((theme.getAuthor().getSex() ? "男" : "女"));
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
}
