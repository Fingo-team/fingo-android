package com.teamfingo.android.fingo.recommend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Statistics;
import com.teamfingo.android.fingo.utils.AppController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStatistics extends Fragment {

    ImageView ivActor;
    TextView tvActorName;


    public FragmentStatistics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        ivActor = (ImageView) view.findViewById(R.id.imageView_preferred_actor);
        tvActorName = (TextView) view.findViewById(R.id.textView_preferred_actor_name);

        Call<Statistics> statisticsCall = AppController.getFingoService()
                .getStatistics(AppController.getToken());

        statisticsCall.enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                if (response.isSuccessful()) {
                    Statistics statistics = response.body();

                    Statistics.Actors actors[] = statistics.getActors();

                    // TODO 처음 가입한 유저의 경우 쌓인 데이터가 없기 때문에 actors에 아무 정보도 없으므로 예외처리 해줘야함

                    if (actors.length > 1) {
                        Glide.with(getContext()).load(actors[1].getActor().getImg()).into(ivActor);
                        tvActorName.setText(actors[1].getActor().getName());
                    }


                }
            }

            @Override
            public void onFailure(Call<Statistics> call, Throwable t) {

            }
        });

        return view;
    }

}
