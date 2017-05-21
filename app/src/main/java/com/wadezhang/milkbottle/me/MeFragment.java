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
import com.wadezhang.milkbottle.UserInfo;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.register_and_login.ChangePasswordActivity;
import com.wadezhang.milkbottle.register_and_login.EditPersonalInfoActivity;
import com.wadezhang.milkbottle.theme.Theme;
import com.wadezhang.milkbottle.theme_category.ThemeListActivity;
import com.wadezhang.milkbottle.watch_big_photo.WatchBigPhotoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
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
    @BindView(R.id.fragment_me_post_item) LinearLayout mPostItem;
    @BindView(R.id.fragment_me_theme_item) LinearLayout mThemeItem;
    @BindView(R.id.fragment_me_text_post_count) TextView mPostCount;
    @BindView(R.id.fragment_me_text_theme_count) TextView mThemeCount;
    @BindView(R.id.fragment_me_text_edit_info) TextView mEditInfo;
    @BindView(R.id.fragment_me_text_settings) TextView mSettings;
    @BindView(R.id.fragment_me_swiperefreshlayout) SwipeRefreshLayout mSwipeRefreshLayout;

    private String mIconUrl;
    private String mUserId;
    private String mUserInfoId;

    private int themeCount = 0;

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
        mUserId = user.getObjectId();
        User u = new User();
        u.setObjectId(mUserId);
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", u);
        query.addQueryKeys("objectId,user,followCount,fansCount");
        query.include("user[icon|nickname|sex|introduction]");
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    UserInfo userInfo = list.get(0);
                    mUserInfoId = userInfo.getObjectId();
                    mIconUrl = userInfo.getUser().getIcon().getFileUrl();
                    ImageLoader.with(getContext(), mIconUrl, mIcon);
                    mNickname.setText(userInfo.getUser().getNickname());
                    mSex.setText(userInfo.getUser().getSex());
                    mIntroduction.setText(userInfo.getUser().getIntroduction());
                    mFollowCount.setText(userInfo.getFollowCount().toString());
                    mFansCount.setText(userInfo.getFansCount().toString());
                }else{
                    Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        final User mUser = new User();
        mUser.setObjectId(mUserId);
        BmobQuery<Post> postQuery = new BmobQuery<>();
        postQuery.addWhereEqualTo("author", mUser);
        postQuery.count(Post.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    mPostCount.setText(integer.toString());
                }else{
                    Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        themeCount = 0;
        BmobQuery<Theme> themeQuery = new BmobQuery<>();
        themeQuery.addWhereEqualTo("author", mUser);
        themeQuery.count(Theme.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    themeCount += integer;
                    BmobQuery<Theme> themeBmobQuery = new BmobQuery<>();
                    themeBmobQuery.addWhereRelatedTo("theme", new BmobPointer(mUser));
                    themeBmobQuery.addQueryKeys("objectId");
                    themeBmobQuery.findObjects(new FindListener<Theme>() {
                        @Override
                        public void done(List<Theme> list, BmobException e) {
                            if(e == null){
                                if(!list.isEmpty()){
                                    themeCount += list.size();
                                }
                                mThemeCount.setText(Integer.toString(themeCount));
                            }else{
                                Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else {
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
                FollowOrFansListActivity.actionStart(getContext(), 0, mUserInfoId);
            }
        });
        mFansItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowOrFansListActivity.actionStart(getContext(), 1, mUserInfoId);
            }
        });
        mPostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPostActivity.actionStart(getContext());
            }
        });
        mThemeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeListActivity.actionStart(getContext(), null, 3, mUserId);
            }
        });
        mEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPersonalInfoActivity.actionStart(getContext(), 1);
            }
        });
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
