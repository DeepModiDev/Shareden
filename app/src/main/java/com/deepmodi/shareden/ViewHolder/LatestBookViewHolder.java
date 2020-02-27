package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.Adapter.HomePostImageViewAdapter;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class LatestBookViewHolder extends RecyclerView.ViewHolder {

   public RoundedImageView profile_img;
   public TextView user_name;
   public TextView user_level;
   public TextView user_book_name,user_book_author,user_book_description,post_time;
   public RecyclerView recyclerView;

    public LatestBookViewHolder(@NonNull final View itemView) {
        super(itemView);
        profile_img = itemView.findViewById(R.id.id_user_profile_img);
        user_name = itemView.findViewById(R.id.id_user_full_name);
        user_book_description = itemView.findViewById(R.id.id_book_description);
        user_book_name = itemView.findViewById(R.id.id_book_name);
        user_book_author = itemView.findViewById(R.id.id_book_author);
        user_level = itemView.findViewById(R.id.id_user_level);
        recyclerView = itemView.findViewById(R.id.post_home_images_recyclerView);
        post_time = itemView.findViewById(R.id.id_post_time);
        recyclerView.setHasFixedSize(true);
    }

}
