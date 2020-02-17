package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.Interface.UploadItemClickListner;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class RecyclerImageUploadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public RoundedImageView roundedImageView;
    public UploadItemClickListner itemClickListner;
    public ImageButton imageButton_delete_selected;

    public RecyclerImageUploadViewHolder(@NonNull View itemView) {
        super(itemView);
        roundedImageView = itemView.findViewById(R.id.roundedImageView_upload_recycler);
        imageButton_delete_selected = itemView.findViewById(R.id.image_btn_remove_selected_item);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(UploadItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
