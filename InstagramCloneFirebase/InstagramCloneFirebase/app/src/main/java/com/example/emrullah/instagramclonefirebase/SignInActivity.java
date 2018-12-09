package com.example.emrullah.instagramclonefirebase;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText emailText;
    EditText passwordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth= FirebaseAuth.getInstance();
        emailText= findViewById(R.id.signInActivity_emailText);
        passwordText=findViewById(R.id.signInActivity_passwordlText);

        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null){
            Intent intentToFeedActivity= new Intent(getApplicationContext(),FeedActivity.class);
            startActivity(intentToFeedActivity);

        }

    }

    public  void createAccount(View view){
        mAuth.createUserWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("tag","createUserWithEmail: Successful");
                            Toast.makeText(SignInActivity.this, "User Account creted!", Toast.LENGTH_SHORT).show();
                            }else{
                            Log.w("Warn","createUserEMail:Failure",task.getException());
                            Toast.makeText(SignInActivity.this,
                                    task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            }
                    }
                });

    }
    public void signIn(View view){
        mAuth.signInWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d("Tag Of Success","signInUserWithEmail : success");
                            Toast.makeText(SignInActivity.this, "You signed in broski!", Toast.LENGTH_SHORT).show();
                            Intent intentToFeedActivity= new Intent(getApplicationContext(),FeedActivity.class);
                            startActivity(intentToFeedActivity);
                        }else {
                            Log.w("Tag of failure", "signInWithEmail : failure",task.getException());
                            Toast.makeText(SignInActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}
