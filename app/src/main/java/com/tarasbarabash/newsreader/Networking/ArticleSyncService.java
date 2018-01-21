package com.tarasbarabash.newsreader.Networking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Taras
 * 21.01.2018, 17:33.
 */

public class ArticleSyncService extends Service {
    private static final Object LOCK = new Object();
    private static ArticleSyncAdapter sSyncAdapter;

    @Override
    public void onCreate() {
        synchronized (LOCK) {
            sSyncAdapter = new ArticleSyncAdapter(this, false);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
