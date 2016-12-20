package com.teamfingo.android.fingo.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
 * Created by taewon on 2016-12-09.
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

        final UserComments.Movie movie_data = mUserComments.get(position).getMovie();
        UserComments.User user_data = mUserComments.get(position).getUser();
        UserComments.Results comment_data = mUserComments.get(position);

        if (mUserComments.get(position).getUser().getUser_img_url() != null)
            Glide.with(mActivity).load(mUserComments.get(position).getUser().getUser_img_url()).into(holder.imgUserProfile);
        else
            Glide.with(mActivity).load(R.drawable.com_facebook_profile_picture_blank_portrait).into(holder.imgUserProfile);

        holder.tvUserName.setText(user_data.getNickname());
        holder.rbUserScore.setRating(comment_data.getScore());
        holder.tvCommentDate.setText(comment_data.getActivity_time());
        holder.tvMovieTitle.setText(movie_data.getTitle());
        holder.tvComment.setText(comment_data.getComment());

        Glide.with(mContext).load(movie_data.getStillcut()[0].getImg()).into(holder.imgMovieStilcut);
        holder.imgMovieStilcut.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        holder.imgMovieStilcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityMovieDetail.class);
                intent.putExtra("movieId", movie_data.getId());
                mContext.startActivity(intent);
            }
        });

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


    public void openModifyMenu(View view, int position) {

        final UserComments.Movie movie_info = mUserComments.get(position).getMovie();
        final UserComments.Results comment_info = mUserComments.get(position);

        new BottomSheet.Builder(mActivity)
                .title("Comment Options")
                .sheet(R.menu.item_comment_option)
                .listener(new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case R.id.menu_correct:
                                Intent intent = new Intent(mActivity, ActivityCorrectComment.class);
                                intent.putExtra("movie_id", movie_info.getId());
                                intent.putExtra("movie_title", movie_info.getTitle());
                                intent.putExtra("comment_score", comment_info.getScore());
                                Log.e("ckeck score before", comment_info.getScore() + "");
                                intent.putExtra("comment", comment_info.getComment());
                                mActivity.startActivity(intent);
                                break;

                            case R.id.menu_delete:
                                deleteComment(movie_info.getId());
                                break;

                            case R.id.menu_share:
                                try {
                                    shareToKakao(movie_info.getTitle(), movie_info.getImg());
                                } catch (KakaoParameterException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }).show();
    }

    private void deleteComment(String movie_id) {
        Call<Void> deleteCommentCall = AppController.getFingoService().deleteUserComment(AppController.getToken(), movie_id);
        deleteCommentCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("delete comment check", response.message());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void shareToKakao(String movieTitle, String moviePoster) throws KakaoParameterException {
        final KakaoLink kakaoLink = KakaoLink.getKakaoLink(mContext.getApplicationContext());
        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

        // 링크 메세지 작성
        String text = movieTitle;
        kakaoTalkLinkMessageBuilder.addText(text);

        String imageSrc = moviePoster;
        int width = 300;
        int height = 500;
        kakaoTalkLinkMessageBuilder.addImage(imageSrc, width, height);

        kakaoTalkLinkMessageBuilder.addAppButton("감동을 전하는 놀이터 - Fingo");
        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, mContext);

    }

}
