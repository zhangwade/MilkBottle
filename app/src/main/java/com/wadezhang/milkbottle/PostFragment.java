package com.wadezhang.milkbottle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhangxix on 2017/3/6.
 */

public class PostFragment extends BaseFragment {

    public static PostFragment newInstance() {
        PostFragment mPostFragment = new PostFragment();
        return mPostFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_test, container, false);
        TextView mTextView = (TextView) mView.findViewById(R.id.text_fragment_test);
        mTextView.setText("1");
        return mView;
    }
}