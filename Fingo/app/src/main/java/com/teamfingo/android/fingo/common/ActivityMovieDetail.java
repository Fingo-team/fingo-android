package com.teamfingo.android.fingo.common;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.MovieScore;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActivityMovieDetail extends AppCompatActivity implements View.OnClickListener{
    private static String FINGO_BASE_URL = "http://fingo-dev.ap-northeast-2.elasticbeanstalk.com/";

    ImageView ivMovieBackgroundStillCut, ivMoviePoster;
    TextView tvMovieTitle, tvMovieScore;
    Button btnWishMovie, btnRate, btnComment, btnShare;
    TextView tvMovieDate, tvMovieGenre, tvMovieStory;
    ImageView ivStillCut1, ivStillCut2, ivStillCut3, ivStillCut4, ivStillCut5;
    LinearLayout llDirectorandActor;

    LinearLayout.LayoutParams mLayoutParams;

    // 버튼 클릭했을 때 Dialog 생성
    AlertDialog.Builder mBuilderRating, mBuilderComment;
    AlertDialog mAlertDialogRating, mAlertDialogComment;
    LayoutInflater mInflater;
    View mViewDialogRating, mViewDialogComment;
    // Rating Dialog
    TextView tvRatingMovieTitle, tvRatingMovieDate;
    RatingBar rbScore;
    // Comment Dialog
    TextView tvMovieTitleComment;
    RatingBar rbRatedScore;
    EditText etComment;

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
                    btnWishMovie.setOnClickListener(ActivityMovieDetail.this);
                    btnRate.setOnClickListener(ActivityMovieDetail.this);
                    btnComment.setOnClickListener(ActivityMovieDetail.this);
                    btnShare.setOnClickListener(ActivityMovieDetail.this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_wish_movie:
                Toast.makeText(v.getContext(), "보고싶어요", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_rate:
                Toast.makeText(v.getContext(), "평가하기", Toast.LENGTH_SHORT).show();
                DialogRating();
                break;
            case R.id.button_comment:
                Toast.makeText(v.getContext(), "코멘트", Toast.LENGTH_SHORT).show();
                DialogComment();
                break;
            case R.id.button_share:
                Toast.makeText(v.getContext(), "공유하기", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void DialogRating() {

        //-------------------------- Fingo Api 호출 ------------------------------//
        final String movieId = getIntent().getStringExtra("movieId");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FINGO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pref = new FingoPreferences(this);
        token = pref.getAccessToken();

        final FingoService service = retrofit.create(FingoService.class);
        Call<MovieScore> getMovieScoreCall = service.getMovieScore(token, movieId); // GET

        //
        String movieTitle;
        String movieDate;

        mBuilderRating = new AlertDialog.Builder(this);
        mInflater = LayoutInflater.from(this);
        mViewDialogRating = mInflater.inflate(R.layout.dialog_rating, null);

        tvRatingMovieTitle = (TextView) mViewDialogRating.findViewById(R.id.textView_movie_title);
        tvRatingMovieDate = (TextView) mViewDialogRating.findViewById(R.id.textView_movie_date);
        rbScore = (RatingBar) mViewDialogRating.findViewById(R.id.ratingBar);

        movieTitle = tvMovieTitle.getText().toString();
        movieDate = tvMovieDate.getText().toString();

        tvRatingMovieTitle.setText(movieTitle);
        tvRatingMovieDate.setText(movieDate);
        //

        getMovieScoreCall.enqueue(new Callback<MovieScore>() {
            @Override
            public void onResponse(Call<MovieScore> call, Response<MovieScore> response) {
                //Toast.makeText(ActivityMovieDetail.this, "성공", Toast.LENGTH_SHORT).show();

                if (response.isSuccessful()) {
                    MovieScore movieScore = response.body();

                    String score = movieScore.getScore();

                    if (score != "0") {
                        rbScore.setRating(Float.parseFloat(score));
                    } else {
                        rbScore.setRating(0.0f);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieScore> call, Throwable t) {

            }
        });


        mBuilderRating.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ActivityMovieDetail.this, "완료", Toast.LENGTH_SHORT).show();

                String ratedScore;
                ratedScore = String.valueOf(rbScore.getRating());

                Log.d("aaaa", "ratedScore ==== "+ratedScore);

                Call<Void> postMovieScoreCall = service.postMovieScore(token, movieId, ratedScore); // POST

                postMovieScoreCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ActivityMovieDetail.this, "점수", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("aaaa", "response message ==== " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });
        mBuilderRating.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ActivityMovieDetail.this, "취소", Toast.LENGTH_SHORT).show();
            }
        });

        mAlertDialogRating = mBuilderRating.setTitle("별점 남기기")
                .setView(mViewDialogRating)
                .create();

        mBuilderRating.show();



    }

    private void DialogComment() {

        String movieTitle;

        mBuilderComment = new AlertDialog.Builder(this);
        mInflater = LayoutInflater.from(this);
        mViewDialogComment = mInflater.inflate(R.layout.dialog_comment, null);

        tvMovieTitleComment = (TextView) mViewDialogComment.findViewById(R.id.textView_movie_title);
        rbRatedScore = (RatingBar) mViewDialogComment.findViewById(R.id.ratingBar_rated);
        etComment = (EditText) mViewDialogComment.findViewById(R.id.editText_comment);

        movieTitle = tvMovieTitle.getText().toString();
        tvMovieTitleComment.setText(movieTitle);
        rbRatedScore.setRating(3.0f);

        mAlertDialogComment = mBuilderComment.setView(mViewDialogComment).create();
        mAlertDialogComment.show();
    }
}
