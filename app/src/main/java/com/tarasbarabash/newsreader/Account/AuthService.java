package com.tarasbarabash.newsreader.Account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Taras
 * 21.01.2018, 10:29.
 */

public class AuthService extends Service {
    private AccountAuth mAccountAuth;

    public AuthService() {
        mAccountAuth = new AccountAuth(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAccountAuth.getIBinder();
    }
}
