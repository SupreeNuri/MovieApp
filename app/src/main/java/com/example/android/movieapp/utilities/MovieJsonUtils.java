package com.example.android.movieapp.utilities;

import android.util.Log;

import com.example.android.movieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieJsonUtils {
    private static final String LOG_TAG = MovieJsonUtils.class.getSimpleName();

    public static List<Movie> getMovieModelsFromJson(String moviesJsonStr) throws JSONException{


        final String MOVIE_RESULTS = "results";

        final String RESULT_TITLE = "title";
        final String RESULT_VOTE_AVERAGE = "vote_average";
        final String RESULT_POSTER_PATH = "poster_path";
        final String RESULT_OVERVIEW = "overview";
        final String RESULT_RELEASE_DATE =  "release_date";

        final String BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w185/";

        List<Movie> parsedMoviesData;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray moviesArray = moviesJson.getJSONArray(MOVIE_RESULTS);

        parsedMoviesData = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            Movie movieModel = new Movie();

            JSONObject movieJson = moviesArray.getJSONObject(i);

            movieModel.setTitle(movieJson.getString(RESULT_TITLE));
            movieModel.setVoteAverage(movieJson.getString(RESULT_VOTE_AVERAGE));

            String posterURL = BASE_POSTER_PATH + movieJson.getString(RESULT_POSTER_PATH);
            movieModel.setPosterURL(posterURL);
            movieModel.setOverview(movieJson.getString(RESULT_OVERVIEW));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date releaseDate;
            try {
                releaseDate = format.parse(movieJson.getString(RESULT_RELEASE_DATE));

                SimpleDateFormat dfShortMonth = new SimpleDateFormat("yyyy MMM dd", Locale.US);
                String strReleaseDate = dfShortMonth.format(releaseDate);
                movieModel.setReleaseDate(strReleaseDate);
            } catch (ParseException e) {
                Log.d(LOG_TAG, e.toString());
            }

            parsedMoviesData.add(movieModel);
        }

        return parsedMoviesData;
    }
}
