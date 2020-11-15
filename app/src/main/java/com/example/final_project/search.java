package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class search extends AppCompatActivity {
    private Button searchButton;
    private Button gpsButton;
    private EditText businessText;
    private EditText locationText;
    private String actualLocationData;
    private String actualBusinessData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        searchButton = findViewById(R.id.searchButton);
        gpsButton = findViewById(R.id.gpsButton);
        businessText = findViewById(R.id.businessText);
        locationText = findViewById(R.id.locationText);


        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), results.class);
                if(!(TextUtils.isEmpty(businessText.getText()))){
                    String businessData = businessText.getText().toString();
                    actualBusinessData = String.valueOf(businessData);

                }


                if(TextUtils.isEmpty(locationText.getText())){
                    locationText.setHint("Please Enter location!");

                }else {
                    String locationData = locationText.getText().toString();
                    actualLocationData = String.valueOf(locationData);
                    intent.putExtra("business_data", actualBusinessData);
                    intent.putExtra("location_data", actualLocationData);
                    startActivity(intent);

                }



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
                        break;
                    case R.id.result:
                        Intent results = new Intent(getApplicationContext(), results.class);
                        startActivity(results);
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