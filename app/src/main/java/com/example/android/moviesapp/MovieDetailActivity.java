package com.example.android.moviesapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.movieapp.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {
    private TextView movieTitleTextView;
    private ImageView moviePosterImageView;
    private TextView releaseDateTextView;
    private TextView voteAverageTextView;
    private TextView plotSynopsisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupActionBar();

        movieTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        moviePosterImageView = (ImageView) findViewById(R.id.movie_poster);
        releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        voteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
        plotSynopsisTextView = (TextView) findViewById(R.id.tv_plot_synopsis);

        setMovieDetail();
    }

    private void setMovieDetail() {
        Movie movie = getIntent().getParcelableExtra("movie");

        String voteAverage = getString(R.string.vote_average,movie.getVoteAverage());

        movieTitleTextView.setText(movie.getTitle());
        Glide.with(this).load(movie.getPosterURL()).into(moviePosterImageView);
        releaseDateTextView.setText(movie.getReleaseDate());
        voteAverageTextView.setText(voteAverage);
        plotSynopsisTextView.setText(movie.getOverview());
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setTitle(getString(R.string.movie_detail));
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
}
