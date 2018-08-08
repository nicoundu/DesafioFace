package com.desafiolatam.desafioface.networks.users;

import com.desafiolatam.desafioface.data.CurrentUserQueries;
import com.desafiolatam.desafioface.models.CurrentUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.desafiolatam.desafioface.networks.sessions.LoginInterceptor.BASE_URL;

public class UserInterceptor {

    public Users get() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request request = originalRequest.newBuilder()
                        .header("authtoken", new CurrentUserQueries().get().getAuth_token())
                        .header("Accept", "application/json")
                        .header("Source", "mobile")
                        .build();

                Response response = chain.proceed(request);

                int retryCount = 0;
                while (!response.isSuccessful() && retryCount < 3) {
                    retryCount++;
                    response = chain.proceed(request);
                }

                return response;
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit interceptor = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        Users request = interceptor.create(Users.class);

        return request;

    }

}
