package com.teamfingo.android.fingo.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Movie;
import com.teamfingo.android.fingo.model.SearchMovie;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySearch extends AppCompatActivity {

    Toolbar searchToolbar;

    RecyclerView mSearchRecyclerView;
    RecyclerAdapterSearch mRecyclerAdapterSearch;
    LinearLayoutManager mLinearLayoutManager;
    EditText mToolbarEditText;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    ArrayList<Movie> mSearchMovies = new ArrayList<>();

    String searchWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        mSearchRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_search);
        mToolbarEditText = (EditText) findViewById(R.id.editText_toolbar);

        mRecyclerAdapterSearch = new RecyclerAdapterSearch(this, mSearchMovies, R.layout.item_search_list);
        mLinearLayoutManager = new LinearLayoutManager(ActivitySearch.this);
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData(currentPage);
            }
        };

        mSearchRecyclerView.setAdapter(mRecyclerAdapterSearch);
        mSearchRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSearchRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        mToolbarEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 버튼을 누를 때마다 새로 검색
                        mSearchMovies.clear();
                        mEndlessRecyclerOnScrollListener.reset();
                        searchWord = mToolbarEditText.getText().toString();

                        loadData(1); // 검색했을 때 처음 page 값은 1

                        break;
                }
                return true;
            }
        });
    }

    public void loadData(int currentPage) {
        Call<SearchMovie> searchMovieCall = AppController.getFingoService()
                .getSearchMovie(AppController.getToken(), searchWord, currentPage);

        searchMovieCall.enqueue(new Callback<SearchMovie>() {
            @Override
            public void onResponse(Call<SearchMovie> call, Response<SearchMovie> response) {
                SearchMovie data = response.body();
                if (data != null) {
                    mSearchMovies.addAll(data.getResults());
                }

                mRecyclerAdapterSearch.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchMovie> call, Throwable t) {

            }
        });
    }

}
