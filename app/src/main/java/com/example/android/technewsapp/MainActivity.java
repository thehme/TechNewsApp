package com.example.android.technewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

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
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // use getString to retrive a String value from the preferences
        // the second parameter is the default value for this preference
        String articleCategory = sharedPrefs.getString(
                getString(R.string.filters_categories_key),
                getString(R.string.filters_categories_default));

        // use parse to break apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // use buildUpon to prepare the baseUri above to add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // append query parameters needed to get article data not filtered
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "body");
        // TODO: replace user's API KEY
        uriBuilder.appendQueryParameter("api-key", "USER_KEY_HERE");

        // append query parameters and its value
        if (!articleCategory.equals("all")) {
            uriBuilder.appendQueryParameter("section", articleCategory);
        }

        // Log.i(TAG, "url is - " + uriBuilder.toString());
        return new ArticleLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> Articles) {
        /** hide progress bar **/
        View progressBar = (View) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        /** clear adapter first **/
        articlesAdapter.clear();
        if (Articles != null && !Articles.isEmpty()) {
            articlesAdapter.addAll(Articles);
        } else {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_data);
            articleListView.setEmptyView(emptyTextView);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        /** reset adapter **/
        articlesAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's filters menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Filters Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method specifies action when a Filters Menu option is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        // which item was selected based on what was passed in
        int id = item.getItemId();
        // there is only one option at the mome
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, FiltersActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
