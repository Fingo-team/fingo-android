package com.teamfingo.android.fingo.common;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.model.Movie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMovieDetail extends Fragment {

    private static String FINGO_BASE_URL = "http://eb-fingo-real.ap-northeast-2.elasticbeanstalk.com/";
    private static String AUTHORIZATION = "token 41059ad0ec56dbc9bfd1e7dc633cef2a6de69d48";

    //Movie mMovie;

    ImageView ivMovieBackgroundStillCut;
    ImageView ivMoviePoster;
    TextView tvMovieTitle;
    TextView tvMovieScore;
    Button btnWishMovie;
    Button btnRate;
    Button btnComment;
    Button btnShare;
    TextView tvMovieDate;
    TextView tvMovieGenre;
    TextView tvMovieStory;
    ImageView ivStillCut1;
    ImageView ivStillCut2;
    ImageView ivStillCut3;
    ImageView ivStillCut4;
    ImageView ivStillCut5;

    public static FragmentMovieDetail newInstance(String movieId) {
        FragmentMovieDetail fragment = new FragmentMovieDetail();

        Bundle args = new Bundle();
        args.putString("movieId", movieId);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // Fingo Api 호출
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FINGO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FingoService service = retrofit.create(FingoService.class);
        Call<Movie> movieCall = service.getMovie(AUTHORIZATION, (String) getArguments().getSerializable("movieId"));

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();

                    ivMovieBackgroundStillCut = (ImageView) view.findViewById(R.id.imageView_movie_detail_backgroundStillcut);
                    Movie.Stillcut[] stillCutImg = movie.getStillcut();
                    Glide.with(getContext()).load(stillCutImg[1].getImg()).into(ivMovieBackgroundStillCut);

                    ivMoviePoster = (ImageView) view.findViewById(R.id.imageView_movie_detail_movie_post);
                    Glide.with(getContext()).load(movie.getImg()).into(ivMoviePoster);
                    tvMovieTitle = (TextView) view.findViewById(R.id.textView_movie_detail_movie_title);
                    tvMovieTitle.setText(movie.getTitle());
                    tvMovieScore = (TextView) view.findViewById(R.id.textView_movie_detail_movie_score);
                    tvMovieScore.setText(movie.getScore());

                    btnWishMovie = (Button) view.findViewById(R.id.button_wish_movie);
                    btnWishMovie.setText("1");
                    btnRate = (Button) view.findViewById(R.id.button_rate);
                    btnRate.setText("2");
                    btnComment= (Button) view.findViewById(R.id.button_comment);
                    btnComment.setText("3");
                    btnShare = (Button) view.findViewById(R.id.button_share);
                    btnShare.setText("4");

                    tvMovieDate = (TextView) view.findViewById(R.id.textView_date);
                    tvMovieDate.setText(movie.getFirst_run_date());
                    tvMovieGenre = (TextView) view.findViewById(R.id.textView_genre);
                    tvMovieGenre.setText(movie.getGenre());
                    tvMovieStory = (TextView) view.findViewById(R.id.textView_story);
                    tvMovieStory.setText(movie.getStory());

                    ivStillCut1 = (ImageView) view.findViewById(R.id.imageView_stillCut1);
                    Glide.with(getContext()).load(stillCutImg[0].getImg()).into(ivStillCut1);
                    ivStillCut2 = (ImageView) view.findViewById(R.id.imageView_stillCut2);
                    Glide.with(getContext()).load(stillCutImg[1].getImg()).into(ivStillCut2);
                    ivStillCut3 = (ImageView) view.findViewById(R.id.imageView_stillCut3);
                    Glide.with(getContext()).load(stillCutImg[2].getImg()).into(ivStillCut3);
                    ivStillCut4 = (ImageView) view.findViewById(R.id.imageView_stillCut4);
                    Glide.with(getContext()).load(stillCutImg[3].getImg()).into(ivStillCut4);
                    ivStillCut5 = (ImageView) view.findViewById(R.id.imageView_stillCut5);
                    Glide.with(getContext()).load(stillCutImg[4].getImg()).into(ivStillCut5);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("aaaa", "onFailure ==== ");

            }
        });

        return view;
    }

}
