package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.shopping.utils.GlobalPreference;
import com.aumento.shopping.utils.SignUpActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // lUserName : loginUserName
    private EditText lUserNameET,lpasswordET;
    private TextView lForgotPasswordTV,lskipTV;
    private LinearLayout lSignUpTextBt;
    private CheckBox lrememberChkbx;
    private Button loginBT;

    private String lusername,lpassword;
    private String ip;
    private GlobalPreference globalPreference;

    private static final String TAG = "LoginActivity";
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();

        init();

    }

    private void init() {
        lUserNameET = findViewById(R.id.loginUsernameEditText);
        lpasswordET = findViewById(R.id.loginPasswordEditText);

        lskipTV = findViewById(R.id.skipLoginTextView);
        lskipTV.setOnClickListener(this);

        lForgotPasswordTV = findViewById(R.id.loginForgotPasswordEditText);
        lForgotPasswordTV.setOnClickListener(this);

        lSignUpTextBt = findViewById(R.id.loginSignUpTextLL);
        lSignUpTextBt.setOnClickListener(this);

        lrememberChkbx = findViewById(R.id.loginRemenberCheckBox);
        lrememberChkbx.setOnClickListener(this);

        loginBT = findViewById(R.id.loginButton);
        loginBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //login button click
            case R.id.loginButton:
                login();
                break;

            //remember the login details
            case R.id.loginRemenberCheckBox:

                break;

            //siin up if u dont have a account
            case R.id.loginSignUpTextLL:

                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

                break;

            //forgot password to reset password
            case R.id.loginForgotPasswordEditText:

                break;

            case R.id.skipLoginTextView:
                intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void login() {
        lusername = lUserNameET.getText().toString();
        lpassword = lpasswordET.getText().toString();

        if (lusername.equals("") || lpassword.equals("")) {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
        }

        else {

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + ip + "/Aumento/Shopping/login.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d(TAG, "onResponse: "+response);

                    if(response.contains("Unsucessfull"))
                    {
                        Toast.makeText(LoginActivity.this, "Please check your Username and Password ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String userId= response.trim();
                        globalPreference.setUserId(userId);

                        if(lrememberChkbx.isEnabled())
                        {
                            globalPreference.setLoginStatus(true);
                        }
                        else
                            globalPreference.setLoginStatus(false);

                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "successfull" + response, Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", lusername);
                    params.put("password", lpassword);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
