package com.teamfingo.android.fingo.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.teamfingo.android.fingo.R;
import com.teamfingo.android.fingo.main.ActivityMain;
import com.teamfingo.android.fingo.model.FingoAccessToken;
import com.teamfingo.android.fingo.utils.AppController;
import com.teamfingo.android.fingo.utils.FingoPreferences;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-11-28
 *
 * == Fragment Login ==
 *
 * Email 또는 Facebook 을 이용한 로그인 기능 구현
 */

public class FragmentLogin extends Fragment implements View.OnClickListener {

    // email 로그인과 facebook 로그인을 위한 버튼 선언
    Button btnEmailLogin;
    Button btnFacebookLogin;

    EditText etEmail;
    EditText etPassword;

    String mEmail;
    String mPassword;

    CallbackManager mCallbackManager;

    public FragmentLogin() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnEmailLogin = (Button) view.findViewById(R.id.button_email_login);
        btnEmailLogin.setOnClickListener(this);

        btnFacebookLogin = (Button) view.findViewById(R.id.button_facebook_login);
        btnFacebookLogin.setOnClickListener(this);

        etEmail = (EditText) view.findViewById(R.id.editText_Login_Email);
        etPassword = (EditText) view.findViewById(R.id.editText_Login_Password);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // TODO facebook SDK 를 이용해 인증 할 수 있도록 구현 해야 함
            case R.id.button_facebook_login:

                mCallbackManager = CallbackManager.Factory.create();
                facebookLoginOnClick();
                break;

            // 버튼 클릭 시 ActivityMain 으로 이동
            case R.id.button_email_login:

                mEmail = etEmail.getText().toString();
                mPassword = etPassword.getText().toString();

                callFingoAPI();

                break;
        }
    }

    // CallFingoAPI Method Overloading
    // 이메일 로그인 할 때의 Fingo login API 호출
    private void callFingoAPI() {

        Log.e("CHECK!!", "==========" + mEmail);
        Log.e("CHECK!!", "==========" + mPassword);

        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().userEmailLogin(mEmail, mPassword);

        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if (response.isSuccessful()) {

                    FingoPreferences pref = new FingoPreferences(getContext());

                    Log.e("CHECK TOKEN_GET_BEFORE", ">>>>>>>>" + pref.getAccessToken());

                    String token = response.body().getToken();
                    pref.setAccessToken(token);

                    Log.e("CHECK TOKEN_GET_AFTER", ">>>>>>>>" + token);

                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "로그인에 실패 하였습니다!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    // CallFingoAPI Method Overloading
    // 페이스북 로그인 할 때의 Fingo login API 호출
    private void callFingoAPI(String facebook_token) {

        Call<FingoAccessToken> fingoAccessTokenCall = AppController.getFingoService().createFacebookUser(facebook_token);
        fingoAccessTokenCall.enqueue(new Callback<FingoAccessToken>() {
            @Override
            public void onResponse(Call<FingoAccessToken> call, Response<FingoAccessToken> response) {
                if (response.isSuccessful()) {

                    FingoPreferences pref = new FingoPreferences(getContext());

                    Log.e("CHECK TOKEN_GET_BEFORE", ">>>>>>>>" + pref.getAccessToken());

                    String token = response.body().getToken();
                    pref.setAccessToken(token);

                    Log.e("CHECK TOKEN_GET_AFTER", ">>>>>>>>" + token);

                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

                    getActivity().finish();

                } else {
                    // TODO 어떤 정보의 중복으로 인해 회원가입이 되지 않는것인지 출력되는 메세지 세분화가 필요
                    Toast.makeText(getContext(), "로그인에 실패 하였습니다!!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FingoAccessToken> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void facebookLoginOnClick() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult result) {

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            Log.i("TAG", "user: " + user.toString());
                            Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
//                            setResult(RESULT_OK);

                            callFingoAPI(result.getAccessToken().getToken());

                        }
                    }
                });

                // TODO GraphRequest 의 onComplete 함수와 아래의 로직 간의 동작 순서를 다시 알아 볼 필요가 있음.
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);

            }

            @Override
            public void onCancel() {

            }
        });
    }

}
