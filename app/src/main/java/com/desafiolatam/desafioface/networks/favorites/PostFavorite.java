package com.desafiolatam.desafioface.networks.favorites;

import android.os.AsyncTask;

import com.desafiolatam.desafioface.models.Developer;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class PostFavorite extends AsyncTask<Long, Integer, Integer> {

    @Override
    protected Integer doInBackground(Long... params) {
        int code = 007;
        Favorites favorites = new FavoriteInterceptor().get();
        Call<Developer> call = favorites.postFavorite(params[0]);
        try {
            Response<Developer> response = call.execute();
            code = response.code();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
}
