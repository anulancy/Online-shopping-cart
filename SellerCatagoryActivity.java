package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.shopping.Adapter.SellerCatagoryRecyclerAdapter;
import com.aumento.shopping.ModelClass.SellerCategoryListModelClass;
import com.aumento.shopping.utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SellerCatagoryActivity extends AppCompatActivity {

    private static final String TAG = "SellerCatagoryActivity";

    private RecyclerView sellerCatRV;
    private SellerCatagoryRecyclerAdapter listRecyclerAdapter;

    List<SellerCategoryListModelClass> sellerCategoryList;

    GlobalPreference globalPreference;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_catagory);

        globalPreference = new GlobalPreference(SellerCatagoryActivity.this);
        ip = globalPreference.RetriveIP();

        init();

        loadSellerCategory();
    }

    private void loadSellerCategory() {

        sellerCategoryList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ip+"/Aumento/Shopping/sellerCategory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray array = obj.getJSONArray("sellercat");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int id = data.getInt("id");
                        String cat_name = data.getString("scat_name");
                        String cat_image = data.getString("scat_image");

                        Log.d("item", "onResponse: "+id+cat_name+cat_image);

                        sellerCategoryList.add(new SellerCategoryListModelClass(id,cat_name,cat_image));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listRecyclerAdapter = new SellerCatagoryRecyclerAdapter(SellerCatagoryActivity.this,sellerCategoryList);
                sellerCatRV.setAdapter(listRecyclerAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerCatagoryActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void init() {
        sellerCatRV = (RecyclerView) findViewById(R.id.sellerCatagoryRecyclerView);
        sellerCatRV.setLayoutManager(new GridLayoutManager(this, 3));
        sellerCatRV.setItemAnimator(new DefaultItemAnimator());
    }
}
