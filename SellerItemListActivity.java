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
import com.aumento.shopping.Adapter.SellerItemListRecyclerAdapter;
import com.aumento.shopping.ModelClass.SellerItemListModelClass;
import com.aumento.shopping.utils.GlobalPreference;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerItemListActivity extends AppCompatActivity {

    private static final String TAG = "SellerItemListActivity";

    private RecyclerView sellerItemListRV;
    private TextView title;
    private String scat;
    private String scatId;
    private GlobalPreference globalPreference;
    private String ip;
    private ArrayList<SellerItemListModelClass> itemList;
    private SellerItemListRecyclerAdapter listRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_item_list);

        title = (TextView) findViewById(R.id.title);

        Intent intent = getIntent();
        scat = intent.getStringExtra("scatagory");
        scatId = intent.getStringExtra("scatid");

        title.setText(scat);

        ImageView BackImageButton = (ImageView) findViewById(R.id.BackImageButton);
        BackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        globalPreference = new GlobalPreference(this);
        ip = globalPreference.RetriveIP();

        init();

        sellerloadItems();
    }

    private void sellerloadItems() {

        itemList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/sellerItemList.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                try {
                    JSONObject obj = new JSONObject(response);

                    JSONArray array = obj.getJSONArray("data");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject data = array.getJSONObject(i);

                        int id = data.getInt("id");
                        String item_name = data.getString("sitem_name");
                        String item_desc = data.getString("sitem_desc");
                        String rate = data.getString("sitem_rate");
                        String image = data.getString("sitem_image");
                        String offer = data.getString("sitem_offer");

                        Log.d("item", "onResponse: "+id+item_name+item_desc+rate+""+image);

                        itemList.add(new SellerItemListModelClass(id,item_name,item_desc,rate,offer,scat,image));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listRecyclerAdapter = new SellerItemListRecyclerAdapter(itemList,SellerItemListActivity.this);
                sellerItemListRV.setAdapter(listRecyclerAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerItemListActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("scatid",scatId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void init() {
        sellerItemListRV = (RecyclerView) findViewById(R.id.sellerItemListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sellerItemListRV.setLayoutManager(mLayoutManager);
        sellerItemListRV.setItemAnimator(new DefaultItemAnimator());
    }
}
