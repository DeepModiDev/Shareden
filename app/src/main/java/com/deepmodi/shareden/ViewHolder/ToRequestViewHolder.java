package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.Interface.RequestItemClickListner;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class ToRequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public RoundedImageView id_to_requested_user_image;
    public TextView id_to_requested_user_name;
    public TextView id_to_requested_user_level;
    public Button id_to_reject_user_btn;
    public RequestItemClickListner requestToItemClickListner;

    public ToRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        id_to_reject_user_btn = itemView.findViewById(R.id.id_to_request_btn_cancel);
        id_to_requested_user_image = itemView.findViewById(R.id.id_to_request_userImage);
        id_to_requested_user_name = itemView.findViewById(R.id.id_to_request_username);
        id_to_requested_user_level = itemView.findViewById(R.id.id_to_request_userlevel);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        requestToItemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void ToRequestItemClick(RequestItemClickListner requestToItemClickListner)
    {
        this.requestToItemClickListner = requestToItemClickListner;
    }
}

