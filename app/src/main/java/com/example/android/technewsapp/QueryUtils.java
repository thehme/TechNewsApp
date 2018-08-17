package com.example.android.technewsapp;

import android.app.LoaderManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    public static final String TAG = QueryUtils.class.getSimpleName();

    public static List<Article> fetchArticlesData(String requestedUrl) {
        // Create URL object using local method
        URL url = createUrl(requestedUrl);

        // Perform HTTP request to the URL and receive the JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Error closing input stream", e);
        }

        List<Article> articles = extractArticles(jsonResponse);
        return articles;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the articles JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    private static List<Article> extractArticles(String jsonResponse) {
        // if no url, return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // create an empty List of Articles
        List<Article> articles = new ArrayList<>();

        // try to parse jsonResponse
        try {
            JSONObject responseJsonObject = new JSONObject(jsonResponse);
            JSONArray articlesArray = responseJsonObject.getJSONArray("results");

            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject currentArticleJson = articlesArray.getJSONObject(i);
                JSONObject currentArticleTags = currentArticleJson.getJSONObject("tags");
                JSONObject currentArticleFields = currentArticleJson.getJSONObject("fields");
                String title = currentArticleJson.getString("webTitle");
                String section = currentArticleJson.getString("sectionName");
                String author = currentArticleTags.getString("webTitle");
                String snippet = currentArticleFields.getString("body");
                long date = currentArticleJson.getLong("webPublicationDate");
                String url = currentArticleJson.getString("webUrl");

                Article article = new Article(title, section, author, snippet, date, url);
                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing articles JSON", e);
        }
        return articles;
    }
}
