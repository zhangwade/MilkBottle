package com.wadezhang.milkbottle.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.new_post.NewPostActivity;
import com.wadezhang.milkbottle.post.Post;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class PostDetailActivity extends BaseActivity {

    @BindView(R.id.activity_post_detail_viewpager_swipetoloadlayout) SwipeToLoadLayout mSwipeToLoadLayout;

    @BindView(R.id.activity_post_detail_tab) TabLayout mTabLayout;
    @BindView(R.id.activity_post_detail_viewpager) ViewPager mViewPager;
    @BindView(R.id.activity_post_detail_imgbtn_back) ImageButton mButtonBack;
    @BindView(R.id.activity_post_detail_text_theme) TextView mTheme;
    @BindView(R.id.activity_post_detail_img_author_icon) ImageView mAuthorIcon;
    @BindView(R.id.activity_post_detail_text_author_name) TextView mAuthorName;
    @BindView(R.id.activity_post_detail_img_photo) ImageView mPhoto;
    @BindView(R.id.activity_post_detail_text_content) TextView mContent;

    private String postObjectId;
    private String themeObjectId;
    private String authorObjectId;

    FragmentManager mFragmentManager;
    ArrayList<Fragment> mFragmentList;

    public static void actionStart(Context context, Post post, boolean isLikes){
        Intent mIntent = new Intent(context, PostDetailActivity.class);
        mIntent.putExtra("postObjectId", post.getObjectId());
        mIntent.putExtra("themeObjectId", post.getTheme().getObjectId());
        mIntent.putExtra("themeName", post.getTheme().getName());
        mIntent.putExtra("authorObjectId", post.getAuthor().getObjectId());
        mIntent.putExtra("authorIcon", post.getAuthor().getIcon().getFileUrl());
        mIntent.putExtra("authorName", post.getAuthor().getUsername());
        mIntent.putExtra("photo", post.getPhoto().getFileUrl());
        mIntent.putExtra("content", post.getContent());
        mIntent.putExtra("IsLikes", isLikes); //TODO:点赞的图案切换
        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        initPost();
        initViewPager();
        mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        mTabLayout.setupWithViewPager(mViewPager);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initPost(){
        Intent mIntent = getIntent();
        postObjectId = mIntent.getStringExtra("postObjectId");
        themeObjectId = mIntent.getStringExtra("themeObjectId");
        authorObjectId = mIntent.getStringExtra("authorObjectId");
        mTheme.setText(mIntent.getStringExtra("themeName"));
        ImageLoader.with(this, mIntent.getStringExtra("authorIcon"), mAuthorIcon);
        mAuthorName.setText(mIntent.getStringExtra("authorName"));
        ImageLoader.with(this, mIntent.getStringExtra("photo"), mPhoto);
        mContent.setText(mIntent.getStringExtra("content"));
    }

    public void initViewPager(){
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<Fragment>();
        Fragment mPostDetailCommentFragment = mFragmentManager.findFragmentByTag(PostDetailCommentFragment.class.getName());
        if(mPostDetailCommentFragment == null) mPostDetailCommentFragment = PostDetailCommentFragment.newInstance();
        Fragment mPostDetailGoodFragment = mFragmentManager.findFragmentByTag(PostDetailLikesFragment.class.getName());
        if(mPostDetailGoodFragment == null) mPostDetailGoodFragment = PostDetailLikesFragment.newInstance();
        mFragmentList.add(mPostDetailCommentFragment);
        mFragmentList.add(mPostDetailGoodFragment);
        mViewPager.setAdapter(new PostDetailViewPagerAdapter(mFragmentManager, mFragmentList));
        mViewPager.setCurrentItem(0);
        new PostDetailCommentPresenter(postObjectId, (PostDetailCommentFragment)mPostDetailCommentFragment);
    }

    public class RefreshListener implements OnRefreshListener {

        @Override
        public void onRefresh(){
            PostDetailCommentFragment postDetailCommentFragment = (PostDetailCommentFragment)mFragmentManager.findFragmentByTag(PostDetailCommentFragment.class.getName());
            postDetailCommentFragment.mPostDetailCommentPresenter.getComment(0);
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener {

        @Override
        public void onLoadMore(){
            PostDetailCommentFragment postDetailCommentFragment = (PostDetailCommentFragment)mFragmentManager.findFragmentByTag(PostDetailCommentFragment.class.getName());
            postDetailCommentFragment.mPostDetailCommentPresenter.getComment(1);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }
}
