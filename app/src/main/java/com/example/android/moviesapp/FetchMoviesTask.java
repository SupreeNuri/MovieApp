package com.example.android.moviesapp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.utilities.MovieJsonUtils;
import com.example.android.movieapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private final Listener fetchFinishedListener;

    interface Listener {
        void onFetchFinished(List<Movie> movies);
    }

    public FetchMoviesTask(Listener fetchFinishedListener){
        this.fetchFinishedListener = fetchFinishedListener;
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        URL moviesRequestUrl = NetworkUtils.buildUrl();

        try {
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
            return MovieJsonUtils.getMovieModelsFromJson(jsonMoviesResponse);

        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            fetchFinishedListener.onFetchFinished(movies);
        }
    }
}
