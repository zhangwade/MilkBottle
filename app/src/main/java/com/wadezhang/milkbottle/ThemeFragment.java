package com.wadezhang.milkbottle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhangxix on 2017/3/6.
 */

public class ThemeFragment extends BaseFragment {

    public static ThemeFragment newInstance() {
        ThemeFragment mThemeFragment = new ThemeFragment();
        return mThemeFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_post_item, container, false);
        return mView;
    }
}
