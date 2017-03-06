package com.wadezhang.milkbottle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by zhangxix on 2017/3/2.
 */

public class BaseFragment extends Fragment {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"; //用于保存该Fragment是否隐藏的状态

    protected Activity mActivity;  //避免getActivity()空指针

    public static BaseFragment newInstance() {
        BaseFragment mBaseFragment = new BaseFragment();
        return mBaseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) { //重新切换到该Fragment
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN); //获取该Fragment原本的状态
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) { //判断该隐藏还是显示
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden()); //保存该Fragment是否隐藏的状态
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }
}
