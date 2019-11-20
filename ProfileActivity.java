package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.shopping.utils.GlobalPreference;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    CircleImageView profileImage;
    TextView profilenameTV, profilePhoneNoTV, profileEmailTV,profileAddressTV,changeAddressTV,orderTextView;
    ImageView profileEditIV;
    private GlobalPreference globalPreference;
    private String ip;
    private String uid;
    private Intent intent;
    String userdata;
    private TextView title;
    private ImageView BackImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();
        uid = globalPreference.getUserId();

        title = (TextView)findViewById(R.id.title);
        title.setText("My Profile");

        BackImageButton = (ImageView)findViewById(R.id.BackImageButton);
        BackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Log.d(TAG, "UID "+uid);

        getUserDetails();

        changeAddressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ProfileActivity.this,AddAddressActivity.class);
                intent.putExtra("userdata",userdata);
                startActivity(intent);
            }
        });

        orderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ProfileActivity.this,MyOrderActivity.class);
                startActivity(intent);
            }
        });

        profileEditIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                finish();
                intent.putExtra("userdata",userdata);
                startActivity(intent);
            }
        });

    }

    private void getUserDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/getAddress.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, "onResponse: " + response);

                if(!response.equals("")) {
                    try {

                        userdata = response;

                        JSONObject obj = new JSONObject(response);
                        JSONArray array = obj.getJSONArray("data");
                        JSONObject data = array.getJSONObject(0);

                        String name = data.getString("cust_name");
                        String phn = data.getString("phn");
                        String email = data.getString("email");
                        String Address = data.getString("Address");
                        String street = data.getString("street");
                        String city = data.getString("city");
                        String state = data.getString("state");
                        String pincode = data.getString("pincode");
                        String image = data.getString("cust_image");

                        Log.d(TAG, "IMAGE URL: http://" + ip + "/Aumento/Shopping/admin/tbl_customer/uploads/" + image);

                        if (!image.equals("")) {
                            Glide.with(getApplicationContext())
                                    .load("http://" + ip + "/Aumento/Shopping/admin/tbl_customer/uploads/" + image)
                                    .into(profileImage);
                        }

                        profilenameTV.setText(name);
                        profilenameTV.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                        profilePhoneNoTV.setText("+91" + phn);
                        profileEmailTV.setText(email);
                        if (Address.equals("") && state.equals("") && city.equals("") && state.equals("") & pincode.equals(""))
                            profileAddressTV.setText("");
                        else
                            profileAddressTV.setText(Address + ",\n" + street + ", " + city + ", \n" + state + ", " + pincode);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    profileEditIV.setVisibility(View.INVISIBLE);
                    orderTextView.setClickable(false);
                    changeAddressTV.setClickable(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                profileEditIV.setVisibility(View.INVISIBLE);
                orderTextView.setClickable(false);
                changeAddressTV.setClickable(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",uid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void init() {
        profileImage = findViewById(R.id.profileImageView);
        profilenameTV = findViewById(R.id.profileNameTextView);
        profilePhoneNoTV = findViewById(R.id.profilePhoneNoTextView);
        profileEmailTV = findViewById(R.id.profileEmailTextView);
        profileEditIV = findViewById(R.id.pofileEditImageView);
        profileAddressTV  = findViewById(R.id.profileAddressTextView);
        changeAddressTV = findViewById(R.id.changeAddressTextView);
        orderTextView = findViewById(R.id.orderTextView);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getUserDetails();
    }

}











