package com.teamfingo.android.fingo.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

                        // 아무것도 입력하지 않고 검색했을 때 검색어를 입력해 달라는 토스트 창을 띄워줌
                        if (searchWord.equals("")) {
                            Toast.makeText(ActivitySearch.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        // 검색 버튼을 누른 뒤 키보드가 내려가게 처리
                        View view = ActivitySearch.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

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
                    if (data.getCount() == 0) {
                        Toast.makeText(ActivitySearch.this, "검색 결과가 없습니다.\n\n다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {

                        mSearchMovies.addAll(data.getResults());
                    }
                }

                mRecyclerAdapterSearch.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchMovie> call, Throwable t) {

            }
        });
    }

}
