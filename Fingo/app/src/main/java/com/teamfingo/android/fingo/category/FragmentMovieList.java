package com.teamfingo.android.fingo.category;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.common.ActivityMovieDetail;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.model.BoxOfficeRanking;
import com.teamfingo.android.fingo.utils.AppController;
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
public class FragmentMovieList extends Fragment {

    ListView mListView;
    ListAdapter mListAdapter;

    ArrayList<BoxOfficeRanking.Data> mRanks = new ArrayList<>();

    ImageView imgViewMoviePoster;
    TextView tvMovieTitle;
    TextView tvAverageScore;
    TextView tvTotalAttendance;
    TextView tvReleaseDate;


    public FragmentMovieList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mListView = (ListView) view.findViewById(R.id.movie_list_listview);
        mListAdapter = new ListAdapter();
        mListView.setAdapter(mListAdapter);



        if (mRanks.size() == 0) {

            Call<BoxOfficeRanking> boxOfficeRankingCall = AppController.getFingoService().getBoxOfficeRanking(AppController.getToken());

            boxOfficeRankingCall.enqueue(new Callback<BoxOfficeRanking>() {
                @Override
                public void onResponse(Call<BoxOfficeRanking> call, Response<BoxOfficeRanking> response) {

                    if (response.isSuccessful()) {
                        Log.d("aaaa", "response ==== " + response);
                        BoxOfficeRanking boxOfficeRanking = response.body();
                        Log.d("aaa", "dataList ==== " + boxOfficeRanking.toString());
                        mRanks.addAll(boxOfficeRanking.getData());
                    }
                    mListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<BoxOfficeRanking> call, Throwable t) {
                    Log.d("aaaa", "onFailure() ==== ");
                    t.printStackTrace();
                }

            });
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieId = mRanks.get(position).getMovie().getId();

                Intent intent = new Intent(getActivity(), ActivityMovieDetail.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });

        return view;
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mRanks.size();
        }

        @Override
        public Object getItem(int position) {
            return mRanks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(getActivity(), R.layout.item_movie_list, null);

            imgViewMoviePoster = (ImageView) convertView.findViewById(R.id.imageView_movie_poster);
            tvMovieTitle = (TextView) convertView.findViewById(R.id.textView_movie_title);
            tvAverageScore = (TextView) convertView.findViewById(R.id.textView_average_score);
            tvTotalAttendance = (TextView) convertView.findViewById(R.id.textView_total_attendance);
            tvReleaseDate = (TextView) convertView.findViewById(R.id.textView_release_date);

            Glide.with(getContext()).load(mRanks.get(position).getMovie().getImg()).into(imgViewMoviePoster);
            tvMovieTitle.setText(mRanks.get(position).getMovie().getTitle());
            tvAverageScore.setText(mRanks.get(position).getMovie().getScore());
            tvTotalAttendance.setText(mRanks.get(position).getMovie().getGenre());
            tvReleaseDate.setText(mRanks.get(position).getMovie().getFirst_run_date());

            return convertView;
        }
    }

}
