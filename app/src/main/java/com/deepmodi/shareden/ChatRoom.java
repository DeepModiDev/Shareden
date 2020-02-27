package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.deepmodi.shareden.Adapter.MessagesAdapter;
import com.deepmodi.shareden.ViewHolder.ChatLeftRightViewHolder;
import com.deepmodi.shareden.model.Messages;
import com.deepmodi.shareden.model.UserChatConModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    private final List<Messages> messagesList = new ArrayList<>();
    EditText edit_text_enter_message;
    ImageButton btn_send;
    String senderNumber,receiverNumber;
    private UserChatConModel userChatConModel;
    private MessagesAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        senderNumber = intent.getStringExtra("senderNumber");
        receiverNumber = intent.getStringExtra("receiverNumber");

        //recyclerView
        adapter = new MessagesAdapter(messagesList);
        recyclerView = findViewById(R.id.load_user_chat);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
        recyclerView.setAdapter(adapter);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("UserChats");

        edit_text_enter_message = findViewById(R.id.edit_text_enter_message);
        btn_send = findViewById(R.id.btn_send_message);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        loadChatConversion();

    }

    private void loadChatConversion() {
       FirebaseDatabase.getInstance().getReference().child("Message").child(senderNumber).child(receiverNumber).addChildEventListener(
               new ChildEventListener() {
                   @Override
                   public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                       if(dataSnapshot.exists())
                       {
                           Messages messages = dataSnapshot.getValue(Messages.class);
                           messagesList.add(messages);
                           adapter.notifyDataSetChanged();
                       }
                   }

                   @Override
                   public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                   }

                   @Override
                   public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                   }

                   @Override
                   public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               }
       );
    }

    private void sendMessage() {
        String  userMessage = edit_text_enter_message.getText().toString();
       if(TextUtils.isEmpty(userMessage))
       {
           edit_text_enter_message.requestFocus();
           edit_text_enter_message.setError("Please enter message...");
       }else{
           String message_sender_ref = "Message/" + senderNumber + "/" + receiverNumber;
           String message_receiver_ref = "Message/" + receiverNumber + "/" + senderNumber;

           DatabaseReference user_message_key_ref = FirebaseDatabase.getInstance().getReference().child("Message").child(senderNumber).child(receiverNumber).push();
           String message_push_id = user_message_key_ref.getKey();

           Calendar calendar = Calendar.getInstance();
           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
           String currentTime = simpleDateFormat.format(calendar.getTime());

           SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
           String currentDate = simpleDateFormat2.format(calendar.getTime());

           Map messageBody = new HashMap();

           messageBody.put("message",userMessage);
           messageBody.put("time",currentTime);
           messageBody.put("date",currentDate);
           messageBody.put("type","text");
           messageBody.put("from",senderNumber);

           Map messageDetails = new HashMap();

           messageDetails.put(message_sender_ref+"/"+message_push_id,messageBody);
           messageDetails.put(message_receiver_ref+"/"+message_push_id,messageBody);

           FirebaseDatabase.getInstance().getReference().updateChildren(messageDetails).addOnCompleteListener(new OnCompleteListener() {
               @Override
               public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        edit_text_enter_message.setText("");
                        Toast.makeText(ChatRoom.this, "Message sends successfully.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        edit_text_enter_message.setText("");
                        Toast.makeText(ChatRoom.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
               }
           });
       }
    }
}
