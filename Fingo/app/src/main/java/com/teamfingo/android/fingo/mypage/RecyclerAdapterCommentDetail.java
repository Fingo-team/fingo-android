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

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.UserComments;

import java.util.ArrayList;

/**
 * Created by taewon on 2016-12-08.
 */

public class RecyclerAdapterCommentDetail extends RecyclerView.Adapter<RecyclerAdapterCommentDetail.ViewHolder> {

    Context mContext;
    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    public RecyclerAdapterCommentDetail(Context context, ArrayList<UserComments.Results> userComments) {

        mContext = context;
        mUserComments = userComments;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgProfile;
        TextView tvUserName, tvDate, tvMovieTitle, tvUserComment;
        RatingBar rbUserRating;
        Button btnLike, btnComment;

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
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage_comment_detail, parent, false);
        RecyclerAdapterCommentDetail.ViewHolder viewHolder = new RecyclerAdapterCommentDetail.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        UserComments.Movie movie_data = mUserComments.get(position).getMovie();
        UserComments.User user_data = mUserComments.get(position).getUser();
        UserComments.Results comment_data = mUserComments.get(position);

//        Glide.with(mContext).load(mUserComments.get(position).getUser().getUser_img()).into(holder.imgProfile);
        holder.tvUserName.setText(user_data.getNickname());
        holder.tvDate.setText(comment_data.getActivity_time());
        holder.tvMovieTitle.setText(movie_data.getTitle());
        holder.tvUserComment.setText(comment_data.getComment());
        holder.rbUserRating.setRating(comment_data.getScore());

    }

    @Override
    public int getItemCount() {
        return mUserComments.size();
    }

}


