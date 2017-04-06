package com.wadezhang.milkbottle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhangxix on 2017/4/6.
 */

public class ViewPagerFindFragment extends BaseFragment{

    public static ViewPagerFindFragment newInstance() {
        ViewPagerFindFragment mViewPagerFindFragment = new ViewPagerFindFragment();
        return mViewPagerFindFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_post_viewpager, container, false);
        return mView;
    }
}
