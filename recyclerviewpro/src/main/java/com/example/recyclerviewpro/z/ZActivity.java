package com.example.recyclerviewpro.z;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.recyclerviewpro.R;
import com.example.recyclerviewpro.z.dataentity.Entity;
import com.example.recyclerviewpro.z.dataentity.Type2entityAdapter;
import com.example.recyclerviewpro.z.tools.ItemToEntity;

import java.util.ArrayList;

public class ZActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_z);

        init2();

    }

    public void init2() {
        ArrayList<Entity> entities = new ItemToEntity().getType2entities(this);

        RecyclerView recyclerView1 = findViewById(R.id.recycler_view_z1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        Type2entityAdapter adapter = new Type2entityAdapter(this, entities);
        recyclerView1.setAdapter(adapter);

    }

    public void init1() {
        RecyclerView recyclerView1 = findViewById(R.id.recycler_view_z1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<MyAdapterOne.Item> subItems = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            MyAdapterOne.Item item = new MyAdapterOne.Item();
            item.s1 = i + "-subItems-s1";
            item.s2 = i + "-subItems-s2";
            subItems.add(item);
        }
//    subItems.get(0).subItems = subItems;
        ArrayList<MyAdapterOne.Item> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            MyAdapterOne.Item item = new MyAdapterOne.Item();
            item.s1 = i + "-s1";
            item.s2 = i + "-s2";
            item.subItems = subItems;
            items.add(item);
        }

        MyAdapterOne adapterOne = new MyAdapterOne(this, items);
        recyclerView1.setAdapter(adapterOne);


    }
}
