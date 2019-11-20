package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.shopping.Adapter.CartItemRecyclerAdapter;
import com.aumento.shopping.Adapter.CatagoryListRecyclerAdapter;
import com.aumento.shopping.ModelClass.CartItemsModelClass;
import com.aumento.shopping.ModelClass.CatagoryListModelClass;
import com.aumento.shopping.ModelClass.SubCatagoryModelClass;
import com.aumento.shopping.utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CatagoriesActivity extends AppCompatActivity {

    RecyclerView catagoryRecycler;
    GlobalPreference globalPreference;
    CatagoryListRecyclerAdapter catagoryListAdapter;
    List<CatagoryListModelClass> catagoryList;
    List<SubCatagoryModelClass> subCatagoryList;
    String ip;
    private TextView title;
    private ImageView BackImageButton;

    private static final String TAG = "CatagoriesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagories);

        title = findViewById(R.id.title);
        title.setText("Catagory");

        BackImageButton = findViewById(R.id.BackImageButton);
        BackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        GlobalPreference globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();

        init();

/*        catagoryRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        catagoryRecycler.setItemAnimator(new DefaultItemAnimator());*/

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        catagoryRecycler.setLayoutManager(mLayoutManager);
        catagoryRecycler.setItemAnimator(new DefaultItemAnimator());

        loadCatagory();
    }

    private void loadCatagory() {
        catagoryList = new ArrayList<>();

        subCatagoryList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ip+"/Aumento/Shopping/catagory.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray array = obj.getJSONArray("catagory");

                    for (int i = 0; i < array.length(); i++) {

                        subCatagoryList = new ArrayList<>();

                        JSONObject data = array.getJSONObject(i);

                        int id = data.getInt("id");
                        String cat_name = data.getString("cat_name");
                        String cat_desc = data.getString("cat_desc");
                        String cat_image = data.getString("cat_image");
                        String cat_offer = data.getString("cat_offer");

                        JSONArray subcat = data.getJSONArray("subcat");

                        for (int j = 0; j < subcat.length(); j++) {
                            JSONObject sub = subcat.getJSONObject(j);
                            int idd = sub.getInt("id");
                            String subcat_name = sub.getString("subcat_name");
                            String subcat_image = sub.getString("subcat_image");
                            String subcatagory_offer = sub.getString("subcatagory_offer");

                            subCatagoryList.add(new SubCatagoryModelClass(idd,subcat_name,subcatagory_offer,subcat_image));
                        }

                        Log.d("cat", "onResponse: cart id" + subCatagoryList.size());
                        catagoryList.add(new CatagoryListModelClass(id,cat_offer,cat_name,cat_desc,cat_image,subCatagoryList));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                catagoryListAdapter = new CatagoryListRecyclerAdapter(getApplicationContext(),catagoryList);
                catagoryRecycler.setAdapter(catagoryListAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void init() {
        catagoryRecycler = findViewById(R.id.catagoryRecyclerView);
    }
}
