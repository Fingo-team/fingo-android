package com.teamfingo.android.fingo.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Person;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaemin on 2016. 12. 16..
 */

public class RecyclerAdapterDirectorAndActor extends RecyclerView.Adapter<RecyclerAdapterDirectorAndActor.ViewHolder> {

    Context mContext;
    ArrayList<Person> mPersons = new ArrayList<>();
    private int itemLayout;

    public RecyclerAdapterDirectorAndActor(Context context, ArrayList<Person> persons, int itemLayout) {
        this.mContext = context;
        mPersons = persons;
        this.itemLayout = itemLayout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView ivFace;
        TextView tvName;
        TextView tvRole;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivFace = (CircleImageView) itemView.findViewById(R.id.imageView_face);
            tvName = (TextView) itemView.findViewById(R.id.textView_name);
            tvRole = (TextView) itemView.findViewById(R.id.textView_role);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Glide.with(mContext).load(mPersons.get(position).getImg()).error(R.drawable.placeholder_gray).into(holder.ivFace);
        holder.tvName.setText(mPersons.get(position).getName());
        holder.tvRole.setText(mPersons.get(position).getRole());
    }

    @Override
    public int getItemCount() {
        return mPersons.size();
    }

}
