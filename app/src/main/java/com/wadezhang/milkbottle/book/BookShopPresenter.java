package com.wadezhang.milkbottle.book;

import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class BookShopPresenter implements BookShopContract.Presenter {

    private BookShopContract.View mBookShopView;
    private List<Book> mBookList;

    public BookShopPresenter(BookShopContract.View view){
        mBookShopView = view;
        mBookShopView.setPresenter(this);
    }

    @Override
    public void start(){
        getBook();
    }

    public void getBook(){
        BmobQuery<Book> mQuery = new BmobQuery<Book>();
        mQuery.addQueryKeys("objectId,photo,name");
        mQuery.findObjects(new FindListener<Book>() {
            @Override
            public void done(List<Book> list, BmobException e) {
                if(e == null){
                    mBookList = list;
                    showBook();
                }else{
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void showBook(){
        if(mBookList != null){
            mBookShopView.updateAdapter(mBookList);
        }
    }
}
