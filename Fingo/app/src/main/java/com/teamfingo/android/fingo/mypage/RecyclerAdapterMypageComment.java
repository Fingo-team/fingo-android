package com.teamfingo.android.fingo.mypage;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.UserComments;

import java.util.ArrayList;

/**
 * Created by taewon on 2016-12-09.
 */

public class RecyclerAdapterMypageComment extends RecyclerView.Adapter<RecyclerAdapterMypageComment.ViewHolder>{

    Context mContext;
    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    public RecyclerAdapterMypageComment(Context mContext, ArrayList<UserComments.Results> mUserComments) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {

        UserComments.Movie movie_data = mUserComments.get(position).getMovie();
        UserComments.User user_data = mUserComments.get(position).getUser();
        UserComments.Results comment_data = mUserComments.get(position);

        holder.tvUserName.setText(user_data.getNickname());
        holder.rbUserScore.setRating(comment_data.getScore());
        holder.tvCommentDate.setText(comment_data.getActivity_time());
        holder.tvMovieTitle.setText(movie_data.getTitle());
        holder.tvComment.setText(comment_data.getComment());

        Glide.with(mContext).load(movie_data.getStillcut()[0].getImg()).into(holder.imgMovieStilcut);
        holder.imgMovieStilcut.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public int getItemCount() {
        return mUserComments.size();
    }

}
