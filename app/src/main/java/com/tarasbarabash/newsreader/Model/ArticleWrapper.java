package com.tarasbarabash.newsreader.Model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns.AUTHOR;
import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns.DESCRIPTION;
import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns.IMAGE_URL;
import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns.PUBLISH_DATE;
import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns.SOURCE_NAME;
import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns.TITLE;
import static com.tarasbarabash.newsreader.Db.ArticleContract.ArticlesColumns.URL;

/**
 * Created by Taras
 * 21.01.2018, 12:40.
 */

public class ArticleWrapper extends CursorWrapper {
    public ArticleWrapper(Cursor cursor) {
        super(cursor);
    }

    public ArticleResponse.Article getArticle(int position) {
        moveToPosition(position);
        ArticleResponse.Article article = new ArticleResponse.Article();
        article.setDescription(getString(getColumnIndex(DESCRIPTION)));
        article.setAuthor(getString(getColumnIndex(AUTHOR)));
        article.setPublishedAt(new Date(getString(getColumnIndex(PUBLISH_DATE))));
        article.setTitle(getString(getColumnIndex(TITLE)));
        ArticleResponse.Source source = new ArticleResponse.Source();
        source.setName(getString(getColumnIndex(SOURCE_NAME)));
        article.setSource(source);
        article.setUrl(getString(getColumnIndex(URL)));
        article.setUrlToImage(getString(getColumnIndex(IMAGE_URL)));
        return article;
    }
}
