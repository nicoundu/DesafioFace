package com.desafiolatam.desafioface.fcm;

import android.util.Log;

import com.desafiolatam.desafioface.data.FcmToken;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class IdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", token);
        new FcmToken(this).save(token);
    }
}
