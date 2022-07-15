package com.example.recyclerviewpro.b;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewpro.CyclicalAdapter;
import com.example.recyclerviewpro.R;

import java.util.ArrayList;

public class SpecificData extends DataPrototype {

  Context context;
  public SpecificData(ArrayList<Item> items,Context context) {
    setItems(items);
    this.context = context;
  }

  @Override
  public int getItemViewType(int position) {
    return R.layout.item_layout3;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    View itemView = holder.itemView;
    TextView textView=itemView.findViewById(R.id.text3);
    EditText edit_text=itemView.findViewById(R.id.edit_text3);

    if (getItems(Item.class).get(position).sss != null && getItems(Item.class).get(position).sss.size() != 0) {
      RecyclerView recyclerView = itemView.findViewById(R.id.recycler_view3);
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
//      recyclerView.setAdapter(new CyclicalAdapter(context,sss));
    }

    textView.setText(getItems(Item.class).get(position).s1);
    edit_text.setText(getItems(Item.class).get(position).s2);

  }

  @Override
  public int getItemCount() {
    return getItems(Item.class).size();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new MyViewHolder(LayoutInflater.from(context).inflate(viewType,null));
  }





  public class Item extends DataPrototype.Item {
    int viewType;

    String s1;
    String s2;
    String s3;
    ArrayList<DataPrototype> sss;


  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }

}
