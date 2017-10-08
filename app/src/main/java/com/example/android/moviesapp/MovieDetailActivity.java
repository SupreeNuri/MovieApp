package com.example.android.moviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.movieapp.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.model.Review;
import com.example.android.movieapp.model.Trailer;
import com.example.android.moviesapp.adapter.MovieTrailerListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailerListAdapter.ListItemClickListener {
    @BindView(R.id.tv_movie_title)
    TextView movieTitleTextView;
    @BindView(R.id.movie_poster)
    ImageView moviePosterImageView;
    @BindView(R.id.tv_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.tv_vote_average)
    TextView voteAverageTextView;
    @BindView(R.id.button_mark_as_favorite)
    Button mButtonMarkAsFavorite;
    @BindView(R.id.button_remove_from_favorite)
    Button mButtonRemoveFromFavorites;
    @BindView(R.id.tv_plot_synopsis)
    TextView plotSynopsisTextView;

    @BindView(R.id.rvMovieTrailers)
    RecyclerView rvMovieTrailers;

    private Movie mMovie;
    private ArrayList<Trailer> trailerList;
    private MovieTrailerListAdapter mAdapter;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupActionBar();
        ButterKnife.bind(this);

        mMovie = getIntent().getParcelableExtra("movie");

        setMovieDetail();
        setMovieTrailerViews();
        fetchMovieTrailers();
        fetchMovieReviews();
    }

    private void setMovieDetail() {
        String voteAverage = getString(R.string.vote_average, mMovie.getVoteAverage());
        movieTitleTextView.setText(mMovie.getTitle());
        Glide.with(this).load(mMovie.getPosterURL()).into(moviePosterImageView);
        releaseDateTextView.setText(mMovie.getReleaseDate());
        voteAverageTextView.setText(voteAverage);
        plotSynopsisTextView.setText(mMovie.getOverview());
    }

    private void setMovieTrailerViews() {
        trailerList = new ArrayList<>();
        rvMovieTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MovieTrailerListAdapter(trailerList, this);
        rvMovieTrailers.setAdapter(mAdapter);
    }

    private void fetchMovieTrailers() {
        FetchTrailersTask.Listener listener = new FetchTrailersTask.Listener() {

            @Override
            public void onFetchFinished(List<Trailer> movies) {
                trailerList.clear();
                trailerList.addAll(movies);
                mAdapter.notifyDataSetChanged();
            }
        };

        FetchTrailersTask fetchTrailersTask = new FetchTrailersTask(listener);
        fetchTrailersTask.execute(mMovie.getId());
    }

    private void fetchMovieReviews() {
        FetchReviewsTask.Listener listener = new FetchReviewsTask.Listener() {

            @Override
            public void onFetchFinished(List<Review> movies) {
//                trailerList.clear();
//                trailerList.addAll(movies);
//                mAdapter.notifyDataSetChanged();
            }
        };

        FetchReviewsTask fetchReviewsTask = new FetchReviewsTask(listener);
        fetchReviewsTask.execute(mMovie.getId());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setTitle(getString(R.string.movie_detail));
        }
    }

    @OnClick(R.id.button_mark_as_favorite)
    public void markAsFavorite() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues favoriteMovieValues = new ContentValues();
                    favoriteMovieValues.put(FavoriteMovieEntry.COLUMN_MOVIE_ID,
                            mMovie.getId());
                    favoriteMovieValues.put(FavoriteMovieEntry.COLUMN_MOVIE_TITLE,
                            mMovie.getTitle());
                    favoriteMovieValues.put(FavoriteMovieEntry.COLUMN_MOVIE_POSTER_URL,
                            mMovie.getPosterURL());
                    favoriteMovieValues.put(FavoriteMovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                            mMovie.getVoteAverage());
                    favoriteMovieValues.put(FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                            mMovie.getReleaseDate());
                    favoriteMovieValues.put(FavoriteMovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS,
                            mMovie.getOverview());

                    getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, favoriteMovieValues);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButton();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick(R.id.button_remove_from_favorite)
    public void removeFromFavorite() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (isFavorite()) {

                    String movieId = Integer.toString(mMovie.getId());
                    Uri uri = FavoriteMovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(movieId).build();

                    getContentResolver().delete(uri, null, null);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                updateFavoriteButton();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateFavoriteButton() {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                return isFavorite();
            }

            @Override
            protected void onPostExecute(Boolean isFavorite) {
                if (isFavorite) {
                    mButtonRemoveFromFavorites.setVisibility(View.VISIBLE);
                    mButtonMarkAsFavorite.setVisibility(View.GONE);
                } else {
                    mButtonMarkAsFavorite.setVisibility(View.VISIBLE);
                    mButtonRemoveFromFavorites.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isFavorite() {
//        String movieId = Integer.toString(mMovie.getId());
//        Uri uri = FavoriteMovieEntry.CONTENT_URI;
//        uri = uri.buildUpon().appendPath(movieId).build();
//
//        Cursor movieCursor = getContentResolver().query(uri,null, null,null,null);

        Cursor movieCursor = getContentResolver().query(
                FavoriteMovieEntry.CONTENT_URI,
                new String[]{FavoriteMovieEntry.COLUMN_MOVIE_ID},
                FavoriteMovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        String youtubeLink = trailerList.get(clickedItemIndex).getYoutubeLink();
        Uri youtubeLinkUri = Uri.parse(youtubeLink);

        Intent intent = new Intent(Intent.ACTION_VIEW, youtubeLinkUri);
        startActivity(intent);
    }
}
