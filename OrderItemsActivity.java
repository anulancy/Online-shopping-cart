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
import com.aumento.shopping.Adapter.ItemListRecyclerAdapter;
import com.aumento.shopping.Adapter.OrderListRecyclerAdapter;
import com.aumento.shopping.ModelClass.ItemListModelClass;
import com.aumento.shopping.ModelClass.OrderListModelClass;
import com.aumento.shopping.utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderItemsActivity extends AppCompatActivity {

    private static final String TAG = "OrderItemsActivity";

    Intent intent;
    int omid;

    List<ItemListModelClass> itemList;
    ItemListRecyclerAdapter listRecyclerAdapter;

    GlobalPreference globalPreference;
    private String ip;
    private RecyclerView orderItemListRV;
    private TextView title;
    private ImageView BackImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        intent = getIntent();
        omid = intent.getIntExtra("itemId",0);

        title = (TextView) findViewById(R.id.title);
        title.setText("Orders Details");

        BackImageButton = (ImageView) findViewById(R.id.BackImageButton);
        BackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        globalPreference = new GlobalPreference(getApplicationContext());
        ip = globalPreference.RetriveIP();

        init();

        loadOrderItem();

    }

    private void init() {
        orderItemListRV = (RecyclerView) findViewById(R.id.orderItemListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        orderItemListRV.setLayoutManager(mLayoutManager);
        orderItemListRV.setItemAnimator(new DefaultItemAnimator());
    }

    private void loadOrderItem() {

        itemList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/OrdersItems.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray array = obj.getJSONArray("OrderItem");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int id = data.getInt("id");
                        String subcat_id = data.getString("subcat_id");
                        String item_name = data.getString("item_name");
                        String item_desc = data.getString("item_desc");
                        String rate = data.getString("rate");
                        String image = data.getString("image");
                        String offer = data.getString("offer");

                        Log.d("item", "onResponse: "+id+item_name+item_desc+rate+subcat_id+image);

                        itemList.add(new ItemListModelClass(id,item_name,item_desc,rate,offer,"",image));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listRecyclerAdapter = new ItemListRecyclerAdapter(itemList,OrderItemsActivity.this);
                orderItemListRV.setAdapter(listRecyclerAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderItemsActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("omid", String.valueOf(omid));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }
}
