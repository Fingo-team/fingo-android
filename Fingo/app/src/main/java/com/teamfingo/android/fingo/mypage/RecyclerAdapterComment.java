package com.teamfingo.android.fingo.mypage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.UserComments;

import java.util.ArrayList;

/**
 * Created by taewon on 2016-12-08.
 */

public class RecyclerAdapterComment extends RecyclerView.Adapter<RecyclerAdapterComment.ViewHolder> {

    Context mContext;
    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    public RecyclerAdapterComment(Context context, ArrayList<UserComments.Results> userComments) {

        mContext = context;
        mUserComments = userComments;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgProfile, imgStilcut;
        TextView tvUserName, tvDate, tvMovieTitle, tvUserComment;
        RatingBar rbUserRating;
        Button btnLike, btnComment, btnShare;

        public ViewHolder(View itemView) {
            super(itemView);

            imgProfile = (ImageView) itemView.findViewById(R.id.image_comment_detail_profile);
            tvUserName = (TextView) itemView.findViewById(R.id.textView_user_nickname);
            tvDate = (TextView) itemView.findViewById(R.id.textView_comment_date);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.textView_movie_title);
            tvUserComment = (TextView) itemView.findViewById(R.id.textView_comment);
            btnLike = (Button) itemView.findViewById(R.id.button_like);
            btnComment = (Button) itemView.findViewById(R.id.button_comment);
            rbUserRating = (RatingBar) itemView.findViewById(R.id.ratingBar_comment_detail);
            btnShare = (Button) itemView.findViewById(R.id.button_share);
            imgStilcut = (ImageView) itemView.findViewById(R.id.image_mypage_stilcut);

        }
    }

    @Override
    public RecyclerAdapterComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage_comment, parent, false);
        RecyclerAdapterComment.ViewHolder viewHolder = new RecyclerAdapterComment.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterComment.ViewHolder holder, int position) {

        UserComments.Movie movie_data = mUserComments.get(position).getMovie();
        UserComments.User user_data = mUserComments.get(position).getUser();
        UserComments.Results comment_data = mUserComments.get(position);

        holder.tvUserName.setText(user_data.getNickname());
        holder.rbUserRating.setRating(comment_data.getScore());
        holder.tvDate.setText(comment_data.getComment());
        Glide.with(mContext).load(movie_data.getStillcut()).into(holder.imgStilcut);
        holder.tvMovieTitle.setText(movie_data.getTitle());
    }

    @Override
    public int getItemCount() {
        return mUserComments.size();
    }

}