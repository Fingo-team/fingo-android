package com.teamfingo.android.fingo.recommend;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
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

    public static FragmentStatistics newInstance() {
        FragmentStatistics f = new FragmentStatistics();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        GraphView graphView = (GraphView) view.findViewById(R.id.graphView);
        // set manual X bounds
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0.5);
        graphView.getViewport().setMaxX(5);

        // set manual Y bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(15);

        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"", "1", "", "2", "", "3", "", "4", "", "5"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0.5, 1),
                new DataPoint(1, 2),
                new DataPoint(1.5, 3),
                new DataPoint(2, 2),
                new DataPoint(2.5, 3),
                new DataPoint(3, 7),
                new DataPoint(3.5, 10),
                new DataPoint(4, 9),
                new DataPoint(4.5, 7),
                new DataPoint(5, 6)
        });
        series.setDataPointsRadius(10);
        series.setDrawDataPoints(true);
        series.setThickness(8);
        series.setColor(Color.MAGENTA);

        graphView.addSeries(series);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        graphView.getGridLabelRenderer().setGridColor(Color.GRAY);
        graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.GRAY);
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.GRAY);


        Call<Statistics> statisticsCall = AppController.getFingoService()
                .getStatistics(AppController.getToken());

        statisticsCall.enqueue(new Callback<Statistics>() {
            @Override
            public void onResponse(Call<Statistics> call, Response<Statistics> response) {

                if (response.isSuccessful()) {
                    Statistics statistics = response.body();

                    // TODO 처음 가입한 유저의 경우 쌓인 데이터가 없기 때문에 actors에 아무 정보도 없으므로 예외처리 해줘야함
                    int movieCount = Integer.parseInt(statistics.getScores().getUser_statistics().getMovie_count());

                    if (movieCount > 5) { // User가 평가한 영화가 5개 이상일 때부터 통계 데이터를 보여준다

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
