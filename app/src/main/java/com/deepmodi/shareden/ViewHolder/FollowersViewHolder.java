package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;


public class FollowersViewHolder extends RecyclerView.ViewHolder {

    public RoundedImageView followerImage;
    public TextView followerName;
    public TextView followerLevel;
    public TextView btn_unfollow;

    public FollowersViewHolder(@NonNull View itemView) {
        super(itemView);
        followerImage = itemView.findViewById(R.id.id_request_userImage);
        followerName = itemView.findViewById(R.id.id_request_username);
        followerLevel = itemView.findViewById(R.id.id_request_userlevel);
        btn_unfollow = itemView.findViewById(R.id.id_request_btn_accept);
        btn_unfollow.setText("Remove");
    }
}
