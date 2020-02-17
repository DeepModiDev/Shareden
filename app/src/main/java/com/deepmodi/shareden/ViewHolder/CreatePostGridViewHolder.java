package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.Interface.CreatePostInterface;
import com.deepmodi.shareden.R;

import java.util.HashMap;

public class CreatePostGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    public ImageView grid_images;
    public TextView textView_selected;
    public CreatePostInterface createPostInterface;

    public CreatePostGridViewHolder(@NonNull View itemView) {
        super(itemView);
        grid_images = itemView.findViewById(R.id.gridImageView);
        textView_selected = itemView.findViewById(R.id.text_selected_image);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        createPostInterface.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View v) {
        return createPostInterface.onLongClick(v,getAdapterPosition(),false);
    }

    public void setCreatePostInterface(CreatePostInterface createPostInterface)
    {
        this.createPostInterface = createPostInterface;
    }


}
