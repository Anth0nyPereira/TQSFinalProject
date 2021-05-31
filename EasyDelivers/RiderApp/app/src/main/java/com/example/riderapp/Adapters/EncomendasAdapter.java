package com.example.riderapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.riderapp.Classes.ClickListener;
import com.example.riderapp.Classes.Encomenda;
import com.example.riderapp.R;

import java.util.List;

public class EncomendasAdapter extends RecyclerView.Adapter<EncomendasAdapter.MyViewHolder>{
    private List<Encomenda> itemsList;
    private ClickListener clickListener;



    public EncomendasAdapter(List<Encomenda> mItemList){
        this.itemsList = mItemList;
    }

    @Override
    public EncomendasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EncomendasAdapter.MyViewHolder holder, final int position) {
        final Encomenda item = itemsList.get(position);
        holder.name.setText(item.getName());
        holder.start.setText(item.getStart());
        holder.destination.setText(item.getDestination());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v,item,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView name,start,destination;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nomeencomenda);
            start = itemView.findViewById(R.id.start);
            destination = itemView.findViewById(R.id.destination);
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
