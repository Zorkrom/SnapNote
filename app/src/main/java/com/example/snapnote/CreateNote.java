package com.example.snapnote;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {
    FirebaseFirestore database;
    EditText createNoteTitle,createNoteDesc;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database=FirebaseFirestore.getInstance();
        createNoteTitle=findViewById(R.id.createNoteTitle);
        createNoteDesc=findViewById(R.id.createNoteDesc);
        progressBar=findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sTitle=createNoteTitle.getText().toString();
                String sDescription=createNoteDesc.getText().toString();
                if (sDescription.isEmpty()){
                    Toast.makeText(CreateNote.this, "No se puede guardar sin una descripción", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sTitle.isEmpty()){
                    sTitle="(Sin título)";
                }
                progressBar.setVisibility(View.VISIBLE);
                DocumentReference documRef= database.collection("notes").document();
                Map<String,Object> note=new HashMap<>();
                note.put("title",sTitle);
                note.put("description",sDescription);
                documRef.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateNote.this, "Añadido correctamente", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateNote.this, "No se ha podido añadir", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
