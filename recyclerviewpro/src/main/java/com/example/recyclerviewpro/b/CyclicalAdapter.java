package com.example.recyclerviewpro.b;

import android.content.Context;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewpro.DataPrototype;

public class CyclicalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  Context context;
  DataPrototype dataPrototype;


  public static final int ONE_ITEM = 1;
  public static final int TWO_ITEM = 2;

  public CyclicalAdapter(Context context, DataPrototype dataPrototype) {
    super();

    this.context = context;
    this.dataPrototype = dataPrototype;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//    LayoutInflater mInflater = LayoutInflater.from(context);
//    RecyclerView.ViewHolder holder;
//    View view = mInflater.inflate(viewType, parent, false);
//    holder = new OneViewHolder(view);
    return dataPrototype.onCreateViewHolder(parent, viewType);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//    if(holder instanceof OneViewHolder){
//      ((OneViewHolder) holder).tv.setText(mDatas.get(position));
//    }else {
//      ((TwoViewHolder) holder).tv1.setText(mDatas.get(position));
//      ((TwoViewHolder) holder).tv2.setText(mDatas.get(position));
//    }
    dataPrototype.onBindViewHolder(holder,position);
  }

  @Override
  public int getItemViewType(int position) {
//    if(position % 3 == 0){
//      return TWO_ITEM;
//    }else{
//      return ONE_ITEM;
//    }
    return dataPrototype.getItemViewType(position);
  }

  @Override
  public int getItemCount() {
//    return mDatas.size();
    return dataPrototype.getItemCount();
  }

//  class OneViewHolder extends RecyclerView.ViewHolder{
//    TextView tv;
//    public OneViewHolder(View itemView) {
//      super(itemView);
//      tv = (TextView) itemView.findViewById(R.id.adapter_linear_text);
//    }
//  }
//
//  class TwoViewHolder extends RecyclerView.ViewHolder{
//    TextView tv1,tv2;
//    public TwoViewHolder(View itemView) {
//      super(itemView);
//      tv1 = (TextView) itemView.findViewById(R.id.adapter_two_1);
//      tv2 = (TextView) itemView.findViewById(R.id.adapter_two_2);
//    }
//  }





}
