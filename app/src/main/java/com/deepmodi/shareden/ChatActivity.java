package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.deepmodi.shareden.Interface.RequestItemClickListner;
import com.deepmodi.shareden.ViewHolder.ChatViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ChatActivity extends AppCompatActivity {

    FirebaseDatabase databaseChat;
    DatabaseReference referenceChat;
    RecyclerView recyclerViewChat;
    FirebaseRecyclerAdapter<ChatRequest,ChatViewHolder> adapterChat;
    TextView text_view_not_following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerViewChat = findViewById(R.id.chat_recyclerView);
        Paper.init(this);
        databaseChat = FirebaseDatabase.getInstance();
        referenceChat = databaseChat.getReference("Following");
        text_view_not_following = findViewById(R.id.text_view_not_following);
        loadData();
    }

    private void loadData()
    {
        try {

            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                    .setQuery(referenceChat.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()),ChatRequest.class)
                    .build();

            adapterChat = new FirebaseRecyclerAdapter<ChatRequest, ChatViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final ChatRequest model) {
                    text_view_not_following.setVisibility(View.GONE);
                    Picasso.get().load(model.getReceiverImage()).into(holder.imageChatUser);
                    holder.chatUserName.setText(model.getReceiverName());
                    holder.chatUserLevel.setText(model.getReceiverLevel());
                    holder.imageChatUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), FullScreenImageView.class);
                            intent.putExtra("fullScreenImageUrl", model.getReceiverImage());
                            startActivity(intent);
                        }
                    });
                   holder.ItemClickListner(new RequestItemClickListner() {
                       @Override
                       public void onClick(View v, int position, boolean isLongClick) {
                           Toast.makeText(ChatActivity.this, ""+model.getReceiverName(), Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(ChatActivity.this,ChatRoom.class);
                           intent.putExtra("senderNumber",model.getSenderNumber());
                           intent.putExtra("receiverNumber",model.getReceiverNumber());
                           startActivity(intent);
                       }
                   });
                }

                @NonNull
                @Override
                public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_view, parent, false);
                    return new ChatViewHolder(view);
                }
            };
            adapterChat.startListening();
            adapterChat.notifyDataSetChanged();
            recyclerViewChat.setAdapter(adapterChat);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
