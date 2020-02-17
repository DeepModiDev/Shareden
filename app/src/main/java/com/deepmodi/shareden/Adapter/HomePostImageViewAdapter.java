package com.deepmodi.shareden.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.FullScreenImageView;
import com.deepmodi.shareden.Interface.UploadItemClickListner;
import com.deepmodi.shareden.R;
import com.deepmodi.shareden.ViewHolder.PostHomeSubRecyclerViewViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePostImageViewAdapter extends RecyclerView.Adapter<PostHomeSubRecyclerViewViewHolder> {

    private Context context;
    private List<String> postURLs = new ArrayList<>();

    public HomePostImageViewAdapter(Context context) {
        this.context = context;
    }

    public void setListImages(List<String> postURLs)
    {
        this.postURLs = postURLs;
    }

    public HomePostImageViewAdapter(Context context, List<String> postURLs) {
        this.context = context;
        this.postURLs = postURLs;
    }

    @NonNull
    @Override
    public PostHomeSubRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_home_images_recyclerview_item_view,parent,false);
        return new PostHomeSubRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHomeSubRecyclerViewViewHolder holder, int position) {
        holder.setItemClickListner(new UploadItemClickListner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(view.getContext(), FullScreenImageView.class);
                intent.putExtra("fullScreenImageUrl",postURLs.get(position));
                context.startActivity(intent);
            }
        });
        Picasso.get().load(postURLs.get(position)).resize(new Point().x,310).centerInside().into(holder.post_home_images_cotainer);
    }

    @Override
    public int getItemCount() {
        try
        {
            return postURLs.size();
        }
        catch (Exception e)
        {

        }
        return 0;
    }
}
