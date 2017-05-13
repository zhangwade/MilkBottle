package com.wadezhang.milkbottle.book;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class BookShopFragment extends BaseFragment implements BookShopContract.View {

    @BindView(R.id.swipe_target) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_book_viewpager_swipe_to_load_layout) SwipeToLoadLayout mSwipeToLoadLayout;

    private BookShopAdapter mBookShopAdapter;
    private List<Book> mBookList = new ArrayList<>();

    private BookShopContract.Presenter mBookShopPresenter;

    public static BookShopFragment newInstance() {
        BookShopFragment mBookShopFragment = new BookShopFragment();
        return mBookShopFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_book_viewpager, container, false);
        ButterKnife.bind(this, mView);
        mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mBookShopAdapter = new BookShopAdapter(mBookList);
        mRecyclerView.setAdapter(mBookShopAdapter);
        return mView;
    }

    @Override
    public void setPresenter(BookShopContract.Presenter presenter){
        mBookShopPresenter = presenter;
    }

    @Override
    public void onResume(){
        super.onResume();
        //mBookShopPresenter.start();
        autoRefresh();
    }

    @Override
    public void updateAdapter(List<Book> bookList){
        mBookList.clear();
        mBookList.addAll(bookList);
        //for(Book book : bookList) mBookList.add(book);
        mBookShopAdapter.notifyDataSetChanged();
    }

    public class RefreshListener implements OnRefreshListener{

        @Override
        public void onRefresh(){
            mBookShopPresenter.start();
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener{

        @Override
        public void onLoadMore(){
            mSwipeToLoadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeToLoadLayout.setLoadingMore(false);
                    Toast.makeText(getContext(), "no more data", Toast.LENGTH_SHORT);
                }
            }, 2000);
        }
    }

    public void autoRefresh(){
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }
}
