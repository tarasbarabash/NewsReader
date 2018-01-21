package com.tarasbarabash.newsreader.Networking;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Taras
 * 21.01.2018, 16:34.
 */

public class ArticleApiFactory {
    private static OkHttpClient sOkHttpClient;
    private static volatile ArticleClient sArticleClient;

    private static final String BASE_URL = "https://newsapi.org/";

    public static ArticleClient getArticleClient() {
        ArticleClient articleClient = sArticleClient;
        if (articleClient == null) {
            synchronized (ArticleApiFactory.class) {
                articleClient = sArticleClient;
                if (articleClient == null)
                    articleClient = buildRetrofit().create(ArticleClient.class);
            }
        }
        return articleClient;
    }

    @NonNull
    private static Retrofit buildRetrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient getClient() {
        OkHttpClient okHttpClient = sOkHttpClient;
        if (sOkHttpClient == null) {
            synchronized (ArticleApiFactory.class) {
                okHttpClient = sOkHttpClient;
                if (okHttpClient == null)
                    okHttpClient = new OkHttpClient();
            }
        }
        return okHttpClient;
    }
}
