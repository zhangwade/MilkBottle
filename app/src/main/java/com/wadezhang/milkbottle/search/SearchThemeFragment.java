package com.wadezhang.milkbottle.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadezhang.milkbottle.R;

/**
 * Created by zhangxix on 2017/4/14.
 */

public class SearchThemeFragment extends Fragment {

    public static SearchThemeFragment newInstance() {
        SearchThemeFragment mSearchThemeFragment = new SearchThemeFragment();
        return mSearchThemeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_search_viewpager_theme_item, container, false);
        return mView;
    }
}
