package com.teamfingo.android.fingo.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cocosw.bottomsheet.BottomSheet;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.common.ActivityCorrectComment;
import com.teamfingo.android.fingo.common.ActivityMovieDetail;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.utils.AppController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-12-07
 *
 * == Recycler Adapter Comment Detail ==
 *
 * # Fragment Comment Detail 에 필요한 Recycler Adapter 입니다.
 *
 * # Comment Detail Page 내의 각각의 코멘트들에서 동작할수 있는 여러 기능들이 들어있습니다.
 *
 *  - 코멘트 수정
 *  - 코멘트 삭제
 *  - 카카오톡으로 공유 하기
 *
 */

public class RecyclerAdapterMypageComment extends RecyclerView.Adapter<RecyclerAdapterMypageComment.ViewHolder> {

    Activity mActivity;
    Context mContext;
    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    public RecyclerAdapterMypageComment(Context mContext, Activity mActivity, ArrayList<UserComments.Results> mUserComments) {

        this.mActivity = mActivity;
        this.mContext = mContext;
        this.mUserComments = mUserComments;
    }

    // 서버로 부터 전달 받은 Model 을 적용시킬 View 선언
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUserProfile, imgMovieStilcut;
        TextView tvUserName, tvMovieTitle, tvComment, tvCommentDate;
        RatingBar rbUserScore;
        Button btnLike, btnAddComment, btnShare;
        ImageButton btnModify;

