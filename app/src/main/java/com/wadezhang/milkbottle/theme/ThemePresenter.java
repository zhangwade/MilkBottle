/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-6 下午3:20
 */

package com.wadezhang.milkbottle.theme;

import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class ThemePresenter implements ThemeContract.Presenter {

    private ThemeContract.View mThemeView;
    private List<Theme> mThemeList;
    private int mHotThemeItem;
    private int mNewestThemeItem;
    private int mSecondGroupPosition;

    public ThemePresenter(ThemeContract.View view){
        mThemeView = view;
        mThemeView.setPresenter(this);
    }

    @Override
    public void start(int hotItem, int newestItem){
        mHotThemeItem = hotItem;
        mNewestThemeItem = newestItem;
        BmobQuery<Theme> mHotThemeQuery = new BmobQuery<>();
        mHotThemeQuery.order("-postCount");
        mHotThemeQuery.addQueryKeys("objectId,name,postCount");
        mHotThemeQuery.setLimit(mHotThemeItem);
        mHotThemeQuery.findObjects(new FindListener<Theme>() {
            @Override
            public void done(List<Theme> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()){
                        mThemeList = list;
                        mSecondGroupPosition = mThemeList.size() + 1;
                        getNewestItem();
                    }else{
                        mThemeView.showToast("暂时还没有话题~~");
                    }
                }else{
                    mThemeView.showToast("网络出了点小差~~");
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void getNewestItem(){
        BmobQuery<Theme> mNewestThemeQuery = new BmobQuery<>();
        mNewestThemeQuery.order("-updatedAt");
        mNewestThemeQuery.addQueryKeys("objectId,name,postCount");
        mNewestThemeQuery.setLimit(mNewestThemeItem);
        mNewestThemeQuery.findObjects(new FindListener<Theme>() {
            @Override
            public void done(List<Theme> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()){
                        mThemeList.addAll(list);
                        showTheme();
                    }else{
                        mThemeView.showToast("暂时还没有话题~~");
                    }
                }else{
                    mThemeView.showToast("网络出了点小差~~");
                }
            }
        });
    }

    @Override
    public void showTheme(){
        mThemeView.updateAdapter(mThemeList, mSecondGroupPosition);
    }
}
