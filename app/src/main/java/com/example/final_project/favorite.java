package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.final_project.R.layout.activity_favorite;

public class favorite extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public TextView favoriteName;
    public ArrayList<String> list;
    public ListView listView;
    public ArrayAdapter<Integer> arrayAdapter;
    public static String userName;
    public static String itemValue;
    public int position;
    public Button removeButton;
    public Button viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_favorite);


        favoriteName = findViewById(R.id.favoriteName);
        listView = (ListView) findViewById(R.id.listViewFavorites);
        removeButton = findViewById(R.id.deleteButton);
        viewButton = findViewById(R.id.viewButton);
        favoriteName.setText(MainActivity.userName + "'s Favorites");
        list = new ArrayList<>();


        db.collection(MainActivity.userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("1", document.getId() + " => " + document.getData());

                                list.add(document.getId());


                            }
                            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(favorite.this, android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(arrayAdapter);


                        } else {
                            Log.d("2", "Error getting documents: ", task.getException());
                        }


                    }

                });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> list, View v, int pos, long id) {

                        itemValue = (String) listView.getItemAtPosition(pos);
                        System.out.println(itemValue);
                        position = pos;
                        db.collection(MainActivity.userID).document(favorite.itemValue)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("1", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("2", "Error deleting document", e);
                                    }
                                });
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });





                System.out.println(list);

            }


        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                        itemValue = (String) listView.getItemAtPosition(pos);
                        Intent intent = new Intent(getApplicationContext(), results.class);
                        startActivity(intent);

                    }
                });

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
                        Intent results = new Intent(getApplicationContext(), results.class);
                        startActivity(results);
                        break;
                    case R.id.favorite:
                        break;
                }
                return true;
            }
        });
    }
}