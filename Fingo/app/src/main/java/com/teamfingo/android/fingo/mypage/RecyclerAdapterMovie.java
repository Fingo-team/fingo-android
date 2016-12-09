package com.teamfingo.android.fingo.mypage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;
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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage_comment_detail, parent, false);
        RecyclerAdapterMovie.ViewHolder mViewHolder = new ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

//        Glide.with(mContext).load(mMovies.getMovies).into(holder.imgMoviePoster);
//        holder.tvMovieTitle.setText(mMovies.getMovieTitle);
//        holder.tvMovieRating.setText(mMovies.getMovieRating);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}
