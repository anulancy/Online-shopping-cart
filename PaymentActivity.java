package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.ETC1;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.shopping.ModelClass.CatagoryListModelClass;
import com.aumento.shopping.ModelClass.SubCatagoryModelClass;
import com.aumento.shopping.utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity{

    private ImageView editAdrssImageView;
    private RadioButton cardRadioButton, codRadioButton;
    private TextView addressTextView,paymentTVButton,amountTextView;
    EditText cardnoEditText,cvvEditText;
    LinearLayout cardDetailsLL;
    private static final String TAG = "PaymentActivity";

    private String uid;
    private String ip;
    private String amount;
    private String ptype;
    private Intent intent;
    private TextView title;

    List<String> month = new ArrayList<>();

    List<String> year = new ArrayList<>();
    private Spinner MMspin;
    private Spinner YYspin;
    private ImageView BackButton;
    private String userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        title = findViewById(R.id.title);
        title.setText("Payment Checkout");

        BackButton = (ImageView) findViewById(R.id.BackImageButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        month.add("MM");
        for (int i = 1 ; i <= 31 ; i++)
        {
            month.add(String.valueOf(i));
        }

        year.add("YY");
        for(int i = 19 ; i<=30 ; i++ )
            year.add(String.valueOf(i));

        MMspin = (Spinner) findViewById(R.id.mmspinner);
        ArrayAdapter MM = new ArrayAdapter(this,android.R.layout.simple_spinner_item,month);
        MM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MMspin.setAdapter(MM);

        YYspin = (Spinner) findViewById(R.id.yyspinner);
        ArrayAdapter YY = new ArrayAdapter(this,android.R.layout.simple_spinner_item,year);
        YY.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        YYspin.setAdapter(YY);

        GlobalPreference globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();
        uid = globalPreference.getUserId();

        intent = getIntent();
        amount = intent.getStringExtra("amount");

        Log.d(TAG, "Intent amount: "+amount+" uid: "+uid);

        init();

        setAddress();

        amountTextView.setText("₹ "+amount);

        editAdrssImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this,AddAddressActivity.class);
                intent.putExtra("userdata",userdata);
                startActivity(intent);
            }
        });


        cardRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardRadioButton.isChecked()) {
                    codRadioButton.setChecked(false);
                    ptype = "Card Payment";
                    cardDetailsLL.setVisibility(View.VISIBLE);

                }
                else{
                    cardRadioButton.setChecked(true);
                }
            }
        });

        codRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(codRadioButton.isChecked()) {
                    cardRadioButton.setChecked(false);
                    ptype = "cod";
                    cardDetailsLL.setVisibility(View.GONE);
                }
                else
                    codRadioButton.setChecked(true);
            }
        });

        paymentTVButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+ptype);

                if(!cardRadioButton.isChecked() && !codRadioButton.isChecked())
                {
                    Toast.makeText(PaymentActivity.this, "Please select a payment method ", Toast.LENGTH_SHORT).show();
                }
                else if(cardRadioButton.isChecked() && cardnoEditText.getText().toString().equals("") ||
                        cardRadioButton.isChecked() && cvvEditText.getText().toString().equals("") ||
                        cardRadioButton.isChecked() && MMspin.getSelectedItem().toString().equals("MM") ||
                        cardRadioButton.isChecked() && YYspin.getSelectedItem().toString().equals("YY"))
                {
                    Toast.makeText(PaymentActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else if(addressTextView.getText().toString().equals(""))
                {
                    Toast.makeText(PaymentActivity.this, "Please Enter the delivery Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    paynow();
                }

            }
        });

    }

    private void paynow() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/checkout.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if (ptype.equals("cod")) {
                    intent = new Intent(PaymentActivity.this, SuccessfullOrderActivity.class);
                    intent.putExtra("orderno",response.trim());
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(PaymentActivity.this, PaymentGatewayActivity.class);
                    intent.putExtra("orderno",response.trim());
                    intent.putExtra("amt",amount);
                    startActivity(intent);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uid",uid);
                params.put("ptype",ptype);
                params.put("amount",amount);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void setAddress() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/getAddress.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                userdata = response;

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray array = obj.getJSONArray("data");

                        JSONObject data = array.getJSONObject(0);

                        String Address = data.getString("Address");
                        String street = data.getString("street");
                        String city = data.getString("city");
                        String state = data.getString("state");
                        String pincode = data.getString("pincode");

                        if(Address.equals("") && state.equals("") && city.equals("") && state.equals("") & pincode.equals(""))
                            addressTextView.setText("");
                        else
                            addressTextView.setText(Address+"\n"+street+","+city+"\n"+state+", "+pincode);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        editAdrssImageView = findViewById(R.id.editAdrssImageView);
        cardRadioButton = findViewById(R.id.radioCard);
        codRadioButton = findViewById(R.id.radioCOD);
        addressTextView = findViewById(R.id.addresTextView);
        paymentTVButton = findViewById(R.id.paymentTextViewButton);
        cardDetailsLL = findViewById(R.id.cardDetailsLinearLayout);
        amountTextView = findViewById(R.id.amountTextView);
        cardnoEditText = findViewById(R.id.cardnoEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setAddress();
    }

}
