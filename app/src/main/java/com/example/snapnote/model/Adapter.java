package com.example.snapnote.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snapnote.NoteDetails;
import com.example.snapnote.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    List<String> titles;
    List<String> description;

    public Adapter(List<String> titles,List<String> description){
        this.titles=titles;
        this.description=description;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView noteTitle;
        TextView noteDescription;
        View view;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle=itemView.findViewById(R.id.titles);
            noteDescription=itemView.findViewById(R.id.description);
            cardView=itemView.findViewById(R.id.cardNote);
            view=itemView;
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {
        holder.noteTitle.setText(titles.get(pos));
        holder.noteDescription.setText(description.get(pos));
        final Integer colorCode=getRandomColor();
        holder.cardView.setCardBackgroundColor(holder.view.getResources().getColor(colorCode,null));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent y=new Intent(v.getContext(), NoteDetails.class);
                y.putExtra("title",titles.get(pos));
                y.putExtra("description",description.get(pos ));
                y.putExtra("colorCode",colorCode);
                v.getContext().startActivity(y);
            }
        });
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
    public int getItemCount() {
        return titles.size();
    }
}
