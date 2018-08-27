package com.desafiolatam.desafioface.data;

import android.content.Context;
import android.content.SharedPreferences;

public class FcmToken {

    private static final String FCM = "com.desafiolatam.desafioface.data.FCM";
    private static final String FCM_TOKEN = "com.desafiolatam.desafioface.data.FCM_TOKEN";
    private Context context;

    public FcmToken(Context context) {
        this.context = context;
    }

    public void save(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(FCM_TOKEN, token);
        prefEditor.apply();
    }

    public String get() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FCM, Context.MODE_PRIVATE);
        return sharedPreferences.getString(FCM_TOKEN, null);
    }

}
