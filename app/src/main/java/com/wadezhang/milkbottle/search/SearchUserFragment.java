package com.wadezhang.milkbottle.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

/**
 * Created by zhangxix on 2017/4/14.
 */

public class SearchUserFragment extends BaseFragment {

    public static SearchUserFragment newInstance() {
        SearchUserFragment mSearchUserFragment = new SearchUserFragment();
        return mSearchUserFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_search_viewpager_user_item, container, false);
        return mView;
    }
}
