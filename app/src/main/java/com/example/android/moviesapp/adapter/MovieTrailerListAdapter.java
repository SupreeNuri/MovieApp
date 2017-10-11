package com.example.android.moviesapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.model.Trailer;
import com.example.android.moviesapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailerListAdapter extends RecyclerView.Adapter<MovieTrailerListAdapter.MovieAdapterViewHolder> {

    private final List<Trailer> mTrailers;
    final private ListTrailerClickListener mOnClickListener;

    public interface ListTrailerClickListener {
        void onTrailerItemClick(int clickedItemIndex);
    }

    public MovieTrailerListAdapter(List<Trailer> trailers, ListTrailerClickListener listener) {
        mTrailers = trailers;
        mOnClickListener = listener;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_movie_trailer, viewGroup, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String trailerName = mTrailers.get(position).getTrailerName();
        holder.tvTrailer.setText(trailerName);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;

        @BindView(R.id.tvTrailer) TextView tvTrailer;

        public MovieAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            mView = view;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onTrailerItemClick(clickedPosition);
        }
    }
}
