package com.example.recyclerviewpro.z.dataentity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Type2entityAdapter  extends RecyclerView.Adapter<Type2entityAdapter.MyViewHolder> {
    Context context;
    ArrayList<Entity> entities;

    public Type2entityAdapter(Context context, ArrayList<Entity> entities) {
        this.context = context;
        this.entities = entities;
    }

    @NonNull
    @Override
    public Type2entityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

//        View v = mInflater.inflate(R.layout.item_linear,parent,false);
//        holder = new OneViewHolder(v);
        return new Type2entityAdapter.MyViewHolder(LayoutInflater.from(context).inflate(viewType, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Type2entityAdapter.MyViewHolder viewHolder, int positon) {
        entities.get(positon).onBindViewHolder(viewHolder,positon);
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    @Override
    public int getItemViewType(int position) {
       return entities.get(position).getItemViewType(position);
    }


    public class MyViewHolder  extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
