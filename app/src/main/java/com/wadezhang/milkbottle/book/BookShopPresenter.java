package com.wadezhang.milkbottle.book;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class BookShopPresenter implements BookShopContract.Presenter {

    private BookShopContract.View mBookShopView;
    private Book mBook;

    public BookShopPresenter(BookShopContract.View bookShopView){
        mBookShopView = bookShopView;
    }

    @Override
    public void start(){
        getBook();
    }

    public void getBook(){
        mBookShopView.showLoading();
    }

    @Override
    public void showBook(){

    }
}
