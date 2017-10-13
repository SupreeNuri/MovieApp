package com.example.android.moviesapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.movieapp.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.utilities.NetworkUtils;
import com.example.android.moviesapp.adapter.MovieListAdapter;
import com.example.android.moviesapp.constants.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.moviesapp.constants.Constants.MODE_FAVORITES;
import static com.example.android.moviesapp.constants.Constants.MODE_POPULAR;
import static com.example.android.moviesapp.constants.Constants.MODE_TOP_RATED;

public class MovieListActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.tv_no_favorite) TextView tvNoFavorite;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    private static final int FAVORITE_MOVIES_LOADER = 1111;
    private static final String EXTRA_KEY = "movie";

    private MovieListAdapter mAdapter;
    private ArrayList<Movie> movieList;
    private String mSortBy = MODE_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        int numberOfColumn = getResources().getInteger(R.integer.number_of_columns);

        movieList = new ArrayList<>();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumn, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MovieListAdapter(movieList, this);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_KEY)) {
                List<Movie> movies = savedInstanceState.getParcelableArrayList(EXTRA_KEY);
                movieList.addAll(movies);
                mAdapter.notifyDataSetChanged();
            }

            updateEmptyState();
        } else {
            fetchMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieList != null && !movieList.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_KEY, movieList);
        }
    }

    private void fetchMovies() {
        FetchMoviesTask.Listener listener = new FetchMoviesTask.Listener() {
            @Override
            public void onFetchFinished(List<Movie> movies) {
                movieList.clear();
                movieList.addAll(movies);
                mAdapter.notifyDataSetChanged();

                updateEmptyState();
            }
        };

        final FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute();
    }

    private void updateEmptyState() {
        progressBar.setVisibility(View.GONE);

        if (mAdapter.getItemCount() == 0) {
            if (mSortBy.equals(MODE_FAVORITES)) {
                tvNoFavorite.setVisibility(View.VISIBLE);
            }
        } else {
            tvNoFavorite.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        switch (menuId) {
            case R.id.most_popular:
                if (mSortBy.equals(Constants.MODE_FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
                mSortBy = MODE_POPULAR;
                NetworkUtils.sortPath = MODE_POPULAR;
                fetchMovies();
                break;
            case R.id.top_rated:
                if (mSortBy.equals(Constants.MODE_FAVORITES)) {
                    getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER);
                }
                mSortBy = MODE_TOP_RATED;
                NetworkUtils.sortPath = MODE_TOP_RATED;
                fetchMovies();
                break;
            case R.id.favorites:
                mSortBy = MODE_FAVORITES;
                getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", movieList.get(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == FAVORITE_MOVIES_LOADER) {
            return new CursorLoader(this,
                    FavoriteMovieEntry.CONTENT_URI,
                    FavoriteMovieEntry.MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.addCursor(cursor);

        updateEmptyState();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
