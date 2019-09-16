package com.example.recyclerviewpro.z.dataentity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.recyclerviewpro.R;

public class Type1entity extends Entity implements CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {
    public Type1entity(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.z_type1entity_layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //准备View 和 data
        CheckBox check_box = holder.itemView.findViewById(R.id.type1_check_box);
        TextView type1_edit_text = holder.itemView.findViewById(R.id.type1_edit_text);
        DataItem dataItem = getItem(DataItem.class);

        //绑定View&data
        check_box.setChecked(dataItem.isCheck);
        check_box.setText(dataItem.checkText);
        type1_edit_text.setHint(dataItem.editTextHint);
        type1_edit_text.setText(dataItem.editText);

        //View的事件（事件处理包括两部分：View的 data的）
        check_box.setOnCheckedChangeListener(this);
        type1_edit_text.setOnEditorActionListener(this);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // 事件处理包括两部分：data的 View的
        DataItem dataItem = getItem(DataItem.class);
        dataItem.isCheck = isChecked;


//        CheckBox check_box = holder.itemView.findViewById(R.id.type1_check_box);
//        TextView type1_edit_text = holder.itemView.findViewById(R.id.type1_edit_text);
//        if(isChecked)
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // 事件处理包括两部分：View的 data的
        DataItem dataItem = getItem(DataItem.class);
//        dataItem.editText = type1_edit_text.getText();

        return false;
    }

    public static class DataItem extends Entity.DataItem{
        public boolean isCheck;
        public String checkText;
        public String editTextHint;
        public String editText;
    }
}
