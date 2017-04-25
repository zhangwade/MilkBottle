package com.wadezhang.milkbottle.book;

import android.util.Log;

import com.wadezhang.milkbottle.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class BookShelfPresenter implements BookShelfContract.Presenter {

    private BookShelfContract.View mBookShelfView;
    private List<Book> mBookList;

    public BookShelfPresenter(BookShelfContract.View view){
        mBookShelfView = view;
        mBookShelfView.setPresenter(this);
    }

    @Override
    public void start(){
        getBook();
    }

    public void getBook(){
        BmobQuery<BookShelf> mQuery = new BmobQuery<BookShelf>();
        User mUser = new User();
        mUser.setObjectId("C0NeXXX3");
        mQuery.addWhereEqualTo("user", new BmobPointer(mUser));
        mQuery.include("book.photo,book.name");
        mQuery.findObjects(new FindListener<BookShelf>() {
            @Override
            public void done(List<BookShelf> list, BmobException e) {
                if(e == null){
                    List<Book> mList = new ArrayList<>();
                    for(BookShelf bookShelf : list) mList.add(bookShelf.getBook());
                    mBookList = mList;
                }else {
                    Log.i(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void showBook(){
        if(mBookList != null){
            mBookShelfView.updateAdapter(mBookList);
        }
    }
}
