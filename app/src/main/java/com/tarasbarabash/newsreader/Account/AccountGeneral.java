package com.tarasbarabash.newsreader.Account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tarasbarabash.newsreader.BuildConfig;
import com.tarasbarabash.newsreader.Networking.ArticleSyncAdapter;
import com.tarasbarabash.newsreader.R;

/**
 * Created by Taras
 * 21.01.2018, 10:19.
 */

public final class AccountGeneral {
    private static final String ACCOUNT_TYPE = BuildConfig.APPLICATION_ID;
    private static final String ACCOUNT_NAME = "RssReader";
    private static final String TAG = AccountGeneral.class.getSimpleName();

    public static Account getAccount() {
        return new Account(ACCOUNT_NAME, ACCOUNT_TYPE);
    }

    public static void createAccount(Context context) {
        boolean created = false;
        Account account = getAccount();
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putString(context.getString(R.string.category_key), context.getString(R.string.category_value));
        bundle.putString(context.getString(R.string.country_key), context.getString(R.string.country_value));

        if (accountManager.addAccountExplicitly(account, null, null)) {
            final int SYNC_INTERVAL = 60 * 60;
            ContentResolver.setIsSyncable(account, ACCOUNT_TYPE, 1);
            ContentResolver.setSyncAutomatically(account, ACCOUNT_TYPE, true);
            ContentResolver.addPeriodicSync(account, ACCOUNT_TYPE, bundle, SYNC_INTERVAL);
            created = true;
        }

        Log.i(TAG, "createAccount: created = " + created);
        if (created) ArticleSyncAdapter.performSync(bundle);
    }
}
