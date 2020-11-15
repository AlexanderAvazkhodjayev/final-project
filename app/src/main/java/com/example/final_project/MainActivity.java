package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.DataOutputStream;

public class MainActivity extends AppCompatActivity {
    private SignInButton signInButton;
    private GoogleSignInClient mGOogleSignInClient;
    private String TAG = "MainActivity";
    private Button btnSignOut;
    private int RC_SIGN_IN = 1;
    private TextView accountName;
    private TextView accountEmail;
    private TextView accountID;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signInButton = findViewById(R.id.sign_in_button);
        btnSignOut = findViewById(R.id.sign_out);
        accountName = findViewById(R.id.accName);
        accountEmail = findViewById(R.id.accEmail);
        accountID= findViewById(R.id.accId);
        imageView = (ImageView) findViewById(R.id.account_photo);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGOogleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signIn();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mGOogleSignInClient.signOut();
                btnSignOut.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.INVISIBLE);
                accountEmail.setVisibility(View.INVISIBLE);
                accountID.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.search:
                        Intent search = new Intent(getApplicationContext(), search.class);
                        startActivity(search);
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
    private void signIn(){
        Intent signInIntent = mGOogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{

            GoogleSignInAccount acc = task.getResult(ApiException.class);
            accountName.setText(acc.getDisplayName());
            accountEmail.setText(acc.getEmail());
            accountID.setText(acc.getId());


            Glide.with(this).load(acc.getPhotoUrl()).into(imageView);

            signInButton.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);
            accountName.setVisibility(View.VISIBLE);
            accountEmail.setVisibility(View.VISIBLE);
            accountID.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);

        }catch(ApiException e){

        }
    }
}