package com.example.recyclerviewpro.z.dataentity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewpro.R;
import com.example.recyclerviewpro.b.DataPrototype;

public abstract class Entity {
    DataItem item = null;
    int displayLayout;
    Context context;

    public Entity(Context context) {
        this.context = context;
    }

    public <T extends Entity.DataItem> T getItem(Class<T> clazz){
        try {
            return clazz.cast(item);
        }catch (Exception e){
            if (item == null){
                Log.e("Err:getItem:", "mItem = null , mItem not set !");
                return null;
            }
            if (clazz == null){
                Log.e("Err:getItem:", "clazz = null !");
                return null;
            }
            Log.e("Err:getItem:", "err type clazz for item !");
            return null;
        }
    }

    public void setItem(DataItem item) {
        this.item = item;
    }

    public abstract int getItemViewType(int position);

    public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        displayLayout = viewType;
        return new EntityViewHolder(LayoutInflater.from(context).inflate(R.layout.z_type1entity_layout, parent, false));

    }




    public static class DataItem{

    }
    public class EntityViewHolder extends RecyclerView.ViewHolder{

        public EntityViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
