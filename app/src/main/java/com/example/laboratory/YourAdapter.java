package com.example.laboratory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class YourAdapter  extends RecyclerView.Adapter<YourAdapter.YourViewHolder>{
    ArrayList<String> arrayList;
    public YourAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public YourAdapter.YourViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new YourViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_your,null));
    }

    @Override
    public void onBindViewHolder(@NonNull YourViewHolder holder, int position) {
        ((TextView) (holder.itemView.findViewById(R.id.ttt))).setText("item:" + arrayList.get(position));
        Log.e("rrr", "onBindViewHolderonBindViewHolderonBindViewHolder:-holder.getAdapterPosition():holder.itemView.findViewById(R.id.ttt))).getText()" + holder.getAdapterPosition() + "-holder.itemView.findViewById(R.id.ttt))).getText():" + ((TextView) (holder.itemView.findViewById(R.id.ttt))).getText().toString());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull YourViewHolder holder) {
        Log.e("rrr", "onViewAttachedToWindow:-holder.getAdapterPosition():holder.itemView.findViewById(R.id.ttt))).getText()" + holder.getAdapterPosition() + "-holder.itemView.findViewById(R.id.ttt))).getText():" + ((TextView) (holder.itemView.findViewById(R.id.ttt))).getText().toString());
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull YourViewHolder holder) {
        Log.e("rrr", "onViewDetachedFromWindow:-holder.getAdapterPosition():holder.itemView.findViewById(R.id.ttt))).getText()" + holder.getAdapterPosition() + "-holder.itemView.findViewById(R.id.ttt))).getText():" + ((TextView) (holder.itemView.findViewById(R.id.ttt))).getText().toString());
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(@NonNull YourViewHolder holder) {

        Log.e("rrr", "onViewRecycled:-holder.getAdapterPosition():holder.itemView.findViewById(R.id.ttt))).getText()" + holder.getAdapterPosition() + "-holder.itemView.findViewById(R.id.ttt))).getText():" + ((TextView) (holder.itemView.findViewById(R.id.ttt))).getText().toString());

        super.onViewRecycled(holder);
    }

    class YourViewHolder extends  RecyclerView.ViewHolder{

        public YourViewHolder(View itemView) {
            super(itemView);
        }
    }
}
