package com.teamfingo.android.fingo.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.main.ActivityMain;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 * == Fragment Login Main ==
 *
 * 로그인의 메인화면 역할을 한다.
 *
 * 기존사용자의 로그인과 신규회원의 회원가입화면으로 이동시켜준다.
 *
 */

public class FragmentLoginMain extends Fragment implements View.OnClickListener{

    Button btnLogIn;
    Button btnEmailSignUp;
    Button btnFacebookSigUp;

    Fragment fragmentLogin;
    Fragment fragmentEmailSignUp;

    CallbackManager mCallbackManager;

    public FragmentLoginMain() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // '페이스북으로 회원가입 하기' 기능에 대한 callback 함수
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);

        // 회원 가입 메인 화면의 view 구성 초기화
        initView(view);

        return view;
    }

    private void initView(View view){

        // 1. 기능 요청에 따라 화면(Fragment) 을 이동 할 수 있도록 필요한 Fragment 미리 설정 합니다.
        // 1.1 기존 사용자에 대한 로그인 화면 입니다.
        fragmentLogin = new FragmentLogin();
        // 1.2 이메일 회원 가입을 위한 화면 입니다.
        fragmentEmailSignUp = new FragmentEmailSignUp();

        // 2. 회원 가입 또는 로그인 액션을 위한 버튼들을 선언합니다.
        btnLogIn = (Button)view.findViewById(R.id.button_login);    // 로그인 버튼
        btnLogIn.setOnClickListener(this);

        btnEmailSignUp = (Button)view.findViewById(R.id.button_email_sign_up);  // 이메일로 회원가입 버튼
        btnEmailSignUp.setOnClickListener(this);

        btnFacebookSigUp = (Button)view.findViewById(R.id.button_facebook_sign_up); // 페이스북으로 회원가입 버튼
        btnFacebookSigUp.setOnClickListener(this);

    }

    // 각 버튼들의 행동을 정의 합니다.
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            // 1. 로그인 화면으로 이동
            case R.id.button_login:
                replaceFragment(fragmentLogin);
                break;

            // 2. 이메일 회원가입 화면으로 이동
            case R.id.button_email_sign_up:
                replaceFragment(fragmentEmailSignUp);
                break;

            // 3. 페이스북 회원 가입을 위한 Facebook Login API 호출
            case R.id.button_facebook_sign_up:
                // 3.1 페이스북에서 제공하는 로그인 액티비티에 대한 결과 값을 전달 받기 위한 callBackManager 를 선언합니다.
                mCallbackManager = CallbackManager.Factory.create();
                facebookLoginOnClick();
                break;
        }
    }

    // pager 를 쓰지않고 단순히 Fragment 를 하나의 Layout container 에서 변경 해주기 위한 method
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);

        // 전달받은 화면으로 이동합니다.
        transaction.replace(R.id.activity_login, fragment);
        transaction.commit();
    }

    // 페이스북 액티비티로 부터 response 받은 데이터를 체크합니다.
    public void facebookLoginOnClick(){

        // 1. 유저의 valid 여부를 체크하기 위한 Login manager 선언
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        // 2. valid 체크에 대한 response
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult result) {
                // 2.1 페이스북 로그인에 성공 했을 때, 서버로 Fingo token 을 요청하게 됩니다.
                requestFingoAccessToken(result.getAccessToken().getToken());
            }

            @Override
            public void onError(FacebookException error) {
                // 2.2 페이스북 로그인에 실패 했을 때, 별도의 토큰 요청 없이 에러 메세지를 띄우고 로그인 메인 화면으로 되돌아 갑니다.
                Toast.makeText(getContext(),"회원가입에 실패 하였습니다."+error, Toast.LENGTH_SHORT);
                getActivity().finish();
            }

            @Override
            public void onCancel() {
                // 2.3 인증 도중 페이스북 액티비티를 취소 했을 때, 별다른 토큰 요청없이 로그인 메인 화면으로 되돌아 갑니다.
                getActivity().finish();
            }
        });
    }

    // 페이스북 토큰을 기반으로 Fingo Token 을 요청
    private void requestFingoAccessToken(String facebook_token) {

        // 1. Fingo Token 발급을 요청
        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().createFacebookUser(facebook_token);

        // 2. 서버 response
        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                // 2.1 페이스북 토큰으로 회원가입이 완료 되면,
                if(response.isSuccessful()){
                    // 2.1.1 서버로 부터 Fingo token 을 받아 오고,
                    String token = response.body().getToken();

                    // 2.1.2 이후 앱 내에서 별도의 요청 없이 지속적으로 Token 을 사용하기위해 Shared preferences 에 Token 을 저장
                    new FingoPreferences(getContext()).setAccessToken(token);

                    // 2.1.3 로그인이 자동적으로 승인 되고, 바로 메인 서비스 화면으로 이동 할 수 있도록 구현
                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
