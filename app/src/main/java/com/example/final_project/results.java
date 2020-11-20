package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class results extends AppCompatActivity implements OnMapReadyCallback {
    private String businessText;
    private String locationText;
    private GoogleMap mMap;

    private TextView companyName;
    private TextView rating;
    private TextView price;
    private TextView location;
    private TextView phoneNumber;
    private TextView openClosed;
    private Button backButton;
    private Button nextButton;
    private int index = 0;
    private String link = "";
    public static yelpData actualYelpData;
    private Button favoriteButton;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user = new HashMap<>();
    Map<String, Object> nestedData = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activityMap);
        mapFragment.getMapAsync(this);




        companyName = findViewById(R.id.companyName);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.price);
        location = findViewById(R.id.location);
        phoneNumber = findViewById(R.id.phoneNumber);
        openClosed = findViewById(R.id.openClosed);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        favoriteButton = findViewById(R.id.favoriteButton);


        Intent intent = getIntent();
        businessText = intent.getStringExtra("business_data");
        locationText = intent.getStringExtra("location_data");
        String lat_coord = intent.getStringExtra("lat");
        String long_coord = intent.getStringExtra("long");


        favoriteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                if (actualYelpData != null) {


                    user.put("companyName", actualYelpData.name);
                    user.put("rating", actualYelpData.rating);
                    user.put("price", actualYelpData.price);
                    user.put("location", actualYelpData.location);
                    user.put("phoneNumber", actualYelpData.phoneNumber);
                    user.put("openClosed", actualYelpData.openClosed);
                    System.out.println(actualYelpData.openClosed);


                }

                    db.collection(MainActivity.userID).document(actualYelpData.name)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("1", "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("2", "Error writing document", e);
                                }
                            });

            }
        });


        if (!TextUtils.isEmpty(lat_coord)) {
            link="https://api.openweathermap.org/data/2.5/weather?lat=" + lat_coord + "&lon=" + long_coord + "&appid=2397d4abcaf90749690c871029817c98";
        }


        if(!TextUtils.isEmpty(businessText)){
            link = "https://api.yelp.com/v3/businesses/search?term="+ businessText +"&location="+ locationText +"";
        } else {
            link = "https://api.yelp.com/v3/businesses/search?location="+locationText+ "";

        }


        final RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            actualYelpData = new yelpData();
                            JSONObject json = new JSONObject(response);
                            JSONObject item = json.getJSONArray("businesses").getJSONObject(0);
                            JSONArray arr = json.getJSONArray("businesses");
                            if(arr.length() == 0){
                                return;
                            }
                            companyName.setText(item.getString("name"));
                            rating.setText(item.getString("rating"));
                            price.setText(item.getString("price"));
                            location.setText(item.getJSONObject("location").getJSONArray("display_address").getString(0));
                            phoneNumber.setText(item.getString("display_phone"));
                            if(item.getBoolean("is_closed") == false){
                                openClosed.setText("Closed");
                                actualYelpData.openClosed = "Closed";
                            } else {
                                openClosed.setText("Open");
                                actualYelpData.openClosed = "Open";
                            }

                            actualYelpData.name = String.valueOf(item.getString("name"));
                            actualYelpData.rating = String.valueOf(item.getString("rating"));
                            actualYelpData.price = String.valueOf(item.getString("price"));
                            actualYelpData.location = String.valueOf(item.getJSONObject("location").getJSONArray("display_address").getString(0));
                            actualYelpData.phoneNumber = String.valueOf(item.getString("display_phone"));
                            actualYelpData.Lat = String.valueOf(item.getJSONObject("coordinates").getString("latitude"));
                            actualYelpData.Long = String.valueOf(item.getJSONObject("coordinates").getString("longitude"));

                            onMapReady(mMap);

                        }catch(Exception e){
                            System.out.println("JSON request didn't work");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("JSON request didn't work");
            }
    }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","bearer " + "uP0H4GmZuvjVUPuNoxeh5EFT6dZvZxgYSF03GmRoIHicNDciEfZwVLPPm6retR2_Co1B5rGIIocujGfB6JMtkrg86KGARcng2oIx4yNbXEFwVhoWDJO07ND0ssSuX3Yx");
                return params;
            }
        };
        queue.add(stringRequest);


        final RequestQueue queue1 = Volley.newRequestQueue(this);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                index += 1;
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (index > 0){
                                        backButton.setVisibility(View.VISIBLE);
                                    }
                                    actualYelpData = new yelpData();
                                    JSONObject json = new JSONObject(response);
                                    JSONObject item = json.getJSONArray("businesses").getJSONObject(index);
                                    companyName.setText(item.getString("name"));
                                    rating.setText(item.getString("rating"));
                                    price.setText(item.getString("price"));
                                    location.setText(item.getJSONObject("location").getJSONArray("display_address").getString(0));
                                    phoneNumber.setText(item.getString("display_phone"));
                                    if(item.getBoolean("is_closed") == false){
                                        openClosed.setText("Closed");
                                        actualYelpData.openClosed = "Closed";
                                    } else {
                                        openClosed.setText("Open");
                                        actualYelpData.openClosed = "Open";
                                    }
                                    actualYelpData.name = String.valueOf(item.getString("name"));
                                    actualYelpData.rating = String.valueOf(item.getString("rating"));
                                    actualYelpData.price = String.valueOf(item.getString("price"));
                                    actualYelpData.location = String.valueOf(item.getJSONObject("location").getJSONArray("display_address").getString(0));
                                    actualYelpData.phoneNumber = String.valueOf(item.getString("display_phone"));
                                    actualYelpData.Lat = String.valueOf(item.getJSONObject("coordinates").getString("latitude"));
                                    actualYelpData.Long = String.valueOf(item.getJSONObject("coordinates").getString("longitude"));
                                    onMapReady(mMap);

                                }catch(Exception e){
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("JSON request didn't work");
                    }
                }){
                    @Override
                    public Map<String,String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("Authorization","bearer " + "uP0H4GmZuvjVUPuNoxeh5EFT6dZvZxgYSF03GmRoIHicNDciEfZwVLPPm6retR2_Co1B5rGIIocujGfB6JMtkrg86KGARcng2oIx4yNbXEFwVhoWDJO07ND0ssSuX3Yx");
                        return params;
                    }
                };
                queue1.add(stringRequest);
            }
        });


        final RequestQueue queue2 = Volley.newRequestQueue(this);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                index -= 1;
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(index == 0){
                                        backButton.setVisibility(View.INVISIBLE);
                                    }
                                    actualYelpData = new yelpData();
                                    JSONObject json = new JSONObject(response);
                                    JSONObject item = json.getJSONArray("businesses").getJSONObject(index);
                                    companyName.setText(item.getString("name"));
                                    rating.setText(item.getString("rating"));
                                    price.setText(item.getString("price"));
                                    location.setText(item.getJSONObject("location").getJSONArray("display_address").getString(0));
                                    phoneNumber.setText(item.getString("display_phone"));
                                    if(item.getBoolean("is_closed") == false){
                                        openClosed.setText("Closed");
                                        actualYelpData.openClosed = "Closed";
                                    } else {
                                        openClosed.setText("Open");
                                        actualYelpData.openClosed = "Open";
                                    }
                                    actualYelpData.name = String.valueOf(item.getString("name"));
                                    actualYelpData.rating = String.valueOf(item.getString("rating"));
                                    actualYelpData.price = String.valueOf(item.getString("price"));
                                    actualYelpData.location = String.valueOf(item.getJSONObject("location").getJSONArray("display_address").getString(0));
                                    actualYelpData.phoneNumber = String.valueOf(item.getString("display_phone"));
                                    actualYelpData.Lat = String.valueOf(item.getJSONObject("coordinates").getString("latitude"));
                                    actualYelpData.Long = String.valueOf(item.getJSONObject("coordinates").getString("longitude"));
                                    onMapReady(mMap);
                                }catch(Exception e){
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("JSON request didn't work");
                    }
                }){
                    @Override
                    public Map<String,String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("Authorization","bearer " + "uP0H4GmZuvjVUPuNoxeh5EFT6dZvZxgYSF03GmRoIHicNDciEfZwVLPPm6retR2_Co1B5rGIIocujGfB6JMtkrg86KGARcng2oIx4yNbXEFwVhoWDJO07ND0ssSuX3Yx");
                        return params;
                    }
                };
                queue2.add(stringRequest);
            }
        });



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent home = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.search:
                        Intent search = new Intent(getApplicationContext(), search.class);
                        startActivity(search);
                        break;
                    case R.id.result:
                        break;
                    case R.id.favorite:
                        Intent favorite = new Intent(getApplicationContext(), favorite.class);
                        startActivity(favorite);
                        break;
                }
                return true;
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double lat_value = 0;
        double long_value = 0;
        String nameOfBusiness= "";

        if(actualYelpData != null){
            lat_value = Double.parseDouble(actualYelpData.Lat);
            long_value = Double.parseDouble(actualYelpData.Long);
            nameOfBusiness = actualYelpData.name;

        } else{
             lat_value = 0;
             long_value = 0;
        }
        LatLng TutorialsPoint = new LatLng(lat_value, long_value);
        mMap.addMarker(new MarkerOptions().position(TutorialsPoint).title(nameOfBusiness));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(TutorialsPoint,14));

    }
}