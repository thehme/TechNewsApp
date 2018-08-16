package com.example.android.technewsapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {
    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mSnippet;
    private long mDate;
    private String mUrl;

    public Article(String title, String section, String author, String snippet, long date, String url) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mSnippet = snippet;
        mDate = date;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public String getDate() {
        long timeInMilliseconds = mDate;
        Date dateObject = new Date(timeInMilliseconds);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        return dateFormatter.format(dateObject);
    }

    public String getUrl() {
        return mUrl;
    }
}
