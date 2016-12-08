package com.teamfingo.android.fingo.mypage;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.interfaces.FingoService;
import com.teamfingo.android.fingo.login.ActivityLogin;
import com.teamfingo.android.fingo.model.UserDetail;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentMyPage extends Fragment implements View.OnClickListener {

    ImageButton btnMyPageSetting, btnMyPageAdd;
    ImageView ivProfile, ivProfileCover;
    TextView tvUserName, tvUserIntroduce;
    Button btnComment, btnWish, btnWatched;

    LinearLayout mLinearLayout;

    FingoPreferences mPref;

    public static final int MY_PAGE_COMMENT = 0;
    public static final int MY_PAGE_WISH = 1;
    public static final int MY_PAGE_WATCHED = 2;

    private static final String BASE_URL = "http://eb-fingo-real.ap-northeast-2.elasticbeanstalk.com/";

    public FragmentMyPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        mPref = new FingoPreferences(getContext());

        btnMyPageSetting = (ImageButton) view.findViewById(R.id.button_mypage_setting);
        btnMyPageSetting.setOnClickListener(this);

        btnMyPageAdd = (ImageButton) view.findViewById(R.id.button_mypage_add);
        btnMyPageAdd.setOnClickListener(this);

        ivProfile = (ImageView) view.findViewById(R.id.image_profile);
        ivProfile.setOnClickListener(this);

        ivProfileCover = (ImageView) view.findViewById(R.id.image_profile_cover);
        ivProfileCover.setOnClickListener(this);

        tvUserName = (TextView) view.findViewById(R.id.textView_user_nickname);
        tvUserIntroduce = (TextView) view.findViewById(R.id.textView_user_introduce);

        btnComment = (Button) view.findViewById(R.id.button_comment);
        btnComment.setOnClickListener(this);

        btnWish = (Button) view.findViewById(R.id.button_wish);
        btnWish.setOnClickListener(this);

        btnWatched = (Button) view.findViewById(R.id.button_watched);
        btnWatched.setOnClickListener(this);

        callFingoService();

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_mypage_setting:
                expireFingoToken();
                break;

            case R.id.button_mypage_add:

                break;

            case R.id.image_profile:

                break;

            case R.id.image_profile_cover:

                break;

            case R.id.button_comment:
                sendFragment(MY_PAGE_COMMENT);
                break;

            case R.id.button_wish:
                sendFragment(MY_PAGE_WISH);
                break;

            case R.id.button_watched:
                sendFragment(MY_PAGE_WATCHED);
                break;
        }
    }

    public void sendFragment(int fragment_id) {

        Intent intent = new Intent(getActivity(), ActivityMyPage.class);
        intent.putExtra("Fragment", fragment_id);
        startActivity(intent);
    }

    private void expireFingoToken() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FingoService fingoService = retrofit.create(FingoService.class);
        Call<Void> fingoLogoutCall = fingoService.userEmailLogout(mPref.getAccessToken());
        fingoLogoutCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                mPref.getAccessToken();
                Log.e("Check Login Status", ">>>>>>>>" + response.message());

                if (response.isSuccessful()) {

                    Log.e("CHECK_TOKEN_BEFORE", mPref.getAccessToken());
                    mPref.removeAccessToken();
                    Log.e("CHECK_TOKEN_AFTER", mPref.getAccessToken());
                    Log.e("Check Token Status", mPref.getAccessToken());
                    Log.e("Check Login Status", ">>>> 로그아웃 성공!!");

                    Intent intent = new Intent(getActivity(), ActivityLogin.class);
                    startActivity(intent);
                    getActivity().finish();

                } else
                    Log.e("Check Login Status", ">>>> 로그아웃 실패!!");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }

    private void callFingoService() {

        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FingoService service = client.create(FingoService.class);
        Call<UserDetail> userDetailCall = service.getUserDetail(mPref.getAccessToken());
        userDetailCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if(response.isSuccessful()){
                    final UserDetail data = response.body();

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            tvUserName.setText(data.getUser_profile().getNickname());

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {

            }
        });
    }
}
