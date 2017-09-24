package com.example.android.moviesapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.model.MovieTrailer;
import com.example.android.moviesapp.adapter.MovieListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_movie_title) TextView movieTitleTextView;
    @BindView(R.id.movie_poster) ImageView moviePosterImageView;
    @BindView(R.id.tv_release_date) TextView releaseDateTextView;
    @BindView(R.id.tv_vote_average) TextView voteAverageTextView;
    @BindView(R.id.tv_plot_synopsis) TextView plotSynopsisTextView;

    @BindView(R.id.rvMovieTrailers) RecyclerView rvMovieTrailers;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupActionBar();

        movie = getIntent().getParcelableExtra("movie");

        setMovieDetail();
        setMovieTrailers()
        fetchMovieTrailers(;
    }

    private void setMovieDetail() {

        String voteAverage = getString(R.string.vote_average,movie.getVoteAverage());

        movieTitleTextView.setText(movie.getTitle());
        Glide.with(this).load(movie.getPosterURL()).into(moviePosterImageView);
        releaseDateTextView.setText(movie.getReleaseDate());
        voteAverageTextView.setText(voteAverage);
        plotSynopsisTextView.setText(movie.getOverview());
    }

    private void setMovieTrailers(){
        ArrayList<MovieTrailer> movieTrailerList = new ArrayList<MovieTrailer>();
        rvMovieTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MovieListAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fetchMovieTrailers(){
        FetchMoviesTask.Listener listener = new FetchMoviesTask.Listener() {
            @Override
            public void onFetchFinished(List<Movie> movies) {
//                movieList.clear();
//                movieList.addAll(movies);
//                mAdapter.notifyDataSetChanged();
            }
        };

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute();
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
