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
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-12-07
 *
 * == Recycler Adapter Comment Detail ==
 *
 * # Fragment wish / watched movie 에 필요한 Recycler Adapter 입니다.
 *
 */

public class RecyclerAdapterMovie extends RecyclerView.Adapter<RecyclerAdapterMovie.ViewHolder> {

    Context mContext;
    ArrayList<UserMovies.Results> mMovies = new ArrayList<>();

    public RecyclerAdapterMovie(Context context, ArrayList<UserMovies.Results> movies) {

        mContext = context;
        mMovies = movies;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        // 1. 서버로 부터 영화 정보를 가져 옵니다.
        final UserMovies.Results movieData = mMovies.get(position);

        // 2. 영화 포스터를 가져 옵니다.
        Glide.with(mContext).load(movieData.getMovie().getImg()).into(holder.imgMoviePoster);
        // 2.1 영화 포스터를 클릭 했을 때, 해당 영화의 상세 정보 페이지로 넘어가도록 합니다.
        holder.imgMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 2.1.1 ActivityMovieDetail 호출
                Intent intent = new Intent(mContext, ActivityMovieDetail.class);
                intent.putExtra("movieId", movieData.getMovie().getId());
                mContext.startActivity(intent);
            }
        });

        // 3. 가져온 텍스트 데이터들에 대한 후처리를 진행합니다.
        // 3.1 영화 제목이 8자 이상일땐 제목을 자릅니다.
        String title = movieData.getMovie().getTitle();
        if (title.length() >= 8) {
            // 다섯번째 글자까지 표시 한 후,
            String temp = title.substring(0, 5);
            // 텍스트를 '...' 으로 마무리 한다.
            holder.tvMovieTitle.setText(temp + "...");
        } else
            holder.tvMovieTitle.setText(title);

        // 3.2 평점을 소수점 첫째 자리까지 표시합니다.
        float temp = Float.parseFloat(movieData.getMovie().getScore());
        String score = String.format("%.1f", temp);
        holder.tvMovieRating.setText(score);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

}
