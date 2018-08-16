package com.example.android.technewsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(Activity context, List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // check if the existing view is being used, otherwise, inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // get Article object located at the position in the list
        Article currentArticle = getItem(position);

        // find the textView in the list_item.xml layout with the ID article_title
        TextView articleTitleTextView = (TextView) listItemView.findViewById(R.id.article_title);
        articleTitleTextView.setText(currentArticle.getTitle());
        // find the textView in the list_item.xml layout with the ID article_author
        TextView articleAuthorTextView = (TextView) listItemView.findViewById(R.id.article_author);
        if (currentArticle.getAuthor() != null) {
            articleAuthorTextView.setText(currentArticle.getAuthor());
        }
        // find the textView in the list_item.xml layout with the ID section_name
        TextView articleSectionNameTextView = (TextView) listItemView.findViewById(R.id.section_name);
        if (currentArticle.getSection() != null) {
            articleSectionNameTextView.setText(currentArticle.getSection());
        }
        // find the textView in the list_item.xml layout with the ID date
        TextView articleDateTextView = (TextView) listItemView.findViewById(R.id.date);
        articleDateTextView.setText(currentArticle.getDate());
        // find the textView in the list_item.xml layout with the ID article_snippet
        TextView articleSnippetTextView = (TextView) listItemView.findViewById(R.id.article_snippet);
        if (currentArticle.getSnippet() != null) {
            articleSnippetTextView.setText(currentArticle.getSnippet());
        }

        return listItemView;
    }
}
