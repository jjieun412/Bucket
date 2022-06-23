package com.example.bucket;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class BKDetailTagsAdapter extends RecyclerView.Adapter<BKDetailTagsAdapter.ViewHolder> {

    private ArrayList<BKDetailTagsItem> mtags;

    @NonNull
    @Override
    public BKDetailTagsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.bkdetail_tags, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BKDetailTagsAdapter.ViewHolder holder, int position) {
        //BKDetailTagsItem contact = mtags.get(position);

        //TextView textView = holder.tag;
        //textView.setText(String.valueOf(contact.getTag()));

        holder.onBind(mtags.get(position));
    }

/*
    public BKDetailTagsAdapter(ArrayList<BKDetailTagsItem> list) {
        mtags = list;
    }
 */

    public void setTagList(ArrayList<BKDetailTagsItem> list) {
        this.mtags = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mtags.size();
    }

    void addItem(BKDetailTagsItem data) {
        mtags.add(data);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tag = (TextView) itemView.findViewById(R.id.mbklist_tag);
        }

        void onBind(BKDetailTagsItem item) {
            System.out.println("BKDetailTags: " + item);
            tag.setText(String.valueOf(item.getTag()));
        }

    }
}
