package com.deepmodi.shareden.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ChatLeftRightViewHolder extends RecyclerView.ViewHolder {
    private TextView left_message;
    private TextView right_message;
    public ChatLeftRightViewHolder(@NonNull View itemView) {
        super(itemView);
        left_message = itemView.findViewById(R.id.left_chat_bubble);
        right_message = itemView.findViewById(R.id.right_chat_bubble);
        left_message.setBackgroundResource(R.drawable.balloon_incoming_normal);
        right_message.setBackgroundResource(R.drawable.balloon_outgoing_normal);

    }
}
