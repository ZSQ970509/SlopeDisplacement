package com.example.administrator.slopedisplacement.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 懒加载的Fragment
 */

public abstract class BaseLazyFragment extends RxFragment {
    private FragmentActivity mActivity;//防止使用getActivity()为空
    private Unbinder mUnbinder;
    // Fragment是否已经绑定Layout和初始化视图
    protected boolean mIsInitView = false;
    // Fragment是否第一次懒加载
    protected boolean mIsFirstLazyLoad = true;
    // Fragment是否是可见
    protected boolean mIsFragmentVisible = false;
    //数据是否为空
    protected boolean mIsDataEmpty = false;

    /**
     * Fragment显示和隐藏时被调用
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onVisible();
            mIsFragmentVisible = true;
//            if (mIsInitView) {
//                mIsFirstVisible = true;
//            }
            lazyLoad();
        } else {
            mIsFragmentVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (FragmentActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View parentView = inflater.inflate(getLayoutResId(), container, false);
        mActivity = getSupportActivity();
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        mIsInitView = true;
        lazyLoad();
    }

    @LayoutRes
    public abstract int getLayoutResId();

    /**
     * 初始化views(完成视图创建后，初始控件)
     */
    public abstract void initView();

    /**
     * 每次Fragment被显示时调用
     */
    protected void onVisible() {

    }

    /**
     * 每次Fragment被隐藏时调用
     */
    protected void onInvisible() {
    }

    protected void lazyLoad() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    /**
     * 数据为空(不调用 initView()和lazyLoadDate())
     */
    public void setDataEmpty() {
        this.mIsDataEmpty = true;
    }

    public FragmentActivity getSupportActivity() {
        return super.getActivity();
    }

    /**
     * 显示吐司()
     *
     * @param msg 内容
     */
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
    }
    /*
    tsetUserVisibleHin和Fragment的先后顺序
    setUserVisibleHint(false)   (Fragment1)
    setUserVisibleHint(false)   (Fragment2)
    setUserVisibleHint(false)   (Fragment3)
    setUserVisibleHint(true)    (Fragment1)
    onAttach                    (Fragment1)
    onCreateView                (Fragment1)
    ....                        (Fragment1)
    onAttach                    (Fragment2)
    onCreateView                (Fragment2)
    ...                         (Fragment2)
    */
}