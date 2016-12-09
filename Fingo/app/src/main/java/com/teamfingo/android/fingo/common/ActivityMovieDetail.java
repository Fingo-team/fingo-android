package com.teamfingo.android.fingo.common;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityMovieDetail extends AppCompatActivity {
    private static String FINGO_BASE_URL = "http://fingo-dev.ap-northeast-2.elasticbeanstalk.com/";

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
    ImageView ivStillCut1, ivStillCut2, ivStillCut3, ivStillCut4, ivStillCut5;
    LinearLayout llDirectorandActor;

    LinearLayout.LayoutParams mLayoutParams;

    FingoPreferences pref;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ivMovieBackgroundStillCut = (ImageView) findViewById(R.id.imageView_movie_detail_backgroundStillcut);
        ivMoviePoster = (ImageView) findViewById(R.id.imageView_movie_detail_movie_post);
        tvMovieTitle = (TextView) findViewById(R.id.textView_movie_detail_movie_title);
        tvMovieScore = (TextView) findViewById(R.id.textView_movie_detail_movie_score);
        btnWishMovie = (Button) findViewById(R.id.button_wish_movie);
        btnRate = (Button) findViewById(R.id.button_rate);
        btnComment= (Button) findViewById(R.id.button_comment);
        btnShare = (Button) findViewById(R.id.button_share);
        tvMovieDate = (TextView) findViewById(R.id.textView_date);
        tvMovieGenre = (TextView) findViewById(R.id.textView_genre);
        tvMovieStory = (TextView) findViewById(R.id.textView_story);
        ivStillCut1 = (ImageView) findViewById(R.id.imageView_stillCut1);
        ivStillCut2 = (ImageView) findViewById(R.id.imageView_stillCut2);
        ivStillCut3 = (ImageView) findViewById(R.id.imageView_stillCut3);
        ivStillCut4 = (ImageView) findViewById(R.id.imageView_stillCut4);
        ivStillCut5 = (ImageView) findViewById(R.id.imageView_stillCut5);
        llDirectorandActor = (LinearLayout) findViewById(R.id.linearLayout_director_and_actor);

        String movieId = getIntent().getStringExtra("movieId");

        // Fingo Api 호출
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FINGO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pref = new FingoPreferences(this);
        token = pref.getAccessToken();

        FingoService service = retrofit.create(FingoService.class);
        Call<Movie> movieCall = service.getMovie(token, movieId);

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();

                    Movie.Stillcut[] stillCutImg = movie.getStillcut();
                    Glide.with(ActivityMovieDetail.this).load(stillCutImg[1].getImg()).into(ivMovieBackgroundStillCut);
                    // movie title, movie score가 더 잘 보이게 하기 위해서 배경 이미지를 반투명 처리
                    ivMovieBackgroundStillCut.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);

                    Glide.with(ActivityMovieDetail.this).load(movie.getImg()).into(ivMoviePoster);
                    tvMovieTitle.setText(movie.getTitle());
                    tvMovieScore.setText(movie.getScore());
                    btnWishMovie.setText("1");
                    btnRate.setText("2");
                    btnComment.setText("3");
                    btnShare.setText("4");

                    tvMovieDate.setText("개봉일: " + movie.getFirst_run_date());
                    tvMovieGenre.setText("장르: " + movie.getGenre());
                    tvMovieStory.setText(movie.getStory());

                    Glide.with(ActivityMovieDetail.this).load(stillCutImg[0].getImg()).into(ivStillCut1);
                    Glide.with(ActivityMovieDetail.this).load(stillCutImg[1].getImg()).into(ivStillCut2);
                    Glide.with(ActivityMovieDetail.this).load(stillCutImg[2].getImg()).into(ivStillCut3);
                    Glide.with(ActivityMovieDetail.this).load(stillCutImg[3].getImg()).into(ivStillCut4);
                    Glide.with(ActivityMovieDetail.this).load(stillCutImg[4].getImg()).into(ivStillCut5);

                    mLayoutParams = new LinearLayout.LayoutParams(200, 200);
                    Movie.Director[] directors = movie.getDirector();
                    Movie.Actor[] actors = movie.getActor();

                    for (int i=0; i<directors.length; i++) {
                        CircleImageView civ = new CircleImageView(ActivityMovieDetail.this);
                        Glide.with(ActivityMovieDetail.this).load(directors[i].getImg()).into(civ);
                        civ.setLayoutParams(mLayoutParams);
                        mLayoutParams.setMargins(40,0,40,0);
                        llDirectorandActor.addView(civ);
                    }

                    for (int i=0; i<actors.length; i++) {
                        CircleImageView civ = new CircleImageView(ActivityMovieDetail.this);
                        Glide.with(ActivityMovieDetail.this).load(actors[i].getImg()).into(civ);
                        civ.setLayoutParams(mLayoutParams);
                        mLayoutParams.setMargins(40,0,40,0);
                        llDirectorandActor.addView(civ);
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d("aaaa", "onFailure ==== ");

            }
        });
    }
}
