package com.tarasbarabash.newsreader.Db;

import android.net.Uri;
import android.provider.BaseColumns;

import com.tarasbarabash.newsreader.BuildConfig;

/**
 * Created by Taras
 * 21.01.2018, 13:54.
 */

public final class ArticleContract {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    public static final String TABLE_NAME = "articles";
    public static final Uri ROOT_URI = BASE_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static final String DB_NAME = TABLE_NAME + ".db";
    public static final int VERSION = 3;

    public static final class ArticlesColumns implements BaseColumns {
        public static final String SOURCE_NAME = "sourceName";
        public static final String AUTHOR = "author";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String URL = "url";
        public static final String IMAGE_URL = "image_url";
        public static final String PUBLISH_DATE = "publishDate";
    }
}
