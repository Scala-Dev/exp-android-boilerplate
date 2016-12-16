package io.goexp.expboilerplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.scala.exp.android.sdk.model.Content;

import java.util.ArrayList;

/**
 * Created by Cesar Oyarzun on 11/8/16.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>  {

    private ArrayList<Content> contentList;
    private Context context;
    private AdapterView.OnItemClickListener mListener;


    public ImageAdapter(Context context, ArrayList<Content> contentList) {
        this.contentList = contentList;
        this.context = context;
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder, int position) {

        Content item = (Content) contentList.get(position);
            try {
                String variantUrl = item.getVariantUrl("320.png");
                if(variantUrl!=null){
                    if (viewHolder.image != null) {
                        new ImageDownloaderTask(viewHolder.image).execute(variantUrl);
                    }
                }else{
                    viewHolder.image.setImageResource(R.drawable.ic_insert_drive_file);
                }
            }catch (IllegalStateException e){
                e.printStackTrace();
                viewHolder.image.setImageResource(R.drawable.ic_insert_drive_file);
            }catch (IllegalArgumentException ex){
                ex.printStackTrace();
                viewHolder.image.setImageResource(R.drawable.ic_insert_drive_file);
            }
    }

    @Override
    public int getItemCount() {
        return this.contentList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView image;
        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Content content = contentList.get(position);
                Intent detailsIntent = new Intent(context, DetaiImageActivity.class);
                detailsIntent.putExtra("content",content.getVariantUrl("1080.png"));
                detailsIntent.putExtra("uuid", content.getUuid());
                context.startActivity(detailsIntent);
            }
        }
    }

}