package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int REQUEST_CODE_PERMISSION = 2;
    GPSTracker gps;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        favorite.itemValue = "";
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

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GPSTracker(search.this);
                Intent intent = new Intent(getApplicationContext(), results.class);
                if(gps.canGetLocation()){
                    String actual_lat= "";
                    String actual_long= "";
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    actual_lat = String.valueOf(latitude);
                    actual_long = String.valueOf(longitude);

                    intent.putExtra("lat", actual_lat);
                    intent.putExtra("long", actual_long);
                    startActivity(intent);



                }else{
                    gps.showSettingsAlert();
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
                        if(TextUtils.isEmpty(locationText.getText())){
                            locationText.setHint("Please Enter location!");
                        }else {
                            Intent results = new Intent(getApplicationContext(), results.class);
                            startActivity(results);
                            break;
                        }
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