package com.desafiolatam.desafioface.networks.favorites;

import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;

import com.desafiolatam.desafioface.models.Developer;

import java.io.IOException;

import retrofit2.Response;

public class PutFcmToken extends AsyncTask<String, Integer, Integer> {

    @Override
    protected Integer doInBackground(String... params) {

        int code = 007;
        String token = params[0];
        Favorites favorites = new FavoriteInterceptor().get();
        retrofit2.Call<String> call = favorites.putFcmToken(token);
        try {
            Response<String> response = call.execute();
            code = response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return code;
    }
}
