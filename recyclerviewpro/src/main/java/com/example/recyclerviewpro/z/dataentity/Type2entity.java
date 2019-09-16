package com.example.recyclerviewpro.z.dataentity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.recyclerviewpro.R;

import java.util.ArrayList;

public class Type2entity extends Entity {


    public Type2entity(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.z_type2entity_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //准备View 和 data
        TextView type2_title = holder.itemView.findViewById(R.id.type2_title);
        RecyclerView type2_recycler_view=holder.itemView.findViewById(R.id.type2_recycler_view);
        Type2entity.DataItem dataItem = getItem(Type2entity.DataItem.class);

        //绑定View&data
//        type2_title.setCompoundDrawables();
        type2_title.setText(dataItem.titleText);
        Type2entityAdapter adapter = new Type2entityAdapter(context,dataItem.entities);
        type2_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        type2_recycler_view.setAdapter(adapter);


        //View的事件（事件处理包括两部分：View的 data的）
//        adapter.setOnItemClickListener();

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent,viewType);
    }




    public static class DataItem extends Entity.DataItem{
//        boolean titleIsCheckable;
//        boolean titleIsChecked;
        public int leftDrawable;
        public String titleText;
        public ArrayList<Entity> entities;
    }

}
