package com.example.recyclerviewpro;

import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class DataPrototype {

  Item item;
  ArrayList<Item> items;

  public void setItem(Item item) {
    this.item = item;
  }
  public <T extends Item> void setItems(ArrayList<T> items) {
    this.items = (ArrayList<Item>) items;
  }

  public <T extends Item> ArrayList<T> getItems(Class<T> clazz) {
    ArrayList<T> newItems = new ArrayList<>();
    for (Item item : items) {
      newItems.add(clazz.cast(item));
    }
    return newItems;
  }
  public <T extends Item> T getItem(Class<T> clazz){
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


  public abstract int getItemViewType(int position);

  public abstract void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

  public abstract int getItemCount();

  public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);







  public class Item{ }
}
