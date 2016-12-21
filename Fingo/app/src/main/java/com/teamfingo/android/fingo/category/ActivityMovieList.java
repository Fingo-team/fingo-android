package com.teamfingo.android.fingo.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.teamfingo.android.fingo.model.BoxOfficeRanking;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.MovieWrapper;
import com.teamfingo.android.fingo.utils.AppController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityMovieList extends AppCompatActivity {
    SwipeRefreshLayout mSwipeRefreshLayout;

    ListView mListView;
    ListAdapter mListAdapter;

    ArrayList<Movie> mMovies = new ArrayList<>();

    ImageView imgViewMoviePoster;
    TextView tvMovieTitle;
    TextView tvAverageScore;
    TextView tvMovieGenre;
    TextView tvReleaseDate;
    TextView tvToolbar;
    Toolbar movieListToolbar;

    String type, genre, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        type = getIntent().getStringExtra("type");
        genre = getIntent().getStringExtra("genre");
        title = getIntent().getStringExtra("title");

        movieListToolbar = (Toolbar) findViewById(R.id.movie_list_toolbar);
        tvToolbar = (TextView) findViewById(R.id.textView_toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_movie_list);
        mListView = (ListView) findViewById(R.id.movie_list_listview);

        setSupportActionBar(movieListToolbar); // back arrow를 달아주기 위해 설정
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvToolbar.setText(title);

        mListAdapter = new ListAdapter();
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieId = mMovies.get(position).getId();

                Intent intent = new Intent(ActivityMovieList.this, ActivityMovieDetail.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        loadData();
    }

    private void loadData() {
        mMovies.clear();

        if(type.equals("boxoffice")) {
            Call<BoxOfficeRanking> boxOfficeRankingCall = AppController.getFingoService().getBoxOfficeRanking(AppController.getToken());
            boxOfficeRankingCall.enqueue(new Callback<BoxOfficeRanking>() {
                @Override
                public void onResponse(Call<BoxOfficeRanking> call, Response<BoxOfficeRanking> response) {
                    if (response.isSuccessful()) {
                        BoxOfficeRanking boxOfficeRanking = response.body();
                        for (int i=0; i<boxOfficeRanking.getData().size(); i++) {
                            mMovies.add(boxOfficeRanking.getData().get(i).getMovie());
                        }
                    }
                    mListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<BoxOfficeRanking> call, Throwable t) { }
            });
        } else {
            Call<MovieWrapper> call = AppController.getFingoService().getMovieList(AppController.getToken(), type, genre);
            call.enqueue(new Callback<MovieWrapper>() {
                @Override
                public void onResponse(Call<MovieWrapper> call, Response<MovieWrapper> response) {
                    if (response.isSuccessful()) {
                        MovieWrapper movieWrapper = response.body();
                        mMovies.addAll(movieWrapper.getData());
                    }
                    mListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieWrapper> call, Throwable t) { }
            });
        }
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

            convertView = View.inflate(ActivityMovieList.this, R.layout.item_movie_list, null);

            imgViewMoviePoster = (ImageView) convertView.findViewById(R.id.imageView_movie_poster);
            tvMovieTitle = (TextView) convertView.findViewById(R.id.textView_movie_title);
            tvAverageScore = (TextView) convertView.findViewById(R.id.textView_average_score);
            tvMovieGenre = (TextView) convertView.findViewById(R.id.textView_genre);
            tvReleaseDate = (TextView) convertView.findViewById(R.id.textView_release_date);

            Glide.with(ActivityMovieList.this).load(mMovies.get(position).getImg()).into(imgViewMoviePoster);
            tvMovieTitle.setText(mMovies.get(position).getTitle());
            tvAverageScore.setText(getString(R.string.fingo_user_average_score) +" "+ mMovies.get(position).getScore());
            Movie.Genre genre[] = mMovies.get(position).getGenre();
            tvMovieGenre.setText(genre[0].toString());
            tvReleaseDate.setText(" ・ " + mMovies.get(position).getFirst_run_date());

            return convertView;
        }
    }
}
