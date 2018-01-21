package com.tarasbarabash.newsreader.Db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.tarasbarabash.newsreader.Db.ArticleContract.TABLE_NAME;

/**
 * Created by Taras
 * 21.01.2018, 14:11.
 */

public class ArticleProvider extends ContentProvider {
    private static UriMatcher sUriMatcher;
    private SQLiteDatabase mDb;

    private static final int TABLE_CODE = 101;
    private static final int ARTICLE_CODE = 102;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ArticleContract.AUTHORITY, TABLE_NAME, TABLE_CODE);
        sUriMatcher.addURI(ArticleContract.AUTHORITY, TABLE_NAME + "#", ARTICLE_CODE);
    }

    @Override
    public boolean onCreate() {
        mDb = ArticlesDatabase.newInstance(getContext()).getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] strings,
                        @Nullable String s,
                        @Nullable String[] strings1,
                        @Nullable String s1) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case TABLE_CODE:
                cursor = mDb.query(TABLE_NAME,
                        strings,
                        s,
                        strings1,
                        null,
                        null,
                        s1);
                break;
            case ARTICLE_CODE:
                long id = ContentUris.parseId(uri);
                cursor = mDb.query(TABLE_NAME,
                        strings,
                        ArticleContract.ArticlesColumns._ID + "=?",
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues contentValues) {
        Uri rUri;
        long id;
        switch (sUriMatcher.match(uri)) {
            case TABLE_CODE:
                id = mDb.insert(TABLE_NAME, null, contentValues);
                rUri = ContentUris.withAppendedId(uri, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rUri != null)
            getContext().getContentResolver().notifyChange(rUri, null);
        return rUri;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String s,
                      @Nullable String[] strings) {
        int rows;
        switch (sUriMatcher.match(uri)) {
            case TABLE_CODE:
                rows = mDb.delete(TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rows != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues contentValues,
                      @Nullable String s,
                      @Nullable String[] strings) {
        int rows;
        switch (sUriMatcher.match(uri)) {
            case TABLE_CODE:
                rows = mDb.update(TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rows != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rows;
    }
}
