package com.example.android.technewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    private static final String TAG = MainActivity.class.getSimpleName();

    /** ListView for article data **/
    private ListView articleListView;

    /** TextView to show empty view **/
    private TextView emptyTextView;

    /** Adapter for the list of articles **/
    private ArticleAdapter articlesAdapter;

    /** URL for articles data from Guardian API **/
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?&show-tags=contributor&show-fields=body&api-key=835fd2f5-893c-4949-98b4-98769b665ef9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** check for internet connection and store status**/
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        /** create articles adapter **/
        articlesAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        /** find reference ListView for articles in the layout, as well as empty TextView **/
        articleListView = (ListView) findViewById(R.id.list);
        emptyTextView = (TextView) findViewById(R.id.empty_view);

        articleListView.setAdapter(articlesAdapter);

        /** set on click event on list view **/
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = articlesAdapter.getItem(position);
                String url = currentArticle.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        /** use loader if connected to internet **/
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            View progressBar = (View) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.i(TAG, "creating loader");
        return new ArticleLoader(MainActivity.this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> Articles) {
        /** hide progress bar **/
        View progressBar = (View) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText(R.string.no_data);

        /** clear adapter first **/
        articlesAdapter.clear();
        if (Articles != null && !Articles.isEmpty()) {
//            Log.i(TAG, "number of Articles: " + Articles.size());
            articlesAdapter.addAll(Articles);
        } else {
            articleListView.setEmptyView(emptyTextView);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        /** reset adapter **/
        articlesAdapter.clear();
    }
}
