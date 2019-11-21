package com.aumento.shopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aumento.shopping.Fragment.HomeFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aumento.shopping.utils.GlobalPreference;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtViewCount;
    private int count=0;
    String ip;
    String uid;
    GlobalPreference globalPreference;
    private View hView;
    private CircleImageView headerUsernameIV;
    private String image;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Grocery Connection");


        globalPreference = new GlobalPreference(this);
        ip= globalPreference.RetriveIP();
        uid = globalPreference.getUserId();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hView =  navigationView.getHeaderView(0);

        if(!uid.equals(""))
        {
            getUserDetails(hView);
        }

        ImageView headerSettingImageView = hView.findViewById(R.id.headerProfileSettings);
        headerSettingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = null;

        fragment = new HomeFragment();

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

//        cartlist();
    }

    private void getUserDetails(final View hView) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/getAddress.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("data");
                    JSONObject data = array.getJSONObject(0);

                    String name = data.getString("cust_name");
                    String phn = data.getString("phn");
                    String email = data.getString("email");
                    image = data.getString("cust_image");



                    Log.d("Image", "http://"+ip+"/Aumento/Shopping/admin/tbl_customer/uploads/"+image);

                    if(!image.equals("")) {

                        Log.d("Image", "onResponse: "+image);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                CircleImageView userIV = hView.findViewById(R.id.headerImageView);
                                Glide.with(userIV.getContext())
                                        .load("http://" + ip + "/Aumento/Shopping/admin/tbl_customer/uploads/" + image)
                                        .into(userIV);

                            }
                        }, 2000);

                    }

                    TextView headerUsernameTV = hView.findViewById(R.id.headerUsernameTextView);
                    headerUsernameTV.setText(name);

                    TextView headerEmailTV = hView.findViewById(R.id.headerEmailTextView);
                    headerEmailTV.setText(email);


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

    @Override
    public void onBackPressed() {



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HomeActivity.super.onBackPressed();
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }

    @Override
    protected void onResume() {
//        count = 10;
//        updateHotCount(count);
        super.onResume();
        if(!uid.equals(""))
            cartlist();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        updateHotCount(count);
    }

    private void cartlist() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip+"/Aumento/Shopping/itemcount.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                count = Integer.parseInt(response.trim());
                updateHotCount(count);
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
    public void onTopResumedActivityChanged(boolean isTopResumedActivity) {
        super.onTopResumedActivityChanged(isTopResumedActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        final View notificaitons = menu.findItem(R.id.actionCart).getActionView();

        txtViewCount = (TextView) notificaitons.findViewById(R.id.txtCount);

        notificaitons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

    public void updateHotCount(final int new_hot_number) {
        count = new_hot_number;
        if (count < 0) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (count == 0)
                    txtViewCount.setVisibility(View.GONE);
                else {
                    txtViewCount.setVisibility(View.VISIBLE);
                    txtViewCount.setText(Integer.toString(count));
                    // supportInvalidateOptionsMenu();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionCart) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //creating fragment object
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragment = new HomeFragment();

        } else if (id == R.id.nav_cart) {

            intent = new Intent(HomeActivity.this,CartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_order) {

            intent = new Intent(HomeActivity.this,MyOrderActivity.class);
            startActivity(intent);

        } /*else if (id == R.id.seller_hub) {

            intent = new Intent(HomeActivity.this,SellerCatagoryActivity.class);
            startActivity(intent);

        } */else if (id == R.id.nav_category) {

            intent = new Intent(HomeActivity.this, CatagoriesActivity.class);
            startActivity(intent);

        }/* else if (id == R.id.nav_offers) {

        }*/ else if (id == R.id.nav_profile) {

            intent = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);

        } /*else if (id == R.id.nav_help) {

        }*/ else if (id == R.id.nav_logout) {
            globalPreference.setLoginStatus(false);
            globalPreference.setUserId("");
            intent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
