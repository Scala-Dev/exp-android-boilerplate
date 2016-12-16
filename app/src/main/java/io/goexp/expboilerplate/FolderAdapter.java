package io.goexp.expboilerplate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scala.exp.android.sdk.model.Content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cesar Oyarzun on 11/8/16.
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private ArrayList<Content> folderList;
    private Context context;

    public FolderAdapter(Context context,ArrayList<Content> folderList) {
        this.folderList = folderList;
        this.context = context;
    }

    @Override
    public FolderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_item_layout, viewGroup, false);
        return new FolderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderAdapter.ViewHolder viewHolder, int position) {
        Content item = (Content) folderList.get(position);
        viewHolder.text.setText(item.getString("path"));
    }

    @Override
    public int getItemCount() {
        return this.folderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView text;
        public ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Content content = folderList.get(position);
                Intent detailsIntent = new Intent(context, FolderActivity.class);
                detailsIntent.putExtra("content", content.getUuid());
                context.startActivity(detailsIntent);
            }
        }
    }
}
