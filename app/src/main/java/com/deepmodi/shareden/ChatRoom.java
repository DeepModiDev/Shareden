package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.deepmodi.shareden.ViewHolder.ChatLeftRightViewHolder;
import com.deepmodi.shareden.model.UserChatConModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatRoom extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseRecyclerAdapter<UserChatConModel,ChatLeftRightViewHolder> adapter;
    EditText edit_text_enter_message;
    ImageButton btn_send;
    String senderNumber,receiverNumber;
    private UserChatConModel userChatConModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        intent.getStringExtra("senderNumber");
        intent.getStringExtra("receiverNumber");

        //Firebase init
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("UserChats");
        loadChatConversion();

        edit_text_enter_message = findViewById(R.id.edit_text_signup_unique_id);
        btn_send = findViewById(R.id.btn_send_message);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(edit_text_enter_message.getText().toString());
            }
        });

        loadChatConversion();
    }

    private void loadChatConversion()
    {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<UserChatConModel>()
                .setQuery(reference,UserChatConModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<UserChatConModel, ChatLeftRightViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatLeftRightViewHolder holder, int position, @NonNull UserChatConModel model) {

            }

            @NonNull
            @Override
            public ChatLeftRightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view_holder_con, parent, false);
                return new ChatLeftRightViewHolder(view);
            }
        };
    }

    private void sendMessage(String message) {
        //userChatConModel = new UserChatConModel()
        //FirebaseDatabase.getInstance().getReference("UserChat").child(senderNumber).child(receiverNumber).
    }
}
