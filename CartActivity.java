package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.aumento.shopping.Adapter.CartItemRecyclerAdapter;
import com.aumento.shopping.ModelClass.CartItemsModelClass;
import com.aumento.shopping.utils.GlobalPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartItemRecyclerAdapter.AdapterCallback {

    TextView title,cartSubTotalTV,cartTotalTV,cartCheckoutBt;
    ImageView cartBackImageButton;
    RecyclerView cartItemRecyclerView;
    CartItemRecyclerAdapter itemRecyclerAdapter;
    ArrayList<CartItemsModelClass> itemsList;
    GlobalPreference globalPreference;
    String ip;
    String uid;
    String amt;
    private static final String TAG = "CartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);

        title = findViewById(R.id.title);
        title.setText("Cart");

        cartSubTotalTV = findViewById(R.id.cartSubTotalTextView);
        cartTotalTV = findViewById(R.id.cartTotalTextView);
        cartBackImageButton = findViewById(R.id.BackImageButton);
        cartBackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cartCheckoutBt = findViewById(R.id.cartCheckoutBt);
        cartCheckoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cartTotalTV.getText().toString().equals("₹ 00.00"))
                {
                    Toast.makeText(CartActivity.this, "Please Add Products to Cart then Checkout", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
                    intent.putExtra("amount", amt);
                    startActivity(intent);
                    finish();
                }
            }
        });

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();
        uid = globalPreference.getUserId();

        cartItemRecyclerView = findViewById(R.id.cartItemRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        cartItemRecyclerView.setLayoutManager(mLayoutManager);
        cartItemRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadCartItem();

    }

    private void loadCartItem() {
        itemsList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/cart.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                if(!response.equals("")) {
                    try {
                        JSONObject obj = new JSONObject(response);

                        JSONArray array = obj.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject data = array.getJSONObject(i);

                            int id = data.getInt("id");
                            String qty = data.getString("o_qty");

                            JSONArray members = data.getJSONArray("item");

                            String item_name = null;
                            String rate = null;
                            String image = null;
                            String offer = null;

                            for (int j = 0; j < members.length(); j++) {
                                JSONObject mem = members.getJSONObject(j);
                                int idd = mem.getInt("id");
                                item_name = mem.getString("item_name");
                                rate = mem.getString("rate");
                                image = mem.getString("image");
                                offer = mem.getString("offer");
                            }

//                        Log.d(TAG, "onResponse: cart id"+id+" qty = "+qty+"\n item_name="+item_name+" rate="+rate);
                            itemsList.add(new CartItemsModelClass(id, item_name, image, rate, qty, offer));

                        }

                        JSONArray array1 = obj.getJSONArray("AMOUNT");
                        JSONObject data = array1.getJSONObject(0);
                        String total = data.getString("Total");
                        String count = data.getString("count");

                        amt = total;
                        cartSubTotalTV.setText("₹ " + total);
                        cartTotalTV.setText("₹ " + total);
                        Log.d(TAG, "Amount " + total + " " + count);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    cartSubTotalTV.setText("₹ 00.00");
                    cartTotalTV.setText("₹ 00.00");
                }

                itemRecyclerAdapter = new CartItemRecyclerAdapter(getApplicationContext(), itemsList, CartActivity.this);
                cartItemRecyclerView.setAdapter(itemRecyclerAdapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClicked(int position) {
        loadCartItem();
    }
}
