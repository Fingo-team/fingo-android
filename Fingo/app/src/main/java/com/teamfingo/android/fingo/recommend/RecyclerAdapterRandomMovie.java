package com.teamfingo.android.fingo.recommend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.common.ActivityMovieDetail;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.utils.AppController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jaemin on 2016. 12. 16..
 */

public class RecyclerAdapterRandomMovie extends RecyclerView.Adapter<RecyclerAdapterRandomMovie.ViewHolder> {

    Context mContext;
    ArrayList<Movie> mRandomMovies = new ArrayList<>();
    private int itemLayout;
    String score;

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

        public ViewHolder(final View itemView) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Glide.with(mContext).load(mRandomMovies.get(position).getImg()).into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText(mRandomMovies.get(position).getTitle());
        holder.tvMovieDate.setText(mRandomMovies.get(position).getFirst_run_date());
        holder.rbScore.setRating(Float.parseFloat(mRandomMovies.get(position).getScore()));
        holder.rbScore.setTag(position);
        Log.e("log", "score1 === "+ mRandomMovies.get(position).getScore());
        Log.e("log","score set");

        String movieId = mRandomMovies.get(position).getId();
        // 영화에 별점을 남기면 통신하여 평점 값을 보내도록 처리
        CustomOnRatingChanged rC = new CustomOnRatingChanged(position, movieId);
        holder.rbScore.setOnRatingBarChangeListener(rC);
        // 영화 포스터를 누르면 영화에 대한 디테일 화면이 보여지도록 처리
        CustomOnImageViewListener imageViewListener = new CustomOnImageViewListener(position, movieId);
        holder.ivMoviePoster.setOnClickListener(imageViewListener);
    }

    private class CustomOnRatingChanged implements  RatingBar.OnRatingBarChangeListener {
        int position;
        String movieId;

        public CustomOnRatingChanged(int position, String movieId) {
            this.position = position;
            this.movieId = movieId;
        }

        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if(fromUser) {
                if (ratingBar.getTag().equals(position)) {
                    final String score = Float.toString(rating);
                    ratingBar.setRating(rating);

                    Log.e("tag", "score = " + mRandomMovies.get(position).getScore());
                    Call<Void> postMovieScoreCall = AppController.getFingoService()
                            .postMovieScore(AppController.getToken(), movieId, score);
                    postMovieScoreCall.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                Toast.makeText(mContext, "점수가 입력되었습니다.", Toast.LENGTH_SHORT).show();
                                mRandomMovies.get(position).setScore(score);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });
                }
            }
        }
    }

    private class CustomOnImageViewListener implements ImageView.OnClickListener {
        int position;
        String movieId;

        public CustomOnImageViewListener(int position, String movieId) {
            this.position = position;
            this.movieId = movieId;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ActivityMovieDetail.class);
            intent.putExtra("movieId", movieId);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return mRandomMovies.size();
    }

}
