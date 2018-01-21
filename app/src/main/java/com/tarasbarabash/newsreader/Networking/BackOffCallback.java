package com.tarasbarabash.newsreader.Networking;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Taras
 * 21.01.2018, 17:03.
 */

public class BackOffCallback<T> implements Callback<T> {
    private static final String TAG = BackOffCallback.class.getSimpleName();
    private Random mRandom = new Random();
    private OnFetchListener<T> mOnFetchListener;
    private Call<T> mCall;
    private ScheduledExecutorService mScheduledExecutorService;
    private int mNumOfRetries;

    BackOffCallback(Call<T> call,
                    OnFetchListener onFetchListener,
                    ScheduledExecutorService service) {
        this(call, 0, onFetchListener, service);
    }

    private BackOffCallback(Call<T> call,
                            int numOfRetries,
                            OnFetchListener<T> listener,
                            ScheduledExecutorService service) {
        mCall = call;
        mNumOfRetries = numOfRetries;
        mOnFetchListener = listener;
        mScheduledExecutorService = service;
    }

    @Override
    public void onResponse(@NonNull Call<T> call,
                           @NonNull Response<T> response) {
        mOnFetchListener.onResponse(call, response);
    }

    @Override
    public void onFailure(@NonNull Call<T> call,
                          @NonNull Throwable t) {
        int maxRetries = 5;
        if (mNumOfRetries < maxRetries) retryCall();
        else mOnFetchListener.onFailure(call, t);
    }

    private void retryCall() {
        final int interval = (1 << mNumOfRetries) * 1000 + mRandom.nextInt(1001);
        mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                Call<T> call = mCall.clone();
                Log.v(TAG, "Number of retry: " + (mNumOfRetries + 1)
                        + ". Next retry in " + interval + " milliseconds.");
                call.enqueue(new BackOffCallback<T>(call, mNumOfRetries + 1,
                        mOnFetchListener, mScheduledExecutorService));
            }
        }, interval, TimeUnit.MILLISECONDS);
    }

    public interface OnFetchListener<T> {
        void onResponse(Call c, Response<T> response);

        void onFailure(Call c, Throwable t);
    }
}
