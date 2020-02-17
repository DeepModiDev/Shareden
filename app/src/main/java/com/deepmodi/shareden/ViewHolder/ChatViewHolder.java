package com.deepmodi.shareden.ViewHolder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.FullScreenImageView;
import com.deepmodi.shareden.Interface.RequestItemClickListner;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

import okhttp3.Request;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public RoundedImageView imageChatUser;
    public TextView chatUserName;
    public TextView chatUserLevel;
    public RequestItemClickListner itemClickListner;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        imageChatUser = itemView.findViewById(R.id.id_chat_userImage);
        chatUserName = itemView.findViewById(R.id.id_chat_username);
        chatUserLevel = itemView.findViewById(R.id.id_chat_userlevel);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void ItemClickListner(RequestItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;
    }
}
