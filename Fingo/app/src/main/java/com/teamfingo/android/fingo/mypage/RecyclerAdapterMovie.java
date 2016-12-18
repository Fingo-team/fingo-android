package com.teamfingo.android.fingo.mypage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.common.ActivityMovieDetail;
import com.teamfingo.android.fingo.model.UserMovies;

import java.util.ArrayList;

/**
 * Created by taewon on 2016-12-08.
 */

public class RecyclerAdapterMovie extends RecyclerView.Adapter<RecyclerAdapterMovie.ViewHolder> {

    Context mContext;
    ArrayList<UserMovies.Results> mMovies = new ArrayList<>();

    public RecyclerAdapterMovie(Context context, ArrayList<UserMovies.Results> movies) {

        mContext = context;
        mMovies = movies;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgMoviePoster;
        TextView tvMovieTitle;
        TextView tvMovieRating;

        public ViewHolder(View itemView) {
            super(itemView);

            imgMoviePoster = (ImageView) itemView.findViewById(R.id.image_movie_poster);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.textView_movie_title);
            tvMovieRating = (TextView) itemView.findViewById(R.id.textView_movie_rating);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wish_watched_movie, parent, false);
        RecyclerAdapterMovie.ViewHolder mViewHolder = new RecyclerAdapterMovie.ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final UserMovies.Results movieData = mMovies.get(position);

        Glide.with(mContext).load(movieData.getMovie().getImg()).into(holder.imgMoviePoster);
        holder.imgMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityMovieDetail.class);
                intent.putExtra("movieId",movieData.getMovie().getId());
                mContext.startActivity(intent);
            }
        });

        String title = movieData.getMovie().getTitle();
        if(title.length()>=8){
           String temp = title.substring(0,5);
            holder.tvMovieTitle.setText(temp+"...");
        }
        else
            holder.tvMovieTitle.setText(title);

        float temp = Float.parseFloat(movieData.getMovie().getScore());
        String score = String.format("%.2f",temp);
        holder.tvMovieRating.setText(score);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}
