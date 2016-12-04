package com.teamfingo.android.fingo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.R.attr.key;
import static android.content.Context.MODE_PRIVATE;

/**
 * 작성자 : 김태원
 * 소속 : fastcampus
 * 작성일 : 2016-12-04
 *
 * == Fingo Preferences ==
 *
 * 1. Fingo 서비스 제공에 있어서 필요한 singleton 정보 정의
 *
 * 2. 유저 토큰정보 이 외에 다른 공유 정보 또한 여기서 키를 지정하고 저장 가능
 *
 */

public class FingoPreferences {

    private static Context mContext;
    private static SharedPreferences mPref;
    private static SharedPreferences.Editor mEditor;

    private static final String PREF_NAME = "com.teamfingo.android.fingo.Utils.pref";
    private static final String PREF_ACCESS_ERROR = "Can not access preference using this key : ";

    public static final String USER_TOKEN = "Token";

    // Shared Preferences 에 접근 하기위한 기본적인 구성 요소( context, preference, editor ) 세팅
    public FingoPreferences(Context context) {

        mContext = context;
        mPref = mContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        mEditor = mPref.edit();

    }

    // 값 저장하기
    public static void setAccessToken(String token) {

        mEditor.putString(USER_TOKEN, token);
        mEditor.commit();
    }

    // 값 불러오기
    public static String getAccessToken() {

        try {
            return mPref.getString(USER_TOKEN, "");
        } catch (Exception e) {

            return PREF_ACCESS_ERROR + key;

        }
    }

    // 값(Key Data) 삭제하기
    public static void RemoveAccessToken() {

        mEditor.remove(USER_TOKEN);
        mEditor.commit();
    }


}
