package com.teamfingo.android.fingo.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.utils.AppController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.teamfingo.android.fingo.utils.AppController.getFingoService;

public class ActivityCorrectComment extends AppCompatActivity implements View.OnClickListener {

    Button btnComplete, btnCancel;
    RatingBar rbScore ,rbScore_detail;
    TextView tvTitle;
    EditText etComment;

    // comment 수정 view 에 필요한 data
    String mMovie_id;
    String mComment;
    String mTitle;
    float mScore;
    String mCorrectComment;

    // Rating 다이얼 components
    AlertDialog.Builder mBuilderRating;
    AlertDialog mAlertDialogRating;
    LayoutInflater mInflater;
    View mViewDialogRating;

    TextView tvRatingMovieTitle, tvRatingMovieDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_comment);

        btnComplete = (Button)findViewById(R.id.button_complete);
        btnComplete.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(this);

        rbScore = (RatingBar) findViewById(R.id.ratingBar_score);
        tvTitle = (TextView) findViewById(R.id.textView_title);
        etComment = (EditText) findViewById(R.id.editText_comment);

        getCommentInfo();
        initView();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_complete:
                mCorrectComment = etComment.getText().toString();
                correctComment(mCorrectComment);
                finish();
                break;

            case R.id.button_cancel:
                finish();
                break;

            case R.id.ratingBar_score:
                openDialogRating();
                break;
        }

    }

    // 수정 전 comment 및 score 초기화
    private void initView(){
        tvTitle.setText(mTitle);
        rbScore.setRating(mScore);
        Log.e("check rating", ""+mScore);
        etComment.setText(mComment);
        mCorrectComment = "";
    }

    // 수정을 위한 작성 코멘트 정보 받아오기
    private void getCommentInfo(){
        mMovie_id = getIntent().getStringExtra("movie_id");
        mTitle = getIntent().getStringExtra("movie_title");
        mScore = getIntent().getFloatExtra("comment_score", 0);
        mComment = getIntent().getStringExtra("comment");
    }

    // 코멘트 수정 요청
    private void correctComment(String comment){
        Call<Void> patchCommentCall = AppController.getFingoService().correctUserComment(AppController.getToken(), mMovie_id, comment);
        patchCommentCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    Toast.makeText(ActivityCorrectComment.this, "수정이 완료 되었습니다", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ActivityCorrectComment.this, "수정에 실패 하였습니다", Toast.LENGTH_SHORT).show();
                Log.e("check patch", response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // 별점 수정을 위한 Alert Dialog
    private void openDialogRating() {

        mBuilderRating = new AlertDialog.Builder(this);
        mInflater = LayoutInflater.from(this);
        mViewDialogRating = mInflater.inflate(R.layout.dialog_rating, null);

        tvRatingMovieTitle = (TextView) mViewDialogRating.findViewById(R.id.textView_movie_title);
        tvRatingMovieDate = (TextView) mViewDialogRating.findViewById(R.id.textView_movie_date);
        rbScore_detail = (RatingBar) mViewDialogRating.findViewById(R.id.ratingBar);

        tvRatingMovieTitle.setText(mTitle);

        mBuilderRating.setPositiveButton("완료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ActivityCorrectComment.this, "완료", Toast.LENGTH_SHORT).show();

                final String ratedScore;
                ratedScore = String.valueOf(rbScore_detail.getRating());

                Call<Void> postMovieScoreCall = getFingoService().postMovieScore(AppController.getToken(), mMovie_id, ratedScore); // POST
                postMovieScoreCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

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
                Toast.makeText(ActivityCorrectComment.this, "취소", Toast.LENGTH_SHORT).show();
            }
        });

        mAlertDialogRating = mBuilderRating.setTitle("별점 남기기")
                .setView(mViewDialogRating)
                .create();

        mAlertDialogRating.show();

    }
}
