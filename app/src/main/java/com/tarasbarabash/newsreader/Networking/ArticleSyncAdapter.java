package com.tarasbarabash.newsreader.Networking;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.tarasbarabash.newsreader.Account.AccountGeneral;
import com.tarasbarabash.newsreader.BuildConfig;
import com.tarasbarabash.newsreader.Db.ArticleContract;
import com.tarasbarabash.newsreader.Model.ArticleResponse;
import com.tarasbarabash.newsreader.R;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Taras
 * 21.01.2018, 16:09.
 */

public class ArticleSyncAdapter extends AbstractThreadedSyncAdapter
        implements BackOffCallback.OnFetchListener<ArticleResponse> {
    private static final String TAG = ArticleSyncAdapter.class.getSimpleName();

    private ContentResolver mContentResolver;

    public ArticleSyncAdapter(Context context,
                              boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = getContext().getContentResolver();
    }

    public ArticleSyncAdapter(Context context,
                              boolean autoInitialize,
                              boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = getContext().getContentResolver();
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle bundle,
                              String s,
                              ContentProviderClient contentProviderClient,
                              SyncResult syncResult) {
        Log.i(TAG, "onPerformSync: sync is started!");
        String country = bundle.getString(getContext().getString(R.string.country_key));
        String category = bundle.getString(getContext().getString(R.string.category_key));
        Call<ArticleResponse> call = ArticleApiFactory.getArticleClient()
                .getArticles(country, category);
        call.enqueue(new BackOffCallback<>(call,
                this,
                Executors.newScheduledThreadPool(1)));
    }

    @Override
    public void onResponse(Call c, Response<ArticleResponse> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                ArticleResponse responses = response.body();
                mContentResolver.delete(ArticleContract.ROOT_URI,
                        null,
                        null);
                mContentResolver.bulkInsert(ArticleContract.ROOT_URI,
                        ArticleResponse.listToContentValues(responses));
                mContentResolver.notifyChange(ArticleContract.ROOT_URI,
                        null,
                        false);
            }
            else onFailure(c, new Exception(String.valueOf(response.code())));
        }
    }

    @Override
    public void onFailure(Call c, Throwable t) {
        Log.i(TAG, "onFailure: req url: " + c.request().url());
        Log.e(TAG, "onFailure: an error occurred: " + t.getLocalizedMessage());
    }

    public static void performSync(Bundle bundle) {
        ContentResolver.requestSync(AccountGeneral.getAccount(), BuildConfig.APPLICATION_ID, bundle);
    }
}
