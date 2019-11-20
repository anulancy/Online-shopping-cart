package com.aumento.shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.aumento.shopping.Adapter.ReviewsRecycleAdapter;
import com.aumento.shopping.Adapter.SlidingImage_Adapter;
import com.aumento.shopping.ModelClass.ReviewModelClass;
import com.aumento.shopping.utils.GlobalPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class SellerProductDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView title;
    private TextView itemNameTV,offerpriceTV,priceTV,offerPercentTV,itemDescTV;
    private TextView number;
    private ImageView add,minus;
    private ImageView favImag,backImageButton;
    private ViewPager viewPager;
    private Button addCartButton,goToCartButton;
    GlobalPreference globalPreference;

    private static final String TAG = "ProductDetailActivity";

    /*recycler data is here*/
    private ArrayList<ReviewModelClass> reviewModelClasses;
    private RecyclerView recyclerview;
    private ReviewsRecycleAdapter mAdapter2;

    private String title1[] = {"Mark Henry","Jemes Bond","Steve Hong"};

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    int count=1;
    int ItemId;
    Float itemPrice;
    String ip;
    boolean favSelect = false;
    String uid;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_detail);

        init();

        globalPreference = new GlobalPreference(getApplicationContext());
        ip = globalPreference.RetriveIP();
        uid = globalPreference.getUserId();

        //Receive Item ID
        Intent intent = getIntent();
        ItemId = intent.getIntExtra("itemId",1);

        //      Product Detail  Recyclerview Code is here
        loadItemDeatils();

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        reviewModelClasses = new ArrayList<>();

        for (int i = 0; i < title1.length; i++) {
            ReviewModelClass beanClassForRecyclerView_contacts = new ReviewModelClass(title1[i]);

            reviewModelClasses.add(beanClassForRecyclerView_contacts);
        }

        mAdapter2 = new ReviewsRecycleAdapter(SellerProductDetailActivity.this,reviewModelClasses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SellerProductDetailActivity.this);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter2);

    }


    //Item Details Load
    private void loadItemDeatils() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/sellerItemDetails.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "onResponse: "+response);

                try {
                    //converting the string to json array object
                    JSONObject obj=new JSONObject(response);

                    //converting the string to json array object
                    JSONArray array = obj.getJSONArray("data");

                    //getting product object from json array
                    JSONObject data = array.getJSONObject(0);

                    //adding to list
                    int id = data.getInt("id");
                    String image =  data.getString("sitem_image");
                    String item_name = data.getString("sitem_name");
                    String item_desc = data.getString("sitem_desc");
                    String item_price = data.getString("sitem_rate");
                    String item_offer = data.getString("sitem_offer");

                    ImagesArray.add(image);
                    itemNameTV.setText(item_name);
                    itemDescTV.setText(item_desc);

//                    priceTV.setText("₹ "+item_price);
                    if(item_offer.equals(""))
                    {
                        itemPrice = Float.parseFloat(item_price);
                        offerpriceTV.setText("₹ "+item_price);
                        priceTV.setVisibility(View.GONE);
                        offerPercentTV.setVisibility(View.GONE);
                    }
                    else {
                        Float price,offer,offerprice;
                        price = Float.parseFloat(item_price);
                        offer = Float.parseFloat(item_offer);
                        offerprice = price - ((price * offer)/100);
                        itemPrice = offerprice;
                        offerpriceTV.setText("₹ "+String.valueOf(offerprice));
                        priceTV.setText("₹ "+item_price);
                        offerPercentTV.setText("-"+item_offer+"%");
                        priceTV.setPaintFlags(priceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LoadImage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerProductDetailActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("ItemId",String.valueOf(ItemId));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    //Item Image
    private void LoadImage() {
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager.setAdapter(new SlidingImage_Adapter(SellerProductDetailActivity.this,ImagesArray));
        indicator.setViewPager(viewPager);
    }

    //On view Click Function
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //Add Quantity button
            case R.id.itemplus:

                count++;
                number.setText(String.valueOf(count));

                break;

            //minus Quantity button
            case R.id.itemminus:

                if(count > 1 )
                    count--;
                number.setText(String.valueOf(count));

                break;

            //Add Favourite button
            case R.id.imgFav:

                if (favSelect == false){
                    favSelect=true;

                    favImag.setImageResource(R.drawable.ic_dark_like);

                }else {
                    favSelect= false;
                    favImag.setImageResource(R.drawable.ic_heart_light);
                }

                break;

            //Add cart button
            case  R.id.itemCartAddBT:

                if(uid.equals(""))
                {
                    Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
                    intent = new Intent(SellerProductDetailActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    addCart();
                }
                break;

            case R.id.BackImageButton:

                finish();

                break;

            case R.id.goToCartBT:

                intent = new Intent(SellerProductDetailActivity.this,CartActivity.class);
                startActivity(intent);

                break;

        }
    }

    //Adding item to cart
    private void addCart() {

        StringRequest addToCartRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/addCart.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+response);
                goToCartButton.setVisibility(View.VISIBLE);
                Toast.makeText(SellerProductDetailActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
//                Toast.makeText(ProductDetailActivity.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellerProductDetailActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid",uid);
                params.put("itemID",String.valueOf(ItemId));
                params.put("Qty", String.valueOf(count));
                params.put("price",String.valueOf(itemPrice));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(addToCartRequest);
    }

    private void init() {
        //Header
        title = findViewById(R.id.title);
        title.setText("Product Detail");

        //item Details
        itemNameTV = findViewById(R.id.itemNameTextView);
        offerpriceTV = findViewById(R.id.offerPriceTextView);
        priceTV = findViewById(R.id.priceTextView);
        offerPercentTV = findViewById(R.id.offerPercentTextView);
        itemDescTV = findViewById(R.id.itemDescTextView);

        //image Slider
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //item count
        number = findViewById(R.id.number);
        add = findViewById(R.id.itemplus);
        add.setOnClickListener(this);
        minus = findViewById(R.id.itemminus);
        minus.setOnClickListener(this);

        //favourite icon
        favImag = (ImageView) findViewById(R.id.imgFav);
        favImag.setOnClickListener(this);

        addCartButton = findViewById(R.id.itemCartAddBT);
        addCartButton.setOnClickListener(this);

        backImageButton = findViewById(R.id.BackImageButton);
        backImageButton.setOnClickListener(this);

        goToCartButton = findViewById(R.id.goToCartBT);
        goToCartButton.setOnClickListener(this);
    }
}
