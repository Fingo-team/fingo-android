package com.teamfingo.android.fingo.recommend;


import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Statistics;
import com.teamfingo.android.fingo.utils.AppController;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStatistics extends Fragment {
    RelativeLayout rlNoData, rlStatistics;
    // 별점 분포
    GraphView graphView;
    //double[] scores = new double[10];
    //double zeroPointFive, one, onePointFive, two, twoPointFive, three, threePointFive, four, fourPointFive, five = 0;
    // 선호 배우 TOP3
    ImageView ivPreferActor1, ivPreferActor2, ivPreferActor3;
    TextView tvActorName1, tvActorScore1, tvActorName2, tvActorScore2, tvActorName3, tvActorScore3;
    // 선호 감독 TOP3
    ImageView ivPreferDirector1, ivPreferDirector2, ivPreferDirector3;
    TextView tvDirectorName1, tvDirectorScore1, tvDirectorName2, tvDirectorScore2, tvDirectorName3, tvDirectorScore3;

    public static FragmentStatistics newInstance() {
        FragmentStatistics f = new FragmentStatistics();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);



        initView(view);

        loadData();

        //graphView(view);


        return view;
    }


    private void initView(View view) {
        rlNoData = (RelativeLayout) view.findViewById(R.id.relativeLayout_no_data);
        rlStatistics = (RelativeLayout) view.findViewById(R.id.relativeLayout_statistics);

        ivPreferActor1 = (ImageView) view.findViewById(R.id.imageView_actor1);
        tvActorName1 = (TextView) view.findViewById(R.id.textView_actor1_name);
        tvActorScore1 = (TextView) view.findViewById(R.id.textView_actor1_score);

        ivPreferActor2 = (ImageView) view.findViewById(R.id.imageView_actor2);
        tvActorName2 = (TextView) view.findViewById(R.id.textView_actor2_name);
        tvActorScore2 = (TextView) view.findViewById(R.id.textView_actor2_score);

        ivPreferActor3 = (ImageView) view.findViewById(R.id.imageView_actor3);
        tvActorName3 = (TextView) view.findViewById(R.id.textView_actor3_name);
        tvActorScore3 = (TextView) view.findViewById(R.id.textView_actor3_score);

        ivPreferDirector1 = (ImageView) view.findViewById(R.id.imageView_director1);
        tvDirectorName1 = (TextView) view.findViewById(R.id.textView_director1_name);
        tvDirectorScore1 = (TextView) view.findViewById(R.id.textView_director1_score);

        ivPreferDirector2 = (ImageView) view.findViewById(R.id.imageView_director2);
        tvDirectorName2 = (TextView) view.findViewById(R.id.textView_director2_name);
        tvDirectorScore2 = (TextView) view.findViewById(R.id.textView_director2_score);

        ivPreferDirector3 = (ImageView) view.findViewById(R.id.imageView_director3);
        tvDirectorName3 = (TextView) view.findViewById(R.id.textView_director3_name);
        tvDirectorScore3 = (TextView) view.findViewById(R.id.textView_director3_score);

        graphView = (GraphView) view.findViewById(R.id.graphView);
    }


    double[] scores = new double[10];

    private void loadData() {
        //double zeroPointFive, one, onePointFive, two, twoPointFive, three, threePointFive, four, fourPointFive, five = 0;
        //final double[] scores = new double[10];

        Call<Statistics> statisticsCall = AppController.getFingoService()
                .getStatistics(AppController.getToken());

        statisticsCall.enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {
                if (response.isSuccessful()) {
                    Statistics statistics = response.body();

                    // 처음 가입한 유저의 경우 쌓인 데이터가 없기 때문에 actors에 아무 정보도 없으므로 예외처리 해줘야함
                    int movieCount = Integer.parseInt(statistics.getScores().getUser_statistics().getMovie_count());

                    if (movieCount >= 5) { // User가 평가한 영화가 5개 이상일 때부터 통계 데이터를 보여준다
                        rlNoData.setVisibility(View.GONE);
                        rlStatistics.setVisibility(View.VISIBLE);

                        scores[0] = Double.parseDouble(statistics.getScores().getPoint_five());
                        scores[1] = Double.parseDouble(statistics.getScores().getOne());
                        scores[2] = Double.parseDouble(statistics.getScores().getOne_point_five());
                        scores[3] = Double.parseDouble(statistics.getScores().getTwo());
                        scores[4] = Double.parseDouble(statistics.getScores().getTwo_point_five());
                        scores[5] = Double.parseDouble(statistics.getScores().getThree());
                        scores[6] = Double.parseDouble(statistics.getScores().getThree_point_five());
                        scores[7] = Double.parseDouble(statistics.getScores().getFour());
                        scores[8] = Double.parseDouble(statistics.getScores().getFour_point_five());
                        scores[9] = Double.parseDouble(statistics.getScores().getFive());

                        Log.e("log", "loaddata() scores[0] ==== "+ scores[0]);


                        Statistics.Actors[] actors = statistics.getActors();
                        Glide.with(getContext()).load(actors[0].getActor().getImg()).into(ivPreferActor1);
                        tvActorName1.setText(actors[0].getActor().getName());
                        tvActorScore1.setText(actors[0].getCount());
                        Glide.with(getContext()).load(actors[1].getActor().getImg()).into(ivPreferActor2);
                        tvActorName2.setText(actors[1].getActor().getName());
                        tvActorScore2.setText(actors[1].getCount());
                        Glide.with(getContext()).load(actors[2].getActor().getImg()).into(ivPreferActor3);
                        tvActorName3.setText(actors[2].getActor().getName());
                        tvActorScore3.setText(actors[2].getCount());

                        Statistics.Directors[] directors = statistics.getDirectors();
                        Glide.with(getContext()).load(directors[0].getDirector().getImg()).into(ivPreferDirector1);
                        tvDirectorName1.setText(directors[0].getDirector().getName());
                        tvDirectorScore1.setText(directors[0].getCount());
                        Glide.with(getContext()).load(directors[1].getDirector().getImg()).into(ivPreferDirector2);
                        tvDirectorName2.setText(directors[1].getDirector().getName());
                        tvDirectorScore2.setText(directors[1].getCount());
                        Glide.with(getContext()).load(directors[2].getDirector().getImg()).into(ivPreferDirector3);
                        tvDirectorName3.setText(directors[2].getDirector().getName());
                        tvDirectorScore3.setText(directors[2].getCount());

                    } else { // User가 평가한 영화가 5개 미만일 때에는 5개 이상 평가해 달라는 화면을 보여준다
                        rlNoData.setVisibility(View.VISIBLE);
                        rlStatistics.setVisibility(View.GONE);
                    }
                }
                graphView();
            }

            @Override
            public void onFailure(Call<Statistics> call, Throwable t) {

            }
        });

    }

    private void graphView() {
        // set manual X bounds
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0.5);
        graphView.getViewport().setMaxX(5);

        // set manual Y bounds
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for(int i = 0; i < scores.length; i++) {
            min = (min > scores[i]) ? scores[i] : min;
            max = (max < scores[i]) ? scores[i] : max;
        }

        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(min);
        graphView.getViewport().setMaxY(max);

        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"", "1", "", "2", "", "3", "", "4", "", "5"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0.5, scores[0]),
                new DataPoint(1, scores[1]),
                new DataPoint(1.5, scores[2]),
                new DataPoint(2, scores[3]),
                new DataPoint(2.5, scores[4]),
                new DataPoint(3, scores[5]),
                new DataPoint(3.5, scores[6]),
                new DataPoint(4, scores[7]),
                new DataPoint(4.5, scores[8]),
                new DataPoint(5, scores[9])
        });
        Log.e("log", "graphView() score[0] ==== " + scores[0]);
        series.setDataPointsRadius(10);
        series.setDrawDataPoints(true);
        series.setThickness(8);
        series.setColor(Color.MAGENTA);

        graphView.addSeries(series);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graphView.getGridLabelRenderer().setGridColor(Color.GRAY);
        graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.GRAY);
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.GRAY);
    }

}
