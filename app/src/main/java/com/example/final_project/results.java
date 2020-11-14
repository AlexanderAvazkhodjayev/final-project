package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        final RequestQueue queue = Volley.newRequestQueue(this);


        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.yelp.com/v3/businesses/search?term=Bagels&location=33496",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONObject item = json.getJSONArray("businesses").getJSONObject(0);
                            System.out.println(json);

                            item.getString("name");
                            item.getString("");

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


        queue.add(stringRequest);









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
}