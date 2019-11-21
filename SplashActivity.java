package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.aumento.shopping.utils.GlobalPreference;

public class SplashActivity extends AppCompatActivity {

    private GlobalPreference globalPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        globalPreference = new GlobalPreference(this);
        final boolean status = globalPreference.getLoginStatus();

        if(status)
        {

        }

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(1*1000);

                    if (status)
                    {
                        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }


                    // After 5 seconds redirect to another intent
                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
