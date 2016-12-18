package com.teamfingo.android.fingo.mypage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCommentDetail extends Fragment {

    ImageButton btnOrdering;
    TextView tvOrdering;

    RecyclerView mRecyclerView;
    RecyclerAdapterCommentDetail mAdapter;
    LinearLayoutManager mLayoutManager;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    private static final int INIT_PAGE = 1;

    public FragmentCommentDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_detail, container, false);

        initView(view);

        callFingoUserComments(INIT_PAGE);

        initRecyclerView(view);

        return view;
    }

    private void initView(View view){

        btnOrdering = (ImageButton) view.findViewById(R.id.button_ordering);
        tvOrdering = (TextView) view.findViewById(R.id.textView_ordering);
    }

    private void initRecyclerView(View view){

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_comment_detail);
        mAdapter = new RecyclerAdapterCommentDetail(this.getContext(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                callFingoUserComments(current_page);
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);

    }

    private void callFingoUserComments(int page) {

        Call<UserComments> userCommentsCall = AppController.getFingoService().getUserComments(AppController.getToken(), page);
        userCommentsCall.enqueue(new Callback<UserComments>() {
            @Override
            public void onResponse(Call<UserComments> call, Response<UserComments> response) {
                if (response.isSuccessful()) {

                    UserComments data = response.body();
                    for (UserComments.Results comment : data.getResults()) {
                        Log.e("Check comment", "---------" + comment.getMovie().getTitle());
                        mUserComments.add(comment);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UserComments> call, Throwable t) {

            }
        });

    }
}
