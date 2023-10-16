package com.example.recyclerviewpro.commonrv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import com.example.recyclerviewpro.R;

import java.util.ArrayList;

public class FooterHolder  extends BasicHolder{

    public FooterHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.item_header_container);

    }

    @Override
    protected void bindView() {

    }

    @Override
    public void bindData(BasicHolder.Item item, int position) {
    }

    public void addHeaderViews(ArrayList<View> views) {
        for (View view : views) {
            ((ViewGroup) itemView).addView(view);
        }
    }

}