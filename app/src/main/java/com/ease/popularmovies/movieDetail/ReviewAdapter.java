package com.ease.popularmovies.movieDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ease.popularmovies.R;
import com.ease.popularmovies.data.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {


    private final Context context;

    private List<Review> reviewList;

    public ReviewAdapter(Context c) {
        context = c;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.author.setText(reviewList.get(position).getAuthor());
        holder.comment.setText(reviewList.get(position).getContent());


    }


    @Override
    public int getItemCount() {
        if (null == reviewList) return 0;

        return reviewList.size();
    }

    public void setReviews(List<Review> reviews) {
        reviewList = reviews;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView comment;

        ViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.review_author);
            comment = itemView.findViewById(R.id.review_comment);

        }

    }
}
