package com.desafiolatam.desafioface.networks.users;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.desafiolatam.desafioface.app.DasfioApp;
import com.desafiolatam.desafioface.models.Developer;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Response;

public class GetUsers extends AsyncTask<Map<String, String>, Integer, Integer> {

    private int additionalPages;
    private Map<String, String> queryMap;
    private int result;
    private final Users request = new UserInterceptor().get();
    private final Context context;


    public GetUsers(int additionalPages, Context context) {
        this.additionalPages = additionalPages;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Map<String, String>... params) {

        queryMap = params[0];
        if (additionalPages < 0) {
            while (200 == connect()) {
                increasePage();
            }
        } else {
            while (additionalPages >= 0) {
                additionalPages--;
                connect();
                increasePage();
            }
        }


        return result;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (500 == integer) {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent().setAction(DasfioApp.SESION_EXPIRED));
        }
    }

    private void increasePage() {
        int page = Integer.parseInt(queryMap.get("page"));
        page++;
        queryMap.put("page", String.valueOf(page));
    }

    private int connect() {
        int code = 007;
        Call<Developer[]> call = request.get(queryMap);
        try {
            Response<Developer[]> response = call.execute();
            code = response.code();
            if (200 == code && response.isSuccessful()) {
                Developer[] developers = response.body();
                if (developers != null && developers.length > 0) {
                    Log.d("DEVELOPERS", String.valueOf(developers.length));
                    for (Developer servDeveloper: developers) {
                        List<Developer> localDevelopers = Developer.find(Developer.class, "serverId = ?", String.valueOf(servDeveloper.getId()));
                        if (localDevelopers != null && localDevelopers.size() > 0) {
                            Developer local = localDevelopers.get(0);
                            local.setEmail(servDeveloper.getEmail());
                            local.setPhoto_url(servDeveloper.getPhoto_url());
                            local.save();
                        } else {
                            servDeveloper.create();
                        }
                    }
                } else {
                    code = 666;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
            code = 000;
        }

        result = code;
        return result;
    }

}
