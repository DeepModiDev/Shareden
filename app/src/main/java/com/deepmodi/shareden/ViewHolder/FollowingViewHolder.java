package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class FollowingViewHolder extends RecyclerView.ViewHolder {
    public TextView id_following_request_username,id_following_request_userlevel;
    public Button id_following_btn_unfollow;
    public RoundedImageView id_following_request_userImage ;

    public FollowingViewHolder(@NonNull View itemView) {
        super(itemView);
        id_following_request_userImage = itemView.findViewById(R.id.id_following_request_userImage);
        id_following_btn_unfollow = itemView.findViewById(R.id.id_following_btn_unfollow);
        id_following_request_username = itemView.findViewById(R.id.id_following_request_username);
        id_following_request_userlevel = itemView.findViewById(R.id.id_following_request_userlevel);
    }
}
