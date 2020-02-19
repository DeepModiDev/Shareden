package com.deepmodi.shareden.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.ImageViewActivity;
import com.deepmodi.shareden.Interface.UploadItemClickListner;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.ViewHolder.RecyclerImageUploadViewHolder;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class UploadImageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerImageUploadViewHolder> {

    private List<String> imageUrls;
    private Context context;
    private boolean isEnabled;
    private List<String> finalSendList;
    private List<String> superImageUrls;

    public UploadImageRecyclerViewAdapter() {
    }

    public UploadImageRecyclerViewAdapter(List<String> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    public UploadImageRecyclerViewAdapter(List<String> imageUrls, Context context, boolean isEnabled) {
        this.imageUrls = imageUrls;
        this.context = context;
        this.isEnabled = isEnabled;
    }

    public UploadImageRecyclerViewAdapter(List<String> imageUrls, Context context, boolean isEnabled, List<String> finalSendList, List<String> superImageUrls) {
        this.imageUrls = imageUrls;
        this.context = context;
        this.isEnabled = isEnabled;
        this.finalSendList = finalSendList;
        this.superImageUrls = superImageUrls;
    }


    @NonNull
    @Override
    public RecyclerImageUploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upload_recyclerview_imageview, parent, false);
        return new RecyclerImageUploadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerImageUploadViewHolder holder, final int position) {
        try {
            if (isEnabled) {
                Picasso.get().load(imageUrls.get(position)).error(R.mipmap.ic_launcher).resize(new Point().x, 400).centerInside().placeholder(R.mipmap.ic_launcher).into(holder.roundedImageView);
                holder.imageButton_delete_selected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            finalSendList.remove(imageUrls.get(position).replace("file://","").trim());
                            //Log.d("UploadActivity",imageUrls.get(position).replace("file://",""));
                            superImageUrls.remove(imageUrls.get(position));
                            imageUrls.remove(position);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                    }
                });
                holder.setItemClickListner(new UploadItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("path", imageUrls.get(position));
                        intent.putExtra("enable", isEnabled);
                        context.startActivity(intent);
                    }
                });
            } else {
                Picasso.get().load("file://" + imageUrls.get(position)).error(R.mipmap.ic_launcher).resize(new Point().x, 400).centerInside().placeholder(R.mipmap.ic_launcher).into(holder.roundedImageView);
                holder.setItemClickListner(new UploadItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("path", imageUrls.get(position));
                        intent.putExtra("enable", false);
                        context.startActivity(intent);
                    }
                });
                holder.imageButton_delete_selected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageUrls.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try
        {
            return imageUrls.size();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
}
