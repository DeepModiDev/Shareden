package com.deepmodi.shareden;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.deepmodi.shareden.ViewHolder.FollowersViewHolder;
import com.deepmodi.shareden.common.Common;
import com.deepmodi.shareden.model.ChatRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class ActivityUserFollower extends AppCompatActivity {

    FirebaseRecyclerAdapter<ChatRequest, FollowersViewHolder> adapter;
    protected FirebaseDatabase databaseFollower,databaseFollowing,databaseUserRequest;
    private DatabaseReference referenceFollower,referenceFollowing,referenceUserequest;
    private RecyclerView recyclerViewFollower;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_follower);

        //firebase and paper init
        Paper.init(this);
        databaseFollower = FirebaseDatabase.getInstance();
        referenceFollower = databaseFollower.getReference("Followers");

        databaseFollowing = FirebaseDatabase.getInstance();
        referenceFollowing = databaseFollowing.getReference("Following");

        databaseUserRequest = FirebaseDatabase.getInstance();
        referenceUserequest  = databaseUserRequest.getReference("UserRequests");
        recyclerViewFollower = findViewById(R.id.recyclerView_follower);

        loadData();
    }

    private void loadData()
    {
        FirebaseRecyclerOptions options  = new FirebaseRecyclerOptions.Builder<ChatRequest>()
                .setQuery(referenceFollower.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()),ChatRequest.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatRequest, FollowersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FollowersViewHolder holder, int position, @NonNull final ChatRequest model) {
                holder.followerName.setText(model.getSenderName());
                Picasso.get().load(model.getSenderImage()).into(holder.followerImage);
                holder.followerLevel.setText(model.getSenderLevel());
                holder.btn_unfollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityUserFollower.this);
                        builder.setTitle("Remove follower");
                        builder.setMessage("Do you really wants to remove "+model.getSenderName()+" from your followers?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ActivityUserFollower.this, "Remove"+model.getSenderName(), Toast.LENGTH_SHORT).show();
                                referenceFollower.child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).child(model.getSenderNumber()).removeValue();
                                referenceFollowing.child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).removeValue();
                                model.setSenderRequestStatus("follow");
                                referenceUserequest.child(model.getSenderNumber()).child(Paper.book().read(Common.USER_FINAL_NUMBER).toString()).setValue(model);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ActivityUserFollower.this, "Not remove"+model.getSenderName(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).setCancelable(false).create().show();
                    }
                });
            }

            @NonNull
            @Override
            public FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(ActivityUserFollower.this).inflate(R.layout.request_item_view_holder,parent,false);
                return new FollowersViewHolder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerViewFollower.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        adapter.stopListening();
        DatabaseReference.goOffline();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        DatabaseReference.goOnline();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        DatabaseReference.goOffline();
    }
}
