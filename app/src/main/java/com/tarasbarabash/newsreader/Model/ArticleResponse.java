package com.tarasbarabash.newsreader.Model;

import android.content.ContentValues;

import java.io.Serializable;
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
 * 21.01.2018, 13:49.
 */

public class ArticleResponse {
    private String status;
    private int totalResults;
    private Article[] articles;

    public ArticleResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public Article[] getArticles() {
        return articles;
    }

    public void setArticles(Article[] articles) {
        this.articles = articles;
    }

    public static class Article implements Serializable {
        private Source source;
        private String author;
        private String title;
        private String description;
        private String url;
        private String urlToImage;
        private Date publishedAt;

        public Article() {
        }

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        public Date getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(Date publishedAt) {
            this.publishedAt = publishedAt;
        }
    }

    public static class Source implements Serializable {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static ContentValues[] listToContentValues(ArticleResponse responses) {
        ContentValues[] contentValues = new ContentValues[responses.getArticles().length];
        for (int i = 0; i < contentValues.length; i++) {
            Article article = responses.getArticles()[i];
            contentValues[i] = new ContentValues();
            contentValues[i].put(AUTHOR, article.getAuthor());
            contentValues[i].put(DESCRIPTION, article.getDescription());
            contentValues[i].put(TITLE, article.getTitle());
            contentValues[i].put(SOURCE_NAME, article.getSource().getName());
            contentValues[i].put(URL, article.getUrl());
            contentValues[i].put(IMAGE_URL, article.getUrlToImage());
            contentValues[i].put(PUBLISH_DATE, String.valueOf(article.getPublishedAt()));
        }
        return contentValues;
    }
}
