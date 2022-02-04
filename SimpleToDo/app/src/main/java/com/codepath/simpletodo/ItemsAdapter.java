package com.codepath.simpletodo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


//Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private final OnClickListener clickListener;

    public  interface OnClickListener{
        void onItemClicked(int position);
    }
    public interface OnLongClickListener{

        void onItemLongClicked(int position);
    }
    List<String>items;
    OnLongClickListener longClickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {

        this.items=items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ItemsAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //use layout inflator to inflate a view
        View todoview = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        // wrap it inside a view Holder and return
        return new ViewHolder(todoview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Grab item at the position

        String item = items.get(position);
        // bind item into the specified view holder
        holder.bind(item);
    }
    //Tells the RV how many item are in the list
    @Override
    public int getItemCount() {
        return  items.size();
    }

    // Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{


        TextView tvItem;
        public ViewHolder(@NonNull View itemView){
            super (itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update the view inside of the view holder with this data
        public void bind (String item){
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());

                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //Notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }


}
