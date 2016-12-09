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
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.model.UserComments;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCommentDetail extends Fragment {

    ImageButton btnOrdering;
    TextView tvOrdering;

    RecyclerView mRecyclerView;
    RecyclerAdapterCommentDetail mAdapter;

    ArrayList<UserComments.Results> mUserComments = new ArrayList<>();

    private static final String BASE_URL = "http://fingo-dev.ap-northeast-2.elasticbeanstalk.com/";

    private FingoPreferences mPref;

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

        mPref = new FingoPreferences(getContext());
        callFingoService();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_comment_detail);
        mAdapter = new RecyclerAdapterCommentDetail(this.getContext(), mUserComments);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return view;
    }

    private void callFingoService() {

        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FingoService service = client.create(FingoService.class);
        Call<UserComments> userCommentsCall = service.getUserComments(mPref.getAccessToken());
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
