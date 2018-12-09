package com.example.emrullah.instagramclonefirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedActivity extends AppCompatActivity {


    PostClass adapter;
    ListView listView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;

    //MENU STUFF STARTS HERE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_post_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPost: {
                Intent intentToUploadActivit = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intentToUploadActivit);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //ONCREATE STUFF STARTS HERE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);



        listView = findViewById(R.id.listView_feed);
        userEmailFromFB = new ArrayList<>();
        userCommentFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        adapter=new PostClass(userEmailFromFB,userCommentFromFB,userImageFromFB,this);
        listView.setAdapter(adapter);

        getDataFromFB();
    }

    public void getDataFromFB() {

        DatabaseReference newReference = firebaseDatabase.getReference("POSTS");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    HashMap<String,String> hashMap= (HashMap<String, String>) ds.getValue();
                    System.out.println("FBV usermail:"+hashMap.get("userMail"));
                    userEmailFromFB.add(hashMap.get("userMail"));
                    userCommentFromFB.add(hashMap.get("userComment"));
                    userImageFromFB.add(hashMap.get("downloadURL"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}
