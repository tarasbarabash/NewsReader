package com.tarasbarabash.newsreader.ViewModel;

import android.content.Context;

/**
 * Created by Taras
 * 21.01.2018, 13:39.
 */

public abstract class BaseViewModel<T> {
    private Context mContext;
    private T mBinding;

    public BaseViewModel(Context context, T dataBinding) {
        mContext = context;
        mBinding = dataBinding;
    }

    public abstract void onStart();
    public abstract void onCreate();
    public abstract void onStop();

    public Context getContext() {
        return mContext;
    }

    public T getBinding() {
        return mBinding;
    }
}
