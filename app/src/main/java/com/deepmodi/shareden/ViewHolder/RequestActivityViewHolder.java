package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.Interface.RequestItemClickListner;
import com.deepmodi.shareden.Interface.UploadItemClickListner;
import com.deepmodi.shareden.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class RequestActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public RoundedImageView id_requested_user_image;
    public TextView id_requested_user_name;
    public TextView id_requested_user_level;
    public Button id_requested_user_btn,id_reject_user_btn;
    public RequestItemClickListner requestItemClickListner;

    public RequestActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        id_requested_user_btn = itemView.findViewById(R.id.id_request_btn_accept);
        id_requested_user_image = itemView.findViewById(R.id.id_request_userImage);
        id_requested_user_name = itemView.findViewById(R.id.id_request_username);
        id_requested_user_level = itemView.findViewById(R.id.id_request_userlevel);
        id_reject_user_btn = itemView.findViewById(R.id.id_request_btn_reject);
    }

    @Override
    public void onClick(View v) {
        requestItemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void RequestItemClick(RequestItemClickListner requestItemClickListner)
    {
        this.requestItemClickListner = requestItemClickListner;
    }
}
