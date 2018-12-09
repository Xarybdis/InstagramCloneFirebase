package com.example.emrullah.instagramclonefirebase;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    //DEFINITIONS
    ImageView postImage;
    EditText postComment;
    Uri selectedImage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseMyReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        postComment = findViewById(R.id.uploadActivity_commentText);
        postImage = findViewById(R.id.uploadActivity_imageView);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseMyReference= firebaseDatabase.getReference();
        mAuth= FirebaseAuth.getInstance();

    }

    public void upload(View view){

        /*With this method we are uploading the image from storage to firebase storage.
          We get the image from storage then we name it with unique id.
          And we use firebase methods to implement these.
          if it
         */
        UUID uuid = UUID.randomUUID();//This code UUID class gives us unique id for each time we use.
        final String imageName="images"+uuid+".jpg";

        StorageReference imageStorageRef = mStorageReference.child(imageName);
        imageStorageRef.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference newReference=FirebaseStorage.getInstance().getReference(imageName);
                newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadURL = uri.toString();
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userEmail= user.getEmail();
                        String userComment = postComment.getText().toString();

                        UUID uuid1 = UUID.randomUUID();
                        String uuidString= uuid1.toString();

                        databaseMyReference.child("POSTS").child(uuidString).child("userMail").setValue(userEmail);
                        databaseMyReference.child("POSTS").child(uuidString).child("userComment").setValue(userComment);
                        databaseMyReference.child("POSTS").child(uuidString).child("downloadURL").setValue(downloadURL);

                        Toast.makeText(getApplicationContext(),"Post shared",Toast.LENGTH_LONG).show();

                        Intent intentToFeed= new Intent(getApplicationContext(),FeedActivity.class);
                        startActivity(intentToFeed);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });






    }


    public void selectImage(View view){

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,2);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==2 && resultCode==RESULT_OK && data !=null){
            selectedImage =data.getData();

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                postImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
