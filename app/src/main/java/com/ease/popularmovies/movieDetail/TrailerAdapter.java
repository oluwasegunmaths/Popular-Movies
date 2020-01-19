package com.ease.popularmovies.movieDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ease.popularmovies.R;
import com.ease.popularmovies.data.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> trailerList;
    private Context context;
    private ItemClickListener itemClickListener;

    public TrailerAdapter(Context c, ItemClickListener ClickListener) {
        context = c;
        this.itemClickListener = ClickListener;
    }


    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailers_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.trailerName.setText(trailerList.get(i).getName());

    }

    @Override
    public int getItemCount() {

        if (null == trailerList) return 0;

        return trailerList.size();

    }


    public void setTrailerList(List<Trailer> trailers) {
        trailerList = trailers;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(String url);

        void onShareImageClick(String url);

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerName;
        ImageView shareTrailerImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name);
            shareTrailerImage = (ImageView) itemView.findViewById(R.id.share_trailer_image);
            shareTrailerImage.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.share_trailer_image) {
                itemClickListener.onShareImageClick(trailerList.get(getAdapterPosition()).getKey());


            } else {


                itemClickListener.onItemClick(trailerList.get(getAdapterPosition()).getKey());
            }

        }
    }
}
