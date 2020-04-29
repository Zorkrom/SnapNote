package com.example.snapnote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.snapnote.authentication.RegisterActivity;
import com.example.snapnote.model.Note;
import com.example.snapnote.notePackage.CreateNote;
import com.example.snapnote.notePackage.EditNote;
import com.example.snapnote.notePackage.NoteDetails;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView nav_view;
    RecyclerView list;
    FirebaseFirestore database;
    FirestoreRecyclerAdapter<Note,NoteHolder> noteAdapter;
    FirebaseUser user;
    FirebaseAuth authentication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        database=FirebaseFirestore.getInstance();
        authentication=FirebaseAuth.getInstance();
        user=authentication.getCurrentUser();
        Query query=database.collection("notes").orderBy("title", com.google.firebase.firestore.Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> noteList=new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        noteAdapter=new FirestoreRecyclerAdapter<Note, NoteHolder>(noteList) {
            @Override
            protected void onBindViewHolder(@NonNull NoteHolder noteHolder, final int i, @NonNull final Note note) {
                noteHolder.noteTitle.setText(note.getTitle());
                noteHolder.noteDescription.setText(note.getDescription());
                final Integer colorCode=getRandomColor();
                noteHolder.cardView.setCardBackgroundColor(noteHolder.view.getResources().getColor(colorCode,null));
                final String docId=noteAdapter.getSnapshots().getSnapshot(i).getId();
                noteHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent y=new Intent(v.getContext(), NoteDetails.class);
                        y.putExtra("title",note.getTitle());
                        y.putExtra("description",note.getDescription());
                        y.putExtra("colorCode",colorCode);
                        y.putExtra("noteId",docId);
                        v.getContext().startActivity(y);
                    }
                });
                ImageView menuIcon=noteHolder.view.findViewById(R.id.menuIcon);
                menuIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final String docId=noteAdapter.getSnapshots().getSnapshot(i).getId();
                        PopupMenu menuPop=new PopupMenu(v.getContext(),v);
                        menuPop.setGravity(Gravity.END);
                        menuPop.getMenu().add("Editar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent x=new Intent(v.getContext(), EditNote.class);
                                x.putExtra("title",note.getTitle());
                                x.putExtra("description",note.getDescription());
                                x.putExtra("noteId",docId );
                                startActivity(x);
                                return false;
                            }
                        });
                        menuPop.getMenu().add("Eliminar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference documRef= database.collection("notes").document(docId);
                                documRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Ha sido borrado exitosamente", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "No se ha podido borrar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });
                        menuPop.show();
                    }
                });
            }

            @NonNull
            @Override
            public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout,parent,false);
                return new NoteHolder(view);
            }
        };

        list=findViewById(R.id.list);
        drawerLayout=findViewById(R.id.drawer);
        nav_view=findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        list.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(noteAdapter);

        FloatingActionButton fab=findViewById(R.id.createNoteFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), CreateNote.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.createNote:
                Intent p=new Intent(this,CreateNote.class);
                startActivity(p);
                break;
            case R.id.logout:
                checkUser();
                break;
            default:
                Toast.makeText(this, "Está en desarrollo", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void checkUser() {
        //comprobar si el usuario es real o no
        if(user.isAnonymous()){
            displayAlert();
        }else{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Splash.class));
            finish();
        }
    }

    private void displayAlert() {
        AlertDialog.Builder warning=new AlertDialog.Builder(this).setTitle("¿Estas seguro?")
                .setMessage("Has iniciado sesión con una cuenta temporal, si cierras sesión se perderan todas las notas y si te registras se guardaran en tu cuenta.")
                .setPositiveButton("Crear cuenta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                        finish();
                    }
                }).setNegativeButton("Cerrar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //borrar notas creadas por la cuenta temporal

                        //borrar usuario anonimo
                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),Splash.class));
                                Toast.makeText(MainActivity.this, "Se ha borrado el usuario y sus notas", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
        warning.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflador=getMenuInflater();
        inflador.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.settings){
            Toast.makeText(this, "Has seleccionado Opciones", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    public class NoteHolder extends RecyclerView.ViewHolder{
        TextView noteTitle;
        TextView noteDescription;
        View view;
        CardView cardView;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle=itemView.findViewById(R.id.titles);
            noteDescription=itemView.findViewById(R.id.description);
            cardView=itemView.findViewById(R.id.cardNote);
            view=itemView;
        }
    }
    private int getRandomColor() {
        List<Integer> colorList=new ArrayList<>();
        colorList.add(R.color.blue);
        colorList.add(R.color.yellow);
        colorList.add(R.color.skyblue);
        colorList.add(R.color.lightPurple);
        colorList.add(R.color.lightGreen);
        colorList.add(R.color.gray);
        colorList.add(R.color.pink);
        colorList.add(R.color.red);
        colorList.add(R.color.greenlight);
        colorList.add(R.color.notgreen);

        Random randomColor = new Random();
        int x=randomColor.nextInt(colorList.size());
        return colorList.get(x);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }
}
