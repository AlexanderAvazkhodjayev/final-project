package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class review extends AppCompatActivity {
    private String link = "";
    public static String businessReviewID;
    public ImageView imageView1;
    public TextView userID1;
    public TextView rating1;
    public TextView comment1;
    public TextView createdAt1;

    public ImageView imageView2;
    public TextView userID2;
    public TextView rating2;
    public TextView comment2;
    public TextView createdAt2;

    public ImageView imageView3;
    public TextView userID3;
    public TextView rating3;
    public TextView comment3;
    public TextView createdAt3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        businessReviewID = results.businessID;
        // Business Review Number 1
        imageView1 = findViewById(R.id.imageView1);
        userID1 = findViewById(R.id.userID1);
        rating1 = findViewById(R.id.rating1);
        comment1 = findViewById(R.id.comment1);
        createdAt1 = findViewById(R.id.createdAt1);

        // Business Review Number 2
        imageView2 = findViewById(R.id.imageView2);
        userID2 = findViewById(R.id.userID2);
        rating2 = findViewById(R.id.rating2);
        comment2 = findViewById(R.id.comment2);
        createdAt2 = findViewById(R.id.createdAt2);

        // Business Review Number 3
        imageView3 = findViewById(R.id.imageView3);
        userID3 = findViewById(R.id.userID3);
        rating3 = findViewById(R.id.rating3);
        comment3 = findViewById(R.id.comment3);
        createdAt3 = findViewById(R.id.createdAt3);


        link = "https://api.yelp.com/v3/businesses/"+ businessReviewID +"/reviews";

        final RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                new Response.Listener < String > () {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONObject item1 = json.getJSONArray("reviews").getJSONObject(0);
                            JSONObject item2 = json.getJSONArray("reviews").getJSONObject(1);
                            JSONObject item3 = json.getJSONArray("reviews").getJSONObject(2);

                            Glide.with(review.this).load(item1.getJSONObject("user").getString("image_url")).into(imageView1);
                            userID1.setText(item1.getJSONObject("user").getString("name"));
                            rating1.setText(item1.getString("rating") + "/5");
                            comment1.setText(item1.getString("text"));
                            createdAt1.setText(item1.getString("time_created"));

                            Glide.with(review.this).load(item2.getJSONObject("user").getString("image_url")).into(imageView2);
                            userID2.setText(item2.getJSONObject("user").getString("name"));
                            rating2.setText(item2.getString("rating") + "/5");
                            comment2.setText(item2.getString("text"));
                            createdAt2.setText(item2.getString("time_created"));

                            Glide.with(review.this).load(item3.getJSONObject("user").getString("image_url")).into(imageView3);
                            userID3.setText(item3.getJSONObject("user").getString("name"));
                            rating3.setText(item3.getString("rating") + "/5");
                            comment3.setText(item3.getString("text"));
                            createdAt3.setText(item3.getString("time_created"));


                        } catch (Exception e) {
                            System.out.println("JSON request didn't work");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("JSON request didn't work");
            }
        }) {
            // Bearer token is needed in order to authenticate the indiviudal
            @Override
            public Map< String, String > getHeaders() throws AuthFailureError {
                Map < String, String > params = new HashMap< >();
                params.put("Authorization", "bearer " + "uP0H4GmZuvjVUPuNoxeh5EFT6dZvZxgYSF03GmRoIHicNDciEfZwVLPPm6retR2_Co1B5rGIIocujGfB6JMtkrg86KGARcng2oIx4yNbXEFwVhoWDJO07ND0ssSuX3Yx");
                return params;
            }
        };
        queue.add(stringRequest);
    }
}