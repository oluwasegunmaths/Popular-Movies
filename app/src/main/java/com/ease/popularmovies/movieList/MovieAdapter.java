package com.ease.popularmovies.movieList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ease.popularmovies.R;
import com.ease.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185//";
    private final Context context;
    private final ItemClickListener itemClickListener;
    private List<Movie> movieList;

    public MovieAdapter(Context c, ItemClickListener ClickListener) {
        context = c;
        this.itemClickListener = ClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        loadImageUsingPicassoAccordingToBuildVersion(holder, position);

    }

    private void loadImageUsingPicassoAccordingToBuildVersion(@NonNull ViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable errorDrawable = context.getDrawable(R.drawable.ic_action_error);
            if (errorDrawable != null) {
                Picasso.get().load(BASE_IMAGE_URL + movieList.get(position).getPosterUrl()).error(errorDrawable).into(holder.imageView);
            } else {
                Picasso.get().load(BASE_IMAGE_URL + movieList.get(position).getPosterUrl()).into(holder.imageView);

            }
        } else {
            Picasso.get().load(BASE_IMAGE_URL + movieList.get(position).getPosterUrl()).into(holder.imageView);

        }
    }

    @Override
    public int getItemCount() {
        if (null == movieList) return 0;

        return movieList.size();
    }

    public void setMovies(List<Movie> movies) {
        movieList = movies;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) itemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
