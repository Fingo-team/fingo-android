package com.teamfingo.android.fingo.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Movie;

import java.util.ArrayList;

/**
 * Created by Jaemin on 2016. 12. 16..
 */

public class RecyclerAdapterRandomMovie extends RecyclerView.Adapter<RecyclerAdapterRandomMovie.ViewHolder> {

    Context mContext;
    ArrayList<Movie> mRandomMovies = new ArrayList<>();
    private int itemLayout;


    public RecyclerAdapterRandomMovie(Context context, ArrayList<Movie> randomMovies, int itemLayout) {
        this.mContext = context;
        mRandomMovies = randomMovies;
        this.itemLayout = itemLayout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvMovieDate;
        RatingBar rbScore;

        public ViewHolder(View itemView) {
            super(itemView);

            ivMoviePoster = (ImageView) itemView.findViewById(R.id.imageView_movie_poster);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.textView_movie_title);
            tvMovieDate = (TextView) itemView.findViewById(R.id.textView_movie_date);
            rbScore = (RatingBar) itemView.findViewById(R.id.ratingBar_score);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(mContext).load(mRandomMovies.get(position).getImg()).into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText(mRandomMovies.get(position).getTitle());
        holder.tvMovieDate.setText(mRandomMovies.get(position).getFirst_run_date());
        holder.rbScore.setRating(Float.parseFloat(mRandomMovies.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return mRandomMovies.size();
    }

}
