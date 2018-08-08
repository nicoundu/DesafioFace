package com.desafiolatam.desafioface.views.login;

import com.desafiolatam.desafioface.models.CurrentUser;
import com.desafiolatam.desafioface.networks.sessions.LoginInterceptor;
import com.desafiolatam.desafioface.networks.sessions.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn {

    private SessionCallback callback;

    public SignIn(SessionCallback callback) {
        this.callback = callback;
    }

    public void toServer(String email, String pass) {

        if (email.trim().length() <= 0 && pass.trim().length() <= 0) {
            callback.requieredField();

        } else {
            if (!email.contains("@")) {
                callback.mailFormat();
            } else {
                Session session = new LoginInterceptor().get();
                Call<CurrentUser> call = session.login(email, pass);
                call.enqueue(new Callback<CurrentUser>() {
                    @Override
                    public void onResponse(Call<CurrentUser> call, Response<CurrentUser> response) {

                        if (200 == response.code() && response.isSuccessful()) {
                            CurrentUser user = response.body();
                            user.create();
                            callback.succes();
                        } else {
                            callback.fail();
                        }

                    }

                    @Override
                    public void onFailure(Call<CurrentUser> call, Throwable t) {

                        callback.fail();

                    }
                });

            }
        }

    }
}
