package com.tarasbarabash.newsreader.Networking;

import com.tarasbarabash.newsreader.BuildConfig;
import com.tarasbarabash.newsreader.Model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Taras
 * 21.01.2018, 16:40.
 */

public interface ArticleClient {
    @GET("v2/top-headlines?apiKey=" + BuildConfig.api_key)
    Call<ArticleResponse> getArticles(@Query("country") String country,
                                      @Query("category") String category);
}
