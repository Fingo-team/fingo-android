package com.teamfingo.android.fingo.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.teamfingo.android.fingo.R;

import org.json.JSONObject;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 *
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 *
 * == Fragment Facebook SignUp ==
 *
 * Facebook을 이용한 가입을 위한 Fragment
 *
 */

public class FragmentFacebookSignUp extends Fragment {

    LoginButton loginButton;

    CallbackManager mCallbackManager;

    //TODO 서버에서는 AccessToken 과 프로필 이미지만을 전달 받는것으로 한다.

    public FragmentFacebookSignUp() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_facebook_sign_up, container, false);

        FacebookSdk.sdkInitialize(getApplicationContext());
        // Fragment 로 구현 할 때에는 Callback Manager 를 private 으로 선언 하면 안됨.
        mCallbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        // Activity 가 아닌 Fragment 에서 해당 로그인 작업을 수행 할 때에는 아래 세팅이 추가적으로 필요하다.
        // 개발자가 별도의 facebook 로그인 화면을 구성하지 않더라도 SDK 상에 이미 구현된 View(Fragment) 를 set 하게 됨.
        loginButton.setFragment(this);

        // 로그인 결과 값을 받아오는 Callback 함수
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // loginResult 를 기반으로 Facebook Graph API 에 접근하여 인증 토큰을 이용한 구체적인 응용 작업( ex)프로필 정보 가져오기) 진행
                GraphRequest mGraphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                                                        new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("Facebook Login Result", object.toString());
                    }
                });

                // 성공적으로 login token 을 기반으로 Graph API 에 접근 했다면
                // Bundle 에 필요한 데이터 형식들을 정의하고
                // mGraphRequest 를 이용해 요청/응답 을 받을 수 있다.
                Bundle mParameters = new Bundle();
                mParameters.putString("fields","id,name,email,gender,birthday");

                mGraphRequest.setParameters(mParameters);
                mGraphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.e("Facebook Login Result", "token denied !!");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
            }
        });

        return view;
    }


}
