package com.teamfingo.android.fingo.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by taewon on 2016-12-02.
 */

public class FingoAccessToken {

    // 값 저장하기
    private void setAccessToken(Context context, String token){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Token", token);
        editor.commit();
    }

    // 값 불러오기
    public static String getAccessToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString("Token", "");
    }

    // 값(Key Data) 삭제하기
    private void RemoveAccessToken(Context context){
        SharedPreferences pref = context.getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("Token");
        editor.commit();
    }


}
