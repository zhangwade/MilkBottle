package com.wadezhang.milkbottle.new_theme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme_category.ThemeCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class SelectThemeCategoryActivity extends BaseActivity {

    @BindView(R.id.activity_theme_category_list_imgbtn_back)
    ImageButton mBtnBack;
    @BindView(R.id.activity_theme_category_list_recyclerview)
    RecyclerView mRecyclerView;

    Context mContext;

    private List<ThemeCategory> mThemeCategoryList = new ArrayList<>();
    private SelectThemeCategoryAdapter mThemeCategoryListAdapter;

    String mSelectedCategoryId;
    String mSelectedCategoryName;

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_theme_category_list);
        ButterKnife.bind(this);
        initThemeCategoryList();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        if(mThemeCategoryListAdapter == null){
            mThemeCategoryListAdapter = new SelectThemeCategoryAdapter(mThemeCategoryList, handler);
            mRecyclerView.setAdapter(mThemeCategoryListAdapter);
        }else{
            mThemeCategoryListAdapter.notifyDataSetChanged();
        }
    }

    private Handler handler =  new Handler(){
        public void handleMessage(Message message){
            if(message.what == 1){
                Intent intent = new Intent();
                intent.putExtras(message.getData());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };
}
