package com.tarasbarabash.newsreader.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns;
import static com.tarasbarabash.newsreader.Db.ArticleContract.DB_NAME;
import static com.tarasbarabash.newsreader.Db.ArticleContract.TABLE_NAME;
import static com.tarasbarabash.newsreader.Db.ArticleContract.VERSION;

/**
 * Created by Taras
 * 21.01.2018, 14:00.
 */

public class ArticlesDatabase extends SQLiteOpenHelper {
    private static volatile ArticlesDatabase sArticlesDatabase;

    public ArticlesDatabase(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static ArticlesDatabase newInstance(Context context) {
        if (sArticlesDatabase == null) {
            synchronized (ArticlesDatabase.class) {
                if (sArticlesDatabase == null) {
                    sArticlesDatabase = new ArticlesDatabase(context);
                }
            }
        }
        return sArticlesDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_ST = "CREATE TABLE " + TABLE_NAME + " (" +
                ArticlesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ArticlesColumns.TITLE + " TEXT NOT NULL, " +
                ArticlesColumns.AUTHOR + " TEXT, " +
                ArticlesColumns.DESCRIPTION + " TEXT NOT NULL, " +
                ArticlesColumns.SOURCE_NAME + " TEXT NOT NULL, " +
                ArticlesColumns.PUBLISH_DATE + " INTEGER NOT NULL, " +
                ArticlesColumns.URL + " TEXT NOT NULL, " +
                ArticlesColumns.IMAGE_URL + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_ST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
