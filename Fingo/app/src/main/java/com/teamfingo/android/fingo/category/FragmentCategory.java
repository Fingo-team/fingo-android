package com.teamfingo.android.fingo.category;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.model.Category;
import com.teamfingo.android.fingo.utils.AppController;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategory extends Fragment {
    ImageView mImageViewBoxOffice;
    ImageView mImageViewMonth;
    ImageView mImageViewGenre;
    TextView mTextViewBoxOffice;
    TextView mTextViewMonth;
    TextView mTextViewGenre;

    public FragmentCategory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mImageViewBoxOffice = (ImageView) view.findViewById(R.id.imageView_boxOffice);
        mImageViewMonth = (ImageView) view.findViewById(R.id.imageView_month);
        mImageViewGenre = (ImageView) view.findViewById(R.id.imageView_genre);
        mTextViewBoxOffice = (TextView) view.findViewById(R.id.textView_boxOffice);
        mTextViewMonth = (TextView) view.findViewById(R.id.textView_month);
        mTextViewGenre = (TextView) view.findViewById(R.id.textView_genre);

        Call<Category> getMovieCategory = AppController.getFingoService().getCategoryMain(AppController.getToken()); // 라라랜드

        Log.d("aaaaa","aaaaaaa");
        getMovieCategory.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                Log.d("aaaaa","aaaaaaa 111");
                if (response.isSuccessful()) {
                    Log.d("aaaaa","aaaaaaa 222");
                    Category category = response.body();
                    updateView(category);
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("log", "Categort fail ==== " + t.getMessage());
                t.printStackTrace();
            }
        });

        return view;
    }

    private void updateView(Category category) {
        final HashMap<String, String> data = category.getData();
        final String titleBoxOffice = "박스오피스 랭킹";
        final String titleMonth = data.get("month") + "월 개봉작";
        final String titleGenre = data.get("genre") + " 영화";

        Glide.with(getContext()).load(data.get("boxoffice_stillcut")).into(mImageViewBoxOffice);
        Glide.with(getContext()).load(data.get("month_movie_stillcut")).into(mImageViewMonth);
        Glide.with(getContext()).load(data.get("genre_movie_stillcut")).into(mImageViewGenre);

        mTextViewBoxOffice.setText(titleBoxOffice);
        mTextViewMonth.setText(titleMonth);
        mTextViewGenre.setText(titleGenre);

        mImageViewBoxOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMovieList("boxoffice", "", titleBoxOffice);
            }
        });

        mImageViewMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMovieList("month", "", titleMonth);
            }
        });

        mImageViewGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMovieList("genre", data.get("genre"), titleGenre);
            }
        });
    }

    private void goMovieList(String type, String genre, String title) {
        Intent intent = new Intent(getActivity(), ActivityMovieList.class);
        intent.putExtra("type", type);
        intent.putExtra("genre", genre);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
