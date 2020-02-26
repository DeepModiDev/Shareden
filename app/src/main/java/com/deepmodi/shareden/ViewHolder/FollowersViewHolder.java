package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;


public class FollowersViewHolder extends RecyclerView.ViewHolder {

    public RoundedImageView id_follower_userImage;
    public TextView id_follower_username;
    public TextView id_follower_userlevel;
    public TextView id_follower_btn_remove;

    public FollowersViewHolder(@NonNull View itemView) {
        super(itemView);
        id_follower_userImage = itemView.findViewById(R.id.id_follower_userImage);
        id_follower_username = itemView.findViewById(R.id.id_follower_username);
        id_follower_userlevel = itemView.findViewById(R.id.id_follower_userlevel);
        id_follower_btn_remove = itemView.findViewById(R.id.id_follower_btn_remove);
    }
}
