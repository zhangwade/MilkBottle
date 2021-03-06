/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-28 上午10:24
 */

package com.wadezhang.milkbottle.book;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class BookShopAdapter extends RecyclerView.Adapter<BookShopAdapter.ViewHolder> {

    private Context mContext;

    private List<Book> mBookList;

    public BookShopAdapter(List<Book> bookList){
        mBookList = bookList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_book_viewpager_item, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mBookList != null){
            Book mBook = mBookList.get(position);
            holder.mBookName.setText(mBook.getName());
            ImageLoader.with(mContext, mBook.getPhoto().getFileUrl(), holder.mBookImage);
        }
    }

    @Override
    public int getItemCount(){
        return mBookList == null ? 0 : mBookList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.fragment_book_viewpager_item_img_photo) ImageView mBookImage;
        @BindView(R.id.fragment_book_viewpager_item_text_name) TextView mBookName;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
