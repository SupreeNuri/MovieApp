package com.example.android.moviesapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.movieapp.model.Review;
import com.example.android.moviesapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewListAdapter extends RecyclerView.Adapter<MovieReviewListAdapter.ReviewAdapterViewHolder> {

    private final List<Review> mReviews;
    final private ListReviewClickListener mOnClickListener;

    public interface ListReviewClickListener {
        void onReviewItemClick(int clickedItemIndex);
    }

    public MovieReviewListAdapter(List<Review> reviews, ListReviewClickListener listener) {
        mReviews = reviews;
        mOnClickListener = listener;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_movie_review, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        String authorName = mReviews.get(position).getAuthor();
        String content = mReviews.get(position).getContent();

        holder.tvAuthor.setText(authorName);
        holder.tvContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;

        @BindView(R.id.tv_author) TextView tvAuthor;
        @BindView(R.id.tv_review_content) TextView tvContent;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            mView = view;
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onReviewItemClick(clickedPosition);
        }
    }
}
