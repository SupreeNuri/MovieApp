package com.example.android.moviesapp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapp.model.Trailer;
import com.example.android.movieapp.utilities.NetworkUtils;
import com.example.android.movieapp.utilities.TrailerJsonUtils;

import java.net.URL;
import java.util.List;

class FetchTrailersTask extends AsyncTask<Integer, Void, List<Trailer>> {

    private static final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

    private final Listener fetchFinishedListener;

    interface Listener {
        void onFetchFinished(List<Trailer> trailers);
    }

    public FetchTrailersTask(Listener fetchFinishedListener){
        this.fetchFinishedListener = fetchFinishedListener;
    }

    @Override
    protected List<Trailer> doInBackground(Integer... params) {
        int movieId = params[0];

        URL trailersRequestUrl = NetworkUtils.buildUrlTrailer(movieId);

        try {
            String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);
            return TrailerJsonUtils.getTrailerModelsFromJson(jsonTrailersResponse);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Trailer> trailers) {
        if (trailers != null) {
            fetchFinishedListener.onFetchFinished(trailers);
        }
    }
}
