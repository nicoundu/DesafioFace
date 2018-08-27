package com.desafiolatam.desafioface.views.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desafiolatam.desafioface.R;
import com.desafiolatam.desafioface.background.RecentUserService;
import com.desafiolatam.desafioface.data.FcmToken;
import com.desafiolatam.desafioface.networks.favorites.PutFcmToken;
import com.desafiolatam.desafioface.views.main.MainActivity;


public class LoginActivity extends AppCompatActivity implements SessionCallback {

    private TextInputLayout mailWrapper, passWrapper;
    private EditText mailEt, passEt;
    private Button button;
    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mailWrapper = findViewById(R.id.emailTil);
        passWrapper = findViewById(R.id.passwordTil);
        mailEt = findViewById(R.id.emailEt);
        passEt = findViewById(R.id.passwordEt);
        button = findViewById(R.id.signBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mailEt.getText().toString();
                String pass = passEt.getText().toString();
                mailWrapper.setVisibility(View.GONE);
                passWrapper.setVisibility(View.GONE);
                button.setVisibility(View.GONE);

                new SignIn(LoginActivity.this).toServer(email, pass);
            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction(RecentUserService.USERS_FINISHED);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (RecentUserService.USERS_FINISHED.equals(action)) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void restoreView() {

        mailEt.setError(null);
        passEt.setError(null);
        mailWrapper.setVisibility(View.VISIBLE);
        passWrapper.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);

    }


    @Override
    public void requieredField() {
        restoreView();
        mailEt.setError("requerido");
        passEt.setError("requerido");

    }

    @Override
    public void mailFormat() {
        restoreView();
        mailEt.setError("formato incorrecto");

    }

    @Override
    public void succes() {
        String fcmToken = new FcmToken(this).get();
        new PutFcmToken().execute(fcmToken);
        RecentUserService.startActionRecentUsers(this);

    }

    @Override
    public void fail() {
        restoreView();
        Toast.makeText(this, "Mail o Password Incorrecto", Toast.LENGTH_SHORT).show();

    }
}
