package com.teamfingo.android.fingo.mypage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
public class FragmentCommentDetail extends Fragment{

    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView mRecyclerView;
    RecyclerAdapterCommentDetail mAdapter;
    LinearLayoutManager mLayoutManager;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    Spinner mSpinner;

    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    private static final int INIT_PAGE = 1;

//    private static final int SORT_INIT = 0;
    private static final int SORT_TIME = 0;
    private static final int SORT_TITLE = 1;
    private static final int SORT_SCORE = 2;

    public FragmentCommentDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment_detail, container, false);

        initView(view);
        callFingoUserComments(INIT_PAGE, "activity_time");

        initRecyclerView(view);

        return view;
    }

    private void initView(View view){

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_commentDetail);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserComments.clear();
                callFingoUserComments(INIT_PAGE, "activity_time");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        String[] str = getResources().getStringArray(R.array.movie_sorting);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, str);

        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mUserComments.clear();
                sortingMovie(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUserComments.clear();
            }
        });

    }

    private void initRecyclerView(View view){

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_comment_detail);
        mAdapter = new RecyclerAdapterCommentDetail(this.getContext(), this.getActivity(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                callFingoUserComments(current_page, "activity_time");
            }
        };
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);

    }

    private void callFingoUserComments(int page, String order) {

        Call<UserComments> userCommentsCall = AppController.getFingoService().getUserComments(AppController.getToken(), page, order);
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
                t.printStackTrace();
            }
        });
    }

    private void sortingMovie(int position){
        switch(position){

            case SORT_TIME:
                callFingoUserComments(INIT_PAGE, "activity_time");
                break;

            case SORT_TITLE:
                callFingoUserComments(INIT_PAGE, "title");
                break;

            case SORT_SCORE:
                callFingoUserComments(INIT_PAGE, "score");
                break;
        }
    }

    private void actions(){


    }
}
