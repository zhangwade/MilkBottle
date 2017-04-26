package com.wadezhang.milkbottle.book;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class BookContentPresenter implements BookContentContract.Presenter {

    private BookContentContract.View mBookContentView;

    public BookContentPresenter(BookContentContract.View view){
        mBookContentView = view;
        mBookContentView.setPresenter(this);
    }

    @Override
    public void start(){}

    @Override
    public void getPreviousPage(){}

    @Override
    public void getNextPage(){}

    @Override
    public void showBookContent(){
        mBookContentView.updateBookContent();
    }
}
