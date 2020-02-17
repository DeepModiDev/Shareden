package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.Interface.UploadItemClickListner;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class PostHomeSubRecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public RoundedImageView post_home_images_cotainer;
    public UploadItemClickListner itemClickListner;
    public PostHomeSubRecyclerViewViewHolder(@NonNull View itemView) {
        super(itemView);
        post_home_images_cotainer = itemView.findViewById(R.id.post_home_images_cotainer);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListner(UploadItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }
}
