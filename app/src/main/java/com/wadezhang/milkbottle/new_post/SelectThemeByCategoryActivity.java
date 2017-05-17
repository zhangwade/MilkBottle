package com.wadezhang.milkbottle.new_post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.search.SearchThemeActivity;
import com.wadezhang.milkbottle.theme_category.ThemeCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class SelectThemeByCategoryActivity extends BaseActivity {

    @BindView(R.id.activity_select_theme_category_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_select_theme_category_imgbtn_search) ImageButton mBtnSearch;
    @BindView(R.id.activity_select_theme_category_recyclerview) RecyclerView mRecyclerView;

    Context mContext;

    private List<ThemeCategory> mThemeCategoryList = new ArrayList<>();
    private SelectThemeByCategoryAdapter mSelectThemeByCategoryAdapter;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, SelectThemeByCategoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_select_theme_by_category);
        ButterKnife.bind(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mSelectThemeByCategoryAdapter = new SelectThemeByCategoryAdapter(mThemeCategoryList);
        mRecyclerView.setAdapter(mSelectThemeByCategoryAdapter);
        initThemeCategoryList();
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchThemeActivity.actionStart(mContext, 0);
            }
        });
    }

    public void initThemeCategoryList(){
        BmobQuery<ThemeCategory> query = new BmobQuery<>();
        query.addQueryKeys("objectId,title,subTitle");
        query.findObjects(new FindListener<ThemeCategory>() {
            @Override
            public void done(List<ThemeCategory> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()){
                        showThemeCategoryList(list);
                    }else{
                        Toast.makeText(mContext, "暂时没有分类哦~~", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差哦~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void showThemeCategoryList(List<ThemeCategory> list){
        mThemeCategoryList.clear();
        mThemeCategoryList.addAll(list);
        mSelectThemeByCategoryAdapter.notifyDataSetChanged();
    }
}
