package com.wadezhang.milkbottle.book;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class BookShopAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private List<Book> mBookList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView mCardView;
        ImageView mBookImage;
        TextView mBookName;

        public ViewHolder(View view){
            super(view);
            mCardView = (CardView) view;
            mBookImage = (ImageView) view.findViewById()
        }
    }
}
