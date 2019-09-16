package com.example.recyclerviewpro.z.dataentity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.recyclerviewpro.R;

import java.util.ArrayList;

public class Type4entity extends Entity {


    public Type4entity(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.z_type4entity_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //准备View 和 data
        CheckBox type4_check_box = holder.itemView.findViewById(R.id.type4_check_box);
        RecyclerView type4_recycler_view = holder.itemView.findViewById(R.id.type4_recycler_view);
        Type4entity.DataItem dataItem = getItem(Type4entity.DataItem.class);

        //绑定View&data
        type4_check_box.setChecked(dataItem.titleIsChecked);
        type4_check_box.setText(dataItem.checkBoxText);
        Type2entityAdapter adapter = new Type2entityAdapter(context, dataItem.entities);
        type4_recycler_view.setLayoutManager(new LinearLayoutManager(context));
        type4_recycler_view.setAdapter(adapter);


        //View的事件（事件处理包括两部分：View的 data的）
//        adapter.setOnItemClickListener();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    public static class DataItem extends Entity.DataItem{
        //        boolean titleIsCheckable;
        public boolean titleIsChecked;
        public String checkBoxText;
        public ArrayList<Entity> entities;
    }

}

