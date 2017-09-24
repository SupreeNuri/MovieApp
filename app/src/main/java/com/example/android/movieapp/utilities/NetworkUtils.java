package com.example.android.movieapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

public class NetworkUtils {
    private static final String API_KEY = "Your key here";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String PATH_VIDEOS = "videos";

    public static final String MODE_POPULAR = "popular";
    public static final String MODE_TOP_RATED = "top_rated";
    public static String sortPath = MODE_POPULAR;

    private final static String API_KEY_PARAM = "api_key";

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(sortPath)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.d(TAG, "");
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlTrailerVideos(String id){
        Uri trailerUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(API_KEY_PARAM,API_KEY)
                .build();

        try {
            URL trailerVideosQueryUrl = new URL(trailerUri.toString());
            Log.v(TAG, "URL: " + trailerVideosQueryUrl);
            return trailerVideosQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
