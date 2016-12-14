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
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.MovieComment;
import com.teamfingo.android.fingo.model.MovieScore;
import com.teamfingo.android.fingo.model.MovieWish;
import com.teamfingo.android.fingo.utils.AppController;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityMovieDetail extends AppCompatActivity implements View.OnClickListener{

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

    String movieId; // 특정 영화의 id

    Boolean wishMovieState;
    String score;
    String comment;
    String ratedScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Log.e("log", "movie detail token ==== " + AppController.getToken());

        movieId = getIntent().getStringExtra("movieId");

        // Movie Detail 화면 초기화
        initMovieDetailView();

        Call<Movie> movieCall = AppController.getFingoService()
                .getMovie(AppController.getToken(), movieId);

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();

                    Movie.Stillcut[] stillCutImg = movie.getStillcut();
                    Glide.with(ActivityMovieDetail.this).load(stillCutImg[1].getImg()).into(ivMovieBackgroundStillCut);
                    // movie title, movie score 글씨를 더 잘 보이게 하기 위해 배경 이미지를 반투명 처리
                    ivMovieBackgroundStillCut.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);

                    Glide.with(ActivityMovieDetail.this).load(movie.getImg()).into(ivMoviePoster);
                    tvMovieTitle.setText(movie.getTitle());
                    tvMovieScore.setText(movie.getScore());

                    btnWishMovie.setOnClickListener(ActivityMovieDetail.this);
                    btnRate.setOnClickListener(ActivityMovieDetail.this);
                    btnComment.setOnClickListener(ActivityMovieDetail.this);
                    btnShare.setOnClickListener(ActivityMovieDetail.this);

                    tvMovieDate.setText("개봉일: " + movie.getFirst_run_date());
                    Movie.Genre[] genre = movie.getGenre();
                    tvMovieGenre.setText("장르: " + genre[0]);
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
                Log.e("log", "error message ==== " + t.getMessage());
            }
        });

        Call<MovieWish> getMovieWishCall = AppController.getFingoService().getMovieWish(AppController.getToken(), movieId);
        getMovieWishCall.enqueue(new Callback<MovieWish>() {
            @Override
            public void onResponse(Call<MovieWish> call, Response<MovieWish> response) {
                if (response.isSuccessful()) {
                    MovieWish movieWish = response.body();

                    wishMovieState = Boolean.valueOf(movieWish.getWish_movie());
                    Log.e("log", "wishMovieState = Boolean.valueOf(movieWish.getWish_movie());" + wishMovieState);
                    btnWishMovie.setActivated(wishMovieState);
                }
            }

            @Override
            public void onFailure(Call<MovieWish> call, Throwable t) {
                Log.e("log", "error message ==== " + t.getMessage());
            }
        });

        Call<MovieScore> getMovieScoreCall = AppController.getFingoService()
                .getMovieScore(AppController.getToken(), movieId); // GET

        getMovieScoreCall.enqueue(new Callback<MovieScore>() {
            @Override
            public void onResponse(Call<MovieScore> call, Response<MovieScore> response) {
                if (response.isSuccessful()) {
                    MovieScore movieScore = response.body();
                    Log.e("log", "response message ==== " + response.body());

                    score = movieScore.getScore();

                    if (!(score.equals("0.0"))) {
                        btnRate.setText(score);
                        btnRate.setActivated(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieScore> call, Throwable t) {
                Log.e("log", "error message ==== " + t.getMessage());
            }
        });

        Call<MovieComment> getMovieCommentCall = AppController.getFingoService().getMovieComment(AppController.getToken(), movieId); // GET

        getMovieCommentCall.enqueue(new Callback<MovieComment>() {
            @Override
            public void onResponse(Call<MovieComment> call, Response<MovieComment> response) {

                if (response.isSuccessful()) {
                    MovieComment movieComment = response.body();

                    comment = movieComment.getComment();
                    ratedScore = movieComment.getScore();

                    if (comment != null) {
                        btnComment.setActivated(true);
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieComment> call, Throwable t) {
                Log.e("log", "error message ==== " + t.getMessage());
            }
        });
    }

    public void initMovieDetailView() {

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_wish_movie:
                wishMovieState = !wishMovieState;
                Log.e("log", "wish button clicked, wishMovieState ==== " + wishMovieState);
                btnWishMovie.setActivated(wishMovieState);
                setWishButtonState();
                break;
            case R.id.button_rate:
                openDialogRating();
                break;
            case R.id.button_comment:
                if (score.equals("0.0")) {
                    Toast.makeText(v.getContext(), "평가 먼저 남겨주세요^.^", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    openDialogComment();
                    break;
                }
            case R.id.button_share:
                break;
        }
    }

    private void setWishButtonState() {
        String wishMovieStateToString; // 서버로 보내주기위해 wishMovieState를 String 값으로 바꿈

        if (wishMovieState) {
            wishMovieStateToString = "True";
        } else {
            wishMovieStateToString = "False";
        }

        // 평가가 되어있는 영화일 경우, 보고싶어요 버튼을 누르게되면 평가되어있던 점수를 0점으로 처리
        if (!(score.equals("0.0"))) {
            Log.e("log", "rated score ==== " + ratedScore);
            Toast.makeText(this, "평가가 취소됩니다", Toast.LENGTH_SHORT).show();
            btnRate.setText("평가하기");
            btnRate.setActivated(false);
            score = "0.0";
            ratedScore = "0.0";
        }

        Call<Void> postMovieWishCall = AppController.getFingoService()
                .postMovieWish(AppController.getToken(), movieId, wishMovieStateToString);

        postMovieWishCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("log", "response message ==== " + response.message());
                    Log.d("log", "response message ==== " + response.raw());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("log", "error message ==== " + t.getMessage());
            }
        });

    }

    private void openDialogRating() {

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


        if (!(score.equals("0.0"))) {
            // 영화에 평가 점수가 있을 경우 rating bar에 점수를 표시해줌
            rbScore.setRating(Float.parseFloat(score));
        } else {
            rbScore.setRating(0.0f);
        }

        mBuilderRating.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(ActivityMovieDetail.this, "완료", Toast.LENGTH_SHORT).show();

            final String ratedScore;
            ratedScore = String.valueOf(rbScore.getRating());

            Call<Void> postMovieScoreCall = AppController.getFingoService().postMovieScore(AppController.getToken(), movieId, ratedScore); // POST

            postMovieScoreCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        score = ratedScore; // 사용자가 영화에 대한 평가 점수를 남기면, 평가 점수가 실시간으로 화면에 반영될 수 있게 처리

                        if (ratedScore.equals("0.0")) {
                            btnRate.setText("평가하기");
                            btnRate.setActivated(false);
                        } else {
                            Log.d("log", "2/ score == "+score);
                            btnRate.setText(ratedScore);
                            btnRate.setActivated(true);

                            // 보고싶어요 버튼이 클릭되어있는 영화가 평가됐을 경우, 보고싶어요 버튼 비활성화되게 처리
                            if (btnWishMovie.isActivated()) {
                                wishMovieState = !wishMovieState;
                                btnWishMovie.setActivated(wishMovieState);
                            }
                        }

                    } else {
                        Log.e("log", "response message ==== " + response.message());
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("log", "error message ==== " + t.getMessage());
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

        mAlertDialogRating.show();

    }

    private void openDialogComment() {

        String movieTitle;

        mBuilderComment = new AlertDialog.Builder(this);
        mInflater = LayoutInflater.from(this);
        mViewDialogComment = mInflater.inflate(R.layout.dialog_comment, null);

        tvMovieTitleComment = (TextView) mViewDialogComment.findViewById(R.id.textView_movie_title);
        rbRatedScore = (RatingBar) mViewDialogComment.findViewById(R.id.ratingBar_rated);
        etComment = (EditText) mViewDialogComment.findViewById(R.id.editText_comment);

        movieTitle = tvMovieTitle.getText().toString();
        tvMovieTitleComment.setText(movieTitle);

        etComment.setText(comment);


        rbRatedScore.setRating(Float.parseFloat(score));


        mBuilderComment.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ActivityMovieDetail.this, "완료", Toast.LENGTH_SHORT).show();

                String writtenComment = etComment.getText().toString();
                comment = writtenComment;

                Call<Void> postMovieComment = AppController.getFingoService().postMovieComment(AppController.getToken(), movieId, writtenComment); // POST

                postMovieComment.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e("log", "response message ==== " + response.message());

                            // 댓글이 써졌을 경우 코멘트 아이콘 색상 변경
                            btnComment.setActivated(true);
                        } else {
                            Log.e("log", "response message ==== " + response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("log", "error message ==== " + t.getMessage());
                    }
                });

            }
        });
        mBuilderComment.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ActivityMovieDetail.this, "취소", Toast.LENGTH_SHORT).show();
            }
        });

        mAlertDialogComment = mBuilderComment.setTitle("코멘트 남기기")
                .setView(mViewDialogComment)
                .create();

        mAlertDialogComment.show();
    }
}
