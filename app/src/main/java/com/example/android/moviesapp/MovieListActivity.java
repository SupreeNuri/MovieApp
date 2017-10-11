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

import com.example.android.movieapp.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.utilities.NetworkUtils;
import com.example.android.moviesapp.adapter.MovieListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity implements MovieListAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FAVORITE_MOVIES_LOADER = 0;
    private static final String EXTRA_KEY = "movie";

    private MovieListAdapter mAdapter;
    private ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

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
            }
        };

        final FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(listener);
        fetchMoviesTask.execute();
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
                NetworkUtils.sortPath = NetworkUtils.MODE_POPULAR;
                fetchMovies();
                break;
            case R.id.top_rated:
                NetworkUtils.sortPath = NetworkUtils.MODE_TOP_RATED;
                fetchMovies();
                break;
            case R.id.favorites:
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
        //TODO Show loading here.

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
//        updateEmptyState();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
