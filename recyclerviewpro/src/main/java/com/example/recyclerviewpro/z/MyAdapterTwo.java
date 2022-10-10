package com.example.recyclerviewpro.z;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewpro.R;

import java.util.ArrayList;

public class MyAdapterTwo  extends RecyclerView.Adapter<MyAdapterTwo.MyViewHolder> {
  ArrayList<Item> items;
  Context context;

  public MyAdapterTwo(Context context,ArrayList<Item> items) {
    this.items = items;
    this.context = context;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.z_item_layout2,viewGroup,false));
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
    View view = myViewHolder.itemView;
    TextView textView = view.findViewById(R.id.text_z2);
    EditText editText = view.findViewById(R.id.edit_text_z2);
//    RecyclerView recyclerView2 = view.findViewById(R.id.recycler_view_z1);

    textView.setText(items.get(position).s1);
    editText.setText(items.get(position).s2);

//    MyAdapterTwo adapterTwo = new MyAdapterTwo(context,items.get(position).subItems);
//    recyclerView2.setLayoutManager(new LinearLayoutManager(context));
//    recyclerView2.setAdapter(adapterTwo);



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
  }
}
