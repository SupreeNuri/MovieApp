package com.example.android.moviesapp;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapp.model.Review;
import com.example.android.movieapp.utilities.NetworkUtils;
import com.example.android.movieapp.utilities.ReviewJsonUtils;

import java.net.URL;
import java.util.List;

class FetchReviewsTask extends AsyncTask<Integer, Void, List<Review>> {

    private static final String LOG_TAG = FetchReviewsTask.class.getSimpleName();

    private final Listener fetchFinishedListener;

    interface Listener {
        void onFetchFinished(List<Review> movies);
    }

    public FetchReviewsTask(Listener fetchFinishedListener){
        this.fetchFinishedListener = fetchFinishedListener;
    }

    @Override
    protected List<Review> doInBackground(Integer... params) {
        int movieId = params[0];

        URL reviewsRequestUrl = NetworkUtils.buildUrlReview(movieId);

        try {
            String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);
            return ReviewJsonUtils.getReviewModelsFromJson(jsonReviewsResponse);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Review> movies) {
        if (movies != null) {
            fetchFinishedListener.onFetchFinished(movies);
        }
    }
}
