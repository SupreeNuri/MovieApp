package com.example.android.moviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.movieapp.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.android.movieapp.model.Movie;
import com.example.android.moviesapp.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieAdapterViewHolder> {

    private final List<Movie> mMovies;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieListAdapter(List<Movie> movies,ListItemClickListener listener) {
        mMovies = movies;
        mOnClickListener = listener;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_movie, viewGroup, false);
//        Context context = viewGroup.getContext();
//        int columnNumber = context.getResources().getInteger(R.integer.number_of_columns);
//        view.getLayoutParams().height = (int) (viewGroup.getWidth() / columnNumber * Movie.ASPECT_RATIO);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Context context = holder.mView.getContext();
        Glide.with(context).load(mMovies.get(position).getPosterURL()).into(holder.mMovieThumbnail);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView mMovieThumbnail;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            view.setOnClickListener(this);
            mView = view;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void addCursor(Cursor cursor) {
        mMovies.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(FavoriteMovieEntry.INDEX_MOVIE_ID);
                String title = cursor.getString(FavoriteMovieEntry.INDEX_MOVIE_TITLE);
                String posterPath = cursor.getString(FavoriteMovieEntry.INDEX_MOVIE_POSTER_URL);
                String voteAverage = cursor.getString(FavoriteMovieEntry.INDEX_MOVIE_VOTE_AVERAGE);
                String releaseDate = cursor.getString(FavoriteMovieEntry.INDEX_MOVIE_RELEASE_DATE);
                String plotSynopsis = cursor.getString(FavoriteMovieEntry.INDEX_MOVIE_PLOT_SYNOPSIS);

                Movie movie = new Movie();
                movie.setId(id);
                movie.setTitle(title);
                movie.setPosterURL(posterPath);
                movie.setVoteAverage(voteAverage);
                movie.setReleaseDate(releaseDate);
                movie.setOverview(plotSynopsis);
                mMovies.add(movie);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }
}