        public ViewHolder(View itemView) {
            super(itemView);

            imgUserProfile = (ImageView) itemView.findViewById(R.id.image_comment_profile);
            tvUserName = (TextView) itemView.findViewById(R.id.textView_user_nickname);
            rbUserScore = (RatingBar) itemView.findViewById(R.id.ratingBar_comment);
            tvCommentDate = (TextView) itemView.findViewById(R.id.textView_comment_date);
            btnModify = (ImageButton) itemView.findViewById(R.id.button_modify);
            imgMovieStilcut = (ImageView) itemView.findViewById(R.id.image_mypage_stilcut);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.textView_movie_title);
            tvComment = (TextView) itemView.findViewById(R.id.textView_comment);
            btnLike = (Button) itemView.findViewById(R.id.button_like);
            btnAddComment = (Button) itemView.findViewById(R.id.button_comment);
            btnShare = (Button) itemView.findViewById(R.id.button_share);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage_comment, parent, false);
        RecyclerAdapterMypageComment.ViewHolder viewHolder = new RecyclerAdapterMypageComment.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // 1. 평가 정보 가져오기
        // 1.1 평가 한 영화 정보 load
        final UserComments.Movie movie_data = mUserComments.get(position).getMovie();   // 내 코멘트의 영화 정보
        UserComments.User user_data = mUserComments.get(position).getUser();    // 내 코멘트의 개인 정보
        UserComments.Results comment_data = mUserComments.get(position);    // 내 코멘트 내용

        // 2. 가져 온 정보를 View 에 대입
        // 2.1 유저 프로필 이미지를 가져 옵니다.
        if (mUserComments.get(position).getUser().getUser_img_url() != null)
            // 2.1.1 서버에 유저 프로필 이미지가 존재하는 경우
            Glide.with(mActivity).load(mUserComments.get(position).getUser().getUser_img_url()).into(holder.imgUserProfile);
        else
            // 2.1.2 서버에 유저 프로필 이미지가 존재하지 않는 경우 - 기본 내장 이미지로 대체
            Glide.with(mActivity).load(R.drawable.com_facebook_profile_picture_blank_portrait).into(holder.imgUserProfile);

        // 2.2 코멘트를 작성한 영화의 스틸컷 이미지를 가져 오고 Shader 를 덮어씌웁니다.
        Glide.with(mContext).load(movie_data.getStillcut()[0].getImg()).into(holder.imgMovieStilcut);
        holder.imgMovieStilcut.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);

        // 2.2.1 띄워진 영화 이미지를 클릭 했을 때, 해당 영화에 대한 상세 정보로 이동합니다.
        holder.imgMovieStilcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityMovieDetail.class);
                intent.putExtra("movieId", movie_data.getId());
                mContext.startActivity(intent);
            }
        });

        // 2.3 텍스트 형태의 유저의 기본 정보와 영화 평가내용을 가져 옵니다.
        holder.tvUserName.setText(user_data.getNickname());     // 유저 닉네임
        holder.rbUserScore.setRating(comment_data.getScore());  // 유저가 평가한 평점
        holder.tvCommentDate.setText(comment_data.getActivity_time());  // 코멘트 작성 일자
        holder.tvMovieTitle.setText(movie_data.getTitle());     // 코멘트를 작성한 영화의 제목
        holder.tvComment.setText(comment_data.getComment());    // 작성 코멘트

        // 3. 작성한 코멘트에 대한 옵션 메뉴
        // Bottom Sheet open source library 를 사용하였으며, 이 곳에서 코멘트의 수정, 삭제, 공유 기능을 수행합니다.
        holder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openModifyMenu(v, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUserComments.size();
    }

    // 코멘트의 수정, 삭제, 공유 기능 구현
    public void openModifyMenu(View view, int position) {

        // 1. 해당 코멘트에 대한 기본정보를 가져 옵니다.
        final UserComments.Movie movie_info = mUserComments.get(position).getMovie();   // 평가 한 영화 정보
        final UserComments.Results comment_info = mUserComments.get(position);  // 유저의 평가 정보

        // 2. Bottom sheet 의 동작 구현
        new BottomSheet.Builder(mActivity)  // 현재 Fragment 가 띄워진 Activity 위로 Bottom sheet 가 생성되도록 설정
                .title("Comment Options")   // Bottom sheet 의 제목
                .sheet(R.menu.item_comment_option)  // Bottom sheet layout 설정
                .listener(new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            // 2.1 '수정하기' 구현
                            case R.id.menu_correct:

                                // 2.1.1 ActivityCorrectComment 호출
                                Intent intent = new Intent(mActivity, ActivityCorrectComment.class);

                                // 2.1.2 수정 페이지에서 필요한 정보들을 intent 로 넘깁니다.
                                intent.putExtra("movie_id", movie_info.getId());        // 영화 고유 번호
                                intent.putExtra("movie_title", movie_info.getTitle());  // 영화 제목
                                intent.putExtra("comment_score", comment_info.getScore());  // 유저 평점
                                intent.putExtra("comment", comment_info.getComment());  // 유저 코멘트

                                mActivity.startActivity(intent);
                                break;

                            // 2.2 '삭제하기' 구현
                            case R.id.menu_delete:

                                // 2.2.1 서버로 해당 영화에 대한 코멘트 삭제 요청 - 영화 고유 번호를 삭제 요청 파라미터로 넘김
                                deleteComment(movie_info.getId());  // 영화 고유 번호
                                break;

                            // 2.3 '공유하기' 구현
                            case R.id.menu_share:
                                try {
                                    // 2.3.1 카카오톡 메세지에 영화제목과 포스터 이미지를 공유
                                    shareToKakao(movie_info.getTitle(), movie_info.getImg());
                                } catch (KakaoParameterException e) {
                                    // 2.3.2 카카오톡을 연결 할 수 없을 때의 예외처리
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }).show();
    }

    // 코멘트 삭제 기능 구현
    private void deleteComment(String movie_id) {

        // 1. 코멘트 삭제 API 호출
        Call<Void> deleteCommentCall = AppController.getFingoService().deleteUserComment(AppController.getToken(), movie_id);

        // 2. 코멘트 삭제 response
        deleteCommentCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // 2.1 정상적으로 코멘트가 삭제 되었다면 이를 사용자에게 알림
                if(response.isSuccessful())
                    Toast.makeText(mContext, "삭제 되었습니다!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // 2.2 통신 오류가 발생 했을 때, 예외처리
                t.printStackTrace();
            }
        });
    }

    // 카카오톡으로 공유하기 구현
    private void shareToKakao(String movieTitle, String moviePoster) throws KakaoParameterException {

        // 1. kakao SDK 의 kakao Link 를 가져옵니다.
        final KakaoLink kakaoLink = KakaoLink.getKakaoLink(mContext.getApplicationContext());   // kakaoLink API 호출
        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();  // kakaoLink 의 Message builder 호출

        // 2. 링크 메세지 작성
        // 2.1 메세지 텍스트 작성 - 영화 제목
        String text = movieTitle;
        kakaoTalkLinkMessageBuilder.addText(text);

        // 2.2 메세지 이미지 로드 - 영화 포스터
        String imageSrc = moviePoster;
        int width = 300;
        int height = 500;
        kakaoTalkLinkMessageBuilder.addImage(imageSrc, width, height);

        // 2.3 App 으로 Re-direction 할 수 있는 버튼 추가 삽입
        kakaoTalkLinkMessageBuilder.addAppButton("감동을 전하는 놀이터 - Fingo");

        // 3. 카카오톡으로 메세지 전송
        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, mContext);
    }

}
