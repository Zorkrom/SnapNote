package com.example.snapnote.notePackage;

import android.content.Intent;
import android.os.Bundle;

import com.example.snapnote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class NoteDetails extends AppCompatActivity {
        Intent notesData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notesData=getIntent();
        TextView title=findViewById(R.id.noteTitle);
        TextView description=findViewById(R.id.noteDescription);
        description.setMovementMethod(new ScrollingMovementMethod());
        title.setText(notesData.getStringExtra("title"));
        description.setText(notesData.getStringExtra("description"));
        description.setBackgroundColor(getResources().getColor(notesData.getIntExtra("colorCode",0)));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent x=new Intent(view.getContext(), EditNote.class);
                x.putExtra("title",notesData.getStringExtra("title"));
                x.putExtra("description",notesData.getStringExtra("description"));
                x.putExtra("noteId", notesData.getStringExtra("noteId"));
                startActivity(x);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
