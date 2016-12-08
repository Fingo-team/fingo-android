package com.teamfingo.android.fingo.mypage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.UserComments;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCommentDetail extends Fragment {

    ImageButton btnOrdering;
    TextView tvOrdering;

    RecyclerView mRecyclerView;
    RecyclerAdapterComment mAdapter;

    ArrayList<UserComments> mUserComments;

    public FragmentCommentDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_detail, container, false);

        btnOrdering = (ImageButton) view.findViewById(R.id.button_ordering);
        tvOrdering = (TextView) view.findViewById(R.id.textView_ordering);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_comment_detail);
        mAdapter = new RecyclerAdapterComment(this.getContext(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

}
