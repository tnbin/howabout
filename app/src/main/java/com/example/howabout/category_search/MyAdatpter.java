package com.example.howabout.category_search;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howabout.R;

import java.util.ArrayList;

public class MyAdatpter extends RecyclerView.Adapter<MyAdatpter.ViewHolder> {
    Context context;
    ArrayList<Document> items;
    EditText editText;
    RecyclerView recyclerView;


    public void addItem(Document item) {
        items.add(item);
    }


    public void clear() {
        items.clear();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(items.get(position).getId());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public interface OnItemClickListener{
        void onItemClick(View v,int pos);
    }

    private OnItemClickListener Listener=null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.Listener=listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameText;
        TextView addressText;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            placeNameText = itemView.findViewById(R.id.ltem_location_tv_placename);
            addressText = itemView.findViewById(R.id.ltem_location_tv_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    if (pos !=RecyclerView.NO_POSITION){
                        if (Listener!=null){
                            Listener.onItemClick(view,pos);
                        }
                    }
                }
            });
        }
    }

    public MyAdatpter(ArrayList<Document> items, Context context, EditText editText, RecyclerView recyclerView) {
        this.context = context;
        this.items = items;
        this.editText = editText;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_location, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int i) {
        final Document model = items.get(i);
        holder.placeNameText.setText(model.getPlaceName());
        holder.addressText.setText(model.getAddressName());
        holder.placeNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(model.getPlaceName());
                recyclerView.setVisibility(View.GONE);
                BusProvider.getInstance().post(model);

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}