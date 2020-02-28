package com.deepmodi.shareden.Adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deepmodi.shareden.R;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.Messages;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.paperdb.Paper;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private List<Messages> userMessagesList;
    private DatabaseReference UsersDatabaseRef;

    public MessagesAdapter(List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view_holder_con, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String currentSenderId = Paper.book().read(Common.USER_FINAL_NUMBER);
        Messages messages = userMessagesList.get(position);
        String fromMessageType = messages.getType();
        String fromUserId = messages.getFrom();
        //UsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentSenderId);

        if(fromMessageType.equals("text"))
        {
            holder.receiverMessage.setVisibility(View.GONE);
            holder.senderMessage.setVisibility(View.VISIBLE);

            if(fromUserId.equals(currentSenderId))
            {
                holder.senderMessage.setBackgroundResource(R.drawable.left_bubble);
                holder.senderMessage.setGravity(Gravity.LEFT);
                holder.senderMessage.setText(messages.getMessage());
            }
            else
                {
                holder.senderMessage.setVisibility(View.GONE);
                holder.receiverMessage.setVisibility(View.VISIBLE);
                holder.receiverMessage.setBackgroundResource(R.drawable.right_bubble);
                holder.receiverMessage.setGravity(Gravity.LEFT);
                holder.receiverMessage.setText(messages.getMessage());
            }
        }

    }

    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView senderMessage,receiverMessage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.right_chat_bubble);
            receiverMessage = itemView.findViewById(R.id.left_chat_bubble);
        }
    }
}
