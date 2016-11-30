package com.teamfingo.android.fingo.common;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.model.BoxOfficeRanking;

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

    private static String FINGO_BASE_URL = "http://eb-fingo-real.ap-northeast-2.elasticbeanstalk.com/";

    ListView mListView;
    ListAdapter mListAdapter;

    ArrayList<BoxOfficeRanking.Movie> mMovies = new ArrayList<>();

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

        mListView = (ListView) view.findViewById(R.id.movie_detail_list_view);
        mListAdapter = new ListAdapter();
        mListView.setAdapter(mListAdapter);

        // Fingo Api 호출
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FINGO_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FingoService service = retrofit.create(FingoService.class);
        Call<BoxOfficeRanking> boxOfficeRankingCall = service.getBoxOfficeRanking();

        boxOfficeRankingCall.enqueue(new Callback<BoxOfficeRanking>() {

            @Override
            public void onResponse(Call<BoxOfficeRanking> call, Response<BoxOfficeRanking> response) {

                if (response.isSuccessful()) {
                    Log.d("aaaa", "response ==== "+response);
                    BoxOfficeRanking dataList = response.body();
                    for (BoxOfficeRanking.Movie movie : dataList.getMovie()) {
                        mMovies.add(movie);
                    }
                }

                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BoxOfficeRanking> call, Throwable t) {
                Log.d("aaaa", "onFailure() ==== ");
                t.printStackTrace();
            }

        });


        return view;
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMovies.size();
        }

        @Override
        public Object getItem(int position) {
            return mMovies.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(getActivity(), R.layout.movie_list_item, null);

            imgViewMoviePoster = (ImageView) convertView.findViewById(R.id.imageView_movie_poster);
            Glide.with(getContext()).load(mMovies.get(position).getImg());
            tvMovieTitle = (TextView) convertView.findViewById(R.id.textView_movie_title);
            tvMovieTitle.setText(mMovies.get(position).getTitle());


            return convertView;
        }
    }

}
