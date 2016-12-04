package com.teamfingo.android.fingo.mypage;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.utils.FingoPreferences;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.login.ActivityLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentMyPage extends Fragment {

    Button btnFacebookLogout;
    Button btnLogout;

    private String BASE_URL = "http://eb-fingo-real.ap-northeast-2.elasticbeanstalk.com/";

    public FragmentMyPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        btnFacebookLogout = (Button) view.findViewById(R.id.button_facebook_logout);
        btnFacebookLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 페이스북 로그아웃을 위한 메소드 호출
                LoginManager.getInstance().logOut();
                Log.e("Check Facebook Logout", "Logout Successfully!!");

            }
        });

        btnLogout = (Button) view.findViewById(R.id.button_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Check Token Status", FingoPreferences.getAccessToken(getContext()));
                FingoPreferences.RemoveAccessToken(getContext());
                Log.e("Check Token Status", FingoPreferences.getAccessToken(getContext()));

                callFingoAPI();
            }
        });
        return view;
    }

    private void callFingoAPI() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FingoService fingoService = retrofit.create(FingoService.class);
        Call<Void> fingoLogoutCall = fingoService.userEmailLogout("token "+ FingoPreferences.getAccessToken(getContext()));
        fingoLogoutCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                FingoPreferences.getAccessToken(getContext());
                Log.e("Check Login Status", ">>>>>>>>" + response.message());

                if (response.isSuccessful()) {

                    FingoPreferences.RemoveAccessToken(getContext());
                    Log.e("Check Token Status", FingoPreferences.getAccessToken(getContext()));
                    Log.e("Check Login Status", ">>>> 로그아웃 성공!!");

                    Intent intent = new Intent(getView().getContext(), ActivityLogin.class);
                    startActivity(intent);
                    getActivity().finish();

                }
                else
                    Log.e("Check Login Status", ">>>> 로그아웃 실패!!");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
