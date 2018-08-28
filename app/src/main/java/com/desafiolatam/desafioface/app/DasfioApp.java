package com.desafiolatam.desafioface.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.desafiolatam.desafioface.models.CurrentUser;
import com.desafiolatam.desafioface.views.login.LoginActivity;
import com.orm.SugarApp;

public class DasfioApp extends SugarApp {

    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    public static final String SESION_EXPIRED = "com.desafiolatam.desafioface.app.action.SESION_EXPIRED";

    @Override
    public void onCreate() {
        super.onCreate();
        intentFilter = new IntentFilter();
        intentFilter.addAction(SESION_EXPIRED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (SESION_EXPIRED.equals(intent.getAction())) {
                    CurrentUser.deleteAll(CurrentUser.class);
                    Intent goToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    goToLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(goToLogin);
                    Toast.makeText(context, "Sesión Expirada", Toast.LENGTH_SHORT).show();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
