package com.example.snapnote.notePackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.snapnote.MainActivity;
import com.example.snapnote.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {
    Intent notesData;
    EditText editNoteTitle;
    EditText editNoteDescription;
    FirebaseFirestore database;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database=database.getInstance();
        notesData=getIntent();
        editNoteTitle=findViewById(R.id.editNoteTitle);
        editNoteDescription=findViewById(R.id.editNoteDescription);
        progress=findViewById(R.id.progressBar2);
        String title=notesData.getStringExtra("title");
        String description=notesData.getStringExtra("description");

        editNoteTitle.setText(title);
        editNoteDescription.setText(description);
        FloatingActionButton fab = findViewById(R.id.saveFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sTitle=editNoteTitle.getText().toString();
                String sDescription=editNoteDescription.getText().toString();
                if (sDescription.isEmpty()){
                    Toast.makeText(EditNote.this, "No se puede guardar sin una descripción", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sTitle.isEmpty()){
                    sTitle="(Sin título)";
                }
                progress.setVisibility(View.VISIBLE);
                DocumentReference documRef= database.collection("notes").document(notesData.getStringExtra("noteId"));

                Map<String,Object> note=new HashMap<>();
                note.put("title",sTitle);
                note.put("description",sDescription);
                documRef.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditNote.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditNote.this, "No se ha podido actualizar", Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
    }
}
