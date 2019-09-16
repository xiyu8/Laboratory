package com.example.recyclerviewpro.z.tools;

import android.content.Context;

import com.example.recyclerviewpro.z.dataentity.Entity;
import com.example.recyclerviewpro.z.dataentity.Type1entity;
import com.example.recyclerviewpro.z.dataentity.Type2entity;
import com.example.recyclerviewpro.z.dataentity.Type3entity;
import com.example.recyclerviewpro.z.dataentity.Type4entity;

import java.util.ArrayList;

public class ItemToEntity {
    public ItemToEntity() {
    }

    public ArrayList<Type1entity> getType1entities(Context context) {
        ArrayList<Type1entity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Type1entity type1entity = generateType1entity(context,i);
            entities.add(type1entity);
        }
        return new ArrayList<>();
    }

    public Type1entity generateType1entity(Context context,int i){
        Type1entity type1entity = new Type1entity(context);
        Type1entity.DataItem dataItem= new Type1entity.DataItem();
        dataItem.editText = "editText:" + i;
        dataItem.isCheck = i % 2 == 1;
        dataItem.checkText = "checkText:" + i;
        dataItem.editTextHint = "editTextHint:" + i;

        type1entity.setItem(dataItem);
        return type1entity;
    }

////////////////////////////////////////////////////////////////////////
    public ArrayList<Entity> getType2entities(Context context) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if(i==1){

            }else
            if (i > 2 && i < 6) {
                Type4entity type4entity = generateType4entity(context, i);
                entities.add(type4entity);
            }else {
                Type2entity type2entity = generateType2entity(context, i);
                entities.add(type2entity);
            }
        }
        return entities;
    }

    public Type2entity generateType2entity(Context context,int i){
        Type2entity type2entity = new Type2entity(context);
        Type2entity.DataItem dataItem= new Type2entity.DataItem();
//        dataItem.leftDrawable = ;
        dataItem.titleText = "titleText:"+i;
        dataItem.entities = getType1entities2(context);

        type2entity.setItem(dataItem);
        return type2entity;
    }

    public ArrayList<Entity> getType1entities2(Context context) {
        ArrayList<Entity> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                Type1entity type1entity = generateType1entity(context,i);
                entities.add(type1entity);
            }else {
                Type3entity type3entity = generateType3entity(context,i);
                entities.add(type3entity);
            }
        }
        return entities;
    }

    public Type3entity generateType3entity(Context context,int i){
        Type3entity type3entity = new Type3entity(context);
        Type3entity.DataItem dataItem= new Type3entity.DataItem();
        dataItem.isCheck = i % 2 == 1;
        dataItem.checkText = "checkText:" + i;

        type3entity.setItem(dataItem);
        return type3entity;
    }
    public Type4entity generateType4entity(Context context, int i){
        i=i-3;
        Type4entity type4entity = new Type4entity(context);
        Type4entity.DataItem dataItem= new Type4entity.DataItem();
//        dataItem.leftDrawable = ;
        dataItem.checkBoxText = "Type4entity:" + i;
        dataItem.titleIsChecked = !(i%2==1);
        dataItem.entities = getType1entities2(context);

        type4entity.setItem(dataItem);
        return type4entity;
    }


}
