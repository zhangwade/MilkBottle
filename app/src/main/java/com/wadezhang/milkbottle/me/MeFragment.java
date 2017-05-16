package com.wadezhang.milkbottle.me;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.register_and_login.ChangePasswordActivity;
import com.wadezhang.milkbottle.register_and_login.EditPersonalInfoActivity;
import com.wadezhang.milkbottle.theme_category.ThemeListActivity;
import com.wadezhang.milkbottle.watch_big_photo.WatchBigPhotoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by zhangxix on 2017/3/6.
 */

public class MeFragment extends BaseFragment {

    @BindView(R.id.fragment_me_img_icon) ImageView mIcon;
    @BindView(R.id.fragment_me_text_nickname) TextView mNickname;
    @BindView(R.id.fragment_me_text_sex) TextView mSex;
    @BindView(R.id.fragment_me_text_introduction) TextView mIntroduction;
    @BindView(R.id.fragment_me_follow_item) LinearLayout mFollowItem;
    @BindView(R.id.fragment_me_fans_item) LinearLayout mFansItem;
    @BindView(R.id.fragment_me_text_follow_count) TextView mFollowCount;
    @BindView(R.id.fragment_me_text_fans_count) TextView mFansCount;
    @BindView(R.id.fragment_me_text_post) TextView mPost;
    @BindView(R.id.fragment_me_text_theme) TextView mTheme;
    @BindView(R.id.fragment_me_text_edit_info) TextView mEditInfo;
    @BindView(R.id.fragment_me_text_settings) TextView mSettings;
    @BindView(R.id.fragment_me_swiperefreshlayout) SwipeRefreshLayout mSwipeRefreshLayout;

    private String mIconUrl;

    public static MeFragment newInstance() {
        MeFragment mMeFragment = new MeFragment();
        return mMeFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, mView);
        init();
        onClick();
        mSwipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        return mView;
    }

    public void init(){
        User user = GetCurrentUser.getCurrentUser(getContext());
        if(user == null) return;
        BmobQuery<User> query = new BmobQuery<>();
        query.addQueryKeys("icon,nickname,sex,introduction,followCount,fansCount");
        query.getObject(user.getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                    mIconUrl = user.getIcon().getFileUrl();
                    ImageLoader.with(getContext(), mIconUrl, mIcon);
                    mNickname.setText(user.getNickname());
                    mSex.setText(user.getSex());
                    mIntroduction.setText(user.getIntroduction());
                    mFollowCount.setText(user.getFollowCount().toString());
                    mFansCount.setText(user.getFansCount().toString());
                }else{
                    Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh(){
            init();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void onClick(){
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIconUrl != null) WatchBigPhotoActivity.actionStart(getContext(), mIconUrl);
            }
        });
        mFollowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowOrFansListActivity.actionStart(getContext(), 0);
            }
        });
        mFansItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowOrFansListActivity.actionStart(getContext(), 1);
            }
        });
        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostActivity.actionStart(getContext());
            }
        });
        mTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeListActivity.actionStart(getContext(), null, 3, null);
            }
        });
        mEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPersonalInfoActivity.actionStart(getContext());
            }
        });
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
