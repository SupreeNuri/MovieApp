package com.example.android.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieContract {
    public static final String AUTHORITY = "com.example.android.moviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIE = "favorite_movie";

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIE).build();

        public static final String TABLE_NAME = "favoriteMovie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_POSTER_URL = "poster_path";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_PLOT_SYNOPSIS = "plot_synopsis";

        public static final int INDEX_MOVIE_ID = 0;
        public static final int INDEX_MOVIE_TITLE = 1;
        public static final int INDEX_MOVIE_POSTER_URL = 2;
        public static final int INDEX_MOVIE_VOTE_AVERAGE = 3;
        public static final int INDEX_MOVIE_RELEASE_DATE = 4;
        public static final int INDEX_MOVIE_PLOT_SYNOPSIS = 5;

        public static final String[] MOVIE_COLUMNS = {
                COLUMN_MOVIE_ID,
                COLUMN_MOVIE_TITLE,
                COLUMN_MOVIE_POSTER_URL,
                COLUMN_MOVIE_VOTE_AVERAGE,
                COLUMN_MOVIE_RELEASE_DATE,
                COLUMN_MOVIE_PLOT_SYNOPSIS
        };
    }
}
