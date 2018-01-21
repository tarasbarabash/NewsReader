package com.tarasbarabash.newsreader.ViewModel;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.tarasbarabash.newsreader.Account.AccountGeneral;
import com.tarasbarabash.newsreader.Activity.MainActivity;
import com.tarasbarabash.newsreader.Adapter.ArticleAdapter;
import com.tarasbarabash.newsreader.Db.ArticleContract;
import com.tarasbarabash.newsreader.Networking.ArticleSyncAdapter;
import com.tarasbarabash.newsreader.R;
import com.tarasbarabash.newsreader.databinding.ActivityMainBinding;

/**
 * Created by Taras
 * 21.01.2018, 13:41.
 */

public class MainActivityVM extends BaseViewModel<ActivityMainBinding>
        implements LoaderManager.LoaderCallbacks<Cursor>, ArticleAdapter.OnItemClickListener {
    private static final String TAG = MainActivityVM.class.getSimpleName();
    private static final int LOADER_ID = 1;

    private ArticleObserver mArticleObserver;
    private ArticleAdapter mArticleAdapter;
    private MainActivity mActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Bundle mBundle;

    public MainActivityVM(MainActivity activity, ActivityMainBinding activityMainBinding) {
        super(activity.getApplicationContext(), activityMainBinding);
        mActivity = activity;
    }

    @Override
    public void onStart() {
        getContext().getContentResolver()
                .registerContentObserver(ArticleContract.ROOT_URI,
                        true,
                        mArticleObserver);
    }

    @Override
    public void onCreate() {
        mActivity.setSupportActionBar(getBinding().toolbar);
        mArticleObserver = new ArticleObserver();
        AccountGeneral.createAccount(getContext());
        mBundle = new Bundle();
        mBundle.putString(getContext().getString(R.string.country_key), mActivity.getString(R.string.country_value));
        mBundle.putString(getContext().getString(R.string.category_key), mActivity.getString(R.string.category_value));
        ArticleSyncAdapter.performSync(mBundle);
        mArticleAdapter = new ArticleAdapter(this);
        RecyclerView recyclerView = getBinding().contentMain.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mArticleAdapter);
        mActivity.getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        ArticleRefreshListener articleRefreshListener = new ArticleRefreshListener();
        mSwipeRefreshLayout = getBinding().contentMain.swipeRefreshLayout;
        mSwipeRefreshLayout.setOnRefreshListener(articleRefreshListener);
    }

    @Override
    public void onStop() {
        if (mArticleObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(mArticleObserver);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        final String[] PROJECTION = {
                ArticleContract.ArticlesColumns.TITLE,
                ArticleContract.ArticlesColumns.DESCRIPTION,
                ArticleContract.ArticlesColumns.IMAGE_URL,
                ArticleContract.ArticlesColumns.URL,
                ArticleContract.ArticlesColumns.PUBLISH_DATE,
                ArticleContract.ArticlesColumns.SOURCE_NAME,
                ArticleContract.ArticlesColumns.AUTHOR
        };
        return new CursorLoader(getContext(),
                ArticleContract.ROOT_URI,
                PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mArticleAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mArticleAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view, String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.launchUrl(mActivity, Uri.parse(url));
    }

    private final class ArticleObserver extends ContentObserver {
        public ArticleObserver() {
            super(new Handler(Looper.myLooper()));
        }

        @Override
        public void onChange(boolean selfChange) {
            mSwipeRefreshLayout.setRefreshing(false);
            Log.i(TAG, "onChange: articles have changed!");
        }
    }

    private final class ArticleRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            ArticleSyncAdapter.performSync(mBundle);
        }
    }
}
