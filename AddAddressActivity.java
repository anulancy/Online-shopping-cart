package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    private static final String TAG = "AddAddressActivity";

    private TextView title;
    private EditText addressEditText;
    private EditText areaEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText pincodeEditText;
    private TextView saveAddressButtonTextView;

    String address,area,city,state,pincode;
    String uid;
    String ip;
    GlobalPreference globalPreference;
    private Intent intent;
    private String dataResponse;
    private ImageView BackImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        title = findViewById(R.id.title);
        title.setText("Add Address");

        BackImageButton = (ImageView) findViewById(R.id.BackImageButton);
        BackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = getIntent();
        dataResponse = intent.getStringExtra("userdata");

        Log.d(TAG, "onCreate: "+dataResponse);

        globalPreference = new GlobalPreference(this);
        uid = globalPreference.getUserId();
        ip = globalPreference.RetriveIP();

        init();

        setData(dataResponse);

        saveAddressButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                address = addressEditText.getText().toString();
                area = areaEditText.getText().toString();
                city = cityEditText.getText().toString();
                state = stateEditText.getText().toString();
                pincode = pincodeEditText.getText().toString();

                addAddress();
            }
        });

    }

    private void setData(String response) {

        try {

            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("data");
            JSONObject data = array.getJSONObject(0);

            String Address = data.getString("Address");
            String street = data.getString("street");
            String city = data.getString("city");
            String state = data.getString("state");
            String pincode = data.getString("pincode");


            addressEditText.setText(Address);
            areaEditText.setText(street);
            cityEditText.setText(city);
            stateEditText.setText(state);
            pincodeEditText.setText(pincode);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void init() {
        addressEditText = findViewById(R.id.addressEditText);
        areaEditText = findViewById(R.id.AreaEditText);
        cityEditText = findViewById(R.id.CityEditText);
        stateEditText = findViewById(R.id.StateEditText);
        pincodeEditText = findViewById(R.id.PincodeTextView);
        saveAddressButtonTextView = findViewById(R.id.saveAddressButton);
    }

    private void addAddress() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/addressUpdate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddAddressActivity.this, "Address Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAddressActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",uid);
                params.put("address",address);
                params.put("area",area);
                params.put("city",city);
                params.put("state",state);
                params.put("pincode",pincode);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
