package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class StationaryViewHolder extends RecyclerView.ViewHolder {

    RoundedImageView id_user_profile_img_stationary;
    TextView id_user_full_name_stationary,id_post_time_stationary,id_stationnary_textview,id_user_level;
    RecyclerView post_stationary_recyclerView;

    public StationaryViewHolder(@NonNull View itemView) {
        super(itemView);
        id_post_time_stationary = itemView.findViewById(R.id.id_post_time_stationary);
        id_user_full_name_stationary = itemView.findViewById(R.id.id_user_full_name_stationary);
        id_stationnary_textview = itemView.findViewById(R.id.id_stationnary_textview);
        id_user_level = itemView.findViewById(R.id.id_user_level);
        post_stationary_recyclerView = itemView.findViewById(R.id.post_stationary_recyclerView);
        id_user_profile_img_stationary = itemView.findViewById(R.id.roundedImageView_upload_recycler);
    }
}
