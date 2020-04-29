package com.example.snapnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
    FirebaseAuth login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        login=FirebaseAuth.getInstance();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 if(login.getCurrentUser()!=null){
                     startActivity(new Intent(getApplicationContext(),MainActivity.class));
                 }else{
                     login.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                         @Override
                         public void onSuccess(AuthResult authResult) {
                             Toast.makeText(Splash.this, "Ha iniciado sesión temporalmente.", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(getApplicationContext(),MainActivity.class));
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(Splash.this, "Ha habido algún error.", Toast.LENGTH_SHORT).show();
                         }
                     });
                 }
            }
        },2000);
    }
}
