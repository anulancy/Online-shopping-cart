package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.shopping.Adapter.OrderListRecyclerAdapter;
import com.aumento.shopping.ModelClass.OrderListModelClass;
import com.aumento.shopping.utils.GlobalPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderActivity extends AppCompatActivity {

    private static final String TAG = "MyOrderActivity";

    GlobalPreference globalPreference;
    private String ip;
    private String uid;
    private RecyclerView orderListRecyclerView;
    List<OrderListModelClass> orderList;
    private OrderListRecyclerAdapter orderListRecyclerAdapter;
    private TextView title;
    private ImageView BackImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        title = findViewById(R.id.title);
        title.setText("My Orders");

        BackImageButton = findViewById(R.id.BackImageButton);
        BackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();
        uid = globalPreference.getUserId();

        init();

        loadOrderList();

    }

    private void loadOrderList() {

        orderList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/Orders.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray array = obj.getJSONArray("Order");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int id = data.getInt("id");
                        String date = data.getString("date");
                        String order_number = data.getString("order_number");
                        String amount = data.getString("amount");
                        String order_rating = data.getString("order_rating");

                        Log.d("item", "onResponse: "+id+date+order_number+amount);

                        orderList.add(new OrderListModelClass(id,date,amount,order_number,order_rating));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                orderListRecyclerAdapter = new OrderListRecyclerAdapter(MyOrderActivity.this,orderList);
                orderListRecyclerView.setAdapter(orderListRecyclerAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyOrderActivity.this, ""+error, Toast.LENGTH_SHORT).show();
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

        orderListRecyclerView = (RecyclerView) findViewById(R.id.orderListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        orderListRecyclerView.setLayoutManager(mLayoutManager);
        orderListRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


}


