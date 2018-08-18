package com.example.android.technewsapp;
import android.text.Html;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {
    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mSnippet;
    private String mDate;
    private String mUrl;

    public Article(String title, String section, String author, String snippet, String date, String url) {
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
        int articleLength = mSnippet.length();
        String snippetSubString = "";
        if (articleLength > 0) {
            snippetSubString = mSnippet.substring(3, articleLength/6) + "...";
        }
        return snippetSubString;
    }

    public String getDate() {
        String destFormatDateString = "";
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date sourceFormatDate = sourceFormat.parse(mDate);
            SimpleDateFormat destFormat = new SimpleDateFormat("dd/MM/yy");
            destFormatDateString = destFormat.format(sourceFormatDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return destFormatDateString;
    }

    public String getUrl() {
        return mUrl;
    }
}
