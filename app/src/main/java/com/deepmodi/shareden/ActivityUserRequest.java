package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deepmodi.shareden.ViewHolder.RequestActivityViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ActivityUserRequest extends AppCompatActivity {

    private FirebaseRecyclerAdapter<ChatRequest, RequestActivityViewHolder> adapter;
    private RecyclerView recyclerView_request;
    protected FirebaseDatabase databaseRequest,databaseAcceptRequest,databaseFollower,databaseFollowing;
    private DatabaseReference referenceRequest,referenceAcceptRequest,referenceFollower,referencefollowing;
    private ChatRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);

        request = new ChatRequest();

        //firebase and paper init
        Paper.init(this);
        databaseRequest = FirebaseDatabase.getInstance();
        referenceRequest = databaseRequest.getReference("UserRequests");

        databaseAcceptRequest = FirebaseDatabase.getInstance();
        referenceAcceptRequest = databaseAcceptRequest.getReference("UserRequests");

        databaseFollower = FirebaseDatabase.getInstance();
        referenceFollower  = databaseFollower.getReference("Followers");

        databaseFollowing = FirebaseDatabase.getInstance();
        referencefollowing = databaseFollowing.getReference("Following");

        recyclerView_request = findViewById(R.id.request_recyclerView);
        recyclerView_request.setHasFixedSize(true);

        /**
         * Load the Requests.
         */
        loadData();
    }

    private void loadData()
    {
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                .setQuery(referenceRequest.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("MyRequests"),ChatRequest.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatRequest, RequestActivityViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestActivityViewHolder holder, int position, @NonNull final ChatRequest model) {
                Picasso.get().load(model.getSenderImage()).into(holder.id_requested_user_image);
                holder.id_requested_user_name.setText(model.getSenderName());
                holder.id_requested_user_level.setText(model.getSenderLevel());
                holder.id_requested_user_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        referenceAcceptRequest.child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    request = dataSnapshot.getValue(ChatRequest.class);
                                    assert request != null;
                                    Log.d("ActivityUserRequest", String.valueOf(request.getReceiverNumber()));
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("ActivityUserRequest", String.valueOf(request.getSenderNumber()));
                                request.setSenderRequestStatus("true");
                                referenceAcceptRequest.child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(request);
                                referenceFollower.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(model.getSenderNumber()).setValue(request);
                                referencefollowing.child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(request);
                                referenceRequest.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child("MyRequests").child(model.getSenderNumber()).removeValue();
                            }
                        },2000);
                    }
                });
            }

            @NonNull
            @Override
            public RequestActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ActivityUserRequest.this).inflate(R.layout.request_item_view_holder,parent,false);
                return new RequestActivityViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView_request.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DatabaseReference.goOffline();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference.goOnline();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DatabaseReference.goOffline();
        adapter.stopListening();
    }
}
