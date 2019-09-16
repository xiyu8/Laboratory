package com.example.recyclerviewpro.z;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recyclerviewpro.R;

import java.util.ArrayList;

public class MyAdapterOne extends RecyclerView.Adapter<MyAdapterOne.MyViewHolder> {
  Context context;
  ArrayList<Item> items;

  public MyAdapterOne(Context context,ArrayList<Item> items) {
    this.context = context;
    this.items = items;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.z_item_layout1,viewGroup,false));
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
    View view = myViewHolder.itemView;
    TextView textView = view.findViewById(R.id.text_z1);
    EditText editText = view.findViewById(R.id.edit_text_z1);
    RecyclerView recyclerView2 = view.findViewById(R.id.recycler_view_z1);

    textView.setText(items.get(position).s1);
    editText.setText(items.get(position).s2);

    MyAdapterOne adapterTwo = new MyAdapterOne(context,items.get(position).subItems);

    if (items.get(position).subItems != null && items.get(position).subItems.size() != 0) {
      recyclerView2.setLayoutManager(new LinearLayoutManager(context));
      recyclerView2.setAdapter(adapterTwo);
    }



  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder{

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }
  public static class Item{
    String s1;
    String s2;
    ArrayList<MyAdapterOne.Item> subItems;
  }

}
