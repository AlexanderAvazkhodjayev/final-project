package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.example.final_project.R.id.home;

public class MainActivity extends AppCompatActivity {
    // Initialization of all variables
    private SignInButton signInButton;
    private GoogleSignInClient mGOogleSignInClient;
    private String TAG = "MainActivity";
    private Button btnSignOut;
    private int RC_SIGN_IN = 1;
    private TextView accountName;
    private TextView accountEmail;
    private TextView accountID;
    private ImageView imageView;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;
    public static String userID;
    private TextView textView9;
    private ImageView imageView3;
    public static String userName;
    private BottomNavigationView bottomNavigationView;
    public static yelpData actualYelpData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set variables equal to the different buttons and textviews
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        signInButton = findViewById(R.id.sign_in_button);
        btnSignOut = findViewById(R.id.sign_out);
        accountName = findViewById(R.id.accName);
        accountEmail = findViewById(R.id.accEmail);
        accountID = findViewById(R.id.accId);
        imageView = (ImageView) findViewById(R.id.account_photo);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textView9 = findViewById(R.id.textView9);
        imageView3 = findViewById(R.id.imageView3);

        // Shared Preferences is an API that stores key-value pairs, it allows for read and write capabilities. The file can be private or shared.
        // Each Shared Preference file is managed by the framework.
        SharedPreferences loadData = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);

        // Sets the status to true after logging in
        boolean checkStatus = loadData.getBoolean("LOGIN", false);
        System.out.println(checkStatus);

        // If signed in show the buttons and textviews below
        if (checkStatus) {
            signInButton.setVisibility(View.INVISIBLE);
            textView9.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);
            accountName.setVisibility(View.VISIBLE);
            accountEmail.setVisibility(View.VISIBLE);
            accountID.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textView6.setVisibility(View.VISIBLE);
            textView7.setVisibility(View.VISIBLE);
            textView8.setVisibility(View.VISIBLE);
            accountName.setText(loadData.getString("NAME", ""));
            accountEmail.setText(loadData.getString("EMAIL", ""));
            accountID.setText(loadData.getString("ACC_ID", ""));
            userID = loadData.getString("EMAIL", "");
            userName = loadData.getString("NAME", "");
            Glide.with(this).load(loadData.getString("PHOTO_URL", "")).into(imageView);
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            // Else keep them hidden and show sign in button
            textView9.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.INVISIBLE);
            accountName.setVisibility(View.INVISIBLE);
            accountEmail.setVisibility(View.INVISIBLE);
            accountID.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            textView6.setVisibility(View.INVISIBLE);
            textView7.setVisibility(View.INVISIBLE);
            textView8.setVisibility(View.INVISIBLE);
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }

        // Standard google sign-in code (Tutorial)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGOogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Calls signIn function if the sign in button is clicked
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // If user presses button signout, then hide the buttons below
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGOogleSignInClient.signOut();
                btnSignOut.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                textView9.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.VISIBLE);
                accountName.setVisibility(View.INVISIBLE);
                accountEmail.setVisibility(View.INVISIBLE);
                accountID.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                textView6.setVisibility(View.INVISIBLE);
                textView7.setVisibility(View.INVISIBLE);
                textView8.setVisibility(View.INVISIBLE);

            }
        });

        // Bottom navigation setup
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case home:
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
    private void signIn() {
        Intent signInIntent = mGOogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task < GoogleSignInAccount > task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    // Handles the sign in request, saves all useful information to shared preferences as shown below
    private void handleSignInResult(Task < GoogleSignInAccount > task) {
        try {

            GoogleSignInAccount acc = task.getResult(ApiException.class);
            accountName.setText(acc.getDisplayName());
            accountEmail.setText(acc.getEmail());
            accountID.setText(acc.getId());

            // Processes the image and is able to display it to an image view
            Glide.with(this).load(acc.getPhotoUrl()).into(imageView);

            // Shared Preferences needed to store data since its scoped
            // This data is then retrieved when the user signs in above
            SharedPreferences saveData = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = saveData.edit();
            editor.putString("NAME", acc.getDisplayName());
            editor.putBoolean("LOGIN", true);
            editor.putString("EMAIL", acc.getEmail());
            editor.putString("PHOTO_URL", String.valueOf(acc.getPhotoUrl()));
            editor.putString("ACC_ID", acc.getId());

            // Unhide the buttons and images when signing in
            editor.apply();
            bottomNavigationView.setVisibility(View.VISIBLE);
            textView9.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);
            accountName.setVisibility(View.VISIBLE);
            accountEmail.setVisibility(View.VISIBLE);
            accountID.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            textView6.setVisibility(View.VISIBLE);
            textView7.setVisibility(View.VISIBLE);
            textView8.setVisibility(View.VISIBLE);

        } catch (ApiException e) {

        }
    }
}